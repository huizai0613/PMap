package xxzx.publicClass;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.TextOptions;

import java.util.List;

import xxzx.activity.R;

/**
 * Created by ch on 2016/2/26.
 */
public class GdGeometryOptions {


    public static TextOptions getTextOptions(LatLng latlng, String title) {
        return new TextOptions()
                .position(
                        new LatLng(latlng.latitude, latlng.longitude))
                .text(title)
                .align(100, 1)
                .zIndex(1000)
                .backgroundColor(Color.argb(0,0,0,0))
                ;
    }


    public static MarkerOptions getMarkerOptions(LatLng latlng,BitmapDescriptor bitmapDescriptor) {
        return new MarkerOptions()
                .position(
                        new LatLng(latlng.latitude, latlng.longitude))
                .icon(bitmapDescriptor)
                .anchor((float) 0.5, (float) 0.5);//中点
    }


    public static PolylineOptions getPolylineOptions(List<LatLng> mPois,int color){
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(6);
        polylineOptions.color(color);
        polylineOptions.zIndex(1000);
        polylineOptions.addAll((Iterable<LatLng>) mPois);
        return polylineOptions;
    }

    public static PolygonOptions getPolygonOptions(List<LatLng> mPois,int stokerColor,int fillColor){
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.strokeColor(stokerColor);
        polygonOptions.strokeWidth(5);
        polygonOptions.fillColor(fillColor);
        polygonOptions.zIndex(1000);
        polygonOptions.addAll((Iterable<LatLng>) mPois);
        return polygonOptions;
    }

    public static CircleOptions getCircleOptions(LatLng centerPt,double radius,int stokerColor,int fillColor){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(centerPt);
        circleOptions.radius(radius);
        circleOptions.strokeColor(stokerColor);
        circleOptions.strokeWidth(5);
        circleOptions.fillColor(fillColor);
        circleOptions.zIndex(1000);
        return circleOptions;
    }





}
