package xxzx.deletePlineData;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import xxzx.myOverlay.PowelineOverLayOpt;
import xxzx.publicClass.MyFile;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.spatialite.TableOptClass.BaseTableOpt;
import xxzx.spatialite.TableOptClass.ChannelDangerTableOpt;
import xxzx.spatialite.TableOptClass.ChannelTableOpt;
import xxzx.spatialite.TableOptClass.CreateSysTable;
import xxzx.spatialite.TableOptClass.DangerSgTableOpt;
import xxzx.spatialite.TableOptClass.PoleDangerTableOpt;
import xxzx.spatialite.TableOptClass.PoleTableOpt;
import xxzx.spatialite.TableOptClass.PowerlineTableOpt;
import xxzx.spatialite.TableStruct.BaseDangerTableDataClass;
import xxzx.spatialite.TableStruct.ChannelDangerTableDataClass;
import xxzx.spatialite.TableStruct.PoleDangerTableDataClass;

/**
 * Created by ch on 2017/1/18.
 */
public class PlineDeleteClass {

    public PlineDeleteClass(){

    }

    public static boolean deleteAllPlines() {
        //删除表数据
        clearTableData();
        //删除图片
        deleteAllImage();

        MySingleClass mySingleClass = MySingleClass.getInstance();
        //获得电力线图层处理类
        PowelineOverLayOpt powelineOverLayOpt = mySingleClass.getBaseMapViewClass().getPowelineOverLayOpt();
        //移除地图线路
        powelineOverLayOpt.removePlineOverlay();
        //移除杆塔和通道
        powelineOverLayOpt.removePoleAndChannel();

        return true;
    }

    public static boolean DeletePlines(List<String> plinelist) {

        MySingleClass mySingleClass = MySingleClass.getInstance();
        //可以删除的线路图片
        List<String> picsList = getPlineDangerPics(plinelist);
        //删除表数据
        boolean result = deleteTableRow(plinelist);
        if (result) {
            //删除隐患图片
            deleteDangerPics(picsList);
            //删除塔杆图片
            deletePolePics(plinelist);

            //获得电力线图层处理类
            PowelineOverLayOpt powelineOverLayOpt = mySingleClass.getBaseMapViewClass().getPowelineOverLayOpt();
            //删除电力线路
            powelineOverLayOpt.removePlineOverlay();
            //删除杆塔和通道
            powelineOverLayOpt.removePoleAndChannel();
            //添加电力线
            powelineOverLayOpt.addPlineOverlay();

            return true;
        }else{
            return false;
        }
    }


    /**
     * 删除图片
     * @param
     */
    public void deletePics(List<String> picNames,String folderPath){
        for (String name:picNames){
            if(!TextUtils.isEmpty(name)){
                MyFile.deleteFolder(folderPath+"/"+name);
            }
        }
    }


    /**
     * 清空数据表中的数据
     */
    private static void  clearTableData(){
        CreateSysTable createSysTable = new CreateSysTable();
        createSysTable.deleteAll();
        createSysTable.create();
    }


    private static boolean deleteTableRow(List<String> plinelist) {

        //删除杆塔隐患表
        PoleDangerTableOpt poleDangerTableOpt = new PoleDangerTableOpt();
        //删除通道隐患表
        ChannelDangerTableOpt chnDangerTableOpt = new ChannelDangerTableOpt();
        //删除杆塔表
        PoleTableOpt poleTableOpt = new PoleTableOpt();
        //删除通道表
        ChannelTableOpt chnTableOpt = new ChannelTableOpt();
        //删除线路表
        PowerlineTableOpt powerlineTableOpt = new PowerlineTableOpt();
        //施工隐患表
        DangerSgTableOpt dangerSgTableOpt = new DangerSgTableOpt();

        poleDangerTableOpt.BEGIN();

        int result1 = poleDangerTableOpt.delete(plinelist);
        int result2 = chnDangerTableOpt.delete(plinelist);
        int result3 = poleTableOpt.delete(plinelist);
        int result4 = chnTableOpt.delete(plinelist);
        int result5 = powerlineTableOpt.delete(plinelist);
        int result6 = dangerSgTableOpt.delete(plinelist);


        if (result1 == 1 && result2 == 1 && result3 == 1 && result4 == 1 && result5 == 1 &&result6==1) {
            poleDangerTableOpt.COMMIT();

            //紧缩数据库
//            ((BaseTableOpt)poleDangerTableOpt).VACUUM();
//            ((BaseTableOpt)chnDangerTableOpt).VACUUM();
//            ((BaseTableOpt)poleTableOpt).VACUUM();
//            ((BaseTableOpt)chnTableOpt).VACUUM();
//            ((BaseTableOpt)powerlineTableOpt).VACUUM();

            return true;
        } else {

            poleDangerTableOpt.ROLLBACK();
            return false;
        }
    }



