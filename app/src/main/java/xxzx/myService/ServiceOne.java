package xxzx.myService;

/**
 * Created by Lenovo on 2017/5/30.
 */

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import xxzx.activity.MainDrawerActivity;
import xxzx.myService.utils.Utils;

public class ServiceOne extends Service {


    public final static String TAG = ServiceOne.class.getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");

        thread.start();
        return START_STICKY;
    }

    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    Log.e(TAG, "ServiceOne Run: " + System.currentTimeMillis());
                    boolean b = Utils.isServiceWork(ServiceOne.this, "xxzx.myService.ServiceTwo");
                    if (!b) {
                        Intent service = new Intent(ServiceOne.this, ServiceTwo.class);
                        startService(service);
                        Log.e(TAG, "Start ServiceTwo");
                    }
                }
            };
            timer.schedule(task, 0, 1000);
        }
    });


    /**
     * 在内存紧张的时候，系统回收内存时，会回调OnTrimMemory， 重写onTrimMemory当系统清理内存时从新启动Service2
     */
    @Override
    public void onTrimMemory(int level) {
		/*
		 * 启动service2
		 */
        Intent service = new Intent(ServiceOne.this, ServiceTwo.class);
        startService(service);
        Log.e(TAG, "Start ServiceTwo");

    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}




