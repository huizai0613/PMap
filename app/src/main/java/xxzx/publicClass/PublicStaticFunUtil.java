package xxzx.publicClass;

import com.amap.api.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ch on 2016/5/26.
 */
public class PublicStaticFunUtil {

    /**
     * 获取24小时制时间
     * @return
     */
    public static String getCurrentDate(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }




}
