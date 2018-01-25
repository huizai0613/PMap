package xxzx.myData.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.R;
import xxzx.library.VectorDrawable;
import xxzx.myOverlay.PowelineOverLayOpt;
import xxzx.myView.ImgButtonVertical;
import xxzx.publicClass.LoadingDialog;
import xxzx.publicClass.MyActivityFragmentListener;
import xxzx.publicClass.MyListView.ListItemSelectState;
import xxzx.publicClass.MyListView.ListItemSelected;
import xxzx.publicClass.MyListView.ListItemSelectedAdapter;
import xxzx.publicClass.MyListView.ListViewCommonHolder;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.spatialite.TableOptClass.PowerlineTableOpt;
import xxzx.spatialite.TableStruct.PowerlineTableDataClass;


/**
 * Created by Daniel on 12/23/2014.
 */
public class OutputMapFragment extends Fragment implements ImgButtonVertical.ICoallBack{

    private Context mContext;
    /**
     * ListView对象
     */
    private ListView listview;

    private LinearLayout top;
    private LinearLayout bottom;
    private TextView tv_selectTitle;

    ImgButtonVertical imgbtn_select;
    ImgButtonVertical imgbtn_cancel;
    //ImgButtonVertical imgbtn_delete;
    ImgButtonVertical imgbtn_inputmap;
    ImgButtonVertical imgbtn_exit;


    private List<ListItemSelected> plineNameList;
    private ListItemSelectedAdapter listItemAdapter;

    //当前选择状态
    private ListItemSelectState selectState=ListItemSelectState.UNSELECT;

    //地图线路显示类
    private PowelineOverLayOpt powelineOverLayOpt=null;
    private LoadingDialog loadingDialog;
    private MyActivityFragmentListener.MyListener myListener;

    private boolean isfragmentCreate =false;

    public static OutputMapFragment newInstance() {
        OutputMapFragment f = new OutputMapFragment();
//        Bundle b = new Bundle();
//        b.putInt(ARG_POSITION, position);
//        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.isfragmentCreate=true;
        //获得数据
        this.getPowerlineNameList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== MyString.requestCode_mydataactivity_to_downloadplineactivity && resultCode == this.getActivity().RESULT_OK) {
            this.getPowerlineNameList();
            listItemAdapter.notifyDataSetChanged();
        }

