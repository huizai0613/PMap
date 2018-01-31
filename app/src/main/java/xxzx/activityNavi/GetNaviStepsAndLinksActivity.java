package xxzx.activityNavi;

import android.os.Bundle;
import android.util.Log;

import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.model.AMapNaviGuide;
import com.amap.api.navi.model.AMapNaviLink;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviStep;
import com.amap.api.navi.model.NaviLatLng;

import java.util.List;

import xxzx.activity.R;

/**
 * 创建时间：15/12/15 17:16
 * 项目名称：newNaviDemo
 *
 * @author lingxiang.wang
 * @email lingxiang.wang@alibaba-inc.com
 * 类说明：
 */

public class GetNaviStepsAndLinksActivity extends BaseActivity
{


    private AMapNaviPath mAMapNaviPath;
    private List<AMapNaviStep> steps;
    private List<AMapNaviLink> links;
    private List<AMapNaviGuide> guides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mEndLatlng = new NaviLatLng(45.742367, 126.661665);
        mStartLatlng = new NaviLatLng(22.373594, 113.562575);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navi_basic);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);


    }


    @Override
    public void onCalculateRouteSuccess() {
        super.onCalculateRouteSuccess();

        //概览
        guides = mAMapNavi.getNaviGuideList();
        for (AMapNaviGuide guide : guides) {
            Log.d("wlx", "路线经纬度:" + guide.getCoord() + "");
            Log.d("wlx", "路线名:" + guide.getName() + "");
            Log.d("wlx", "路线长:" + guide.getLength() + "m");
            Log.d("wlx", "路线耗时:" + guide.getTime() + "s");
            Log.d("wlx", "路线IconType" + guide.getIconType() + "");
        }

        //详情
        mAMapNaviPath = mAMapNavi.getNaviPath();
        steps = mAMapNaviPath.getSteps();
        for (AMapNaviStep step : steps) {
            Log.d("wlx", "该step:" + step.getLength() + "m" + " " + "耗时:" + step.getTime() + "s");
            links = step.getLinks();
            for (AMapNaviLink link : links) {
//          请看com.amap.api.navi.enums.RoadClass，以及帮助文档
                Log.d("wlx", "该道名:" + link.getRoadName() + " " + "道路等级:" + link.getRoadClass());
//          请看com.amap.api.navi.enums.RoadType，以及帮助文档
                Log.d("wlx", "且该道路的道路类型为:" + link.getRoadType());

            }
        }

    }
}
