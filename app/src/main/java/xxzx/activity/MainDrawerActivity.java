package xxzx.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.amap.api.maps.LocationSource;

import java.util.List;
import java.util.Properties;

import xxzx.Config.PropertiesUtil;
import xxzx.Permissions.PermissionsActivity;
import xxzx.Permissions.PermissionsChecker;
import xxzx.activity.MyData.MyDataActivity;
import xxzx.activity.OfflineMap.OfflineMapActivity;
import xxzx.activity.Patrol.Record.RecordChnAndPoleActivity;
import xxzx.apkUpdateLibrary.ApkUpdateLibrary;
import xxzx.baseMapState.MainMap.BaseMainMapState;
import xxzx.baseMapState.MainMap.MapStateContext;
import xxzx.baseMapState.MainMap.MapStateNullOpt;
import xxzx.login.DrawHeaderView;
import xxzx.login.LoginFileUtils;
import xxzx.login.LoginState;
import xxzx.login.ModifyPswDlgFragment;
import xxzx.login.User;
import xxzx.mapLayerOpt.BaseMapViewClass;
import xxzx.myService.service.Service1;
import xxzx.myService.service.Service2;
import xxzx.polePlace.UploadPlacesClass;
import xxzx.publicClass.CeateFolderClass;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.ToastUtil;
import xxzx.spatialite.SpatialiteDataOpt;
import xxzx.spatialite.TableOptClass.CreateSysTable;
import xxzx.spatialite.TableOptClass.PolePlaceTableOpt;
import xxzx.spatialite.TableStruct.PolePlaceTableDataClass;

public class MainDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationSource
{

    private Bundle savedInstanceState = null;
    private AppBarLayout appBarLayout = null;
    private LinearLayout bottomContainer = null;
    // 地图控件的状态
    private MapStateContext mapStateContext = null;
    //地图视图类
    private BaseMapViewClass baseMapViewClass = null;
    //地图容器
    private LinearLayout mapContainer = null;

    public LocationSource mLocationSource;

    //登录View
    private DrawHeaderView drawHeaderView = null;
    //抽屉控件
    private DrawerLayout drawer = null;
    //导航视图
    private NavigationView navigationView = null;

    private SpatialiteDataOpt spatialiteDataOpt = null;

    private MySingleClass mySingleClass = null;

    private MyBroadcastReceiver myBroadcastReceiver = null;

    //权限检查
    private PermissionsChecker mPermissionsChecker = null;

    private ModifyPswDlgFragment modifyPswDlgFragment = null;

    //private static final int REQUEST_CODE = 0; // 请求码


    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
//            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
//            Manifest.permission.READ_LOGS,
//            Manifest.permission.WRITE_SETTINGS,
//            Manifest.permission.CHANGE_CONFIGURATION,
//            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
//            Manifest.permission.BLUETOOTH,
//            Manifest.permission.BLUETOOTH_ADMIN,
//            Manifest.permission.VIBRATE,
//            Manifest.permission.WAKE_LOCK,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        this.savedInstanceState = savedInstanceState;

