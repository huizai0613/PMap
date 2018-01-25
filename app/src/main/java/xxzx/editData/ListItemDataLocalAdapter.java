package xxzx.editData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.R;
import xxzx.library.VectorDrawable;
import xxzx.publicClass.MyListView.ListViewCommonHolder;

/**
 * 首页ListView的数据适配器
 * 
 * @author Administrator
 * 
 */
public class ListItemDataLocalAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private ArrayList<EditDataInfo> items;
	private List<Integer> isselectedList;
	private boolean isSelected = false;

	public ListItemDataLocalAdapter(Context ctx, ArrayList<EditDataInfo> items, List<Integer> _selectedList) {
		mInflater = LayoutInflater.from(ctx);
		this.mContext = ctx;
		this.items = items;
		this.isselectedList = _selectedList;
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
			convertView = mInflater.inflate(R.layout.list_item_data_local, null);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.iv_icon.setBackground(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_vex));
//			holder.imgbtn_select = (ImgButton) convertView.findViewById(R.id.btn_select);
//			holder.btn_select.setImage(VectorDrawable.getDrawable(mContext, R.drawable.iconfont_listitem_arrowright));

			convertView.setTag(holder);
		} else {
			holder = (ListViewCommonHolder) convertView.getTag();
		}

		String title = items.get(position).getTitle();

		if(title.length()>13){
			title=title.substring(0,13)+"...";
		}

		holder.tv_title.setText(title);
		holder.tv_content.setText(items.get(position).getDescribtion());

        //如果为空。该控件不显示
		return convertView;
	}
}
