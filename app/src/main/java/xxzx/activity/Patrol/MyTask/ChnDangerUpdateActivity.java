package xxzx.activity.Patrol.MyTask;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import xxzx.ImageView.NoScrollGridAdapter;
import xxzx.ImageView.NoScrollGridView;
import xxzx.activity.ImagePagerActivity;
import xxzx.activity.R;
import xxzx.mylibrary.PromotedActionsLibrary;
import xxzx.publicClass.DangerVersion;
import xxzx.publicClass.MyFile;
import xxzx.publicClass.MyImgOpt.ImageCompress;
import xxzx.publicClass.IntentFlag;
import xxzx.publicClass.IntentPublicMapState;
import xxzx.publicClass.LoadingDialog;
import xxzx.publicClass.MyImgOpt.MyHttpJPG;
import xxzx.publicClass.MyImgOpt.MyImageOptClass;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.PublicStaticFunUtil;
import xxzx.publicClass.ToastUtil;
import xxzx.share.qq.ShareToQQFriend;
import xxzx.share.wx.ShareToWeixinFriend;
import xxzx.share.wx.WxShareClass;
import xxzx.spatialite.TableOptClass.ChannelDangerTableOpt;
import xxzx.spatialite.TableOptClass.DangerSgTableOpt;
import xxzx.spatialite.TableOptClass.PoleDangerTableOpt;
import xxzx.spatialite.TableStruct.ChannelDangerTableDataClass;
import xxzx.spatialite.TableStruct.DangerSgTableColumn;
import xxzx.spatialite.TableStruct.DangerSgTableDataClass;
import xxzx.spatialite.TableStruct.PoleDangerTableDataClass;

public class ChnDangerUpdateActivity extends AppCompatActivity {

    private static final String LOG_TAG = ChnDangerUpdateActivity.class.getSimpleName();

    private final int request_fail = 0;//请求失败
    private final int request_jpg_success = 1;//图片请求成功

    private LoadingDialog loadingDialog;

    private Button btn_camaro;
    private Button btn_solve;
    private Button btn_update;
    private Button btn_sgDetail;
    private AutoCompleteTextView tv_dangerName;
    private AutoCompleteTextView tv_spotMarks;

    private TextView tv_time;
    private TextView tv_username;
    private RadioGroup rdoGroup;

    private String geometryWKT;

    private NoScrollGridView gridView1 = null;
    private NoScrollGridView gridView2 = null;


    private ArrayList<String> imageUrls1 = null;
    //记录当前的图片路径文件列表,用于实时显示
    private ArrayList<String> imageUrls2 = null;

    private NoScrollGridAdapter noScrollGridAdapter1 = null;
    private NoScrollGridAdapter noScrollGridAdapter2 = null;


    private String chnName;
    private int chnObjectId;
    private String plineName;
    private int dangerType;
    private int dangerLevel=-1;
    private int rowID=-1;
    private int keyID=-1;

    private DangerSgTableDataClass dangerSgTableDataClass = null;
    private ChannelDangerTableOpt chnDangerTableOpt = null;
    private DangerSgTableOpt dangerSgTableOpt = null;

    //微信分享类
    private WxShareClass wxShareClass;

    private PromotedActionsLibrary promotedActionsLibrary = null;

    private String btn_share_qq="btn_share_qq";
    private String btn_share_wx="btn_share_wx";

