package xxzx.activity.Patrol.TaskSetting;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Menu;
import android.widget.TextView;

import xxzx.activity.PublicBaseActivity.BaseSmartTabsActivity;
import xxzx.activity.PublicBaseActivity.BaseTabStripActivity;
import xxzx.activity.R;
import xxzx.patrol.Powerline.PowerChnFragment;
import xxzx.patrol.Powerline.PowerPoleFragment;

public class ChnAndPoleActivity extends BaseSmartTabsActivity {

    private static final String LOG_TAG = ChnAndPoleActivity.class.getSimpleName();

    private String plineName;

    private PowerChnFragment powerChannelFragment;
    private PowerPoleFragment powerPoleFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        plineName = intent.getStringExtra("plinename");

        this.initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_toolbar_patrol_channel_pole, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        setResult(0);
        this.finish();
        super.onBackPressed();
    }


    @Override
    public void initData() {

        getSupportActionBar().setTitle(this.plineName);

        this.viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        this.viewPagerTab.setViewPager(this.viewPager);
        if(powerChannelFragment==null) {
            powerChannelFragment = PowerChnFragment.newInstance(plineName);
        }
        if(powerPoleFragment==null) {
            powerPoleFragment = PowerPoleFragment.newInstance(plineName);
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
                    return powerChannelFragment;
                case 1:
                    return powerPoleFragment;
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




