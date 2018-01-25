package xxzx.publicClass;

/**
 * Created by ch on 2016/4/25.
 */
public class MyActivityFragmentListener {
    /** Acitivity要实现这个接口，这样Fragment和Activity就可以共享事件触发的资源了 */
    public interface MyListener
    {
        public void initFragmentData();
    }
}
