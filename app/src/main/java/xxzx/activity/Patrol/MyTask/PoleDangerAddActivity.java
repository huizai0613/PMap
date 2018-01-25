package xxzx.activity.Patrol.MyTask;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import xxzx.ImageView.NoScrollGridAdapter;
import xxzx.ImageView.NoScrollGridView;
import xxzx.activity.ImagePagerActivity;
import xxzx.activity.R;
import xxzx.publicClass.DangerVersion;
import xxzx.publicClass.MyImgOpt.ImageCompress;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.PublicStaticFunUtil;
import xxzx.publicClass.ToastUtil;
import xxzx.spatialite.TableOptClass.PoleDangerTableOpt;
import xxzx.spatialite.TableStruct.PoleDangerTableDataClass;

public class PoleDangerAddActivity extends AppCompatActivity {

    private static final String LOG_TAG = PoleDangerAddActivity.class.getSimpleName();

    private Button btn_camaro;
    private Button btn_save;
    private AutoCompleteTextView tv_dangerName;
    private AutoCompleteTextView tv_spotMarks;
    private RadioGroup rdoGroup;
    private RadioGroup rdoGroup_huilu;
    private LinearLayout ll_huilu ;

    private NoScrollGridView gridView = null;
    //记录SD卡中图片文件列表，用于图片删除等
    //private ArrayList<String> imageUrls_init = null;
    //记录当前的图片路径文件列表,用于实时显示
    private ArrayList<String> imageUrls = null;
    private NoScrollGridAdapter noScrollGridAdapter = null;


