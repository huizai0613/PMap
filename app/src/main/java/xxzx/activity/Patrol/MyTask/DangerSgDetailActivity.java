package xxzx.activity.Patrol.MyTask;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import xxzx.activity.R;
import xxzx.publicClass.IntentFlag;
import xxzx.spatialite.TableOptClass.DangerSgTableOpt;
import xxzx.spatialite.TableStruct.DangerSgTableDataClass;

public class DangerSgDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DangerSgDetailActivity.class.getSimpleName();

    //private Button btn_save;
    private AutoCompleteTextView tv_sgDepartment;
    private AutoCompleteTextView tv_sgContact;
    private AutoCompleteTextView tv_sgPhone;
    private AutoCompleteTextView tv_keeper;
    private AutoCompleteTextView tv_keeperPhone;
    private AutoCompleteTextView tv_ywContact;
    private AutoCompleteTextView tv_ywPhone;
    private AutoCompleteTextView tv_guank1;
    private AutoCompleteTextView tv_guank2;
    private AutoCompleteTextView tv_guank3;
    private AutoCompleteTextView tv_guank4;
    private AutoCompleteTextView tv_guank5;
    private AutoCompleteTextView tv_location;

    private DangerSgTableDataClass dangerSgTableDataClass = null;

    //记录通道隐患详细信息所对应的隐患表的rowid
    private int danger_rowid=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_sg_detail);
        Intent intent = getIntent();
        this.dangerSgTableDataClass = (DangerSgTableDataClass)intent.getSerializableExtra(IntentFlag.INTENT_FLAG_DANGER_SG_INFO);

        this.initView();
        this.initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_toolbar_patrol_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }


    private void initView() {
        tv_sgDepartment = (AutoCompleteTextView) findViewById(R.id.tv_sgDepartment);
        tv_sgContact = (AutoCompleteTextView) findViewById(R.id.tv_sgContact);
        tv_sgPhone = (AutoCompleteTextView) findViewById(R.id.tv_sgPhone);
        tv_keeper = (AutoCompleteTextView) findViewById(R.id.tv_keeper);
        tv_keeperPhone = (AutoCompleteTextView) findViewById(R.id.tv_keeperPhone);
        tv_ywContact = (AutoCompleteTextView) findViewById(R.id.tv_ywContact);
        tv_ywPhone = (AutoCompleteTextView) findViewById(R.id.tv_ywPhone);
        tv_guank1 = (AutoCompleteTextView) findViewById(R.id.tv_guank1);
        tv_guank2 = (AutoCompleteTextView) findViewById(R.id.tv_guank2);
        tv_guank3 = (AutoCompleteTextView) findViewById(R.id.tv_guank3);
        tv_guank4 = (AutoCompleteTextView) findViewById(R.id.tv_guank4);
        tv_guank5 = (AutoCompleteTextView) findViewById(R.id.tv_guank5);
        tv_location = (AutoCompleteTextView) findViewById(R.id.tv_location);

        RelativeLayout rl_toolbar =(RelativeLayout) findViewById(R.id.include_toolbar_danger_sg_detail);

        Toolbar toolbar = (Toolbar) rl_toolbar.findViewById(R.id.toolbar);

        toolbar.setTitle("");
        TextView tv_title = (TextView) toolbar.findViewById(R.id.tv_title);
        TextView tv_subtitle = (TextView) toolbar.findViewById(R.id.tv_subtitle);
        tv_title.setText("施工隐患详细信息");
        tv_subtitle.setText("");

        Button btn_right = (Button) toolbar.findViewById(R.id.btn_right);
        btn_right.setText("完成");
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setSgDetail();
                Intent intent=new Intent();
                intent.putExtra(IntentFlag.INTENT_FLAG_DANGER_SG_INFO, dangerSgTableDataClass);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.toolbar_navigation_back);
            //设置点击事件
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DangerSgDetailActivity.this.finish();
                }
            });
        }

//        // 点击保存事件处理函数
//        btn_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//
//            }
//        });
    }

    private void initData() {

        tv_sgDepartment.setText(this.dangerSgTableDataClass.getSgdepartment());
        tv_sgContact.setText(this.dangerSgTableDataClass.getSgcontact());
        tv_sgPhone.setText(this.dangerSgTableDataClass.getSgphone());
        tv_keeper.setText(this.dangerSgTableDataClass.getKeeper());
        tv_keeperPhone.setText(this.dangerSgTableDataClass.getKeeperphone());
        tv_ywContact.setText(this.dangerSgTableDataClass.getYwcontact());
        tv_ywPhone.setText(this.dangerSgTableDataClass.getYwphone());
        tv_guank1.setText(this.dangerSgTableDataClass.getGuank1());
        tv_guank2.setText(this.dangerSgTableDataClass.getGuank2());
        tv_guank3.setText(this.dangerSgTableDataClass.getGuank3());
        tv_guank4.setText(this.dangerSgTableDataClass.getGuank4());
        tv_guank5.setText(this.dangerSgTableDataClass.getGuank5());
        tv_location.setText(this.dangerSgTableDataClass.getLocation());
    }


    private  void  setSgDetail(){
        this.dangerSgTableDataClass.setSgdepartment(tv_sgDepartment.getText().toString().trim());
        this.dangerSgTableDataClass.setSgcontact(tv_sgContact.getText().toString().trim());
        this.dangerSgTableDataClass.setSgphone(tv_sgPhone.getText().toString().trim());
        this.dangerSgTableDataClass.setKeeper(tv_keeper.getText().toString().trim());
        this.dangerSgTableDataClass.setKeeperphone(tv_keeperPhone.getText().toString().trim());
        this.dangerSgTableDataClass.setYwcontact(tv_ywContact.getText().toString().trim());
        this.dangerSgTableDataClass.setYwphone(tv_ywPhone.getText().toString().trim());
        this.dangerSgTableDataClass.setGuank1(tv_guank1.getText().toString().trim());
        this.dangerSgTableDataClass.setGuank2(tv_guank2.getText().toString().trim());
        this.dangerSgTableDataClass.setGuank3(tv_guank3.getText().toString().trim());
        this.dangerSgTableDataClass.setGuank4(tv_guank4.getText().toString().trim());
        this.dangerSgTableDataClass.setGuank5(tv_guank5.getText().toString().trim());
        this.dangerSgTableDataClass.setLocation(tv_location.getText().toString().trim());
    }





}




