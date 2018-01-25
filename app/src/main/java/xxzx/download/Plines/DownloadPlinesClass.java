package xxzx.download.Plines;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.security.DomainCombiner;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import xxzx.download.MultiThreadBaseClass;
import xxzx.myData.ReadHttpResultJson;
import xxzx.publicClass.LoadingDialog;
import xxzx.publicClass.MyHttpRequst;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyTaskExecutorClass;
import xxzx.publicClass.ToastUtil;

/**
 * Created by ch on 2017/1/21.
 */
public class DownloadPlinesClass extends MultiThreadBaseClass {

    public DownloadPlinesClass(Context context) {
        super(context);
    }


    public void Download(List<String> plineNameList) {

        this.progressCountValue = plineNameList.size();
        //计数器
        this.progressCurrentValue = 0;
        //创建线程池
        this.limitedTaskExecutor = Executors.newFixedThreadPool(limitedTaskExecutorNum);// 限制线程池大小线程池

        for (String item : plineNameList) {
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
            String plineName = (String) data.get(0);
            //获取网络地址
            MySingleClass mySingleClass = MySingleClass.getInstance();
            Properties properties = mySingleClass.getProperties();
            String download_url = properties.get("url_pline_info2").toString();
            String download_poststring = properties.get("url_pline_info2_poststring").toString();

            download_poststring  = String.format(download_poststring, plineName);
            String result = MyHttpRequst.getHttpPostRequst2(download_url,download_poststring);

            if (TextUtils.isEmpty(result.trim())) {
                message.what = -1;
                message.obj = "线路下载失败,请确认网络是否连接！";
                return;
            }

            JSONObject json = new JSONObject(result);
            String success = json.getString("message");
            if (success.equals("succeed")) {
                //读取json格式数据并写txt文件
                ReadHttpResultJson readHttpResultJson = new ReadHttpResultJson();
                int re = readHttpResultJson.readAndWriteTxt(result);
                //读取json数据成功
                if (re == 1) {
                    message.what = 1;
                } else {
                    message.what = -1;
                    message.obj = "读取下载的线路数据失败！";
                }
            } else {
                message.what = -1;
                message.obj = result;
            }

        }catch (Exception e){
            message.what = -1;
            message.obj = e.toString();
            e.printStackTrace();
        }
    }


    @Override
    protected void HandlerFanction(){

    }








}
