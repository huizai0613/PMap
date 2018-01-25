package xxzx.patrol.Powerline;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.Patrol.ChnDangerStatisticsActivity;
import xxzx.activity.R;
import xxzx.library.VectorDrawable;
import xxzx.myView.ImgButtonVertical;
import xxzx.publicClass.MyListView.ListItemSelected;
import xxzx.publicClass.MyListView.ListItemSelectedAdapter;
import xxzx.publicClass.MyListView.ListItemSelectState;
import xxzx.publicClass.MyListView.ListViewCommonHolder;
import xxzx.publicClass.ToastUtil;
import xxzx.spatialite.TableOptClass.ChannelTableOpt;
import xxzx.spatialite.TableOptClass.PowerlineTableOpt;
import xxzx.spatialite.TableStruct.ChannelTableDataClass;


/**
 * Created by Daniel on 12/23/2014.
 */
public class PowerChnFragment extends Fragment  implements ImgButtonVertical.ICoallBack{

    private static String ARG_PLINENAME="ARG_PLINENAME";

    private List<ListItemSelected> channelNameList;
    private List<Boolean> isChecked_init;
    private ListItemSelectedAdapter listItemAdapter;
    private String plineName;

    private ListView listview;
    private LinearLayout top;
    private LinearLayout bottom;
    private TextView tv_selectTitle;

    ImgButtonVertical imgbtn_select;
    ImgButtonVertical imgbtn_cancel;
    ImgButtonVertical imgbtn_task;
    ImgButtonVertical imgbtn_exit;

    //当前选择状态
    private ListItemSelectState selectState= ListItemSelectState.UNSELECT;



    public static PowerChnFragment newInstance(String plinename) {
        PowerChnFragment f = new PowerChnFragment();
        Bundle b = new Bundle();
        b.putString(ARG_PLINENAME, plinename);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plineName = getArguments().getString(ARG_PLINENAME);
        this.getChannelNameList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_patrol_pline_pole, container, false);
        listview = (ListView) v.findViewById(R.id.listview);
        top = (LinearLayout) v.findViewById(R.id.top);
        bottom = (LinearLayout) v.findViewById(R.id.bottom);
        tv_selectTitle = (TextView) v.findViewById(R.id.tv_selectTitle);

        top.setVisibility(View.GONE);
        bottom.setVisibility(View.GONE);

        imgbtn_select =(ImgButtonVertical)v.findViewById(R.id.imgbtn_select);
        imgbtn_cancel =(ImgButtonVertical)v.findViewById(R.id.imgbtn_cancel);
        imgbtn_task =(ImgButtonVertical)v.findViewById(R.id.imgbtn_task);
        imgbtn_exit =(ImgButtonVertical)v.findViewById(R.id.imgbtn_exit);

