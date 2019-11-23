package com.bear.bp.ui.My;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bear.bp.R;

public class AccountSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        // 隐藏actionbar
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }
}
