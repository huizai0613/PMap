package xxzx.publicClass.MyListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import xxzx.activity.R;
import xxzx.library.VectorDrawable;

/**
 * Created by ch on 2016/4/7.
 */
public class ListItemMenuAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<ListItemMenu> items;

    public ListItemMenuAdapter(Context ctx, List<ListItemMenu> items) {
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
            convertView = mInflater.inflate(R.layout.list_item_menu, null);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
//            holder.iv_icon.setBackground(VectorDrawable.getDrawable(mContext, R.drawable.iconfont_file));
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.iv_arrow = (ImageView) convertView.findViewById(R.id.iv_checked);

            convertView.setTag(holder);
        } else {
            holder = (ListViewCommonHolder) convertView.getTag();
        }

        String title = items.get(position).getTitle();
        if (title.length() > 20) {
            title = title.substring(0, 20) + "...";
        }
        holder.tv_title.setText(title);

        //修改icon
        if(items.get(position).getIcon()!=null) {
            (holder.iv_icon).setBackground(items.get(position).getIcon());
        }

        return convertView;
    }
}
