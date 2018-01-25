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


public class ImgButton extends ImgBaseButton {

	private int DefaultResouceImageId = -1;
	private ImageView mImage = null;

	private Boolean isselected=false;

	public ImgButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.button_img, this);

		RelativeLayout relativeLayout2 = (RelativeLayout) findViewById(R.id.relativeLayout2);
		mImage = (ImageView) findViewById(R.id.image);

		// TODO Auto-generated constructor stub
		int resouceImageId = -1;
		resouceImageId = attrs.getAttributeResourceValue(null, "iconSrc", 0);

		if (resouceImageId > 0) {
			mImage.setImageDrawable(getResources().getDrawable(resouceImageId));
			this.DefaultResouceImageId = resouceImageId;
		}

		// 点击事件处理函数
		relativeLayout2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (icallBack != null) {
					icallBack.onClick(ImgButton.this);
				}
			}
		});
	}

	public void setImage(Drawable drawable){
		mImage.setBackground(drawable);
	}

	public Boolean getIsselected() {
		return isselected;
	}

	public void setIsselected(Boolean isselected) {
		this.isselected = isselected;
	}
}
