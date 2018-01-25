package xxzx.patrol.Record;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jsqlite.Stmt;
import xxzx.activity.R;
import xxzx.activity.Update.UpdateDangerActivity;
import xxzx.library.VectorDrawable;
import xxzx.publicClass.DangerVersion;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.spatialite.TableStruct.ChannelDangerTableColumn;
import xxzx.spatialite.TableStruct.PoleDangerTableColumn;
import xxzx.upload.ListItemUpdateChnPoleAdapter;
import xxzx.upload.ListItemUpdateChnPoleClass;


/**
 * Created by Daniel on 12/23/2014.
 */
public class RecordChnFragment extends Fragment {

    private static String ARG_PLINENAME="ARG_PLINENAME";
    private static String ARG_SHOWTYPE="ARG_SHOWTYPE";

    private ArrayList<ListItemUpdateChnPoleClass> channelNameList;
    private ListItemUpdateChnPoleAdapter listitemAdapter;

    private ListView listview;
    private LinearLayout top;
    private LinearLayout bottom;


    public static RecordChnFragment newInstance() {
        RecordChnFragment f = new RecordChnFragment();
//        Bundle b = new Bundle();
//        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItemUpdateChnPoleClass item = (ListItemUpdateChnPoleClass) parent.getAdapter().getItem(position);
                Intent intent = new Intent();
                intent.putExtra("channelorpolename", item.getTitle());
                intent.putExtra("objectid", item.getObjectid());
                intent.putExtra("chnorpoletype", ChnOrPole.CHANNEL);//0,标示通道
                intent.setClass(getContext(), UpdateDangerActivity.class);
                startActivityForResult(intent,0);
            }
        });


        //初始化数据
        this.initData();

        return v;
    }

    /**
     * 初始化数据
     */
    public void initData() {

        boolean result = this.getChannelNameList();
        if(!result){
            //ToastUtil.show(getContext(), "没有查询到通道的巡检记录");
        }
        listitemAdapter = new ListItemUpdateChnPoleAdapter(getContext(), channelNameList);
        listview.setAdapter(listitemAdapter);
    }

    /**
     * 获得具有更新的杆塔名称
     *
     * @return
     */
    private boolean getChannelNameList() {

        MySingleClass mySingleClass = MySingleClass.getInstance();
        StringBuilder sql = new StringBuilder("");

        if (this.channelNameList == null) {
            this.channelNameList = new ArrayList<ListItemUpdateChnPoleClass>();
        }
        this.channelNameList.clear();

        sql.append("SELECT ");
        sql.append(ChannelDangerTableColumn.ChannelName);
        sql.append(",");
        sql.append(ChannelDangerTableColumn.ChannelObjectId);

        sql.append(" FROM " + MyString.channel_danger_table_name);
        sql.append(" WHERE " + PoleDangerTableColumn.Version + " != "+ DangerVersion.DANGER_VERSION_INIT);

        Stmt stmt = mySingleClass.getSpatialiteDataOpt().QueryExecute(sql.toString());
        //解析stmt
        boolean result = this.addQueryResult(stmt);
        return result;
    }


    /**
     * 解析数据库查询出来的信息
     *
     * @param stmt
     */
    private boolean addQueryResult(Stmt stmt) {

        try {
            Map<Integer,Integer> map_id_count=new HashMap<>();
            Map<Integer,String> map_id_name=new HashMap<>();


            while (stmt.step()) {
                String name = stmt.column_string(0);
                Integer objectid = stmt.column_int(1);
                if(map_id_count.containsKey(objectid)){
                    Integer count=map_id_count.get(objectid);
                    map_id_count.put(objectid,count+1);
                }else{
                    map_id_count.put(objectid,1);
                }


                if(!map_id_name.containsKey(objectid)){
                    map_id_name.put(objectid,name);
                }

            }

            stmt.close();

            if (map_id_count.size() > 0) {
                for (Integer key : map_id_count.keySet()) {

                    ListItemUpdateChnPoleClass optItemClass = new ListItemUpdateChnPoleClass();
                    optItemClass.setTitle(map_id_name.get(key));
                    optItemClass.setObjectid(key);
                    optItemClass.setDangerNum(map_id_count.get(key));
                    optItemClass.setIcon(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_powerline_channel));
                    channelNameList.add(optItemClass);
                }
                return true;
            } else {
                return false;
            }
        } catch (jsqlite.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }


    }


}




