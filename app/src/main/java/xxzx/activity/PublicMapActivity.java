package xxzx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import com.amap.api.maps.LocationSource;

import xxzx.baseMapState.PublicMap.BasePublicMapState;
import xxzx.baseMapState.PublicMap.PublicMapStateRoutePlanning;
import xxzx.baseMapState.PublicMap.PublicMapStateSelectPoi;
import xxzx.baseMapState.PublicMap.PublicMapStateSelectGeometry;
import xxzx.baseMapState.PublicMap.PublicMapStateShowGeometry;
import xxzx.mapLayerOpt.PublicBaseMapViewClass;
import xxzx.publicClass.MyString;

public class PublicMapActivity extends AppCompatActivity implements LocationSource {

    private AppBarLayout appBarLayout = null;
    private LinearLayout bottomContainer = null;

    //地图视图类
    private PublicBaseMapViewClass publicBaseMapViewClass = null;
    //地图容器
    private LinearLayout mapContainer = null;

    private BasePublicMapState mapState=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);

        //初始化
        this.initView(savedInstanceState);
        this.initData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            if(mapState!=null){
                mapState.BackKeyDown();
            }
        }

        return false;

    }


    private void initView(Bundle savedInstanceState) {
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        bottomContainer = (LinearLayout) findViewById(R.id.bottomContainer);
        mapContainer = (LinearLayout) findViewById(R.id.mapContainer);

        publicBaseMapViewClass = new PublicBaseMapViewClass(this);

        publicBaseMapViewClass.initView(savedInstanceState);
        publicBaseMapViewClass.initData();

        mapContainer.addView(publicBaseMapViewClass);

        // 创建状态环境类
        Intent intent = getIntent();

        String state = intent.getStringExtra("state");
        String data = intent.getStringExtra("data");


        if(state.equals("")){
            return;
        }
        //路径规划
        if (state.equals(MyString.intent_map_state_routeplanning)) {
            mapState = new PublicMapStateRoutePlanning(this, getDelegate(), appBarLayout, bottomContainer, null);
        }

        //地图采点
        if (state.equals(MyString.intent_map_state_selectpoi)) {
            mapState = new PublicMapStateSelectPoi(this, getDelegate(), appBarLayout, bottomContainer, null);
        }

        //地图采面，用于离线地图范围
        if (state.equals(MyString.intent_map_state_selectgeometry)) {
            mapState = new PublicMapStateSelectGeometry(this, getDelegate(), appBarLayout, bottomContainer, data);
        }

        //显示数据
        if (state.equals(MyString.intent_map_state_showgeometry)) {
            mapState = new PublicMapStateShowGeometry(this, getDelegate(), appBarLayout, bottomContainer, data);
        }

        mapState.setBaseMapView(publicBaseMapViewClass);

    }

    private void initData() {


    }


    @Override
    protected void onNewIntent(Intent intent) {
        //setTitle("I am Activity1 too, but I called onNewIntent!");


        super.onNewIntent(intent);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        //返回键
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if(mapStateContext!=null){
//                mapStateContext.BackKeyDown();
//            }
//        }
//        return false;
//    }

    /**
     * 地图控件暂停
     */
    @Override
    public void onPause() {
        super.onPause();
        if (publicBaseMapViewClass != null) {
            publicBaseMapViewClass.onPause();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (publicBaseMapViewClass != null) {
            publicBaseMapViewClass.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onStart(){
        //注册广播接收器
        super.onStart();
    }

    /**
     * 停止
     */
    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * 地图控件重新启动
     */
    @Override
    public void onResume() {
        super.onResume();
        if (publicBaseMapViewClass != null) {
            publicBaseMapViewClass.onResume();
        }
    }

    @Override
    public void onDestroy() {

        if(this.publicBaseMapViewClass!=null){
            this.publicBaseMapViewClass.onDestroy();
        }

        super.onDestroy();
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        if(publicBaseMapViewClass!=null){
            publicBaseMapViewClass.activate(listener);
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
    }

}
