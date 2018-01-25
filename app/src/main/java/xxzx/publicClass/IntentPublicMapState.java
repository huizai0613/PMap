package xxzx.publicClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import xxzx.activity.MainDrawerActivity;
import xxzx.activity.PublicMapActivity;

/**
 * Created by ch on 2016/2/27.
 * 切换到地图状态的类
 */
public class IntentPublicMapState {

    public static void  Intent(Context context,int requestCode,String state,String data){
        Intent intent = new Intent();
        intent.putExtra("state", state);
        intent.putExtra("data", data);
        intent.setClass(context, PublicMapActivity.class);
        ((Activity)context).startActivityForResult(intent,requestCode);
    }
}
