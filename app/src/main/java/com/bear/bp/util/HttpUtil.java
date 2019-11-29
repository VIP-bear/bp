package com.bear.bp.util;

import android.util.Log;

import com.bear.bp.data.Picture;

import java.util.List;

public class HttpUtil {

    private static final String TAG = "HttpUtil";

    // 解析返回的数据
    public static void paserDataAndGetPictureUrl(List<Picture> pictureList, String responseData, int love){
        String[] imageNameList = responseData.split(" ");
        String imageUrl = "";
        for (int i = 0; i < imageNameList.length; i++){
            imageUrl = "http://182.92.159.2/pic/uploadImage/" + imageNameList[i];
            Log.d(TAG, "paserDataAndGetPictureUrl: "+imageNameList[i]);
            Picture picture = new Picture();
            picture.setPictureName(imageNameList[i]);
            picture.setPictureUrl(imageUrl);
            picture.setIsLove(love);
            pictureList.add(picture);
        }
    }

}
