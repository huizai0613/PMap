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

public class ServiceTwo extends Service {

    public final static String TAG = ServiceTwo.class.getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");

        thread.start();
        return START_REDELIVER_INTENT;
    }

    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    Log.e(TAG, "ServiceTwo Run: " + System.currentTimeMillis());
                    boolean b = Utils.isServiceWork(ServiceTwo.this, "xxzx.myService.ServiceOne");
                    if(!b) {
                        Intent service = new Intent(ServiceTwo.this, ServiceOne.class);
                        startService(service);
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
		 * 启动service1
		 */
        Intent service = new Intent(ServiceTwo.this, ServiceOne.class);
        startService(service);
        Log.e(TAG, "Start ServiceOne");

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
