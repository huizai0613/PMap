package xxzx.download;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import xxzx.myData.ReadHttpResultJson;
import xxzx.publicClass.LoadingDialog;
import xxzx.publicClass.MyHttpRequst;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.ToastUtil;

/**
 * Created by ch on 2017/1/21.
 */
public abstract class MultiThreadBaseClass {

    protected Context mContext;
    protected LoadingDialog loadingDialog;
    protected SparseArray<Object> data;

    protected int progressCurrentValue=0;
    protected int progressCountValue=0;

    protected boolean isSuccess = true;
    /**
     * 每次执行限定个数个任务的线程池
     */
    protected ExecutorService limitedTaskExecutor = null;
    /**
     * 线程池数量
     */
    protected int limitedTaskExecutorNum = 5;



    // ----------------定义点击事件-----------------------------
    /**
     * 一定一个接口
     */
    public interface ICoallBack {
        public void onCompleted(boolean result);
    }

    /**
     * 初始化接口变量
     */
    public ICoallBack icallBack = null;

    /**
     * 自定义控件的自定义事件
     *
     * @param iBack
     *            接口类型
     **/
    public void setOnCompleted(ICoallBack iBack) {
        icallBack = iBack;
    }


    public MultiThreadBaseClass(Context context){
        this.mContext= context;
        if(this.loadingDialog ==null){
            this.loadingDialog = new LoadingDialog(mContext);
        }
        //线程池创建
        this.limitedTaskExecutor = Executors.newFixedThreadPool(limitedTaskExecutorNum);// 限制线程池大小线程池
        this.progressCurrentValue = 0;
        this.progressCountValue = 0;

        this.data = new SparseArray<>();
    }


    public class MyRunnable implements Runnable {

        //传入参数集
        private SparseArray<Object> data;

        public void setData(SparseArray<Object> data) {
            this.data = data;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Message message = new Message();
            RunableFanction(this.data,message);
            myHandler.sendMessage(message);
        }
    }


    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            progressCurrentValue++;

            if (msg.what == -1) {
                isSuccess = false;
                ToastUtil.show(mContext, msg.obj.toString());
            }

            if(progressCountValue==progressCurrentValue){
                //
                loadingDialog.dismiss();
                //
                HandlerFanction();

                //完成触发函数
                if (icallBack != null) {
                    icallBack.onCompleted(isSuccess);
                }
            }
            super.handleMessage(msg);
        }
    };



    protected abstract void RunableFanction(SparseArray<Object> data,Message message);
    protected abstract void HandlerFanction();


}
