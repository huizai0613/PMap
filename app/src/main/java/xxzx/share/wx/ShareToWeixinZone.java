package xxzx.share.wx;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2017/6/9.
 */

public class ShareToWeixinZone {

    private static final String PackageName = "com.tencent.mm";
    private static final String ActivityName = "com.tencent.mm.ui.tools.ShareToTimeLineUI";

    public static void share(Activity activity, String msg, ArrayList<Uri> images) {
        if (AppInstallHelper.isInstalled(activity, PackageName,
                ActivityName)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.putExtra("Kdescription", msg);
            intent.setType("image/*");
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, images);
            intent.setClassName(PackageName, ActivityName);
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, "您还没有安装微信！", Toast.LENGTH_SHORT).show();
        }
    }
}
