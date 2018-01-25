package xxzx.publicClass;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * Created by ch on 2016/4/26.
 */
public class AlertDialogUtil {

    public static void show(Context context,String title, String info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(info);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }
}
