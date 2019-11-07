package com.bear.bp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bear.bp.data.Picture;
import com.bear.bp.inf.ImageDownLoadCallBack;
import com.bear.bp.util.DownLoadImageService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PictureActivity extends AppCompatActivity {

    private ImageView imageView;    // 图片

    private Picture picture;

    private Button back;    // 返回主界面

    private int position;   // 记录图片位置

    private ExecutorService executorService = null;     // 线程

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        initLayout();
        setClickListener();

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        picture = (Picture) getIntent().getSerializableExtra("pictureMessage");

        RequestOptions options = new RequestOptions();
        options.fitCenter()
                .error(R.drawable.error);

        // 显示图片
        Glide.with(this)
                .asBitmap()
                .load(picture.getPictureUrl())
                .apply(options)
                .into(imageView);

    }

    // 寻找各控件
    private void initLayout(){
        back = findViewById(R.id.back);
        imageView = findViewById(R.id.big_picture);
    }

    // 设置点击监听事件
    private void setClickListener(){
        // 返回首页
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PictureActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // 长按保存图片
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (ContextCompat.checkSelfPermission(PictureActivity.this,
                        Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(PictureActivity.this, new String[]
                            { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
                }else {
                    savePicture();
                }
                return true;
            }
        });
    }

    // 保存图片到本地
    private void savePicture(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PictureActivity.this);
        builder.setTitle("保存图片")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                DownLoadImageService downLoadImageService =
                                        new DownLoadImageService(picture.getPictureUrl(),
                                                getApplicationContext(), new ImageDownLoadCallBack() {
                                            @Override
                                            public void onDownLoadSuccess(File file) {
                                                try{
                                                    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                                            .getAbsoluteFile()
                                                            .getPath();
                                                    FileOutputStream outputStream = new FileOutputStream(new File(path + System.currentTimeMillis() + ".jpg"));

                                                    FileInputStream inputStream = new FileInputStream(file);
                                                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                                    outputStream.flush();
                                                    inputStream.close();
                                                    outputStream.close();
                                                    Toast.makeText(getApplicationContext(), "下载成功", Toast.LENGTH_SHORT).show();
                                                }catch (IOException e){
                                                    Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onDownLoadFailed() {
                                                Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                // 启动图片下载线程
                                runOnQueue(downLoadImageService);
                                break;
                                default:
                        }
                    }
                })
                .create()
                .show();
    }

    // 执行单线程队列
    private void runOnQueue(Runnable runnable){
        if (executorService == null){
            executorService = Executors.newSingleThreadExecutor();
        }
        executorService.submit(runnable);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    savePicture();
                }else{
                    Toast.makeText(PictureActivity.this, "You denied the permission",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
