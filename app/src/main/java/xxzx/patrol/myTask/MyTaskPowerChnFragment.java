package xxzx.patrol.myTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.Patrol.ChnDangerStatisticsActivity;
import xxzx.activity.R;
import xxzx.library.VectorDrawable;
import xxzx.patrol.Record.ChnOrPole;
import xxzx.publicClass.MyListView.ListItemOpt;
import xxzx.publicClass.MyListView.ListItemPoleChnAdapter;
import xxzx.spatialite.TableOptClass.ChannelTableOpt;
import xxzx.spatialite.TableStruct.ChannelTableDataClass;


/**
 * Created by Daniel on 12/23/2014.
 */
public class MyTaskPowerChnFragment extends Fragment {

    private static String ARG_PLINENAME = "ARG_PLINENAME";

    private ArrayList<ListItemOpt> channelNameList;
    private ListItemPoleChnAdapter listItemAdapter;

    private String plineName;

    private ListView listview;
    private LinearLayout top;
    private LinearLayout bottom;

    /**
     * @param plinename
     * @return
     */
    public static MyTaskPowerChnFragment newInstance(String plinename) {
        MyTaskPowerChnFragment f = new MyTaskPowerChnFragment();
        Bundle b = new Bundle();
        b.putString(ARG_PLINENAME, plinename);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plineName = getArguments().getString(ARG_PLINENAME);
        this.initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_patrol_pline_pole, container, false);
        listview = (ListView) v.findViewById(R.id.listview);
        top = (LinearLayout) v.findViewById(R.id.top);
        bottom = (LinearLayout) v.findViewById(R.id.bottom);

        top.setVisibility(View.GONE);
        bottom.setVisibility(View.GONE);

        //初始化界面
        this.initView();
        return v;
    }

    /**
     * 初始化数据
     */
    private void initView() {

        listItemAdapter = new ListItemPoleChnAdapter(getContext(), channelNameList);
        listview.setAdapter(listItemAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                Intent intent = new Intent();
                intent.putExtra("chnname", tv_title.getText().toString().trim());
                intent.putExtra("plinename", plineName);
                intent.putExtra("chnobjectid", channelNameList.get(position).getObjectid());
                intent.putExtra("istaskdanger", true);
                intent.setClass(getContext(), ChnDangerStatisticsActivity.class);
                ((Activity)getContext()).startActivityForResult(intent,0);
            }
        });
    }


    /**
     * 获取通道列表
     */
    public void initData() {

        if (channelNameList == null) {
            channelNameList = new ArrayList<ListItemOpt>();
        }

        channelNameList.clear();

        ChannelTableOpt channelTableOpt = new ChannelTableOpt();
        List<ChannelTableDataClass> channels = channelTableOpt.getMyTaskRow(plineName);

        for (ChannelTableDataClass channel : channels) {
            ListItemOpt listItemOpt = new ListItemOpt();
            listItemOpt.setChnorpole(ChnOrPole.CHANNEL);
            listItemOpt.setOptName(channel.getChannelName());
            listItemOpt.setObjectid(channel.getChannelObjectId());
            listItemOpt.setDangerCount(channel.getDangerCount());
            listItemOpt.setIcon(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_powerline_channel));

            channelNameList.add(listItemOpt);
        }

        if(listItemAdapter!=null){
            listItemAdapter.notifyDataSetChanged();
        }
    }

}




