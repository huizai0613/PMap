package xxzx.activity.Patrol.MyTask;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.R;
import xxzx.activity.PublicBaseActivity.BaseToolBarActivity;
import xxzx.library.VectorDrawable;
import xxzx.publicClass.MyListView.ListItemOptAdapter;
import xxzx.publicClass.MyListView.ListItemOpt;
import xxzx.spatialite.TableOptClass.PowerlineTableOpt;
import xxzx.spatialite.TableStruct.PowerlineTableDataClass;

public class MyTaskPowerlineActivity extends BaseToolBarActivity{

    /** ListView对象 */
    private ListView listview;

    private ArrayList<ListItemOpt> plineNameList;
    private ListItemOptAdapter listItemAdapter;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // 点击返回图标事件
        if (id == android.R.id.home) {
            //回到原来的activity
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void initView(){
        listview = (ListView)findViewById(R.id.listview);

//        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        // 决定左上角图标的右侧是否有向左的小箭头, true
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        // 有小箭头，并且图标可以点击
//        getSupportActionBar().setDisplayShowHomeEnabled(false);

    }

    private void initData() {
        this.getPowerlineNameList();
        listItemAdapter = new ListItemOptAdapter(this, plineNameList);
        listview.setAdapter(listItemAdapter);
        //listviewitem点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItemOpt item = plineNameList.get(position);
                Intent intent = new Intent();
                intent.putExtra("plinename", item.getOptName());
                intent.setClass(MyTaskPowerlineActivity.this, MyTaskChnAndPoleActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * 获得电力线名称
     * @return
     */
    private void getPowerlineNameList(){
        if(plineNameList==null){
            plineNameList = new ArrayList<ListItemOpt>();
        }

        plineNameList.clear();

        PowerlineTableOpt powerlineTableOpt=new PowerlineTableOpt();
        List<PowerlineTableDataClass> list_powerline = powerlineTableOpt.getMyTaskRow();

        for(PowerlineTableDataClass p:list_powerline) {
            ListItemOpt listItemOpt = new ListItemOpt();
            listItemOpt.setOptName(p.getPowerName());
            listItemOpt.setIcon(VectorDrawable.getDrawable(this, R.drawable.iconfont_powerline));
            plineNameList.add(listItemOpt);
        }
    }

}
