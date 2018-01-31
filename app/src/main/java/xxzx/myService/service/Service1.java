package xxzx.myService.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.widget.Toast;

import com.amap.api.maps.LocationSource;

import java.util.Properties;

import xxzx.Config.PropertiesUtil;
import xxzx.activity.StrongService;
import xxzx.myBdLocation.MyGdLocationClass;
import xxzx.myService.utils.Utils;
import xxzx.publicClass.MySingleClass;


/**
 * http://blog.csdn.net/liuzg1220
 *
 * @author henry
 */
public class Service1 extends Service implements LocationSource.OnLocationChangedListener, LocationSource
{
    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what) {
                case 1:
                    startService2();
                    break;

                default:
                    break;
            }

        }

        ;
    };

    /**
     * 使用aidl 启动Service2
     */
    private StrongService startS2 = new StrongService.Stub()
    {
        @Override
        public void stopService() throws RemoteException
        {
            Intent i = new Intent(getBaseContext(), Service2.class);
            getBaseContext().stopService(i);
        }

        @Override
        public void startService() throws RemoteException
        {
            Intent i = new Intent(getBaseContext(), Service2.class);
            getBaseContext().startService(i);
        }
    };
    private MyGdLocationClass myGdLocationClass;

    /**
     * 在内存紧张的时候，系统回收内存时，会回调OnTrimMemory， 重写onTrimMemory当系统清理内存时从新启动Service2
     */
    @Override
    public void onTrimMemory(int level)
    {
        /*
         * 启动service2
		 */
        startService2();

    }

    @Override
    public void onLocationChanged(Location location)
    {

    }

    @Override
    public void onCreate()
    {
        Toast.makeText(Service1.this, "易巡 Service1 正在启动...", Toast.LENGTH_SHORT)
                .show();
        startService2();
        MySingleClass mySingleClass = MySingleClass.getInstance();
        try {
            //获取配置文件
            Properties properties = PropertiesUtil.loadConfig(getResources().getAssets().open("config.properties"));
            //记录配置文件
            mySingleClass.setProperties(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (myGdLocationClass == null) {
            myGdLocationClass = new MyGdLocationClass(getApplication(), false);
            myGdLocationClass.mListener = Service1.this;
            myGdLocationClass.start();
        }
        /*
         * 此线程用监听Service2的状态
		 */
        new Thread()
        {
            public void run()
            {
                while (true) {
                    boolean isRun = Utils.isServiceWork(Service1.this,
                            "xxzx.myService.service.Service2");
                    if (!isRun) {
                        Message msg = Message.obtain();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            ;
        }.start();
    }

    /**
     * 判断Service2是否还在运行，如果不是则启动Service2
     */
    private void startService2()
    {
        boolean isRun = Utils.isServiceWork(Service1.this,
                "xxzx.myService.service.Service2");
        if (isRun == false) {
            try {
                startS2.startService();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return (IBinder) startS2;
    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener)
    {

    }

    @Override
    public void deactivate()
    {

    }
}

