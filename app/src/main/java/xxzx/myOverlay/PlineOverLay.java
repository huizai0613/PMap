package xxzx.myOverlay;

import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;

import xxzx.publicClass.GdGeometryOptions;
import xxzx.publicClass.geometryJson.WKT;
import xxzx.spatialite.TableStruct.PowerlineTableDataClass;

/**
 * Created by ch on 2016/2/9.
 */
public class PlineOverLay extends MyBaseOverlay {

    private List<PowerlineTableDataClass> powerlines;
    //记录线数据
    private ArrayList<Polyline> mPolylines = new ArrayList<Polyline>();

    public PlineOverLay() {

    }

    /**
     * 设置图层数据
     */
    public void setData(List<PowerlineTableDataClass> powerlines) {
        this.powerlines = powerlines;
    }
    @Override
    public void addToMap(AMap amap) {
        //添加线
        for(PowerlineTableDataClass powerline:powerlines) {
//            List<LatLng> list_pline = WKT.POLYLINEWktToPtsList(powerline.getGeometry());
//
//
//            if(list_pline==null||list_pline.size()==0){
//                continue;
//            }
//            Polyline mPolyline = amap.addPolyline(GdGeometryOptions.getPolylineOptions(list_pline, Color.RED));
//            mPolyline.setWidth(15);
//            mPolylines.add(mPolyline);


            List<List<LatLng>> list_plines = WKT.MUTILINEWktToPtsList(powerline.getGeometry());
            if(list_plines==null||list_plines.size()==0){
                continue;
            }

            for(List<LatLng> list:list_plines) {

                Polyline mPolyline = amap.addPolyline(GdGeometryOptions.getPolylineOptions(list, Color.RED));
                mPolyline.setWidth(15);
                mPolylines.add(mPolyline);
            }
        }
    }


    @Override
    public void removeFromMap() {

        for (Polyline polyline : mPolylines) {
            polyline.remove();
        }
        mPolylines.clear();
    }

}
