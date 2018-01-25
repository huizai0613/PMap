package xxzx.myBdLocation;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import xxzx.publicClass.geometryJson.WKT;
import xxzx.spatialite.TableOptClass.PoleTableOpt;
import xxzx.spatialite.TableStruct.PoleTableDataClass;

/**
 * Created by ch on 2016/5/25.
 * 到位查询类，根据当前的坐标位置查询杆塔表信息，查询离定位位置一定距离的杆塔信息，也就是到位杆塔信息
 */
public class PlaceQueryClass {

    PoleTableOpt poleTableOpt;
    private double offset=0.00045;

    public PlaceQueryClass(){
        poleTableOpt=new PoleTableOpt();

    }


    /**
     * 杆塔查询
     */
    public int queryPoleObjectId(double lon,double lat){
//        lon=116.5863 ;
//        lat=31.6350;
        double minLon=lon-offset;
        double minLat=lat-offset;
        double maxLon=lon+offset;
        double maxLat=lat+offset;

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

        String polygonWKT = WKT.PointListToPolygonWKT(list);
        List<PoleTableDataClass> poles = poleTableOpt.getRowWithinGeometry(polygonWKT);
        if(poles.size()>0){
            return poles.get(0).getPoleObjectId();
        }else{
            return -1;
        }
    }


    /**
     * 上传到位杆塔信息
     */
    public void updatePlacePoles(){

    }



}
