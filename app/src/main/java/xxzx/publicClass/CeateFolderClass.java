package xxzx.publicClass;

/**
 * Created by ch on 2016/1/18.
 */
public class CeateFolderClass {

    private final int ERROR=-1;//创建错误
    private final int EXIST=0;//已经存在
    private final int EXIST_UN=1;//不存在，创建成功


    /**
     * 创建文件夹
     */
    public static void CeateAppFolder() {
        MyFile.createFolder(MyString.root_folder_path);

        MyFile.createFolder(MyString.crash_folder_path);

        int result1=MyFile.createFolder(MyString.maps_cache_google_img_folder_path);
        int result2=MyFile.createFolder(MyString.maps_cache_google_img_anno_folder_path);
        int result3=MyFile.createFolder(MyString.maps_cache_gaode_vec_folder_path);
        //如果影像文件夹不存在，存储标示为0
        if(result1==1||result2==1){
            MySharedPreferences.putOfflineTileSize(0.00);
        }

        MyFile.createFolder(MyString.maps_cache_tile_folder_path);

        MyFile.createFolder(MyString.gps_folder_path);

        MyFile.createFolder(MyString.download_pline_folder_path);

        MyFile.createFolder(MyString.input_folder_path);

        MyFile.createFolder(MyString.system_folder_path);
        MyFile.createFolder(MyString.config_folder_path);

        MyFile.createFolder(MyString.image_pline_danger_folder_path);
        MyFile.createFolder(MyString.image_pline_pole_folder_path);
        MyFile.createFolder(MyString.image_temp_folder_path);
    }
}
