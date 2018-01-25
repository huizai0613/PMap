package xxzx.activity.Update;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.ImagePagerActivity;
import xxzx.activity.R;
import xxzx.activity.PublicBaseActivity.BaseToolBarActivity;
import xxzx.patrol.Record.ChnOrPole;
import xxzx.publicClass.LoadingDialog;
import xxzx.publicClass.MyString;
import xxzx.publicClass.ToastUtil;
import xxzx.spatialite.TableOptClass.ChannelDangerTableOpt;
import xxzx.spatialite.TableOptClass.PoleDangerTableOpt;
import xxzx.spatialite.TableStruct.ChannelDangerTableDataClass;
import xxzx.spatialite.TableStruct.PoleDangerTableDataClass;
import xxzx.upload.ListItemUpdateDangerAdapter;
import xxzx.upload.ListItemUpdateDangerClass;
import xxzx.upload.UpLoadDangerClass;

public class UpdateDangerActivity extends BaseToolBarActivity {

    /**
     * ListView对象
     */
    private ListView listview;
    private TextView tv_title;

    private ArrayList<ListItemUpdateDangerClass> itemList;
    private ListItemUpdateDangerAdapter listItemAdapter;
    private UpLoadDangerClass upLoadDangerClass;


    private String channelorpoleName;
    private int objectId;
    private int mChnOrPoleType = ChnOrPole.NULL;

    private LoadingDialog loadingDialog = null;

    private String[] items = {"查看相片", "上传隐患点"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_listview);

        Intent intent = getIntent();
        this.channelorpoleName = intent.getStringExtra("channelorpolename");
        this.objectId = intent.getIntExtra("objectid",-1);
        this.mChnOrPoleType = intent.getIntExtra("chnorpoletype", ChnOrPole.NULL);

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
            //回到原来的activity
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);
        toolbar.showOverflowMenu();
        getLayoutInflater().inflate(R.layout.public_base_toolbar_title_left_btn_content, toolbar);

        //设置标题
        tv_title = (TextView) toolbar.findViewById(R.id.tv_title_toobar);

        //线路下载
        Button btn_right = (Button) toolbar.findViewById(R.id.btn_right);
        btn_right.setText("全部上传");

        btn_right.setVisibility(View.VISIBLE);
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemList.size() == 0) {
                    ToastUtil.show(UpdateDangerActivity.this, "没有可上传的记录信息");
                    return;
                }

                upLoadDangerClass.setUpdateItemList(itemList);
                upLoadDangerClass.update();
            }
        });
    }

    private void initView() {
        loadingDialog = new LoadingDialog(this);
        listview = (ListView) findViewById(R.id.listview);

        tv_title.setText(this.channelorpoleName);

    }

    private void initData() {
        //初始化上传类
        upLoadDangerClass = new UpLoadDangerClass(UpdateDangerActivity.this);
        //设置监听函数
        upLoadDangerClass.setOnComplatedListener(new UpLoadDangerClass.ICoallBack() {
            @Override
            public void onComplatedListener() {
                //重新获取更新
                itemList.clear();
                List<ListItemUpdateDangerClass> list = UpLoadDangerClass.getAllDangerItem(mChnOrPoleType, objectId);
                itemList.addAll(list);
                //更新listview
                listItemAdapter.notifyDataSetChanged();
            }
        });

        //获得符合条件的隐患点信息
        this.itemList = UpLoadDangerClass.getAllDangerItem(this.mChnOrPoleType, this.objectId);

        listItemAdapter = new ListItemUpdateDangerAdapter(this, itemList);
        listview.setAdapter(listItemAdapter);
        //listviewitem点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDangerActivity.this);
                builder.setTitle("请选择");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListItemUpdateDangerClass item = (ListItemUpdateDangerClass) parent.getAdapter().getItem(position);

                        switch (which) {
                            case 0:
                                showDangerPics(item);
                                break;
                            case 1:
                                ArrayList<ListItemUpdateDangerClass> list = new ArrayList<ListItemUpdateDangerClass>();
                                list.add(item);
                                upLoadDangerClass.setUpdateItemList(list);
                                upLoadDangerClass.update();
                                break;
                        }
                    }
                });

                builder.create().show();
            }
        });
    }


    /**
     * 显示隐患点图片
     */
    private void showDangerPics(ListItemUpdateDangerClass item){

        ArrayList<String> urls = new ArrayList<String>();
        Object object = null;
        String[] strs = null;
        if (mChnOrPoleType == ChnOrPole.POLE) {
            PoleDangerTableOpt poleDangerTableOpt = new PoleDangerTableOpt();
            object = (PoleDangerTableDataClass) poleDangerTableOpt.getRow(item.getRowID());
            strs = ((PoleDangerTableDataClass) object).getPicsJson().split(";");
        } else if (mChnOrPoleType == ChnOrPole.CHANNEL) {
            ChannelDangerTableOpt channelDangerTableOpt = new ChannelDangerTableOpt();
            object = (ChannelDangerTableDataClass) channelDangerTableOpt.getRow(item.getRowID());
            strs = ((ChannelDangerTableDataClass) object).getPicsJson().split(";");
        }

        if (strs == null||strs.length==0) {
            ToastUtil.show(UpdateDangerActivity.this, "没有查询到图片路径");
            return;
        }
        for (String picname : strs) {
            if (!TextUtils.isEmpty(picname)) {
                urls.add(picname);
            }
        }

        if (urls.size() == 0) {
            ToastUtil.show(UpdateDangerActivity.this, "该隐患点没有图片信息");
            return;
        }

        //切换到图片查看
        Intent intent = new Intent(UpdateDangerActivity.this, ImagePagerActivity.class);
        // 图片url
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_FOLDER, MyString.image_pline_danger_folder_path);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
        startActivity(intent);


    }



}



