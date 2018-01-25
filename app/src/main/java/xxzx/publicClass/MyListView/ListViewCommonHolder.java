package xxzx.publicClass.MyListView;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import xxzx.myView.ImgButton;

/**
 * Created by ch on 2016/2/25.
 */
/**
 * listview组件复用，防止“卡顿”
 *
 * @author Administrator
 *
 */
public class ListViewCommonHolder {
    public TextView tv_title;
    public TextView tv_content;
    public ImageView iv_icon;
    public RelativeLayout rl_numColor;
    public TextView tv_num;
    public ImageView iv_arrow;
}
