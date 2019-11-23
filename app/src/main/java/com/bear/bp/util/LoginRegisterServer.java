package com.bear.bp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.bear.bp.MainActivity;
import com.bear.bp.login.LoginActivity;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginRegisterServer {
    /*
       处理登录和注册
     */

    public static Activity activity;

    public static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String loginMessage = (String) msg.obj;
                    if (loginMessage.equals("登录成功")) {
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    } else {
                        Toast.makeText(activity, "用户名或者密码错误",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    String registerMessage = (String) msg.obj;
                    if (registerMessage.equals("注册成功")){
                        Intent intent = new Intent(activity, LoginActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }else {
                        Toast.makeText(activity, "该用户已存在",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    // 发送请求
    public static void postRequest(String username, String password, String url, Activity newActivity, final int what){
        activity = newActivity;
        final OkHttpClient client = new OkHttpClient();
        // 建立请求表单
        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();
        // 发送请求
        final Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    // 回调
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                        // 将服务器返回的数据发送给handler
                        handler.obtainMessage(what, response.body().string()).sendToTarget();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
