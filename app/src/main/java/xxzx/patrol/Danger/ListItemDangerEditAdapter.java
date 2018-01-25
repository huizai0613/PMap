package xxzx.patrol.Danger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import xxzx.activity.R;
import xxzx.library.VectorDrawable;
import xxzx.publicClass.MyString;
import xxzx.spatialite.TableStruct.BaseDangerTableDataClass;

/**
 * Created by ch on 2016/4/4.
 */
public class ListItemDangerEditAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<BaseDangerTableDataClass> items;

    public ListItemDangerEditAdapter(Context ctx, List<BaseDangerTableDataClass> items) {
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
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            convertView.setTag(holder);
        } else {
            holder = (ListViewDangerHolder) convertView.getTag();
        }

        String title = items.get(position).getDangerName();

        if (title.length() > 13) {
            title = title.substring(0, 13) + "...";
        }

        holder.tv_title.setText(title);
        holder.rowid = items.get(position).getRowId();
        holder.keyid= items.get(position).getKeyID();
        holder.iv_icon.setBackground(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_danger));

//        holder.rl_levelColor.setBackground(mContext.getResources().getDrawable(R.drawable.patrol_statistic_level_bg_circle_green));
//        holder.tv_num.setText(MyString.dangerLevels[items.get(position).getDangerLevel()]);


        switch (items.get(position).getDangerLevel()) {
            case 0:
                holder.rl_levelColor.setBackground(mContext.getResources().getDrawable(R.drawable.patrol_statistic_level_bg_circle_green));
                holder.tv_num.setText(MyString.dangerLevels[0]);
                break;
            case 1:
                holder.rl_levelColor.setBackground(mContext.getResources().getDrawable(R.drawable.patrol_statistic_level_bg_circle_blue));
                holder.tv_num.setText(MyString.dangerLevels[1]);
                break;
            case 2:
                holder.rl_levelColor.setBackground(mContext.getResources().getDrawable(R.drawable.patrol_statistic_level_bg_circle_orange));
                holder.tv_num.setText(MyString.dangerLevels[2]);
                break;
            case 3:
                holder.rl_levelColor.setBackground(mContext.getResources().getDrawable(R.drawable.patrol_statistic_level_bg_circle_red));
                holder.tv_num.setText(MyString.dangerLevels[3]);
                break;
            default:
                break;
        }

        return convertView;
    }
}
