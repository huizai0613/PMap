package xxzx.baseMapState.MainMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;

import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ActionMenuView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.amap.api.maps.AMap;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import xxzx.activity.OfflineMap.OfflineMapGgActivity;
import xxzx.activity.R;
import xxzx.myOverlay.EditOverlay;
import xxzx.publicClass.MySingleClass;

/**
 * Created by ch on 2016/1/20.
 */

public class MapStateSelectPolygon extends BaseMainMapState implements ActionMenuView.OnMenuItemClickListener{

    private AMap aMap = null;
    //记录点集
    private List<LatLng> pts = null;
    private EditOverlay editOverlay=null;
    private int geometryType = 2;//种类，0表示点，1表示线，2表示面
    private RelativeLayout pointCrossOverLay = null;

    //保留小数点位数
    DecimalFormat df = new DecimalFormat("###.00");

    // 构造函数
    public MapStateSelectPolygon(Context context, AppCompatDelegate delegate, AppBarLayout appBarLayout, LinearLayout bottomContainer, Object _mData) {
        super.BaseMainMapState(context, delegate, appBarLayout, bottomContainer, _mData);
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        MySingleClass mySingleClass = MySingleClass.getInstance();
        aMap = mySingleClass.getBaseMapViewClass().getMapView().getMap();
        //设置十字丝图层可见
        pointCrossOverLay = mySingleClass.getBaseMapViewClass().getRl_pointCrossOverLay();
        pointCrossOverLay.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initToolBar() {
        // TODO Auto-generated method stub
        LayoutInflater flater = LayoutInflater.from(this.mContext);
        View view_toolbar = flater.inflate(R.layout.public_base_toolbar_map, null);
        Toolbar toolbar = (Toolbar) view_toolbar.findViewById(R.id.toolbar);
        // App Logo
        // Title
        toolbar.setTitle(R.string.name_tool_bar_edit);
        toolbar.setTitleTextColor(Color.WHITE);
        // Navigation Icon 要設定在 setSupoortActionBar 才有作用
        // 否則會出現 back button
        toolbar.setNavigationIcon(R.mipmap.toolbar_navigation_back);
        this.delegate.setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackKeyDown();
            }
        });

        ActionMenuView actionMenuView = this.getActionMenuView(toolbar);

        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.RIGHT;
        toolbar.addView(actionMenuView, layoutParams);

        appBarLayout.removeAllViews();
        appBarLayout.addView(toolbar);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

    }

    @Override
    protected void initbottomBar() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void initData() {
        // TODO Auto-generated method stub
        pts = new ArrayList<LatLng>();
        editOverlay=new EditOverlay();
        editOverlay.setGeometryType(geometryType);
    }

    @Override
    protected void BackKeyDown() {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setClass(mContext, OfflineMapGgActivity.class);
        mContext.startActivity(intent);

        MySingleClass mySingleClass = MySingleClass.getInstance();
        MapStateContext mapStateContext = mySingleClass.getMapStateContext();
        mapStateContext.backMainState();
        mapStateContext.InitViewAndData();
    }


    @Override
    protected void Close() {
        //清除十字丝图层
        if (pointCrossOverLay != null) {
            pointCrossOverLay.setVisibility(View.GONE);
        }
        //清空底部容器
        this.bottomContainer.removeAllViews();
        this.pointCrossOverLay = null;
        //清空地图
        this.editOverlay.removeFromMap();

        this.pts.clear();
        this.pts = null;

    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_addVector:
                addPoint();
                draw();
                break;
            case R.id.action_delete:
                pts.clear();
                editOverlay.removeFromMap();
                break;
            case R.id.action_completed:
                //获得定位点
                if(pts.size()<3){
                    Toast.makeText(mContext,"所绘图形不是多边形，请重新绘制！",Toast.LENGTH_LONG).show();
                    break;
                }

                StringBuilder sb=new StringBuilder();
                for(LatLng pt:pts){
                    sb.append(String.valueOf(pt.latitude));
                    sb.append(",");
                    sb.append(String.valueOf(pt.longitude));
                    sb.append(";");
                }


                //跳转activity
                Intent intent = new Intent();
                //传递选点数据
                intent.putExtra("data", sb.toString());
                intent.setClass(mContext, OfflineMapGgActivity.class);
                mContext.startActivity(intent);
                //恢复原有的地图状态
                MySingleClass mySingleClass = MySingleClass.getInstance();
                MapStateContext mapStateContext = mySingleClass.getMapStateContext();
                mapStateContext.backMainState();
                mapStateContext.InitViewAndData();
                break;
        }
        return true;
    }


    private void addPoint() {
        //如果是点，首先要清除点集合
        if (geometryType == 0) {
            pts.clear();
        }
        int[] location = new int[2];
        pointCrossOverLay.getLocationOnScreen(location);
        Point ptScreen = new Point();
        ptScreen.set(location[0] + pointCrossOverLay.getWidth()/2,location[0] + pointCrossOverLay.getHeight()/2);

        Projection proj = aMap.getProjection(); // 获取投影对象
        LatLng latlng = proj.fromScreenLocation(ptScreen);

        pts.add(latlng);
    }


    private void draw() {
        editOverlay.setmPois(pts);
        editOverlay.addToMap(this.aMap);
    }


    /**
     * 创建工具条菜单
     * @return
     */
    private ActionMenuView getActionMenuView(Toolbar toolbar){

        //创建工具条菜单
        ActionMenuView actionMenuView = new ActionMenuView(this.mContext);
        actionMenuView.getMenu().clear();
        ((Activity) this.mContext).getMenuInflater().inflate(R.menu.menu_map_select_polygon, actionMenuView.getMenu());
        //判断菜单是否存在，若存在则删除
        View view_menu = toolbar.getChildAt(this.toolBarMenuID);
        if (view_menu != null) {
            toolbar.removeViewAt(this.toolBarMenuID);
        }
        actionMenuView.setOnMenuItemClickListener(this);

        return actionMenuView;
    }

}
