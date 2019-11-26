package com.bear.bp.util;

import com.bear.bp.data.Picture;

import java.util.List;

public class HttpUtil {

    // 解析返回的数据
    public static void paserDataAndGetPictureUrl(List<Picture> pictureList, String responseData){
        String[] imageNameList = responseData.split(" ");
        String imageUrl = "";
        for (int i = 0; i < imageNameList.length; i++){
            imageUrl = "http://182.92.159.2/pic/uploadImage/" + imageNameList[i];
            Picture picture = new Picture();
            picture.setPictureName(imageNameList[i]);
            picture.setPictureUrl(imageUrl);
            picture.setIsLove(0);
            pictureList.add(picture);
        }
    }

}
