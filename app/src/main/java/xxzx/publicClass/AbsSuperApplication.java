package xxzx.publicClass;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by ch on 2017/3/29.
 */
public abstract class AbsSuperApplication extends Application {

    protected static Context context;
    protected static String appName;
    private int mFinalCount;
    protected static AbsSuperApplication instance;

    /**
     * 维护Activity 的list
     */
    private static List<Activity> mActivitys = Collections
            .synchronizedList(new LinkedList<Activity>());

    @Override
    public void onCreate() {
        super.onCreate();
        instance =this;
        context = this.getApplicationContext();
        appName =  getAppNameFromSub();
        registerActivityListener();
    }

    public static AbsSuperApplication getInstance() {
        return instance;
    }

    public static String getAppName() {
        return appName;
    }

    public static Context getContext() {
        return context;
    }

    protected abstract String  getAppNameFromSub();


    /**
     * @param activity 作用说明 ：添加一个activity到管理里
     */
    public void pushActivity(Activity activity) {
        mActivitys.add(activity);
        Log.d("App","activityList:size:" + mActivitys.size());
    }

    /**
     * @param activity 作用说明 ：删除一个activity在管理里
     */
    public void popActivity(Activity activity) {
        mActivitys.remove(activity);
        Log.d("App","activityList:size:" + mActivitys.size());
    }



    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        if (mActivitys == null||mActivitys.isEmpty()) {
            return null;
        }
        Activity activity = mActivitys.get(mActivitys.size()-1);
        return activity;
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public static void finishCurrentActivity() {
        if (mActivitys == null||mActivitys.isEmpty()) {
            return;
        }
        Activity activity = mActivitys.get(mActivitys.size()-1);
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        if (mActivitys == null||mActivitys.isEmpty()) {
            return;
        }
        if (activity != null) {
            mActivitys.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        if (mActivitys == null||mActivitys.isEmpty()) {
            return;
        }
        for (Activity activity : mActivitys) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 按照指定类名找到activity
     *
     * @param cls
     * @return
     */
    public static Activity findActivity(Class<?> cls) {
        Activity targetActivity = null;
        if (mActivitys != null) {
            for (Activity activity : mActivitys) {
                if (activity.getClass().equals(cls)) {
                    targetActivity = activity;
                    break;
                }
            }
        }
        return targetActivity;
    }

    /**
     * @return 作用说明 ：获取当前最顶部activity的实例
     */
    public Activity getTopActivity() {
        Activity mBaseActivity = null;
        synchronized (mActivitys) {
            final int size = mActivitys.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = mActivitys.get(size);
        }
        return mBaseActivity;

    }

    /**
     * @return 作用说明 ：获取当前最顶部的acitivity 名字
     */
    public String getTopActivityName() {
        Activity mBaseActivity = null;
        synchronized (mActivitys) {
            final int size = mActivitys.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = mActivitys.get(size);
        }
        return mBaseActivity.getClass().getName();
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        if (mActivitys == null) {
            return;
        }
        for (Activity activity : mActivitys) {
            activity.finish();
        }
        mActivitys.clear();
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            Log.d("App","app exit");
            finishAllActivity();
            onAppExit();
        } catch (Exception e) {
        }
    }

    abstract protected void onAppExit();


    private void registerActivityListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    /**
                     *  监听到 Activity创建事件 将该 Activity 加入list
                     */
                    pushActivity(activity);

                }

                @Override
                public void onActivityStarted(Activity activity) {
                    //如果mFinalCount ==1，说明是从后台到前台
                    Log.e("onActivityStarted", mFinalCount + "");
                    if (mFinalCount == 0) {
                        //说明从后台回到了前台
                        Log.v("viclee", ">>>>>>>>>>>>>>>>>>>切到前台  lifecycle");
                    }
                    mFinalCount++;
                }

                @Override
                public void onActivityStopped(Activity activity) {
                    //如果是包活activity不参与计算
                    mFinalCount--;
                    //如果mFinalCount ==0，说明是前台到后台
                    Log.i("onActivityStopped", mFinalCount + "");
                    if (mFinalCount == 0) {
                        //说明从前台回到了后台
                        Log.v("viclee", ">>>>>>>>>>>>>>>>>>>切到后台  lifecycle");
                    }
                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }


                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    if (null==mActivitys&&mActivitys.isEmpty()){
                        return;
                    }
                    if (mActivitys.contains(activity)){
                        /**
                         *  监听到 Activity销毁事件 将该Activity 从list中移除
                         */
                        popActivity(activity);
                    }
                }
            });
        }
    }

}
