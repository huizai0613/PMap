package xxzx.myBdLocation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import xxzx.Utils.GPSUtil;
import xxzx.publicClass.MyHttpRequst;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.PublicStaticFunUtil;


/**
 * Created by mulin on 2015/10/12.
 */
public class MyGdLocationClass implements AMapLocationListener
{
    private Context mContext;
    //写gps点文件的数据流
    private FileOutputStream gpsFileOutputStream = null;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //private PlaceQueryClass placeQueryClass=null;
    //公共变量
    public LocationSource.OnLocationChangedListener mListener;

    //GPS心跳时间，主要采用原始GPS同步，采用累计次数
    private int GpsBeatTimeCount = 0;
    private int GPSBEATTIME = 20;

    //是否是第一次定位
    private boolean isfirstLocation = true;
    private boolean isMap;

    /**
     * 构造函数
     *
     * @param mContext
     */
    public MyGdLocationClass(Context mContext, boolean isMap)
    {
        this.mContext = mContext;
        //初始化定位
        mLocationClient = new AMapLocationClient(this.mContext);
        // 设置定位监听
        mLocationClient.setLocationListener(this);
        this.isMap = isMap;
    }

    public void start()
    {
        //对象设置定位参数
        mLocationClient.setLocationOption(this.initOption());
        //启动定位
        mLocationClient.startLocation();
    }

//    public void stop() {
//        // 停止定位
//        if(mLocationClient!=null) {
//            mLocationClient.stopLocation();
//        }
//    }

    public void onDestroy()
    {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }

        if (gpsFileOutputStream != null) {
            try {
                gpsFileOutputStream.close();
                gpsFileOutputStream = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mLocationClient = null;
    }


    /**
     * 设置定位参数
     *
     * @return
     */
    private AMapLocationClientOption initOption()
    {

        //初始化定位参数
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(isMap);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        return mLocationOption;
    }

    // 定位监听
    @Override
    public void onLocationChanged(AMapLocation amapLocation)
    {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                Message msg = new Message();
                // 如果经纬度在中国范围内
                if (!PositionUtil.outOfChina(amapLocation.getLatitude(), amapLocation.getLongitude())) {
                    msg.obj = amapLocation;
                    msg.what = GDLocationUtils.MSG_LOCATION_FINISH;
                    locationResult(msg);
                }
                Log.e("AmapErr", amapLocation.getAddress());
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }

    }

    private void locationResult(Message msg)
    {
        switch (msg.what) {
            //开始定位
            case GDLocationUtils.MSG_LOCATION_START:
                //tvReult.setText("正在定位...");
                break;
            // 定位完成
            case GDLocationUtils.MSG_LOCATION_FINISH:
                AMapLocation loc = (AMapLocation) msg.obj;

                MySingleClass mySingleClass = MySingleClass.getInstance();

                //如果是第一次定位成功,并且有map
                if (isfirstLocation && isMap) {
                    AMap amap = mySingleClass.getBaseMapViewClass().getMapView().getMap();
                    amap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 11));
                    isfirstLocation = false;
                }
                if (!isMap) {
                    String result = GDLocationUtils.getLocationStr(loc);

                    //如果当前没有登录，退出以下操作
                    if (mySingleClass.getUser() == null) {
                        return;
                    }

                    if (!mySingleClass.getUser().getLoginSuccess()) {
                        return;
                    }

                    //将定位信息保存
                    mySingleClass.setMyGdLocation(loc);

                    double[] doubles = GPSUtil.gcj02_To_Bd09(loc.getLatitude(), loc.getLongitude());

                    //写GPS文件
                    writeGpsPoints(doubles[1] + "," + doubles[0] + ";");
                    //设置|| GpsBeatTimeCount == 0,一开始就上传心跳数据
                    if (GpsBeatTimeCount == GPSBEATTIME || GpsBeatTimeCount == 0) {
                        GpsBeatTimeCount = 0;//重新计数
                        //将GPS点上传服务器
                        MyRunnable runnable = new MyRunnable();

                        //转换坐标为百度地图
                        runnable.setData(String.valueOf(doubles[1]), String.valueOf(doubles[0]));

                        new Thread(runnable).start();
                    }
                    //GPS计数
                    GpsBeatTimeCount++;
                    Log.i("高德定位", result);
                }
                break;
            //停止定位
            case GDLocationUtils.MSG_LOCATION_STOP:
                //tvReult.setText("定位停止");
                break;
            case 10000:
                Log.e("心跳:", msg.obj.toString());
                break;
            default:
                break;
        }
    }


    /**
     * 写GPS点数据,每次打开GPS记录开关都生成一个GPS点文件
     */
    private void writeGpsPoints(String pointStr)
    {
        try {
            if (gpsFileOutputStream == null) {
                //通过SimpleDateFormat获取24小时制时间
                String name = PublicStaticFunUtil.getCurrentDate() + ".txt";

                String gpsFileName = MyString.gps_folder_path + "/" + name;
                File gpsFile = new File(gpsFileName);
                if (!gpsFile.exists()) {
                    gpsFile.createNewFile();
                }
                gpsFileOutputStream = new FileOutputStream(gpsFile, true);
            }
            gpsFileOutputStream.write(pointStr.getBytes());
            Log.i("写gps路径信息", "写gps路径信息");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * GPS心跳进程
     */
    public class MyRunnable implements Runnable
    {
        private String x;
        private String y;

        public void setData(String x, String y)
        {
            this.x = x;
            this.y = y;
        }

        @Override
        public void run()
        {
            // TODO Auto-generated method stub
            MySingleClass mySingleClass = MySingleClass.getInstance();
            Properties properties = mySingleClass.getProperties();
            try {
                if (mySingleClass.getUser() != null) {
                    String url = properties.get("url_gps_breat2").toString();

                    String poststring = properties.get("url_gps_breat2_poststring").toString();
                    poststring = String.format(poststring, mySingleClass.getUser().getmName(), this.x, this.y);

                    String result = MyHttpRequst.getHttpPostRequst2(url, poststring);

                    Message msg = new Message();
                    msg.what = 10000;
                    msg.obj = url + poststring + ";" + result;
                    locationResult(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 发送Gps广播消息
     *
     * @param myGpsLocation
     */
    private void sendGpsBroadcast(MyGpsLocation myGpsLocation)
    {
        // GPS广播
        Intent intent = new Intent(MyString.ACTION_BROADCAST_INTENT_GPS);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MyString.intent_gps_information, myGpsLocation);
        intent.putExtras(bundle);
        this.mContext.sendBroadcast(intent);
    }


}