    private static final int share_type_wx = 0;
    private static final int share_type_qq = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_pole_update);

        Intent intent = getIntent();
        this.chnName = intent.getStringExtra("chnname");
        this.chnObjectId = intent.getIntExtra("chnobjectid",-1);
        this.plineName = intent.getStringExtra("plinename");
        this.rowID = intent.getIntExtra("rowid", -1);
        this.dangerType = intent.getIntExtra("dangertype", -1);
        this.initView();
        this.initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_toolbar_patrol_danger_update_channel, menu);
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
                MyFile.deleteFolder(MyString.image_temp_folder_path);
                this.finish();
            case R.id.action_history:
                //this.finish();
            case R.id.action_solver:
                //this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        MyFile.deleteFolder(MyString.image_temp_folder_path);
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

        if(requestCode == MyString.requestCode_activity_to_publicmapactivity&&resultCode==RESULT_OK){
            Bundle b = data.getExtras();  //data为B中回传的Intent
            if(b!=null&&b.containsKey("geometry")){
                this.geometryWKT = (String) b.get("geometry");//poiItem
            }
        }


        if (requestCode == MyString.requestCode_activity_to_photoactivity && resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();

            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile", "SD card is not avaiable/writeable right now.");
                return;
            }
            String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
            String fileName = MyString.image_temp_folder_path + "/" + name;
            //this.cutfileToNewFile(tempImg, fileName, true);
            //图片压缩
            ImageCompress.compressImage(MyString.image_temp_path, fileName);

            imageUrls2.add(name);
            noScrollGridAdapter2.notifyDataSetChanged();
        }

        if (requestCode == MyString.requestCode_activity_to_dangersgdetailactivity && resultCode == Activity.RESULT_OK) {
            this.dangerSgTableDataClass = (DangerSgTableDataClass)data.getSerializableExtra(IntentFlag.INTENT_FLAG_DANGER_SG_INFO);  //data为B中回传的Intent
        }
    }

    private void initView() {

        loadingDialog=new LoadingDialog(this);

        tv_dangerName = (AutoCompleteTextView) findViewById(R.id.tv_dangerName);
        tv_spotMarks = (AutoCompleteTextView) findViewById(R.id.tv_spotMarks);

        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_time= (TextView) findViewById(R.id.tv_time);

        btn_camaro = (Button) findViewById(R.id.btn_camaro);
        btn_sgDetail = (Button) findViewById(R.id.btn_sgDetail);
        btn_update= (Button) findViewById(R.id.btn_update);
        btn_solve = (Button) findViewById(R.id.btn_solve);

        //如果施工隐患，按钮可见
        if(this.dangerType==MyString.DANGER_SG_TYPE){
            btn_sgDetail.setVisibility(View.VISIBLE);//按钮可见
        }

        gridView1 = (NoScrollGridView) findViewById(R.id.gridView1);
        gridView2 = (NoScrollGridView) findViewById(R.id.gridView2);
        rdoGroup = (RadioGroup) findViewById(R.id.rdoGroup);

        RelativeLayout rl_toolbar =(RelativeLayout) findViewById(R.id.include_toolbar_danger_pole_update);

        Toolbar toolbar = (Toolbar) rl_toolbar.findViewById(R.id.toolbar);

        toolbar.setTitle("");
        TextView tv_title = (TextView) toolbar.findViewById(R.id.tv_title);
        TextView tv_subtitle = (TextView) toolbar.findViewById(R.id.tv_subtitle);
        tv_title.setText("巡视信息更新");
        tv_subtitle.setText(MyString.channelDangerTypes[this.dangerType]);

        Button btn_right = (Button) toolbar.findViewById(R.id.btn_right);
        btn_right.setText("位置");
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentPublicMapState.Intent(ChnDangerUpdateActivity.this, MyString.requestCode_activity_to_publicmapactivity, MyString.intent_map_state_selectgeometry, geometryWKT);
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
                    MyFile.deleteFolder(MyString.image_temp_folder_path);
                    ChnDangerUpdateActivity.this.finish();
                }
            });
        }

        //显示微信和QQ分享
        //this.initFloatButtonView();
    }


    /**
     * 初始化FloatButton按钮
     * @param
     */
    private void initFloatButtonView(){
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.container);
        this.promotedActionsLibrary = new PromotedActionsLibrary(null);
        this.promotedActionsLibrary.setup(this, frameLayout);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ChannelDangerTableDataClass chnDangerTableDataClass = (ChannelDangerTableDataClass) chnDangerTableOpt.getRow(rowID);


                StringBuilder builder = new StringBuilder();

                builder.append("隐患描述:" + chnDangerTableDataClass.getDangerName() + "\n");
                builder.append("巡视情况:" + chnDangerTableDataClass.getSpotMark() + "\n");
                builder.append("危险级别:" + MyString.dangerLevels[chnDangerTableDataClass.getDangerLevel()] + "\n");
                builder.append("隐患类别:" + MyString.channelDangerTypes_nonum[chnDangerTableDataClass.getDangerType()] + "\n");
                builder.append("巡视时间:" + chnDangerTableDataClass.getDateTime() + "\n");
                builder.append("线路名称:" + chnDangerTableDataClass.getPowerName() + "\n");
                builder.append("通道名称:" + chnDangerTableDataClass.getChannelName() + "\n");
                builder.append("通道号:" + chnDangerTableDataClass.getChannelObjectId() + "\n");
                builder.append("回路:" + chnDangerTableDataClass.getHuiLuType() + "\n");
                builder.append("巡视人员:" + chnDangerTableDataClass.getUserName() + "\n");


                //如果是施工隐患
                if(chnDangerTableDataClass.getDangerType()==0){
                    DangerSgTableDataClass dangerSgTableDataClass =(DangerSgTableDataClass) dangerSgTableOpt.getRow(chnDangerTableDataClass.getRowId());

                    if(dangerSgTableDataClass!=null) {

                        builder.append("\n");
                        builder.append("施工隐患信息\n");
                        builder.append("位置描述:" + dangerSgTableDataClass.getLocation() + "\n");
                        builder.append("安全告知书下达:" + dangerSgTableDataClass.getGuank1() + "\n");
                        builder.append("安全协议签订:" + dangerSgTableDataClass.getGuank2() + "\n");
                        builder.append("行政许可办理:" + dangerSgTableDataClass.getGuank3() + "\n");
                        builder.append("警示、限高措施:" + dangerSgTableDataClass.getGuank4() + "\n");
                        builder.append("视频安装:" + dangerSgTableDataClass.getGuank5() + "\n");
                        builder.append("看守人员:" + dangerSgTableDataClass.getKeeper() + "\n");
                        builder.append("看守人员联系方式:" + dangerSgTableDataClass.getKeeperphone() + "\n");
                        builder.append("施工单位联系人:" + dangerSgTableDataClass.getSgcontact() + "\n");
                        builder.append("施工单位:" + dangerSgTableDataClass.getSgdepartment() + "\n");
                        builder.append("施工单位联系方式:" + dangerSgTableDataClass.getSgphone() + "\n");
                        builder.append("运维单位:" + dangerSgTableDataClass.getYwcontact() + "\n");
                        builder.append("运维单位联系人:" + dangerSgTableDataClass.getYwphone());
                    }
                }


                String picJson = chnDangerTableDataClass.getPicsJson();

                //分享隐患信息微信
                if (String.valueOf(view.getTag()).equals(btn_share_wx)) {

                    if (!TextUtils.isEmpty(picJson)) {
                        String[] paths = picJson.split(";");

                        if (paths.length > 0) {

                            ArrayList<Uri> list = new ArrayList<Uri>();

                            for (String s:paths){
                                File file = new File(MyString.image_pline_danger_folder_path  +"/" +  s);
                                Uri uri = Uri.fromFile(file);
                                list.add(uri);
                            }

                            showSareDialog(share_type_wx,builder.toString(),list);

                        } else {
                            //分享文字
                            wxShareClass.shareText(builder.toString(), 0);
                        }
                    }else{
                        //分享文字
                        wxShareClass.shareText(builder.toString(), 0);
                    }
                }
                //分享隐患信息qq
                if (String.valueOf(view.getTag()).equals(btn_share_qq)) {

                    if (!TextUtils.isEmpty(picJson)) {
                        String[] paths = picJson.split(";");

                        if (paths.length > 0) {

                            ArrayList<Uri> list = new ArrayList<Uri>();

                            for (String s:paths){
                                File file = new File(MyString.image_pline_danger_folder_path +"/" + s);
                                Uri uri = Uri.fromFile(file);
                                list.add(uri);
                            }

                            showSareDialog(share_type_qq,builder.toString(),list);

                        } else {
                            //分享文字
                            ShareToQQFriend.shareText(ChnDangerUpdateActivity.this, builder.toString());
                        }
                    }else{
                        //分享文字
                        ShareToQQFriend.shareText(ChnDangerUpdateActivity.this, builder.toString());
                    }

                }
            }
        };

        this.promotedActionsLibrary.addItem(getResources().getDrawable(R.mipmap.fab_share_qq),btn_share_qq, onClickListener);
        this.promotedActionsLibrary.addItem(getResources().getDrawable(R.mipmap.fab_share_wx),btn_share_wx, onClickListener);
        this.promotedActionsLibrary.addMainItem(getResources().getDrawable(R.mipmap.fab_share));

    }


    /**
     *
     * @param shareType 1表示微信，2表示qq
     * @param msg
     * @param imgpaths
     */
    private void showSareDialog(final int shareType, final String msg, final ArrayList<Uri> imgpaths){

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("选择");
        builder.setItems(new String[]{"分享文字", "分享图片"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                switch (which) {
                    case 0:
                        switch(shareType){

                            case share_type_wx:
                                //微信分享文字
                                wxShareClass.shareText(msg, 0);
                                break;
                            case share_type_qq:
                                //qq分享文字
                                ShareToQQFriend.shareText(ChnDangerUpdateActivity.this, msg);
                                break;
                        }
                        break;
                    case 1:
                        switch(shareType){

                            case share_type_wx:
                                //分享图片
                                ShareToWeixinFriend.shareImgs(ChnDangerUpdateActivity.this,imgpaths);
                                break;
                            case share_type_qq:
                                //qq分享图片
                                ShareToQQFriend.share(ChnDangerUpdateActivity.this, msg,imgpaths);
                                break;
                        }

                        break;
                }
            }
        });
        builder.show();
    }




    private void initData() {

        this.wxShareClass = new WxShareClass(this);

        this.chnDangerTableOpt = new ChannelDangerTableOpt();
        this.dangerSgTableOpt = new DangerSgTableOpt();
        //初始化图片数组
        this.imageUrls1 = new ArrayList<String>();
        this.imageUrls2 = new ArrayList<String>();

        this.getDangerData();

        if(this.keyID==-1){
            //如果本地数据的数据，效应归档按钮不可见
            btn_solve.setVisibility(View.GONE);
        }else{
            //如果是从服务器上下载的数据，隐患点描述不能更改
            //tv_dangerName.setEnabled(false);
        }

        noScrollGridAdapter1 = new NoScrollGridAdapter(this, imageUrls1, MyString.image_pline_danger_folder_path);
        gridView1.setAdapter(noScrollGridAdapter1);

        noScrollGridAdapter2 = new NoScrollGridAdapter(this, imageUrls2,MyString.image_temp_folder_path);
        gridView2.setAdapter(noScrollGridAdapter2);

        this.initListener();

    }


    private void initListener(){
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

        btn_sgDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChnDangerUpdateActivity.this, DangerSgDetailActivity.class);
                intent.putExtra(IntentFlag.INTENT_FLAG_DANGER_SG_INFO,dangerSgTableDataClass);
                startActivityForResult(intent, MyString.requestCode_activity_to_dangersgdetailactivity);
            }
        });

        //点击更新
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUrls2.size() > 3) {
                    ToastUtil.show(ChnDangerUpdateActivity.this, "请拍摄至多3张照片!");
                    return;
                }
                updateData(DangerVersion.DANGER_VERSION_SOLVE_UN);
            }
        });

        // 点击消缺事件处理函数
        btn_solve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if(imageUrls2.size()>3){
                    ToastUtil.show(ChnDangerUpdateActivity.this,"请拍摄至多3张照片！");
                    return;
                }

                AlertDialog.Builder build = new AlertDialog.Builder(ChnDangerUpdateActivity.this);
                build.setTitle("注意").setMessage("确定消缺归档该隐患信息吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO
                                try {
                                    updateData(DangerVersion.DANGER_VERSION_SOLVE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //如果点击的是取消，不做任何处理
                        dialog.dismiss();
                    }
                }).show();




            }
        });

        // 单击回帖九宫格，查看大图
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                imageBrower(position, imageUrls1, MyString.image_pline_danger_folder_path);
            }
        });

        // 单击回帖九宫格，查看大图
        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                imageBrower(position, imageUrls2, MyString.image_temp_folder_path);
            }
        });

        //长按九宫格，提示是否删除图片
        gridView2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

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
    }


    private void updateData(int dangerversion){
        Boolean ok = checkInfoView();
        if (ok) {
            this.chnDangerTableOpt.BEGIN();
            //保存处理
            boolean result = this.updateChannelDangerData(dangerversion);

            if (result) {
                //如果是施工隐患
                if (this.dangerType == MyString.DANGER_SG_TYPE) {
                    result = this.updateDangerDtail();
                }
                if (result) {
                    //提交事务
                    this.chnDangerTableOpt.COMMIT();

                    if (imageUrls2.size() > 0) {
                        //剪切临时文件夹图片
                        cutTempFolderImg();
                        //删除图片
                        deleteImgs(imageUrls1, MyString.image_pline_danger_folder_path);
                    }
                    Toast.makeText(ChnDangerUpdateActivity.this, "数据更新成功！", Toast.LENGTH_SHORT).show();
                    //转换到前一个acivity
                    setResult(Activity.RESULT_OK);
                    ChnDangerUpdateActivity.this.finish();
                }else {
                    this.chnDangerTableOpt.ROLLBACK();
                    Toast.makeText(ChnDangerUpdateActivity.this, "数据更新失败！", Toast.LENGTH_SHORT).show();
                }

            } else {
                this.chnDangerTableOpt.ROLLBACK();
                Toast.makeText(ChnDangerUpdateActivity.this, "数据更新失败！", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    /**
     * 确认填写否正确
     * @return
     */
    private boolean checkInfoView(){
        // Reset errors.
        tv_dangerName.setError(null);
        // Store values at the time of the login attempt.
        String dangerName = tv_dangerName.getText().toString().trim();

        boolean ok = true;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(dangerName)) {
            tv_dangerName.setError("隐患点描述不能为空！");
            focusView = tv_dangerName;
            focusView.requestFocus();
            ok = false;
        }

        if (dangerLevel == -1) {
            ToastUtil.show(ChnDangerUpdateActivity.this, "请选择隐患点危险级别！");
            ok = false;
        }

        return ok;
    }


    /**
     * 将临时文件夹中的图片剪切到最终的文件夹中
     */
    private void cutTempFolderImg(){
        for (String temp_name : imageUrls2) {
            String temp_path = MyString.image_temp_folder_path + "/" + temp_name;
            String img_path = MyString.image_pline_danger_folder_path + "/" + temp_name;
            //将临时图片文件夹中的图片剪切到最终文件夹中
            //cutfileToNewFile(temp_path, img_path, true);

            MyFile.copyFile(temp_path,img_path);

        }
        //删除图片临时文件夹中的所有文件
        MyFile.deleteFolder(MyString.image_temp_folder_path);
    }


    /**
     * 打开图片查看器
     *
     * @param position
     * @param urls2
     */
    protected void imageBrower(int position, ArrayList<String> urls2,String imgFolderPath) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        // 图片url
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_FOLDER, imgFolderPath);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }


//    /**
//     * @param fromFilePath 被复制的文件
//     * @param toFilePath   复制的目录文件
//     * @param rewrite      是否重新创建文件
//     *                     <p/>
//     *                     <p>文件的复制操作方法
//     */
//    private void cutfileToNewFile(String fromFilePath, String toFilePath, Boolean rewrite) {
//
//        File fromFile = new File(fromFilePath);
//        File toFile = new File(toFilePath);
//
//        if (!fromFile.exists()) {
//            return;
//        }
//
//        if (!fromFile.isFile()) {
//            return;
//        }
//        if (!fromFile.canRead()) {
//            return;
//        }
//        if (!toFile.getParentFile().exists()) {
//            toFile.getParentFile().mkdirs();
//        }
//        if (toFile.exists() && rewrite) {
//            toFile.delete();
//        }
//        try {
//            FileInputStream fosfrom = new FileInputStream(fromFile);
//            FileOutputStream fosto = new FileOutputStream(toFile);
//
//            byte[] bt = new byte[1024];
//            int c;
//            while ((c = fosfrom.read(bt)) > 0) {
//                fosto.write(bt, 0, c);
//            }
//            //关闭输入、输出流
//            fosfrom.close();
//            fosto.close();
//
//            fromFile.delete();
//
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }


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
                File file=new File(MyString.image_temp_folder_path+"/"+imageUrls2.get(position));
                //如果存在，则删除
                if(file.exists()) {
                    file.delete();
                }

                //删除图片列表中的文件路径
                imageUrls2.remove(position);
                noScrollGridAdapter2.notifyDataSetChanged();
                dialog.dismiss();

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
     * 初始化数据，通过数据库查询将原先的数据加载进来
     */
    private void getDangerData(){
        //获取通道隐患
        ChannelDangerTableDataClass channelDangerTableDataClass = (ChannelDangerTableDataClass)this.chnDangerTableOpt.getRow(this.rowID);
        //如果是施工隐患，获取施工隐患详细信息
        if(this.dangerType == MyString.DANGER_SG_TYPE) {
            this.dangerSgTableDataClass = this.dangerSgTableOpt.getRowFormDangerRowID(this.rowID);
        }
        this.tv_dangerName.setText(channelDangerTableDataClass.getDangerName());
        this.tv_spotMarks.setText(channelDangerTableDataClass.getSpotMark());

        this.tv_time.setText(channelDangerTableDataClass.getDateTime());
        this.tv_username.setText(channelDangerTableDataClass.getUserName());
        this.geometryWKT=channelDangerTableDataClass.getGeometry();
        this.dangerLevel=channelDangerTableDataClass.getDangerLevel();
        this.keyID=channelDangerTableDataClass.getKeyID();

        //设置隐患点显示级别
        int dangerlevel = channelDangerTableDataClass.getDangerLevel();
        switch (dangerlevel){
            case 0:
                rdoGroup.check(R.id.rdo_0);
                break;
            case 1:
                rdoGroup.check(R.id.rdo_1);
                break;
            case 2:
                rdoGroup.check(R.id.rdo_2);
                break;
            case 3:
                rdoGroup.check(R.id.rdo_3);
                break;
        }

        String picJson = channelDangerTableDataClass.getPicsJson();
        //如果keyid不为-1，表示服务器的数据 请求网络资源
        if(TextUtils.isEmpty(picJson)&&keyID!=-1){

            for(int i=0;i<channelDangerTableDataClass.getImgNum();i++) {
                MyRunnable myRunnable = new MyRunnable();
                myRunnable.setData(this.chnObjectId, this.keyID, this.chnName + MyString.channelDangerTypes_nonum[this.dangerType]+i+".jpg");
                new Thread(myRunnable).start();
            }

        }else {
            //设置图片信息
            String[] imgs = channelDangerTableDataClass.getPicsJson().split(";");
            for (String str : imgs) {
                if (!TextUtils.isEmpty(str)) {
                    imageUrls1.add(str);
                }
            }
        }
    }

    /**
     * 保存杆塔隐患点数据
     * @return
     */
    private boolean updateChannelDangerData(int dangerVersion) {

        ChannelDangerTableDataClass channelDangerTableDataClass = new ChannelDangerTableDataClass();

        MySingleClass mySingleClass = MySingleClass.getInstance();
        String username = mySingleClass.getUser().getmName();

        String dangername = tv_dangerName.getText().toString().trim();
        String spotmarks = tv_spotMarks.getText().toString().trim();

        channelDangerTableDataClass.setUserName(username);
        channelDangerTableDataClass.setDangerName(dangername);
        channelDangerTableDataClass.setSpotMark(spotmarks);

        channelDangerTableDataClass.setDateTime(PublicStaticFunUtil.getCurrentDate());
        channelDangerTableDataClass.setChannelName(this.chnName);
        channelDangerTableDataClass.setChannelObjectId(this.chnObjectId);
        channelDangerTableDataClass.setDangerLevel(this.dangerLevel);
        channelDangerTableDataClass.setPowerName(this.plineName);
        channelDangerTableDataClass.setDangerType(this.dangerType);
        channelDangerTableDataClass.setVersion(dangerVersion);
        channelDangerTableDataClass.setDangerMark("");
        channelDangerTableDataClass.setPicsJson(this.getPics());
        channelDangerTableDataClass.setRowId(this.rowID);
        channelDangerTableDataClass.setGeometry(this.geometryWKT);

        int result = this.chnDangerTableOpt.update(channelDangerTableDataClass);

        if (result != -1) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 保存杆塔隐患点数据
     * @return
     */
    private boolean updateDangerDtail() {
        int result = this.dangerSgTableOpt.update(this.dangerSgTableDataClass);
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

        //如果没有新拍的图片，采用老图片
        if(imageUrls2.size()==0){
            for(String str:imageUrls1){
                builder.append(str+";");
            }
        }else{
            for(String str:imageUrls2){
                builder.append(str+";");
            }
        }

        return builder.toString();
    }


//    /**
//     * 删除指定目录下所有文件
//     *
//     */
//    public void deleteFolderFile(String filePath) {
//        try {
//            if (!TextUtils.isEmpty(filePath)) {
//                File file = new File(filePath);
//                if (file.isDirectory()) {// 处理目录
//                    File files[] = file.listFiles();
//                    for (int i = 0; i < files.length; i++) {
//                        if (!files[i].isDirectory()) {// 如果是文件，删除
//                            files[i].delete();
//                        }
//                    }
//                }
//            }
//        }catch (Exception e){
//            ToastUtil.show(this,"删除临时图片文件失败！");
//        }
//    }


    /**
     * 删除图片
     * @param imgNames
     */
    private void deleteImgs(List<String> imgNames,String imgFolderPath){
        for(String name:imgNames) {
            File file = new File(imgFolderPath + "/" + name);
            //如果存在，则删除
            if (file.exists()) {
                file.delete();
            }
        }
        //删除图片列表中的文件路径
        imgNames.clear();
    }



    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            loadingDialog.dismiss();
            switch (msg.what) {
                case request_fail:
                    ToastUtil.show(ChnDangerUpdateActivity.this, msg.obj.toString());
                    break;
                case request_jpg_success:
                    //更新图片显示
                    noScrollGridAdapter1.notifyDataSetChanged();

                    ChannelDangerTableDataClass channelDangerTableDataClass=new ChannelDangerTableDataClass();
                    channelDangerTableDataClass.setRowId(rowID);

                    StringBuilder builder=new StringBuilder("");
                    for(String str:imageUrls1){
                        builder.append(str+";");
                    }
                    channelDangerTableDataClass.setPicsJson(builder.toString());

                    //更新数据库中的图片存储
                    ChannelDangerTableOpt channelDangerTableOpt=new ChannelDangerTableOpt();
                    channelDangerTableOpt.updateDangerPicsJson(channelDangerTableDataClass);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public class MyRunnable implements Runnable {
        private int chnObectId;
        private String jpgName;
        private int keyId;


        public void setData(int chnobjectid,int keyid, String jpg) {
            this.chnObectId = chnobjectid;
            this.jpgName = jpg;
            this.keyId = keyid;
        }
        @Override
        public void run() {
            // TODO Auto-generated method stub
            MySingleClass mySingleClass = MySingleClass.getInstance();
            Properties properties = mySingleClass.getProperties();

            Message message = new Message();
            try {
                String url = properties.get("url_channel_danger_jpg").toString();
                String httpPath = String.format(url, String.valueOf(this.chnObectId),String.valueOf(this.keyId),URLEncoder.encode(jpgName, "UTF-8"));
                //******** 方法2：取得的是InputStream，直接从InputStream生成bitmap ***********/
                InputStream inputStream = MyHttpJPG.getImageStream(httpPath);
                if (inputStream == null) {
                    message.what = request_fail;
                    message.obj = "不能下载图片,请确定图片是否存在以及确定网络连接是否正常！";
                } else {
                    Bitmap mBitmap = BitmapFactory.decodeStream(inputStream);
                    //********************************************************************/
                    String imgName = String.valueOf(this.chnObectId) + "_"+ String.valueOf(this.keyId) + "_" + jpgName;
                    String imgPath = MyString.image_pline_danger_folder_path + "/" + imgName;
                    //保存图片
                    MyImageOptClass.saveFile(mBitmap,80, imgPath);
                    //延迟1秒
                    Thread.sleep(1000);
                    imageUrls1.add(imgName);

                    // 发送消息，通知handler在主线程中更新UI
                    message.what = request_jpg_success;
                    message.obj = imgPath;
                }
            } catch (Exception e) {
                message.what = request_fail;
                message.obj = e.toString();
                e.printStackTrace();
            } finally {
                myHandler.sendMessage(message);
            }
        }
    }
























}




