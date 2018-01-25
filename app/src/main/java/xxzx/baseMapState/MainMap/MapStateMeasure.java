package xxzx.baseMapState.MainMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import xxzx.myOverlay.MeasureOverlay;
import xxzx.myView.ImgButtonVertical;

import xxzx.activity.R;
import xxzx.library.VectorDrawable;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.WGS84MercatorConvert;

/**
 * Created by ch on 2016/1/20.
 */

public class MapStateMeasure extends BaseMainMapState {

    // 布局文件
    private ImgButtonVertical imgbtn_typeSwitch = null;
    private ImgButtonVertical imgbtn_addVex = null;
    private ImgButtonVertical imgbtn_deleteVex = null;
    private ImgButtonVertical imgbtn_delete = null;

    private TextView txtView_result=null;

    private AMap aMap=null;
    //记录测量的点集
    private List<LatLng> pts=null;

    private int geometryType;//测量种类，0表示长度测量，1表示面积测量

    private RelativeLayout pointCrossOverLay=null;

    //作图图层
    private MeasureOverlay measureOverlay = null;

    private AlertDialog.Builder builder;

    //保留小数点位数
    DecimalFormat df = new DecimalFormat("0.00");

    // 构造函数
    public MapStateMeasure(Context context,AppCompatDelegate delegate,AppBarLayout appBarLayout, LinearLayout bottomContainer, Object _mData) {
        super.BaseMainMapState(context, delegate, appBarLayout, bottomContainer, _mData);
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        MySingleClass mySingleClass = MySingleClass.getInstance();
        aMap = mySingleClass.getBaseMapViewClass().getMapView().getMap();
        //设置十字丝图层可见
        pointCrossOverLay=mySingleClass.getBaseMapViewClass().getRl_pointCrossOverLay();
        pointCrossOverLay.setVisibility(View.VISIBLE);
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
        toolbar.setTitle(R.string.name_tool_bar_measure);
        toolbar.setTitleTextColor(Color.WHITE);
        // Navigation Icon 要設定在 setSupoortActionBar 才有作用
        // 否則會出現 back button
        toolbar.setNavigationIcon(R.mipmap.toolbar_navigation_back);

        this.delegate.setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySingleClass mySingleClass=MySingleClass.getInstance();
                MapStateContext mapStateContext = mySingleClass.getMapStateContext();
                mapStateContext.backMainState();
                mapStateContext.InitViewAndData();
            }
        });


        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.RIGHT;
        layoutParams.rightMargin = 40;
        txtView_result = new TextView(mContext);
        txtView_result.setTextSize(16);
        txtView_result.setTextColor(Color.WHITE);
        txtView_result.setText("0 米");

        toolbar.addView(txtView_result, layoutParams);
        appBarLayout.removeAllViews();
        appBarLayout.addView(toolbar);
    }

    @Override
    protected void initbottomBar() {
        // TODO Auto-generated method stub
        LayoutInflater flater = LayoutInflater.from(this.mContext);
        View view = flater.inflate(R.layout.bottom_map_state_measure, null);

        this.bottomContainer.removeAllViews();
        this.bottomContainer.addView(view);

        imgbtn_typeSwitch = (ImgButtonVertical) view.findViewById(R.id.imgbtn_typeSwitch);
        imgbtn_addVex = (ImgButtonVertical) view.findViewById(R.id.imgbtn_addVex);
        imgbtn_deleteVex = (ImgButtonVertical) view.findViewById(R.id.imgbtn_deleteVex);
        imgbtn_delete = (ImgButtonVertical) view.findViewById(R.id.imgbtn_delete);

        imgbtn_typeSwitch.setImage(VectorDrawable.getDrawable(mContext, R.drawable.iconfont_switch));
        imgbtn_addVex.setImage(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_add));
        imgbtn_deleteVex.setImage(VectorDrawable.getDrawable(mContext, R.drawable.iconfont_back));
        imgbtn_delete.setImage(VectorDrawable.getDrawable(mContext, R.drawable.iconfont_delete));


        imgbtn_typeSwitch.setOnClick(new BtnOnClickListener());
        imgbtn_addVex.setOnClick(new BtnOnClickListener());
        imgbtn_deleteVex.setOnClick(new BtnOnClickListener());
        imgbtn_delete.setOnClick(new BtnOnClickListener());
    }

    @Override
    protected void initData() {
        // TODO Auto-generated method stub
        pts = new ArrayList<LatLng>();

        //设置量测类别
        geometryType=0;
        measureOverlay = new MeasureOverlay();
        measureOverlay.setGeometryType(geometryType);

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
        if(pointCrossOverLay!=null){
            pointCrossOverLay.setVisibility(View.GONE);
        }
        //清空标题容器
        appBarLayout.removeAllViews();
        //清空底部容器
        this.bottomContainer.removeAllViews();
        //清空所有图形
        measureOverlay.removeFromMap();

        pointCrossOverLay=null;
        pts.clear();
        pts=null;
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
                    showMeasureResult();
                    break;
                case R.id.imgbtn_deleteVex:
                    if(pts.size()>0) {
                        pts.remove(pts.size() - 1);
                        draw();
                        showMeasureResult();
                    } else {
                        Toast.makeText(mContext, "当前节点数为0！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.imgbtn_delete:
                    pts.clear();
                    measureOverlay.removeFromMap();
                    showMeasureResult();
                    break;
            }
        }
    }

    /**
     * 显示测量对话框
     */
    private void showTypeDialog() {

        if (builder != null) {
            builder.create().show();
            return;
        }

        final String items[] = {"距离量测", "面积量测"};
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
                    measureOverlay.removeFromMap();
                    measureOverlay.setGeometryType(geometryType);
                    //修改按钮点线面的状态
                    switch (geometryType) {
                        case 0:
                            imgbtn_typeSwitch.setTitle("距离量测");
                            txtView_result.setText("0 米");
                            break;
                        case 1:
                            imgbtn_typeSwitch.setTitle("面积量测");
                            txtView_result.setText("0 平方米");
                            break;
                    }
                }
            }
        });

        builder.create().show();
    }

    private void addPoint(){
        int[] location = new int[2];
        pointCrossOverLay.getLocationOnScreen(location);
        Point ptScreen = new Point();
        ptScreen.set(location[0] + pointCrossOverLay.getWidth()/2,location[0] + pointCrossOverLay.getHeight()/2);

        Projection proj = aMap.getProjection(); // 获取投影对象
        LatLng latlng = proj.fromScreenLocation(ptScreen);

        pts.add(latlng);
    }

    private void draw() {
        measureOverlay.setmPois(pts);
        measureOverlay.addToMap(this.aMap);
    }


    /**
     * 显示测量结果
     */
    private void showMeasureResult(){

       if(this.geometryType==0){
           double length= measureLength();
           if(length==0){
               txtView_result.setText("0 米");
               return;
           }
           if(length>1000){
               txtView_result.setText(String.valueOf(df.format(length/1000))+" 公里");
           }else{
               txtView_result.setText(String.valueOf(df.format(length))+" 米");
           }
       }else{

           double area= measureArea();
           if(area==0){
               txtView_result.setText("0 米");
           }

           if(area>1000000){
               txtView_result.setText(String.valueOf(df.format(area/1000000))+" 平方公里");

           }else{
               txtView_result.setText(String.valueOf(df.format(area))+" 平方米");

           }
       }
    }

    private double measureLength() {
        double length = 0;
        if (pts.size() < 2) {
            return length;
        }
        for (int i = 1; i < pts.size(); i++) {
            length = length + AMapUtils.calculateLineDistance(pts.get(i - 1), pts.get(i));
        }
        return length;
    }


    private double measureArea() {
        double area = 0;
        if (pts.size() < 3) {
            return area;
        }

        double[] pt_0 = null;
        double[] pt_pre = null;
        double sum = 0;
        for (int i = 0; i < pts.size(); i++) {
            double[] pt =WGS84MercatorConvert.lonLat2Mercator(pts.get(i).longitude, pts.get(i).latitude);
            if (i == 0) {
                pt_0 = pt;
                pt_pre = pt;
            }
            sum = sum + pt_pre[0] * pt[1] - pt_pre[1] * pt[0];
            pt_pre = pt;
        }

        sum = sum + pt_pre[0] * pt_0[1] - pt_pre[1] * pt_0[0];
        area=0.5*Math.abs(sum);
        return area;
    }





}
