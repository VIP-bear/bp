package com.bear.bp.ui.My;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bear.bp.MainActivity;
import com.bear.bp.R;
import com.bear.bp.login.LoginActivity;

public class SettingActivity extends AppCompatActivity {

    private ImageView back;                 // 返回my界面

    private RelativeLayout accountSetting;  // 账号设置

    private RelativeLayout loginOut;        // 登出

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // 隐藏actionbar
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        initLayout();
        setListener();

    }
    // 寻找各个控件
    private void initLayout(){
        back = findViewById(R.id.from_setting_back_my);
        accountSetting = findViewById(R.id.account_setting);
        loginOut = findViewById(R.id.login_out);
    }
    // 设置监听事件
    private void setListener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        accountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, AccountSettingActivity.class);
                startActivity(intent);
                finish();
            }
        });
        loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消自动登录
                SharedPreferences.Editor editor = getSharedPreferences("userMessage",
                        MODE_PRIVATE).edit();
                editor.putBoolean("autologin", false);
                editor.apply();
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
