package xxzx.routePlanning;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;

import java.util.List;

import xxzx.activity.R;
import xxzx.library.VectorDrawable;

/**
 * 首页ListView的数据适配器
 * 
 * @author Administrator
 * 
 */
public class ListItemPoiResultAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private List<PoiItem> items;


	public ListItemPoiResultAdapter(Context ctx, List<PoiItem> items) {

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
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.list_item_poi_result, null);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.iv_icon.setBackground(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_maplocation));

			holder.iv_arrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
			holder.iv_arrow.setBackground(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_listitem_arrowright));

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String title = items.get(position).getTitle();
		String address = items.get(position).getSnippet();
		if(title.length()>15){
			title = title.substring(0,15)+"...";
		}

		holder.tv_title.setText(title);
		holder.tv_address.setText(address);
		return convertView;
	}


	/**
	 * listview组件复用，防止“卡顿”
	 * 
	 * @author Administrator
	 * 
	 */
	class ViewHolder {
		private TextView tv_title;
		private TextView tv_address;
		private ImageView iv_icon;
		private ImageView iv_arrow;
	}
}
