package xxzx.upload;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import xxzx.patrol.Record.ChnOrPole;
import xxzx.publicClass.DangerVersion;
import xxzx.publicClass.FileImageUpload;
import xxzx.publicClass.LoadingDialog;
import xxzx.publicClass.MyHttpRequst;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.MyTaskExecutorClass;
import xxzx.publicClass.ToastUtil;
import xxzx.spatialite.TableOptClass.ChannelDangerTableOpt;
import xxzx.spatialite.TableOptClass.DangerSgTableOpt;
import xxzx.spatialite.TableOptClass.PoleDangerTableOpt;
import xxzx.spatialite.TableStruct.ChannelDangerTableDataClass;
import xxzx.spatialite.TableStruct.DangerSgTableDataClass;
import xxzx.spatialite.TableStruct.PoleDangerTableDataClass;

/**
 * Created by ch on 2016/6/16.
 */
public class UpLoadDangerClass {

    private Context mContext=null;
    private List<ListItemUpdateDangerClass> dangerItems;
    private LoadingDialog loadingDialog=null;

    /**
     * 每次执行限定个数个任务的线程池
     */
    private ExecutorService limitedTaskExecutor = null;
    /**
     * 线程池数量
     */
    private int limitedTaskExecutorNum = 5;

    //当前上传的进度
    private int currenPprogressValue = 0;
    //上传总数
    private int updateItemCount = 0;


    // ----------------定义点击事件-----------------------------
    /**
     * 一定一个接口
     */
    public interface ICoallBack {
        public void onComplatedListener();
    }

    /**
     * 初始化接口变量
     */
    ICoallBack icallBack = null;

    /**
     * 自定义控件的自定义事件
     *
     * @param iBack
     *            接口类型
     **/
    public void setOnComplatedListener(ICoallBack iBack) {
        icallBack = iBack;
    }

    /**
     * 构造函数
     * @param mContext
     */
    public UpLoadDangerClass(Context mContext) {
        this.mContext = mContext;
        this.loadingDialog = new LoadingDialog(mContext);
    }

    /**
     * 设置上传的数据
     * @param itemList
     */
    public void setUpdateItemList(ArrayList<ListItemUpdateDangerClass> itemList){
        this.dangerItems=itemList;
    }


    /**
     * 上传隐患点采集函数
     */
    public void update(){

        loadingDialog.show();
        currenPprogressValue = 0;
        updateItemCount = dangerItems.size();

        //线程池创建
        limitedTaskExecutor = Executors.newFixedThreadPool(limitedTaskExecutorNum);// 限制线程池大小线程池

        PoleDangerTableOpt poleDangerTableOpt = new PoleDangerTableOpt();
        ChannelDangerTableOpt channelDangerTableOpt = new ChannelDangerTableOpt();

        for (ListItemUpdateDangerClass item : dangerItems) {
            Object object = null;
            String[] strs = null;

            if (item.getChnOrpole() == ChnOrPole.POLE) {
                object = (PoleDangerTableDataClass) poleDangerTableOpt.getRow(item.getRowID());
            } else if (item.getChnOrpole() == ChnOrPole.CHANNEL) {
                object = (ChannelDangerTableDataClass) channelDangerTableOpt.getRow(item.getRowID());
            }

            MyRunnable runnable = new MyRunnable();
            runnable.setDangerInfo(object);
            //执行线程池
            limitedTaskExecutor.execute(runnable);
        }
        //关闭所有子线程，不关的话主线程会一直阻塞
        //线程结束处理程序
        MyTaskExecutorClass.TaskExecutorTerminated(limitedTaskExecutor, loadingDialog);
    }




    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            currenPprogressValue++;

            Log.e("线程返回数量",String.valueOf(currenPprogressValue));

