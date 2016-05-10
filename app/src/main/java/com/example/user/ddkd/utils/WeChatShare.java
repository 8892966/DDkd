package com.example.user.ddkd.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.BitmapFactory;

import com.example.user.ddkd.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


/**
 * Created by Administrator on 2016/5/7.
 */
public class WeChatShare{// extends Activity implements View.OnClickListener{
    private static final String APP_ID="wxf3c63e6592c694e8";
    private IWXAPI api;
    private EditText sharetext;
    private TextView sure;
    private ImageView setExit;
    private ImageView shareimage;
    private Activity activity;
    private String status;
    public WeChatShare(Activity activity,String status){
        api=WXAPIFactory.createWXAPI(activity,APP_ID);
        //将APP_ID注册到微信
        api.registerApp(APP_ID);
        this.activity=activity;
        this.status=status;
    }
    public void Open_WeChat(){
        api.openWXApp();
//        Toast.makeText(WeChatShare.this,String.valueOf(api.openWXApp()),Toast.LENGTH_SHORT).show();
    }

    //***************分享文本
    public void Send_Text(){
        String message=sharetext.getText().toString();
        if (message==null||message.length()==0){
            return;
        }
        //获取需要发送的文本
        WXTextObject textObject=new WXTextObject();
        textObject.text=message;

        WXMediaMessage msg=new WXMediaMessage();
        msg.mediaObject=textObject;
        msg.description=message;

        SendMessageToWX.Req req=new SendMessageToWX.Req();
        req.message=msg;
        req.transaction=buildtransaction("text");
        String Static= (String) activity.getIntent().getSerializableExtra("Static");
        if ("1".equals(Static)){
            req.scene=SendMessageToWX.Req.WXSceneSession;
//            Toast.makeText(WeChatShare.this,"分享到微信好友",Toast.LENGTH_SHORT).show();
        }else if("2".equals(Static)){
            req.scene=SendMessageToWX.Req.WXSceneTimeline;
//            Toast.makeText(WeChatShare.this,"分享到朋友圈",Toast.LENGTH_SHORT).show();
        }
        api.sendReq(req);
//        Toast.makeText(WeChatShare.this,String.valueOf(api.sendReq(req)),Toast.LENGTH_SHORT).show();
    }

    //********************分享图片
    public boolean Send_Image(){
        //**********************分享图片**********************
        Bitmap bitmap=BitmapFactory.decodeResource(activity.getResources(), R.drawable.download);
        //压缩选取的图片
        Bitmap thrum=Bitmap.createScaledBitmap(bitmap,120,150,true);

        WXImageObject imageobject=new WXImageObject(bitmap);

        WXMediaMessage msg2=new WXMediaMessage();
        msg2.mediaObject=imageobject;
        bitmap.recycle();
        msg2.thumbData=BittoByte(thrum,true);//设置缩略图

        SendMessageToWX.Req req2=new SendMessageToWX.Req();
        req2.message=msg2;
        req2.transaction=buildtransaction("image");
        if ("1".equals(status)){
            req2.scene=SendMessageToWX.Req.WXSceneSession;
//            Toast.makeText(WeChatShare.this,"分享到微信好友",Toast.LENGTH_SHORT).show();
        }else if("2".equals(status)){
            req2.scene=SendMessageToWX.Req.WXSceneTimeline;
//            Toast.makeText(WeChatShare.this,"分享到朋友圈",Toast.LENGTH_SHORT).show();
        }
        boolean result=api.sendReq(req2);
        Log.i("Shsre", String.valueOf(result));
        return result;
    }
    //将Bitmap转化为二进制的数组
    private byte[] BittoByte(final Bitmap bitmap,final boolean needrecycle){
        ByteArrayOutputStream output=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,output);
        if(needrecycle){
            bitmap.recycle();
        }
        byte[] result=output.toByteArray();
        try {
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //**********************分享URL
    public boolean Send_Url(){
        WXWebpageObject webpageObject=new WXWebpageObject();
        webpageObject.webpageUrl="http://www.louxiago.com/app/index.php?name=DDKD";

        WXMediaMessage wxMediaMessage=new WXMediaMessage(webpageObject);
        wxMediaMessage.title="DD镖师创业平台";
        wxMediaMessage.description="一个代寄、代拿快递的兼职平台";
        //设置缩略图
        Bitmap bitmap=BitmapFactory.decodeResource(activity.getResources(),R.drawable.download);
        Bitmap thrum=Bitmap.createScaledBitmap(bitmap, 120, 150, true);
        bitmap.recycle();
        wxMediaMessage.thumbData=BittoByte(thrum,true);

        SendMessageToWX.Req req=new SendMessageToWX.Req();
        req.transaction=buildtransaction("webpageObject");
        req.message=wxMediaMessage;
        if ("1".equals(status)){
            req.scene=SendMessageToWX.Req.WXSceneSession;
//            Toast.makeText(WeChatShare.this,"分享到微信好友",Toast.LENGTH_SHORT).show();
        }else if("2".equals(status)){
            req.scene=SendMessageToWX.Req.WXSceneTimeline;
//            Toast.makeText(WeChatShare.this,"分享到朋友圈",Toast.LENGTH_SHORT).show();
        }
        boolean result=api.sendReq(req);
        return result;

    }

    //为请求生成一个唯一的标示
    private String buildtransaction(final String type){
        return (type==null)?String.valueOf(System.currentTimeMillis()):type+System.currentTimeMillis();
    }
//
//    //分享本地图片
//    public void Sent_Local_Image(){
//        //判断图像文件是否存在
//        String path="/sdcard/imagename.jpg";
//        File file=new File(path);
//        if(!file.exists()){
////            Toast.makeText(WeChatShare.this,"该文件不存在",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        //根据路径获取图片的内容
//        WXImageObject imageobject=new WXImageObject();
//        imageobject.setImagePath(path);//设置文件的路径
//
//        //包装imageobject
//        WXMediaMessage msg=new WXMediaMessage();
//        msg.mediaObject=imageobject;
//        //压缩图像
//        Bitmap bitmap=BitmapFactory.decodeFile(path);
//        Bitmap thrum=Bitmap.createScaledBitmap(bitmap,120,150,true);
//        bitmap.recycle();
//        msg.thumbData=BittoByte(thrum,true);//设置缩略图
//        SendMessageToWX.Req req=new SendMessageToWX.Req();
//        req.message=msg;
//        req.transaction=buildtransaction("image");
//        String Static= (String) activity.getIntent().getSerializableExtra("Static");
//        if ("1".equals(Static)){
//            req.scene=SendMessageToWX.Req.WXSceneSession;
////            Toast.makeText(WeChatShare.this,"分享到微信好友",Toast.LENGTH_SHORT).show();
//        }else if("2".equals(Static)){
//            req.scene=SendMessageToWX.Req.WXSceneTimeline;
////            Toast.makeText(WeChatShare.this,"分享到朋友圈",Toast.LENGTH_SHORT).show();
//        }
//        api.sendReq(req);
////        Toast.makeText(WeChatShare.this,String.valueOf(api.sendReq(req)),Toast.LENGTH_SHORT).show();
//    }




}
