package xxzx.publicClass.MyListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.R;
import xxzx.library.VectorDrawable;
import xxzx.patrol.Record.ChnOrPole;
import xxzx.spatialite.TableOptClass.BaseTableOpt;
import xxzx.spatialite.TableOptClass.ChannelDangerTableOpt;
import xxzx.spatialite.TableOptClass.PoleDangerTableOpt;
import xxzx.spatialite.TableStruct.BaseDangerTableDataClass;
import xxzx.spatialite.TableStruct.ChannelDangerTableDataClass;
import xxzx.spatialite.TableStruct.PoleDangerTableDataClass;

/**
 * 首页ListView的数据适配器
 * 
 * @author Administrator
 * 
 */
public class ListItemPoleChnAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private ArrayList<ListItemOpt> items;


	public ListItemPoleChnAdapter(Context ctx, ArrayList<ListItemOpt> items) {

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
		ListViewCommonHolder holder;
		if (convertView == null) {
			holder = new ListViewCommonHolder();
			convertView = mInflater.inflate(R.layout.list_item_arrow_pole_chn, null);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			holder.rl_numColor = (RelativeLayout) convertView.findViewById(R.id.rl_numColor);

			holder.iv_icon.setBackground(items.get(position).getIcon());

			holder.iv_arrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
			holder.iv_arrow.setBackground(VectorDrawable.getDrawable(mContext, R.drawable.iconfont_listitem_arrowright));

			convertView.setTag(holder);
		} else {
			holder = (ListViewCommonHolder) convertView.getTag();
		}

		String title = items.get(position).getOptName();

		if (title.length() > 30) {
			title = title.substring(0, 30) + "...";
		}

		holder.tv_title.setText(title);

		//首先查询隐患表
		int dangerCount=0;
		if(items.get(position).getChnorpole()== ChnOrPole.POLE){
			PoleDangerTableOpt poleDangerTableOpt = new PoleDangerTableOpt();
			List<PoleDangerTableDataClass> list = poleDangerTableOpt.getRowFormObjectId(items.get(position).getObjectid());
			dangerCount = list.size();
		}else if(items.get(position).getChnorpole()== ChnOrPole.CHANNEL){
			ChannelDangerTableOpt channelDangerTableOpt = new ChannelDangerTableOpt();
			List<ChannelDangerTableDataClass> list = channelDangerTableOpt.getRowFromObjectId(items.get(position).getObjectid());
			dangerCount = list.size();
		}

		//如果为0，说明没有下载隐患数据，所以显示原始杆塔的隐患数据
		if (dangerCount > 0) {
			holder.rl_numColor.setVisibility(View.VISIBLE);
			holder.tv_num.setText(String.valueOf(dangerCount));
		} else {
			if (items.get(position).getDangerCount() > 0) {
				holder.rl_numColor.setVisibility(View.VISIBLE);
				holder.tv_num.setText(String.valueOf(items.get(position).getDangerCount()));
			} else {
				holder.rl_numColor.setVisibility(View.INVISIBLE);
			}
		}

		return convertView;
	}
}
