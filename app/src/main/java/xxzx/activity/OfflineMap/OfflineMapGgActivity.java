package xxzx.activity.OfflineMap;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import xxzx.activity.R;
import xxzx.activity.PublicBaseActivity.BaseToolBarActivity;
import xxzx.publicClass.IntentPublicMapState;
import xxzx.publicClass.MySharedPreferences;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.MyTaskExecutorClass;
import xxzx.publicClass.SDTool;
import xxzx.publicClass.ToastUtil;
import xxzx.publicClass.WGS84MercatorConvert;
import xxzx.publicClass.geometryJson.WKT;

public class OfflineMapGgActivity extends BaseToolBarActivity implements View.OnClickListener {

    private SeekBar seekbar;
    private TextView tv_mapLevel;
    private EditText et_mapType;
    private EditText et_mapRange;
    private Button btn_caculate;
    private Button btn_typeSelect;
    private Button btn_map;
    private ProgressDialog progDialog;
    //切片信息
    private int progressValue,tilesSize;
    //数据格式
    private DecimalFormat df = new DecimalFormat("0.00");

    //切片保存路径
    private String savePath;

    final String items[] = {"谷歌卫星矢量混合图"};
    private int mapType = -1;//-1表示没有选择地图类型 0表示谷歌影像 1表示谷歌卫星混合
    //默认一张切片的数量大小
    private double oneTileSize = 0.01293747583;
    //需下载的切片大小
    private double downloadTileSize=0.0;
    //需下载的切片数量
    private int downloadTileCount=0;
    //下载的数据量大小
    private double offlinesize=0.0;

    //下载切片范围
    private LatLngBounds.Builder bb;
    //下载最大级别
    private int levelMax=0;

    private final int TYPE_RUNNABLE_CACULATE=1;//计算数据量
    private final int TYPE_RUNNABLE_DOWNLOAD=2;//下载切片

    /**
     * 每次执行限定个数个任务的线程池
     */
    private ExecutorService limitedTaskExecutor = null;
    /**
     * 线程池数量
     */
    private int limitedTaskExecutorNum = 5;


    double[] res = {156543.03392800014, 78271.516963999937, 39135.758482000092, 19567.879240999919,
            9783.9396204999593, 4891.9698102499797, 2445.9849051249898, 1222.9924525624949,
            611.49622628137968, 305.74811314055756, 152.87405657041106, 76.437028285073239,
            38.21851414253662, 19.10925707126831, 9.5546285356341549, 4.7773142679493699,
            2.3886571339746849, 1.1943285668550503, 0.59716428355981721, 0.29858214164761665};
    double[] scale = {591657527.591555, 295828763.79577702, 147914381.89788899, 73957190.948944002,
            36978595.474472001, 18489297.737236001, 9244648.8686180003, 4622324.4343090001,
            2311162.2171550002, 1155581.108577, 577790.55428899999, 288895.27714399999,
            144447.638572, 72223.819285999998, 36111.909642999999, 18055.954822,
            9027.9774109999998, 4513.9887049999998, 2256.994353, 1128.4971760000001};


    double startLon = -20037508.3427892;
    double startLat = -20037508.3427892;

