package xxzx.apkUpdateLibrary;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import xxzx.apkUpdateLibrary.receiver.InstallReceiver;
import xxzx.apkUpdateLibrary.utils.UpdateAppManager;
import xxzx.publicClass.MyHttpRequst;
import xxzx.publicClass.NetworkConnected;


/**
 * Created by mulin on 2016/12/7.
 */
public class ApkUpdateLibrary {

    private Context mContext;
    private String version_url = "";
    private String apk_url = "";

    private InstallReceiver installReceiver = new InstallReceiver();


    public ApkUpdateLibrary(Context context,String versionUrl,String apkUrl){
        this.mContext=context;
        this.version_url = versionUrl;
        this.apk_url = apkUrl;
    }


    /**
     * 启动检查
     */
    public void StartCheckVersion(){

        if(version_url.trim().equals("")) return;
        if(apk_url.trim().equals("")) return;

        if (NetworkConnected.isWifiConnected(this.mContext)) {
            //启动http请求的线程
            new Thread(runnable).start();
        }
    }

    /**
     * 登录http请求，另一个线程中进行
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            String result = MyHttpRequst.getHttpGetRequst2(version_url);

            if (!result.trim().equals("")) {
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("serverVersionName", result.trim());

                msg.setData(data);
                handler.sendMessage(msg);
            }

        }
    };

    /**
     * 请求返回主线程处理函数
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String serverVersionCode = data.getString("serverVersionName");
            String curVersonName = getVersionName();

            Log.e("当前版本",curVersonName);
            Log.e("当前服务器版本",serverVersionCode);
            //if(curVersonName.equals("1.0")){
            if(!curVersonName.equals(serverVersionCode)){
                //获取权限
                //getPersimmions();
                //下载
                checkUpdate();
                //注册下载完成的广播
                mContext.registerReceiver(installReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                return;
            }
        }
    };


    private void checkUpdate() {
        if(true){
            Version version = new Version();
            version.setUri(apk_url);

            download(version);
        }
    }

    private void download(final Version version) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示")
                .setMessage("检测到新版本，是否下载更新？下载过程中请不要退出该应用APP，您可以在通知栏中查看下载进度！")
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        UpdateAppManager.downloadApk(mContext, version.getUri(), "版本升级", getApplicationName());
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        builder.setCancelable(false);
        builder.show();
    }


    /**
     * 获得app的名称
     * @return
     */
    private String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = mContext.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }




    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    private String getVersionName() {
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0";
        }
    }


//    //如果api>=23
//    @TargetApi(23)
//    private void getPersimmions() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ArrayList<String> permissions = new ArrayList<String>();
//			/*
//			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
//			 */
//            // 读写权限
//            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
//            }
//
//            if (permissions.size() > 0) {
//                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
//            }
//        }
//    }
//
//    @TargetApi(23)
//    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
//        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
//            if (shouldShowRequestPermissionRationale(permission)){
//                return true;
//            }else{
//                permissionsList.add(permission);
//                return false;
//            }
//        }else{
//            return true;
//        }
//    }
//
//    @TargetApi(23)
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        // TODO Auto-generated method stub
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//    }







}
