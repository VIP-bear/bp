package com.bear.bp;

import com.bear.bp.data.Picture;

import java.util.ArrayList;
import java.util.List;

public class StaticGlobal {

    public static String responseData;      // 请求返回的数据

    public static String url = "http://182.92.159.2/pic/";  // ip地址

    public static List<Picture> pictureList = new ArrayList<>();  // 存储图片

}
