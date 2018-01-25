package xxzx.download.Plines;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.Executors;

import xxzx.download.MultiThreadBaseClass;
import xxzx.myData.dataInput.DataJsonPointClass;
import xxzx.publicClass.MyFile;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.MyTaskExecutorClass;
import xxzx.publicClass.ToastUtil;
import xxzx.spatialite.TableOptClass.BaseTableOpt;
import xxzx.spatialite.TableOptClass.ChannelTableOpt;
import xxzx.spatialite.TableOptClass.PoleTableOpt;
import xxzx.spatialite.TableOptClass.PowerlineTableOpt;
import xxzx.spatialite.TableStruct.ChannelTableDataClass;
import xxzx.spatialite.TableStruct.PoleTableDataClass;

/**
 * Created by ch on 2017/1/21.
 */
public class WriteDatabaseClass extends MultiThreadBaseClass {

    public WriteDatabaseClass(Context context) {
        super(context);
    }


    public void WritePlines(List<String> plineNameList) {

        this.data.clear();

        for (int i=0;i<plineNameList.size();i++) {
            data.put(i, plineNameList.get(i));
        }


        //一个线程
        this.progressCountValue = 1;
        //计数器
        this.progressCurrentValue = 0;
        //创建线程池
        this.limitedTaskExecutor = Executors.newFixedThreadPool(limitedTaskExecutorNum);// 限制线程池大小线程池


        //启动http请求的线程
        MyRunnable runnable = new MyRunnable();
        runnable.setData(data);
        //执行线程池
        this.limitedTaskExecutor.execute(runnable);

        //关闭所有子线程，不关的话主线程会一直阻塞
        //线程结束处理程序
        MyTaskExecutorClass.TaskExecutorTerminated(limitedTaskExecutor, loadingDialog);

        this.loadingDialog.show();
    }




    @Override
    protected void RunableFanction(SparseArray<Object> data,Message message){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            PowerlineTableOpt powerlineTableOpt = new PowerlineTableOpt();
            PoleTableOpt poleTableOpt = new PoleTableOpt();
            ChannelTableOpt channelTableOpt = new ChannelTableOpt();
            //创建事务
            ((BaseTableOpt) powerlineTableOpt).BEGIN();
            String currentTime = sdf.format(new Date());
            Log.e("数据库写入开始时间：", currentTime);

            for (int i=0;i<data.size();i++) {
                String item = (String)data.valueAt(i);

                DataJsonPointClass dataJsonPointClass = new DataJsonPointClass(MyString.download_pline_folder_path, item + ".txt", mContext);

                powerlineTableOpt.insert(dataJsonPointClass.plineTableDataClass);

                for (PoleTableDataClass pole : dataJsonPointClass.poleList) {
                    poleTableOpt.insert(pole);
                }
                for (ChannelTableDataClass channel : dataJsonPointClass.chnList) {
                    channelTableOpt.insert(channel);
                }
            }
            //提交事务
            ((BaseTableOpt) powerlineTableOpt).COMMIT();
            //暂停3秒
            Thread.sleep(3000);

            message.what = 1;
        }catch (Exception e) {
            message.what = -1;
            message.obj = e.toString();
            e.printStackTrace();
        }
    }

    @Override
    protected void HandlerFanction(){
        //删除线路存储文件夹
        MyFile.deleteFolder(MyString.download_pline_folder_path);
        ToastUtil.show(mContext, "写入数据库完成！");
    }
}
