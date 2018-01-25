package xxzx.myData;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;

import xxzx.myData.dataInput.DataJsonPointClass;
import xxzx.publicClass.MyString;
import xxzx.spatialite.TableStruct.PowerlineTableDataClass;

/**
 * Created by ch on 2016/6/15.
 */
public class ReadHttpResultJson {

    public int readAndWriteTxt(String result){
        try {
            JSONObject json = new JSONObject(result);
            JSONArray data = json.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                JSONObject plineObject = data.getJSONObject(i);
                String plinename = plineObject.getString("plinename");
                //保存线路文件
                this.savePlineFile(plinename, plineObject);
            }
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }


    //保存线路的txt文件
    private void savePlineFile(String plinname, JSONObject json){

        FileOutputStream fileOutputStream = null;
        try {
            if (fileOutputStream == null) {
                String fileName = MyString.download_pline_folder_path + "/" + plinname + ".txt";
                File file = new File(fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                //false 为覆盖，true为追加
                fileOutputStream = new FileOutputStream(file, false);
            }
            fileOutputStream.write(json.toString().getBytes());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

