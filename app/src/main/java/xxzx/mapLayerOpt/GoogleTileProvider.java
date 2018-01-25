package xxzx.mapLayerOpt;

import android.net.ParseException;
import android.util.Log;

import com.amap.api.maps.model.Tile;
import com.amap.api.maps.model.TileProvider;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import xxzx.publicClass.MySharedPreferences;
import xxzx.publicClass.MyString;
import xxzx.publicClass.SDTool;

/**
 * Created by ch on 2016/2/13.
 */
public class GoogleTileProvider implements TileProvider {

    // 切片临时缓存地图文件夹路径
    private String savePath ;// 本地存储路径
    private String url ;//切片地址

    private int minFreeSDCardSize=50;//sd卡剩余空间

    private static final int TILE_WIDTH = 256;
    private static final int TILE_HEIGHT = 256;
    public static final int BUFFER_SIZE = 16 * 1024;

    private byte[] tile;


    public GoogleTileProvider(String url,String savePath){
        this.url=url;
        this.savePath=savePath;
    }


    @Override
    public Tile getTile(int x, int y, int zoom) {
        byte[] result = null;
        try {
//            zoom=9;
//            y=13324;
//            x=27058;
            zoom = zoom -6;
            result=this.getOfflineCacheFile(x,y,zoom);
            if (result == null) {
                result = getHttpByte(x, y, zoom);
                //写入缓存数据文件夹
                addOfflineCacheFile(x,y,zoom,result);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Tile tile = new Tile(TILE_WIDTH, TILE_HEIGHT, result);
        return tile;
    }

    @Override
    public int getTileHeight() {
        return TILE_HEIGHT;
    }

    @Override
    public int getTileWidth() {
        return TILE_WIDTH;
    }


    // 将图片保存到本地 目录结构可以随便定义 只要你找得到对应的图片
    protected void addOfflineCacheFile(int row, int col,int level, byte[] bytes) {
        if(bytes==null){
            return;
        }
        String path = savePath;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        // 删除之前的切片
        this.IsOrNotDeleteTiles(file);

        File rowfile = new File(path + "/" + level + "_" + col + "_" + row + ".png");
        if (!rowfile.exists()) {
            FileOutputStream out=null;
            try {
                out = new FileOutputStream(rowfile);
                out.write(bytes);
                out.close();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }finally {
                MySharedPreferences.addOfflineTileSize(Double.valueOf(bytes.length)/(1024*1024));
                Log.e("存储数据量：",String.valueOf(Double.valueOf(bytes.length)/(1024*1024)));
            }
        }
    }

    // 从本地获取图片
    private byte[] getOfflineCacheFile( int row, int col,int level) {
        byte[] bytes = null;
        String path = savePath;
        File rowfile = new File(path + "/" + level + "_" + col + "_" + row + ".png");

        if (rowfile.exists()) {
            try {
                bytes = CopySdcardbytes(rowfile);
                Log.w("调用本地缓存地图", path + "/" + level + "_" + col + "_" + row + ".png");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return bytes;
    }

    // 读取本地图片流
    private byte[] CopySdcardbytes(File file) throws IOException {
//        FileInputStream in = new FileInputStream(file);
//        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
//        byte[] temp = new byte[1024];
//        int size = 0;
//        while ((size = in.read(temp)) != -1) {
//            out.write(temp, 0, size);
//        }
//
//        byte[] bytes = out.toByteArray();
//        temp=null;
//        in.close();
//        out.close();
//        return bytes;

        InputStream in = null;
        ByteArrayOutputStream buffer = null;

        try {
            buffer = new ByteArrayOutputStream();
            in = new FileInputStream(file);

            int nRead;
            byte[] data = new byte[BUFFER_SIZE];

            while ((nRead = in.read(data, 0, BUFFER_SIZE)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();

            return buffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) try { in.close(); } catch (Exception ignored) {}
            if (buffer != null) try { buffer.close(); } catch (Exception ignored) {}
            Log.e("切片", "释放读取本地切片的缓存");
        }
    }



    /**
     * 根据类型、行列号获得切片字节
     * @param level
     * @param col
     * @param row
     * @return
     * @throws MalformedURLException
     * @throws ParseException
     * @throws IOException
     */
    private byte[] getHttpByte(int row, int col,int level) {
        byte[] result = null;

        ByteArrayOutputStream buffer=null;
        BufferedInputStream in = null;

        try {
            buffer = new ByteArrayOutputStream();

            URL sjwurl = new URL(String.format(url, row, col, level));
            Log.e("google:",sjwurl.toString());
            HttpURLConnection httpUrl = null;

            byte[] data = new byte[BUFFER_SIZE];

            httpUrl = (HttpURLConnection) sjwurl.openConnection();
            httpUrl.setConnectTimeout(6 * 1000);
            httpUrl.connect();
            if (httpUrl.getResponseCode() == 200) {
                in = new BufferedInputStream(httpUrl.getInputStream());

                while (true) {
                    int bytes_read = in.read(data);
                    if (bytes_read > 0) {
                        buffer.write(data, 0, bytes_read);
                    } else {
                        break;
                    }
                }
                httpUrl.disconnect();

                Log.e("切片", sjwurl.toString());

                result = buffer.toByteArray();
            }

            buffer.flush();

            return result;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        finally {
            if (in != null) try { in.close(); } catch (Exception ignored) {}
            if (buffer != null) try { buffer.close(); } catch (Exception ignored) {}
            Log.e("切片", "释放读取网络切片的缓存");
        }

    }

    /**
     * 符合条件时要清空所有切片数据 条件有两个，二是SD卡存储量的控制
     *
     * @param colfile
     */
    protected void IsOrNotDeleteTiles(File colfile) {

        File[] t_file = colfile.listFiles();
        // 如果SD卡上剩余不到50mb
        if (SDTool.getSDFreeSize() < (1024 * 1024) * minFreeSDCardSize) {
            for (int i = 0; i < t_file.length; i++) {
                // 判断文件列表中的对象是否为文件夹对象，如果是则执行tree递归，直到把此文件夹中所有文件输出为止
                if (!t_file[i].isDirectory()) {
                    t_file[i].delete();
                }
            }

            MySharedPreferences.putOfflineTileSize(0.0);
        }
    }










}
