package xxzx.publicClass;

import android.graphics.Point;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;

import java.util.List;

/**
 * Created by ch on 2016/2/27.
 * 高德地图的一些常用的处理函数
 */
public class GdMapTool {


    /**
     * 放大到点序列
     * @param aMap
     * @param mPois
     */
    public static void zoomToSpan(AMap aMap,List<LatLng> mPois) {
        if (mPois != null && mPois.size() > 0) {
            if (aMap == null)
                return;
            LatLngBounds bounds = getLatLngBounds(mPois);
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
        }
    }


    /**
     * 获取所有点的集合的最小矩形
     *
     * @param mPois
     * @return
     */
    public static LatLngBounds getLatLngBounds(List<LatLng> mPois) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < mPois.size(); i++) {
            b.include(new LatLng(mPois.get(i).latitude, mPois.get(i).longitude));
        }
        return b.build();
    }


    /**
     * 获得中心点坐标
     * @param list
     * @return
     */
    public static LatLng getCenterPoi(List<LatLng> list){
        if(list.size()==0){
            return null;
        }

        double lat=0.0;
        double lng = 0.0;

        for(LatLng poi:list){
            lat = lat + poi.latitude;
            lng = lng + poi.longitude;
        }

        lat = lat/list.size();
        lng = lng/list.size();

        return new LatLng(lat,lng);
    }

}
