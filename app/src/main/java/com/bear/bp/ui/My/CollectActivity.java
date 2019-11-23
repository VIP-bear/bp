package com.bear.bp.ui.My;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bear.bp.MainActivity;
import com.bear.bp.R;
import com.bear.bp.adapter.PictureAdapter;
import com.bear.bp.data.Picture;

import org.litepal.LitePal;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class CollectActivity extends AppCompatActivity {

    private ImageView back;     // 返回my界面

    private PictureAdapter pictureAdapter;

    private RecyclerView collectView;

    private List<Picture> collectPictureList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        // 隐藏actionbar
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        initLayout();
        setListener();

        // 获取图片信息
        collectPictureList = LitePal.findAll(Picture.class);

        // 数据加载
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        collectView.setLayoutManager(layoutManager);
        pictureAdapter = new PictureAdapter(CollectActivity.this, collectPictureList);
        collectView.setAdapter(pictureAdapter);

    }
    // 寻找各个控件
    private void initLayout(){
        back = findViewById(R.id.back_my);
        collectView = findViewById(R.id.collect_view);
    }
    // 设置监听事件
    private void setListener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CollectActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
