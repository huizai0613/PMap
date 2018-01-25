package xxzx.baseMapState.PublicMap;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.amap.api.maps.CameraUpdateFactory;
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

import xxzx.activity.R;
import xxzx.publicClass.GdGeometryOptions;
import xxzx.publicClass.GdMapTool;
import xxzx.publicClass.geometryJson.WKT;

/**
 * Created by ch on 2016/1/20.
 */

public class PublicMapStateShowGeometry extends BasePublicMapState{

    private Marker maker;
    private Polyline polyline;
    private Polygon polygon;

    // 构造函数
    public PublicMapStateShowGeometry(Context context, AppCompatDelegate delegate, AppBarLayout appBarLayout, LinearLayout bottomContainer, Object _mData) {
        super.BasePublicMapState(context, delegate, appBarLayout, bottomContainer, _mData);
        //this.activityType = type;
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
    protected void initbottomBar() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void initData() {
        // TODO Auto-generated method stub

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

    @Override
    public void onMapSubOption() {

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


    /**
     * 增加点数据
     */
    private void addPoint(String wkt){
        List<LatLng> pts=WKT.POINTWktToPtsList(wkt);

        for(LatLng latlng:pts){
            MarkerOptions markerOptions= GdGeometryOptions.getMarkerOptions(latlng, BitmapDescriptorFactory.fromResource(R.mipmap.map_point_red));
            maker = this.aMap.addMarker(markerOptions);
        }

        //平移到该图形
        LatLngBounds bounds = GdMapTool.getLatLngBounds(pts);
        this.aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
    }

    /**
     * 增加线数据
     */
    private void addPolyline(String wkt) {
        List<LatLng> pts = WKT.POLYLINEWktToPtsList(wkt);
        PolylineOptions polylineOptions = GdGeometryOptions.getPolylineOptions(pts, Color.RED);
        polyline = this.aMap.addPolyline(polylineOptions);

        //平移到该图形
        LatLngBounds bounds = GdMapTool.getLatLngBounds(pts);
        //this.mapView.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.83, 117.27), 19));

//        LatLngBounds.Builder b = LatLngBounds.builder();
//        b.include(new LatLng(31.2, 17.3));
//        b.include(new LatLng(31.1, 17.5));

        this.aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));

    }

    /**
     * 增加面数据
     */
    private void addPolygon(String wkt){
        List<LatLng> pts = WKT.POLYGONWktToPtsList(wkt);
        PolygonOptions polygonOptions = GdGeometryOptions.getPolygonOptions(pts, Color.argb(255, 247, 10, 28), Color.argb(50, 247, 10, 28));
        polygon = this.aMap.addPolygon(polygonOptions);

        //平移到该图形
        LatLngBounds bounds = GdMapTool.getLatLngBounds(pts);
        this.aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
    }


}
