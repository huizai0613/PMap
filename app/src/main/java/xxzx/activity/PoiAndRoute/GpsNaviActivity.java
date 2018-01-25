package xxzx.activity.PoiAndRoute;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.R;
import xxzx.activityNavi.TTSController;

/**
 * 创建时间：11/11/15 11:02
 * 项目名称：newNaviDemo
 *
 * @author lingxiang.wang
 * @email lingxiang.wang@alibaba-inc.com
 * 类说明：
 */

public class GpsNaviActivity extends Activity implements AMapNaviListener, AMapNaviViewListener
{

    AMapNaviView mAMapNaviView;
    AMapNavi mAMapNavi;
    TTSController mTtsManager;
    List<NaviLatLng> mStartList = new ArrayList<NaviLatLng>();
    List<NaviLatLng> mEndList = new ArrayList<NaviLatLng>();
    List<NaviLatLng> mWayPointList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_route_gps_navi);

        this.initView(savedInstanceState);
        this.initData();
    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo)
    {

    }

    @Override
    public void onPlayRing(int i)
    {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat)
    {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfo)
    {

    }

    @Override
    public void notifyParallelRoad(int parallelRoadType)
    {

    }

    @Override
    public void onNaviViewLoaded()
    {

    }


    private void initView(Bundle savedInstanceState)
    {
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);

        //为了尽最大可能避免内存泄露问题，建议这么写
        mTtsManager = TTSController.getInstance(getApplicationContext());
        mTtsManager.init();
        mTtsManager.startSpeaking();

        //为了尽最大可能避免内存泄露问题，建议这么写
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.setAMapNaviListener(this);
        mAMapNavi.setAMapNaviListener(mTtsManager);
        mAMapNavi.setEmulatorNaviSpeed(150);
    }


    private void initData()
    {
        mAMapNavi.startNavi(AMapNavi.GPSNaviMode);
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mAMapNaviView.onPause();

//        仅仅是停止你当前在说的这句话，一会到新的路口还是会再说的
        mTtsManager.stopSpeaking();
//
//        停止导航之后，会触及底层stop，然后就不会再有回调了，但是讯飞当前还是没有说完的半句话还是会说完
//        mAMapNavi.stopNavi();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mAMapNaviView.onDestroy();
        //since 1.6.0 不再在naviview destroy的时候自动执行AMapNavi.stopNavi();

    }

    @Override
    public void onInitNaviFailure()
    {
        Toast.makeText(this, "init navi Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInitNaviSuccess()
    {
        //mAMapNavi.calculateDriveRoute(mStartList, mEndList, mWayPointList, PathPlanningStrategy.DRIVING_DEFAULT);
    }

    @Override
    public void onStartNavi(int type)
    {

    }

    @Override
    public void onTrafficStatusUpdate()
    {

    }

    @Override
    public void onLocationChange(AMapNaviLocation location)
    {

    }

    @Override
    public void onGetNavigationText(int type, String text)
    {

    }

    @Override
    public void onGetNavigationText(String s)
    {

    }

    @Override
    public void onEndEmulatorNavi()
    {
    }

    @Override
    public void onArriveDestination()
    {
    }


    @Override
    public void onCalculateRouteFailure(int errorInfo)
    {
    }

    @Override
    public void onReCalculateRouteForYaw()
    {

    }

    @Override
    public void onReCalculateRouteForTrafficJam()
    {

    }

    @Override
    public void onArrivedWayPoint(int wayID)
    {

    }

    @Override
    public void onGpsOpenStatus(boolean enabled)
    {
    }

    @Override
    public void onNaviSetting()
    {
    }

    @Override
    public void onNaviMapMode(int isLock)
    {

    }

    @Override
    public void onNaviCancel()
    {
        finish();
    }


    @Override
    public void onNaviTurnClick()
    {

    }

    @Override
    public void onNextRoadClick()
    {

    }


    @Override
    public void onScanViewButtonClick()
    {
    }

    @Deprecated
    @Override
    public void onNaviInfoUpdated(AMapNaviInfo naviInfo)
    {
    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos)
    {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos)
    {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviinfo)
    {
    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo)
    {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo)
    {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross)
    {
    }

    @Override
    public void hideCross()
    {
    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross)
    {

    }

    @Override
    public void hideModeCross()
    {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] laneInfos, byte[] laneBackgroundInfo, byte[] laneRecommendedInfo)
    {

    }

    @Override
    public void hideLaneInfo()
    {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints)
    {
        mAMapNavi.startNavi(AMapNavi.EmulatorNaviMode);
    }


    @Override
    public void onLockMap(boolean isLock)
    {
    }

    @Override
    public boolean onNaviBackClick()
    {
        return false;
    }


}