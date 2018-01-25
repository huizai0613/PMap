package xxzx.apkUpdateLibrary.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

import java.io.File;

import xxzx.publicClass.MyString;


/**
 * 更新管理器
 * <p>
 * Created by maimingliang
 */
@SuppressWarnings("unused")
public class UpdateAppManager {

    @SuppressWarnings("unused")
    private static final String TAG = "UpdateAppManager";

    /**
     * 下载Apk, 并设置Apk地址,
     * 默认位置: /storage/sdcard0/Download
     *
     * @param context    上下文
     * @param downLoadUrl 下载地址
     * @param infoName   通知名称
     * @param description  通知描述
     */
    @SuppressWarnings("unused")
    public static void downloadApk(
            Context context,
            String downLoadUrl,
            String description,
            String infoName) {

        if (!isDownloadManagerAvailable()) {
            return;
        }


        File folder = new File(MyString.SAVE_APP_LOCATION);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        //如果已经存在下载过的apk文件，则删除
        File file = new File(MyString.APP_FILE_NAME);
        if (file.exists()) {
            file.delete();
        }


        String appUrl = downLoadUrl;
        if (appUrl == null || appUrl.isEmpty()) {

            return;
        }
        appUrl = appUrl.trim(); // 去掉首尾空格
        if (!appUrl.startsWith("http")) {
            appUrl = "http://" + appUrl; // 添加Http信息
        }



        DownloadManager.Request request;
        try {
            request = new DownloadManager.Request(Uri.parse(appUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        request.setTitle(infoName);
        request.setDescription(description);

        //在通知栏显示下载进度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }


        //sdcard目录下的download文件夹
        request.setDestinationInExternalPublicDir(MyString.SAVE_APP_LOCATION, MyString.SAVE_APP_NAME);

        Context appContext = context.getApplicationContext();
        DownloadManager manager = (DownloadManager)
                appContext.getSystemService(Context.DOWNLOAD_SERVICE);

        manager.enqueue(request);

    }

    // 最小版本号大于9
    private static boolean isDownloadManagerAvailable() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

}