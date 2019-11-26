package com.bear.bp.ui.My;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bear.bp.R;

public class AccountSettingActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;     // 返回

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        // 隐藏actionbar
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        initLayout();
        setListener();

    }
    // 寻找各个控件
    private void initLayout(){
        back = findViewById(R.id.from_accountsetting_back_setting);
    }
    // 设置监听事件
    private void setListener(){
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.from_accountsetting_back_setting:
                Intent intent = new Intent(AccountSettingActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
