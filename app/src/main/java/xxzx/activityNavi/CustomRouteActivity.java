package xxzx.activityNavi;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviException;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;

import java.util.ArrayList;

import xxzx.activity.R;

/**
 * 创建时间：11/10/15 17:08
 * 项目名称：newNaviDemo
 *
 * @author lingxiang.wang
 * @email lingxiang.wang@alibaba-inc.com
 * 类说明：
 */

public class CustomRouteActivity extends BaseActivity implements AMapNaviListener
{

    NaviLatLng wayPoint = new NaviLatLng(39.935041, 116.447901);
    NaviLatLng wayPoint1 = new NaviLatLng(39.945041, 116.447901);
    NaviLatLng wayPoint2 = new NaviLatLng(39.955041, 116.447901);
    NaviLatLng wayPoint3 = new NaviLatLng(39.965041, 116.447901);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWayPointList = new ArrayList<NaviLatLng>();

        setContentView(R.layout.activity_navi_basic);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        //关闭自动绘制路线（如果你想自行绘制路线的话，必须关闭！！！）
        //PS:必须在onCreate之前
        mAMapNaviView.getViewOptions().setAutoDrawRoute(false);
        mAMapNaviView.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        mWayPointList.add(wayPoint);
        mWayPointList.add(wayPoint1);
        mWayPointList.add(wayPoint2);
        mWayPointList.add(wayPoint3);

        super.onResume();
    }


    @Override
    public void onCalculateRouteSuccess() {
//        如果根据获取的导航路线来自定义绘制
        RouteOverLay routeOverlay = new RouteOverLay(mAMapNaviView.getMap(), mAMapNavi.getNaviPath(), this);
        routeOverlay.setStartPointBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.r1));
        routeOverlay.setEndPointBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.b1));
        routeOverlay.setWayPointBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.b2));
        try {
            routeOverlay.setWidth(30);
        } catch (AMapNaviException e) {
            //宽度须>0
            e.printStackTrace();
        }
        int color[] = new int[10];
        color[0] = Color.BLACK;
        color[1] = Color.RED;
        color[2] = Color.BLUE;
        color[3] = Color.YELLOW;
        color[4] = Color.GRAY;
        routeOverlay.addToMap(color, mAMapNavi.getNaviPath().getWayPointIndex());


        mAMapNavi.startNavi(AMapNavi.EmulatorNaviMode);
    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo){

    }


}
