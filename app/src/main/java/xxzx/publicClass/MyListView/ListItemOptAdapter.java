package xxzx.publicClass.MyListView;

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

/**
 * 首页ListView的数据适配器
 * 
 * @author Administrator
 * 
 */
public class ListItemOptAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private ArrayList<ListItemOpt> items;


	public ListItemOptAdapter(Context ctx, ArrayList<ListItemOpt> items) {

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
			convertView = mInflater.inflate(R.layout.list_item_arrow,null);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.iv_icon.setBackground(items.get(position).getIcon());

			holder.iv_arrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
			holder.iv_arrow.setBackground(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_listitem_arrowright));

			//holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);

			convertView.setTag(holder);
		} else {
			holder = (ListViewCommonHolder) convertView.getTag();
		}

		String title = items.get(position).getOptName();

		if(title.length()>30){
			title=title.substring(0,30)+"...";
		}

		holder.tv_title.setText(title);

		return convertView;
	}
}
