package xxzx.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.PublicBaseActivity.BaseToolBarActivity;
import xxzx.login.HttpRequestResult;
import xxzx.login.LoginFileUtils;
import xxzx.login.ModifyPswDlgFragment;
import xxzx.login.User;
import xxzx.login.UserLoginTask;
import xxzx.publicClass.LoadingDialog;
import xxzx.publicClass.MySingleClass;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseToolBarActivity implements LoaderCallbacks<Cursor>
{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private CheckBox cb_remPwd;
    private CheckBox cb_autoLogin;
    private User user;
    private LoadingDialog loadingDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        this.mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);

        populateAutoComplete();

        this.mPasswordView = (EditText) findViewById(R.id.password);

        this.loadingDialog = new LoadingDialog(this);

        Button btn_sign = (Button) findViewById(R.id.name_sign_in_button);
        btn_sign.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                attemptLogin();
            }
        });

        cb_remPwd = (CheckBox) findViewById(R.id.cb_remPwd);
        cb_autoLogin = (CheckBox) findViewById(R.id.cb_autoLogin);

        //工具栏上的箭头
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        // 决定左上角图标的右侧是否有向左的小箭头, true
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 有小箭头，并且图标可以点击
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        this.initData();
    }

    @Override
    public void onCreateCustomToolBar(Toolbar toolbar)
    {
        super.onCreateCustomToolBar(toolbar);

    }


    private void initData()
    {
        this.user = MySingleClass.getInstance().getUser();
        if (this.user != null) {
            if (this.user.getmRemPwd()) {
                cb_remPwd.setChecked(true);
                mUsernameView.setText(user.getmName());
                mPasswordView.setText(user.getmPwd());
            }
            if (user.getmAutoLogin()) {
                cb_autoLogin.setChecked(true);
            }
        }

        cb_remPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                user.setmRemPwd(cb_remPwd.isChecked());
                LoginFileUtils.writeLoginJsonFile(user);
            }
        });

        cb_autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                user.setmAutoLogin(cb_autoLogin.isChecked());
                LoginFileUtils.writeLoginJsonFile(user);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        // 点击返回图标事件
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateAutoComplete()
    {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mUsernameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener()
                    {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v)
                        {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin()
    {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            this.user.setmName(username);
            this.user.setmPwd(password);
            this.user.setmRemPwd(cb_remPwd.isChecked());
            this.user.setmAutoLogin(cb_autoLogin.isChecked());
            this.user.setLoginSuccess(false);

            this.mAuthTask = new UserLoginTask();
            this.mAuthTask.setUser(this.user, LoginActivity.this);
            this.mAuthTask.setOnCompleted(new UserLoginTask.ICoallBack()
            {
                @Override
                public void onCompleted(int result)
                {
                    showProgress(false);
                    mAuthTask = null;
                    if (result == HttpRequestResult.SUCCESS) {

                        loginSuccess(user);

                    } else if (result == HttpRequestResult.SUCCESS_FIRSTLOGIN) {//修改密码
                        loginSuccess(user);
                        ModifyPswDlgFragment modifyPswDlgFragment = new ModifyPswDlgFragment();
                        modifyPswDlgFragment.setUser(user);
                        modifyPswDlgFragment.show(getFragmentManager(), "ModifyPswDlgFragment");
                        modifyPswDlgFragment.setOnCompleted(new ModifyPswDlgFragment.ICoallBack()
                        {
                            @Override
                            public void onCompleted(boolean result, User user)
                            {
                                if (result) {
                                    loginSuccess(user);
                                }
                            }
                        });

                    } else {
                        user.setLoginSuccess(false);
                    }
                }
            });

            this.mAuthTask.execute((Void) null);
        }
    }


    /**
     * 登陆成功后的操作
     *
     * @param user
     */
    private void loginSuccess(User user)
    {
        //设置登录成功
        user.setLoginSuccess(true);
        SharedPreferences pref = getSharedPreferences("data", MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("USERNAME", user.getmName());
        editor.putBoolean("LOGINED", true);
        editor.commit();
        MySingleClass.getInstance().setUser(user);
        //写入文件
        LoginFileUtils.writeLoginJsonFile(user);

        Intent intent = new Intent();
        intent.putExtra("loginstate", true);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        if (show) {
            this.loadingDialog.show();
        } else {
            this.loadingDialog.dismiss();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
    {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader)
    {

    }

    private interface ProfileQuery
    {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection)
    {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUsernameView.setAdapter(adapter);
    }
}

