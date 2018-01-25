package xxzx.baseMapState.MainMap;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatDelegate;
import android.widget.LinearLayout;

/**
 * 地图控件的操作状态接口
 *
 * @author Administrator
 */
public abstract class BaseMainMapState {

    protected AppBarLayout appBarLayout = null;//toolbar容器
    protected LinearLayout bottomContainer = null;// 底部容器父类
    protected Object mData = null;// 当前处理的数据
    protected Context mContext = null;//上下文
    protected AppCompatDelegate delegate = null;//代理
    protected int toolBarMenuID = 3;//工具条上菜单索引,这个值是固定不变的
    protected MapStateContext stateContext = null;// 管理状态的上下文

    /**
     * 构造函数
     * <p/>
     * 地图控件
     *
     * @param _mData 处理的参数
     */
    void BaseMainMapState(Context context, AppCompatDelegate delegate, AppBarLayout appBarLayout, LinearLayout bottomContainer, Object _mData) {
        this.mContext = context;
        this.delegate = delegate;
        this.appBarLayout = appBarLayout;
        this.mData = _mData;
        this.bottomContainer = bottomContainer;
    }

    /**
     * 界面元素和数据初始化加载
     */
    public void InitViewAndData() {
        initView();
        initToolBar();
        initbottomBar();
        initData();
    }

    protected void setContext(MapStateContext context) {
        this.stateContext = context;
    }

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
     * 返回键点击事件
     */
    protected abstract void BackKeyDown();

//    /**
//     * 菜单键点击事件
//     */
//    protected abstract void MenuKeyDown();
    /**
     * 关闭当前状态的函数
     */
    protected abstract void Close();

    /**
     * 在外部调用
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

}
