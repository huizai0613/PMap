package xxzx.dangerRadar;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import xxzx.myOverlay.DangerRadarOverlay;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.ToastUtil;
import xxzx.publicClass.geometryJson.WKT;
import xxzx.spatialite.TableOptClass.ChannelDangerTableOpt;
import xxzx.spatialite.TableOptClass.PoleDangerTableOpt;
import xxzx.spatialite.TableOptClass.PoleTableOpt;
import xxzx.spatialite.TableStruct.ChannelDangerTableDataClass;
import xxzx.spatialite.TableStruct.PoleTableDataClass;

/**
 * Created by Lenovo on 2017/6/6.
 */

public class DangerRadarClass {

    private Context mContext;
    private PoleTableOpt poleTableOpt;
    private PoleDangerTableOpt poleDangerTableOpt;
    private ChannelDangerTableOpt channelDangerTableOpt;

    private DangerRadarOverlay dangerRadarOverlay;
    private AMap amap;

    double offset = 0.0;

    AMapLocation location = null;


    public DangerRadarClass(Context context) {

        this.mContext = context;

        this.poleTableOpt = new PoleTableOpt();
        this.poleDangerTableOpt = new PoleDangerTableOpt();
        this.channelDangerTableOpt = new ChannelDangerTableOpt();
        this.amap = MySingleClass.getInstance().getBaseMapViewClass().getMapView().getMap();
        this.dangerRadarOverlay = new DangerRadarOverlay();

        this.offset = MyString.distance_danger_radar;
    }

    public void addDangersToMap() {



        this.location = MySingleClass.getInstance().getMyGdLocation();
        if (this.location == null) {
            ToastUtil.show( this.mContext,"当前没有成功定位");
            return;
        }

        //实验用
        this.location.setLatitude(33.7872);
        this.location.setLongitude(116.8296);

        List<LatLng> regionPois = getRegion();

        List<PoleTableDataClass> list_pole = getPoleDangers(regionPois);
        List<ChannelDangerTableDataClass> list_chndanger = getChnDangers(regionPois);

        dangerRadarOverlay.setDataToMap(list_pole,list_chndanger,amap);

        //MySingleClass mySingleClass = MySingleClass.getInstance();
        //this.overlayMap = new HashMap<>();
        //获得自定义图层列表
        //overlayMap = mySingleClass.getOverlayMap();
    }


    private List<LatLng> getRegion(){

        double minLon=location.getLongitude()-offset;
        double minLat=location.getLatitude()-offset;
        double maxLon=location.getLongitude()+offset;
        double maxLat=location.getLatitude()+offset;

        LatLng latLng1=new LatLng(minLat,minLon);
        LatLng latLng2=new LatLng(maxLat,minLon);
        LatLng latLng3=new LatLng(maxLat,maxLon);
        LatLng latLng4=new LatLng(minLat,maxLon);
        LatLng latLng5=new LatLng(minLat,minLon);

        List<LatLng> list=new ArrayList<>();
        list.add(latLng1);
        list.add(latLng2);
        list.add(latLng3);
        list.add(latLng4);
        list.add(latLng5);

        return list;
    }




    private List<PoleTableDataClass> getPoleDangers(List<LatLng> list){

        String polygonWKT = WKT.PointListToPolygonWKT(list);
        List<PoleTableDataClass> poles = poleTableOpt.getRowWithinGeometry(polygonWKT);
        return poles;
    }

    private List<ChannelDangerTableDataClass> getChnDangers(List<LatLng> list){

        String polygonWKT = WKT.PointListToPolygonWKT(list);
        List<ChannelDangerTableDataClass> dangers = channelDangerTableOpt.getRowWithinGeometry(polygonWKT);
        return dangers;
    }





}
