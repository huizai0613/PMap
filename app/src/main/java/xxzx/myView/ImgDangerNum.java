package xxzx.myView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import xxzx.activity.Patrol.Record.RecordChnAndPoleActivity;
import xxzx.activity.R;
import xxzx.library.VectorDrawable;
import xxzx.login.CheckLogin;
import xxzx.spatialite.TableOptClass.ChannelDangerTableOpt;
import xxzx.spatialite.TableOptClass.PoleDangerTableOpt;
import xxzx.spatialite.TableStruct.ChannelDangerTableDataClass;
import xxzx.spatialite.TableStruct.PoleDangerTableDataClass;


public class ImgDangerNum extends LinearLayout {

	private Context mContext;
	private ImageView mImage = null;
	private TextView mText = null;

	public ImgDangerNum(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.mContext=context;

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.button_danger_num, this);
		mImage = (ImageView) findViewById(R.id.image);
		mImage.setBackground(VectorDrawable.getDrawable(this.mContext, R.drawable.iconfont_danger_num_bubble));

		mText = (TextView) findViewById(R.id.text);

		this.initData();
	}


	public void initData(){
		PoleDangerTableOpt poleDangerTableOpt = new PoleDangerTableOpt();
		List<PoleDangerTableDataClass> list_poledanger = poleDangerTableOpt.getRowFromUpdate();

		ChannelDangerTableOpt channelDangerTableOpt=new ChannelDangerTableOpt();
		List<ChannelDangerTableDataClass> list_channeldanger = channelDangerTableOpt.getRowFromUpdate();

		int countDanger = list_poledanger.size()+list_channeldanger.size();

		if(countDanger==0){
			mImage.setVisibility(GONE);
			mText.setVisibility(GONE);
			mText.setText("");
			return;
		}

		mImage.setVisibility(VISIBLE);
		mText.setVisibility(VISIBLE);
		mText.setText(String.valueOf(countDanger));
	}
}
