package xxzx.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Properties;

import xxzx.activity.MyData.DownloadPlineActivity;
import xxzx.activity.PublicBaseActivity.BaseSmartTabsActivity;
import xxzx.dangerRadar.DangerChnFragment;
import xxzx.dangerRadar.DangerPoleFragment;
import xxzx.myData.fragment.DataLocalFragment;
import xxzx.myData.fragment.InputMapFragment;
import xxzx.myData.fragment.OutputMapFragment;
import xxzx.publicClass.LoadingDialog;
import xxzx.publicClass.MyActivityFragmentListener;
import xxzx.publicClass.MyActivityManager;
import xxzx.publicClass.MyHttpRequst;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.ToastUtil;


public class DangerRadarActivity extends BaseSmartTabsActivity implements MyActivityFragmentListener.MyListener {

    private static final String LOG_TAG = DangerRadarActivity.class.getSimpleName();

    DangerChnFragment dangerChnFragment;
    DangerPoleFragment dangerPoleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_toolbar_my_data, menu);
        return true;
    }

    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);
        toolbar.showOverflowMenu() ;
        getLayoutInflater().inflate(R.layout.public_base_toolbar_title_left_btn_content, toolbar) ;

        //设置标题
        TextView tv_title_toobar = (TextView) toolbar.findViewById(R.id.tv_title_toobar);
        tv_title_toobar.setText(R.string.activity_app_danger_radar);

        //线路下载
        Button btn_right = (Button) toolbar.findViewById(R.id.btn_right);
        btn_right.setText("下载");
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动http请求的线程




            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode== MyString.requestCode_mydataactivity_to_downloadplineactivity && resultCode == this.RESULT_OK) {
//            this.viewPager.setCurrentItem(0);
//        }
        //传入fragment更新数据
        //dangerChnFragment.onActivityResult(requestCode, resultCode, data);
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
    public void onDestroy() {
        super.onDestroy();
        MyActivityManager.getMyActivityManager().finishActivity(this);
    }

    @Override
    public void initData() {

        dangerChnFragment=DangerChnFragment.newInstance();
        dangerPoleFragment=DangerPoleFragment.newInstance();

        this.viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        this.viewPagerTab.setViewPager(viewPager);

        MyActivityManager.getMyActivityManager().addActivity(this);
    }
    @Override
    public void initFragmentData() {
        if(dangerChnFragment!=null){
            dangerChnFragment.setmContext(DangerRadarActivity.this);
            //dangerChnFragment.initPlineData();
        }
        if(dangerPoleFragment!=null){
            dangerPoleFragment.setmContext(DangerRadarActivity.this);
            //dangerPoleFragment.initPlineData();
        }
    }


    public class MyAdapter extends FragmentPagerAdapter {
        private String[] titles = { "通道隐患","杆塔隐患"};

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return dangerChnFragment;
                case 1:
                    return dangerPoleFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }




}




