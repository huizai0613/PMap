package xxzx.myData.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import xxzx.activity.MyData.LocChnAndPoleActivity;
import xxzx.activity.R;
import xxzx.deletePlineData.PlineDeleteClass;
import xxzx.library.VectorDrawable;

import xxzx.myOverlay.PowelineOverLayOpt;
import xxzx.myView.ImgButtonVertical;
import xxzx.publicClass.MyListView.ListItemSelected;
import xxzx.publicClass.MyListView.ListItemSelectedAdapter;
import xxzx.publicClass.MyListView.ListItemSelectState;
import xxzx.publicClass.MyListView.ListViewCommonHolder;
import xxzx.publicClass.LoadingDialog;
import xxzx.publicClass.MyActivityFragmentListener;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.ToastUtil;
import xxzx.spatialite.TableOptClass.PowerlineTableOpt;
import xxzx.spatialite.TableStruct.PowerlineTableDataClass;


/**
 * Created by Daniel on 12/23/2014.
 */
public class DataLocalFragment extends Fragment implements ImgButtonVertical.ICoallBack {

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
    ImgButtonVertical imgbtn_delete;
    ImgButtonVertical imgbtn_exit;


    private List<ListItemSelected> plineNameList;
    private ListItemSelectedAdapter listItemAdapter;

    //当前选择状态
    private ListItemSelectState selectState = ListItemSelectState.UNSELECT;

    //获得地图
    //private AMap aMap;
    //获得自定义图层列表
    //private Map<String, Object> overlayMap;

    //地图线路显示类
    private PowelineOverLayOpt powelineOverLayOpt = null;
    private LoadingDialog loadingDialog;
    private MyActivityFragmentListener.MyListener myListener;

    private boolean isfragmentCreate=false;