        imgbtn_select.setImage(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_select_un));
        imgbtn_cancel.setImage(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_select_un));
        imgbtn_task.setImage(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_select_un));
        imgbtn_exit.setImage(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_select_un));

        imgbtn_select.setOnClick(this);
        imgbtn_cancel.setOnClick(this);
        imgbtn_task.setOnClick(this);
        imgbtn_exit.setOnClick(this);

        //初始化数据
        this.initData();

        if(this.selectState==ListItemSelectState.UNSELECT){
            this.initSelectState(false);
        }else if(this.selectState==ListItemSelectState.SELECT){
            this.initSelectState(true);
        }
        return v;
    }

    /**
     * 初始化数据
     */
    private void initData() {

        listItemAdapter = new ListItemSelectedAdapter(getContext(), channelNameList);
        listview.setAdapter(listItemAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (selectState == ListItemSelectState.UNSELECT) {
                   TextView tv_title = (TextView) view.findViewById(R.id.tv_title);

                    Intent intent=new Intent();
                    intent.putExtra("chnname", tv_title.getText().toString().trim());
                    intent.putExtra("plinename", plineName);
                    intent.putExtra("chnobjectid", channelNameList.get(position).getObjectid());
                    intent.putExtra("istaskdanger",false);
                    intent.setClass(getContext(), ChnDangerStatisticsActivity.class);
                    getContext().startActivity(intent);
                    ToastUtil.show(getContext(), "当前状态下,不能进行任务操作！");

                } else {

                    ListItemSelected itemClass = (ListItemSelected) parent.getAdapter().getItem(position);
                    ListViewCommonHolder viewHolder = (ListViewCommonHolder) view.getTag();

                    itemClass.setIsChecked(!itemClass.isChecked());
                    if (itemClass.isChecked()) {
                        viewHolder.iv_arrow.setBackground(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_select));
                    } else {
                        viewHolder.iv_arrow.setBackground(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_select_un));
                    }

                    //修改选中条目
                    setIsCheckedCountTitle();
                }
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (selectState == ListItemSelectState.UNSELECT) {
                    ListItemSelected itemClass = (ListItemSelected) parent.getAdapter().getItem(position);
                    ListViewCommonHolder viewHolder = (ListViewCommonHolder) view.getTag();

                    itemClass.setIsChecked(!itemClass.isChecked());
                    if (itemClass.isChecked()) {
                        viewHolder.iv_arrow.setBackground(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_select));
                    } else {
                        viewHolder.iv_arrow.setBackground(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_select_un));
                    }

                    initSelectState(true);
                }

                return true;
            }
        });
    }

    private void getChannelNameList() {

        if (channelNameList == null) {
            channelNameList = new ArrayList<ListItemSelected>();
        }
        if (isChecked_init == null) {
            isChecked_init = new ArrayList<Boolean>();
        }
        channelNameList.clear();
        isChecked_init.clear();

        ChannelTableOpt channelTableOpt = new ChannelTableOpt();
        List<ChannelTableDataClass> channels = channelTableOpt.getRow(plineName);

        for (ChannelTableDataClass channel : channels) {
            ListItemSelected item = new ListItemSelected();
            item.setTitle(channel.getChannelName());
            item.setObjectid(channel.getChannelObjectId());
            item.setIcon(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_powerline_channel));
            item.setIsChecked(channel.getIsSelect() == 0 ? false : true);
            isChecked_init.add(channel.getIsSelect() == 0 ? false : true);
            channelNameList.add(item);
        }
    }


    private void initSelectState(boolean checked) {
        //底部工具条是否显示
        bottom.setVisibility(checked ? View.VISIBLE : View.GONE);
        top.setVisibility(checked ? View.VISIBLE : View.GONE);

        this.setIsCheckedCountTitle();

        //设置当前选择状态
        this.selectState = checked ? ListItemSelectState.SELECT : ListItemSelectState.UNSELECT;
    }


    private void setIsCheckedCountTitle(){
        int isCheckedCount = 0;
        for(int i=0;i<listItemAdapter.getCount();i++) {
            ListItemSelected item = (ListItemSelected) listItemAdapter.getItem(i);
            if (item.isChecked()) {
                isCheckedCount++;
            }
        }

        tv_selectTitle.setText("当前选中 " + isCheckedCount+ " 个");
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.imgbtn_select:
                for(ListItemSelected item:channelNameList){
                    item.setIsChecked(true);
                }
                listItemAdapter.notifyDataSetChanged();

                this.setIsCheckedCountTitle();
                break;
            case R.id.imgbtn_cancel:
                for(ListItemSelected item:channelNameList){
                    item.setIsChecked(false);
                }
                listItemAdapter.notifyDataSetChanged();

                this.setIsCheckedCountTitle();
                break;
            case R.id.imgbtn_task:
                //选中的条目
                int isCheckedCount=0;
                for(int i=0;i<listItemAdapter.getCount();i++){

                    ListItemSelected item = (ListItemSelected)listItemAdapter.getItem(i);
                    if(item.isChecked()==isChecked_init.get(i)){
                        continue;
                    }

                    //修改通道表
                    ChannelTableOpt channelTableOpt=new ChannelTableOpt();
                    channelTableOpt.updateTaskFromChnName(item.getTitle(), (item).isChecked());
                    isCheckedCount++;
                }

                //当选中的条目大于0时，要将该线路表中的该线路置为选中状态
                if(isCheckedCount>0){
                    //修改线路表
                    PowerlineTableOpt powerOpt =new PowerlineTableOpt();
                    powerOpt.updateTask(this.plineName, true);
                }

                initSelectState(false);
                //重新数据库中读取数据
                getChannelNameList();

                //更新listview
                listItemAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "处理完成！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imgbtn_exit:
                //重新冲数据库中读取数据
                getChannelNameList();
                //更新listview
                listItemAdapter.notifyDataSetChanged();
                initSelectState(false);
                break;
        }
    }







}




