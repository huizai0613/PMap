package xxzx.dangerRadar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.Patrol.MyTask.ChnDangerUpdateActivity;
import xxzx.activity.Patrol.MyTask.MyTaskPowerlineActivity;
import xxzx.activity.Patrol.Record.RecordChnAndPoleActivity;
import xxzx.activity.Patrol.TaskSetting.PatrolPowerlineActivity;
import xxzx.activity.R;
import xxzx.patrol.Danger.ListItemDangerEditAdapter;
import xxzx.patrol.Danger.ListViewDangerHolder;
import xxzx.publicClass.MyActivityFragmentListener;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.ToastUtil;
import xxzx.publicClass.geometryJson.WKT;
import xxzx.spatialite.TableOptClass.ChannelDangerTableOpt;
import xxzx.spatialite.TableStruct.BaseDangerTableDataClass;
import xxzx.spatialite.TableStruct.ChannelDangerTableDataClass;


/**
 * Created by Daniel on 12/23/2014.
 */
public class DangerChnFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    /**
     * ListView对象
     */
    private ListView listview;

    private Button btn_dangerType;
    private Button btn_dangerLevel;

    private List<BaseDangerTableDataClass> dangerList;
    private ListItemDangerEditAdapter adapter;

    //获得地图
    //private AMap aMap;
    //获得自定义图层列表
    //private Map<String, Object> overlayMap;

    private MyActivityFragmentListener.MyListener myListener;

    private boolean isfragmentCreate=false;

    private ChannelDangerTableOpt channelDangerTableOpt;

    private AMapLocation location = null;
    private double offset = 0.0;

    private int dangerType = -1;
    private int dangerLevel = -1;

    private LatLng minLatLng = null;
    private LatLng maxLatLng = null;

    String[] new_dangerTypeItems = new String[MyString.channelDangerTypes.length+1];
    String[] new_dangerLevelItems = new String[MyString.dangerLevels_num.length+1];

    public static DangerChnFragment newInstance() {
        DangerChnFragment f = new DangerChnFragment();
//        Bundle b = new Bundle();
//        b.putInt(ARG_POSITION, position);
//        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.isfragmentCreate=true;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_danger_radar, container, false);

        listview = (ListView) v.findViewById(R.id.listview);
        btn_dangerType = (Button) v.findViewById(R.id.btn_dangerType);
        btn_dangerLevel = (Button) v.findViewById(R.id.btn_dangerLevel);

        btn_dangerType.setOnClickListener(this);
        btn_dangerLevel.setOnClickListener(this);

        this.initData();

        return v;
    }

    /**
     * Fragment第一次附属于Activity时调用,在onCreate之前调用
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myListener = (MyActivityFragmentListener.MyListener) activity;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }


    /**
     * 初始化数据
     */
    private void initData() {

        if(this.dangerList ==null){
            this.dangerList = new ArrayList<>();
        }

        new_dangerTypeItems[0] = "0.所有";
        for(int i=0;i<MyString.channelDangerTypes.length;i++){
            new_dangerTypeItems[i+1] = MyString.channelDangerTypes[i];
        }

        new_dangerTypeItems[0] = "0.所有";
        for(int i=0;i<MyString.channelDangerTypes.length;i++){
            new_dangerLevelItems[i+1] = MyString.channelDangerTypes[i];
        }

        this.btn_dangerType.setText(new_dangerTypeItems[0]);
        this.btn_dangerLevel.setText(new_dangerLevelItems[0]);

        this.dangerType = -1;
        this.dangerLevel = -1;

        this.adapter = new ListItemDangerEditAdapter(getContext(), dangerList);
        this.listview.setAdapter(this.adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewDangerHolder viewHolder = (ListViewDangerHolder) view.getTag();

                Intent intent = new Intent();
                intent.setClass(getContext(), ChnDangerUpdateActivity.class);
                intent.putExtra("chnname", ((ChannelDangerTableDataClass)dangerList.get(position)).getChannelName());
                intent.putExtra("chnobjectid", ((ChannelDangerTableDataClass)dangerList.get(position)).getChannelObjectId());
                intent.putExtra("plinename", ((ChannelDangerTableDataClass)dangerList.get(position)).getPowerName());
                intent.putExtra("rowid", ((ChannelDangerTableDataClass)dangerList.get(position)).getRowId());
                intent.putExtra("dangertype", ((ChannelDangerTableDataClass)dangerList.get(position)).getDangerType());
                startActivityForResult(intent, 0);
            }
        });

        showDangerList();
    }

    @Override
    public void onClick(View view) {

        AlertDialog.Builder builder;

        switch (view.getId()) {
            case R.id.btn_dangerType:
                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("请选择");
                //builder.setIcon(android.R.drawable.ic_dialog_info)
                builder.setItems(new_dangerTypeItems,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                btn_dangerType.setText(new_dangerTypeItems[which]);
                                dangerType = which-1;
                                showDangerList();
                            }
                        }
                );
                builder.show();
                break;
            case R.id.btn_dangerLevel:

                builder = new AlertDialog.Builder(getContext());
                builder.setTitle("请选择");
                //builder.setIcon(android.R.drawable.ic_dialog_info)
                builder.setItems(new_dangerLevelItems,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                btn_dangerLevel.setText(new_dangerLevelItems[which]);
                                dangerLevel = which-1;
                                showDangerList();
                            }
                        }
                );
                builder.show();
                break;
        }
    }


    private List<LatLng> getRegion(){

        double minLon=location.getLongitude()-offset;
        double minLat=location.getLatitude()-offset;
        double maxLon=location.getLongitude()+offset;
        double maxLat=location.getLatitude()+offset;

        this.minLatLng=new LatLng(minLat,minLon);
        LatLng latLng2=new LatLng(maxLat,minLon);
        this.maxLatLng=new LatLng(maxLat,maxLon);
        LatLng latLng4=new LatLng(minLat,maxLon);
        LatLng latLng5=new LatLng(minLat,minLon);

        List<LatLng> list=new ArrayList<>();
        list.add(this.minLatLng);
        list.add(latLng2);
        list.add(this.maxLatLng);
        list.add(latLng4);
        list.add(latLng5);

        return list;
    }


    private void showDangerList(){

        this.offset = MyString.distance_danger_radar;

        this.location = MySingleClass.getInstance().getMyGdLocation();
        if (this.location == null) {
            ToastUtil.show( this.mContext,"当前没有成功定位");
            return;
        }

        //测试用用
        this.location.setLatitude(31.8770);
        this.location.setLongitude(117.5727);

        //生成矩形范围
        List<LatLng> region = getRegion();

        this.dangerList.clear();

        List<ChannelDangerTableDataClass> list =  getChnDangers(region);

        for(ChannelDangerTableDataClass danger:list){

            if(dangerType!=-1){
                if(danger.getDangerType()==dangerType){
                    if(dangerLevel!=-1){
                        if(danger.getDangerLevel()==dangerLevel){
                            this.dangerList.add(danger);
                        }
                    }else{
                       this.dangerList.add(danger);
                    }
                }
            }else{
                if(dangerLevel!=-1){
                    if(danger.getDangerLevel()==dangerLevel){
                        this.dangerList.add(danger);
                    }
                }else{
                    this.dangerList.add(danger);
                }
            }
        }

        this.adapter.notifyDataSetChanged();
    }



    private List<ChannelDangerTableDataClass> getChnDangers(List<LatLng> list){

        String polygonWKT = WKT.PointListToPolygonWKT(list);

        if(channelDangerTableOpt == null){
            channelDangerTableOpt = new ChannelDangerTableOpt();
        }

        //空间查询
        List<ChannelDangerTableDataClass> dangers = channelDangerTableOpt.getRowWithinGeometry(polygonWKT);

        //传统的查询
        //List<ChannelDangerTableDataClass> dangers2 = channelDangerTableOpt.getRow(this.minLatLng,this.maxLatLng);
        return dangers;
    }
}




