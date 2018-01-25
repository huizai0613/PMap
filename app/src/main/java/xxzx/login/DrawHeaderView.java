package xxzx.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import xxzx.activity.LoginActivity;
import xxzx.activity.R;
import xxzx.library.VectorDrawable;
import xxzx.publicClass.MyHttpRequst;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.NetworkConnected;
import xxzx.publicClass.ToastUtil;


public class DrawHeaderView extends LinearLayout {

	private Context mContext;

	private UserLoginTask mAuthTask = null;

	private User user;

	private TextView id_username;
	private ImageView iv_user = null;
	private TextView tv_date;
	private Button btn_loginout;

	public DrawHeaderView(Context context) {
		super(context);
		this.mContext=context;
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_draw_header, this);

		id_username =(TextView)findViewById(R.id.id_username);
		id_username.setText("未登录");

		iv_user =(ImageView)findViewById(R.id.iv_user);
		tv_date=(TextView)findViewById(R.id.id_date);
		btn_loginout=(Button)findViewById(R.id.btn_loginout);

		this.initData();
	}


	private void initData() {
		//通过SimpleDateFormat获取24小时制时间
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		tv_date.setText(sdf.format(new java.util.Date()));

		iv_user.setBackground(VectorDrawable.getDrawable(mContext, R.drawable.iconfont_user));

		this.user = LoginFileUtils.readLoginJsonFile();

		this.mAuthTask = new UserLoginTask();

		//设置当前软件未登录状态
		MySingleClass.getInstance().setLoginState(LoginState.FAIL_LOGIN);


		if (this.user != null) {
			boolean auto=this.user.getmAutoLogin();
			if(auto){
				this.mAuthTask.setUser(this.user, mContext);
				this.mAuthTask.execute((Void) null);
			}else{
				ToastUtil.show(mContext, "用户未登录");
			}
		}else{
			this.user = new User();
		}

		MySingleClass.getInstance().setUser(this.user);

		btn_loginout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				User user = MySingleClass.getInstance().getUser();
				int state = MySingleClass.getInstance().getLoginState();
				//如果当前在线登录
				if (state == LoginState.SUCCESS_LOGIN_ONLINE) {
					((Button) v).setText("登录");
					id_username.setText("未登录");
					//设置当前软件未登录状态
					MySingleClass.getInstance().setLoginState(LoginState.FAIL_LOGIN);
					//修改自动登录,一旦退出登录，下次将不再自动登录,并且记录登录情况
					user.setmAutoLogin(false);
					user.setLoginSuccess(false);
					//重新写入用户登录信息
					LoginFileUtils.writeLoginJsonFile(user);
				} else {
					Intent intent = new Intent();
					intent.setClass(mContext, LoginActivity.class);
					((Activity) mContext).startActivityForResult(intent, MyString.requestCode_mainactivity_to_loginactivity);
				}
			}
		});


		this.mAuthTask.setOnCompleted(new UserLoginTask.ICoallBack() {
			@Override
			public void onCompleted(int result) {
				//网络连接错误
				if(result==HttpRequestResult.ERROR){
					//采用离线登录方式
					if(user.getLoginSuccess()){
						MySingleClass.getInstance().setLoginState(LoginState.SUCCESS_LOGIN_UNLINE);
						ToastUtil.show(mContext, "当前为离线登录状态，不支持下载、上传等功能");
						MySingleClass.getInstance().setUser(user);
						id_username.setText(user.getmName()+"-" + "离线");
					}
				}else if(result==HttpRequestResult.SUCCESS){
					setLoginViewState(true);
				}else{
					setLoginViewState(false);
				}

			}
		});
	}


	/**
	 * 设置登录状态，登录、未登录
	 * @param issuccess true登录，false未登录
	 */
	public void setLoginViewState(boolean issuccess){
		//记住该用户名
		MySingleClass mySingleClass = MySingleClass.getInstance();
		if(issuccess) {
			this.user = mySingleClass.getUser();
			id_username.setText(this.user.getmName());
			btn_loginout.setText("注销");
		}else{
			id_username.setText("未登录");
			btn_loginout.setText("登录");
		}
	}








}
