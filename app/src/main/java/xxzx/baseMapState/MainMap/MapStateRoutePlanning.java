package xxzx.baseMapState.MainMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.amap.api.navi.AMapNavi;

import xxzx.activity.PoiAndRoute.GpsNaviActivity;

import xxzx.activity.R;
import xxzx.activityNavi.TTSController;
import xxzx.publicClass.MySingleClass;

/**
 * Created by ch on 2016/1/20.
 */

public class MapStateRoutePlanning extends BaseMainMapState {

    private TTSController ttsManager;
    private AMapNavi aMapNavi;

    // 构造函数
    public MapStateRoutePlanning(Context context, AppCompatDelegate delegate, AppBarLayout appBarLayout, LinearLayout bottomContainer, Object _mData) {
        super.BaseMainMapState(context, delegate, appBarLayout, bottomContainer, _mData);
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void initToolBar() {
        // TODO Auto-generated method stub
        LayoutInflater flater = LayoutInflater.from(this.mContext);
        View view_toolbar = flater.inflate(R.layout.public_base_toolbar_map, null);
        Toolbar toolbar = (Toolbar) view_toolbar.findViewById(R.id.toolbar);
        // App Logo
        // Title
        toolbar.setTitle(R.string.name_tool_bar_route_planning);
        toolbar.setTitleTextColor(Color.WHITE);
        // Navigation Icon 要設定在 setSupoortActionBar 才有作用
        // 否則會出現 back button
        toolbar.setNavigationIcon(R.mipmap.toolbar_navigation_back);
        this.delegate.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //后退按钮
                BackKeyDown();
            }
        });
        appBarLayout.removeAllViews();
        appBarLayout.addView(toolbar);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

    }

    @Override
    protected void initbottomBar() {
        // TODO Auto-generated method stub
        LayoutInflater flater = LayoutInflater.from(this.mContext);
        View view = flater.inflate(R.layout.bottom_map_state_routeplanning, null);

        Button btn_gpsNavi =(Button)view.findViewById(R.id.btn_gpsNavi);
        btn_gpsNavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //转换到实时导航界面
                Intent intent=new Intent();
                intent.setClass(mContext, GpsNaviActivity.class);
                mContext.startActivity(intent);
            }
        });

        this.bottomContainer.removeAllViews();
        this.bottomContainer.addView(view);
    }

    @Override
    protected void initData() {
        // TODO Auto-generated method stub
        ttsManager = TTSController.getInstance(mContext);
        ttsManager.init();
        ttsManager.startSpeaking();

        aMapNavi = AMapNavi.getInstance(mContext);
        aMapNavi.setAMapNaviListener(ttsManager);
        aMapNavi.setEmulatorNaviSpeed(150);
    }

    @Override
    protected void BackKeyDown() {
        // TODO Auto-generated method stub
        MySingleClass mySingleClass=MySingleClass.getInstance();

        //移除路网图层
        //RouteOverLay mRouteOverLay = (RouteOverLay)mySingleClass.getOverlayArray().get(MyString.overlay_route);
        //mRouteOverLay.removeFromMap();
        //mRouteOverLay.destroy();

        //销毁路径信息
        aMapNavi.stopNavi();
        aMapNavi.destroy();
        ttsManager.destroy();

        //返回主状态
        MapStateContext mapStateContext = mySingleClass.getMapStateContext();
        mapStateContext.backMainState();
        mapStateContext.InitViewAndData();
    }

    @Override
    protected void Close() {
        //清空标题容器
        appBarLayout.removeAllViews();
        //清空底部容器
        this.bottomContainer.removeAllViews();
    }

}
