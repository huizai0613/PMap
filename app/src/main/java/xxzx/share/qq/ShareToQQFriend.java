package xxzx.share.qq;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import xxzx.activity.R;
import xxzx.publicClass.ToastUtil;
import xxzx.share.ShareUtil;
import xxzx.share.wx.AppInstallHelper;

/**
 * Created by Lenovo on 2017/6/9.
 */

public class ShareToQQFriend {
    private static final String PackageName = "com.tencent.mobileqq";
    private static final String ActivityName = "com.tencent.mobileqq.activity.JumpActivity";

    //pics are OK。 text is not....
    public static void share(Activity activity, String msg, ArrayList<Uri> images) {
        if (AppInstallHelper.isInstalled(activity, PackageName,
                ActivityName)) {
            ShareUtil shareUtil = new ShareUtil(activity, images);
            Intent baseIntent = shareUtil.getBaseIntent(ActivityName);
            baseIntent.putExtra("summary", msg);
            activity.startActivity(baseIntent);
        } else {
            Toast.makeText(activity, "您还没有安装QQ！", Toast.LENGTH_SHORT).show();
        }
    }



    public static void shareText(Activity activity, String msg){
        Intent intent = new Intent(Intent.ACTION_SEND); // 地址
        ComponentName component = new ComponentName(
                "com.tencent.mobileqq",
                "com.tencent.mobileqq.activity.JumpActivity");
        intent.setComponent(component);
        intent.putExtra(Intent.EXTRA_TEXT,msg);
        intent.setType("text/plain");
        activity.startActivity(Intent.createChooser(intent, "分享"));
    }







    public static void shareImage(Activity activity,String imgpath){
        try {

            File file = new File(imgpath);

            if(!file.exists()){
                ToastUtil.show(activity,"图片文件不存在");
                return;
            }

            Uri u = Uri.fromFile(file);

            Intent intent = new Intent(Intent.ACTION_SEND); // 地址
            ComponentName component = new ComponentName(
                    "com.tencent.mobileqq",
                    "com.tencent.mobileqq.activity.JumpActivity");
            intent.setComponent(component);

            intent.putExtra(Intent.EXTRA_STREAM, u);
            intent.setType("image/*");
            activity.startActivity(Intent.createChooser(intent, "分享"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //
    public static void shareImage(Activity activity, ArrayList<Uri> images) {
        if (AppInstallHelper.isInstalled(activity, PackageName,
                ActivityName)) {
            ShareUtil shareUtil = new ShareUtil(activity, images);
            Intent baseIntent = shareUtil.getBaseIntent(ActivityName);

            activity.startActivity(baseIntent);
        } else {
            Toast.makeText(activity, "您还没有安装QQ！", Toast.LENGTH_SHORT).show();
        }
    }



}
