package xxzx.activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import xxzx.ImageView.NoScrollGridAdapter;
import xxzx.ImageView.NoScrollGridView;
import xxzx.activity.PublicBaseActivity.BaseToolBarActivity;
import xxzx.baseMapState.MainMap.MapStateContext;
import xxzx.editData.EditDataInfo;

import xxzx.editData.EditDataJsonFileClass;
import xxzx.library.VectorDrawable;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;

public class EditDataCreateActivity extends BaseToolBarActivity {

    private EditDataJsonFileClass  editDataJsonFileClass=null;
    private TextView txt_describtion = null;
    private TextView txt_title = null;
    private ImageButton btn_Camaro = null;
    private Button btn_Save = null;
    private TextView txt_type;
    private Button btn_type;
    private NoScrollGridView gridView = null;
    //记录SD卡中图片文件列表，用于图片删除等
    private ArrayList<String> imageUrls_init = null;
    //记录当前的图片路径文件列表,用于实时显示
    private ArrayList<String> imageUrls = null;
    private NoScrollGridAdapter noScrollGridAdapter = null;

    private String geometryWKT="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_create);
        //取得从上一个Activity当中传递过来的Intent对象
        Intent intent = getIntent();
        //从Intent当中根据key取得value
        geometryWKT = intent.getStringExtra("geometry");

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
            //请出无用的图片
            editDataJsonFileClass.deleteUnnecessaryPhotoImages();
            this.finish();
        }
        if (id == R.id.action_completed) {
            //回到原来的activity
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {

        txt_describtion = (TextView) findViewById(R.id.txt_describtion);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_type = (TextView) findViewById(R.id.txt_type);
        btn_type = (Button) findViewById(R.id.btn_type);
        gridView = (NoScrollGridView) findViewById(R.id.gridView);
        btn_Camaro = (ImageButton) findViewById(R.id.btn_Camaro);
        btn_Camaro.setBackground(VectorDrawable.getDrawable(this, R.drawable.iconfont_camaro));

        btn_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final String items[] = {"通道", "隐患点"};
                //dialog参数设置,先得到构造器
                AlertDialog.Builder builder = new AlertDialog.Builder(EditDataCreateActivity.this);
                builder.setTitle("选择"); //设置标题
                //builder.setIcon(R.mipmap.ic_launcher);
                //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txt_type.setText(items[which]);
                    }
                });
                builder.create().show();
            }
        });

        // 点击拍照事件处理函数
        btn_Camaro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //调用系统拍照功能
                // 跳转至拍照界面
                Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                File out = new File(MyString.image_temp_path);
                Uri uri = Uri.fromFile(out);
                // 获取拍照后未压缩的原图片，并保存在uri路径中
                intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                //intentPhote.putExtra(MediaStore.Images.Media.ORIENTATION, 180);
                startActivityForResult(intentPhote, MyString.requestCode_activity_to_photoactivity);
            }
        });

        btn_Save = (Button) findViewById(R.id.btn_Save);
        // 点击保存事件处理函数
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //保存处理
                boolean is = saveNewEditInfo();

                if (!is) {
                    Toast.makeText(EditDataCreateActivity.this, "保存失败，确认标题不为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                //保存完成之后询问转换界面
                Dialog dialog = new AlertDialog.Builder(EditDataCreateActivity.this).setIcon(
                        android.R.drawable.btn_star).setTitle("提示").setMessage("数据保存成功，请选择如下操作："
                ).setPositiveButton("返回地图界面",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                //地图界面回到主界面
                                MySingleClass mySingleClass = MySingleClass.getInstance();
                                MapStateContext mapStateContext = mySingleClass.getMapStateContext();
                                mapStateContext.backMainState();
                                mapStateContext.InitViewAndData();

                                //销毁该activity
                                EditDataCreateActivity.this.finish();
                            }
                        }).setNegativeButton("继续编辑", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        //销毁该activity
                        EditDataCreateActivity.this.finish();

                    }
                }).create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.show();
            }
        });
    }


    private void initData() {
        this.createPhotoFolder();
        //创建数据类
        editDataJsonFileClass = new EditDataJsonFileClass(EditDataCreateActivity.this);
        //获取本地任务列表
        editDataJsonFileClass.ReadSDTaskJsonFile();
        //删除多余的图片信息，防止删除不被记录的图片过多
        editDataJsonFileClass.deleteUnnecessaryPhotoImages();
        //初始化图片数组
        imageUrls_init = new ArrayList<String>();
        imageUrls = new ArrayList<String>();

        noScrollGridAdapter = new NoScrollGridAdapter(this, imageUrls,MyString.edit_photo_folder_path);
        gridView.setAdapter(noScrollGridAdapter);

        // 单击回帖九宫格，查看大图
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                imageBrower(position, imageUrls);
            }
        });

        //长按九宫格，提示是否删除图片
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // TODO Auto-generated method stub
                showDeleteDialog(position);
                return true;
            }
        });
    }

    /**
     * 打开图片查看器
     *
     * @param position
     * @param urls2
     */
    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        // 图片url
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_FOLDER,MyString.edit_photo_folder_path);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }

    /**
     * 主要用于activity之间的传值，拍照activity完成之后返回该activity时数据处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MyString.requestCode_activity_to_photoactivity && resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();

            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile", "SD card is not avaiable/writeable right now.");
                return;
            }
            String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
            String fileName = MyString.edit_photo_folder_path + "/" + name;
            this.cutfileToNewFile(MyString.image_temp_path, fileName, true);

            imageUrls_init.add(name);
            imageUrls.add(name);

            noScrollGridAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 创建照片文件夹
     */
    private void createPhotoFolder() {
        //创建task根目录
        File folder = new File(MyString.edit_photo_folder_path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    /**
     * @param fromFilePath 被复制的文件
     * @param toFilePath   复制的目录文件
     * @param rewrite      是否重新创建文件
     *                     <p/>
     *                     <p>文件的复制操作方法
     */
    private void cutfileToNewFile(String fromFilePath, String toFilePath, Boolean rewrite) {

        File fromFile = new File(fromFilePath);
        File toFile = new File(toFilePath);

        if (!fromFile.exists()) {
            return;
        }

        if (!fromFile.isFile()) {
            return;
        }
        if (!fromFile.canRead()) {
            return;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists() && rewrite) {
            toFile.delete();
        }
        try {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);

            byte[] bt = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            //关闭输入、输出流
            fosfrom.close();
            fosto.close();

            fromFile.delete();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 显示删除对话框
     * @param position
     */
    protected void showDeleteDialog( final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确实要删除该图片么吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //删除图片列表中的文件路径
                imageUrls.remove(position);
                noScrollGridAdapter.notifyDataSetChanged();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    /**
     * 删除从九宫格中移除的图片
     */
    private void deleteImages(){
        for(String imageName:imageUrls_init){
            //如果现在的图片列表不包含原始的图片列表中的路径，删除该路径文件
            if(!imageUrls.contains(imageName)){
                File file=new File(MyString.edit_photo_folder_path+"/"+imageName);
                //如果存在，则删除
                if(file.exists()) {
                    file.delete();
                }
            }
        }
    }


    /**
     * 保存新的editinfo
     */
    private boolean saveNewEditInfo(){

        String title = txt_title.getText().toString().trim();

        if(title.equals("")){
            return false;
        }

        try {
            String describtion = txt_describtion.getText().toString().trim();
            EditDataInfo editDataInfo = new EditDataInfo();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(new Date());

            editDataInfo.setTitle(title);//标题
            editDataInfo.setDescribtion(describtion);//描述
            editDataInfo.setGeometry(geometryWKT);//图形
            editDataInfo.setDate(time);//日期
            editDataInfo.setId(editDataJsonFileClass.editDataInfoArray.size());//id号

            //将图片路径添加到任务类中
            editDataInfo.setImgs(imageUrls);//图片列表

            if (imageUrls_init != null || imageUrls_init.size() != 0) {
                deleteImages();
            }
            //替换当前的taskinfo
            editDataJsonFileClass.editDataInfoArray.add(0,editDataInfo);
            //将新的任务信息写入json文件中
            editDataJsonFileClass.writeSDTaskJsonFile();
            //删除多余的图片信息
            editDataJsonFileClass.deleteUnnecessaryPhotoImages();
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}
