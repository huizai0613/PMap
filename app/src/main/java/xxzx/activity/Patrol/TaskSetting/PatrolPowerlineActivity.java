package xxzx.activity.Patrol.TaskSetting;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.R;
import xxzx.activity.PublicBaseActivity.BaseToolBarActivity;
import xxzx.library.VectorDrawable;
import xxzx.myView.ImgButtonVertical;
import xxzx.publicClass.MyListView.ListItemSelected;
import xxzx.publicClass.MyListView.ListItemSelectedAdapter;
import xxzx.publicClass.MyListView.ListItemSelectState;
import xxzx.publicClass.MyListView.ListViewCommonHolder;
import xxzx.spatialite.TableOptClass.ChannelTableOpt;
import xxzx.spatialite.TableOptClass.PoleTableOpt;
import xxzx.spatialite.TableOptClass.PowerlineTableOpt;
import xxzx.spatialite.TableStruct.PowerlineTableDataClass;

public class PatrolPowerlineActivity extends BaseToolBarActivity implements ImgButtonVertical.ICoallBack{

    /** ListView对象 */
    private ListView listview;
    private LinearLayout bottom;
    private LinearLayout top;
    private TextView tv_selectTitle;

    ImgButtonVertical imgbtn_select;
    ImgButtonVertical imgbtn_cancel;
    ImgButtonVertical imgbtn_task;
    ImgButtonVertical imgbtn_exit;

