package xxzx.myOverlay;

import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.Text;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.R;
import xxzx.publicClass.GdGeometryOptions;
import xxzx.publicClass.MyString;
import xxzx.publicClass.geometryJson.WKT;
import xxzx.spatialite.TableStruct.ChannelTableDataClass;
import xxzx.spatialite.TableStruct.PoleTableDataClass;
import xxzx.spatialite.TableStruct.PowerlineTableDataClass;

/**
 * Created by ch on 2016/2/9.
 */
public class PoleChnOverLay extends MyBaseOverlay {

    private List<ChannelTableDataClass> channels;
    private List<PoleTableDataClass> poles;
    //private PowerlineTableDataClass powerline;

    private List<PowerlineTableDataClass> powerlines;

    //记录通道的唯一标识和名称的对应,外部可访问
    //public Map<String,String> mPolylineMap = new HashMap<>();


    private Polyline mPolyline;
    //记录通道的线数据
    private ArrayList<Polyline> mPolylines = new ArrayList<Polyline>();
    //记录杆塔数据
    private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();
    //记录通道中点数据
    private ArrayList<Marker> mChnCenterMarks = new ArrayList<Marker>();
    //记录标注数据
    private ArrayList<Text> mPoiTexts = new ArrayList<Text>();


    public PoleChnOverLay() {

    }

    /**
     * 设置图层数据
     */
    public void setData(List<PowerlineTableDataClass> powerlines,List<ChannelTableDataClass> channels,List<PoleTableDataClass> poles) {
        this.powerlines = powerlines;
        this.channels = channels;
        this.poles = poles;
    }

    public void setChannels(List<ChannelTableDataClass> channels){
        this.channels = channels;
    }

    public void setPoles(List<PoleTableDataClass> poles){
        this.poles = poles;
    }


    @Override
    public void addToMap(AMap amap) {
        //添加线
        for(PowerlineTableDataClass powerline:powerlines) {
            List<LatLng> list_pline = WKT.POLYLINEWktToPtsList(powerline.getGeometry());
            if(list_pline==null||list_pline.size()==0){
                continue;
            }
            Polyline mPolyline = amap.addPolyline(GdGeometryOptions.getPolylineOptions(list_pline, Color.RED));
            mPolyline.setWidth(15);
            mPolylines.add(mPolyline);
        }


        //添加通道中点信息
        for(ChannelTableDataClass channel:channels){
            //添加电力线
            List<LatLng> list = WKT.POLYLINEWktToPtsList(channel.getGeometry());

            //添加电力线中点图标
            double centerLat = (list.get(0).latitude+list.get(list.size()-1).latitude)/2;
            double centerLon = (list.get(0).longitude+list.get(list.size()-1).longitude)/2;
            LatLng centerPoi = new LatLng(centerLat,centerLon);

            Marker marker = amap.addMarker(GdGeometryOptions.getMarkerOptions(centerPoi, BitmapDescriptorFactory.fromResource(R.mipmap.location_channel)));
            marker.setObject(channel);
            marker.setTitle(MyString.maker_title_pole_chn);
            mChnCenterMarks.add(marker);
        }

        //添加塔杆和文字注记
        for (PoleTableDataClass pole:poles) {
            List<LatLng> list = WKT.POINTWktToPtsList(pole.Geometry);
            Marker marker = amap.addMarker(GdGeometryOptions.getMarkerOptions(list.get(0), BitmapDescriptorFactory.fromResource(R.mipmap.location_pole)));
            marker.setObject(pole);
            marker.setTitle(MyString.maker_title_pole_chn);
            mPoiMarks.add(marker);

            Text text = amap.addText(GdGeometryOptions.getTextOptions(list.get(0), pole.getPoleName()));
            //text.setObject(pole.getPoleNumber());
            text.setFontSize(36);
            text.setBackgroundColor(Color.WHITE);
            text.setFontColor(Color.RED);
            text.setAlign(Text.ALIGN_LEFT, Text.ALIGN_TOP);

            mPoiTexts.add(text);
        }
    }


