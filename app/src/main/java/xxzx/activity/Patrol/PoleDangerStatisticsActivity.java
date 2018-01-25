package xxzx.activity.Patrol;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import xxzx.activity.ImageViewActivity;
import xxzx.activity.Patrol.MyTask.PoleDangerListActivity;
import xxzx.activity.R;
import xxzx.download.MultiThreadBaseClass;
import xxzx.patrol.Danger.DangerStatisticsClass;
import xxzx.patrol.Danger.ListItemDangerStatisticsAdapter;
import xxzx.polePlace.UploadPlacesClass;
import xxzx.publicClass.FileImageUpload;
import xxzx.publicClass.MyImgOpt.ImageCompress;
import xxzx.publicClass.IntentPublicMapState;
import xxzx.publicClass.LoadingDialog;
import xxzx.publicClass.MyFile;
import xxzx.publicClass.MyImgOpt.MyHttpJPG;
import xxzx.publicClass.MyHttpRequst;
import xxzx.publicClass.MyImgOpt.MyImageOptClass;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.NetworkConnected;
import xxzx.publicClass.PublicStaticFunUtil;
import xxzx.publicClass.ToastUtil;
import xxzx.publicClass.geometryJson.WKT;
import xxzx.spatialite.TableOptClass.PoleDangerTableOpt;
import xxzx.spatialite.TableOptClass.PoleTableOpt;
import xxzx.spatialite.TableStruct.PoleDangerTableDataClass;
import xxzx.spatialite.TableStruct.PolePlaceTableDataClass;
import xxzx.spatialite.TableStruct.PoleTableDataClass;

