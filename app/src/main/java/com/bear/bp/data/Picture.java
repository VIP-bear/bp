package com.bear.bp.data;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Picture extends LitePalSupport implements Serializable {

    private String pictureUrl;      // 图片地址

    private String pictureName;     // 图片名

    private int isLove;             // 是否是喜欢的图片


    public int getIsLove() {
        return isLove;
    }

    public void setIsLove(int isLove) {
        this.isLove = isLove;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
