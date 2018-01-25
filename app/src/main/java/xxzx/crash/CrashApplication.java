package xxzx.crash;

/**
 * Created by ch on 2016/4/20.
 */

import xxzx.publicClass.AbsSuperApplication;

public class CrashApplication extends AbsSuperApplication
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }




    @Override
    public void onAppExit()
    {

    }

    @Override
    public String getAppNameFromSub()
    {

        return "";
    }


}
