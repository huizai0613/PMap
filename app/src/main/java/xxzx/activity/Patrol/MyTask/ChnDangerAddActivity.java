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
import xxzx.publicClass.IntentFlag;
import xxzx.publicClass.IntentPublicMapState;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.PublicStaticFunUtil;
import xxzx.publicClass.ToastUtil;
import xxzx.spatialite.TableOptClass.ChannelDangerTableOpt;
import xxzx.spatialite.TableOptClass.DangerSgTableOpt;
import xxzx.spatialite.TableStruct.ChannelDangerTableDataClass;
import xxzx.spatialite.TableStruct.DangerSgTableDataClass;

public class ChnDangerAddActivity extends AppCompatActivity {

    private static final String LOG_TAG = ChnDangerAddActivity.class.getSimpleName();

    private Button btn_camaro;
    private Button btn_save;
    private Button btn_sgDetail;
    private AutoCompleteTextView tv_dangerName;
    private AutoCompleteTextView tv_spotMarks;

    private RadioGroup rdoGroup;

    private NoScrollGridView gridView = null;
    //记录SD卡中图片文件列表，用于图片删除等
    //private ArrayList<String> imageUrls_init = null;
    //记录当前的图片路径文件列表,用于实时显示
    private ArrayList<String> imageUrls = null;
    private NoScrollGridAdapter noScrollGridAdapter = null;


    private String chnName;
    private int chnObjectId;
    private String plineName;
    //隐患点类型
    private int dangerType;
    //隐患点级别
    private int dangerLevel=-1;

    //隐患点位置信息
    private String geometryWKT="";

