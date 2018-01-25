package xxzx.editData;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import xxzx.publicClass.MyString;


/**
 * Created by mulin on 2015/11/9.
 */
public class EditDataJsonFileClass {

    private Context mContext = null;
    private List<Integer> taskidSelect = null;
    public ArrayList<EditDataInfo> editDataInfoArray = null;

    //构造函数
    public EditDataJsonFileClass(Context context) {
        this.mContext = context;
        this.editDataInfoArray=new ArrayList<EditDataInfo>();
        //loadingDialog = new LoadingDialog(mContext);
    }


    /**
     * 刪除当前选中任务
     */
    public void deleteTaskInfos() {
        for (int id : taskidSelect) {

            EditDataInfo editDataInfo = editDataInfoArray.get(id);
            editDataInfoArray.remove(id);

            //删除图片信息
            for (String name : editDataInfo.getImgs()) {
                String path = MyString.edit_photo_folder_path + "/" + name;
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }


    /**
     * 获得任务文件里的任务信息，并转化为列表
     *
     * @return
     */
    public void ReadSDTaskJsonFile() {
        //读取json数据字符串
        String jsonStr = this.readJsonFile(MyString.edit_my_file_path);
        //如果为空
        if (jsonStr.trim().equals("")) {
            return;
        }
        Gson gson =new Gson();
        //数据转换
        editDataInfoArray = gson.fromJson(jsonStr, new TypeToken<List<EditDataInfo>>(){}.getType());
    }


    /**
     * 根据现在任务的图片信息，删除photo文件夹中多余的图片
     */
    public void deleteUnnecessaryPhotoImages() {
        List<String> allImags = new ArrayList<String>();
        for (int i = 0; i < this.editDataInfoArray.size(); i++) {
            EditDataInfo editDataInfo = this.editDataInfoArray.get(i);
            if (editDataInfo.getImgs().size() > 0) {
                for (String imgName : editDataInfo.getImgs()) {
                    allImags.add(imgName);
                }
            }
        }

        File path = new File(MyString.edit_photo_folder_path);
        if (path.isDirectory()) {
            //返回文件夹中有的数据
            File[] files = path.listFiles();
            for (File file : files) {
                if (file.getName().toLowerCase().endsWith(".jpg")) {

                    if (!allImags.contains(file.getName())) {
                        //删除
                        file.delete();
                    }
                }
            }
        }
    }


    /**
     * 根据路径写json文件
     */
    public void writeSDTaskJsonFile() {
        Gson gson=new Gson();
        File file = new File(MyString.edit_my_file_path);
        //创建task根目录
        File folder = new File(MyString.edit_folder_path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(gson.toJson(editDataInfoArray).getBytes());
            out.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    //----------------------------私有函数----------------------------

    /**
     * 根据路径读取json文件
     *
     * @param path
     */
    private String readJsonFile(String path) {

        File file = new File(path);
        String JsonValue = "";
        if (!file.exists()) {
            return JsonValue;
        }
        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file),"UTF-8");

            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTXT = null;
            while ((lineTXT = bufferedReader.readLine()) != null) {
                JsonValue+=lineTXT.toString();
            }
            read.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonValue;
    }
}
