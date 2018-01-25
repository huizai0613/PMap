package xxzx.login;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Properties;

import xxzx.activity.R;
import xxzx.publicClass.MD5;
import xxzx.publicClass.MyHttpRequst;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.ToastUtil;

/**
 * Created by ch on 2017/3/15.
 *
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class PswModifyTask extends AsyncTask<Void, Void, Integer> {

    private User user;
    private String newPsw;
    private Context context;
    /**
     * 一定一个接口
     */
    public interface ICoallBack {
        public void onCompleted(int result);
    }

    /**
     * 初始化接口变量
     */
    ICoallBack icallBack = null;

    /**
     * 自定义控件的自定义事件
     *
     * @param iBack
     *            接口类型
     **/
    public void setOnCompleted(ICoallBack iBack) {
        icallBack = iBack;
    }


    /**
     * 构造函数
     */
    public PswModifyTask() {
    }

    /**
     * 设置用户信息
     * @param newpsw
     */
    public void setNewPsw(User user,String newpsw,Context context){
        this.user = user;
        this.newPsw = newpsw;
        this.context = context;
    }


    @Override
    protected Integer doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.
        try {
            // Simulate network access.
            MySingleClass mySingleClass = MySingleClass.getInstance();
            Properties properties = mySingleClass.getProperties();

            String url = properties.get("url_psw_modify2").toString();

            String poststring = properties.get("url_psw_modify2_poststring").toString();
            poststring  = String.format(poststring, this.user.getmName(), newPsw);

            String result = MyHttpRequst.getHttpPostRequst2(url,poststring);

            if (result.equals("")) {//表示错误
                return HttpRequestResult.ERROR;
            } if (result.equals("True")){
                return HttpRequestResult.SUCCESS;
            }else if(result.equals("False")){
                return HttpRequestResult.FAIL;
            }else{
                return HttpRequestResult.FAIL;
            }
        } catch (Exception e) {
            return HttpRequestResult.ERROR;
        }
    }

    @Override
    protected void onPostExecute(final Integer result) {
        MySingleClass mySingleClass = MySingleClass.getInstance();
        if(result==HttpRequestResult.SUCCESS){
            ToastUtil.show(context, "密码修改成功");
        }else if(result==HttpRequestResult.ERROR) {
            ToastUtil.show(context, "密码修改错误,请检查网络是否连接");
        }else{
            ToastUtil.show(context, "密码修改错误");
        }
        //完成返回
        if (icallBack != null) {
            icallBack.onCompleted(result);
        }
    }

    @Override
    protected void onCancelled() {
        //取消完成返回
        if (icallBack != null) {
            icallBack.onCompleted(HttpRequestResult.CANCEL);
        }
    }
}
