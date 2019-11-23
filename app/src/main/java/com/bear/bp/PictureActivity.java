package com.bear.bp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bear.bp.data.Picture;
import com.bear.bp.inf.ImageDownLoadCallBack;
import com.bear.bp.util.DownLoadImageService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PictureActivity extends AppCompatActivity {

    private ImageView imageView;    // 图片

    private Picture picture;

    private Button back;    // 返回主界面

    private ImageView love;   // 喜欢/取消

    private int position;   // 记录图片位置

    private ExecutorService executorService = null;     // 线程

    private GestureDetector gestureDetector;

    private static final String TAG = "PictureActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        Intent intent = getIntent();
        picture = (Picture) intent.getSerializableExtra("pictureMessage");
        position = intent.getIntExtra("position", 0);

        initLayout();
        setClickListener();

        if (picture.getIsLove() == 1){
            love.setImageResource(R.drawable.love);
        }

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        RequestOptions options = new RequestOptions();
        options.fitCenter()
                .error(R.drawable.error);
        // 显示图片
        Glide.with(this)
                .load(picture.getPictureUrl())
                .apply(options)
                .into(imageView);

    }

    // 寻找各控件
    private void initLayout(){
        back = findViewById(R.id.back);
        imageView = findViewById(R.id.big_picture);
        love = findViewById(R.id.love);
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

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        // 添加/取消喜欢
        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (picture.getIsLove() == 0){
                    picture.setIsLove(1);
                    love.setImageResource(R.drawable.love);
                    picture.save();     // 保存到本地数据库
                }else {
                    picture.setIsLove(0);
                    love.setImageResource(R.drawable.un_love);
                    // 从本地数据库删除
                    if (picture.isSaved()){
                        picture.delete();
                    }else {
                        LitePal.deleteAll(Picture.class, "pictureName = ?",
                                picture.getPictureName());
                    }
                }
            }
        });
        // 手势
        imageView.setOnTouchListener(touchListener);
        gestureDetector = new GestureDetector(this, new MyGestureListener());

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

    // 触碰时间监听
    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }
    };


    // 手势时间监听
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener{

        // 长按保存图片
        @Override
        public void onLongPress(MotionEvent e) {
            //Toast.makeText(PictureActivity.this, "longpress", Toast.LENGTH_SHORT).show();
            super.onLongPress(e);
            if (e.getEventTime() > 1000){
                if (ContextCompat.checkSelfPermission(PictureActivity.this,
                        Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(PictureActivity.this, new String[]
                            { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
                }else {
                    savePicture();
                }
            }
        }

        // 滑动切换图片
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getRawX() - e2.getRawX() > 50){
                // 下一张图片
                position++;
                if (position < StaticGlobal.pictureList.size()){
                    picture = StaticGlobal.pictureList.get(position);
                    Glide.with(getApplicationContext()).load(picture.getPictureUrl()).into(imageView);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }else {
                    position--;
                }
            }
            if (e2.getRawX() - e1.getRawX() > 50){
                // 上一张图片
                position--;
                if (position >= 0){
                    picture = StaticGlobal.pictureList.get(position);
                    Glide.with(getApplicationContext()).load(picture.getPictureUrl()).into(imageView);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }else {
                    position++;
                }
            }
            return true;
        }
    }
}
