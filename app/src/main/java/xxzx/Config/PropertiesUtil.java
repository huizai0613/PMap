package xxzx.Config;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ch on 2016/2/29.
 */
public class PropertiesUtil {
    public static void  CopeConfig(Context mContext) {
        try {
            AssetCopyer assetCopyer = new AssetCopyer(mContext);
            assetCopyer.copy();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Properties loadConfig(String file) {
        Properties properties = new Properties();
        try {
            FileInputStream s = new FileInputStream(file);
            properties.load(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static Properties loadConfig(InputStream inputStream) {
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public void saveConfig(Context context, String file, Properties properties) {
        try {
            FileOutputStream s = new FileOutputStream(file, false);
            properties.store(s, "");
        } catch (Exception e){
            e.printStackTrace();
        }
    }





}
