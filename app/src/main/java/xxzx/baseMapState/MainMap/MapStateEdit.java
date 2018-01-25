package xxzx.baseMapState.MainMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.EditDataCreateActivity;
import xxzx.activity.R;
import xxzx.library.VectorDrawable;
import xxzx.myOverlay.EditOverlay;
import xxzx.myView.ImgButtonVertical;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.geometryJson.WKT;

/**
 * Created by ch on 2016/1/20.
 */

public class MapStateEdit extends BaseMainMapState implements View.OnClickListener {

    // 布局文件
    private ImgButtonVertical imgbtn_typeSwitch = null;
    private ImgButtonVertical imgbtn_addVex = null;
    private ImgButtonVertical imgbtn_deleteVex = null;
    private ImgButtonVertical imgbtn_delete = null;
    private ImgButtonVertical imgbtn_more = null;

    private AMap aMap = null;
    //记录测量的点集
    private List<LatLng> pts = null;
    private EditOverlay editOverlay=null;
    private int geometryType = 0;//测量种类，0表示点，1表示线，2表示面
    private RelativeLayout pointCrossOverLay = null;

    //图形种类对话框
    AlertDialog.Builder builder;

    // 构造函数
    public MapStateEdit(Context context, AppCompatDelegate delegate, AppBarLayout appBarLayout, LinearLayout bottomContainer, Object _mData) {
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


        final String items[] = {"点", "线", "自由面"};
        //dialog参数设置
        builder = new AlertDialog.Builder(mContext);  //先得到构造器
        builder.setTitle("请选择图形类型"); //设置标题
        //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (geometryType != which) {
                    geometryType = which;
                    dialog.dismiss();
                    pts.clear();
                    editOverlay.removeFromMap();
                    editOverlay.setGeometryType(geometryType);
                    //修改按钮点线面的状态
                    switch (geometryType) {
                        case 0:
                            imgbtn_typeSwitch.setTitle("点");
                            break;
                        case 1:
                            imgbtn_typeSwitch.setTitle("线");
                            break;
                        case 2:
                            imgbtn_typeSwitch.setTitle("自由面");
                            break;
                        case 3:
                            imgbtn_typeSwitch.setTitle("矩形");
                            break;
                        case 4:
                            imgbtn_typeSwitch.setTitle("圆");
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

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
                MySingleClass mySingleClass = MySingleClass.getInstance();
                MapStateContext mapStateContext = mySingleClass.getMapStateContext();
                mapStateContext.backMainState();
                mapStateContext.InitViewAndData();
            }
        });

        ActionMenuView actionMenuView = new ActionMenuView(this.mContext);
        actionMenuView.getMenu().clear();
        //判断菜单是否存在，若存在则删除
        View view = toolbar.getChildAt(this.toolBarMenuID);
        if (view != null) {
            toolbar.removeViewAt(this.toolBarMenuID);
        }
        //actionMenuView.setOnMenuItemClickListener(this);

        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.RIGHT;
        toolbar.addView(actionMenuView, layoutParams);

        appBarLayout.removeAllViews();
        appBarLayout.addView(toolbar);

    }

    @Override
    protected void initbottomBar() {
        // TODO Auto-generated method stub
        LayoutInflater flater = LayoutInflater.from(this.mContext);
        View view = flater.inflate(R.layout.bottom_map_state_edit, null);

        this.bottomContainer.removeAllViews();
        this.bottomContainer.addView(view);

        imgbtn_typeSwitch = (ImgButtonVertical) view.findViewById(R.id.imgbtn_typeSwitch);
        imgbtn_addVex = (ImgButtonVertical) view.findViewById(R.id.imgbtn_addVex);
        imgbtn_deleteVex = (ImgButtonVertical) view.findViewById(R.id.imgbtn_deleteVex);
        imgbtn_delete = (ImgButtonVertical) view.findViewById(R.id.imgbtn_delete);
        imgbtn_more = (ImgButtonVertical) view.findViewById(R.id.imgbtn_more);

        imgbtn_typeSwitch.setImage(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_switch));
        imgbtn_addVex.setImage(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_add));
        imgbtn_deleteVex.setImage(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_back));
        imgbtn_delete.setImage(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_delete));
        imgbtn_more.setImage(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_more));

        imgbtn_typeSwitch.setOnClick(new BtnOnClickListener());
        imgbtn_addVex.setOnClick(new BtnOnClickListener());
        imgbtn_deleteVex.setOnClick(new BtnOnClickListener());
        imgbtn_delete.setOnClick(new BtnOnClickListener());
        imgbtn_more.setOnClick(new BtnOnClickListener());
    }

    @Override
    protected void initData() {
        // TODO Auto-generated method stub
        pts = new ArrayList<LatLng>();
        editOverlay=new EditOverlay();
    }

    @Override
    protected void BackKeyDown() {
        // TODO Auto-generated method stub
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
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    /**
     * 按钮点击监听器
     */
    private class BtnOnClickListener implements ImgButtonVertical.ICoallBack {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.imgbtn_typeSwitch:
                    showTypeDialog();
                    break;
                case R.id.imgbtn_addVex:
                    addPoint();
                    draw();
                    break;
                case R.id.imgbtn_deleteVex:
                    if (pts.size() > 0) {
                        pts.remove(pts.size() - 1);
                        draw();
                    } else {
                        Toast.makeText(mContext, "当前节点数为0！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.imgbtn_delete:
                    pts.clear();
                    draw();
                    break;
                case R.id.imgbtn_more:
                    //如果当前图形为空
                    if(pts.size()==0){
                        Toast.makeText(mContext, "没有完成图形编辑!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String wkt = getGeometryWKT();
                    if(wkt.equals("")){
                        Toast.makeText(mContext, "没有完成图形编辑!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //生成一个Intent对象
                    Intent intent = new Intent();
                    //在Intent对象当中添加一个键值对
                    intent.putExtra("geometry", wkt);
                    intent.setClass(mContext, EditDataCreateActivity.class);
                    mContext.startActivity(intent);
                    break;
            }
        }
    }

    private void addPoint() {

        //如果是画矩形和圆，最多只能存储两个点，多点需要清空
        if((geometryType==3||geometryType==4)&pts.size()==2){
            pts.clear();
        }

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

    private void showTypeDialog() {
        if(builder!=null){
            builder.show();
            return;
        }
        builder.create().show();
    }


    /**
     * 获得图形的wkt字符串
     *
     * @return
     */
    private String getGeometryWKT() {
        String wktStr = "";
        switch (this.geometryType) {
            case 0:
                wktStr = WKT.PointListToPointWKT(pts);
                break;
            case 1:
                wktStr = WKT.PointListToPolylineWKT(pts);
                break;
            case 2:
                wktStr = WKT.PointListToPolygonWKT(pts);
                break;
        }

        return wktStr;
    }

}
