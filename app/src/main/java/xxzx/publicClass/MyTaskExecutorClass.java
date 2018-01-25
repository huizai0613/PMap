package xxzx.publicClass;

import android.app.Dialog;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by ch on 2016/6/2.
 */
public class MyTaskExecutorClass {
    public static void TaskExecutorTerminated(ExecutorService limitedTaskExecutor,Dialog dialog) {

        //关闭所有子线程，不关的话主线程会一直阻塞
        limitedTaskExecutor.shutdown();

//        try {
//
//            while (!limitedTaskExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
//                System.out.println("线程池没有关闭");
//            }
//
//            if(dialog!=null){
//                dialog.dismiss();
//            }
//            limitedTaskExecutor = null;
//        }catch (InterruptedException e){
//
//        }

        try {

            //等待子线程全部结束,这样写其实是没有意义的
            while (limitedTaskExecutor.isTerminated()) {
                try {
                    Thread.sleep(500);
                    limitedTaskExecutor = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {

        }
    }
}
