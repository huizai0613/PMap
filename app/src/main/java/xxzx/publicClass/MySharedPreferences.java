package xxzx.publicClass;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.text.DecimalFormat;

/**
 * Created by ch on 2016/6/5.
 */
public class MySharedPreferences
{

    public static void addOfflineTileSize(double size)
    {
        Context context = MySingleClass.getInstance().getmContext();
        //获取SharedPreferences对象
        SharedPreferences sp = context.getSharedPreferences("PMAP", context.MODE_PRIVATE);
        String str = sp.getString("OFFLINE_TILESIZE_KEY", "");
        double dou = TextUtils.isEmpty(str) ? 0.0 : Double.valueOf(str);
        dou = dou + size;
        DecimalFormat df = new DecimalFormat("0.00");
        //添加
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("OFFLINE_TILESIZE_KEY", df.format(dou));
        editor.commit();

    }


    public static void putOfflineTileSize(double size)
    {
        Context context = MySingleClass.getInstance().getmContext();
        //获取SharedPreferences对象
        SharedPreferences sp = context.getSharedPreferences("PMAP", context.MODE_PRIVATE);
        DecimalFormat df = new DecimalFormat("0.00");
        //添加
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("OFFLINE_TILESIZE_KEY", df.format(size));
        editor.commit();
    }


    public static double getOfflineTileSize()
    {
        Context context = MySingleClass.getInstance().getmContext();
        //获取SharedPreferences对象
        SharedPreferences sp = context.getSharedPreferences("PMAP", context.MODE_PRIVATE);
        String size = sp.getString("OFFLINE_TILESIZE_KEY", "");
        double result = TextUtils.isEmpty(size) ? 0.0 : Double.valueOf(size);
        return result;
    }


}
