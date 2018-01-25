package xxzx.myData.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.MainDrawerActivity;
import xxzx.activity.R;
import xxzx.library.VectorDrawable;
import xxzx.publicClass.MyListView.ListItemOpt;
import xxzx.publicClass.GdMapTool;
import xxzx.publicClass.MyActivityManager;
import xxzx.publicClass.MyListView.ListItemPoleChnAdapter;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.geometryJson.WKT;
import xxzx.spatialite.TableOptClass.ChannelTableOpt;
import xxzx.spatialite.TableStruct.ChannelTableDataClass;


/**
 * Created by Daniel on 12/23/2014.
 */
public class LocationPlineChnFragment extends Fragment {

    private static String ARG_PLINENAME = "ARG_PLINENAME";
    private static String ARG_SHOWTYPE = "ARG_SHOWTYPE";

    private ArrayList<ListItemOpt> channelNameList;
    private ListItemPoleChnAdapter listItemAdapter;
    private String plineName;

    private ListView listview;
    private LinearLayout top;
    private LinearLayout bottom;

    /**
     * @param plinename
     * @return
     */
    public static LocationPlineChnFragment newInstance(String plinename) {
        LocationPlineChnFragment f = new LocationPlineChnFragment();
        Bundle b = new Bundle();
        b.putString(ARG_PLINENAME, plinename);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plineName = getArguments().getString(ARG_PLINENAME);
        this.getChannelNameList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_patrol_pline_pole, container, false);
        listview = (ListView) v.findViewById(R.id.listview);
        top = (LinearLayout) v.findViewById(R.id.top);
        bottom = (LinearLayout) v.findViewById(R.id.bottom);

        top.setVisibility(View.GONE);
        bottom.setVisibility(View.GONE);

        this.initData();
        return v;
    }


    /**
     * 初始化数据
     */
    private void initData() {
        listItemAdapter = new ListItemPoleChnAdapter(getContext(), channelNameList);
        listview.setAdapter(listItemAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItemOpt item = (ListItemOpt) parent.getAdapter().getItem(position);

                ChannelTableOpt channelTableOpt = new ChannelTableOpt();
                List<ChannelTableDataClass> list=channelTableOpt.getRowFromChnObjectId(item.getObjectid());

                if(list.size()>0){
                    List<LatLng> latlngs = WKT.POLYLINEWktToPtsList(list.get(0).getGeometry());
                    MySingleClass mySingleClass=MySingleClass.getInstance();
                    //地图放大平移
                    GdMapTool.zoomToSpan(mySingleClass.getBaseMapViewClass().getMapView().getMap(), latlngs);
                }

                //结束activity
                MyActivityManager.getMyActivityManager().finishAllActivity();

                Intent intent = new Intent();
                intent.putExtra("state","");
                intent.putExtra("data","");
                intent.setClass(getContext(), MainDrawerActivity.class);
                getContext().startActivity(intent);
            }
        });
    }



    private void getChannelNameList() {

        if (channelNameList == null) {
            channelNameList = new ArrayList<ListItemOpt>();
        }

        channelNameList.clear();

        ChannelTableOpt channelTableOpt = new ChannelTableOpt();
        List<ChannelTableDataClass> channels = channelTableOpt.getRow(plineName);

        for (ChannelTableDataClass channel : channels) {
            ListItemOpt listItemOpt = new ListItemOpt();
            listItemOpt.setOptName(channel.getChannelName());
            listItemOpt.setObjectid(channel.getChannelObjectId());
            listItemOpt.setIcon(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_powerline_channel));

            channelNameList.add(listItemOpt);
        }
    }


}




