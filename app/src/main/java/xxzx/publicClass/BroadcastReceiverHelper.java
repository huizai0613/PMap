package xxzx.publicClass;


import android.app.Notification;
import android.app.NotificationManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


public class BroadcastReceiverHelper extends BroadcastReceiver {

	NotificationManager mn = null;
	Notification notification = null;
	Context ct = null;
	BroadcastReceiverHelper receiver;

	// ----------------定义事件-----------------------------
	/**
	 * 一定一个接口
	 */
	public interface IOnReceiveIntent {
		public void onReceive(Intent intent);
	}

	/**
	 * 初始化接口变量
	 */
	IOnReceiveIntent iOnReceiveIntent = null;

	/**
	 * 自定义控件的自定义事件
	 * 
	 * @param iBack
	 *            接口类型
	 **/
	public void setOnReceiveIntent(IOnReceiveIntent iBack) {
		iOnReceiveIntent = iBack;
	}
	
	//--------------------------------------------

	public BroadcastReceiverHelper(Context c) {
		ct = c;
		receiver = this;
	}

	// 注册
	public void registerAction(String action) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(action);
		ct.registerReceiver(receiver, filter);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		if (iOnReceiveIntent != null) {
			iOnReceiveIntent.onReceive(intent);
		}
	}
}
