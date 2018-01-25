package xxzx.myView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import xxzx.activity.R;


public class ImgButtonHorizontal extends ImgBaseButton {

	private int DefaultResouceImageId = -1;
	private ImageView mImage = null;
	private TextView mText = null;

	private boolean IsSelected = false;


	public ImgButtonHorizontal(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.button_img_horizontal, this);

		//RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
		RelativeLayout relativeLayout2 = (RelativeLayout) findViewById(R.id.relativeLayout2);
		mImage = (ImageView) findViewById(R.id.image);
		mText = (TextView) findViewById(R.id.text);

		// TODO Auto-generated constructor stub
		int resouceTextId = -1;
		int resouceImageId = -1;
		int resouceTextColorId = -1;
		int resouceTextSizeId = -1;

		resouceTextId = attrs.getAttributeResourceValue(null, "title", 0);
		resouceImageId = attrs.getAttributeResourceValue(null, "iconSrc", 0);
		resouceTextColorId = attrs.getAttributeResourceValue(null, "titleColor", 0);
		resouceTextSizeId = attrs.getAttributeResourceValue(null, "titleSize", 0);

		if (resouceTextId > 0) {
			String textStr = context.getResources().getText(resouceTextId).toString();
			mText.setText(textStr);
		}

		if (resouceImageId > 0) {
			mImage.setImageDrawable(getResources().getDrawable(resouceImageId));
			this.DefaultResouceImageId = resouceImageId;
		}

		if (resouceTextColorId > 0) {
			mText.setTextColor(getResources().getColor(resouceTextColorId));
		}
//		if (resouceTextSizeId > 0) {
//			mText.setTextColor(getResources().get(resouceTextColorId));
//		}

		// 点击事件处理函数
		relativeLayout2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (icallBack != null) {
					icallBack.onClick(ImgButtonHorizontal.this);
				}
			}
		});
	}

	/**
	 * 选中的状态，图片改变，将这个函数写出来，是因为有些具有这个状态，有些则没有
	 */
	public void setSelectState(boolean isSelect) {
		this.IsSelected = isSelect;

		if (isSelect) {
			//mImage.setImageDrawable(getResources().getDrawable(R.drawable.topbar_selected));
		} else {
			if (DefaultResouceImageId > -1)
				mImage.setImageDrawable(getResources().getDrawable(DefaultResouceImageId));
		}
	}

	public boolean getSelectState() {
		return IsSelected;
	}

	public void setTitle(String title){
		mText.setText(title);
	}

	public void setImage(Drawable drawable){
		mImage.setBackground(drawable);//.setImageDrawable(drawable);
	}
}
