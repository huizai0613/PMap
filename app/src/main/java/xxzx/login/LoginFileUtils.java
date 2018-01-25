package xxzx.login;

/**
 * Created by mulin on 2015/11/15.
 */

import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONObject;


import com.google.gson.Gson;

import xxzx.publicClass.MyString;


public class LoginFileUtils {
    private static final String FILENAME = MyString.login_setting_file_path; // 用户保存文件名
    private static final String TAG = "LoginFileUtils";
    /**
     * 根据路径写json文件
     * 返回true说明写文件成功，false表示写文件失败
     */
    public static Boolean writeLoginJsonFile( User user) {
        File file = new File(FILENAME);
        try {
            FileOutputStream out = new FileOutputStream(file);
            String ss=user.toJSON().toString();
            out.write(user.toJSON().toString().getBytes());
            out.close();
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获得任务文件里的案件信息，并转化为列表
     * @return
     */
    public static User readLoginJsonFile(){
        User user = null;
        //读取json数据字符串
        String jsonStr = readJsonFile(FILENAME);
        //如果为空
        if (jsonStr.trim().equals("")) {
            //如果为空
            return null;
        }
        //获取当地的配置文件
        try {
            JSONObject json=new JSONObject(jsonStr);
            user = new User(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }


    /**
     * 根据路径读取用户信息的json文件
     *
     */
    /**
     * 根据路径读取json文件
     * @param path
     */
    private static String readJsonFile(String path) {

        File file = new File(path);
        String JsonValue = "";
        if (!file.exists()) {
            return JsonValue;
        }

        try {
            FileInputStream fin = new FileInputStream(file);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            JsonValue = new String(buffer);
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonValue;
    }
}
