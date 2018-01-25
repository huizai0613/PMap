package xxzx.activity.Patrol.MyTask;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.R;
import xxzx.patrol.Danger.DangerOpt;
import xxzx.patrol.Danger.ListItemDangerEditAdapter;
import xxzx.patrol.Danger.ListViewDangerHolder;
import xxzx.publicClass.MyString;
import xxzx.spatialite.TableOptClass.ChannelDangerTableOpt;
import xxzx.spatialite.TableStruct.BaseDangerTableDataClass;
import xxzx.spatialite.TableStruct.ChannelDangerTableDataClass;

public class ChnDangerListActivity extends AppCompatActivity {

    private static final String LOG_TAG = ChnDangerListActivity.class.getSimpleName();

    private ListView listview;
    private Button btn_add;
    private String chnName;
    private int chnObjectId;
    private String plineName;
    private List<BaseDangerTableDataClass> dangerList;
    private ListItemDangerEditAdapter adapter;
    private int dangerType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_pole_list);

        Intent intent = getIntent();
        chnName = intent.getStringExtra("chnname");
        chnObjectId = intent.getIntExtra("chnobjectid",-1);
        plineName = intent.getStringExtra("plinename");
        dangerType = intent.getIntExtra("dangertype", -1);

        this.initView();
        this.initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_toolbar_patrol_channel_pole, menu);
        return true;
    }

    @Override
         protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //更新数据
            this.getChnDangerData();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        setResult(Activity.RESULT_OK);
        this.finish();
        super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                setResult(Activity.RESULT_OK);
                this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        listview = (ListView) findViewById(R.id.listview);
        btn_add = (Button) findViewById(R.id.btn_add);


        RelativeLayout rl_toolbar =(RelativeLayout) findViewById(R.id.include_toolbar_danger_pole_list);

        Toolbar toolbar = (Toolbar) rl_toolbar.findViewById(R.id.toolbar);

        toolbar.setTitle("");
        TextView tv_title = (TextView) toolbar.findViewById(R.id.tv_title);
        TextView tv_subtitle = (TextView) toolbar.findViewById(R.id.tv_subtitle);
        tv_subtitle.setText(this.chnName);
        tv_title.setText(MyString.channelDangerTypes[this.dangerType] + "信息");

        toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.toolbar_navigation_back);
            //设置点击事件
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(Activity.RESULT_OK);
                    ChnDangerListActivity.this.finish();
                }
            });
        }
    }

    private void initData() {

        this.dangerList = new ArrayList<>();
        this.getChnDangerData();
        this.adapter = new ListItemDangerEditAdapter(this, dangerList);
        this.listview.setAdapter(this.adapter);

        this.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ListViewDangerHolder viewHolder = (ListViewDangerHolder) view.getTag();

                Intent intent = new Intent();
                intent.setClass(ChnDangerListActivity.this, ChnDangerUpdateActivity.class);
                intent.putExtra("chnname", chnName);
                intent.putExtra("chnobjectid", chnObjectId);
                intent.putExtra("plinename", plineName);
                intent.putExtra("rowid", viewHolder.rowid);
                intent.putExtra("dangertype", dangerType);
                startActivityForResult(intent, 0);
            }
        });

        this.listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewDangerHolder viewHolder = (ListViewDangerHolder) view.getTag();

                int keyid=viewHolder.keyid;
                //如果是新增的数据
                if(keyid==-1) {
                    final int rowid = viewHolder.rowid;
                    AlertDialog.Builder build = new AlertDialog.Builder(ChnDangerListActivity.this);
                    build.setTitle("删除").setMessage("确定删除该隐患信息吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO
                                    try {
                                        DangerOpt.deleteChnDangerFormRowID(rowid);
                                        //刷新界面
                                        getChnDangerData();
                                        adapter.notifyDataSetChanged();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //如果点击的是取消，不做任何处理
                            dialog.dismiss();
                        }
                    }).show();

                }else{
                    AlertDialog.Builder build = new AlertDialog.Builder(ChnDangerListActivity.this);
                    build.setTitle("注意").setMessage("该隐患为服务器数据，不能删除！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO
                                    //如果点击的是取消，不做任何处理
                                    dialog.dismiss();
                                }
                            }).show();
                }
                return true;
            }
        });

        this.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChnDangerListActivity.this, ChnDangerAddActivity.class);
                intent.putExtra("chnname", chnName);
                intent.putExtra("chnobjectid", chnObjectId);
                intent.putExtra("plinename", plineName);
                intent.putExtra("dangertype", dangerType);
                startActivityForResult(intent, 0);
            }
        });
    }

    /**
     * 获得通道隐患点数据
     */
    private void getChnDangerData() {
        this.dangerList.clear();
        ChannelDangerTableOpt channelDangerTableOpt = new ChannelDangerTableOpt();
        List<ChannelDangerTableDataClass> list = channelDangerTableOpt.getRow(this.chnObjectId, this.dangerType);
        this.dangerList.addAll(list);
    }


}




