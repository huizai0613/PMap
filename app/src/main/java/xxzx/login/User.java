package xxzx.login;

/**
 * Created by mulin on 2015/11/15.
 */
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class User {
    private String mName;
    private String mPwd;
    private Boolean mAutoLogin;//自动登录
    private Boolean mRemPwd;//记住密码
    private Boolean loginSuccess;//记住登录是否成功

    private static final String masterPassword = "FORYOU"; // AES加密算法的种子
    private static final String JSON_NAME = "user_name";
    private static final String JSON_PWD = "user_pwd";
    private static final String JSON_AUTOLOGIN = "auto_login";
    private static final String JSON_REMPWD = "rem_pwd";
    private static final String JSON_LOGINSTATE = "login_state";
    private static final String TAG = "User";

    public User() {
        this.mName="";
        this.mPwd="";
        this.mAutoLogin=false;
        this.mRemPwd=false;
        this.loginSuccess=false;

    }

    public User(JSONObject json) throws Exception {
        if (json.has(JSON_NAME)) {
            String name = json.getString(JSON_NAME);
            String pwd = json.getString(JSON_PWD);
            mAutoLogin = json.getBoolean(JSON_AUTOLOGIN);
            mRemPwd = json.getBoolean(JSON_REMPWD);

            // 解密后存放
            mName = AESUtils.decrypt(masterPassword, name);
            mPwd = AESUtils.decrypt(masterPassword, pwd);

            if(!json.has(JSON_LOGINSTATE)){
                loginSuccess = true;
            }else {
                loginSuccess = json.getBoolean(JSON_LOGINSTATE);
            }
        }
    }

    public JSONObject toJSON() throws Exception {
        // 使用AES加密算法加密后保存
        String name = AESUtils.encrypt(masterPassword, mName);
        String pwd = AESUtils.encrypt(masterPassword, mPwd);

        Log.i(TAG, "加密后:" + name + "  " + pwd);
        JSONObject json = new JSONObject();
        try {
            json.put(JSON_NAME, name);
            json.put(JSON_PWD, pwd);
            json.put(JSON_AUTOLOGIN, mAutoLogin);
            json.put(JSON_REMPWD, mRemPwd);
            json.put(JSON_LOGINSTATE, loginSuccess);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPwd() {
        return mPwd;
    }

    public void setmPwd(String mPwd) {
        this.mPwd = mPwd;
    }

    public Boolean getmAutoLogin() {
        return mAutoLogin;
    }

    public void setmAutoLogin(Boolean mAutoLogin) {
        this.mAutoLogin = mAutoLogin;
    }

    public Boolean getmRemPwd() {
        return mRemPwd;
    }

    public void setmRemPwd(Boolean mRemPwd) {
        this.mRemPwd = mRemPwd;
    }

    public Boolean getLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(Boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }
}
