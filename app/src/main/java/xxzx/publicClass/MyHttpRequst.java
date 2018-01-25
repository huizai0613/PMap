package xxzx.publicClass;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mulin on 2015/11/30.
 */
public class MyHttpRequst {

    private static final String TAG="MyHttpRequst";
    private static final int timeout=7000;
    /**
     *
     * @param Url
     * @return
     */
    public static String getHttpGetRequst2(String Url){

        HttpURLConnection conn = null;
        InputStream inputStream = null;
        String result = "";
        try {
            //String u=URLEncoder.encode(Url, "UTF-8");
            //注意乱码
            URL url = new URL(Url);
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            inputStream = conn.getInputStream();
            byte[] buffer = null;
            if(conn.getResponseCode() == 200){
                buffer = new byte[1024];
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int len;
                while ((len = inputStream.read(buffer)) != -1)
                {
                    out.write(buffer, 0, len);
                }
                buffer = out.toByteArray();
                result=new String(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("sjr", "Network-error");
        }finally{
            try {
                if(inputStream != null){
                    inputStream.close();
                }
                if(conn != null){
                    conn.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("sjr","InvokeWebServiceHelper类中释放资源出错");
            }

            return result;
        }
    }

    public static String getHttpPostRequst2(String Url,String postData) {
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        String result = "";
        try {
            //注意乱码
            URL url = new URL(Url);
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setRequestProperty("Content-Length", String.valueOf(postData.getBytes().length));
            conn.getOutputStream().write(postData.getBytes());
            inputStream = conn.getInputStream();
            byte[] buffer = null;
            if(conn.getResponseCode() == 200){
                buffer = new byte[1024];
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int len;
                while ((len = inputStream.read(buffer)) != -1)
                {
                    out.write(buffer, 0, len);
                }
                buffer = out.toByteArray();
            }

            result=new String(buffer);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("sjr", "Network-error");
        }
        finally{
            try {
                if(inputStream != null){
                    inputStream.close();
                }
                if(conn != null){
                    conn.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("sjr","InvokeWebServiceHelper类中释放资源出错");
            }

            return result;
        }
    }
}
