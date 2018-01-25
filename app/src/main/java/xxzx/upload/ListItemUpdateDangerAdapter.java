package xxzx.upload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import xxzx.activity.R;
import xxzx.library.VectorDrawable;
import xxzx.patrol.Record.ChnOrPole;
import xxzx.publicClass.MyString;

/**
 * 首页ListView的数据适配器
 * 
 * @author Administrator
 * 
 */
public class ListItemUpdateDangerAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private ArrayList<ListItemUpdateDangerClass> items;

	SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat  dateformat = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat  timeformat = new SimpleDateFormat("HH:mm:ss");

	public ListItemUpdateDangerAdapter(Context ctx, ArrayList<ListItemUpdateDangerClass> items) {

		mInflater = LayoutInflater.from(ctx);
		this.mContext = ctx;
		this.items = items;

	}

	@Override
	public int getCount() {
		return items == null ? 0 : items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ListViewHolder holder;
		if (convertView == null) {
			holder = new ListViewHolder();
			convertView = mInflater.inflate(R.layout.list_item_arrow_update_danger,null);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_dangertype = (TextView) convertView.findViewById(R.id.tv_dangertype);
			holder.tv_date= (TextView) convertView.findViewById(R.id.tv_date);
			holder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_level = (TextView) convertView.findViewById(R.id.tv_level);
			holder.rl_levelColor = (RelativeLayout) convertView.findViewById(R.id.rl_levelColor);
			holder.iv_arrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
			holder.iv_arrow.setBackground(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_listitem_arrowright));
			convertView.setTag(holder);
		} else {
			holder = (ListViewHolder) convertView.getTag();
		}

		String title = items.get(position).getTitle();

		if(title.length()>30){
			title=title.substring(0,30)+"...";
		}

		holder.tv_title.setText(title);

		if(items.get(position).getChnOrpole()== ChnOrPole.POLE) {
			holder.tv_dangertype.setText(MyString.poleDangerTypes_nonum[items.get(position).getDangerType()]);
		}else if(items.get(position).getChnOrpole()== ChnOrPole.CHANNEL){
			holder.tv_dangertype.setText(MyString.channelDangerTypes_nonum[items.get(position).getDangerType()]);
		}

		try {
			Date date = format.parse(items.get(position).getDate());
			String date_str = dateformat.format(date);
			String time_str = timeformat.format(date);
			holder.tv_date.setText(date_str);
			holder.tv_time.setText(time_str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int level=items.get(position).getDangetLevel();
		holder.tv_level.setText(MyString.dangerLevels[level]);

		switch (level) {
			case 0:
				holder.rl_levelColor.setBackground(mContext.getResources().getDrawable(R.drawable.patrol_statistic_level_bg_circle_blue));
				break;
			case 1:
				holder.rl_levelColor.setBackground(mContext.getResources().getDrawable(R.drawable.patrol_statistic_level_bg_circle_green));
				break;
			case 2:
				holder.rl_levelColor.setBackground(mContext.getResources().getDrawable(R.drawable.patrol_statistic_level_bg_circle_orange));
				break;
			case 3:
				holder.rl_levelColor.setBackground(mContext.getResources().getDrawable(R.drawable.patrol_statistic_level_bg_circle_red));
				break;
			default:
				holder.rl_levelColor.setBackground(mContext.getResources().getDrawable(R.drawable.patrol_statistic_level_bg_circle_default));
				break;
		}

		return convertView;
	}



	public class ListViewHolder{

		public TextView tv_title;
		public TextView tv_date;
		public TextView tv_time;
		public TextView tv_dangertype;
		public TextView tv_level;
		public ImageView iv_arrow;
		public RelativeLayout rl_levelColor;




	}




}
