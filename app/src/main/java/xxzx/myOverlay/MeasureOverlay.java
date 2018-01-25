package xxzx.myOverlay;

import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.R;
import xxzx.publicClass.GdGeometryOptions;
import xxzx.publicClass.GdMapTool;

/**
 * Created by ch on 2016/2/9.
 */
public class MeasureOverlay extends MyBaseOverlay {

    private Polygon polygon;
    private Polyline polyline;
    private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();
    private List<LatLng> mPois;
    private int geometryType = 0;//图形类型,0表示点,1表示线,2表示面

    private String OverlayName="MeasureOverlay";

    public MeasureOverlay() {

    }

    /**
     * 传入点数据
     * @param mPois
     */
    public void setmPois(List<LatLng> mPois){
        this.mPois=mPois;
    }

    /**
     * 设置图形类型，点线面
     * @param geometryType
     */
    public void setGeometryType(int geometryType) {
        this.geometryType = geometryType + 1;
    }

    @Override
    public void addToMap(AMap amap) {
        //首先移除该图层上的所有信息
        this.removeFromMap();

        if(mPois.size()==0){
            return;
        }
        switch (geometryType){
            case 0:
                break;
            case 1:
                //添加线
                polyline = amap.addPolyline(GdGeometryOptions.getPolylineOptions(this.mPois, Color.RED));
                break;
            case 2:
                polygon = amap.addPolygon(GdGeometryOptions.getPolygonOptions(this.mPois, Color.argb(255, 247, 10, 28), Color.argb(50, 247, 10, 28)));
                break;
        }

        //添加点
        for (int i = 0; i < mPois.size(); i++) {

            MarkerOptions options=GdGeometryOptions.getMarkerOptions(mPois.get(i),BitmapDescriptorFactory.fromResource(R.mipmap.map_point_red));
            Marker marker = amap.addMarker(options);
            marker.setObject(i);
            mPoiMarks.add(marker);
        }
    }

    @Override
    public void removeFromMap() {
        for (Marker mark : mPoiMarks) {
            mark.remove();
        }

        mPoiMarks.clear();

        if(polyline!=null){
            polyline.remove();
        }

        if(polygon!=null){
            polygon.remove();
        }
    }

//    public void zoomToSpan() {
//        if (mPois != null && mPois.size() > 0) {
//            if (mAMap == null)
//                return;
//            LatLngBounds bounds = getLatLngBounds();
//            mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
//        }
//    }

    private LatLngBounds getLatLngBounds() {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < mPois.size(); i++) {
            b.include(new LatLng(mPois.get(i).latitude, mPois.get(i).longitude));
        }
        return b.build();
    }

    protected BitmapDescriptor getBitmapDescriptor(int index) {
        return null;
    }

    public int getPoiIndex(Marker marker) {
        for (int i = 0; i < mPoiMarks.size(); i++) {
            if (mPoiMarks.get(i).equals(marker)) {
                return i;
            }
        }
        return -1;
    }

}