    //施工隐患详细信息
    private DangerSgTableDataClass dangerSgTableDataClass = null;
    //通道隐患处理类
    private ChannelDangerTableOpt chnDangerTableOpt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_pole_add);

        Intent intent = getIntent();
        this.chnName = intent.getStringExtra("chnname");
        this.chnObjectId = intent.getIntExtra("chnobjectid",-1);
        this.plineName = intent.getStringExtra("plinename");
        this.dangerType = intent.getIntExtra("dangertype", -1);
        this.initView();
        this.initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_toolbar_patrol_location, menu);
        return true;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                //删除图片临时文件夹
                this.deleteFolderFile(MyString.image_temp_folder_path);
                this.finish();
                break;
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

        if (requestCode == MyString.requestCode_activity_to_publicmapactivity && resultCode == RESULT_OK) {
            Bundle b = data.getExtras();  //data为B中回传的Intent
            if (b != null && b.containsKey("geometry")) {
                this.geometryWKT = (String) b.get("geometry");//poiItem
            }
        }

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
            ImageCompress.compressImage(MyString.image_temp_path, fileName);

            imageUrls.add(name);

            noScrollGridAdapter.notifyDataSetChanged();
        }

        if (requestCode == MyString.requestCode_activity_to_dangersgdetailactivity && resultCode == Activity.RESULT_OK) {
            this.dangerSgTableDataClass = (DangerSgTableDataClass)data.getSerializableExtra(IntentFlag.INTENT_FLAG_DANGER_SG_INFO);  //data为B中回传的Intent
        }
    }

    private void initView() {
        tv_dangerName = (AutoCompleteTextView) findViewById(R.id.tv_dangerName);
        tv_spotMarks = (AutoCompleteTextView) findViewById(R.id.tv_spotMarks);
        btn_camaro = (Button) findViewById(R.id.btn_camaro);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_sgDetail = (Button) findViewById(R.id.btn_sgDetail);
        //如果施工隐患，按钮可见
        if(this.dangerType==MyString.DANGER_SG_TYPE){
            btn_sgDetail.setVisibility(View.VISIBLE);//按钮可见
        }

        gridView = (NoScrollGridView) findViewById(R.id.gridView);
        rdoGroup = (RadioGroup) findViewById(R.id.rdoGroup);


        RelativeLayout rl_toolbar =(RelativeLayout) findViewById(R.id.include_toolbar_danger_pole_add);

        Toolbar toolbar = (Toolbar) rl_toolbar.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        TextView tv_title = (TextView) toolbar.findViewById(R.id.tv_title);
        TextView tv_subtitle = (TextView) toolbar.findViewById(R.id.tv_subtitle);
        tv_subtitle.setText(this.chnName);
        tv_title.setText(MyString.channelDangerTypes[this.dangerType]);

        Button btn_right = (Button) toolbar.findViewById(R.id.btn_right);
        btn_right.setText("位置");
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentPublicMapState.Intent(ChnDangerAddActivity.this,MyString.requestCode_activity_to_publicmapactivity, MyString.intent_map_state_selectgeometry, geometryWKT);
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
                    //清空图片临时文件夹
                    deleteFolderFile(MyString.image_temp_folder_path);
                    ChnDangerAddActivity.this.finish();
                }
            });
        }
    }

    private void initData() {
        this.dangerSgTableDataClass = new DangerSgTableDataClass();
        //初始化图片数组
        //imageUrls_init = new ArrayList<String>();
        this.imageUrls = new ArrayList<String>();
        this.chnDangerTableOpt = new ChannelDangerTableOpt();
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
                // Reset errors.
                tv_dangerName.setError(null);
                // Store values at the time of the login attempt.
                String dangerName = tv_dangerName.getText().toString().trim();

                View focusView = null;

                if (imageUrls.size() > 3) {
                    ToastUtil.show(ChnDangerAddActivity.this, "请拍摄至多3张照片！");
                    return;
                }

                if (TextUtils.isEmpty(dangerName)) {
                    tv_dangerName.setError("隐患点描述不能为空！");
                    Toast.makeText(ChnDangerAddActivity.this, "请将信息填写完整！", Toast.LENGTH_SHORT).show();
                    focusView = tv_dangerName;
                    focusView.requestFocus();
                    return;
                }

                if (dangerLevel == -1) {
                    ToastUtil.show(ChnDangerAddActivity.this, "请选择隐患点危险级别！");
                    return;
                }

                if (TextUtils.isEmpty(geometryWKT)) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ChnDangerAddActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("当前没有隐患点位置信息,是否确定保存？");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //如果是施工隐患
                            if(dangerType== MyString.DANGER_SG_TYPE){
                                if(dangerSgTableDataClass==null){
                                    ToastUtil.show(ChnDangerAddActivity.this,"施工详细信息不完整，请填写完整后再保存！");
                                    return;
                                }
                            }
                            saveDangerData();
                        }
                    });

                    builder.create().show();
                    return;
                }

                //保存处理
                saveDangerData();
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

        btn_sgDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChnDangerAddActivity.this, DangerSgDetailActivity.class);
                intent.putExtra(IntentFlag.INTENT_FLAG_DANGER_SG_INFO, dangerSgTableDataClass);
                startActivityForResult(intent, MyString.requestCode_activity_to_dangersgdetailactivity);
            }
        });
    }



    private void saveDangerData(){
        //创建事务
        this.chnDangerTableOpt.BEGIN();
        //保存处理
        int rowid = saveChannelDangerData();

        if(rowid != -1) {
            boolean result2 = true;
            //如果是施工隐患
            if (this.dangerType == MyString.DANGER_SG_TYPE) {
                result2 = this.saveDangerDtailData(rowid);
            }

            if (result2) {
                //提交事务
                this.chnDangerTableOpt.COMMIT();

                for (String temp_name : imageUrls) {
                    String temp_path = MyString.image_temp_folder_path + "/" + temp_name;
                    String img_path = MyString.image_pline_danger_folder_path + "/" + temp_name;
                    //将临时图片文件夹中的图片剪切到最终文件夹中
                    cutfileToNewFile(temp_path, img_path, true);
                }

                //删除图片临时文件夹
                deleteFolderFile(MyString.image_temp_folder_path);

                Toast.makeText(ChnDangerAddActivity.this, "保存数据成功！", Toast.LENGTH_SHORT).show();
                //转换到前一个acivity
                setResult(Activity.RESULT_OK);
                ChnDangerAddActivity.this.finish();
            } else {
                //回滚事务
                this.chnDangerTableOpt.ROLLBACK();
                Toast.makeText(ChnDangerAddActivity.this, "隐患详细表保存失败，请确认填写是否正确！", Toast.LENGTH_SHORT).show();
            }
        }else{
            //回滚事务
            this.chnDangerTableOpt.ROLLBACK();
            Toast.makeText(ChnDangerAddActivity.this, "隐患数据保存失败，请确认填写是否正确！", Toast.LENGTH_SHORT).show();
        }
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
     * 保存隐患点数据
     * @return
     */
    private int saveChannelDangerData() {

        ChannelDangerTableDataClass channelDangerTableDataClass = new ChannelDangerTableDataClass();

        MySingleClass mySingleClass = MySingleClass.getInstance();
        String username = mySingleClass.getUser().getmName();

        String dangername = tv_dangerName.getText().toString().trim();
        String tv_spotmarks = tv_spotMarks.getText().toString().trim();
        channelDangerTableDataClass.setUserName(username);
        channelDangerTableDataClass.setDangerName(dangername);
        channelDangerTableDataClass.setSpotMark(tv_spotmarks);


        channelDangerTableDataClass.setDateTime(PublicStaticFunUtil.getCurrentDate());
        channelDangerTableDataClass.setPowerName(this.plineName);
        channelDangerTableDataClass.setChannelName(this.chnName);
        channelDangerTableDataClass.setChannelObjectId(this.chnObjectId);
        channelDangerTableDataClass.setDangerLevel(this.dangerLevel);
        channelDangerTableDataClass.setDangerType(this.dangerType);
        channelDangerTableDataClass.setVersion(DangerVersion.DANGER_VERSION_ADD);
        channelDangerTableDataClass.setDangerMark("");
        channelDangerTableDataClass.setPicsJson(this.getPics());
        channelDangerTableDataClass.setGeometry(this.geometryWKT);

        int rowid = chnDangerTableOpt.insert(channelDangerTableDataClass);

        return rowid;
    }


    /**
     * 保存隐患详细表数据
     * @return
     */
    private boolean saveDangerDtailData(int dangerRowid) {
        DangerSgTableOpt dangerSgTableOpt = new DangerSgTableOpt();
        this.dangerSgTableDataClass.setDangerRowId(dangerRowid);
        int result = dangerSgTableOpt.insert(this.dangerSgTableDataClass);

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




