package xxzx.patrol.Danger;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.R;

/**
 * Created by ch on 2016/4/4.
 */
public class ListItemDangerStatisticsAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<DangerStatisticsClass> items;

    public ListItemDangerStatisticsAdapter(Context ctx, List<DangerStatisticsClass> items) {
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
        ListViewDangerHolder holder;

        if (convertView == null) {
            holder = new ListViewDangerHolder();
            convertView = mInflater.inflate(R.layout.list_item_danger_statistics, null);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.rl_levelColor = (RelativeLayout) convertView.findViewById(R.id.rl_levelColor);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);

            convertView.setTag(holder);

        } else {
            holder = (ListViewDangerHolder) convertView.getTag();
        }

        String title = items.get(position).title;

        if (title.length() > 20) {
            title = title.substring(0, 20) + "...";
        }

        holder.tv_title.setText(title);
        String dangerNum =items.get(position).num==0?"":String.valueOf(items.get(position).num);
        holder.tv_num.setText(dangerNum);
        switch (items.get(position).maxLevel) {
            case 0:
                holder.rl_levelColor.setBackground(mContext.getResources().getDrawable(R.drawable.patrol_statistic_level_bg_circle_green));
                break;
            case 1:
                holder.rl_levelColor.setBackground(mContext.getResources().getDrawable(R.drawable.patrol_statistic_level_bg_circle_blue));
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
}
