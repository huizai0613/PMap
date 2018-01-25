package xxzx.publicClass;

/**
 * Created by ch on 2016/4/13.
 */
public class DangerVersion {

    //Version可以等于-1,0,1
    public static final int DANGER_VERSION_INIT=-1;//从服务器中下载的原始数据
    public static final int DANGER_VERSION_SOLVE_UN = 0;//覆盖巡视
    public static final int DANGER_VERSION_ADD = 0;//新增数据
    public static final int DANGER_VERSION_SOLVE = 1;//消缺归档
}
