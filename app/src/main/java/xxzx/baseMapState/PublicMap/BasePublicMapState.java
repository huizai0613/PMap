package xxzx.baseMapState.PublicMap;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatDelegate;
import android.widget.LinearLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xxzx.baseMapState.MainMap.MapStateContext;
import xxzx.mapLayerOpt.BaseMapViewClass;
import xxzx.mapLayerOpt.PublicBaseMapViewClass;
import xxzx.myOverlay.MyBaseOverlay;
import xxzx.publicClass.GdMapTool;
import xxzx.publicClass.MySingleClass;

/**
 * 地图控件的操作状态接口
 *
 * @author Administrator
 */
public abstract class BasePublicMapState implements AMap.OnMapLoadedListener{

    protected AppBarLayout appBarLayout = null;//toolbar容器
    protected LinearLayout bottomContainer = null;// 底部容器父类
    protected Object mData = null;// 当前处理的数据
    protected Context mContext = null;//上下文
    protected AppCompatDelegate delegate = null;//代理
    protected int toolBarMenuID = 3;//工具条上菜单索引,这个值是固定不变的
    protected MapStateContext stateContext = null;// 管理状态的上下文

    protected PublicBaseMapViewClass publicBaseMapViewClass;
    protected AMap aMap;

    /**
     * 构造函数
     * <p/>
     * 地图控件
     *
     * @param _mData 处理的参数
     */
    void BasePublicMapState(Context context, AppCompatDelegate delegate, AppBarLayout appBarLayout, LinearLayout bottomContainer, Object _mData) {
        this.mContext = context;
        this.delegate = delegate;
        this.appBarLayout = appBarLayout;
        this.mData = _mData;
        this.bottomContainer = bottomContainer;
        this.initView();
        this.initToolBar();
        this.initbottomBar();

    }

    public void setBaseMapView(PublicBaseMapViewClass publicBaseMapViewClass){
        this.publicBaseMapViewClass = publicBaseMapViewClass;
        this.aMap = this.publicBaseMapViewClass.getMapView().getMap();

        this.initData();

        this.aMap.setOnMapLoadedListener(this);
    }

    protected void setContext(MapStateContext context) {
        this.stateContext = context;
    }

    /**
     * 返回键点击事件
     */
    public void BackKeyDown(){
        //主界面地图平移到这个位置
        //
        //
        //

        ((Activity)mContext).setResult(Activity.RESULT_CANCELED, null);
        ((Activity)mContext).finish();
    }

    /**
     * 地图定位到当前位置,这个函数必须在地图加载完成以后执行
     */
    private void CameraLocation(){
        MySingleClass mySingleClass = MySingleClass.getInstance();
        AMap amap = mySingleClass.getBaseMapViewClass().getMapView().getMap();
        if(this.aMap!=null) {
            this.aMap.moveCamera(CameraUpdateFactory.newCameraPosition(mySingleClass.getMainMapCameraPosition()));
        }
    }

    /**
     * 加载当前地图主图的数据
     */
    private void addMainMapData() {
//        MySingleClass mySingleClass = MySingleClass.getInstance();
//        Map<String, Object> map = mySingleClass.getOverlayMap();
//
//        for (Object v : map.values()) {
//            if (v instanceof MyBaseOverlay) {
//                ((MyBaseOverlay) v).addToMap(this.aMap);
//
//            }
//        }
    }


    /**
     * 初始化空间布局
     */
    @Override
    public void onMapLoaded() {
        //移动到定位点
        this.CameraLocation();
        //添加主地图上的图层
        this.addMainMapData();
        //各子类实例的操作
        this.onMapSubOption();

    }


    /**
     * 各子类实例对应的地图操作
     */
    public  abstract void onMapSubOption();

    /**
     * 初始化空间布局
     */
    protected abstract void initView();

    /**
     * 初始化头部工具条函数
     */
    protected abstract void initToolBar();

    /**
     * 初始化底部工具条函数
     */
    protected abstract void initbottomBar();

    /**
     * 初始化函数
     */
    protected abstract void initData();

    /**
     * 关闭当前状态的函数
     */
    protected abstract void Close();

}
