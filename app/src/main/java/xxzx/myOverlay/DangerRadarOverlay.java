package xxzx.myOverlay;

import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.R;
import xxzx.publicClass.GdGeometryOptions;
import xxzx.publicClass.GdMapTool;
import xxzx.publicClass.MyString;
import xxzx.publicClass.geometryJson.WKT;
import xxzx.spatialite.TableStruct.ChannelDangerTableDataClass;
import xxzx.spatialite.TableStruct.PoleTableDataClass;

/**
 * Created by ch on 2016/2/9.
 * 隐患点雷达图层
 */
public class DangerRadarOverlay extends MyBaseOverlay {

    private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();

    private List<LatLng> mPois;
    private List<LatLng> mPolylinePois;
    private List<LatLng> mPolygonPois;


    private int geometryType = 0;//图形类型,0表示点,1表示线,2表示面

    private String TAG = "DangerRadarOverlay";

    public DangerRadarOverlay() {

    }


    public void setDataToMap(List<PoleTableDataClass> list_pole,List<ChannelDangerTableDataClass> list_chndanger,AMap amap){

        this.removeFromMap();

        for(PoleTableDataClass pole:list_pole) {
            pole.getPoleObjectId();
            pole.getGeometry();
            List<LatLng> pois = WKT.wktToPtsList(pole.getGeometry());

            if (pois.size() > 0) {

                MarkerOptions options = GdGeometryOptions.getMarkerOptions(pois.get(0), BitmapDescriptorFactory.fromResource(R.mipmap.map_point_red));
                Marker marker = amap.addMarker(options);
                marker.setTitle(MyString.maker_title_danger);
                marker.setObject(pole);

                mPoiMarks.add(marker);
            }
        }

        for(ChannelDangerTableDataClass chndanger:list_chndanger){
            List<LatLng> pois = WKT.wktToPtsList(chndanger.getGeometry());

            LatLng latLng = GdMapTool.getCenterPoi(pois);

            MarkerOptions options = GdGeometryOptions.getMarkerOptions(latLng, BitmapDescriptorFactory.fromResource(R.mipmap.map_point_red));
            Marker marker = amap.addMarker(options);
            marker.setObject(chndanger);
            mPoiMarks.add(marker);
        }
    }




    @Override
    public void addToMap(AMap amap) {
        //首先移除该图层上的所有信息
        this.removeFromMap();

        if (mPois.size() == 0 && mPolylinePois.size() == 0 && mPolygonPois.size() == 0) {
            return;
        }


        //添加点
        for (int i = 0; i < mPois.size(); i++) {
            MarkerOptions options = GdGeometryOptions.getMarkerOptions(mPois.get(i), BitmapDescriptorFactory.fromResource(R.mipmap.map_point_red));
            Marker marker = amap.addMarker(options);
            marker.setObject(i);
            mPoiMarks.add(marker);
        }


        //添加点
        for (int i = 0; i < mPolylinePois.size(); i++) {
            MarkerOptions options = GdGeometryOptions.getMarkerOptions(mPolylinePois.get(i), BitmapDescriptorFactory.fromResource(R.mipmap.map_point_red));
            Marker marker = amap.addMarker(options);
            marker.setObject(i);
            mPoiMarks.add(marker);
        }

        //添加点
        for (int i = 0; i < mPolygonPois.size(); i++) {
            MarkerOptions options = GdGeometryOptions.getMarkerOptions(mPolygonPois.get(i), BitmapDescriptorFactory.fromResource(R.mipmap.map_point_red));
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
    }



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
