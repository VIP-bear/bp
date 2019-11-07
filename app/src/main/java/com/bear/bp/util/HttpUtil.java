package com.bear.bp.util;

import android.util.Log;

import com.bear.bp.StaticGlobal;
import com.bear.bp.data.Picture;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {

    // 发送一个网络请求
    public static void sendRequestWithOkHttp(){
        Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(StaticGlobal.url + "image.txt")
                                .build();
                        Response response = client.newCall(request).execute();
                        StaticGlobal.responseData = response.body().string();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        thread.start();
        try{
            thread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    // 解析返回的数据
    public static void paserDataAndGetPictureUrl(List<Picture> pictureList){
        Log.d("http", "paserDataAndGetPictureUrl: " + StaticGlobal.responseData);
        String[] lineList = StaticGlobal.responseData.split("\n");
        for (int i = 0; i < lineList.length; i++){
            String fileName = lineList[i].split(" ")[0];
            int fileNum = Integer.parseInt(lineList[i].split(" ")[1]);
            for (int j = 1; j <= fileNum; j++){
                String pictureName = fileName + "-" + j + ".JPG";
                Picture picture = new Picture();
                picture.setPictureName(pictureName);
                picture.setPictureUrl(StaticGlobal.url + fileName + "/" + pictureName);
                pictureList.add(picture);
            }
        }
    }

}
