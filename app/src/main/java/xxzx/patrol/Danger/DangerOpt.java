package xxzx.patrol.Danger;

import android.text.TextUtils;

import xxzx.publicClass.MyFile;
import xxzx.publicClass.MyString;
import xxzx.spatialite.TableOptClass.ChannelDangerTableOpt;
import xxzx.spatialite.TableOptClass.DangerSgTableOpt;
import xxzx.spatialite.TableOptClass.PoleDangerTableOpt;
import xxzx.spatialite.TableStruct.ChannelDangerTableDataClass;
import xxzx.spatialite.TableStruct.PoleDangerTableDataClass;

/**
 * Created by ch on 2016/6/17.
 */
public class DangerOpt {

    /**
     * 删除通道隐患
     * @param id
     */
    public static void deleteChnDangerFormRowID(int id){
        ChannelDangerTableOpt channelDangerTableOpt = new ChannelDangerTableOpt();
        ChannelDangerTableDataClass data =(ChannelDangerTableDataClass)channelDangerTableOpt.getRow(id);

        //删除隐患行
        int result=channelDangerTableOpt.delete(id);
        if(result!=1){
            return;
        }

        //删除通道隐患
        if(data.getDangerType()==MyString.DANGER_SG_TYPE){
            int dangerRowid = data.getRowId();
            DangerSgTableOpt dangerSgTableOpt = new DangerSgTableOpt();
            dangerSgTableOpt.deleteFromDangerRowID(dangerRowid);
            if(result!=1){
                return;
            }
        }

        String[] picnames=data.getPicsJson().split(";");
        //删除隐患图片
        deleteDangerPics(picnames);
    }


    /**
     * 删除杆塔隐患
     * @param id
     */
    public static void deletePoleDangerFormRowID(int id){
        PoleDangerTableOpt poleDangerTableOpt = new PoleDangerTableOpt();
        PoleDangerTableDataClass data =(PoleDangerTableDataClass)poleDangerTableOpt.getRow(id);

        //删除隐患行
        int result=poleDangerTableOpt.delete(id);
        if(result!=1){
            return;
        }


        String[] picnames=data.getPicsJson().split(";");
        //删除隐患图片
        deleteDangerPics(picnames);
    }


    private static void deleteDangerPics(String[] picnames){
        for(String picname:picnames){
            if(!TextUtils.isEmpty(picname)){
                MyFile.deleteFolder(MyString.image_pline_danger_folder_path + "/" + picname);
            }
        }
    }


}
