package xxzx.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.PublicBaseActivity.BaseToolBarActivity;
import xxzx.deletePlineData.PlineDeleteClass;
import xxzx.deletePlineData.PlineUpdateClass;
import xxzx.library.VectorDrawable;
import xxzx.publicClass.MyListView.ListItemOptAdapter;
import xxzx.publicClass.MyListView.ListItemOpt;
import xxzx.publicClass.ToastUtil;
import xxzx.spatialite.TableOptClass.PowerlineTableOpt;
import xxzx.spatialite.TableStruct.PowerlineTableDataClass;

/**
 * A login screen that offers login via email/password.
 */
public class SysSettingActivity extends BaseToolBarActivity {


    /** ListView对象 */
    private ListView listview1;
    private ListView listview2;
    private ListView listview3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_setting);

        //工具栏上的箭头
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        // 决定左上角图标的右侧是否有向左的小箭头, true
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 有小箭头，并且图标可以点击
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        this.initView();
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

    private void initView(){
        listview1 = (ListView)findViewById(R.id.listview1);
        listview2 = (ListView)findViewById(R.id.listview2);
        listview3 = (ListView)findViewById(R.id.listview3);
    }



    private void initData(){
        ArrayList<ListItemOpt> mArray1 = new ArrayList<>();
        ArrayList<ListItemOpt> mArray2 = new ArrayList<>();
        ArrayList<ListItemOpt> mArray3 = new ArrayList<>();

        ListItemOpt listItemOpt0 = new ListItemOpt();
        listItemOpt0.setOptName("恢复初始化设置");
        listItemOpt0.setIcon(VectorDrawable.getDrawable(this, R.drawable.iconfont_file));

        ListItemOpt listItemOpt1 = new ListItemOpt();
        listItemOpt1.setOptName("清空线路数据库信息");
        listItemOpt1.setIcon(VectorDrawable.getDrawable(this, R.drawable.iconfont_file));

        ListItemOpt listItemOpt2 = new ListItemOpt();
        listItemOpt2.setOptName("清空错误日志缓存");
        listItemOpt2.setIcon(VectorDrawable.getDrawable(this, R.drawable.iconfont_file));

        ListItemOpt listItemOpt3 = new ListItemOpt();
        listItemOpt3.setOptName("清空GPS线路采集信息");
        listItemOpt3.setIcon(VectorDrawable.getDrawable(this, R.drawable.iconfont_file));


        ListItemOpt listItemOpt4 = new ListItemOpt();
        listItemOpt4.setOptName("数据库结构一键修复");
        listItemOpt4.setIcon(VectorDrawable.getDrawable(this, R.drawable.iconfont_file));


        mArray1.add(listItemOpt0);

        mArray2.add(listItemOpt1);
        mArray2.add(listItemOpt2);
        mArray2.add(listItemOpt3);

        mArray3.add(listItemOpt4);

        listview1.setAdapter(new ListItemOptAdapter(this, mArray1));
        //listviewitem点击事件
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        AlertDialog.Builder builder = new AlertDialog.Builder(SysSettingActivity.this);
                        builder.setTitle("系统初始化");
                        builder.setCancelable(true);
                        builder.setMessage("该操作将删除所有线路数据库、图片信息以及错误日志信息,确定执行该操作？");
                        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PlineDeleteClass.deleteAllPlines();
                                PlineDeleteClass.deleteCrashFile();
                                PlineDeleteClass.deleteGpsFile();
                                ToastUtil.show(SysSettingActivity.this, "系统初始化完成！");
                            }
                        });
                        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                        break;
                }
            }
        });


        listview2.setAdapter(new ListItemOptAdapter(this, mArray2));
        //listviewitem点击事件
        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SysSettingActivity.this);
                switch (position) {
                    case 0:
                        builder.setTitle("删除线路数据库信息");
                        builder.setCancelable(true);
                        builder.setMessage("该操作将删除所有线路数据库以及对应图片信息,确定执行该操作？");
                        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PlineDeleteClass.deleteAllPlines();
                                ToastUtil.show(SysSettingActivity.this, "数据库数据清空完成！");
                            }
                        });
                        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                        break;
                    case 1:
                        builder.setTitle("删除错误日志信息");
                        builder.setCancelable(true);
                        builder.setMessage("该操作将删除错误日志信息,确定执行该操作？");
                        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PlineDeleteClass.deleteCrashFile();
                                ToastUtil.show(SysSettingActivity.this, "系统错误日志文件删除完成！");
                            }
                        });
                        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                        break;
                    case 2:
                        builder.setTitle("删除GPS路径");
                        builder.setCancelable(true);
                        builder.setMessage("该操作将删除GPS路径记录文件,确定执行该操作？");
                        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PlineDeleteClass.deleteGpsFile();
                                ToastUtil.show(SysSettingActivity.this, "GPS路径记录文件删除完成！");
                            }
                        });
                        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                        break;
                    case 3:
                        break;
                }
            }
        });



        listview3.setAdapter(new ListItemOptAdapter(this, mArray3));

        //listviewitem点击事件
        listview3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:

                        List<String> list_plinename = new ArrayList<String>();
                        //先读取已有线路
                        PowerlineTableOpt powerlineTableOpt = new PowerlineTableOpt();
                        List<Object> list = powerlineTableOpt.getRow();

                        if (list.size() == 0) {
                            PlineDeleteClass.deleteAllPlines();
                            ToastUtil.show(SysSettingActivity.this,"修复完成");
                            return;
                        }

                        List<Integer> isInMapList = new ArrayList<Integer>();
                        List<Integer> isTaskList = new ArrayList<Integer>();
                        for (Object o : list) {
                            String name = ((PowerlineTableDataClass) o).getPowerName();
                            list_plinename.add(name);
                            isInMapList.add(((PowerlineTableDataClass) o).getIsInMap());
                            isTaskList.add(((PowerlineTableDataClass) o).getIsSelect());
                        }

                        PlineUpdateClass plineUpdateClass = new PlineUpdateClass(SysSettingActivity.this);
                        plineUpdateClass.list_plinename = list_plinename;
                        plineUpdateClass.isTaskList = isTaskList;
                        plineUpdateClass.isInMapList = isTaskList;
                        plineUpdateClass.update();

                        break;
                }
            }
        });




    }



}