    private static void deleteAllImage(){
        MyFile.deleteFolder(MyString.image_pline_pole_folder_path);
        MyFile.deleteFolder(MyString.image_pline_danger_folder_path);
        MyFile.deleteFolder(MyString.image_temp_folder_path);
    }


    /**
     * 获得线路的所有隐患图片
     * @param plinenames
     * @return
     */
    private static List<String> getPlineDangerPics(List<String> plinenames){

        List<String> picsList=new ArrayList<>();
        PoleDangerTableOpt poleDangerTableOpt = new PoleDangerTableOpt();
        ChannelDangerTableOpt chnDangerTableOpt = new ChannelDangerTableOpt();

        for(String item:plinenames){
            List<PoleDangerTableDataClass> list_poledanger2 = poleDangerTableOpt.getRowFormPlineName(item);
            //获取杆塔所有图片名称信息
            for(PoleDangerTableDataClass poledanger:list_poledanger2){
                String pics = poledanger.getPicsJson();
                String[] picslist=pics.split(";");
                for (String str: picslist) {
                    if (!TextUtils.isEmpty(str))
                        picsList.add(str);
                }
            }

            List<ChannelDangerTableDataClass> list_chndanger2 = chnDangerTableOpt.getRowFormPlineName(item);

            //获取杆塔隐患点所有图片名称信息
            for(ChannelDangerTableDataClass chndanger:list_chndanger2){
                String pics = chndanger.getPicsJson();
                String[] picslist=pics.split(";");
                for (String str: picslist) {
                    if (!TextUtils.isEmpty(str))
                        picsList.add(str);
                }
            }
        }
        return picsList;
    }


    /**
     * 获得含有未上传隐患的线路名称
     * @param plinenames
     * @return
     */
    public static String getNoDeletePlines(List<String> plinenames){

        StringBuilder builder = new StringBuilder("");

        PoleDangerTableOpt poleDangerTableOpt = new PoleDangerTableOpt();
        ChannelDangerTableOpt chnDangerTableOpt = new ChannelDangerTableOpt();
        for(String item:plinenames){
            List<PoleDangerTableDataClass> list_poledanger = poleDangerTableOpt.getRowFromUpdate2(item);

            if (list_poledanger.size() != 0) {
                builder.append(item);
                builder.append(",");
                continue;
            }

            List<ChannelDangerTableDataClass> list_chn = chnDangerTableOpt.getRowFromUpdate2(item);
            if (list_chn.size() != 0) {
                builder.append(item);
                builder.append(",");
                continue;
            }
        }
        return builder.toString();
    }

    public static void deleteCrashFile(){
        MyFile.deleteFolder(MyString.crash_folder_path);
    }

    public static void deleteGpsFile(){
        MyFile.deleteFolder(MyString.gps_folder_path);
    }

    /**
     * 删除图片信息
     * @param picnames
     */
    private static void deleteDangerPics(List<String> picnames){
        for (String picname:picnames){
            if(!TextUtils.isEmpty(picname)){
                MyFile.deleteFolder(MyString.image_pline_danger_folder_path + "/" + picname);
            }
        }
    }

    /**
     * 删除杆塔六张图片
     * @param plineNames
     */
    private static void deletePolePics(List<String> plineNames){
        for (String name:plineNames){
            if(!TextUtils.isEmpty(name)){
                MyFile.deleteFolder(MyString.image_pline_pole_folder_path+"/"+name);
            }
        }
    }



}
