package xxzx.publicClass;

/**
 * Created by mulin on 2015/10/12.
 */

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

public class SDTool {

    public SDTool() {

    }

   /* public static SDTool instance() {
        if (tool == null) {
            synchronized (SDTool.class) {
                if (tool == null) {
                    tool = new SDTool();
                    tool.SD_EXIST = Environment.MEDIA_MOUNTED
                            .equals(Environment.getExternalStorageState());
                }
            }
        }
        return tool;
    }  */

    /**
     * 获取SD卡剩余容量
     * @return
     */
    public static long getSDFreeSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        return freeBlocks * blockSize; // 单位Byte
    }

    /**
     * 获取SD卡总容量
     * @return
     */
    public static long getSDAllSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 获取所有数据块数
        long allBlocks = sf.getBlockCount();
        // 返回SD卡大小
        return allBlocks * blockSize; // 单位Byte
    }
}
