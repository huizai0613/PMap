package xxzx.baseMapState.MainMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;

import java.util.List;

import xxzx.activity.EditDataLocalActivity;
import xxzx.activity.MyGpsLineActivity;
import xxzx.activity.R;
import xxzx.publicClass.GdMapTool;
import xxzx.publicClass.GdGeometryOptions;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.geometryJson.WKT;

/**
 * Created by ch on 2016/1/20.
 */

public class MapStateShowGeometry extends BaseMainMapState {

    private MapView mapView=null;
    private Marker maker;
    private Polyline polyline;
    private Polygon polygon;
    public BackActivityType activityType;

    public enum  BackActivityType{
        EditDatalocal,
        GpsLine;
    }

    // 构造函数
    public MapStateShowGeometry(Context context, AppCompatDelegate delegate, AppBarLayout appBarLayout, LinearLayout bottomContainer, Object _mData,BackActivityType type) {
        super.BaseMainMapState(context, delegate, appBarLayout, bottomContainer, _mData);
        this.activityType = type;
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        MySingleClass mySingleClass = MySingleClass.getInstance();
        mapView = mySingleClass.getBaseMapViewClass().getMapView();
    }

    @Override
    protected void initToolBar() {
        // TODO Auto-generated method stub
        LayoutInflater flater = LayoutInflater.from(this.mContext);
        View view_toolbar = flater.inflate(R.layout.public_base_toolbar_map, null);
        Toolbar toolbar = (Toolbar) view_toolbar.findViewById(R.id.toolbar);
        // Title
        toolbar.setTitle(R.string.name_tool_bar_data_geometry);
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
    }

    @Override
    protected void initData() {
        // TODO Auto-generated method stub
        String wkt=(String)this.mData;
        if(wkt.contains("POINT")){
            this.addPoint(wkt);
        }
        if(wkt.contains("LINESTRING")){
            this.addPolyline(wkt);
        }
        if(wkt.contains("POLYGON")){
            this.addPolygon(wkt);
        }
    }



    @Override
    protected void BackKeyDown() {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        if (this.activityType == BackActivityType.EditDatalocal) {
            intent.setClass(mContext, EditDataLocalActivity.class);
        }
        if (this.activityType == BackActivityType.GpsLine) {
            intent.setClass(mContext, MyGpsLineActivity.class);
        }
        mContext.startActivity(intent);

        MySingleClass mySingleClass = MySingleClass.getInstance();
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

        //移除图形
        if(maker!=null){
            maker.remove();
        }

        if(polyline!=null){
            polyline.remove();
        }

        if(polygon!=null){
            polygon.remove();
        }
    }


    /**
     * 增加点数据
     */
    private void addPoint(String wkt){
        List<LatLng> pts=WKT.POINTWktToPtsList(wkt);

        for(LatLng latlng:pts){
            MarkerOptions markerOptions= GdGeometryOptions.getMarkerOptions(latlng, BitmapDescriptorFactory.fromResource(R.mipmap.map_point_red));
            maker = this.mapView.getMap().addMarker(markerOptions);
        }

        //平移到该图形
        LatLngBounds bounds = GdMapTool.getLatLngBounds(pts);
        this.mapView.getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
    }

    /**
     * 增加线数据
     */
    private void addPolyline(String wkt) {
        List<LatLng> pts = WKT.POLYLINEWktToPtsList(wkt);
        PolylineOptions polylineOptions = GdGeometryOptions.getPolylineOptions(pts, Color.RED);
        polyline = this.mapView.getMap().addPolyline(polylineOptions);

        //平移到该图形
        LatLngBounds bounds = GdMapTool.getLatLngBounds(pts);
        this.mapView.getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
    }

    /**
     * 增加面数据
     */
    private void addPolygon(String wkt){
        List<LatLng> pts = WKT.POLYGONWktToPtsList(wkt);
        PolygonOptions polygonOptions = GdGeometryOptions.getPolygonOptions(pts, Color.argb(255, 247, 10, 28), Color.argb(50, 247, 10, 28));
        polygon = this.mapView.getMap().addPolygon(polygonOptions);

        //平移到该图形
        LatLngBounds bounds = GdMapTool.getLatLngBounds(pts);
        this.mapView.getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
    }


}
