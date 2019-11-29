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

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView back;                 // 返回my界面

    private RelativeLayout accountSetting;  // 账号设置

    private RelativeLayout privalSetting;   // 个人资料设置

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
        privalSetting = findViewById(R.id.prival_setting);
        loginOut = findViewById(R.id.login_out);
    }
    // 设置监听事件
    private void setListener(){
        back.setOnClickListener(this);
        accountSetting.setOnClickListener(this);
        privalSetting.setOnClickListener(this);
        loginOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.from_setting_back_my:
                Intent intent1 = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.account_setting:
                Intent intent2 = new Intent(SettingActivity.this, AccountSettingActivity.class);
                startActivity(intent2);
                break;
            case R.id.prival_setting:
                Intent intent3 = new Intent(SettingActivity.this, PrivalSettingActivity.class);
                startActivity(intent3);
                break;
            case R.id.login_out:
                // 取消自动登录
                SharedPreferences.Editor editor = getSharedPreferences("userMessage",
                        MODE_PRIVATE).edit();
                editor.putBoolean("autologin", false);
                editor.apply();
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