    private String poleName;
    private int poleObjectId;
    private String plineName;
    private int dangerType;
    private int dangerLevel=-1;
    private int huiluType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_pole_add);

        Intent intent = getIntent();
        this.poleName = intent.getStringExtra("polename");
        this.poleObjectId = intent.getIntExtra("poleobjectid",-1);
        this.plineName = intent.getStringExtra("plinename");
        this.dangerType = intent.getIntExtra("dangertype", -1);
        this.initView();
        this.initData();
    }

    /**
     * 复写acitivity的onConfigurationChanged方法,防止屏幕旋转后重建
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //其实这里什么都不要做
        super.onConfigurationChanged(newConfig);
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

        switch (id) {
            case android.R.id.home:
                //删除图片临时文件夹
                this.deleteFolderFile(MyString.image_temp_folder_path);
                this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        this.deleteFolderFile(MyString.image_temp_folder_path);
        super.onBackPressed();
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
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd_hhmmss", Locale.getDefault());
            String name = sdf2.format(new Date()) + ".jpg";
            String fileName = MyString.image_temp_folder_path + "/" + name;

            //图片压缩
            ImageCompress.compressImage(MyString.image_temp_path,fileName);

            imageUrls.add(name);

            noScrollGridAdapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        tv_dangerName = (AutoCompleteTextView) findViewById(R.id.tv_dangerName);
        tv_spotMarks = (AutoCompleteTextView) findViewById(R.id.tv_spotMarks);
        btn_camaro = (Button) findViewById(R.id.btn_camaro);
        btn_save = (Button) findViewById(R.id.btn_save);
        gridView = (NoScrollGridView) findViewById(R.id.gridView);
        rdoGroup = (RadioGroup) findViewById(R.id.rdoGroup);
        rdoGroup_huilu = (RadioGroup) findViewById(R.id.rdoGroup_huilu);

        //判断是否显示回路信息
        ll_huilu =  (LinearLayout) findViewById(R.id.ll_huilu);
        if(this.dangerType == 2||this.dangerType == 3||this.dangerType == 4||this.dangerType == 6){
            ll_huilu.setVisibility(View.VISIBLE);
        }else{
            ll_huilu.setVisibility(View.GONE);
        }


        RelativeLayout rl_toolbar =(RelativeLayout) findViewById(R.id.include_toolbar_danger_pole_add);

        Toolbar toolbar = (Toolbar)rl_toolbar.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        TextView tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);
        tv_subtitle.setText(this.poleName);
        tv_title.setText(MyString.poleDangerTypes[this.dangerType]);
        toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.toolbar_navigation_back);
            //设置点击事件
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //清空图片临时文件夹
                    deleteFolderFile(MyString.image_temp_folder_path);
                    PoleDangerAddActivity.this.finish();
                }
            });
        }
    }

    private void initData() {
        //初始化图片数组
        imageUrls = new ArrayList<String>();

        noScrollGridAdapter = new NoScrollGridAdapter(this, imageUrls,MyString.image_temp_folder_path);
        gridView.setAdapter(noScrollGridAdapter);

        // 点击拍照事件处理函数
        btn_camaro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //调用系统拍照功能
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

        // 点击保存事件处理函数
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if(imageUrls.size()>3){
                    ToastUtil.show(PoleDangerAddActivity.this,"请拍摄至多3张照片！");
                    return;
                }

                // Reset errors.
                tv_dangerName.setError(null);
                // Store values at the time of the login attempt.
                String dangerName = tv_dangerName.getText().toString().trim();

                boolean cancel = false;
                View focusView = null;

                // Check for a valid password, if the user entered one.
                if (TextUtils.isEmpty(dangerName)) {
                    tv_dangerName.setError("隐患点描述不能为空！");
                    focusView = tv_dangerName;
                    focusView.requestFocus();
                    cancel = true;
                }

                if (dangerLevel == -1) {
                    ToastUtil.show(PoleDangerAddActivity.this, "请选择隐患点危险级别！");
                    cancel = true;
                }

                if(ll_huilu.getVisibility()==View.VISIBLE){
                    if(huiluType==-1){
                        ToastUtil.show(PoleDangerAddActivity.this, "请选择隐患点回路信息！");
                        cancel = true;
                    }
                }


                if (!cancel) {
                    //保存处理
                    boolean result = savePoleDangerData();

                    if (result) {
                        for (String temp_name : imageUrls) {
                            String temp_path = MyString.image_temp_folder_path + "/" + temp_name;
                            String img_path = MyString.image_pline_danger_folder_path + "/" + temp_name;
                            //将临时图片文件夹中的图片剪切到最终文件夹中
                            cutfileToNewFile(temp_path, img_path, true);
                        }

                        //删除图片临时文件夹
                        deleteFolderFile(MyString.image_temp_folder_path);

                        Toast.makeText(PoleDangerAddActivity.this, "保存数据成功！", Toast.LENGTH_SHORT).show();
                        //转换到前一个acivity
                        setResult(Activity.RESULT_OK);
                        PoleDangerAddActivity.this.finish();

                    } else {
                        Toast.makeText(PoleDangerAddActivity.this, "保存失败，确认标题不为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(PoleDangerAddActivity.this, "请将信息填写完整！", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

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


        rdoGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                //获取变更后的选中项的ID
                int selectID = arg0.getCheckedRadioButtonId();
                switch (selectID) {
                    case R.id.rdo_0:
                        dangerLevel = 0;
                        break;
                    case R.id.rdo_1:
                        dangerLevel = 1;
                        break;
                    case R.id.rdo_2:
                        dangerLevel = 2;
                        break;
                    case R.id.rdo_3:
                        dangerLevel = 3;
                        break;
                }
            }
        });

        rdoGroup_huilu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                //获取变更后的选中项的ID
                int selectID = arg0.getCheckedRadioButtonId();
                switch (selectID) {
                    case R.id.rdo_huilu_left:
                        huiluType = 0;
                        break;
                    case R.id.rdo_huilu_right:
                        huiluType = 2;
                        break;
                   default:
                       break;
                }
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
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_FOLDER,MyString.image_temp_folder_path);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
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
                File file=new File(MyString.image_temp_folder_path+"/"+imageUrls.get(position));
                //如果存在，则删除
                if(file.exists()) {
                    file.delete();
                }

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
     * 保存杆塔隐患点数据
     * @return
     */
    private boolean savePoleDangerData() {

        PoleDangerTableDataClass poleDangerTableDataClass = new PoleDangerTableDataClass();

        MySingleClass mySingleClass = MySingleClass.getInstance();
        String username = mySingleClass.getUser().getmName();

        String dangername = tv_dangerName.getText().toString().trim();
        String tv_spotmarks = tv_spotMarks.getText().toString().trim();

        poleDangerTableDataClass.setUserName(username);
        poleDangerTableDataClass.setDangerName(dangername);
        poleDangerTableDataClass.setSpotMark(tv_spotmarks);
        poleDangerTableDataClass.setDateTime(PublicStaticFunUtil.getCurrentDate());
        poleDangerTableDataClass.setPowerName(this.plineName);
        poleDangerTableDataClass.setPoleName(this.poleName);
        poleDangerTableDataClass.setHuiLuType(this.huiluType);
        poleDangerTableDataClass.setPoleObjectId(this.poleObjectId);
        poleDangerTableDataClass.setDangerLevel(this.dangerLevel);
        poleDangerTableDataClass.setDangerType(this.dangerType);
        poleDangerTableDataClass.setVersion(DangerVersion.DANGER_VERSION_ADD);
        poleDangerTableDataClass.setDangerMark("");
        poleDangerTableDataClass.setPicsJson(this.getPics());

        PoleDangerTableOpt poleDangerTableOpt = new PoleDangerTableOpt();
        int result = poleDangerTableOpt.insert(poleDangerTableDataClass);


        if (result != -1) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获得图片列表的字符串
     * @return
     */
    private String  getPics(){
        StringBuilder builder=new StringBuilder("");
        for(String str:imageUrls){
            builder.append(str+";");
        }
        return builder.toString();
    }


    /**
     * 删除指定目录下所有文件
     *
     */
    private void deleteFolderFile(String filePath) {
        try {
            if (!TextUtils.isEmpty(filePath)) {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        if (!files[i].isDirectory()) {// 如果是文件，删除
                            files[i].delete();
                        }
                    }
                }
            }
        }catch (Exception e){
            ToastUtil.show(this,"删除临时图片文件失败！");
        }
    }


}