    /**
     * 添加杆塔信息到地图
     * @param amap
     */
    public void addPoleToMap(AMap amap){
        //添加塔杆和文字注记
        for (PoleTableDataClass pole:poles) {
            List<LatLng> list = WKT.POINTWktToPtsList(pole.Geometry);
            Marker marker = amap.addMarker(GdGeometryOptions.getMarkerOptions(list.get(0), BitmapDescriptorFactory.fromResource(R.mipmap.location_pole)));

            marker.setObject(pole);
            marker.setTitle(MyString.maker_title_pole_chn);
            mPoiMarks.add(marker);

            Text text = amap.addText(GdGeometryOptions.getTextOptions(list.get(0), pole.getPoleName()));
            //text.setObject(pole.getPoleNumber());
            text.setFontSize(36);
            text.setBackgroundColor(Color.WHITE);
            text.setFontColor(Color.RED);
            text.setAlign(Text.ALIGN_LEFT, Text.ALIGN_TOP);

            mPoiTexts.add(text);
        }
    }


    /**
     * 添加通道中心点到地图
     * @param amap
     */
    public void addChannelToMap(AMap amap) {
        //添加通道中点信息
        for(ChannelTableDataClass channel:channels){
            //添加电力线
            List<LatLng> list = WKT.POLYLINEWktToPtsList(channel.getGeometry());

            //添加通道中点图标
            double centerLat = (list.get(0).latitude+list.get(list.size()-1).latitude)/2;
            double centerLon = (list.get(0).longitude+list.get(list.size()-1).longitude)/2;
            LatLng centerPoi = new LatLng(centerLat,centerLon);

            Marker marker = amap.addMarker(GdGeometryOptions.getMarkerOptions(centerPoi, BitmapDescriptorFactory.fromResource(R.mipmap.location_channel)));
            marker.setObject(channel);
            marker.setTitle(MyString.maker_title_pole_chn);
            mChnCenterMarks.add(marker);
        }
    }


    public void removeChannelFromMap() {
        for (Marker mark : mChnCenterMarks) {
            mark.remove();
        }
        mChnCenterMarks.clear();
    }

    public void removePoleFromMap() {
        for (Marker mark : mPoiMarks) {
            mark.remove();
        }
        for (Text text : mPoiTexts) {
            text.remove();
        }

        mPoiMarks.clear();
        mPoiTexts.clear();
    }



    @Override
    public void removeFromMap() {

        for (Marker mark : mPoiMarks) {
            mark.remove();
        }
        for (Text text : mPoiTexts) {
            text.remove();
        }
        for (Polyline polyline : mPolylines) {
            polyline.remove();
        }

        for (Marker mark : mChnCenterMarks) {
            mark.remove();
        }

        mPoiMarks.clear();
        mPoiTexts.clear();
        mPolylines.clear();
        mChnCenterMarks.clear();
    }

//    public void zoomToSpan() {
//        if (mPois != null && mPois.size() > 0) {
//            if (mAMap == null)
//                return;
//            LatLngBounds bounds = getLatLngBounds();
//            mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
//        }
//    }

//    private LatLngBounds getLatLngBounds() {
//        LatLngBounds.Builder b = LatLngBounds.builder();
//        for (int i = 0; i < mPois.size(); i++) {
//            b.include(new LatLng(mPois.get(i).latitude, mPois.get(i).longitude));
//        }
//        return b.build();
//    }
//
//
//    protected BitmapDescriptor getBitmapDescriptor(int index) {
//        return null;
//    }
//
//    public int getPoiIndex(Marker marker) {
//        for (int i = 0; i < mPoiMarks.size(); i++) {
//            if (mPoiMarks.get(i).equals(marker)) {
//                return i;
//            }
//        }
//        return -1;
//    }

}
