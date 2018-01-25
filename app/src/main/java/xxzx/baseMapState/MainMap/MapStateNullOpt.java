package xxzx.baseMapState.MainMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import xxzx.activity.DangerRadarActivity;
import xxzx.activity.Patrol.ChnDangerStatisticsActivity;
import xxzx.activity.Patrol.MyTask.ChnDangerUpdateActivity;
import xxzx.activity.Patrol.MyTask.MyTaskPowerlineActivity;
import xxzx.activity.Patrol.PoleDangerStatisticsActivity;
import xxzx.activity.Patrol.TaskSetting.PatrolPowerlineActivity;
import xxzx.activity.Patrol.Record.RecordChnAndPoleActivity;
import xxzx.activity.R;
import xxzx.activity.PoiAndRoute.RoutePlanningActivity;
import xxzx.deletePlineData.PlineUpdateClass;
import xxzx.library.VectorDrawable;
import xxzx.login.CheckLogin;
import xxzx.login.LoginState;
import xxzx.myBdLocation.AroudFriends.NbUserDlgFragment;
import xxzx.myBdLocation.AroudFriends.NbUserInfo;
import xxzx.myBdLocation.AroudFriends.NearbyUsersClass;
import xxzx.myOverlay.DangerRadarOverlay;
import xxzx.myView.ImgBaseButton;
import xxzx.myView.ImgButton;
import xxzx.myView.ImgDangerNum;
import xxzx.myView.ImgButtonHorizontal;
import xxzx.mylibrary.PromotedActionsLibrary;
import xxzx.dangerRadar.DangerRadarClass;
import xxzx.publicClass.MySingleClass;
import xxzx.publicClass.MyString;
import xxzx.publicClass.ToastUtil;
import xxzx.spatialite.TableOptClass.ChannelDangerTableOpt;
import xxzx.spatialite.TableOptClass.ChannelTableOpt;
import xxzx.spatialite.TableOptClass.PoleDangerTableOpt;
import xxzx.spatialite.TableOptClass.PowerlineTableOpt;
import xxzx.spatialite.TableStruct.ChannelDangerTableDataClass;
import xxzx.spatialite.TableStruct.ChannelTableDataClass;
import xxzx.spatialite.TableStruct.PoleDangerTableDataClass;
import xxzx.spatialite.TableStruct.PoleTableDataClass;
import xxzx.spatialite.TableStruct.PowerlineTableDataClass;


/**
 * Created by ch on 2016/1/20.
 */

/**
 * 起始状态，也就是不做任何操作的状态
 *
 * @author Administrator
 */
public class MapStateNullOpt extends BaseMainMapState implements ActionMenuView.OnMenuItemClickListener {
    // 布局文件
    private DrawerLayout drawer = null;
    private Toolbar toolbar = null;
    private ImgDangerNum imgbtn_dangerupload = null;
    private ImgButton imgbtn_nearbyUsers = null;
    private PromotedActionsLibrary promotedActionsLibrary = null;

    private String btn_upload="btn_upload";
    private String btn_download="btn_download";

    private NearbyUsersClass nearbyUsersClass;


    // 构造函数
    public MapStateNullOpt(Context context, AppCompatDelegate delegate, AppBarLayout appBarLayout, LinearLayout bottomContainer, Object _mData) {
        super.BaseMainMapState(context, delegate, appBarLayout, bottomContainer, _mData);
    }


    @Override
    protected void initView() {
        // TODO Auto-generated method stub

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(imgbtn_dangerupload!=null){
            imgbtn_dangerupload.initData();
        }
    }

