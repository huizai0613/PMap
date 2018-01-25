package xxzx.myView;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;

import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import xxzx.activity.R;
import xxzx.publicClass.MyListView.ListItemMenu;
import xxzx.publicClass.MyListView.ListItemMenuAdapter;

/**
 * Created by ch on 2016/5/3.
 */
public class MyOptionMenu extends PopupWindow {

    private ListView listview;
    private View conentView;



    // ----------------定义点击事件-----------------------------
    /**
     * 一定一个接口
     */
    public interface ICoallBack {
        public void onClick(int position);
    }

    /**
     * 初始化接口变量
     */
    ICoallBack icallBack = null;

    /**
     * 自定义控件的自定义事件
     *
     * @param iBack
     *            接口类型
     **/
    public void setOnClick(ICoallBack iBack) {
        icallBack = iBack;
    }



    /**
     * @param context 上下文
     * @return
     */
    public MyOptionMenu(Context context,List<ListItemMenu> items) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.public_popupwindow_menu, null);

        listview = (ListView) conentView.findViewById(R.id.listview);
        listview.setAdapter(new ListItemMenuAdapter(context, items));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (icallBack != null) {
                    icallBack.onClick(position);
                }
                MyOptionMenu.this.dismiss();
            }
        });

        this.setContentView(conentView);
        this.setWidth(700);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.transparent)));
        this.setFocusable(true);
    }


    /**
     * 设置动画
     * @param aniTabMenu
     */
    public void setAnimation(int aniTabMenu){
        this.setAnimationStyle(aniTabMenu);
    }
}
