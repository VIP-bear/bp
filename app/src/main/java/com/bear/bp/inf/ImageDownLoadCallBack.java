package com.bear.bp.inf;

import java.io.File;

public interface ImageDownLoadCallBack {

    void onDownLoadSuccess(File file);      // 下载成功

    void onDownLoadFailed();                // 下载失败

}
