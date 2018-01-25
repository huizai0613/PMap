package xxzx.deletePlineData;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.Patrol.PoleDangerStatisticsActivity;
import xxzx.download.MultiThreadBaseClass;
import xxzx.download.Plines.DownloadPlinesClass;
import xxzx.download.Plines.WriteDatabaseClass;
import xxzx.myOverlay.PowelineOverLayOpt;
import xxzx.publicClass.MyFile;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.ToastUtil;
import xxzx.spatialite.TableOptClass.ChannelDangerTableOpt;
import xxzx.spatialite.TableOptClass.ChannelTableOpt;
import xxzx.spatialite.TableOptClass.CreateSysTable;
import xxzx.spatialite.TableOptClass.DangerSgTableOpt;
import xxzx.spatialite.TableOptClass.PoleDangerTableOpt;
import xxzx.spatialite.TableOptClass.PoleTableOpt;
import xxzx.spatialite.TableOptClass.PowerlineTableOpt;

/**
 * Created by ch on 2017/1/20.
 */
public class PlineUpdateClass {

    private Context mContext;
    private DownloadPlinesClass downloadPlinesClass;
    private WriteDatabaseClass writeDatabaseClass;

    public List<String> list_plinename = null;
    public List<Integer> isInMapList = null;
    public List<Integer> isTaskList = null;

    public PlineUpdateClass(Context context){

        this.mContext = context;
        this.downloadPlinesClass = new DownloadPlinesClass(context);
        this.writeDatabaseClass = new WriteDatabaseClass(context);


        this.downloadPlinesClass.setOnCompleted(new MultiThreadBaseClass.ICoallBack() {
            @Override
            public void onCompleted(boolean result) {
                if(result) {
                    //删除所有表数据
                    PlineDeleteClass.deleteAllPlines();
                    //重新写入数据库
                    writeDatabaseClass.WritePlines(list_plinename);
                }else{
                    ToastUtil.show(mContext, "线路下载失败");
                }
            }
        });

        this.writeDatabaseClass.setOnCompleted(new MultiThreadBaseClass.ICoallBack() {
            @Override
            public void onCompleted(boolean result) {
                //如果写入成功
                if (result) {
                    PowerlineTableOpt powerlineTableOpt = new PowerlineTableOpt();
                    PoleTableOpt poleTableOpt = new PoleTableOpt();
                    ChannelTableOpt channelTableOpt = new ChannelTableOpt();

                    //修改是否导入地图
                    for (int i = 0; i < isInMapList.size(); i++) {
                        if (isInMapList.get(i) == 1) {
                            powerlineTableOpt.updateInMap(list_plinename.get(i), true);
                        }
                    }

                    //修改是否我的任务
                    for (int i = 0; i < isTaskList.size(); i++) {
                        if (isTaskList.get(i) == 1) {
                            powerlineTableOpt.updateTask(list_plinename.get(i), true);
                            poleTableOpt.updateTaskFromPlineName(list_plinename.get(i), true);
                            channelTableOpt.updateTaskFormPlineName(list_plinename.get(i), true);
                        }
                    }

                    //删除图片文件
                    deleteAllImage();

                    MySingleClass mySingleClass = MySingleClass.getInstance();
                    //获得电力线图层处理类
                    PowelineOverLayOpt powelineOverLayOpt = mySingleClass.getBaseMapViewClass().getPowelineOverLayOpt();
                    //删除电力线路
                    powelineOverLayOpt.removePlineOverlay();
                    //删除杆塔和通道
                    powelineOverLayOpt.removePoleAndChannel();
                    //添加电力线
                    powelineOverLayOpt.addPlineOverlay();

                    ToastUtil.show(mContext, "更新完成");
                }else{
                    ToastUtil.show(mContext, "更新失败");
                }

            }
        });
    }


    public void update(){
        this.downloadPlinesClass.Download(list_plinename);
    }


    private static void deleteAllImage(){
        MyFile.deleteFolder(MyString.image_pline_pole_folder_path);
        MyFile.deleteFolder(MyString.image_pline_danger_folder_path);
        MyFile.deleteFolder(MyString.image_temp_folder_path);
    }
}
