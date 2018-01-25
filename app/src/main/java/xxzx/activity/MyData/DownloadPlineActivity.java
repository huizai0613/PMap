package xxzx.activity.MyData;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import xxzx.activity.R;
import xxzx.activity.PublicBaseActivity.BaseToolBarActivity;
import xxzx.download.MultiThreadBaseClass;
import xxzx.download.Plines.DownloadPlinesClass;
import xxzx.download.Plines.WriteDatabaseClass;
import xxzx.library.VectorDrawable;
import xxzx.myView.MyOptionMenu;
import xxzx.publicClass.MyListView.ListItemMenu;
import xxzx.publicClass.MyListView.ListItemSelected;
import xxzx.publicClass.MyListView.ListItemSelectedAdapter;
import xxzx.publicClass.MyListView.ListViewCommonHolder;
import xxzx.publicClass.LoadingDialog;
import xxzx.publicClass.MyHttpRequst;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.MyTaskExecutorClass;
import xxzx.publicClass.ToastUtil;
import xxzx.spatialite.TableOptClass.BaseTableOpt;
import xxzx.spatialite.TableOptClass.ChannelTableOpt;
import xxzx.spatialite.TableOptClass.PoleTableOpt;
import xxzx.spatialite.TableOptClass.PowerlineTableOpt;
import xxzx.spatialite.TableStruct.ChannelTableDataClass;
import xxzx.spatialite.TableStruct.PoleTableDataClass;
import xxzx.spatialite.TableStruct.PowerlineTableDataClass;


public class DownloadPlineActivity extends BaseToolBarActivity {
    /**
     * ListView对象
     */
    private ListView listview;

    private ArrayList<ListItemSelected> plineNameList;
    private ListItemSelectedAdapter listItemAdapter;

    private MyOptionMenu myOptionMenu;

    private DownloadPlinesClass downloadPlinesClass;
    private WriteDatabaseClass writeDatabaseClass;

    private List<String> downloadPlineList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_powerline_download);

        Intent intent = getIntent();
        String result = intent.getStringExtra("result");

        this.initView();
        this.initData(result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.action_more:
                myOptionMenu.showAsDropDown(findViewById(R.id.action_more), 0, 20);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        listview = (ListView) findViewById(R.id.listview);

        //listviewitem点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ListItemSelected itemClass = (ListItemSelected) parent.getAdapter().getItem(position);
                ListViewCommonHolder viewHolder = (ListViewCommonHolder) view.getTag();

                itemClass.setIsChecked(!itemClass.isChecked());
                if (itemClass.isChecked()) {
                    viewHolder.iv_arrow.setBackground(VectorDrawable.getDrawable(DownloadPlineActivity.this, R.drawable.iconfont_select));
                } else {
                    viewHolder.iv_arrow.setBackground(VectorDrawable.getDrawable(DownloadPlineActivity.this, R.drawable.iconfont_select_un));
                }
            }
        });
        this.initOptionMenu();
    }


    /**
     * 初始化optionMenu
     */
    private void initOptionMenu(){
        List<ListItemMenu> items=new ArrayList<>();
        ListItemMenu listItemMenu1 = new ListItemMenu();
        listItemMenu1.setTitle("全部取消");

        ListItemMenu listItemMenu2 = new ListItemMenu();
        listItemMenu2.setTitle("全部选中");

        ListItemMenu listItemMenu3 = new ListItemMenu();
        listItemMenu3.setTitle("线路下载");
        items.add(listItemMenu1);
        items.add(listItemMenu2);
        items.add(listItemMenu3);

        myOptionMenu = new MyOptionMenu(this,items);

        myOptionMenu.setOnClick(new MyOptionMenu.ICoallBack() {
            @Override
            public void onClick(int position) {
                switch (position) {
                    case 0:
                        for (ListItemSelected itemCommon : plineNameList) {
                            itemCommon.setIsChecked(false);
                        }
                        listItemAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        for (ListItemSelected itemCommon : plineNameList) {
                            itemCommon.setIsChecked(true);
                        }
                        listItemAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        downloadPlineList.clear();

                        for (ListItemSelected item : plineNameList) {
                            if (item.isChecked()) {
                                downloadPlineList.add(item.getTitle());
                            }
                        }

                        if(downloadPlineList.size()==0){
                            ToastUtil.show(DownloadPlineActivity.this, "当前选中的线路已经存在或者没有选择线路！");
                            return;
                        }

                        downloadPlinesClass.Download(downloadPlineList);
                        break;
                }
            }
        });
    }


    /**
     * 获取线路的数据
     *
     * @param result
     */
    private void initData(String result) {
        this.downloadPlineList = new ArrayList<String>() ;
        this.plineNameList = this.getPowerlines(result);
        this.listItemAdapter = new ListItemSelectedAdapter(this, plineNameList);
        this.listview.setAdapter(listItemAdapter);


        this.downloadPlinesClass =new DownloadPlinesClass(DownloadPlineActivity.this);
        this.downloadPlinesClass.setOnCompleted(new MultiThreadBaseClass.ICoallBack() {
            @Override
            public void onCompleted(boolean result) {
                writeDatabaseClass.WritePlines(downloadPlineList);
            }
        });

        this.writeDatabaseClass = new WriteDatabaseClass(DownloadPlineActivity.this);
        this.writeDatabaseClass.setOnCompleted(new MultiThreadBaseClass.ICoallBack() {
            @Override
            public void onCompleted(boolean result) {
                //返回之前的activity
                setResult(RESULT_OK);
                finish();
            }
        });

    }


    /**
     * 解析JSON
     *
     * @param result
     * @return
     */
    private ArrayList<ListItemSelected> getPowerlines(String result) {
        ArrayList<ListItemSelected> list = new ArrayList<>();

        PowerlineTableOpt powerlineTableOpt = new PowerlineTableOpt();
        List<Object> plineList = powerlineTableOpt.getRow();
        List<String> plinnameList = new ArrayList<>();

        for(Object object:plineList){
            plinnameList.add(((PowerlineTableDataClass)object).getPowerName());
        }

        try {
            JSONObject json = new JSONObject(result);
            String success = json.getString("message");

            if (success.equals("succeed")) {
                JSONObject data = json.getJSONObject("data");
                JSONArray table = data.getJSONArray("Table");

                for (int i = 0; i < table.length(); i++) {
                    String plinename=table.getJSONObject(i).getString("Linename");
                    //如果当前线路已经加载
                    if(plinnameList.contains(plinename)){
                        continue;
                    }

                    ListItemSelected item = new ListItemSelected();
                    item.setTitle(plinename);
                    item.setIcon(VectorDrawable.getDrawable(this, R.drawable.iconfont_powerline));
                    item.setIsChecked(false);
                    list.add(item);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }




}