        if(this.bottom!=null) {
            this.initSelectState(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map_pline, container, false);

        loadingDialog = new LoadingDialog(getContext());

        listview = (ListView) v.findViewById(R.id.listview);

        top = (LinearLayout) v.findViewById(R.id.top);
        bottom = (LinearLayout) v.findViewById(R.id.bottom);
        tv_selectTitle = (TextView) v.findViewById(R.id.tv_selectTitle);

        //top.setVisibility(View.GONE);
        //bottom.setVisibility(View.GONE);

        imgbtn_select =(ImgButtonVertical)v.findViewById(R.id.imgbtn_select);
        imgbtn_cancel =(ImgButtonVertical)v.findViewById(R.id.imgbtn_cancel);
        //imgbtn_delete =(ImgButtonVertical)v.findViewById(R.id.imgbtn_delete);
        imgbtn_inputmap =(ImgButtonVertical)v.findViewById(R.id.imgbtn_task);
        imgbtn_inputmap.setTitle("导入地图");
        imgbtn_exit =(ImgButtonVertical)v.findViewById(R.id.imgbtn_exit);

        imgbtn_select.setImage(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_select_un));
        imgbtn_cancel.setImage(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_select_un));
        //imgbtn_delete.setImage(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_select_un));
        imgbtn_inputmap.setImage(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_select_un));
        imgbtn_exit.setImage(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_select_un));

        imgbtn_select.setOnClick(this);
        imgbtn_cancel.setOnClick(this);
        //imgbtn_delete.setOnClick(this);
        imgbtn_inputmap.setOnClick(this);
        imgbtn_exit.setOnClick(this);


        this.initData();

        if(this.selectState==ListItemSelectState.UNSELECT){
            this.initSelectState(false);
        }else if(this.selectState==ListItemSelectState.SELECT){
            this.initSelectState(true);
        }

        return v;
    }

    /** Fragment第一次附属于Activity时调用,在onCreate之前调用 */
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        myListener = (MyActivityFragmentListener.MyListener)activity;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 重新获得数据
     */
    public void initPlineData(){

        if(!isfragmentCreate){
            return;
        }

        //获得数据
        this.getPowerlineNameList();
        if(this.listItemAdapter!=null) {
            this.listItemAdapter.notifyDataSetChanged();
        }
        if(this.bottom!=null) {
            this.initSelectState(false);
        }
    }


    /**
     * 初始化数据
     */
    private void initData() {
        //获取操作类
        if(powelineOverLayOpt==null){
            //获得电力线图层处理类
            powelineOverLayOpt = MySingleClass.getInstance().getBaseMapViewClass().getPowelineOverLayOpt();
        }
        listItemAdapter = new ListItemSelectedAdapter(getContext(), plineNameList);
        listview.setAdapter(listItemAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (selectState == ListItemSelectState.UNSELECT) {
//                    ListItemSelected itemClass = (ListItemSelected) parent.getAdapter().getItem(position);
//                    Intent intent = new Intent();
//                    intent.putExtra("plinename", itemClass.getTitle());
//                    intent.setClass(getContext(), LocChnAndPoleActivity.class);
//                    getContext().startActivity(intent);
                } else {
                    ListItemSelected itemClass = (ListItemSelected) parent.getAdapter().getItem(position);
                    ListViewCommonHolder viewHolder = (ListViewCommonHolder) view.getTag();

                    itemClass.setIsChecked(!itemClass.isChecked());
                    if (itemClass.isChecked()) {
                        viewHolder.iv_arrow.setBackground(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_select));
                    } else {
                        viewHolder.iv_arrow.setBackground(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_select_un));
                    }

                    //修改选中条目
                    setIsCheckedCountTitle();
                }
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (selectState == ListItemSelectState.UNSELECT) {
                    ListItemSelected itemClass = (ListItemSelected) parent.getAdapter().getItem(position);
                    ListViewCommonHolder viewHolder = (ListViewCommonHolder) view.getTag();

                    itemClass.setIsChecked(!itemClass.isChecked());
                    if (itemClass.isChecked()) {
                        viewHolder.iv_arrow.setBackground(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_select));
                    } else {
                        viewHolder.iv_arrow.setBackground(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_select_un));
                    }
                    initSelectState(true);
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.imgbtn_select:
                for(ListItemSelected item:plineNameList){
                    item.setIsChecked(true);
                }
                listItemAdapter.notifyDataSetChanged();
                setIsCheckedCountTitle();
                break;
            case R.id.imgbtn_cancel:
                for(ListItemSelected item:plineNameList){
                    item.setIsChecked(false);
                }
                listItemAdapter.notifyDataSetChanged();
                setIsCheckedCountTitle();
                break;
            case R.id.imgbtn_task:
                //选中的条目
                for(int i=0;i<listItemAdapter.getCount();i++){
                    ListItemSelected item = (ListItemSelected)listItemAdapter.getItem(i);

                    //修改电力线表，标识是否加入地图中
                    PowerlineTableOpt powerlineTableOpt = new PowerlineTableOpt();

                    if(item.isChecked()){
                        powerlineTableOpt.updateInMap(item.getTitle(), true);
                    }

                    //删除电力线路
                    powelineOverLayOpt.removePlineOverlay();
                    //添加电力线
                    powelineOverLayOpt.addPlineOverlay();
                }

                initSelectState(false);
                Toast.makeText(getContext(), "处理完成！", Toast.LENGTH_SHORT).show();

                myListener.initFragmentData();
                break;
            case R.id.imgbtn_exit:
                //重新读取数据
                getPowerlineNameList();
                //更新listview
                listItemAdapter.notifyDataSetChanged();
                initSelectState(false);
                break;
        }
    }


    private void initSelectState(boolean checked) {
        //底部工具条是否显示
        bottom.setVisibility(checked?View.VISIBLE:View.GONE);
        top.setVisibility(checked ? View.VISIBLE : View.GONE);
        this.setIsCheckedCountTitle();
        //设置当前选择状态
        this.selectState = checked? ListItemSelectState.SELECT:ListItemSelectState.UNSELECT;
    }


    /**
     * 获得电力线名称
     *
     * @return
     */
    private void getPowerlineNameList() {
        if (plineNameList == null) {
            plineNameList = new ArrayList<ListItemSelected>();
        }

        plineNameList.clear();

        PowerlineTableOpt powerlineTableOpt = new PowerlineTableOpt();
        List<Object> list_powerline = powerlineTableOpt.getRow();

        for (Object o : list_powerline) {
            ListItemSelected item = new ListItemSelected();
            item.setTitle(((PowerlineTableDataClass)o).getPowerName());

            //判断是否在地图中
            Boolean isselect = ((PowerlineTableDataClass)o).getIsInMap() == 1 ? true : false;
            if (isselect) {
                item.setIcon(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_pline_inmap));
            } else {
                item.setIcon(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_pline_inmap_un));
                item.setIsChecked(false);
                plineNameList.add(item);
            }

            //item.setIsChecked(isselect);
            //item.setIsChecked(false);

            //isChecked_init.add(isselect);
            //plineNameList.add(item);
        }
    }


    /**
     * 设置选中条目标题
     */
    private void setIsCheckedCountTitle(){
        int isCheckedCount = 0;
        for(int i=0;i<listItemAdapter.getCount();i++) {
            ListItemSelected item = (ListItemSelected) listItemAdapter.getItem(i);
            if (item.isChecked()) {
                isCheckedCount++;
            }
        }

        tv_selectTitle.setText("当前选中 " + isCheckedCount+ " 个");
    }

}




