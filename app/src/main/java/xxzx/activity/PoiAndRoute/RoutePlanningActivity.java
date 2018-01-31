package xxzx.activity.PoiAndRoute;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;

import java.util.ArrayList;

import xxzx.activity.PublicBaseActivity.BaseToolBarActivity;
import xxzx.activity.R;
import xxzx.activityNavi.TTSController;
import xxzx.library.VectorDrawable;
import xxzx.myView.ImgButton;
import xxzx.publicClass.IntentPublicMapState;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.ToastUtil;

public class RoutePlanningActivity extends BaseToolBarActivity implements AMapNaviListener, View.OnClickListener{

    private Button btn_start;
    private Button btn_end;

    private ImgButton imgbtn_switch;
    private ImgButton imgbtn_search;
    private ImageView iv_start;
    private ImageView iv_end;

    private int queryType = -1;//0表示查询起点，1表示查询终点

    // 起点终点坐标
    private NaviLatLng mNaviStart =null;
    private NaviLatLng mNaviEnd =null;
    // 起点终点列表
    private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
    private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();

    private TTSController ttsManager;
    private AMapNavi aMapNavi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_planning);

        this.initView();
        this.initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Bundle b = intent.getExtras();  //data为B中回传的Intent
        String str = (String) b.get("data");//poiItem

        String[] strs = str.split(";");

        if (queryType == 0) {
            btn_start.setText(strs[0]);
            if(mNaviStart==null){
                mNaviStart = new NaviLatLng();
            }
            mNaviStart.setLatitude(Double.valueOf(strs[1]));
            mNaviStart.setLongitude(Double.valueOf(strs[2]));
        }
        if (queryType == 1) {
            btn_end.setText(strs[0]);
            if(mNaviEnd==null){
                mNaviEnd = new NaviLatLng();
            }
            mNaviEnd.setLatitude(Double.valueOf(strs[1]));
            mNaviEnd.setLongitude(Double.valueOf(strs[2]));
        }

        super.onNewIntent(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            Bundle b = data.getExtras();  //data为B中回传的Intent
            String str = (String) b.get("data");//poiItem

            String[] strs = str.split(";");

            if (queryType == 0) {
                btn_start.setText(strs[0]);
                if(mNaviStart==null){
                    mNaviStart = new NaviLatLng();
                }
                mNaviStart.setLatitude(Double.valueOf(strs[1]));
                mNaviStart.setLongitude(Double.valueOf(strs[2]));
            }
            if (queryType == 1) {
                btn_end.setText(strs[0]);
                if(mNaviEnd==null){
                    mNaviEnd = new NaviLatLng();
                }
                mNaviEnd.setLatitude(Double.valueOf(strs[1]));
                mNaviEnd.setLongitude(Double.valueOf(strs[2]));
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_toolbar_inputdata, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // 点击返回图标事件
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.btn_start:
                queryType = 0;//查询起点
                intent = new Intent();
                intent.setClass(RoutePlanningActivity.this, PoiSearchActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.btn_end:
                queryType = 1;//查询终点
                intent = new Intent();
                intent.setClass(RoutePlanningActivity.this, PoiSearchActivity.class);
                startActivityForResult(intent, 0);
                break;
        }
    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo){

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat){

    }
    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfo){

    }
    @Override
    public void notifyParallelRoad(int parallelRoadType){

    }




    private void initView() {

        btn_start=(Button)findViewById(R.id.btn_start);
        btn_end=(Button)findViewById(R.id.btn_end);

        imgbtn_switch=(ImgButton)findViewById(R.id.imgbtn_switch);
        imgbtn_search=(ImgButton)findViewById(R.id.imgbtn_search);
        iv_start=(ImageView)findViewById(R.id.iv_start);
        iv_end=(ImageView)findViewById(R.id.iv_end);

        imgbtn_switch.setImage(VectorDrawable.getDrawable(this, R.drawable.iconfont_route_qiehuan));
        imgbtn_search.setImage(VectorDrawable.getDrawable(this, R.drawable.iconfont_route_search));
        iv_start.setBackground(VectorDrawable.getDrawable(this, R.drawable.iconfont_route_start));
        iv_end.setBackground(VectorDrawable.getDrawable(this, R.drawable.iconfont_route_end));

        imgbtn_switch.setOnClick(new imgbtnOnClickListener());
        imgbtn_search.setOnClick(new imgbtnOnClickListener());
        iv_start.setOnClickListener(this);
        iv_end.setOnClickListener(this);


        btn_start.setOnClickListener(this);
        btn_end.setOnClickListener(this);
    }


    private void initData() {
        ttsManager = TTSController.getInstance(this);
        ttsManager.init();
        ttsManager.startSpeaking();

        aMapNavi = AMapNavi.getInstance(this);
        aMapNavi.setAMapNaviListener(this);
        aMapNavi.setAMapNaviListener(ttsManager);
        aMapNavi.setEmulatorNaviSpeed(150);

    }

    /**
     * 按钮点击监听器
     */
    private class imgbtnOnClickListener implements ImgButton.ICoallBack {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.imgbtn_switch:
                    String start = "";
                    String end = "";
                    NaviLatLng tempNaviLatLng = null;
                    if (mNaviStart != null) {
                        start = btn_start.getText().toString();
                    }
                    if (mNaviEnd != null) {
                        end = btn_end.getText().toString();
                        tempNaviLatLng = new NaviLatLng();
                        tempNaviLatLng.setLongitude(mNaviEnd.getLongitude());
                        tempNaviLatLng.setLongitude(mNaviEnd.getLatitude());
                    }

                    if (!start.equals("")) {
                        btn_end.setText(start);
                        mNaviEnd = mNaviStart;
                    } else {
                        btn_end.setText("输入终点…");
                        mNaviEnd = null;
                    }
                    if (!end.equals("")) {
                        btn_start.setText(end);
                        mNaviStart = tempNaviLatLng;
                    } else {
                        btn_start.setText("输入起点…");
                        mNaviStart = null;
                    }
                    break;
                case R.id.imgbtn_search:
                    if (mNaviStart != null && mNaviEnd != null) {
                        calculateDriveRoute();
                    } else {
                        ToastUtil.show(RoutePlanningActivity.this, "请确定起点和终点！");
                    }
                    break;
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    //计算驾车路线
    private void calculateDriveRoute() {
        mStartPoints.clear();
        mEndPoints.clear();
        mStartPoints.add(mNaviStart);
        mEndPoints.add(mNaviEnd);

        boolean isSuccess = aMapNavi.calculateDriveRoute(mStartPoints,
                mEndPoints, null, PathPlanningStrategy.DRIVING_DEFAULT);
        if (!isSuccess) {
            showToast("路线计算失败,检查参数情况");
        }
    }

    //计算步行路线
    private void calculateFootRoute() {
        boolean isSuccess = aMapNavi.calculateWalkRoute(mNaviStart, mNaviEnd);
        if (!isSuccess) {
            showToast("路线计算失败,检查参数情况");
        }
    }



    //--------------------导航监听回调事件-----------------------------
    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onArrivedWayPoint(int arg0) {

    }

    @Override
    public void onCalculateRouteFailure(int arg0) {
        showToast("路径规划出错" + arg0);
    }

    @Override
    public void onCalculateRouteSuccess() {
        AMapNaviPath naviPath = aMapNavi.getNaviPath();
        if (naviPath == null) {
            return;
        }

        MySingleClass mySingleClass= MySingleClass.getInstance();
        mySingleClass.setNaviPath(naviPath);

        //转入地图主界面
        IntentPublicMapState.Intent(RoutePlanningActivity.this, MyString.requestCode_activity_to_publicmapactivity, MyString.intent_map_state_routeplanning, "");

        //销毁该activity
        finish();
    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onGetNavigationText(int arg0, String arg1) {

    }

    @Override
    public void onGpsOpenStatus(boolean arg0) {

    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation arg0) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo arg0) {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onStartNavi(int arg0) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

//------------------生命周期重写函数---------------------------

    @Override
    public void onResume() {
        super.onResume();
//        mStartPoints.add(mNaviStart);
//        mEndPoints.add(mNaviEnd);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        aMapNavi.destroy();
//        ttsManager.destroy();
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo arg0) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {

    }





}
