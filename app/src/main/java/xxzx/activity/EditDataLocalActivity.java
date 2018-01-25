package xxzx.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.PublicBaseActivity.BaseToolBarActivity;
import xxzx.editData.EditDataInfo;
import xxzx.editData.EditDataJsonFileClass;
import xxzx.editData.ListItemDataLocalAdapter;
import xxzx.myView.DialogEditLocalData;
import xxzx.publicClass.IntentPublicMapState;
import xxzx.publicClass.MyString;
import xxzx.publicClass.ToastUtil;


public class EditDataLocalActivity extends BaseToolBarActivity {

    /**
     * ListView对象
     */
    private ListView listview;
    /**
     * 记录当前选中条目的任务索引taskID
     */
    private List<Integer> isSelectedList = new ArrayList<Integer>();
    /**
     * 读取当地数据的类
     */
    EditDataJsonFileClass editDataJsonFileClass;
    /**
     * dialog类
     */
    DialogEditLocalData dialog;

    int currentIndex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_local);

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
            setResult(RESULT_CANCELED);
            this.finish();
        }
        if (id == R.id.action_completed) {
            // 回到原来的activity
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {

        listview = (ListView) findViewById(R.id.listview);
        if (dialog == null) {
            dialog = new DialogEditLocalData(EditDataLocalActivity.this);
        }
    }


    private void initData() {
        //创建数据类
        editDataJsonFileClass = new EditDataJsonFileClass(this);
        //获取本地任务列表
        editDataJsonFileClass.ReadSDTaskJsonFile();
        //显示数据
        this.showItems();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                currentIndex = position;
                dialog.show();
            }
        });

        dialog.setOnClick(new DialogEditLocalData.ICoallBack() {
            @Override
            public void onClick(View view) {
                //对话框消失
                dialog.dismiss();

                switch (view.getId()) {
                    case R.id.imgbtn_photo:
                        imageBrower(0,editDataJsonFileClass.editDataInfoArray.get(currentIndex).getImgs());
                        break;
                    case R.id.imgbtn_map:
                        EditDataInfo editDataInfo = editDataJsonFileClass.editDataInfoArray.get(currentIndex);
                        IntentPublicMapState.Intent(EditDataLocalActivity.this,MyString.requestCode_activity_to_publicmapactivity,MyString.intent_map_state_showgeometry,editDataInfo.getGeometry());
                        break;
                    case R.id.imgbtn_delete:
                        //显示删除对话框
                        showDeleteDialog();
                        break;
                }
            }
        });
    }


    /**
     * 显示是否删除数据的对话框
     */
    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("注意");
        builder.setCancelable(false);
        builder.setMessage("确定需要删除该信息么？");
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //删除数据源
                editDataJsonFileClass.editDataInfoArray.remove(currentIndex);
                //将数据重新写入存储
                editDataJsonFileClass.writeSDTaskJsonFile();
                //显示数据列表
                showItems();
            }
        });


        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }


    /**
     * 显示数据列表
     */
    private void showItems(){
        //删除多余的图片信息，防止不被记录的图片过多
        editDataJsonFileClass.deleteUnnecessaryPhotoImages();

        //是否为空
        if (editDataJsonFileClass.editDataInfoArray == null) {
            return;
        }
        if (listview == null) {
            return;
        }
        int count = 0;//当前任务数量
        for (int i = 0; i < editDataJsonFileClass.editDataInfoArray.size(); i++) {
            EditDataInfo taskInfo = editDataJsonFileClass.editDataInfoArray.get(i);
            if (taskInfo.getTitle().trim().equals("")) {
                continue;
            }
            count++;
        }

        listview.setAdapter(new ListItemDataLocalAdapter(this, editDataJsonFileClass.editDataInfoArray, isSelectedList));
    }


    /**
     * 打开图片查看器
     *
     * @param position
     * @param urls2
     */
    protected void imageBrower(int position, ArrayList<String> urls2) {

        if(urls2.size()==0){
            ToastUtil.show(this,"没有图片信息");
            return;
        }

        Intent intent = new Intent(this, ImagePagerActivity.class);
        // 图片url
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_FOLDER,MyString.edit_photo_folder_path);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }



}