    public static DataLocalFragment newInstance() {
        DataLocalFragment f = new DataLocalFragment();
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

        if (requestCode == MyString.requestCode_mydataactivity_to_downloadplineactivity && resultCode == this.getActivity().RESULT_OK) {
            this.getPowerlineNameList();
            listItemAdapter.notifyDataSetChanged();
        }
        if(this.bottom!=null) {
            this.initSelectState(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mydata_local, container, false);

        loadingDialog = new LoadingDialog(getContext());

        listview = (ListView) v.findViewById(R.id.listview);

        top = (LinearLayout) v.findViewById(R.id.top);
        bottom = (LinearLayout) v.findViewById(R.id.bottom);
        tv_selectTitle = (TextView) v.findViewById(R.id.tv_selectTitle);


        imgbtn_select = (ImgButtonVertical) v.findViewById(R.id.imgbtn_select);
        imgbtn_cancel = (ImgButtonVertical) v.findViewById(R.id.imgbtn_cancel);
        imgbtn_delete = (ImgButtonVertical) v.findViewById(R.id.imgbtn_delete);
        imgbtn_exit = (ImgButtonVertical) v.findViewById(R.id.imgbtn_exit);

        imgbtn_select.setImage(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_select_un));
        imgbtn_cancel.setImage(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_select_un));
        imgbtn_delete.setImage(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_select_un));
        imgbtn_exit.setImage(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_select_un));

        imgbtn_select.setOnClick(this);
        imgbtn_cancel.setOnClick(this);
        imgbtn_delete.setOnClick(this);
        imgbtn_exit.setOnClick(this);


        this.initData();

        if (this.selectState == ListItemSelectState.UNSELECT) {
            this.initSelectState(false);
        } else if (this.selectState == ListItemSelectState.SELECT) {
            this.initSelectState(true);
        }

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
     * 重新获得数据
     */
    public void initPlineData() {

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
        if (powelineOverLayOpt == null) {
            //获得电力线图层处理类
            powelineOverLayOpt = MySingleClass.getInstance().getBaseMapViewClass().getPowelineOverLayOpt();
        }
        listItemAdapter = new ListItemSelectedAdapter(getContext(), plineNameList);
        listview.setAdapter(listItemAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectState == ListItemSelectState.UNSELECT) {
                    final ListItemSelected item = (ListItemSelected) parent.getAdapter().getItem(position);


                    String[] items = {"线路定位"};
                    //String[] items = {"线路定位","更新隐患点", "删除线路"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("请选择");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            List<String> listitems = new ArrayList<>();
                            listitems.add(item.getTitle());
                            switch (which) {
                                case 0:
                                    Intent intent = new Intent();
                                    intent.putExtra("plinename", item.getTitle());
                                    intent.setClass(getContext(), LocChnAndPoleActivity.class);
                                    getContext().startActivity(intent);
                                    break;
                                case 1:
                                    deletePlines(listitems);
                                    //删除电力线路
                                    powelineOverLayOpt.removePlineOverlay();
                                    //删除杆塔和通道
                                    powelineOverLayOpt.removePoleAndChannel();
                                    //添加电力线
                                    powelineOverLayOpt.addPlineOverlay();
                                    break;
                                default:
                                    break;
                            }
                            //更新界面
                            initPlineData();
                        }
                    });

                    builder.create().show();

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
        switch (view.getId()) {
            case R.id.imgbtn_select:
                for (ListItemSelected item : plineNameList) {
                    item.setIsChecked(true);
                }
                listItemAdapter.notifyDataSetChanged();
                setIsCheckedCountTitle();
                break;
            case R.id.imgbtn_cancel:
                for (ListItemSelected item : plineNameList) {
                    item.setIsChecked(false);
                }
                listItemAdapter.notifyDataSetChanged();
                setIsCheckedCountTitle();
                break;
            case R.id.imgbtn_delete:
                List<String> items = new ArrayList<>();

                for (int i = 0; i < listItemAdapter.getCount(); i++) {
                    ListItemSelected item = (ListItemSelected) listItemAdapter.getItem(i);
                    if (item.isChecked()) {
                        items.add(item.getTitle());
                    }
                }

                if(items.size()==0){
                    ToastUtil.show(getContext(),"当前选择的数量为0,不能执行该操作！");
                    return;
                }

                deletePlines(items);

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
        bottom.setVisibility(checked ? View.VISIBLE : View.GONE);
        top.setVisibility(checked ? View.VISIBLE : View.GONE);
        this.setIsCheckedCountTitle();
        //设置当前选择状态
        this.selectState = checked ? ListItemSelectState.SELECT : ListItemSelectState.UNSELECT;
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

//            Object overlay = overlayMap.get(p.getPowerName());
//            //判断是否在地图中
//            Boolean isselect = overlay != null ? true : false;
            //判断是否在地图中
            Boolean isselect = ((PowerlineTableDataClass)o).getIsInMap() == 1 ? true : false;
            if (isselect) {
                item.setIcon(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_pline_inmap));
            } else {
                item.setIcon(VectorDrawable.getDrawable(getContext(), R.drawable.iconfont_pline_inmap_un));
            }

            item.setIsChecked(false);

            plineNameList.add(item);
        }
    }


    private void setIsCheckedCountTitle() {
        int isCheckedCount = 0;
        for (int i = 0; i < listItemAdapter.getCount(); i++) {
            ListItemSelected item = (ListItemSelected) listItemAdapter.getItem(i);
            if (item.isChecked()) {
                isCheckedCount++;
            }
        }

        tv_selectTitle.setText("当前选中 " + isCheckedCount + " 个");
    }


    /**
     * @param plineNames
     */
    private void deletePlines(final List<String> plineNames) {
        String noplines = PlineDeleteClass.getNoDeletePlines(plineNames);

        if (!TextUtils.isEmpty(noplines)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("注意");
            builder.setMessage(noplines + " 有任务记录未上传，是否确定删除？");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setPositiveButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    delete(plineNames);
                }
            });
            builder.create().show();
        } else {
            delete(plineNames);
        }
    }

    private void delete(List<String> plineNames){
        boolean result = PlineDeleteClass.DeletePlines(plineNames);
        if (result) {
            ToastUtil.show(getContext(), "删除线路完成");
            initSelectState(false);
            //通知activity处理更新
            myListener.initFragmentData();
        } else {
            ToastUtil.show(getContext(), "删除线路失败");
        }
    }
}




