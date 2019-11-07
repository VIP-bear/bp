package com.bear.bp.util;

import android.content.Context;

import com.bear.bp.inf.ImageDownLoadCallBack;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.io.File;

public class DownLoadImageService implements Runnable{

    private String url;

    private Context context;

    private ImageDownLoadCallBack callBack;

    public DownLoadImageService(String url, Context context, ImageDownLoadCallBack callBack) {
        this.url = url;
        this.context = context;
        this.callBack = callBack;
    }

    @Override
    public void run() {
        File file = null;
        try {
            // 从服务器上下载图片
            RequestManager manager = Glide.with(context);
            file = manager.downloadOnly().load(url).submit().get();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (file != null){
                callBack.onDownLoadSuccess(file);
            }else {
                callBack.onDownLoadFailed();
            }
        }
    }
}
