package xxzx.activity.Patrol.Record;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.PublicBaseActivity.BaseSmartTabsActivity;
import xxzx.activity.R;
import xxzx.patrol.Record.ChnOrPole;
import xxzx.patrol.Record.RecordChnFragment;
import xxzx.patrol.Record.RecordPoleFragment;
import xxzx.publicClass.ToastUtil;
import xxzx.upload.ListItemUpdateDangerClass;
import xxzx.upload.UpLoadDangerClass;

public class RecordChnAndPoleActivity extends BaseSmartTabsActivity {

    private static final String LOG_TAG = RecordChnAndPoleActivity.class.getSimpleName();
    private UpLoadDangerClass upLoadDangerClass;
    private ArrayList<ListItemUpdateDangerClass> itemList;

    private RecordChnFragment recordChnFragment;
    private RecordPoleFragment recordPoleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // 点击返回图标事件
        if (id == android.R.id.home) {
            setResult(RESULT_CANCELED);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void initData() {

        this.viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        this.viewPagerTab.setViewPager(this.viewPager);

        if(recordChnFragment==null) {
            recordChnFragment = RecordChnFragment.newInstance();
        }
        if(recordPoleFragment==null) {
            recordPoleFragment = RecordPoleFragment.newInstance();
        }

        //初始化上传类
        upLoadDangerClass = new UpLoadDangerClass(RecordChnAndPoleActivity.this);
        //设置监听函数
        upLoadDangerClass.setOnComplatedListener(new UpLoadDangerClass.ICoallBack() {
            @Override
            public void onComplatedListener() {
                //重新获取更新
                itemList.clear();
                //重新加载数据
                recordChnFragment.initData();
                recordPoleFragment.initData();
            }
        });
    }

    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);
        toolbar.showOverflowMenu() ;
        getLayoutInflater().inflate(R.layout.public_base_toolbar_title_left_btn_content, toolbar) ;

        //设置标题
        TextView tv_title_toobar = (TextView) toolbar.findViewById(R.id.tv_title_toobar);
        tv_title_toobar.setText(R.string.activity_app_update_data);
        //线路下载
        Button btn_right = (Button) toolbar.findViewById(R.id.btn_right);
        btn_right.setText("全部上传");
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemList == null) {
                    itemList = new ArrayList<ListItemUpdateDangerClass>();
                }
                itemList.clear();
                List<ListItemUpdateDangerClass> list1 = UpLoadDangerClass.getAllDangerItem(ChnOrPole.CHANNEL, -1);
                List<ListItemUpdateDangerClass> list2 = UpLoadDangerClass.getAllDangerItem(ChnOrPole.POLE, -1);
                itemList.addAll(list1);
                itemList.addAll(list2);


                if (itemList.size() == 0) {
                    ToastUtil.show(RecordChnAndPoleActivity.this, "没有可上传的记录信息");
                    return;
                }

                upLoadDangerClass.setUpdateItemList(itemList);
                upLoadDangerClass.update();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //重新加载数据
        if(recordChnFragment!=null){
            recordChnFragment.initData();
        }

        if(recordPoleFragment!=null){
            recordPoleFragment.initData();
        }
    }


    public class MyAdapter extends FragmentPagerAdapter {
        private String[] titles = { "通道","杆塔"};
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return recordChnFragment;
                case 1:
                    return recordPoleFragment;
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