    int imgWight = 256;
    int imgHeight = 256;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_map_gg_download);
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
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 决定左上角图标的右侧是否有向左的小箭头, true
        // 有小箭头，并且图标可以点击
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        seekbar = (SeekBar) findViewById(R.id.seekbar);
        tv_mapLevel = (TextView) findViewById(R.id.tv_mapLevel);
        et_mapType = (EditText) findViewById(R.id.et_mapType);
        et_mapRange = (EditText) findViewById(R.id.et_mapRange);
        btn_caculate = (Button) findViewById(R.id.btn_caculate);
        btn_typeSelect = (Button) findViewById(R.id.btn_typeSelect);
        btn_map = (Button) findViewById(R.id.btn_map);

        btn_map.setOnClickListener(this);
        btn_caculate.setOnClickListener(this);
        btn_typeSelect.setOnClickListener(this);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_mapLevel.setText(String.valueOf(progress / 10));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initData() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_typeSelect:
                selectMapType();
                break;
            case R.id.btn_map:
                IntentPublicMapState.Intent(OfflineMapGgActivity.this,MyString.requestCode_activity_to_publicmapactivity, MyString.intent_map_state_selectgeometry, "");
                break;
            case R.id.btn_caculate:
                //初始化
                this.tilesSize = 0;

                if (mapType == -1 || et_mapRange.getText().toString().trim().equals("")) {
                    Toast.makeText(OfflineMapGgActivity.this, "请将下载信息填写完整！", Toast.LENGTH_SHORT).show();
                    return;
                }

                this.bb = this.getLatLngBounds();

                if(bb==null){
                    ToastUtil.show(this, "下载范围解析错误！");
                    return;
                }

                this.levelMax = Integer.valueOf(tv_mapLevel.getText().toString());

                //计算切片大小
                this.caculateTilesSize();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            Bundle b = data.getExtras();  //data为B中回传的Intent
            if(b!=null&&b.containsKey("geometry")){
                String wkt = (String) b.get("geometry");//poiItem
                et_mapRange.setText(wkt);
            }
        }

       super.onActivityResult(requestCode, resultCode, data);
    }




    private void selectMapType() {
        //dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(OfflineMapGgActivity.this);  //先得到构造器
        builder.setCancelable(true);
        builder.setTitle("选择地图类型"); //设置标题
        //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mapType = which;
                switch (which) {
                    case 0:
                        et_mapType.setText(items[0]);
                        break;
//                    case 1:
//                        et_mapType.setText(items[1]);
//                        break;
                }
            }
        });
        builder.create().show();
    }

    /**
     * 计算切片数量大小
     */
    private void caculateTilesSize() {

        this.downloadTileCount = 0;
        this.downloadTileSize = 0.0;

        //初始化进度条
        showProgressDialog(levelMax + 1, "数据量计算", "正在计算切片数量，请稍等……");

        //线程结束处理程序
        limitedTaskExecutor = Executors.newFixedThreadPool(limitedTaskExecutorNum);// 限制线程池大小线程池

        double[] min = WGS84MercatorConvert.lonLat2Mercator(bb.build().southwest.longitude, bb.build().southwest.latitude);
        double[] max = WGS84MercatorConvert.lonLat2Mercator(bb.build().northeast.longitude, bb.build().northeast.latitude);

        for (int level = 0; level <= levelMax; level++) {
            int colmin = (int) ((min[0] - (startLon)) / (res[level] * imgWight));
            int rowmax = (int) ((-min[1] - (startLon)) / (res[level] * imgWight));

            int colmax = (int) ((max[0] - (startLon)) / (res[level] * imgWight));
            int rowmin = (int) ((-max[1] - (startLon)) / (res[level] * imgWight));


            //启动线程
            MyCaculateRunnable runnable = new MyCaculateRunnable();
            runnable.setData(level, colmin, rowmin, colmax, rowmax);
            //执行线程池
            limitedTaskExecutor.execute(runnable);
        }

        //终止线程
        MyTaskExecutorClass.TaskExecutorTerminated(this.limitedTaskExecutor,this.progDialog);
    }


    /**
     * 下载切片
     */
    private void downTilesBytes() {

        int count = this.caculateTilesCount();
        //初始化进度条
        showProgressDialog(count, "数据下载", "正在下载切片数据，请稍等……");

        String url = this.getUrl();

        //检查线程结束处理程序
        limitedTaskExecutor = Executors.newFixedThreadPool(limitedTaskExecutorNum);// 限制线程池大小线程池

        double[] min = WGS84MercatorConvert.lonLat2Mercator(bb.build().southwest.longitude, bb.build().southwest.latitude);
        double[] max = WGS84MercatorConvert.lonLat2Mercator(bb.build().northeast.longitude, bb.build().northeast.latitude);

        for (int level = 0; level <= levelMax; level++) {
            int colmin = (int) ((min[0] - (startLon)) / (res[level] * imgWight));
            int rowmax = (int) ((-min[1] - (startLon)) / (res[level] * imgWight));

            int colmax = (int) ((max[0] - (startLon)) / (res[level] * imgWight));
            int rowmin = (int) ((-max[1] - (startLon)) / (res[level] * imgWight));

            //遍历下载切片
            for (int row = colmin; row <= colmax; row++) {
                for (int col = rowmin; col <= rowmax; col++) {
                    Log.d("DDD","url地址："+String.valueOf(url));
                    //启动线程
                    MyDownloadRunnable runnable = new MyDownloadRunnable();
                    runnable.setUrl(url, row, col, level);
                    runnable.setType(TYPE_RUNNABLE_DOWNLOAD);
                    //执行线程池
                    limitedTaskExecutor.execute(runnable);
                }
            }
        }
        //终止线程
        MyTaskExecutorClass.TaskExecutorTerminated(this.limitedTaskExecutor,this.progDialog);

    }


    /**
     * 根据范围计算切片数量
     * @return
     */
    private int caculateTilesCount() {
        int count = 0;

        double[] min = WGS84MercatorConvert.lonLat2Mercator(bb.build().southwest.longitude, bb.build().southwest.latitude);
        double[] max = WGS84MercatorConvert.lonLat2Mercator(bb.build().northeast.longitude, bb.build().northeast.latitude);

        for (int level = 0; level <= levelMax; level++) {
            int colmin = (int) ((min[0] - (startLon)) / (res[level] * imgWight));
            int rowmax = (int) ((-min[1] - (startLon)) / (res[level] * imgWight));

            int colmax = (int) ((max[0] - (startLon)) / (res[level] * imgWight));
            int rowmin = (int) ((-max[1] - (startLon)) / (res[level] * imgWight));

            //计算切片数量
            count = count + (colmax - colmin + 1) * (rowmax - rowmin + 1);
        }

        return count;
    }



    private String getUrl(){
        Properties properties = MySingleClass.getInstance().getProperties();
        String url = "";

        switch (this.mapType) {
            case 0:
                url = properties.get("zhiji_img_anno").toString();
                savePath = MyString.maps_cache_google_img_folder_path;
                break;
//            case 1:
//                url = properties.get("google_img_anno").toString();
//                savePath = MyString.maps_cache_google_img_anno_folder_path;
//                break;
        }
        return url;
    }

    private LatLngBounds.Builder getLatLngBounds() {
        LatLngBounds.Builder b = new LatLngBounds.Builder();

        String wkt = et_mapRange.getText().toString().trim();
        List<LatLng> ptStrs = WKT.wktToPtsList(wkt);

        if(ptStrs==null){
            return null;
        }

        for (int i = 0; i < ptStrs.size(); i++) {
            try {
                b.include(new LatLng(ptStrs.get(i).latitude, ptStrs.get(i).longitude));
            } catch (Exception ex) {
                ex.printStackTrace();
                continue;
            }
        }
        return b;
    }


    private int getHttpByteSize(String url, int row, int col, int level) {
        int result = 0;
        try {
            //rowcollevelList.add(row+";"+col+";"+level);

            URL sjwurl = new URL(String.format(url, row, col, level));
            HttpURLConnection httpUrl = null;
            Log.d("开始连接切片地址：",sjwurl.toString()+"-------"+String.valueOf(result));
            httpUrl = (HttpURLConnection) sjwurl.openConnection();
            httpUrl.setConnectTimeout(6 * 1000);
            result = httpUrl.getContentLength();

            //Log.d("切片大大小：",sjwurl.toString()+"-------"+String.valueOf(result));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("下载出现问题：","");
        }
        return result;
    }

    private byte[] getHttpByte(String url, int row, int col, int level) {
        byte[] result = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            URL sjwurl = new URL(String.format(url, row, col, level));
            HttpURLConnection httpUrl = null;
            BufferedInputStream bis = null;
            byte[] buf = new byte[1024];

            httpUrl = (HttpURLConnection) sjwurl.openConnection();
            httpUrl.setConnectTimeout(6 * 1000);
            httpUrl.connect();
            Log.d("DDD", "返回码：" + String.valueOf(httpUrl.getResponseCode()));
            if (httpUrl.getResponseCode() == 200) {
                bis = new BufferedInputStream(httpUrl.getInputStream());

                while (true) {
                    int bytes_read = bis.read(buf);
                    if (bytes_read > 0) {
                        bos.write(buf, 0, bytes_read);
                    } else {
                        break;
                    }
                }
                bis.close();
                httpUrl.disconnect();
                Log.e("切片", sjwurl.toString());
                result = bos.toByteArray();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    Handler mHandler = new Handler() {
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case TYPE_RUNNABLE_CACULATE:
                    progressValue++;
                    progDialog.setProgress(progressValue);
                    //处理完成，对话框消失
                    if (progressValue == progDialog.getMax()) {
                        //计算对话框消失,显示下载确认对话框
                        progDialog.dismiss();
                        showdownloadDialog();
                    }
                    Log.e("切片数量：",String.valueOf(progressValue));
                    break;
                case TYPE_RUNNABLE_DOWNLOAD:
                    progressValue++;
                    tilesSize = tilesSize + (int) msg.obj;
                    progDialog.setProgress(progressValue);
                    //处理完成，对话框消失
                    if (progressValue == progDialog.getMax()) {
                        //存储数据量大小
                        MySharedPreferences.addOfflineTileSize(offlinesize);

                        //计算对话框消失
                        progDialog.dismiss();
                        Toast.makeText(OfflineMapGgActivity.this, "下载完成！", Toast.LENGTH_SHORT).show();

                        //actiity切换
                        setResult(RESULT_OK, null);
                        finish();//此处一定要调用finish()方法
                    }

                    break;
                default:
                    break;
            }

        }
    };



    public class MyDownloadRunnable implements Runnable
    {
        String url;
        int row,col,level;
        int type;
        public void setUrl(String url, int row, int col, int level) {
            this.url = url;
            this.row = row;
            this.col = col;
            this.level = level;
        }

        public void setType(int type) {
            this.type = type;
        }
        @Override
        public void run()
        {
            int result=0;
            try {
                //判断切片是否存在
                boolean isexsit = isTileExsit(row, col, level);
                //如果该切片不存在
                if (!isexsit) {
                    byte[] _byte = getHttpByte(url, row, col, level);
                    //保存切片
                    addOfflineCacheFile(row, col, level, _byte);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }finally {
                Message message = new Message();
                message.obj=result;
                message.what = this.type;
                mHandler.sendMessage(message);
            }
        }
    }


    public class MyCaculateRunnable implements Runnable
    {
        int level;
        int colmin;
        int colmax;
        int rowmin;
        int rowmax;

        int type;
        public void setData(int level,int colmin,int rowmin,int colmax,int rowmax) {
            this.level=level;
            this.colmin=colmin;
            this.rowmin=rowmin;
            this.colmax=colmax;
            this.rowmax=rowmax;
        }
        @Override
        public void run()
        {
            double size=0.0;
            int count=0;
            Message message = new Message();
            try {
                //遍历下载切片
                for (int row = colmin; row <= colmax; row++) {
                    for (int col = rowmin; col <= rowmax; col++) {

                        //判断切片是否存在
                        boolean isexsit = isTileExsit(row, col, level);
                        //如果该切片不存在
                        if (!isexsit) {
                            //网络请求大小
                            //result = getHttpByteSize(url, row, col, level);
                            //不存在，切片大小为固定值
                            size = size + oneTileSize;
                            count++;
                        }
                    }
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }finally {
                downloadTileSize = downloadTileSize + size;
                downloadTileCount = downloadTileCount + count;
                message.what = TYPE_RUNNABLE_CACULATE;
                mHandler.sendMessage(message);
            }
        }
    }


    /**
     * 判断该切片在离线文件中村不存在
     * @param row
     * @param col
     * @param level
     * @return ture表示存在，false表示不存在
     */
    private boolean isTileExsit(int row,int col,int level){
        File rowfile = new File(savePath + "/" + level + "_" + col + "_" + row + ".png");
        if (rowfile.exists())
            return true;
        return false;
    }

    // 将图片保存到本地 目录结构可以随便定义 只要你找得到对应的图片
    protected void addOfflineCacheFile(int row, int col,int level, byte[] bytes) {
        if(bytes==null){
            return;
        }

        String path = savePath;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        File rowfile = new File(path + "/" + level + "_" + col + "_" + row + ".png");

        Log.e("保存文件路径：", rowfile.getAbsoluteFile().getName());
        if (!rowfile.exists()) {
            FileOutputStream out=null;
            try {
                out = new FileOutputStream(rowfile);
                out.write(bytes);
                out.close();

                offlinesize = offlinesize + Double.valueOf(bytes.length)/(1024*1024);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }finally {
            }
        }
    }

    /**
     * 显示下载确认对话框
     */
    private void showdownloadDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("数据信息");
        builder.setCancelable(false);
        String content="";

        if(downloadTileCount==0){
            content="下载切片数量为" + downloadTileCount + ",数据量大小约为" + df.format(downloadTileSize) + "MB,所以不需要下载切片！";
        } else if (SDTool.getSDFreeSize() + 50 < downloadTileSize) {//如果下载完成后SD卡不足50M,不再提供下载
            content="下载切片数量为" + downloadTileCount + ",数据量大小约为" + df.format(downloadTileSize) + "MB,SD卡剩余空间不足,请清空缓存后在下载！";
        }else {
            content="下载切片数量为" + downloadTileCount + ",数据量大小约为" + df.format(downloadTileSize) + "MB,是否确认下载？";
            builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    // 下载文件
                    downTilesBytes();
                }
            });
        }
        builder.setMessage(content);

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }


    /**
     * 显示进度框
     */
    private void showProgressDialog(int max_Progress,String title,String content) {
        if(progDialog==null) {
            progDialog = new ProgressDialog(this);
            //对话框点击返回事件
//            downloadProgDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                @Override
//                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(OfflineMapGgActivity.this);
//                        builder.setTitle("提示");
//                        builder.setMessage("切片数据没有下载完成,确定取消下载吗？");
//                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // TODO
//                                try {
//                                    if (limitedTaskExecutor != null) {
//                                        limitedTaskExecutor.shutdownNow();
//                                        TaskExecutorTerminated();
//                                        downloadProgDialog.dismiss();
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //如果点击的是取消，不做任何处理
//                                dialog.dismiss();
//                            }
//                        });
//
//                        builder.create().show();
//                    }
//                    return false;
//                }
//            });
        }

        progressValue = 0;
        progDialog.setProgress(progressValue);
        progDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setTitle(title);
        progDialog.setMessage(content);
        //进度最大值
        progDialog.setMax(max_Progress);

        progDialog.show();
    }


}




