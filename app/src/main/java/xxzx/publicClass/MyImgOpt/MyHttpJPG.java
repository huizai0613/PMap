package xxzx.publicClass.MyImgOpt;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mulin on 2015/11/30.
 */
public class MyHttpJPG {

    private static final String TAG="MyHttpRequst";
    private static final int timeout=5000;


    /**
     * Get image from newwork
     * @param imgurl The path of image
     * @return InputStream
     * @throws Exception
     */
    public static InputStream getImageStream(String imgurl) throws Exception{
        URL url = new URL(imgurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(timeout);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
            return conn.getInputStream();
        }
        return null;
    }


}
