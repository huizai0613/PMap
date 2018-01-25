package xxzx.myView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import xxzx.activity.R;


public abstract class  ImgBaseButton extends LinearLayout {

	// ----------------定义点击事件-----------------------------
	/**
	 * 一定一个接口
	 */
	public interface ICoallBack {
		public void onClick(View view);
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
	public void setOnClick(ICoallBack iBack) {
		icallBack = iBack;
	}

	public ImgBaseButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
}
