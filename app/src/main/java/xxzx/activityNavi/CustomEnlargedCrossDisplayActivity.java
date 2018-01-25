package xxzx.activityNavi;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AimLessModeCongestionInfo;

import xxzx.activity.R;


/**
 * 创建时间：11/10/15 16:08
 * 项目名称：newNaviDemo
 *
 * @author lingxiang.wang
 * @email lingxiang.wang@alibaba-inc.com
 * 类说明：
 */

public class CustomEnlargedCrossDisplayActivity extends BaseActivity implements AMapNaviListener {

    private ImageView myCustomEnlargedCross;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_custom_enlarged_cross);
        myCustomEnlargedCross = (ImageView) findViewById(R.id.myEnlargedCross);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);

        //设置布局完全不可见
        AMapNaviViewOptions viewOptions = mAMapNaviView.getViewOptions();
        viewOptions.setLayoutVisible(false);

    }


    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        myCustomEnlargedCross.setImageBitmap(aMapNaviCross.getBitmap());
        myCustomEnlargedCross.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCross() {
        myCustomEnlargedCross.setVisibility(View.INVISIBLE);
    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo){

    }
}