    private List<ListItemSelected> plineNameList;
    private List<Boolean> isChecked_init;
    private ListItemSelectedAdapter listItemAdapter;
    //当前选择状态
    private ListItemSelectState selectState;
    /**
     * 记录当前选中条目的任务索引taskID
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_powerline);
        this.initView();
        this.initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_toolbar_inputdata, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        this.getPowerlineNameList();
        this.listItemAdapter.notifyDataSetChanged();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // 点击返回图标事件
        if (id == android.R.id.home) {
            if (selectState == ListItemSelectState.SELECT) {
                //重新冲数据库中读取数据
                getPowerlineNameList();
                //更新listview
                listItemAdapter.notifyDataSetChanged();

                initSelectState(false);
            } else {
                //回到原来的activity
                this.finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (selectState == ListItemSelectState.SELECT) {
            //重新冲数据库中读取数据
            getPowerlineNameList();
            //更新listview
            listItemAdapter.notifyDataSetChanged();
            initSelectState(false);
        } else {
            //回到原来的activity
            this.finish();
        }
    }

    private void initView(){
        listview = (ListView)findViewById(R.id.listview);
        bottom =(LinearLayout)findViewById(R.id.bottom);
        top =(LinearLayout)findViewById(R.id.top);
        tv_selectTitle =(TextView)findViewById(R.id.tv_selectTitle);

        imgbtn_select =(ImgButtonVertical)findViewById(R.id.imgbtn_select);
        imgbtn_cancel =(ImgButtonVertical)findViewById(R.id.imgbtn_cancel);
        imgbtn_task =(ImgButtonVertical)findViewById(R.id.imgbtn_task);
        imgbtn_exit =(ImgButtonVertical)findViewById(R.id.imgbtn_exit);

        imgbtn_select.setImage(VectorDrawable.getDrawable(PatrolPowerlineActivity.this, R.drawable.iconfont_select_un));
        imgbtn_cancel.setImage(VectorDrawable.getDrawable(PatrolPowerlineActivity.this, R.drawable.iconfont_select_un));
        imgbtn_task.setImage(VectorDrawable.getDrawable(PatrolPowerlineActivity.this, R.drawable.iconfont_select_un));
        imgbtn_exit.setImage(VectorDrawable.getDrawable(PatrolPowerlineActivity.this, R.drawable.iconfont_select_un));

        imgbtn_select.setOnClick(this);
        imgbtn_cancel.setOnClick(this);
        imgbtn_task.setOnClick(this);
        imgbtn_exit.setOnClick(this);

        //默认底部工具条不显示
        bottom.setVisibility(View.GONE);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        // 决定左上角图标的右侧是否有向左的小箭头, true
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 有小箭头，并且图标可以点击
        getSupportActionBar().setDisplayShowHomeEnabled(false);

    }

    private void initData() {
        this.getPowerlineNameList();
        listItemAdapter = new ListItemSelectedAdapter(this, plineNameList);
        listview.setAdapter(listItemAdapter);

        this.initSelectState(false);

        //listviewitem点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectState == ListItemSelectState.UNSELECT) {

                    ListItemSelected item = (ListItemSelected) parent.getAdapter().getItem(position);
                    Intent intent = new Intent();
                    intent.putExtra("plinename", item.getTitle());
                    intent.setClass(PatrolPowerlineActivity.this, ChnAndPoleActivity.class);
                    startActivityForResult(intent, 0);
                } else {
                    ListItemSelected itemClass = (ListItemSelected) parent.getAdapter().getItem(position);
                    ListViewCommonHolder viewHolder = (ListViewCommonHolder) view.getTag();

                    itemClass.setIsChecked(!itemClass.isChecked());
                    if (itemClass.isChecked()) {
                        viewHolder.iv_arrow.setBackground(VectorDrawable.getDrawable(PatrolPowerlineActivity.this, R.drawable.iconfont_select));
                    } else {
                        viewHolder.iv_arrow.setBackground(VectorDrawable.getDrawable(PatrolPowerlineActivity.this, R.drawable.iconfont_select_un));
                    }
                    initSelectState(true);
                }
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            // @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectState == ListItemSelectState.UNSELECT) {
                    ListItemSelected itemClass = (ListItemSelected) parent.getAdapter().getItem(position);
                    ListViewCommonHolder viewHolder = (ListViewCommonHolder) view.getTag();

                    itemClass.setIsChecked(!itemClass.isChecked());
                    if (itemClass.isChecked()) {
                        viewHolder.iv_arrow.setBackground(VectorDrawable.getDrawable(PatrolPowerlineActivity.this, R.drawable.iconfont_select));
                    } else {
                        viewHolder.iv_arrow.setBackground(VectorDrawable.getDrawable(PatrolPowerlineActivity.this, R.drawable.iconfont_select_un));
                    }

                    initSelectState(true);
                }
                return true;
            }
        });
    }


    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.imgbtn_select:
                for(ListItemSelected item:plineNameList){
                    item.setIsChecked(true);
                }
                listItemAdapter.notifyDataSetChanged();
                break;
            case R.id.imgbtn_cancel:
                for(ListItemSelected item:plineNameList){
                    item.setIsChecked(false);
                }
                listItemAdapter.notifyDataSetChanged();
                break;
            case R.id.imgbtn_task:
                for(int i=0;i<listItemAdapter.getCount();i++){
                    ListItemSelected item = (ListItemSelected)listItemAdapter.getItem(i);

                    if(item.isChecked()==isChecked_init.get(i)){
                        continue;
                    }
                    //修改线路表
                    PowerlineTableOpt powerOpt =new PowerlineTableOpt();
                    powerOpt.updateTask(item.getTitle(), (item).isChecked());
                    //修改通道表
                    ChannelTableOpt channelTableOpt=new ChannelTableOpt();
                    channelTableOpt.updateTaskFormPlineName(item.getTitle(), (item).isChecked());
                    //修改杆塔表
                    PoleTableOpt poleTableOpt=new PoleTableOpt();
                    poleTableOpt.updateTaskFromPlineName(item.getTitle(), (item).isChecked());
                }

                initSelectState(false);
                //重新冲数据库中读取数据
                getPowerlineNameList();
                //更新listview
                listItemAdapter.notifyDataSetChanged();
                Toast.makeText(PatrolPowerlineActivity.this,"处理完成！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.imgbtn_exit:
                //重新冲数据库中读取数据
                getPowerlineNameList();
                //更新listview
                listItemAdapter.notifyDataSetChanged();
                initSelectState(false);
                break;
        }
    }




    /**
     * 获得电力线名称
     * @return
     */
    private void getPowerlineNameList(){
        if(plineNameList==null){
            plineNameList = new ArrayList<ListItemSelected>();
        }
        if(isChecked_init==null){
            isChecked_init = new ArrayList<Boolean>();
        }
        plineNameList.clear();
        isChecked_init.clear();

        PowerlineTableOpt powerlineTableOpt=new PowerlineTableOpt();
        List<Object> list_powerline = powerlineTableOpt.getRow();

        for(Object o:list_powerline) {
            ListItemSelected item = new ListItemSelected();
            item.setTitle(((PowerlineTableDataClass)o).getPowerName());
            item.setIcon(VectorDrawable.getDrawable(this, R.drawable.iconfont_powerline));

            item.setIsChecked(((PowerlineTableDataClass)o).getIsSelect() == 1 ? true : false);
            isChecked_init.add(((PowerlineTableDataClass)o).getIsSelect() == 1 ? true : false);
            plineNameList.add(item);
        }
    }


    private void initSelectState(boolean checked) {
        //底部工具条是否显示
        bottom.setVisibility(checked?View.VISIBLE:View.GONE);
        top.setVisibility(checked ? View.VISIBLE : View.GONE);
        this.setIsCheckedCountTitle();
        //设置当前选择状态
        this.selectState = checked?ListItemSelectState.SELECT:ListItemSelectState.UNSELECT;
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


}
