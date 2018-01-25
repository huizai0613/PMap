package xxzx.activity.Patrol.MyTask;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import xxzx.activity.PublicBaseActivity.BaseSmartTabsActivity;
import xxzx.activity.PublicBaseActivity.BaseTabStripActivity;
import xxzx.activity.R;
import xxzx.patrol.myTask.MyTaskPowerChnFragment;
import xxzx.patrol.myTask.MyTaskPowerPoleFragment;

public class MyTaskChnAndPoleActivity extends BaseSmartTabsActivity {

    private static final String LOG_TAG = MyTaskChnAndPoleActivity.class.getSimpleName();

    private String plineName;

    private MyTaskPowerChnFragment myTaskPowerChannelFragment;
    private MyTaskPowerPoleFragment myTaskPowerPoleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        plineName = intent.getStringExtra("plinename");

        this.initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(myTaskPowerChannelFragment!=null){
            myTaskPowerChannelFragment.initData();
        }
        if(myTaskPowerPoleFragment!=null){
            myTaskPowerPoleFragment.initData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_toolbar_patrol_channel_pole, menu);
        return true;
    }


    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);
    }


    @Override
    public void initData() {
        //设置标题
        getSupportActionBar().setTitle(this.plineName);

        this.viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        this.viewPagerTab.setViewPager(this.viewPager);

        if(myTaskPowerChannelFragment==null) {
            myTaskPowerChannelFragment = MyTaskPowerChnFragment.newInstance(plineName);
        }
        if(myTaskPowerPoleFragment==null) {
            myTaskPowerPoleFragment = MyTaskPowerPoleFragment.newInstance(plineName);
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
                    return myTaskPowerChannelFragment;
                case 1:
                    return myTaskPowerPoleFragment;
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