            if (msg.what == -1) {
                ToastUtil.show(mContext, msg.obj.toString());
            } else if (msg.what == 1) {
                Bundle bundle = msg.getData();
                int keyid = bundle.getInt("keyid");
                int rowid = bundle.getInt("rowid");
                int version = bundle.getInt("version");
                int type = bundle.getInt("chnorpole");

                ToastUtil.show(mContext, "隐患标示为:"+keyid+"的隐患数据上传完成");

                //修改数据库标示版本
                if (type == ChnOrPole.CHANNEL) {//通道
                    //更新数据表
                    ChannelDangerTableOpt channelDangerTableOpt = new ChannelDangerTableOpt();

                    channelDangerTableOpt.updateDangerCompleted(rowid, keyid, DangerVersion.DANGER_VERSION_INIT);

                    //如果是消隐归档，上传完成后删除
                    if(version == DangerVersion.DANGER_VERSION_SOLVE){
                        Object object = channelDangerTableOpt.getRow(rowid);

                        //删除图片
                        String pics=((ChannelDangerTableDataClass)object).getPicsJson();
                        String[] picnames=pics.split(";");
                        for(String name:picnames){
                            if(!TextUtils.isEmpty(name)){
                                File file=new File(MyString.image_pline_danger_folder_path +"/"+name);
                                if(file.exists()){
                                    file.delete();
                                }
                            }
                        }
                        //删除数据库数据
                        channelDangerTableOpt.delete(rowid);
                    }
                } else if (type == ChnOrPole.POLE) {//杆塔
                    //更新数据表
                    PoleDangerTableOpt poleDangerTableOpt = new PoleDangerTableOpt();
                    poleDangerTableOpt.updateDangerCompleted(rowid, keyid, DangerVersion.DANGER_VERSION_INIT);

                    //如果是消隐归档，上传完成后删除
                    if(version==DangerVersion.DANGER_VERSION_SOLVE){
                        Object object = poleDangerTableOpt.getRow(rowid);

                        //删除图片
                        String pics=((PoleDangerTableDataClass)object).getPicsJson();
                        String[] picnames=pics.split(";");
                        for(String name:picnames){
                            if(!TextUtils.isEmpty(name)){
                                File file=new File(MyString.image_pline_danger_folder_path +"/"+name);
                                if(file.exists()){
                                    file.delete();
                                }
                            }
                        }
                        //删除数据库数据
                        poleDangerTableOpt.delete(rowid);
                    }
                }
            }

