package xxzx.publicClass.MyImgOpt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Lenovo on 2017/6/9.
 */

public class MyImageOptClass {

    /**
     * 保存文件
     * @param bm
     * @param filePath
     * @throws IOException
     */
    public static void saveFile(Bitmap bm,int compressRate, String filePath){
        try{
            File myCaptureFile = new File(filePath);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, compressRate, bos);
            bos.flush();
            bos.close();}
        catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 在图片上写文字
     * @param imgpath
     * @param gText
     * @return
     */
    public static Bitmap drawTextToBitmap(String imgpath, String gText) {

        Bitmap bitmap = BitmapFactory.decodeFile(imgpath);
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        // 获取图片的宽高
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        //Bitmap newBitmap = Bitmap.createBitmap(bitmapWidth,bitmapHeight, Bitmap.Config.ARGB_4444);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.RED);
        textPaint.setTextSize((int) (30));
        textPaint.setDither(true); //获取跟清晰的图像采样
        textPaint.setFilterBitmap(true);//过滤一些
        textPaint.setShadowLayer(0f, 2f, 2f, Color.WHITE);
        StaticLayout sl= new StaticLayout(gText, textPaint, bitmap.getWidth()-8, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        canvas.translate(20, 20);
        sl.draw(canvas);
        return bitmap;
    }








}
