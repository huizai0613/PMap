package xxzx.share.wx;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2017/6/9.
 */

public class ShareToWeixinFriend {
    private static final String PackageName = "com.tencent.mm";
    private static final String ActivityName = "com.tencent.mm.ui.tools.ShareImgUI";

    public static void shareImgs(Activity activity, ArrayList<Uri> images) {
        if (AppInstallHelper.isInstalled(activity, PackageName,ActivityName)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("image/*");
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, images);
            intent.setClassName(PackageName, ActivityName);
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, "您还没有安装微信！", Toast.LENGTH_SHORT).show();
        }
    }

    public static void shareText(Activity activity,String msg) {
        if (AppInstallHelper.isInstalled(activity, PackageName,
                ActivityName)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, msg);
            intent.setType("text/*");
            intent.setClassName(PackageName, ActivityName);
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, "您还没有安装微信！", Toast.LENGTH_SHORT).show();
        }
    }
}