            if(currenPprogressValue >= updateItemCount){
                loadingDialog.dismiss();
                //处理完成后回调
                if (icallBack != null) {
                    icallBack.onComplatedListener();
                }
            }
            super.handleMessage(msg);
        }
    };








    public class MyRunnable implements Runnable {

        private Object object;

        public void setDangerInfo(Object object) {
            this.object = object;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            MySingleClass mySingleClass = MySingleClass.getInstance();
            Properties properties = mySingleClass.getProperties();

            Message message = new Message();
            String poststring = "";
            String url="";
            int dangerVersion = -10;
            try {
                if (object instanceof PoleDangerTableDataClass) {
                    PoleDangerTableDataClass poledanger = (PoleDangerTableDataClass) object;

                    ////////////post测试
                    url = properties.get("url_pole_danger_update2").toString();
                    poststring = properties.get("url_pole_danger_update2_poststring").toString();

                    String poleName = poledanger.getPoleName();
                    String poleObjectId = String.valueOf(poledanger.getPoleObjectId());
                    String keyID = String.valueOf(poledanger.getKeyID());
                    String dangerType = String.valueOf(poledanger.getDangerType());
                    String dangerLevel = String.valueOf(poledanger.getDangerLevel());
                    String dangerName = poledanger.getDangerName();
                    String spotMark = poledanger.getSpotMark();
                    dangerVersion = poledanger.getVersion();
                    String userName = poledanger.getUserName();
                    String dateTime = poledanger.getDateTime().replace(" ", "T");

                    poststring = String.format(poststring,
                            poleObjectId,
                            keyID,
                            dangerType,
                            dangerLevel,
                            dangerName,
                            String.valueOf(dangerVersion),
                            userName,
                            dateTime,
                            spotMark
                    );

                    String result = MyHttpRequst.getHttpPostRequst2(url, poststring);


                    Log.e("上传杆塔隐患信息", result);

                    if(TextUtils.isEmpty(result.trim())){
                        message.what = -1;
                        message.obj = "无法上传，请确认网络连接是否正常";
                        return;
                    }

                    JSONObject json = new JSONObject(result);
                    String success = json.getString("message");
                    if (success.equals("succeed")) {
                        //读取json格式数据，并添加到数据库中
                        int keyid = json.getInt("keyid");

                        url = properties.get("url_pole_danger_update_img").toString();
                        //上传图片
                        int dd = uploadPics(url, poledanger.getPicsJson(), poledanger.getPoleObjectId(), keyid);
                        Log.e("上传杆塔图片信息完成", String.valueOf(dd));
                        message.what = dd;
                        Bundle bundle = new Bundle();
                        bundle.putInt("rowid", poledanger.getRowId());
                        bundle.putInt("keyid", keyid);
                        bundle.putInt("version", dangerVersion);
                        bundle.putInt("chnorpole", ChnOrPole.POLE);
                        message.setData(bundle);

                    } else {
                        message.what = -1;
                        message.obj = json.getString("usermessage");
                    }
                } else if (object instanceof ChannelDangerTableDataClass) {

                    url = properties.get("url_channel_danger_update2").toString();
                    poststring = properties.get("url_channel_danger_update2_poststring").toString();

                    ChannelDangerTableDataClass channeldanger = (ChannelDangerTableDataClass) object;

                    String chnName = channeldanger.getChannelName();
                    String chnObjectId = String.valueOf(channeldanger.getChannelObjectId());
                    String keyID = String.valueOf(channeldanger.getKeyID());
                    String dangerType = String.valueOf(channeldanger.getDangerType());
                    String dangerLevel = String.valueOf(channeldanger.getDangerLevel());
                    String dangerName = channeldanger.getDangerName();
                    String spotMark = channeldanger.getSpotMark();
                    dangerVersion = channeldanger.getVersion();
                    String userName = channeldanger.getUserName();
                    String dateTime = channeldanger.getDateTime().replace(" ", "T");

                    //考虑到空格问题，采用android特有的类
                    String geometry = Uri.encode(channeldanger.getGeometry());

                    //如果是施工隐患
                    if (channeldanger.getDangerType() == MyString.DANGER_SG_TYPE) {
                        url = properties.get("url_danger_sg_update2").toString();
                        poststring = properties.get("url_danger_sg_update2_poststring").toString();

                        DangerSgTableOpt dangerSgTableOpt = new DangerSgTableOpt();
                        DangerSgTableDataClass dangerSgTableDataClass = dangerSgTableOpt.getRowFormDangerRowID(channeldanger.getRowId());

                        String location = dangerSgTableDataClass.getLocation();
                        String sgdepartment = dangerSgTableDataClass.getSgdepartment();
                        String sgcontact = dangerSgTableDataClass.getSgcontact();
                        String sgphone = dangerSgTableDataClass.getSgphone();
                        String ywcontact = dangerSgTableDataClass.getYwcontact();
                        String ywphone = dangerSgTableDataClass.getYwphone();
                        String keeper = dangerSgTableDataClass.getKeeper();
                        String keeperphone = dangerSgTableDataClass.getKeeperphone();
                        String guank1 = dangerSgTableDataClass.getGuank1();
                        String guank2 = dangerSgTableDataClass.getGuank2();
                        String guank3 = dangerSgTableDataClass.getGuank3();
                        String guank4 = dangerSgTableDataClass.getGuank4();
                        String guank5 = dangerSgTableDataClass.getGuank5();

                        poststring = String.format(poststring,
                                chnObjectId,
                                keyID,
                                dangerType,
                                dangerLevel,
                                dangerName,
                                String.valueOf(dangerVersion),
                                userName,
                                dateTime,
                                geometry,
                                spotMark,
                                location,
                                sgdepartment,
                                sgcontact,
                                sgphone,
                                ywcontact,
                                ywphone,
                                keeper,
                                keeperphone,
                                guank1,
                                guank2,
                                guank3,
                                guank4,
                                guank5
                        );
                    } else {
                        poststring = String.format(poststring,
                                chnObjectId,
                                keyID,
                                dangerType,
                                dangerLevel,
                                dangerName,
                                String.valueOf(dangerVersion),
                                userName,
                                dateTime,
                                geometry,
                                spotMark
                        );
                    }

                    //上传隐患点信息
                    String result = MyHttpRequst.getHttpPostRequst2(url,poststring);

                    Log.e("上传通道隐患信息", result);

                    if(TextUtils.isEmpty(result.trim())){
                        message.what = -1;
                        message.obj = "无法上传，请确认网络连接是否正常";
                        return;
                    }

                    JSONObject json = new JSONObject(result);
                    String success = json.getString("message");

                    Log.e("上传返回JSON数据：",result);

                    if (success.equals("succeed")) {
                        //读取json格式数据，并添加到数据库中
                        int keyid = json.getInt("keyid");
                        url = properties.get("url_channel_danger_update_img").toString();

                        //上传图片
                        int dd = uploadPics(url, channeldanger.getPicsJson(), channeldanger.getChannelObjectId(), keyid);
                        Log.e("上传通道图片信息完成", String.valueOf(dd));
                        message.what = dd;
                        Bundle bundle = new Bundle();
                        bundle.putInt("rowid", channeldanger.getRowId());
                        bundle.putInt("keyid", keyid);
                        bundle.putInt("version", dangerVersion);
                        bundle.putInt("chnorpole", ChnOrPole.CHANNEL);
                        message.setData(bundle);
                    } else {
                        message.what = -1;
                        message.obj = json.getString("usermessage");
                    }
                }
            } catch (JSONException e) {
                message.what = -1;
                message.obj = e.toString();
                e.printStackTrace();
            } catch (Exception e) {
                message.what = -1;
                message.obj = e.toString();
                e.printStackTrace();
            } finally {
                myHandler.sendMessage(message);
            }
        }
    }


    /**
     * 上传图片信息
     * @param url
     * @param picjson
     * @param keyid
     * @return
     */
    private int uploadPics(String url, String picjson, int objectid, int keyid) {
        String[] names = picjson.split(";");
        try {
            for (int i = 0; i < names.length; i++) {
                if (!TextUtils.isEmpty(names[i])) {

                    File f = new File(MyString.image_pline_danger_folder_path + "/" + names[i]);
                    if (!f.exists()) {
                        continue;
                    }

                    String myUrl = String.format(url, String.valueOf(objectid), String.valueOf(keyid), i);

                    Log.e("上传通道图片地址",myUrl);

                    String result = FileImageUpload.uploadFile(f, myUrl);

                    Log.e("上传通道图片返回",result);
                    if (result.equals("0")) {
                        return -1;
                    }
                }
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 获得杆塔隐患表或者通道隐患表中所有隐患信息
     * @param mChnOrPoleType
     */
    public static ArrayList<ListItemUpdateDangerClass> getAllDangerItem(int mChnOrPoleType,int objectid){

        ArrayList  itemList = new ArrayList<ListItemUpdateDangerClass>();

        if (mChnOrPoleType == ChnOrPole.POLE) {
            PoleDangerTableOpt poleDangerTableOpt = new PoleDangerTableOpt();
            List<PoleDangerTableDataClass> list_poledanger;
            if(objectid ==-1) {
                list_poledanger = poleDangerTableOpt.getRowFromUpdate();
            }else {
                list_poledanger = poleDangerTableOpt.getRowFromUpdate(objectid);
            }
            for (PoleDangerTableDataClass p : list_poledanger) {
                ListItemUpdateDangerClass item = new ListItemUpdateDangerClass();
                item.setRowID(p.getRowId());
                item.setObjectid(p.getPoleObjectId());
                item.setTitle(p.getDangerName());
                item.setDate(p.getDateTime());
                item.setDangerType(p.getDangerType());
                item.setDangetLevel(p.getDangerLevel());
                item.setChnOrpole(mChnOrPoleType);
                itemList.add(item);
            }

        } else if (mChnOrPoleType == ChnOrPole.CHANNEL) {
            ChannelDangerTableOpt channelDangerTableOpt = new ChannelDangerTableOpt();
            List<ChannelDangerTableDataClass> list_poledanger;
            if(objectid ==-1) {
                list_poledanger = channelDangerTableOpt.getRowFromUpdate();
            }else {
                list_poledanger = channelDangerTableOpt.getRowFromUpdate(objectid);
            }

            for (ChannelDangerTableDataClass c : list_poledanger) {
                ListItemUpdateDangerClass item = new ListItemUpdateDangerClass();
                item.setRowID(c.getRowId());
                item.setObjectid(c.getChannelObjectId());
                item.setTitle(c.getDangerName());
                item.setDate(c.getDateTime());
                item.setDangerType(c.getDangerType());
                item.setDangetLevel(c.getDangerLevel());
                item.setChnOrpole(mChnOrPoleType);
                itemList.add(item);
            }
        }

        return itemList;
    }



}
