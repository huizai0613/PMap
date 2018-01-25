package xxzx.activityNavi;

import android.os.Bundle;
import android.view.View;

import com.amap.api.navi.AMapNaviView;

import xxzx.activity.R;


/**
 * 创建时间：15/12/29 15:43
 * 项目名称：newNaviDemo
 *
 * @author lingxiang.wang
 * @email lingxiang.wang@alibaba-inc.com
 * 类说明：
 */

public class OverviewModeActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_overview_mode);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
        mAMapNaviView.getViewOptions().setLayoutVisible(false);
    }

    public void overview(View view) {
        mAMapNaviView.displayOverview();
    }

    public void goOnNavi(View view) {
        mAMapNaviView.recoverLockMode();
    }
}
