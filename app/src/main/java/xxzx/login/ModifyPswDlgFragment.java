package xxzx.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xxzx.activity.R;
import xxzx.publicClass.LoadingDialog;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.ToastUtil;

/**
 * Created by Lenovo on 2017/6/13.
 */

public class ModifyPswDlgFragment extends DialogFragment implements DialogInterface.OnKeyListener{

    private EditText et_password;
    private EditText et_passwordconfirm;
    private LoadingDialog loadingDialog = null;
    private User user;
    private PswModifyTask mAuthTask = null;

    private static final int cancel_1 = 1;
    private static final int cancel_2 = 2;
    private static final int cancel_3 = 3;
    private static final int cancel_4 = 4;

    private boolean cancel = false;
    private View focusView = null;
    /**
     * 一定一个接口
     */
    public interface ICoallBack {
        public void onCompleted(boolean result,User user);
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



    public void setUser(User user1){
        this.user =user1;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            dismiss();
            return true;
        }else {
            //这里注意当不是返回键时需将事件扩散，否则无法处理其他点击事件
            return false;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialog_modify_psw, null);

        this.et_password = (EditText) view.findViewById(R.id.et_password);
        this.et_passwordconfirm = (EditText) view.findViewById(R.id.et_passwordconfirm);
        this.loadingDialog = new LoadingDialog(getActivity());
        Button btn_OK =  (Button) view.findViewById(R.id.btn_OK);

        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptModifyPsw();
            }
        });
        return view;
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptModifyPsw() {
        // Reset errors.
        et_password.setError(null);
        et_passwordconfirm.setError(null);

        this.cancel = false;

        this.checkNewPsw();

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            this.loadingDialog.show();

            this.mAuthTask = new PswModifyTask();
            this.mAuthTask.setNewPsw(this.user, et_password.getText().toString().trim(), getActivity());

            this.mAuthTask.setOnCompleted(new PswModifyTask.ICoallBack() {
                @Override
                public void onCompleted(int result) {

                    loadingDialog.dismiss();

                    mAuthTask = null;

                    if (result == HttpRequestResult.SUCCESS) {

                        user.setmPwd(et_password.getText().toString().trim());

                        ModifyPswDlgFragment.this.dismiss();

                        if(icallBack!=null){
                            icallBack.onCompleted(true,user);
                        }
                    } else {
                        if(icallBack!=null){
                            icallBack.onCompleted(false,null);
                        }
                    }
                }
            });

            this.mAuthTask.execute((Void) null);
        }
    }



    private void checkNewPsw(){
        // Store values at the time of the login attempt.
        final String password = et_password.getText().toString().trim();
        String password_confirm = et_passwordconfirm.getText().toString().trim();

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            et_password.setError("新密码不能为空");
            focusView = et_password;
            cancel = true;
            return;
        }



        if (password.equals(user.getmName())) {
            et_password.setError("新密码不能和用户名相同");
            focusView = et_password;
            cancel = true;
            return;
        }

        // 符合正则表达式
        String strPattern = "^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*]+$)(?![a-zA-z\\d]+$)(?![a-zA-z!@#$%^&*]+$)(?![\\d!@#$%^&*]+$)[a-zA-Z\\d!@#$%^&*]+.{7,}$";
        Pattern p = Pattern.compile(strPattern);
        Matcher p1 = p.matcher(password);


        if (!p1.matches()) {
            et_password.setError("必须为字母、数字、以及特殊符号的不低于8个字符的组合");
            focusView = et_password;
            cancel = true;
            return;
        }

        if (password.equals(user.getmPwd())) {
            et_password.setError("新密码不能和原始密码相同");
            focusView = et_password;
            cancel = true;
            return;
        }


        //检查是否相等
        if (!password.equals(password_confirm)) {
            et_passwordconfirm.setError("新密码和确认密码不相等");
            focusView = et_passwordconfirm;
            cancel = true;
            return;
        }
    }

}
