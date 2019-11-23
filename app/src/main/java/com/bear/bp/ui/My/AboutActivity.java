package com.bear.bp.ui.My;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bear.bp.MainActivity;
import com.bear.bp.R;

public class AboutActivity extends AppCompatActivity {

    private ImageView back;     // 返回my界面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // 隐藏actionbar
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        back = findViewById(R.id.from_about_back_my);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