        //权限检查
        this.mPermissionsChecker = new PermissionsChecker(this);
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        } else {
            this.initViewAndData();
        }
    }

    private void startPermissionsActivity()
    {
        PermissionsActivity.startActivityForResult(this, MyString.requestCode_activity_to_permissionsactivity, PERMISSIONS);
    }


    private void initViewAndData()
    {
        if (this.mySingleClass == null) {
            this.mySingleClass = MySingleClass.getInstance();
        }
        this.mySingleClass.setmContext(this);

        //创建app文件夹
        CeateFolderClass.CeateAppFolder();
        //拷贝assets需要拷贝到当地盘的文件
        PropertiesUtil.CopeConfig(this);

        try {
            //获取配置文件
            Properties properties = PropertiesUtil.loadConfig(getResources().getAssets().open("config.properties"));
            //记录配置文件
            mySingleClass.setProperties(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //数据库操作类
        this.spatialiteDataOpt = new SpatialiteDataOpt(this, MyString.spatialite_file_path);
        mySingleClass.setSpatialiteDataOpt(spatialiteDataOpt);

        //创建数据表
        CreateSysTable createSysTable = new CreateSysTable();
        createSysTable.create();

        //初始化
        this.initView(savedInstanceState);
        this.initData();

    }


    private void initView(Bundle savedInstanceState)
    {
        this.appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        this.bottomContainer = (LinearLayout) findViewById(R.id.bottomContainer);
        this.mapContainer = (LinearLayout) findViewById(R.id.mapContainer);
        this.drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.drawer.closeDrawer(GravityCompat.START);
        this.navigationView = (NavigationView) findViewById(R.id.nav_view);

        //设置header
        this.drawHeaderView = new DrawHeaderView(this);
        this.navigationView.addHeaderView(drawHeaderView);
        //定义导航点击事件
        this.navigationView.setNavigationItemSelectedListener(this);

        //创建地图视图
        if (this.baseMapViewClass == null) {
            this.baseMapViewClass = new BaseMapViewClass(this);
        }
        mLocationSource = this;
        this.baseMapViewClass.initView(savedInstanceState);
        this.baseMapViewClass.initData();

        this.mySingleClass.setBaseMapViewClass(baseMapViewClass);
        this.mapContainer.addView(baseMapViewClass);

        // 创建状态环境类
        if (this.mapStateContext == null) {
            this.mapStateContext = new MapStateContext();
        }
        this.mySingleClass.setMapStateContext(mapStateContext);

        // 定义初始化状态类
        MapStateNullOpt nullOptState = new MapStateNullOpt(this, getDelegate(), appBarLayout, bottomContainer, null);
        nullOptState.setDrawer(drawer);
        this.mapStateContext.setMainState(nullOptState);
        this.mapStateContext.setState(nullOptState);
        this.mapStateContext.InitViewAndData();
    }


    private void initData()
    {
        //版本下载更新
        Properties properties = this.mySingleClass.getProperties();
        String app_version_name_url = properties.get("app_version_name_url").toString();
        String apk_download_url = properties.get("apk_download_url").toString();
        ApkUpdateLibrary apkUpdateLibrary = new ApkUpdateLibrary(this, app_version_name_url, apk_download_url);
        apkUpdateLibrary.StartCheckVersion();

        //启动服务
        Intent i1 = new Intent(MainDrawerActivity.this, Service1.class);
        startService(i1);

        Intent i2 = new Intent(MainDrawerActivity.this, Service2.class);
        startService(i2);


        //注册自己的监听
        if (myBroadcastReceiver == null) {
            myBroadcastReceiver = new MyBroadcastReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.MY_BROADCAST");

        registerReceiver(myBroadcastReceiver, filter);

        //上传签到信息
        PolePlaceTableOpt polePlaceTableOpt = new PolePlaceTableOpt();
        List<PolePlaceTableDataClass> list = polePlaceTableOpt.getAllRow();

        UploadPlacesClass uploadPlacesClass = new UploadPlacesClass(this);
        uploadPlacesClass.Upload(list, UploadPlacesClass.PlaceType.fromDatabase);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        this.initViewAndData();
    }


    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        //用于权限检查结果返回
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == MyString.requestCode_activity_to_permissionsactivity) {
            if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                this.finish();
            } else {
                this.initViewAndData();
            }
            return;
        }


        //requestCode标示请求的标示   resultCode表示有数据
        //登录界面返回登录成功数据
        if (requestCode == MyString.requestCode_mainactivity_to_loginactivity && resultCode == RESULT_OK) {
            Boolean loginsuccess = data.getBooleanExtra("loginstate", false);
            if (this.drawHeaderView != null) {
                this.drawHeaderView.setLoginViewState(loginsuccess);
                this.drawer.closeDrawers();
            }
        }

        //重新统计隐患点更新的数量
        BaseMainMapState baseMainMapState = mapStateContext.getCurrentState();
        if (baseMainMapState instanceof MapStateNullOpt) {
            baseMainMapState.onActivityResult(requestCode, resultCode, data);
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mapStateContext != null) {
                mapStateContext.BackKeyDown();
            }
        }
        return false;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        //判断是否是退出按键
        if (item.getItemId() == R.id.nav_app_exsit) {
            // TODO Auto-generated method stub
            AlertDialog.Builder build = new AlertDialog.Builder(this);
            build.setTitle("注意").setMessage("确定要退出该应用吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener()
                    {

                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // TODO
                            try {
                                MainDrawerActivity.this.finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    //如果点击的是取消，不做任何处理
                    dialog.dismiss();
                }
            }).show();
            return true;
        }

        //判断当前登录状态
        if (MySingleClass.getInstance().getLoginState() == LoginState.FAIL_LOGIN) {
            ToastUtil.show(this, "当前未登录状态，不能操作");
            return true;
        }

        int id = item.getItemId();
        Intent intent = new Intent();
        switch (id) {
            case R.id.nav_pws_modify:

                if (MySingleClass.getInstance().getLoginState() == LoginState.SUCCESS_LOGIN_UNLINE) {
                    ToastUtil.show(this, "当前离线登录状态，不能修改密码");
                    return true;
                }

                modifyPswDlgFragment = new ModifyPswDlgFragment();
                modifyPswDlgFragment.setUser(MySingleClass.getInstance().getUser());
                modifyPswDlgFragment.show(getFragmentManager(), "ModifyPswDlgFragment");

                modifyPswDlgFragment.setOnCompleted(new ModifyPswDlgFragment.ICoallBack()
                {
                    @Override
                    public void onCompleted(boolean result, User user)
                    {
                        if (result) {
                            //设置登录成功
                            user.setLoginSuccess(true);

                            MySingleClass.getInstance().setUser(user);
                            //写入文件
                            LoginFileUtils.writeLoginJsonFile(user);
                        }
                    }
                });

                break;
            case R.id.nav_data_my:
                intent.setClass(this, MyDataActivity.class);
                this.startActivityForResult(intent, 0);
                break;
            case R.id.nav_data_update:
                intent = new Intent();
                intent.setClass(this, RecordChnAndPoleActivity.class);
                this.startActivityForResult(intent, 0);
                break;
            case R.id.nav_data_gpsline:
                intent.setClass(this, MyGpsLineActivity.class);
                this.startActivity(intent);
                break;
            case R.id.nav_data_local:
                //生成一个Intent对象
                intent.setClass(this, EditDataLocalActivity.class);
                this.startActivity(intent);
                break;
            case R.id.nav_offline_map:
                //生成一个Intent对象
                intent.setClass(this, OfflineMapActivity.class);
                this.startActivity(intent);
                break;
            case R.id.nav_sys_setting:
                //生成一个Intent对象
                intent.setClass(this, SysSettingActivity.class);
                this.startActivityForResult(intent, 0);
                break;
        }
        return true;
    }


    /**
     * 地图控件暂停
     */
    @Override
    public void onPause()
    {
        super.onPause();
        if (baseMapViewClass != null) {
            baseMapViewClass.onPause();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (baseMapViewClass != null) {
            baseMapViewClass.onSaveInstanceState(outState);
        }

    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    /**
     * 停止
     */
    @Override
    public void onStop()
    {
        super.onStop();
    }

    /**
     * 地图控件重新启动
     */
    @Override
    public void onResume()
    {
        super.onResume();
        if (baseMapViewClass != null) {
            baseMapViewClass.onResume();
        }
    }

    @Override
    public void onDestroy()
    {

        if (this.baseMapViewClass != null) {
            this.baseMapViewClass.onDestroy();
        }

        if (mapStateContext != null) {
            mapStateContext.close();
        }

        MySingleClass mySingleClass = MySingleClass.getInstance();
        mySingleClass.ExitApp();

        //取消监听
        unregisterReceiver(this.myBroadcastReceiver);

        super.onDestroy();
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener)
    {
        if (baseMapViewClass != null) {
            baseMapViewClass.activate(listener);
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate()
    {
    }


    /**
     * 自定义广播
     */
    public class MyBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // TODO Auto-generated method stub

            String action = intent.getAction();
//            if (action.equals(Intent.ACTION_SCREEN_OFF)) //锁屏、黑屏
//            {
//                //启动保活Activity
//                Intent intent2 = new Intent(MainDrawerActivity.this,KeepAliveActivity.class);
//                startActivity(intent2);
//            }


//            boolean isBackProcess = intent.getBooleanExtra(MyString.intent_broadcast_flag_start_keepliveactivity,false);
//            if (isBackProcess) {
//                Intent intent2 = new Intent(MainDrawerActivity.this,KeepAliveActivity.class);
//
//                startActivity(intent2);
//
//                Log.v("viclee", ">>>>>>>>>>>>>>>>>>>开始启动  KeepAliveActivity");
//            }
        }
    }

}
