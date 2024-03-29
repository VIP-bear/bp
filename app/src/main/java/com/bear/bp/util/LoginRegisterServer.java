package com.bear.bp.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.bear.bp.MainActivity;
import com.bear.bp.login.LoginActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginRegisterServer {
    /*
       处理登录、注册、图片上传
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

    // 发送登录/注册请求
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
    // 上传头像
    public static void uploadHeadImage(String url, String imagePath,String name, String fileName){
        final OkHttpClient client = new OkHttpClient();
        File file = new File(imagePath);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        // 建立请求表单
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(name, fileName,fileBody)
                .build();
        // 发送请求
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        // 在子线程中发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.d("load", "onFailure: "+"上传失败");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Log.d("load", "onResponse: "+"上传成功"+response.body().string());
                    }
                });
            }
        }).start();

    }
    // 上传图片
    public static void uploadImage(String url, String imagePath,String username, String label,
                                   String title, String description, String fileName) {
        final OkHttpClient client = new OkHttpClient();
        File file = new File(imagePath);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        // 建立请求表单
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", username)
                .addFormDataPart("label", label)
                .addFormDataPart("title", title)
                .addFormDataPart("description", description)
                .addFormDataPart("image", fileName, fileBody)
                .build();
        // 发送请求
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        // 在子线程中发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.d("load", "onFailure: " + "上传失败");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Log.d("load", "onResponse: " + "上传成功" + response.body().string());
                    }
                });
            }
        }).start();
    }

}
