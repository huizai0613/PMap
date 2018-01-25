package xxzx.publicClass;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.navi.model.AMapNaviPath;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import xxzx.baseMapState.MainMap.MapStateContext;
import xxzx.login.LoginState;
import xxzx.login.User;
import xxzx.mapLayerOpt.BaseMapViewClass;
import xxzx.spatialite.SpatialiteDataOpt;

/**
 * Created by mulin on 2015/9/29.
 */
public class MySingleClass {

    /**
     * 上下文
     */
    private Context mContext=null;

    /**
     * 地图状态上下文
     */
    private MapStateContext mapStateContext = null;

    /**
     * 地图类
     */
    private BaseMapViewClass baseMapViewClass = null;

    /**
     * 定位信息
     */
    private AMapLocation myGdLocation = null;

    /**
     * 地图覆盖物的类，存储在主图上覆盖的覆盖物
     */
    private Map<String,Object> overlayMap = new HashMap<>();

    /**
     * 用户
     */
    private User user=null;

    /**
     * 登录状态
     */
    private int loginState = LoginState.FAIL_LOGIN;

    /**
     * 配置文件
     */
    private Properties properties=null;

    /**
     * 是否记录当前的gps路径信息
     */
    private Boolean isRecordGPS=false;

    /**
     * 数据库操作类
     */
    private SpatialiteDataOpt spatialiteDataOpt = null;

    /**
     * 主地图的范围视图
     */
    private CameraPosition mainMapCameraPosition = null;


    /**
     * 导航路径类，用于activity之间共享
     */
    private AMapNaviPath naviPath=null;




    private static MySingleClass mInstance = new MySingleClass();
    // 构造函数
    private MySingleClass() {

    }

    public static MySingleClass getInstance() {
        if (mInstance == null) {
            mInstance = new MySingleClass();
        }
        return mInstance;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public BaseMapViewClass getBaseMapViewClass() {
        return baseMapViewClass;
    }

    public void setBaseMapViewClass(BaseMapViewClass baseMapViewClass) {
        this.baseMapViewClass = baseMapViewClass;
    }

    public AMapLocation getMyGdLocation() {
        return myGdLocation;
    }

    public void setMyGdLocation(AMapLocation myGdLocation) {
        this.myGdLocation = myGdLocation;
    }

    /**
     * 用户信息保存
     * @return
     */
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getLoginState() {
        return loginState;
    }

    public void setLoginState(int loginState) {
        this.loginState = loginState;
    }

    public MapStateContext getMapStateContext() {
        return mapStateContext;
    }

    public void setMapStateContext(MapStateContext mapStateContext) {
        this.mapStateContext = mapStateContext;
    }


//    public Map<String, Object> getOverlayMap() {
//        return overlayMap;
//    }

//    public void setOverlayMap(Map<String, Object> overlayMap) {
//        this.overlayMap = overlayMap;
//    }


    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Boolean getIsRecordGPS() {
        return isRecordGPS;
    }

    public void setIsRecordGPS(Boolean isRecordGPS) {
        this.isRecordGPS = isRecordGPS;
    }

    public SpatialiteDataOpt getSpatialiteDataOpt() {
        return spatialiteDataOpt;
    }

    public void setSpatialiteDataOpt(SpatialiteDataOpt spatialiteDataOpt) {
        this.spatialiteDataOpt = spatialiteDataOpt;
    }

    public CameraPosition getMainMapCameraPosition() {
        return mainMapCameraPosition;
    }

    public void setMainMapCameraPosition(CameraPosition mainMapCameraPosition) {
        this.mainMapCameraPosition = mainMapCameraPosition;
    }

    public AMapNaviPath getNaviPath() {
        return naviPath;
    }

    public void setNaviPath(AMapNaviPath naviPath) {
        this.naviPath = naviPath;
    }

    /**
     * 程序关闭时，清空
     */
    public void ExitApp() {

        if (spatialiteDataOpt != null) {
            spatialiteDataOpt.close();
        }
        if(overlayMap!=null){
            overlayMap.clear();
        }

//        currentLocation = null;// 记录当前的定位点坐标信息

//        mapView = null;// 记录地图控件

//        mapSheetInfo = null;// 当前工作任务的图幅
//
//        BaseMapName = null;// 当前工作底图名称
//
//        ShpSpatialiteDB = null;// Shp空间数据库
//
//        shpFileInfoList = null;

    }
}
