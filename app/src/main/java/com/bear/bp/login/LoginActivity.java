package com.bear.bp.login;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bear.bp.MainActivity;
import com.bear.bp.R;
import com.bear.bp.util.LoginRegisterServer;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button login;       // 登录按钮
    private Button register;    // 注册按钮
    private EditText userName;  // 用户名
    private  EditText password; // 密码

    private static final String TAG = "LoginActivity";
    private String url = "http://182.92.159.2/LoginDemo/AndroidTestDemo/LoginServlet?";
    private boolean autoLogin = false;  // 是否自动登录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        initView();
        setListener();

        // 获取存储在本地的用户信息
        SharedPreferences preferences = getSharedPreferences("userMessage", MODE_PRIVATE);
        if (preferences != null){
            userName.setText(preferences.getString("username", ""));
            password.setText(preferences.getString("password", ""));
            autoLogin = preferences.getBoolean("autologin", false);
        }
        // 自动登录
        if (autoLogin){
            LoginRegisterServer.postRequest(userName.getText().toString(),
                    password.getText().toString(), url, LoginActivity.this, 1);
        }

    }
    // 绑定控件
    private void initView(){
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);
    }
    // 设置监听事件
    private void setListener(){
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                // 进行登录验证
                String inputUserName = userName.getText().toString();
                String inputPassword = password.getText().toString();
                if (TextUtils.isEmpty(inputUserName)){
                    Toast.makeText(LoginActivity.this,
                            "请输入用户名", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(inputPassword)){
                    Toast.makeText(LoginActivity.this,
                            "请输入密码", Toast.LENGTH_SHORT).show();
                }else {
                    LoginRegisterServer.postRequest(inputUserName, inputPassword, url,
                            LoginActivity.this, 1);
                }
                break;
            case R.id.register:
                // 进入注册界面
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 存储用户名和密码
        SharedPreferences.Editor editor = getSharedPreferences("userMessage",
                MODE_PRIVATE).edit();
        editor.putString("username", userName.getText().toString());
        editor.putString("password", password.getText().toString());
        editor.putBoolean("autologin", true);
        editor.apply();
    }
}
