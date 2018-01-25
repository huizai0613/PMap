package xxzx.publicClass;

import java.io.File;

public class MyString {

    // sd卡的绝对路径
    public static String sdPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    // 初始文件夹根目录
    public static String root_folder_path = sdPath + "/PMap";

    //google影像切片存储根路径
    public static String maps_cache_google_img_folder_path = root_folder_path + "/titles/img_google";
    //google影像和注记切片存储根路径
    public static String maps_cache_google_img_anno_folder_path = root_folder_path + "/titles/imganno_google";
    //高德矢量离线下载的数据
    public static String maps_cache_gaode_vec_folder_path = root_folder_path + "/vectorCache";
    //运行地图切片过程中的地图缓存文件路径,高德需要
    public static String maps_cache_tile_folder_path = root_folder_path + "/titles/cache";

    //路径文件存储路径
    public static String gps_folder_path = root_folder_path + "/gps";
    //下载的线路文件存储路径
    public static String download_pline_folder_path = root_folder_path + "/download_pline";
    // 程序异常崩溃日志文件夹
    public static String crash_folder_path = root_folder_path + "/crash";

    //系统文件存储路径
    public static String system_folder_path = root_folder_path + "/system";
    //配置文件存放目录
    public static String config_folder_path = system_folder_path + "/config";
    //线路图导入存放文件夹路径
    public static String input_folder_path = root_folder_path + "/input";
    //数据库存储文件
    public static String spatialite_file_path = config_folder_path + "/db.sqlite";

    //系统杆塔点照片存储文件夹
    public static String image_pline_pole_folder_path = system_folder_path+"/photo_pole";
    //系统隐患点照片存储文件夹
    public static String image_pline_danger_folder_path = system_folder_path+"/photo_danger";
    //系统照片临时存储文件夹
    public static String image_temp_folder_path = system_folder_path+"/photo_temp";
    //圖片臨時文件路徑
    public static String image_temp_path = image_temp_folder_path+"/img_temp.jpg";
    // 数据采集根路径
    public static String edit_folder_path = root_folder_path + "/edit";
    // 数据采集根路径
    public static String edit_my_file_path = edit_folder_path + "/data.txt";
    // 数据采集照片文件夹路径
    public static String edit_photo_folder_path = edit_folder_path + "/photo";
    //用户信息存储文件
    public static String login_setting_file_path = config_folder_path + "/user.txt";

    // 电力线表名称
    public static String powerline_table_name = "powerline";
    // 通道表名称
    public static String channel_table_name = "channel";
    // 杆塔隐患点表名称
    public static String channel_danger_table_name = "channeldanger";
    // 杆塔表名称
    public static String pole_table_name = "pole";
    // 杆塔隐患点表名称
    public static String pole_danger_table_name = "poledanger";
    // 施工隐患表名称
    public static String danger_sg_table_name = "danger_sg";
    // 杆塔签到表名称
    public static String pole_place_table_name = "poleplace";

    public static int gps_srid=4326;

    public static String sys_setting_file_path = system_folder_path + "/setting.txt";

    public static String intent_gps_information = "intent_gps_information";

    //内部广播表示
    public static String ACTION_BROADCAST_INTENT_LOGIN="action_broadcast_broadcast_login";
    public static String ACTION_BROADCAST_INTENT_GPS = "action_broadcast_broadcast_gps";

    /*intent传输表示*/
    public static String intent_task_taskinfo = "task.taskinfo";
    //caseinfo传输表示
    public static String intent_case_caseinfo = "case.caseinfo";
    //定义wkid的值，用于图形转换
    public static int wkid = 102100;

    //签到距离，经纬度，大概500米
    public static double distance_place = 0.005;
    //隐患雷达距离，经纬度，大概500米
    public static double distance_danger_radar = 0.01;

    public static int requestCode_mapactivity_to_taskinfoactivity = 1000;
    public static int requestCode_mydataactivity_to_downloadplineactivity = 2000;
    public static int requestCode_activity_to_photoactivity = 3000;
    public static int requestCode_casecreateactivity_to_casemapactivity = 4000;
    public static int requestCode_caselocalactivity_to_casemapactivity = 5000;
    public static int requestCode_mainactivity_to_loginactivity = 6000;

    public static int requestCode_poledangeractivity_to_poledangereditactivity = 7000;
    public static int requestCode_activity_to_publicmapactivity = 8000;
    public static int requestCode_activity_to_dangersgdetailactivity = 9000;
    public static int requestCode_activity_to_permissionsactivity = 10000;//REQUEST_CODE = 0; // 请求码

    //请求IP域名
    public static String httpStr = "http://223.244.255.5";


    //地图图层名称
    public static String base_tilelayer_name = "base_tilelayer_name";
    public static String location_graphiclayer_name = "location_graphiclayer_name";
    public static String input_data_graphiclayer_name = "input_data_graphiclayer_name";
    public static String edit_graphiclayer_name = "edit_graphiclayer_name";

    public static final int SUCCESS=1;
    public static final int FAIL=-1;


    //地图自定义overlay图层Key
    public static int overlay_input_data = 3000;
    public static int overlay_measure = 4000;
    public static int overlay_route=5000;


    public static String intent_map_state_routeplanning="intent_map_state_routeplanning";
    public static String intent_map_state_selectpoi="intent_map_state_selectpoi";
    public static String intent_map_state_selectgeometry="intent_map_state_selectgeometry";
    public static String intent_map_state_localdata="intent_map_state_localdata";
    public static String intent_map_state_showgeometry="intent_map_state_showgeometry";



    public static String[] poleDangerTypes={"1.基础","2.杆塔(含鸟巢等异物)","3.导地线","4.绝缘子串","5.金具","6.接地装置","7.附属设施"};
    public static String[] poleDangerTypes_nonum={"基础","杆塔(含鸟巢等异物)","导地线","绝缘子串","金具","接地装置","附属设施"};

    public static String[] channelDangerTypes={"1.施工隐患","2.树障隐患(苗圃吊装)","3.垂钓隐患","4.异物漂浮隐患","5.山火(秸秆焚烧)隐患","6.违章建筑构筑物","7.覆冰隐患","8.交跨信息","9.其他"};
    public static String[] channelDangerTypes_nonum={"施工隐患","树障隐患(苗圃吊装)","垂钓隐患","异物漂浮隐患","山火(秸秆焚烧)隐患","违章建筑构筑物","覆冰隐患","交跨信息","其他"};

    public static final int DANGER_SG_TYPE=0;
    public static final int DANGER_SZ_TYPE=1;

    public static String[] dangerLevels={"无妨","一般","严重","危急"};
    public static String[] dangerLevels_num={"1.无妨","2.一般","3.严重","4.危急"};

    //用于APK更新下载
    public final static String SAVE_APP_NAME = "pmap-download.apk";
    public final static String SAVE_APP_LOCATION = "/download";
    public final static String APP_FILE_NAME = "/sdcard"+SAVE_APP_LOCATION+ File.separator + SAVE_APP_NAME;


    //用于标识地图Marker的类型，点击事件
    public final static String maker_title_pole_chn = "maker_title_pole_chn";
    public final static String maker_title_danger = "maker_title_danger";
    public final static String maker_title_nearby_user ="maker_title_nearby_user";


    //微信appid
    public final static String WX_APP_ID = "wx9ce8df74b1bc132e";

    /**附近人员参数设置**/
    //距离，单位米
    public final static int nearDistance =50000;
    //时间，单位分钟
    public final static int nearTime =30;

}
