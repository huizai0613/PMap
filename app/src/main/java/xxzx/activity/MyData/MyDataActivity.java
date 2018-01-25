package xxzx.activity.MyData;


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

import xxzx.activity.PublicBaseActivity.BaseSmartTabsActivity;
import xxzx.activity.R;
import xxzx.myData.fragment.InputMapFragment;
import xxzx.myData.fragment.OutputMapFragment;
import xxzx.myData.fragment.DataLocalFragment;
import xxzx.publicClass.LoadingDialog;
import xxzx.publicClass.MyActivityFragmentListener;
import xxzx.publicClass.MyActivityManager;
import xxzx.publicClass.MyHttpRequst;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.ToastUtil;


public class MyDataActivity extends BaseSmartTabsActivity implements MyActivityFragmentListener.MyListener {

    private static final String LOG_TAG = MyDataActivity.class.getSimpleName();

    DataLocalFragment dataLocalFragment;
    OutputMapFragment outputMapFragment;
    InputMapFragment inputMapFragment;

    private LoadingDialog loadingDialog;


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
        tv_title_toobar.setText(R.string.activity_app_my_data);

        //线路下载
        Button btn_right = (Button) toolbar.findViewById(R.id.btn_right);
        btn_right.setText("下载");
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动http请求的线程
                MyRunnable runnable=new MyRunnable();
                new Thread(runnable).start();

                loadingDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== MyString.requestCode_mydataactivity_to_downloadplineactivity && resultCode == this.RESULT_OK) {
            this.viewPager.setCurrentItem(0);
        }
        //传入fragment更新数据
        dataLocalFragment.onActivityResult(requestCode, resultCode, data);
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

        loadingDialog=new LoadingDialog(this);

        dataLocalFragment=DataLocalFragment.newInstance();
        inputMapFragment=InputMapFragment.newInstance();
        outputMapFragment=OutputMapFragment.newInstance();

        this.viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        this.viewPagerTab.setViewPager(viewPager);

        MyActivityManager.getMyActivityManager().addActivity(this);
    }
    @Override
    public void initFragmentData() {
        if(dataLocalFragment!=null){
            dataLocalFragment.setmContext(MyDataActivity.this);
            dataLocalFragment.initPlineData();
        }
        if(inputMapFragment!=null){
            dataLocalFragment.setmContext(MyDataActivity.this);
            inputMapFragment.initPlineData();
        }
        if(outputMapFragment!=null){
            dataLocalFragment.setmContext(MyDataActivity.this);
            outputMapFragment.initPlineData();
        }
    }


    public class MyAdapter extends FragmentPagerAdapter {
        private String[] titles = { "本地数据","已导入地图","未导入地图"};

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return dataLocalFragment;
                case 1:
                    return inputMapFragment;
                case 2:
                    return outputMapFragment;
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



    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            loadingDialog.dismiss();
            switch (msg.what) {
                case 0:
                    ToastUtil.show(MyDataActivity.this, msg.obj.toString());
                    break;
                case 1:
                    Intent intent = new Intent();
                    intent.putExtra("result", msg.obj.toString());
                    intent.setClass(MyDataActivity.this, DownloadPlineActivity.class);

                    MyDataActivity.this.startActivityForResult(intent, MyString.requestCode_mydataactivity_to_downloadplineactivity);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    public class MyRunnable implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            MySingleClass mySingleClass = MySingleClass.getInstance();
            Properties properties = mySingleClass.getProperties();

//            String url = properties.get("url_pline_list").toString();
//            String myplinesUrl = String.format(url, mySingleClass.getUser().getmName());
//
//            String result = MyHttpRequst.getHttpGetRequst2(myplinesUrl);

            String url = properties.get("url_pline_list2").toString();

            String poststring = properties.get("url_pline_list2_poststring").toString();
            poststring  = String.format(poststring,  mySingleClass.getUser().getmName());

            String result = MyHttpRequst.getHttpPostRequst2(url,poststring);

            Message message = new Message();
            if(result.equals("")){
                message.what = 0;
                message.obj="下载错误，请确定网络是否连接";
            }else {
                try {
                    JSONObject json = new JSONObject(result);
                    String success = json.getString("message");
                    if (success.equals("succeed")) {
                        message.what = 1;
                        message.obj=result;
                    }else{
                        message.what = 0;
                        message.obj=result;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    message.what = 0;
                    message.obj=e.toString();
                }
            }

            myHandler.sendMessage(message);
        }
    }







}




