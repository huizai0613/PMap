package xxzx.mapLayerOpt;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.TileOverlay;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.maps.model.TileProvider;
import com.amap.api.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;

import xxzx.publicClass.MyString;

/**
 * Created by ch on 2016/2/13.
 */
public class GoogleTileOverlay {

    public static TileOverlay addToMap(AMap aMap,String url,String savePath) {
        TileOverlay tileOverlay=null;
        TileProvider tileProvider = getTileProvider(url,savePath);

        if (tileProvider != null) {
            tileOverlay = aMap.addTileOverlay(new TileOverlayOptions()
                    .tileProvider(tileProvider)
                    .diskCacheDir(MyString.maps_cache_tile_folder_path)
                    .diskCacheEnabled(true)
                    .memoryCacheEnabled(false)
                    .zIndex(0)
            );
        }
        return tileOverlay;
    }

    private static TileProvider getTileProvider(String url,String savePath) {
        return new GoogleTileProvider(url,savePath);
    }





    /**
      * 显示第几层的tileOverlay
      */
//    private TileOverlay showTileOverlay(final int floor) {
//
//        if (tileOverlay != null) {
//
//            tileOverlay.remove();
//
//        }
//        TileProvider tileProvider = new UrlTileProvider(256, 256) {
//
//            public URL getTileUrl(int x, int y, int zoom) {
//
//                try {
//
//                    return new URL(String.format(url, x, y, zoom, floor));
//
//                } catch (MalformedURLException e) {
//
//                    e.printStackTrace();
//
//                }
//                return null;
//
//            }
//
//        };
//        if (tileProvider != null) {
//
//            tileOverlay = aMap.addTileOverlay(new TileOverlayOptions()
//                    .tileProvider(tileProvider)
//                    .diskCacheDir("/storage/amap/cache").diskCacheEnabled(true)
//                    .diskCacheSize(100));
//        }
//
//        return tileOverlay;
//    }

}
