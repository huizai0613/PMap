package xxzx.mapLayerOpt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.TileOverlay;

import java.util.Properties;

import xxzx.activity.R;
import xxzx.library.VectorDrawable;
import xxzx.myOverlay.PowelineOverLayOpt;
import xxzx.myView.ImgButton;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;


/**
 * Created by mulin on 2015/9/16.
 */
public class BaseMapViewClass_old extends RelativeLayout {
    private Context mContext = null;
    private MapView mMapView = null;
    private AMap aMap = null;

    //google图层类
    private TileOverlay googleTileOverlay;
    //电力线图层操作
    private PowelineOverLayOpt powelineOverLayOpt;
    //定位类
//    private MyGdLocationClass myGdLocationClass = null;
    //十字丝覆盖图层
    private RelativeLayout rl_pointCrossOverLay;

    private ImageButton btn_map_zoomout=null;
    private ImageButton btn_map_zoomin=null;

    private ImgButton imgbtn_map_switch=null;
    private ImgButton imgbtn_map_location=null;
    //private ImgButton imgbtn_map_recordgps=null;

    //地图切换对话框
    private AlertDialog.Builder builder;
    //标记是否测量地图控件的宽度
    private boolean hasMeasured = false;
    //地图控件的高和宽
    private int mapHeight=0;
    private int mapWidth=0;

    /**
     * 构造函数
     *
     * @param context
     */
    public BaseMapViewClass_old(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.mContext = context;
    }

    /**
     * 初始化自定义UI界面控件
     */
    public void initView(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_basemap, this);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        rl_pointCrossOverLay= (RelativeLayout) findViewById(R.id.rl_pointCrossOverLay);

        btn_map_zoomout= (ImageButton) findViewById(R.id.btn_map_zoomout);
        btn_map_zoomin= (ImageButton) findViewById(R.id.btn_map_zoomin);
        imgbtn_map_switch= (ImgButton) findViewById(R.id.imgbtn_map_switch);
        imgbtn_map_switch.setImage(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_map_switch));
        imgbtn_map_location= (ImgButton) findViewById(R.id.imgbtn_map_location);
        imgbtn_map_location.setImage(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_map_location));
//        imgbtn_map_recordgps= (ImgButton) findViewById(R.id.imgbtn_map_recordgps);
//        imgbtn_map_recordgps.setImage(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_gpsline_start));