    @Override
    protected void initToolBar() {
        // TODO Auto-generated method stub
        LayoutInflater flater = LayoutInflater.from(this.mContext);
        View view_toolbar = flater.inflate(R.layout.public_base_toolbar_map, null);
        if (toolbar == null) {
            toolbar = (Toolbar) view_toolbar.findViewById(R.id.toolbar);
            TextView tv_title_toobar = (TextView) view_toolbar.findViewById(R.id.tv_title_toobar);
            // Title
            toolbar.setTitle("");
            tv_title_toobar.setText(R.string.name_tool_bar_null_opt);

            toolbar.setTitleTextColor(Color.WHITE);
            this.delegate.setSupportActionBar(toolbar);
            // Navigation Icon 要設定在 setSupoortActionBar 才有作用
            // 否則會出現 back button
            toolbar.setNavigationIcon(R.mipmap.toolbar_navigation);

            //设置点击事件
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        drawer.openDrawer(GravityCompat.START);
                    }
                }
            });

            //创建工具条菜单
            ActionMenuView actionMenuView = this.getActionMenuView(toolbar);

            Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.RIGHT;
            toolbar.addView(actionMenuView, layoutParams);
        }

        appBarLayout.removeAllViews();
        appBarLayout.addView(toolbar);
    }

    @Override
    protected void initbottomBar() {
        // TODO Auto-generated method stub
        LayoutInflater flater = LayoutInflater.from(this.mContext);
        View view = flater.inflate(R.layout.bottom_map_state_nullopt, null);

        ImgButtonHorizontal imgbtn_patrol = (ImgButtonHorizontal) view.findViewById(R.id.imgbtn_patrol);
        ImgButtonHorizontal imgbtn_edit = (ImgButtonHorizontal) view.findViewById(R.id.imgbtn_edit);
        ImgButtonHorizontal imgbtn_navi = (ImgButtonHorizontal) view.findViewById(R.id.imgbtn_navi);
        ImgButtonHorizontal imgbtn_tools = (ImgButtonHorizontal) view.findViewById(R.id.imgbtn_tools);

        //ImgButton imgbtn_dangerRadar = (ImgButton) view.findViewById(R.id.imgbtn_dangerRadar);
        imgbtn_nearbyUsers = (ImgButton) view.findViewById(R.id.imgbtn_nearbyUsers);

        imgbtn_patrol.setImage(VectorDrawable.getDrawable(mContext, R.drawable.iconfont_location));
        imgbtn_edit.setImage(VectorDrawable.getDrawable(mContext, R.drawable.iconfont_edit));
        imgbtn_navi.setImage(VectorDrawable.getDrawable(mContext, R.drawable.iconfont_navi));
        imgbtn_tools.setImage(VectorDrawable.getDrawable(mContext, R.drawable.iconfont_tool));

        imgbtn_nearbyUsers.setImage(VectorDrawable.getDrawable(mContext,R.drawable.iconfont_map_nearby_users));

        imgbtn_patrol.setOnClick(new ImgbtnOnClickListener());
        imgbtn_edit.setOnClick(new ImgbtnOnClickListener());
        imgbtn_navi.setOnClick(new ImgbtnOnClickListener());
        imgbtn_tools.setOnClick(new ImgbtnOnClickListener());
        imgbtn_nearbyUsers.setOnClick(new ImgbtnOnClickListener());

        //初始化上传按钮
        imgbtn_dangerupload = (ImgDangerNum) view.findViewById(R.id.imgbtn_dangerupload);

        this.bottomContainer.removeAllViews();
        this.bottomContainer.addView(view);

        this.initFloatButtonView(view);
    }

    @Override
    protected void initData() {
        // TODO Auto-generated method stub
        //打开drawer的手势滑动
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        //定义地图点击事件
        final MySingleClass mySingleClass = MySingleClass.getInstance();
        AMap amap = mySingleClass.getBaseMapViewClass().getMapView().getMap();
        amap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                String markerTitleType = (String) marker.getTitle();

                if (TextUtils.equals(markerTitleType, MyString.maker_title_pole_chn)) {

                    Object object = (Object) marker.getObject();

                    if(object instanceof PoleTableDataClass){

                        PoleTableDataClass poleTableDataClass = (PoleTableDataClass)object;

                        Intent intent = new Intent();
                        intent.putExtra("polename", poleTableDataClass.getPoleName());
                        intent.putExtra("poleobjectid", poleTableDataClass.getPoleObjectId());
                        intent.putExtra("plinename", poleTableDataClass.getPowerName());
                        intent.putExtra("istaskdanger", poleTableDataClass.getIsSelect() == 0?false:true);
                        intent.setClass(mContext, PoleDangerStatisticsActivity.class);
                        mContext.startActivity(intent);
                        if(poleTableDataClass.getIsSelect() == 0) {
                            ToastUtil.show(mContext, "该杆塔没有添加到巡查任务中,不能进行任务操作！");
                        }

                    }else if(object instanceof ChannelTableDataClass){

                        ChannelTableDataClass channelTableDataClass = (ChannelTableDataClass) object;

                        Intent intent = new Intent();
                        intent.putExtra("chnname", channelTableDataClass.getChannelName());
                        intent.putExtra("plinename", channelTableDataClass.getPowerName());
                        intent.putExtra("chnobjectid", channelTableDataClass.getChannelObjectId());
                        intent.putExtra("istaskdanger", channelTableDataClass.getIsSelect() == 0?false:true);
                        intent.setClass(mContext, ChnDangerStatisticsActivity.class);
                        mContext.startActivity(intent);
                        if(channelTableDataClass.getIsSelect() == 0) {
                            ToastUtil.show(mContext, "该通道没有添加到巡查任务中,不能进行任务操作！");
                        }
                    }
                }

                //周边人员图标点击
                if(TextUtils.equals(markerTitleType, MyString.maker_title_nearby_user)){

                    NbUserInfo  userinfo = (NbUserInfo) marker.getObject();

                    NbUserDlgFragment nbUserDlgFragment = NbUserDlgFragment.newInstance(userinfo.name,userinfo.telephone);

                    nbUserDlgFragment.show(((Activity)mContext).getFragmentManager(),"NbUserDlgFragment");
                }

                return true;
            }
        });

    }


    @Override
    protected void BackKeyDown() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        //退到后台
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        mContext.startActivity(intent);

    }


    @Override
    protected void Close() {
        //关闭drawer的手势滑动
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //清空底部容器
        this.bottomContainer.removeAllViews();
    }


    /**
     * toolbar上面的菜单点击事件
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        String msg = "";
        switch (menuItem.getItemId()) {

        }
        return true;
    }


    public void setDrawer(DrawerLayout drawer) {
        this.drawer = drawer;
    }

    /**
     * 创建工具条菜单
     *
     * @return
     */
    private ActionMenuView getActionMenuView(Toolbar toolbar) {

        //创建工具条菜单
        ActionMenuView actionMenuView = new ActionMenuView(this.mContext);
        actionMenuView.getMenu().clear();
        ((Activity) this.mContext).getMenuInflater().inflate(R.menu.menu_map_nullopt, actionMenuView.getMenu());
        //判断菜单是否存在，若存在则删除
        View view_menu = toolbar.getChildAt(this.toolBarMenuID);
        if (view_menu != null) {
            toolbar.removeViewAt(this.toolBarMenuID);
        }
        actionMenuView.setOnMenuItemClickListener(this);

        return actionMenuView;
    }


    /**
     * 初始化FloatButton按钮
     * @param view
     */
    private void initFloatButtonView(View view){
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.container);
        this.promotedActionsLibrary = new PromotedActionsLibrary(this.imgbtn_dangerupload);
        this.promotedActionsLibrary.setup(mContext, frameLayout);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断登录状态
                if (!CheckLogin.isLogin(mContext)) {
                    return;
                }
                //重新下载隐患信息
                if (String.valueOf(view.getTag()).equals(btn_download)) {

                    //检查是否有为上传的隐患信息，并提示
                    final PoleDangerTableOpt poleDangerTableOpt = new PoleDangerTableOpt();
                    ChannelDangerTableOpt channelDangerTableOpt = new ChannelDangerTableOpt();
                    List<PoleDangerTableDataClass> list_poledanger = poleDangerTableOpt.getRowFromUpdate();
                    List<ChannelDangerTableDataClass> list_chndanger = channelDangerTableOpt.getRowFromUpdate();

                    String message = "更新隐患点将清空本地保存的所有隐患点信息，确定要更新吗？";
                    if (list_poledanger.size() > 0 || list_chndanger.size() > 0) {
                        message = "有未上传的隐患点信息，更新后将删除该隐患点信息，确定更新吗？";
                    }

                    AlertDialog.Builder build = new AlertDialog.Builder(mContext);
                    build.setTitle("注意").setMessage(message)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO
                                    try {
                                        List<String> list_plinename = new ArrayList<String>();
                                        //先读取已有线路
                                        PowerlineTableOpt powerlineTableOpt = new PowerlineTableOpt();
                                        List<Object> list = powerlineTableOpt.getRow();

                                        if (list.size() == 0) {
                                            ToastUtil.show(mContext, "当前线路数量为0，无法更新！");
                                            return;
                                        }

                                        List<Integer> isInMapList = new ArrayList<Integer>();
                                        List<Integer> isTaskList = new ArrayList<Integer>();
                                        for (Object o : list) {
                                            String name = ((PowerlineTableDataClass) o).getPowerName();
                                            list_plinename.add(name);
                                            isInMapList.add(((PowerlineTableDataClass) o).getIsInMap());
                                            isTaskList.add(((PowerlineTableDataClass) o).getIsSelect());
                                        }

                                        PlineUpdateClass plineUpdateClass = new PlineUpdateClass(mContext);
                                        plineUpdateClass.list_plinename=list_plinename;
                                        plineUpdateClass.isTaskList=isTaskList;
                                        plineUpdateClass.isInMapList=isTaskList;
                                        plineUpdateClass.update();

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
                if (String.valueOf(view.getTag()).equals(btn_upload)) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, RecordChnAndPoleActivity.class);
                    ((Activity) mContext).startActivityForResult(intent, 0);
                }
            }
        };



        this.promotedActionsLibrary.addItem(mContext.getResources().getDrawable(R.mipmap.fab_download),btn_download, onClickListener);
        this.promotedActionsLibrary.addItem(mContext.getResources().getDrawable(R.mipmap.fab_upload),btn_upload, onClickListener);
        this.promotedActionsLibrary.addMainItem(mContext.getResources().getDrawable(R.mipmap.toolbar_add));

    }


    /**
     * 按钮点击监听器
     */
    private class ImgbtnOnClickListener implements ImgBaseButton.ICoallBack {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            if(MySingleClass.getInstance().getLoginState()== LoginState.FAIL_LOGIN){
                ToastUtil.show(mContext,"当前未登录状态，不能操作");
                return;
            }


            Intent intent;
            AlertDialog.Builder builder;
            switch (v.getId()) {
                case R.id.imgbtn_edit:
                    MapStateEdit _MapStateEdit = new MapStateEdit(mContext, delegate, appBarLayout, bottomContainer, null);
                    MySingleClass.getInstance().getMapStateContext().setState(_MapStateEdit);
                    MySingleClass.getInstance().getMapStateContext().InitViewAndData();
                    break;
                case R.id.imgbtn_tools:

                    builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("工具选择");
                    builder.setItems(new String[]{"地图测量", "其他"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    MapStateMeasure _MapStateMeasure = new MapStateMeasure(mContext, delegate, appBarLayout, bottomContainer, null);
                                    MySingleClass.getInstance().getMapStateContext().setState(_MapStateMeasure);
                                    MySingleClass.getInstance().getMapStateContext().InitViewAndData();
                                    break;
                                case 1:
                                    break;
                            }
                        }
                    });
                    builder.show();
                    break;
                case R.id.imgbtn_navi:
                    intent = new Intent();
                    intent.setClass(mContext, RoutePlanningActivity.class);
                    mContext.startActivity(intent);
                    break;
                case R.id.imgbtn_patrol:
                    builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("请选择");
                    //builder.setIcon(android.R.drawable.ic_dialog_info)
                    builder.setItems(new String[]{"任务设置", "我的任务", "巡检记录"},
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent;
                                    switch (which) {
                                        case 0:
                                            intent = new Intent();
                                            intent.setClass(mContext, PatrolPowerlineActivity.class);
                                            mContext.startActivity(intent);
                                            break;
                                        case 1:
                                            intent = new Intent();
                                            intent.setClass(mContext, MyTaskPowerlineActivity.class);
                                            ((Activity) mContext).startActivityForResult(intent, 0);
                                            break;
                                        case 2:
                                            intent = new Intent();
                                            intent.setClass(mContext, RecordChnAndPoleActivity.class);
                                            ((Activity) mContext).startActivityForResult(intent, 0);
                                            break;
                                    }
                                }
                            }
                    );
                    builder.show();
                    break;
                case R.id.imgbtn_nearbyUsers:
//                    DangerRadarClass dangerRadarClass =new DangerRadarClass(mContext);
//                    dangerRadarClass.addDangersToMap();
//                    intent = new Intent();
//                    intent.setClass(mContext, DangerRadarActivity.class);
//                    mContext.startActivity(intent);

                    if(nearbyUsersClass==null){
                        nearbyUsersClass = new NearbyUsersClass(mContext);
                    }

                    nearbyUsersClass.Switch();

                    break;
            }
        }
    }


}