public class PoleDangerStatisticsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = PoleDangerStatisticsActivity.class.getSimpleName();

    private TextView tv_poleplaceinfo;
    private ListView listview;
    private String poleName;
    private int poleObjectId;
    private String plineName;
    private boolean isTask = false;
    private LoadingDialog loadingDialog;
    private PoleDangerTableOpt poleDangerTableOpt;

    private List<DangerStatisticsClass> listStatistics;
    private ListItemDangerStatisticsAdapter listItemAdapter;

    private UploadPlacesClass uploadPlacesClass;

    private LatLng latlng = null;

    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;
    private Button btn_5;
    private Button btn_6;
    //签到按钮
    private Button btn_place;


    public enum RequestType {
        JPGREQUEST,//jpg请求
        JPGUPLOAD,//jpg上传
        DANGERREQUEST,//隐患点请求
        POLEPLACEREQUEST,//历史到位信息请求
    }

    private final int request_fail = 0;//请求失败
    private final int request_jpg_success = 1;//图片请求成功
    private final int request_danger_success = 2;//隐患点请求成功
    private final int request_pole_place_success = 3;//请求到位信息成功
    private final int request_danger_fail = 4;//请求隐患点失败
    private final int upload_jpg_success = 5;//图片请求成功
    private final int upload_jpg_fail = 6;//图片请求成功


    private int poleJpgIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_pole_statistics);

        Intent intent = getIntent();
        poleName = intent.getStringExtra("polename");
        plineName = intent.getStringExtra("plinename");
        poleObjectId = intent.getIntExtra("poleobjectid",-1);
        isTask = intent.getBooleanExtra("istaskdanger", false);
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
                setResult(0,null);
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //更新数据
            this.getDangerStatisticsFormDatabase();
        }

        if (requestCode == MyString.requestCode_activity_to_photoactivity && resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();

            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile", "SD card is not avaiable/writeable right now.");
                return;
            }

            //图片压缩
            ImageCompress.compressImage(MyString.image_temp_path,MyString.image_temp_path);

            this.showUploadJpgDialog("是否上传该照片？");

        }
    }


    private void showUploadJpgDialog(String message) {

        AlertDialog.Builder build = new AlertDialog.Builder(PoleDangerStatisticsActivity.this);
        build.setTitle("注意").setMessage(message)
                .setPositiveButton("上传", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO
                        try {
                            //启动http请求的线程
                            MyRunnable runnable = new MyRunnable();
                            runnable.setRequestType(RequestType.JPGUPLOAD);
                            new Thread(runnable).start();

                            loadingDialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除图片
                        File file = new File(MyString.image_temp_path);
                        if (file.exists()) {
                            file.delete();
                        }

                        dialog.dismiss();
                    }
                }).show();
    }


    @Override
    public void onClick(final  View view) {


        AlertDialog.Builder builder = new AlertDialog.Builder(PoleDangerStatisticsActivity.this);
        builder.setTitle("选择");
        builder.setItems(new String[]{"图片浏览", "图片更新"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                poleJpgIndex = (int) view.getTag();
                String imageFolder = MyString.image_pline_pole_folder_path +"/"+ plineName;
                //检测并创建文件夹
                MyFile.createFolder(imageFolder);

                switch (which) {
                    case 0:
                        String imgPath = imageFolder + "/" + poleObjectId + "_" + String.valueOf(poleJpgIndex)+".jpg";
                        File file = new File(imgPath);
                        if (file.exists()) {
                            showImage(imgPath);
                        } else {
                            //启动http请求的线程
                            MyRunnable runnable = new MyRunnable();
                            runnable.setRequestType(RequestType.JPGREQUEST);
                            new Thread(runnable).start();

                            loadingDialog.show();
                        }
                        break;
                    case 1:
                        //调用系统拍照功能
                        Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                        File out = new File(MyString.image_temp_path);
                        Uri uri = Uri.fromFile(out);
                        // 获取拍照后未压缩的原图片，并保存在uri路径中
                        intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                        startActivityForResult(intentPhote, MyString.requestCode_activity_to_photoactivity);
                        break;
                }
            }
        });
        builder.show();

    }

    private void initView() {
        tv_poleplaceinfo = (TextView) findViewById(R.id.tv_poleplaceinfo);
        listview = (ListView) findViewById(R.id.listview);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_place  = (Button) findViewById(R.id.btn_place);

        btn_1.setTag(1);
        btn_2.setTag(2);
        btn_3.setTag(3);
        btn_4.setTag(4);
        btn_5.setTag(5);
        btn_6.setTag(6);

        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);

        loadingDialog = new LoadingDialog(this);

        RelativeLayout rl_toolbar =(RelativeLayout) findViewById(R.id.include_toolbar_danger_pole_statistics);

        Toolbar toolbar = (Toolbar) rl_toolbar.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        TextView tv_title = (TextView) toolbar.findViewById(R.id.tv_title);
        TextView tv_subtitle = (TextView) toolbar.findViewById(R.id.tv_subtitle);
        tv_subtitle.setText(this.poleName);
        tv_title.setText("线路本体信息");

        Button btn_right = (Button) toolbar.findViewById(R.id.btn_right);
        btn_right.setText("位置");
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PoleTableOpt poleTableOpt = new PoleTableOpt();
                List<PoleTableDataClass> list = poleTableOpt.getRowFromObjectId(poleObjectId);
                if(list.size()==0){
                    ToastUtil.show(PoleDangerStatisticsActivity.this,"没有查询到该杆塔的地理位置");
                }
                IntentPublicMapState.Intent(PoleDangerStatisticsActivity.this,MyString.requestCode_activity_to_publicmapactivity,MyString.intent_map_state_showgeometry,list.get(0).getGeometry());
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
                    PoleDangerStatisticsActivity.this.finish();
                }
            });
        }

        btn_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isPlace()){
                    //上传到位信息
                    try {
                        MySingleClass mySingleClass = MySingleClass.getInstance();

                        String username = mySingleClass.getUser().getmName();
                        String date = PublicStaticFunUtil.getCurrentDate().replace(" ", "T");

                        PolePlaceTableDataClass placeitem = new PolePlaceTableDataClass();
                        placeitem.setPoleObjectId(poleObjectId);
                        placeitem.setDateTime(date);
                        placeitem.setPoleName(poleName);
                        placeitem.setPowerName(plineName);
                        placeitem.setUserName(username);

                        List<PolePlaceTableDataClass> list = new ArrayList<PolePlaceTableDataClass>();
                        list.add(placeitem);

                        //上传
                        uploadPlacesClass.Upload(list, UploadPlacesClass.PlaceType.fromNull);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    ToastUtil.show(PoleDangerStatisticsActivity.this, "超出距离范围，无法签到！");
                }
            }
        });
    }

    private void initData() {
        poleDangerTableOpt = new PoleDangerTableOpt();
        this.listStatistics = new ArrayList<DangerStatisticsClass>();
        listItemAdapter = new ListItemDangerStatisticsAdapter(this, listStatistics);
        listview.setAdapter(listItemAdapter);

        //当标示为我的任务时，listview可点击
        if (isTask) {
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.putExtra("polename", poleName);
                    intent.putExtra("poleobjectid", poleObjectId);
                    intent.putExtra("plinename", plineName);
                    intent.putExtra("dangertype", position);
                    intent.setClass(PoleDangerStatisticsActivity.this, PoleDangerListActivity.class);
                    startActivityForResult(intent, 0);
                }
            });
        }

        List<PoleDangerTableDataClass> list_danger = poleDangerTableOpt.getRowFormObjectId(this.poleObjectId);
        //判断本地是否含有隐患点数据,如果无隐患点并网络连接成功
        if (list_danger.size() == 0 && NetworkConnected.isConnected(this)){
            this.getDangerStatisticsFormServer();
        }else{
            this.getDangerStatisticsFormDatabase();
        }

        //请求历史到位信息
        MyRunnable runnable = new MyRunnable();
        runnable.setRequestType(RequestType.POLEPLACEREQUEST);
        new Thread(runnable).start();


        if (uploadPlacesClass == null) {
            uploadPlacesClass = new UploadPlacesClass(PoleDangerStatisticsActivity.this);
        }

        uploadPlacesClass.setOnCompleted(new MultiThreadBaseClass.ICoallBack() {
            @Override
            public void onCompleted(boolean result) {
                if(result){
                    MySingleClass mySingleClass = MySingleClass.getInstance();
                    String username = mySingleClass.getUser().getmName();
                    String date = PublicStaticFunUtil.getCurrentDate();
                    String polereplaceStr = date + "   " + username;
                    //显示到位信息
                    tv_poleplaceinfo.setText(polereplaceStr);
                }
            }
        });
    }



    /**
     * 判断是否到位
     * @return
     */
    private boolean isPlace() {

        MySingleClass mySingleClass = MySingleClass.getInstance();
        AMapLocation aMapLocation = mySingleClass.getMyGdLocation();

        if(aMapLocation==null){
            return false;
        }

        Double lon = aMapLocation.getLongitude();
        Double lat = aMapLocation.getLatitude();

        //测试用
//        lon = 116.8295;
//        lat = 33.7899;

        if(latlng == null) {
            PoleTableOpt poleTableOpt = new PoleTableOpt();

            List<PoleTableDataClass> list_pole = poleTableOpt.getRowFromObjectId(this.poleObjectId);
            if (list_pole.size() > 0) {
                PoleTableDataClass poleTableDataClass = list_pole.get(0);
                List<LatLng> pts = WKT.POINTWktToPtsList(poleTableDataClass.getGeometry());
                latlng = pts.get(0);
            }
        }

        double distance = Math.sqrt(Math.pow((lon - latlng.longitude), 2) + Math.pow((lat - latlng.latitude), 2));
        if (distance <= MyString.distance_place) {
            return true;
        }
        return false;
    }


    /**
     * 从服务器中读取数据
     */
    private void getDangerStatisticsFormServer(){
        this.clearDangerStatistics();

        this.loadingDialog.show();

        MyRunnable runnable = new MyRunnable();
        runnable.setRequestType(RequestType.DANGERREQUEST);
        new Thread(runnable).start();
    }


    /**
     * 从数据库中读取数据
     */
    private void getDangerStatisticsFormDatabase(){
        this.clearDangerStatistics();

        //查询隐患表中该杆塔的隐患点
        List<PoleDangerTableDataClass> list_danger = poleDangerTableOpt.getRowFormObjectId(this.poleObjectId);

        for (PoleDangerTableDataClass danger : list_danger) {
            DangerStatisticsClass item = listStatistics.get(danger.getDangerType());
            if (item != null) {
                item.num++;
                item.maxLevel = danger.getDangerLevel() > item.maxLevel ? danger.getDangerLevel() : item.maxLevel;
            }
        }
        //更新统计列表
        listItemAdapter.notifyDataSetChanged();
    }


    private void clearDangerStatistics(){
        this.listStatistics.clear();
        for (int i = 0; i < MyString.poleDangerTypes.length; i++) {
            DangerStatisticsClass item = new DangerStatisticsClass();
            item.title = MyString.poleDangerTypes[i];
            item.type = i;
            item.num = 0;
            item.maxLevel = -1;
            listStatistics.add(item);
        }
    }


    private void showImage(String imgPath) {
        //显示图片
        Intent intent = new Intent();
        intent.setClass(PoleDangerStatisticsActivity.this, ImageViewActivity.class);
        intent.putExtra("imgpath", imgPath);
        startActivity(intent);
    }

    private void checkInsertDanger(String result) {
        try {
            JSONObject json = new JSONObject(result);
            String message = json.getString("message");

            if (message.equals("succeed")) {
                //再写入数据库
                JSONArray imagenum = json.getJSONArray("imagenum");
                List<Integer> imgNumList=new ArrayList<>();
                for (int i = 0; i < imagenum.length(); i++) {
                    imgNumList.add(imagenum.getJSONObject(i).getInt("ImageNum"));
                }

                //删除原始数据
                //poleDangerTableOpt.getRowFormKeyID(tableDataClass.getKeyID());

                JSONArray data = json.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject danger = data.getJSONObject(i);

                    PoleDangerTableDataClass tableDataClass = new PoleDangerTableDataClass();
                    tableDataClass.setDangerType(danger.getInt("Type"));
                    //tableDataClass.setVersion(0);
                    tableDataClass.setDangerMark("");
                    tableDataClass.setSpotMark(danger.getString("SpotMarks") == null ? "" : danger.getString("SpotMarks"));
                    tableDataClass.setDateTime(danger.getString("Checkdate").replace("T", " "));
                    tableDataClass.setDangerLevel(danger.getInt("State"));
                    tableDataClass.setKeyID(danger.getInt("KeyId"));
                    tableDataClass.setDangerName(danger.getString("Marks") == null ? "没有标题" : danger.getString("Marks"));
                    tableDataClass.setPicsJson("");
                    tableDataClass.setPoleName(danger.getString("Gtname"));
                    tableDataClass.setPoleObjectId(danger.getInt("Gtid"));
                    tableDataClass.setUserName(danger.getString("Username"));
                    tableDataClass.setPowerName(this.plineName);
                    tableDataClass.setImgNum(imgNumList.get(i));
                    //插入数据库
                    poleDangerTableOpt.insert(tableDataClass);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void showPolePlace(String result) {
        //显示图片
        try {
            JSONObject json = new JSONObject(result);
            String message = json.getString("message");

            if (message.equals("succeed")) {
                JSONArray resultData = json.getJSONArray("result");

                String polereplaceStr="无历史到位信息";

                for (int i = 0; i < resultData.length(); i++) {
                    String username = resultData.getJSONObject(i).getString("UserName");
                    String date = resultData.getJSONObject(i).getString("Date").replace("T"," ");
                    polereplaceStr = date+"   "+username;
                }

                tv_poleplaceinfo.setText(polereplaceStr);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 杆塔隐患点请求
     */
    private void requestPoleDanger() {
        MySingleClass mySingleClass = MySingleClass.getInstance();
        Properties properties = mySingleClass.getProperties();

        Message message = new Message();
        try {
            String url = properties.get("url_pole_danger2").toString();
            String poststring = properties.get("url_pole_danger2_poststring").toString();

            poststring  = String.format(poststring,  String.valueOf(poleObjectId));

            String result = MyHttpRequst.getHttpPostRequst2(url,poststring);

            if (result == null || result.trim().equals("")) {
                message.what = request_danger_fail;
                message.obj = "无法连接服务器，不能更新当前隐患点信息，请确定网络连接是否正常！";
            } else {
                // 发送消息，通知handler在主线程中更新UI
                message.what = request_danger_success;
                message.obj = result;
            }
        } catch (Exception e) {
            message.what = request_danger_fail;
            message.obj = e.toString();
            e.printStackTrace();
        } finally {
            myHandler.sendMessage(message);
        }
    }




    /**
     * 杆塔照片请求
     *
     */
    private void requestPoleJpg() {
        MySingleClass mySingleClass = MySingleClass.getInstance();
        Properties properties = mySingleClass.getProperties();

        Message message = new Message();
        try {
            String url = properties.get("url_pole_jpg").toString();
            String httpPath = String.format(url, String.valueOf(poleObjectId), String.valueOf(poleJpgIndex)+".jpg");
            //Log.e("pole",httpPath);
            //******** 方法2：取得的是InputStream，直接从InputStream生成bitmap ***********/
            InputStream inputStream = MyHttpJPG.getImageStream(httpPath);
            if (inputStream == null) {
                message.what = request_fail;
                message.obj = "不能下载图片,请确定图片是否存在以及确定网络连接是否正常！";
            } else {
                Bitmap mBitmap = BitmapFactory.decodeStream(inputStream);
                //********************************************************************/
                String imgPath = MyString.image_pline_pole_folder_path +"/"+plineName+ "/" + String.valueOf(poleObjectId) + "_" + String.valueOf(poleJpgIndex)+".jpg";
                //保存图片
                MyImageOptClass.saveFile(mBitmap,80, imgPath);
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




    private void uploadPoleJpg(){

        MySingleClass mySingleClass = MySingleClass.getInstance();
        Properties properties = mySingleClass.getProperties();

        Message message = new Message();
        try {

            String url = properties.get("url_upload_pole_jpg").toString();
            //上传图片
            String myUrl = String.format(url, String.valueOf(this.poleObjectId), String.valueOf(poleJpgIndex));

            File f = new File(MyString.image_temp_path);
            if (!f.exists()) {
                return;
            }
            String result = FileImageUpload.uploadFile(f, myUrl);

            if(result.equals(FileImageUpload.SUCCESS)) {
                //删除临时图片
                String imageFolder = MyString.image_pline_pole_folder_path + "/" + plineName;
                String imgPath = imageFolder + "/" + poleObjectId + "_" + String.valueOf(poleJpgIndex) + ".jpg";

                //图片copy
                MyFile.copyFile(MyString.image_temp_path,imgPath);

                File file = new File(MyString.image_temp_path);
                if (file.exists()) {
                    file.delete();
                }

                message.what = upload_jpg_success;
            }else{
                message.what = upload_jpg_fail;
            }

        }catch (Exception e) {
            message.what = upload_jpg_fail;
            message.obj = e.toString();
            e.printStackTrace();
        } finally {
            myHandler.sendMessage(message);
        }
    }



    /**
     * 杆塔到位信息请求
     */
    private void requestPolePlace() {
        MySingleClass mySingleClass = MySingleClass.getInstance();
        Properties properties = mySingleClass.getProperties();

        Message message = new Message();
        try {
            String url = properties.get("url_place_pole_get2").toString();
            String poststring = properties.get("url_place_pole_get2_poststring").toString();

            poststring  = String.format(poststring,  String.valueOf(poleObjectId));
            String result = MyHttpRequst.getHttpPostRequst2(url,poststring);
            Log.e("result","url"+url);
            Log.e("result","poststring"+poststring);
            Log.e("result",result);

            if (result == null || result.trim().equals("")) {
                message.what = request_fail;
                message.obj = "无法连接服务器，请确定网络连接是否正常！";
            } else {
                // 发送消息，通知handler在主线程中更新UI
                message.what = request_pole_place_success;
                message.obj = result;
            }
        } catch (Exception e) {
            message.what = request_fail;
            message.obj = e.toString();
            e.printStackTrace();
        } finally {
            myHandler.sendMessage(message);
        }
    }

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            loadingDialog.dismiss();
            switch (msg.what) {
                case request_fail:
                    ToastUtil.show(PoleDangerStatisticsActivity.this, msg.obj.toString());
                    break;
                case request_jpg_success:
                    showImage((String) msg.obj);
                    break;
                case request_danger_success:
                    //检查并写入数据库
                    checkInsertDanger((String) msg.obj);
                    //直接读取本地数据库的隐患信息
                    getDangerStatisticsFormDatabase();
                    break;
                case request_danger_fail:
                    ToastUtil.show(PoleDangerStatisticsActivity.this, msg.obj.toString());
                    //网络请求失败，直接读取本地数据库的隐患信息
                    getDangerStatisticsFormDatabase();
                    break;
                case request_pole_place_success:
                    showPolePlace((String) msg.obj);
                    break;
                case upload_jpg_fail:
                    showUploadJpgDialog("图片上传失败，是否重新上传？");
                    break;
                case upload_jpg_success:
                    ToastUtil.show(PoleDangerStatisticsActivity.this, "图片上传成功");
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    /**
     *
     */
    public class MyRunnable implements Runnable {

        private RequestType requestType;


        public void setRequestType(RequestType requestType) {
            this.requestType = requestType;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (this.requestType == RequestType.DANGERREQUEST) {
                requestPoleDanger();
            } else if (this.requestType == RequestType.JPGREQUEST) {
                requestPoleJpg();
            }else if(this.requestType==RequestType.POLEPLACEREQUEST){
                requestPolePlace();
            }else if(this.requestType==RequestType.JPGUPLOAD){
                uploadPoleJpg();
            }
        }
    }
}