//        btn_map_switch.setOnClickListener(new BtnOnClickListener());
//        btn_map_location.setOnClickListener(new BtnOnClickListener());
        btn_map_zoomout.setOnClickListener(new BtnOnClickListener());
        btn_map_zoomin.setOnClickListener(new BtnOnClickListener());

        imgbtn_map_switch.setOnClick(new ImgBtnOnClickListener());
        imgbtn_map_location.setOnClick(new ImgBtnOnClickListener());
        //imgbtn_map_recordgps.setOnClick(new ImgBtnOnClickListener());


        //获取地图的高和宽
        ViewTreeObserver vto = mMapView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                if (hasMeasured == false) {
                    mapHeight = mMapView.getMeasuredHeight();
                    mapWidth = mMapView.getMeasuredWidth();
                    //获取到宽度和高度后，可用于计算
                    hasMeasured = true;
                }
                return true;
            }
        });
    }

    /**
     * 初始化数据
     */
    public void initData() {
        //初始化地图视图参数
        this.initMap();

        //默认是谷歌混合影像
//        MySingleClass mySingleClass = MySingleClass.getInstance();
//        Properties properties = mySingleClass.getProperties();
//        String url = properties.get("google_img_anno").toString();
//        String savePath = MyString.maps_cache_google_img_anno_folder_path;
//
//        this.AddGoogleTileOverlay(url, savePath);
    }

    /**
     * 初始化地图
     */
    private void initMap(){
        if (aMap == null) {
            //重新设置矢量地图缓存路径
            aMap = mMapView.getMap();
            aMap.setMyLocationStyle(this.getMyLocationStyle());
            aMap.setMyLocationRotateAngle(180);

            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.83, 117.27), 11));
            aMap.setLocationSource((LocationSource) this.mContext);// 设置定位监听
            aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
            aMap.getUiSettings().setTiltGesturesEnabled(false);// 设置地图是否可以倾斜
            aMap.getUiSettings().setRotateGesturesEnabled(false);// 设置地图是否可以旋转
            aMap.getUiSettings().setZoomControlsEnabled(false);//放大按钮是否可见
            // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            aMap.setMyLocationEnabled(true);
            // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
            //aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);

            //地图范围改变后回调
            aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {

                }

                @Override
                public void onCameraChangeFinish(CameraPosition cameraPosition) {
                    MySingleClass mySingleClass = MySingleClass.getInstance();
                    mySingleClass.setMainMapCameraPosition(cameraPosition);

                    Projection projection = aMap.getProjection();
                    //将地图的中心点，转换为屏幕上的点 
                    Point center = projection.toScreenLocation(cameraPosition.target);

                    //获取距离中心点为pixel像素的左、右两点（屏幕上的点 
                    Point right = new Point(center.x + mapWidth / 2, center.y);
                    Point left = new Point(center.x - mapWidth / 2, center.y);
                    Point bottom = new Point(center.x, center.y - mapHeight / 2);
                    Point top = new Point(center.x, center.y + mapHeight / 2);

                    //注意屏幕坐标和地图平面坐标起点方向不同
                    Point min = new Point(center.x - mapWidth / 2, center.y + mapHeight / 2);
                    Point max = new Point(center.x + mapWidth / 2, center.y - mapHeight / 2);


                    //将屏幕上的点转换为地图上的点 
//                    LatLng rightLatlng = projection.fromScreenLocation(right);
//                    LatLng leftLatlng = projection.fromScreenLocation(left);
//
//                    LatLng bottomLatlng = projection.fromScreenLocation(bottom);
//                    LatLng topLatlng = projection.fromScreenLocation(top);

                    LatLng minLatLng = projection.fromScreenLocation(min);
                    LatLng maxLatLng = projection.fromScreenLocation(max);

                    Log.e("屏幕范围：", minLatLng.longitude + "," + minLatLng.latitude + ";" + maxLatLng.longitude + "," + maxLatLng.latitude + ";");

                    //加载线路图形
                    if (powelineOverLayOpt == null) {
                        powelineOverLayOpt = new PowelineOverLayOpt(aMap);
                    }
                    powelineOverLayOpt.updatePoleChnOverlay(cameraPosition.zoom, minLatLng, maxLatLng);
                }
            });

            //地图加载完成监听
            aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {
                    //缩放级别
                    //aMap.moveCamera(CameraUpdateFactory.zoomTo(11));
                    //默认位置
                    //aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.83, 117.27), 11));
                    //加载电力线
                    if (powelineOverLayOpt == null) {
                        powelineOverLayOpt = new PowelineOverLayOpt(aMap);
                    }
                    //加载电力线
                    powelineOverLayOpt.addPlineOverlay();
                }
            });
        }
    }

    private MyLocationStyle getMyLocationStyle(){
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_location_icon));
        myLocationStyle.strokeColor(Color.argb(255, 1, 144, 251));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(50, 1, 144, 251));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(0);// 设置圆形的边框粗细

        return myLocationStyle;
    }


    /**
     * 短按监听器
     * (non-Javadoc)
     */
    private class BtnOnClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()){
                case R.id.btn_map_zoomin:
                    mapZoomIn();
                    break;
                case R.id.btn_map_zoomout:
                    mapZoomOut();
                    break;
                default:
                    break;
            }
        }
    }

    private class ImgBtnOnClickListener implements ImgButton.ICoallBack{
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()){
                case R.id.imgbtn_map_switch:
                    mapSwitch();
                    break;
                case R.id.imgbtn_map_location:
                    mapLocation();
                    break;
//                case R.id.imgbtn_map_recordgps:
//                    MySingleClass mySingleClass = MySingleClass.getInstance();
//                    Boolean b=mySingleClass.getIsRecordGPS();
//                    //设置是否记录
//                    mySingleClass.setIsRecordGPS(!b);
//                    //设置当前图标
//                    if(mySingleClass.getIsRecordGPS()){
//                        ((ImgButton)v).setImage(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_gpsline_stop));
//                    }else{
//                        ((ImgButton)v).setImage(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_gpsline_start));
//                    }
//                    break;
//                case R.id.btn_map_zoomout:
//                    mapZoomOut();
//                    break;
//                default:
//                    break;
            }
        }
    }


    /**
     * 加载谷歌地图
     */
    private void AddGoogleTileOverlay(String url,String savePath) {
        //获得地图地址
        //首先移除该图层
        if (googleTileOverlay != null) {
            googleTileOverlay.remove();
        }
        //创建该图层
        googleTileOverlay = GoogleTileOverlay.addToMap(aMap, url, savePath);
        googleTileOverlay.setZIndex(-1);
        Log.d("图层z", String.valueOf(googleTileOverlay.getZIndex()));
    }



    /**
     * 地图矢量和影像切换
     */
    private void mapSwitch() {

        if(builder!=null){
            builder.show();
            return;
        }

        final String items[] = {"高德矢量图", "谷歌卫星矢量混合图"};
        //dialog参数设置
        builder = new AlertDialog.Builder(mContext);  //先得到构造器
        builder.setTitle("选择图层"); //设置标题
        //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MySingleClass mySingleClass = MySingleClass.getInstance();
                Properties properties = mySingleClass.getProperties();
                String url = "";
                String savePath = "";
                //修改按钮点线面的状态
                switch (which) {
                    case 0:
                        if (googleTileOverlay != null) {
                            googleTileOverlay.remove();
                            googleTileOverlay = null;
                        }
                        break;
                    case 1:
                        url = properties.get("zhiji_img_anno").toString();
                        savePath = MyString.maps_cache_google_img_folder_path;
                        AddGoogleTileOverlay(url, savePath);
                        break;
//                    case 2:
//                        url = properties.get("google_img_anno").toString();
//                        savePath = MyString.maps_cache_google_img_anno_folder_path;
//                        AddGoogleTileOverlay(url, savePath);
//                        break;
                }
            }
        });

        builder.create().show();
    }

    /**
     * 定位到当前定位点
     */
    private void mapLocation() {
        Location location = aMap.getMyLocation();
        if (location != null) {
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
        }else{
            Toast.makeText(mContext,"无法获得当前定位信息！",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 放大地图
     */
    private void mapZoomIn(){
        this.aMap.moveCamera(CameraUpdateFactory.zoomIn());
    }

    /**
     * 缩小地图
     */
    private void mapZoomOut(){
        this.aMap.moveCamera(CameraUpdateFactory. zoomOut());
    }


// ----------public method----------------------------


    /**
     * 获得当前的地图控件
     *
     * @return
     */
    public MapView getMapView() {
        return mMapView;
    }

    public RelativeLayout getRl_pointCrossOverLay() {
        return rl_pointCrossOverLay;
    }

    public PowelineOverLayOpt getPowelineOverLayOpt() {
        return powelineOverLayOpt;
    }

    /**
     * 方法必须重写
     */
    public void onResume() {
        mMapView.onResume();
    }
    /**
     * 方法必须重写
     */
    public void onPause() {
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    public void onSaveInstanceState(Bundle outState) {
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    public void onDestroy() {
        mMapView.onDestroy();
//        if(myGdLocationClass!=null){
//            myGdLocationClass.onDestroy();
//        }
    }

    /**
     * 方法重写
     * @param listener
     */
    public void activate(LocationSource.OnLocationChangedListener listener) {
//        if(myGdLocationClass==null){
//            myGdLocationClass=new MyGdLocationClass(mContext);
//            myGdLocationClass.mListener = listener;
//            myGdLocationClass.start();
//        }
    }
}
