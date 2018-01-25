package xxzx.baseMapState.PublicMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.amap.api.maps.Projection;
import com.amap.api.maps.model.LatLng;

import xxzx.activity.PoiAndRoute.RoutePlanningActivity;
import xxzx.activity.R;

/**
 * Created by ch on 2016/1/20.
 */

public class PublicMapStateSelectPoi extends BasePublicMapState implements ActionMenuView.OnMenuItemClickListener{

    private RelativeLayout pointCrossOverLay=null;


    // 构造函数
    public PublicMapStateSelectPoi(Context context, AppCompatDelegate delegate, AppBarLayout appBarLayout, LinearLayout bottomContainer, Object _mData) {
        super.BasePublicMapState(context, delegate, appBarLayout, bottomContainer, _mData);
    }

    @Override
    public void onMapSubOption() {

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
        toolbar.setTitle(R.string.name_tool_bar_select_poi);
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

        //创建工具条菜单
        ActionMenuView actionMenuView = this.getActionMenuView(toolbar);

        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.RIGHT;
        toolbar.addView(actionMenuView, layoutParams);

        appBarLayout.removeAllViews();
        appBarLayout.addView(toolbar);
    }

    @Override
    protected void initbottomBar() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void initData() {
        // TODO Auto-generated method stub
        //设置十字丝图层可见
        pointCrossOverLay=this.publicBaseMapViewClass.getRl_pointCrossOverLay();
        pointCrossOverLay.setVisibility(View.VISIBLE);
    }


    @Override
    protected void Close() {
        //清除十字丝图层
        if(pointCrossOverLay!=null){
            pointCrossOverLay.setVisibility(View.GONE);
        }
        //清空标题容器
        appBarLayout.removeAllViews();
        //清空底部容器
        this.bottomContainer.removeAllViews();
        pointCrossOverLay=null;
    }

    /**
     * toolbar上面的菜单点击事件
     * @param menuItem
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_completed:
                //获得定位点
                LatLng latlng=getPoint();
                //跳转activity
                Intent intent = new Intent();
                //传递选点数据
                intent.putExtra("data", "地图选点;"+latlng.latitude+";"+latlng.longitude);
                intent.setClass(mContext, RoutePlanningActivity.class);
                mContext.startActivity(intent);
//                ((Activity)mContext).setResult(Activity.RESULT_OK, intent);
//                ((Activity)mContext).finish();
                break;
        }
        return true;
    }

    /**
     * 创建工具条菜单
     * @return
     */
    private ActionMenuView getActionMenuView(Toolbar toolbar){

        //创建工具条菜单
        ActionMenuView actionMenuView = new ActionMenuView(this.mContext);
        actionMenuView.getMenu().clear();
        ((Activity) this.mContext).getMenuInflater().inflate(R.menu.menu_map_select_poi, actionMenuView.getMenu());
        //判断菜单是否存在，若存在则删除
        View view_menu = toolbar.getChildAt(this.toolBarMenuID);
        if (view_menu != null) {
            toolbar.removeViewAt(this.toolBarMenuID);
        }
        actionMenuView.setOnMenuItemClickListener(this);

        return actionMenuView;
    }


    private LatLng getPoint(){
        int[] location = new int[2];
        pointCrossOverLay.getLocationOnScreen(location);
        Point ptScreen = new Point();
        ptScreen.set(location[0] + pointCrossOverLay.getWidth() / 2, location[0] + pointCrossOverLay.getHeight() / 2);

        Projection proj = this.aMap.getProjection(); // 获取投影对象
        LatLng latlng = proj.fromScreenLocation(ptScreen);
        return latlng;
    }
}
