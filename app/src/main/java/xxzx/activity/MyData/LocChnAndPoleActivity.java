package xxzx.activity.MyData;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import xxzx.activity.PublicBaseActivity.BaseSmartTabsActivity;
import xxzx.activity.R;
import xxzx.myData.fragment.LocationPlineChnFragment;
import xxzx.myData.fragment.LocationPlinePoleFragment;
import xxzx.publicClass.MyActivityManager;

public class LocChnAndPoleActivity extends BaseSmartTabsActivity {

    private static final String LOG_TAG = LocChnAndPoleActivity.class.getSimpleName();

    private String plineName;

    private LocationPlineChnFragment powerChannelFragment;
    private LocationPlinePoleFragment powerPoleFragment;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);
        //toolbar.showOverflowMenu() ;
        //getLayoutInflater().inflate(R.layout.public_base_toolbar, toolbar) ;
        //tv_title = (TextView) toolbar.findViewById(R.id.tv_title);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyActivityManager.getMyActivityManager().finishActivity(this);
    }


    @Override
    public void initData() {
        MyActivityManager.getMyActivityManager().addActivity(this);

        getSupportActionBar().setTitle(this.plineName);

        this.viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        this.viewPagerTab.setViewPager(this.viewPager);

        if(powerChannelFragment==null) {
            powerChannelFragment = LocationPlineChnFragment.newInstance(plineName);

        }
        if(powerPoleFragment==null) {
            powerPoleFragment = LocationPlinePoleFragment.newInstance(plineName);
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




