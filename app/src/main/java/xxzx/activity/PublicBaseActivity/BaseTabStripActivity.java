package xxzx.activity.PublicBaseActivity;


import android.os.Bundle;
import android.view.Menu;

import com.astuetz.PagerSlidingTabStrip;

import xxzx.activity.R;
import xxzx.myView.MyViewPager;

public abstract class BaseTabStripActivity extends BaseToolBarActivity {

    private static final String LOG_TAG = BaseTabStripActivity.class.getSimpleName();

    protected PagerSlidingTabStrip tabs;
    protected MyViewPager pager;
//    protected Toolbar toolbar;
//    protected TextView tv_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_pager_tabstrip);
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
        pager = (MyViewPager) findViewById(R.id.pager);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);


//        tv_title = (TextView) findViewById(R.id.tv_title);
//
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("");
//        toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);
//        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//            toolbar.setNavigationIcon(R.mipmap.toolbar_navigation_back);
//            //设置点击事件
//            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    setResult(0);
//                    BaseTabStripActivity.this.finish();
//                }
//            });
//        }
    }

    public abstract void initData();
}




