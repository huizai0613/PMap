package xxzx.activity.PublicBaseActivity;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import xxzx.activity.R;

public abstract class BaseSmartTabsActivity extends BaseToolBarActivity {

    private static final String LOG_TAG = BaseSmartTabsActivity.class.getSimpleName();

    protected ViewPager viewPager;
    protected SmartTabLayout viewPagerTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_pager_tabs_smart);
        this.initView();
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


    private void initView() {
        ViewGroup tab = (ViewGroup) findViewById(R.id.tab);
        tab.addView(LayoutInflater.from(this).inflate(R.layout.public_pager_tabs_header, tab, false));

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
    }

    public abstract void initData();
}




