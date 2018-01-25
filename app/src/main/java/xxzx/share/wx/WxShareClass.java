package xxzx.share.wx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import xxzx.publicClass.MyString;
import xxzx.publicClass.ToastUtil;

/**
 * Created by Lenovo on 2017/6/8.
 */

public class WxShareClass {

    private static final int THUMB_SIZE = 120;

    private Context mContext;
    //微信分享
    private IWXAPI wxApi;


    public WxShareClass(Context context){
        this.mContext = context;
        //实例化
        wxApi = WXAPIFactory.createWXAPI(context, MyString.WX_APP_ID,true);
        wxApi.registerApp(MyString.WX_APP_ID);
    }


    /**
     * flag =0微信好友，flag=1 朋友圈
     * @param text
     * @param flag
     */
    public void shareText(String text,int flag){

        WXTextObject textObj = new WXTextObject();
        textObj.text = text;
        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        // 发送文本类型的消息时，title字段不起作用
        msg.title = "Will be ignored";
        msg.description = text;

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求

        req.message = msg;
        /**
         * 判断是否是朋友圈
         */
        req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;


        // 调用api接口发送数据到微信
        wxApi.sendReq(req);
    }



    public void shareImg(String imgpath, int flag){


        File file = new File(imgpath);
        if (!file.exists()) {

            ToastUtil.show(mContext,"文件不存在！路径="+imgpath);
            return;
        }

        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(imgpath);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap bmp = BitmapFactory.decodeFile(imgpath);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;

        wxApi.sendReq(req);
    }



    public void shareWebPage(String text,String imgpath, int flag){

        File file = new File(imgpath);
        if (!file.exists()) {

            ToastUtil.show(mContext,"文件不存在！路径="+imgpath);
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://www.baidu.com";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "隐患点信息";
        msg.description = text;


        FileInputStream input = null;
        try {
            input = new FileInputStream(new File(imgpath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //先转换成bitmap
        Bitmap thumb = BitmapFactory.decodeStream(input);

        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = flag==0 ? SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);
    }






    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


}
