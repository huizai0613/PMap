package xxzx.myOverlay;

import com.amap.api.maps.AMap;

/**
 * Created by ch on 2016/2/10.
 */
public abstract class MyBaseOverlay {
    public abstract void addToMap(AMap amap);
    public abstract void removeFromMap();
//    public abstract void setMap(AMap amap);
//    public abstract void addToMap();
}
