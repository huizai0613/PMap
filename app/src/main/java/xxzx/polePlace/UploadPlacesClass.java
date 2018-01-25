package xxzx.polePlace;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.util.SparseArray;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;

import xxzx.download.MultiThreadBaseClass;
import xxzx.myData.ReadHttpResultJson;
import xxzx.publicClass.MyHttpRequst;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyTaskExecutorClass;
import xxzx.publicClass.PublicStaticFunUtil;
import xxzx.publicClass.ToastUtil;
import xxzx.spatialite.TableOptClass.PolePlaceTableOpt;
import xxzx.spatialite.TableStruct.PolePlaceTableDataClass;

/**
 * Created by ch on 2017/1/21.
 */
public class UploadPlacesClass extends MultiThreadBaseClass {


    private String update_place_url="";
    private String update_place_poststring = "";
    //默认是点击签到
    private PlaceType placeType = PlaceType.fromNull;
    private List<PolePlaceTableDataClass> placelist;


    public enum PlaceType{
        fromNull,//点击签到上传
        fromDatabase//数据库上传
    }

    public UploadPlacesClass(Context context) {
        super(context);
        //获取网络地址
        MySingleClass mySingleClass = MySingleClass.getInstance();
        Properties properties = mySingleClass.getProperties();
        this.update_place_url = properties.get("url_place_pole_update2").toString();
        this.update_place_poststring = properties.get("url_place_pole_update2_poststring").toString();
    }


    public void Upload(List<PolePlaceTableDataClass> list,PlaceType placeType) {

        if(list.size()==0){
            return;
        }

        this.placelist = list;
        this.placeType = placeType;

        this.progressCountValue = list.size();
        this.progressCurrentValue = 0;
        //创建线程池
        this.limitedTaskExecutor = Executors.newFixedThreadPool(limitedTaskExecutorNum);// 限制线程池大小线程池

        for (PolePlaceTableDataClass item : list) {
            SparseArray<Object> data = new SparseArray<>();
            data.put(0, item);
            //启动http请求的线程
            MyRunnable runnable = new MyRunnable();
            runnable.setData(data);
            //执行线程池
            this.limitedTaskExecutor.execute(runnable);
        }

        //关闭所有子线程，不关的话主线程会一直阻塞
        //线程结束处理程序
        MyTaskExecutorClass.TaskExecutorTerminated(limitedTaskExecutor, loadingDialog);

        this.loadingDialog.show();
    }


    @Override
    protected void RunableFanction(SparseArray<Object> data,Message message) {
        try {
            PolePlaceTableDataClass polePlaceTableDataClass = (PolePlaceTableDataClass) data.get(0);

            String poleObjectId =  String.valueOf(polePlaceTableDataClass.getPoleObjectId());
            String username = polePlaceTableDataClass.getUserName();
            String date = polePlaceTableDataClass.getDateTime();

            String postdata = String.format(this.update_place_poststring, poleObjectId, username, date);

            //上传到位信息
            //String result = MyHttpRequst.getHttpGetRequst2(httpPath);

            String result = MyHttpRequst.getHttpPostRequst2(this.update_place_url, postdata);




            if (result.trim().equals("")) {
                message.obj="签到上传失败，请确定网络连接是否成功！";
                message.what = -1;
                return;
            }

            JSONObject json = new JSONObject(result);
            String issuccess = json.getString("message");
            if (issuccess.equals("succeed")) {
                message.obj="签到成功！";
                message.what = 1;
            } else {
                message.obj="网络签到上传失败！";
                message.what = -1;
            }

        }catch (Exception e){
            message.what = -1;
            message.obj = e.toString();
            e.printStackTrace();
        }
    }




    @Override
    protected void HandlerFanction(){

        int result = -1;

        if(this.isSuccess){

            if(this.placeType == PlaceType.fromDatabase) {

                PolePlaceTableOpt polePlaceTableOpt = new PolePlaceTableOpt();
                result = polePlaceTableOpt.delete();

                if (result != -1) {
                    ToastUtil.show(mContext, "签到信息上传成功");
                } else {
                    ToastUtil.show(mContext, "签到信息数据表删除失败");
                }
            }else{
                ToastUtil.show(mContext, "签到信息上传成功");
            }
        }else{
            //写入数据库
            if(this.placeType == PlaceType.fromNull){

                PolePlaceTableOpt polePlaceTableOpt = new PolePlaceTableOpt();

                if(placelist.size()==0){
                    return;
                }

                List<PolePlaceTableDataClass>  list = polePlaceTableOpt.getRowFromObjectId(placelist.get(0).getPoleObjectId());
                if(list.size()==0){
                    result = polePlaceTableOpt.insert(placelist.get(0));
                }else{

                    placelist.get(0).setRowId(list.get(0).getRowId());

                    result = polePlaceTableOpt.update(placelist.get(0));
                }

                if(result!=-1){
                    ToastUtil.show(mContext, "离线签到成功");
                }else{
                    ToastUtil.show(mContext, "离线签到失败");

                }

            }
        }

    }








}
