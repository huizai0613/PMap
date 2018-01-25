package xxzx.publicClass;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ch on 2016/3/2.
 */
public class ReadFileClass {

    /**
     * 读取文件数据
     * @return
     */
    public static String getString(String path) {
        String value="";
        try {
            InputStream instream = new FileInputStream(path);
            if (instream != null) {
                InputStreamReader inputreader = new InputStreamReader(instream, "UTF-8");
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                //分行读取
                while ((line = buffreader.readLine()) != null) {
                    value = value + line.trim();
                }
                instream.close();
            }
        } catch (java.io.FileNotFoundException e) {
            Log.d("InputDataTxtItemView", "The File doesn't not exist.");
        } catch (IOException e) {
            Log.d("InputDataTxtItemView", e.getMessage());
        }
        finally {
            return value;
        }
    }
}
