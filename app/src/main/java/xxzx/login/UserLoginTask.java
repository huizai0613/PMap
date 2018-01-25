package xxzx.login;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.Properties;

import xxzx.activity.R;
import xxzx.publicClass.MD5;
import xxzx.publicClass.MyHttpRequst;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.ToastUtil;

/**
 * Created by ch on 2017/3/15.
 * <p>
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class UserLoginTask extends AsyncTask<Void, Void, Integer>
{

    private User user;
    private Context context;

    /**
     * 一定一个接口
     */
    public interface ICoallBack
    {
        public void onCompleted(int result);
    }

    /**
     * 初始化接口变量
     */
    ICoallBack icallBack = null;

    /**
     * 自定义控件的自定义事件
     *
     * @param iBack 接口类型
     **/
    public void setOnCompleted(ICoallBack iBack)
    {
        icallBack = iBack;
    }


    /**
     * 构造函数
     */
    public UserLoginTask()
    {
    }

    /**
     * 设置用户信息
     *
     * @param user
     */
    public void setUser(User user, Context context)
    {
        this.user = user;
        this.context = context;
    }


    @Override
    protected Integer doInBackground(Void... params)
    {
        // TODO: attempt authentication against a network service.
        try {
            // Simulate network access.
            MySingleClass mySingleClass = MySingleClass.getInstance();
            Properties properties = mySingleClass.getProperties();

            String url = properties.get("url_login2").toString();

            String poststring = properties.get("url_login2_poststring").toString();

            poststring = String.format(poststring, this.user.getmName(), MD5.getMD5(this.user.getmPwd()));

            String result = MyHttpRequst.getHttpPostRequst2(url, poststring);

            if (result.equals("")) {//表示错误

                return HttpRequestResult.ERROR;

            } else {
                JSONObject jsonObject = new JSONObject(result);
                String msg = jsonObject.getString("Msg");
                if (msg.equals("3")) {//表示验证正确
                    Boolean firstlogin = jsonObject.getBoolean("FirstLogin");
                    if (firstlogin) {
                        return HttpRequestResult.SUCCESS_FIRSTLOGIN;
                    } else {
                        return HttpRequestResult.SUCCESS;
                    }
                } else if (msg.equals("2")) {//表示登陆密码被锁定
                    return HttpRequestResult.LOCK;
                } else {
                    return HttpRequestResult.FAIL;
                }
            }
        } catch (Exception e) {
            return HttpRequestResult.ERROR;
        }
    }

    @Override
    protected void onPostExecute(final Integer result)
    {
        MySingleClass mySingleClass = MySingleClass.getInstance();
        //记住登录情况
        if (result == HttpRequestResult.SUCCESS || result == HttpRequestResult.SUCCESS_FIRSTLOGIN) {
            //表示在线登录成功
            mySingleClass.setLoginState(LoginState.SUCCESS_LOGIN_ONLINE);
            ToastUtil.show(context, context.getString(R.string.success_login));
        } else if (result == HttpRequestResult.ERROR) {
            ToastUtil.show(context, context.getString(R.string.error_login));
        } else if (result == HttpRequestResult.LOCK) {
            ToastUtil.show(context, context.getString(R.string.lock_login));
        } else {
            mySingleClass.setLoginState(LoginState.FAIL_LOGIN);
            ToastUtil.show(context, context.getString(R.string.fail_login));
        }
        //完成返回
        if (icallBack != null) {
            icallBack.onCompleted(result);
        }
    }

    @Override
    protected void onCancelled()
    {
        //取消完成返回
        if (icallBack != null) {
            icallBack.onCompleted(HttpRequestResult.CANCEL);
        }
    }
}
