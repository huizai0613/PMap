package xxzx.upload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import xxzx.activity.R;
import xxzx.library.VectorDrawable;
import xxzx.publicClass.MyListView.ListViewCommonHolder;

/**
 * 首页ListView的数据适配器
 * 
 * @author Administrator
 * 
 */
public class ListItemUpdateChnPoleAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private ArrayList<ListItemUpdateChnPoleClass> items;


	public ListItemUpdateChnPoleAdapter(Context ctx, ArrayList<ListItemUpdateChnPoleClass> items) {

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
			convertView = mInflater.inflate(R.layout.list_item_arrow_update_chn_pole_num,null);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_num);
			holder.iv_arrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
			holder.iv_arrow.setBackground(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_listitem_arrowright));
			convertView.setTag(holder);
		} else {
			holder = (ListViewCommonHolder) convertView.getTag();
		}

		String title = items.get(position).getTitle();

		if(title.length()>20){
			title=title.substring(0,20)+"...";
		}

		holder.tv_title.setText(title);
		holder.iv_icon.setBackground(items.get(position).getIcon());
		holder.tv_content.setText(String.valueOf(items.get(position).getDangerNum()));
		return convertView;
	}
}
