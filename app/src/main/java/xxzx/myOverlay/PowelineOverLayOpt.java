package xxzx.myOverlay;

import android.os.Handler;
import android.os.Message;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.MyTaskExecutorClass;
import xxzx.spatialite.TableOptClass.ChannelTableOpt;
import xxzx.spatialite.TableOptClass.PoleTableOpt;
import xxzx.spatialite.TableOptClass.PowerlineTableOpt;
import xxzx.spatialite.TableStruct.ChannelTableDataClass;
import xxzx.spatialite.TableStruct.PoleTableDataClass;
import xxzx.spatialite.TableStruct.PowerlineTableDataClass;

/**
 * Created by ch on 2016/5/24.
 */
public class PowelineOverLayOpt {

    //获得自定义图层列表
    //private Map<String, Object> overlayMap;

    //获得地图
    private AMap aMap;
    //通道点和杆塔点显示级别控制
    private int showMarkerLevel = 14;

    private List<PoleChnOverLay> powelineOverLays=new ArrayList<>();
    private PlineOverLay plineOverLay = null;

    /**
     * 每次执行限定个数个任务的线程池
     */
    private ExecutorService limitedTaskExecutor = null;
    /**
     * 线程池数量
     */
    private int limitedTaskExecutorNum = 5;

    /**
     * 这个函数是在BaseMapViewClass中调用，因为BaseMapViewClass没有创建完成时
     * 函数mySingleClass.getBaseMapViewClass()并不存在
     * aMap也就不存在
     * @param aMap
     */
    public PowelineOverLayOpt(AMap aMap) {
        //获得地图
        this.aMap = aMap;
        this.plineOverLay = new PlineOverLay();
        //MySingleClass mySingleClass = MySingleClass.getInstance();
        //this.overlayMap = new HashMap<>();
        //获得自定义图层列表
        //overlayMap = mySingleClass.getOverlayMap();
    }


    public PowelineOverLayOpt() {
        MySingleClass mySingleClass = MySingleClass.getInstance();
        //获得地图
        this.aMap = mySingleClass.getBaseMapViewClass().getMapView().getMap();
        this.plineOverLay = new PlineOverLay();

    }


    public void addPlineOverlay(){
        PowerlineTableOpt powerlineTableOpt = new PowerlineTableOpt();
        //获取电力线路
        List<PowerlineTableDataClass> listPowerline = powerlineTableOpt.getRowInMap();
        plineOverLay.setData(listPowerline);
        plineOverLay.addToMap(aMap);
    }


    public void removePlineOverlay(){
        if(plineOverLay!=null){
            plineOverLay.removeFromMap();
        }
    }

    /**
     * 添加线路数据
     * @param zoom
     * @param minLatLng
     * @param maxLatLng
     */
    public void updatePoleChnOverlay(float zoom,LatLng minLatLng,LatLng maxLatLng) {

        this.removePoleAndChannel();
        //数据库操作类
        PowerlineTableOpt powerlineTableOpt = new PowerlineTableOpt();
//        ChannelTableOpt channelTableOpt=new ChannelTableOpt();
//        PoleTableOpt poleTableOpt=new PoleTableOpt();
        //获取电力线路
        List<PowerlineTableDataClass> listPowerline = powerlineTableOpt.getRowInMap();

//        //通道杆塔数据储存
//        List<ChannelTableDataClass> listChannel=new ArrayList<ChannelTableDataClass>() ;
//        List<PoleTableDataClass> listPole=new ArrayList<PoleTableDataClass>() ;
        //线程结束处理程序
        limitedTaskExecutor = Executors.newFixedThreadPool(limitedTaskExecutorNum);// 限制线程池大小线程池

        //this.overlayMap.clear();

        //如果地图级别大于等于15级
        if (zoom >= showMarkerLevel) {
            for (PowerlineTableDataClass powerline : listPowerline) {

                MyRunnable myRunnable = new MyRunnable();
                myRunnable.setData(zoom, minLatLng, maxLatLng);
                myRunnable.setData(powerline);
                //执行线程池
                limitedTaskExecutor.execute(myRunnable);
            }
            //线程结束处理程序
            MyTaskExecutorClass.TaskExecutorTerminated(limitedTaskExecutor,null);

        }
    }


    /**
     * 删除杆塔和通道
     */
    public void removePoleAndChannel(){
        for (PoleChnOverLay powelineOverLay:powelineOverLays)
        {
            powelineOverLay.removePoleFromMap();
            powelineOverLay.removeChannelFromMap();
        }
    }




    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case MyString.FAIL:
                    //ToastUtil.show(m.this, msg.obj.toString());
                    break;
                case MyString.SUCCESS:
                    //显示在地图中
                    ((PoleChnOverLay)msg.obj).addChannelToMap(aMap);
                    ((PoleChnOverLay)msg.obj).addPoleToMap(aMap);

                    powelineOverLays.add((PoleChnOverLay)msg.obj);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };




    public class MyRunnable implements Runnable {
        private float zoom;
        private LatLng minLatLng;
        private LatLng maxLatLng;
        //private String powerline;
        private PowerlineTableDataClass powerline;
        //private PoleChnOverLay powelineOverLay=null;

        public void setData(float zoom,LatLng minLatLng,LatLng maxLatLng) {
            this.zoom = zoom;
            this.minLatLng = minLatLng;
            this.maxLatLng = maxLatLng;
        }

        public void setData(PowerlineTableDataClass powerline){
            this.powerline=powerline;
        }


        @Override
        public void run() {
            // TODO Auto-generated method stub
            Message message = new Message();
            try {
                PoleChnOverLay poleChnOverLay = new PoleChnOverLay();
                //数据库操作类
                ChannelTableOpt channelTableOpt = new ChannelTableOpt();
                PoleTableOpt poleTableOpt = new PoleTableOpt();

                //通道
                List<ChannelTableDataClass> listChannel = channelTableOpt.getRow(powerline.getPowerName(), minLatLng, maxLatLng);
                //杆塔
                List<PoleTableDataClass> listPole = poleTableOpt.getRow(powerline.getPowerName(), minLatLng, maxLatLng);

                //powelineOverLay.setData(powerline, listChannel, listPole);
                //powelineOverLay.setPowerline(powerline);
                poleChnOverLay.setPoles(listPole);
                poleChnOverLay.setChannels(listChannel);

                // 发送消息，通知handler在主线程中更新UI
                message.what = MyString.SUCCESS;
                message.obj = poleChnOverLay;
            } catch (Exception e) {
                message.what = MyString.FAIL;
                message.obj = e.toString();
                e.printStackTrace();
            } finally {
                myHandler.sendMessage(message);
            }
        }
    }





}
