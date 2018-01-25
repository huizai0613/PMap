package xxzx.login;

/**
 * Created by ch on 2017/3/15.
 * 网络连接返回简单结果表示
 */
public class HttpRequestResult {
    public static int FAIL = 0;//失败
    public static int SUCCESS = 1;//成功
    public static int ERROR = 2;//错误，相当于网络连接错误
    public static int CANCEL = 3;//取消
    public static int LOCK = 4;//成功
    public static int SUCCESS_FIRSTLOGIN = 5;//登陆成功并是第一次登录
}
