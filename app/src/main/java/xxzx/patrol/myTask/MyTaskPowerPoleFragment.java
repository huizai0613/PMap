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

import xxzx.activity.Patrol.PoleDangerStatisticsActivity;
import xxzx.activity.R;
import xxzx.library.VectorDrawable;
import xxzx.patrol.Record.ChnOrPole;
import xxzx.publicClass.MyListView.ListItemOpt;
import xxzx.publicClass.MyListView.ListItemPoleChnAdapter;
import xxzx.spatialite.TableOptClass.PoleTableOpt;
import xxzx.spatialite.TableStruct.PoleTableDataClass;


/**
 * Created by Daniel on 12/23/2014.
 */
public class MyTaskPowerPoleFragment extends Fragment{

    private static String ARG_PLINENAME = "ARG_PLINENAME";

    private ArrayList<ListItemOpt> poleNameList;
    private ListItemPoleChnAdapter listItemAdapter;

    private String plineName;

    private ListView listview;
    private LinearLayout top;
    private LinearLayout bottom;

    /**
     * @param plinename
     * @return
     */
    public static MyTaskPowerPoleFragment newInstance(String plinename) {
        MyTaskPowerPoleFragment f = new MyTaskPowerPoleFragment();
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

        //初始化数据
        this.initView();
        return v;
    }

    /**
     * 初始化数据
     */
    private void initView() {

        listItemAdapter = new ListItemPoleChnAdapter(getContext(), poleNameList);
        listview.setAdapter(listItemAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                Intent intent = new Intent();
                intent.putExtra("polename", tv_title.getText().toString().trim());
                intent.putExtra("poleobjectid",poleNameList.get(position).getObjectid());
                intent.putExtra("plinename", plineName);
                intent.putExtra("istaskdanger",true);
                intent.setClass(getContext(), PoleDangerStatisticsActivity.class);
                ((Activity)getContext()).startActivityForResult(intent, 0);
            }
        });
    }

    /**
     * 获取杆塔列表
     */
    public void initData() {

        if (poleNameList == null) {
            poleNameList = new ArrayList<ListItemOpt>();
        }

        poleNameList.clear();

        PoleTableOpt poleTableOpt = new PoleTableOpt();
        List<PoleTableDataClass> poles = poleTableOpt.getMyTaskRow(plineName);

        for (PoleTableDataClass pole : poles) {
            ListItemOpt listItemOpt = new ListItemOpt();
            listItemOpt.setChnorpole(ChnOrPole.POLE);
            listItemOpt.setOptName(pole.getPoleName());
            listItemOpt.setObjectid(pole.getPoleObjectId());
            listItemOpt.setDangerCount(pole.getDangerCount());
            listItemOpt.setIcon(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_powerline_pole));

            poleNameList.add(listItemOpt);
        }

        if(listItemAdapter!=null){
            listItemAdapter.notifyDataSetChanged();
        }
    }
}




