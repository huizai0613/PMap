package xxzx.login;

import android.content.Context;

import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.ToastUtil;

/**
 * Created by ch on 2017/1/16.
 */
public class CheckLogin {

    public static boolean isLogin(Context mContext){
        //判断登录状态
        MySingleClass mySingleClass=MySingleClass.getInstance();
        User user=mySingleClass.getUser();
        if(user==null){
            ToastUtil.show(mContext, "当前没有用户登录！");
            return false;
        }
        return true;
    }
}
