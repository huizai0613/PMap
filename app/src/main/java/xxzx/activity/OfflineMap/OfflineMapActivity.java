package xxzx.activity.OfflineMap;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Properties;

import xxzx.activity.R;
import xxzx.library.VectorDrawable;
import xxzx.login.LoginFileUtils;
import xxzx.login.User;
import xxzx.publicClass.LoadingDialog;
import xxzx.publicClass.MyHttpRequst;
import xxzx.publicClass.MyListView.ListItemOptAdapter;
import xxzx.publicClass.MyListView.ListItemOpt;
import xxzx.publicClass.FileSizeUtil;
import xxzx.publicClass.MySharedPreferences;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.SDTool;
import xxzx.publicClass.ToastUtil;

public class OfflineMapActivity extends AppCompatActivity {

    /** ListView对象 */
    private ListView listview;
    private TextView tv_storageInfo;
    private ImageView img_yunxiazai;
    private ProgressBar progress;
    private StatisticsTask statisticsTask=null;
    private LoadingDialog loadingDialog=null;
    //剩余存储空间
    private double freeSize=0.0;

    DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_map);
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
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                initStorageInfo();
                break;
            default:
                break;
        }
    }


    private void initView(){
        loadingDialog=new LoadingDialog(this);
        listview = (ListView)findViewById(R.id.listview);
        tv_storageInfo=(TextView)findViewById(R.id.tv_storageInfo);
//        ll_free=(LinearLayout)findViewById(R.id.ll_free);
//        ll_storage=(LinearLayout)findViewById(R.id.ll_storage);
        img_yunxiazai=(ImageView)findViewById(R.id.img_yunxiazai);
        progress=(ProgressBar)findViewById(R.id.progress);
        img_yunxiazai.setBackground(VectorDrawable.getDrawable(this, R.drawable.iconfont_yunxiazai));
    }




    private void initData() {
        this.initListView();
        this.initStorageInfo();
    }


    private void initListView(){
        ArrayList<ListItemOpt> mArray = new ArrayList<>();
        ListItemOpt listItemOpt1 = new ListItemOpt();
        listItemOpt1.setOptName("清空缓存");
        listItemOpt1.setIcon(VectorDrawable.getDrawable(this, R.drawable.iconfont_file));

        ListItemOpt listItemOpt2 = new ListItemOpt();
        listItemOpt2.setOptName("影像地图下载");
        listItemOpt2.setIcon(VectorDrawable.getDrawable(this, R.drawable.iconfont_file));

        ListItemOpt listItemOpt3 = new ListItemOpt();
        listItemOpt3.setOptName("矢量地图下载");
        listItemOpt3.setIcon(VectorDrawable.getDrawable(this, R.drawable.iconfont_file));

        ListItemOpt listItemOpt4 = new ListItemOpt();
        listItemOpt4.setOptName("已下载信息");
        listItemOpt4.setIcon(VectorDrawable.getDrawable(this, R.drawable.iconfont_file));

        mArray.add(listItemOpt1);
        mArray.add(listItemOpt2);
        mArray.add(listItemOpt3);

        listview.setAdapter(new ListItemOptAdapter(this, mArray));

        //listviewitem点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        clearTileCache(MyString.maps_cache_google_img_folder_path);
                        clearTileCache(MyString.maps_cache_google_img_anno_folder_path);

                        //重新计算切片缓存
                        MySharedPreferences.putOfflineTileSize(0.0);

                        initStorageInfo();
                        //完成提示框
                        Toast.makeText(OfflineMapActivity.this, "切片缓存清空完成！", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        //生成一个Intent对象
                        intent = new Intent();
                        intent.setClass(OfflineMapActivity.this, OfflineMapGgActivity.class);

                        OfflineMapActivity.this.startActivityForResult(intent, 0);
                        break;
                    case 2:
                        //生成一个Intent对象
                        intent = new Intent();
                        intent.setClass(OfflineMapActivity.this, OfflineMapGdActivity.class);
                        OfflineMapActivity.this.startActivity(intent);
                        break;
                    case 3:
                        break;
                }
            }
        });
    }

    private void initStorageInfo(){


        double storage =0.0;
        freeSize = Double.valueOf(String.valueOf(SDTool.getSDFreeSize()))/(1024*1024);

        //获取SharedPreferences对象
        double offlinesize = MySharedPreferences.getOfflineTileSize();

        //如果等于0.0,需要从新计算数据量
        if(offlinesize==0.0) {

            if (statisticsTask != null) {
                return;
            }
            loadingDialog.show();
            statisticsTask = new StatisticsTask();
            statisticsTask.execute((Void) null);
        }else{
            storage=Double.valueOf(offlinesize);
            this.showStatisticsResult(storage);
        }
    }

    /**
     * 清空离线下载的数据存储
     */
    private void clearTileCache(String folderPath){
        File root=new File(folderPath);
        File[] files=root.listFiles();
        if(files!=null){
            for(File file:files){
                try{file.delete();}catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }


    private void showStatisticsResult(double storage){
        tv_storageInfo.setText("已存储:" + df.format(storage) + "MB / 剩余:" + df.format(freeSize) + "MB");

        int process=0;

        if(freeSize==0){
            process=100;
        }else{
            process=(int)(storage*100/(freeSize+storage));
        }

        progress.setMax(100);
        progress.setProgress(process);
    }



    public class StatisticsTask extends AsyncTask<Void, Void, Double> {

        StatisticsTask() {
        }
        @Override
        protected Double doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                double size1 = FileSizeUtil.getFileOrFilesSize(MyString.maps_cache_google_img_folder_path, FileSizeUtil.SIZETYPE_MB);
                double size2 = FileSizeUtil.getFileOrFilesSize(MyString.maps_cache_google_img_anno_folder_path, FileSizeUtil.SIZETYPE_MB);
                double storage = size1 + size2;
                //存入数据
                MySharedPreferences.putOfflineTileSize(storage);
                return storage;
            } catch (Exception e) {
                return 0.0;
            }
        }

        @Override
        protected void onPostExecute(final Double result) {
            statisticsTask = null;
            loadingDialog.dismiss();
            showStatisticsResult(result);
        }

        @Override
        protected void onCancelled() {

        }
    }







}
