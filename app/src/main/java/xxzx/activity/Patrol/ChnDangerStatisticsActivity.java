package xxzx.activity.Patrol;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import xxzx.activity.Patrol.MyTask.ChnDangerListActivity;
import xxzx.activity.R;
import xxzx.patrol.Danger.DangerStatisticsClass;
import xxzx.patrol.Danger.ListItemDangerStatisticsAdapter;
import xxzx.publicClass.LoadingDialog;
import xxzx.publicClass.MyHttpRequst;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.NetworkConnected;
import xxzx.publicClass.ToastUtil;
import xxzx.HttpJsonReadClass.JsonChnDanger;
import xxzx.spatialite.TableOptClass.ChannelDangerTableOpt;
import xxzx.spatialite.TableStruct.ChannelDangerTableDataClass;

public class ChnDangerStatisticsActivity extends AppCompatActivity {

    private static final String LOG_TAG = ChnDangerStatisticsActivity.class.getSimpleName();

    private ListView listview;
    private String chnName;
    private int chnObjectId;
    private String plineName;
    private boolean isTask = false;
    private LoadingDialog loadingDialog;

    private List<DangerStatisticsClass> listStatistics;
    private ListItemDangerStatisticsAdapter listItemAdapter;

    private final int request_danger_fail = 0;//请求失败
    private final int request_danger_success = 1;//隐患点请求成功
    private ChannelDangerTableOpt chnDangerTableOpt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_chn_statistics);

        Intent intent = getIntent();
        chnName = intent.getStringExtra("chnname");
        chnObjectId = intent.getIntExtra("chnobjectid",-1);
        plineName = intent.getStringExtra("plinename");
        isTask = intent.getBooleanExtra("istaskdanger", false);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                setResult(0,null);
                this.finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //更新数据
            this.getDangerStatisticsFormDatabase();
        }
    }


    private void initView() {
        listview = (ListView) findViewById(R.id.listview);

        loadingDialog = new LoadingDialog(this);


        RelativeLayout rl_toolbar =(RelativeLayout) findViewById(R.id.include_toolbar_danger_chn_statistics);

        Toolbar toolbar = (Toolbar)rl_toolbar.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        TextView tv_title = (TextView) toolbar.findViewById(R.id.tv_title);
        TextView tv_subtitle = (TextView) toolbar.findViewById(R.id.tv_subtitle);
        tv_subtitle.setText(this.chnName);
        tv_title.setText("线路通道信息");
        toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.toolbar_navigation_back);
            //设置点击事件
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChnDangerStatisticsActivity.this.finish();
                }
            });
        }
    }

    private void initData() {
        this.chnDangerTableOpt = new ChannelDangerTableOpt();
        this.listStatistics = new ArrayList<DangerStatisticsClass>();

        listItemAdapter = new ListItemDangerStatisticsAdapter(this, listStatistics);
        listview.setAdapter(listItemAdapter);

        //当标示为我的任务时，listview可点击
        if (isTask) {
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.putExtra("chnname", chnName);
                    intent.putExtra("chnobjectid", chnObjectId);
                    intent.putExtra("plinename", plineName);
                    intent.putExtra("dangertype", position);
                    intent.setClass(ChnDangerStatisticsActivity.this, ChnDangerListActivity.class);
                    startActivityForResult(intent, 0);
                }
            });
        }

        List<ChannelDangerTableDataClass> list_danger = chnDangerTableOpt.getRowFromObjectId(this.chnObjectId);

        //表示数据库中没有存储该杆塔的隐患信息，网络连接成功
        if (list_danger.size() == 0 && NetworkConnected.isConnected(this)) {
            this.getDangerStatisticsFormServer();
        } else {
            this.getDangerStatisticsFormDatabase();
        }
    }


    /**
     * 从服务器中读取数据
     */
    private void getDangerStatisticsFormServer() {
        this.clearDangerStatistics();

        this.loadingDialog.show();

        MyRunnable runnable = new MyRunnable();
        runnable.setData(this.chnObjectId);
        new Thread(runnable).start();
    }


    /**
     * 从数据库中读取数据
     */
    private void getDangerStatisticsFormDatabase() {
        this.clearDangerStatistics();

        //查询隐患表中该通道的隐患点
        List<ChannelDangerTableDataClass> list_danger = chnDangerTableOpt.getRowFromObjectId(this.chnObjectId);

        for (ChannelDangerTableDataClass danger : list_danger) {
            DangerStatisticsClass item = listStatistics.get(danger.getDangerType());
            if (item != null) {
                item.num++;
                item.maxLevel = danger.getDangerLevel() > item.maxLevel ? danger.getDangerLevel() : item.maxLevel;
            }
        }
        //更新统计列表
        listItemAdapter.notifyDataSetChanged();
    }


    private void clearDangerStatistics() {
        this.listStatistics.clear();

        for (int i = 0; i < MyString.channelDangerTypes.length; i++) {
            DangerStatisticsClass item = new DangerStatisticsClass();
            item.title = MyString.channelDangerTypes[i];
            item.type = i;
            item.num = 0;
            item.maxLevel = -1;
            listStatistics.add(item);
        }
    }


    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            loadingDialog.dismiss();
            switch (msg.what) {
                case request_danger_fail:
                    ToastUtil.show(ChnDangerStatisticsActivity.this, msg.obj.toString());
                    getDangerStatisticsFormDatabase();
                    break;
                case request_danger_success:
                    //读取并写入数据库
                    JsonChnDanger jsonChnDanger = new JsonChnDanger(ChnDangerStatisticsActivity.this,plineName,chnName);
                    jsonChnDanger.readDangerJson((String) msg.obj);
                    //从数据库中读取数据
                    getDangerStatisticsFormDatabase();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public class MyRunnable implements Runnable {
        private int chnObjectId;

        public void setData(int chnobjectid) {
            this.chnObjectId = chnobjectid;
        }
        @Override
        public void run() {
            // TODO Auto-generated method stub

            MySingleClass mySingleClass = MySingleClass.getInstance();
            Properties properties = mySingleClass.getProperties();
            Message message = new Message();
            try {
//                String url = properties.get("url_channel_danger").toString();
//                String httpPath = String.format(url, String.valueOf(this.chnObjectId));
//
//                String result = MyHttpRequst.getHttpGetRequst2(httpPath);


                String url = properties.get("url_channel_danger2").toString();

                String poststring = properties.get("url_channel_danger2_poststring").toString();
                poststring  = String.format(poststring, String.valueOf(this.chnObjectId));

                String result = MyHttpRequst.getHttpPostRequst2(url,poststring);

                Log.e("channel",result);
                if (result == null || result.trim().equals("")) {
                    message.what = request_danger_fail;
                    message.obj = "无法连接服务器，不能更新当前隐患点信息，请确定网络连接是否正常！";
                } else {
                    // 发送消息，通知handler在主线程中更新UI
                    message.what = request_danger_success;
                    message.obj = result;
                }
            } catch (Exception e) {
                message.what = request_danger_fail;
                message.obj = e.toString();
                e.printStackTrace();
            } finally {
                myHandler.sendMessage(message);
            }

        }
    }


}




