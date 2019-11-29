package com.bear.bp.ui.My;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.bear.bp.MainActivity;
import com.bear.bp.R;
import com.bear.bp.StaticGlobal;
import com.bear.bp.adapter.PictureAdapter;
import com.bear.bp.data.Picture;
import com.bear.bp.util.HttpUtil;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CollectActivity extends AppCompatActivity {

    private ImageView back;     // 返回my界面

    private PictureAdapter pictureAdapter;

    private RecyclerView collectView;

    private List<Picture> collectPictureList = new ArrayList<>();

    private String url = "http://182.92.159.2/LoginDemo/AndroidTestDemo/GetCollectImageServlet?";

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    // 解析返回的数据
                    HttpUtil.paserDataAndGetPictureUrl(collectPictureList, (String) msg.obj, 1);
                    pictureAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

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
        getCollectImage(url, StaticGlobal.username);

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

    // 获取收藏图片名称
    private void getCollectImage(String url, String username){
        final OkHttpClient client = new OkHttpClient();
        // 建立请求表单
        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
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
                        handler.obtainMessage(1, response.body().string()).sendToTarget();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
