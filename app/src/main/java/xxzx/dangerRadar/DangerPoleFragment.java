package xxzx.dangerRadar;

import android.app.Activity;
import android.content.Context;
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
public class DangerPoleFragment extends Fragment implements View.OnClickListener {

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
    private int dnagerLevel = -1;

    public static DangerPoleFragment newInstance() {
        DangerPoleFragment f = new DangerPoleFragment();
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
        switch (view.getId()) {
            case R.id.btn_dangerType:
                showDangerList();
                break;
            case R.id.btn_dangerLevel:
                showDangerList();
                break;
        }
    }


    private List<LatLng> getRegion(){

        double minLon=location.getLongitude()-offset;
        double minLat=location.getLatitude()-offset;
        double maxLon=location.getLongitude()+offset;
        double maxLat=location.getLatitude()+offset;

        LatLng latLng1=new LatLng(minLat,minLon);
        LatLng latLng2=new LatLng(maxLat,minLon);
        LatLng latLng3=new LatLng(maxLat,maxLon);
        LatLng latLng4=new LatLng(minLat,maxLon);
        LatLng latLng5=new LatLng(minLat,minLon);

        List<LatLng> list=new ArrayList<>();
        list.add(latLng1);
        list.add(latLng2);
        list.add(latLng3);
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

        //实验用
        this.location.setLatitude(33.7872);
        this.location.setLongitude(116.8296);

        //生成矩形范围
        List<LatLng> region = getRegion();

        if(this.dangerList ==null){
            this.dangerList = new ArrayList<>();
        }
        this.dangerList.clear();

        List<ChannelDangerTableDataClass> list =  getChnDangers(region);


        for(ChannelDangerTableDataClass danger:list){

//            if(this.dangerType==-1&&this.dnagerLevel ==-1){
//
//            }

            if(danger.getDangerType()==this.dangerType && danger.getDangerLevel() == this.dnagerLevel){

                this.dangerList.add(danger);
            }
        }

        this.adapter.notifyDataSetChanged();
    }



    private List<ChannelDangerTableDataClass> getChnDangers(List<LatLng> list){

        String polygonWKT = WKT.PointListToPolygonWKT(list);

        if(channelDangerTableOpt == null){
            channelDangerTableOpt = new ChannelDangerTableOpt();
        }

        List<ChannelDangerTableDataClass> dangers = channelDangerTableOpt.getRowWithinGeometry(polygonWKT);
        return dangers;
    }
}




