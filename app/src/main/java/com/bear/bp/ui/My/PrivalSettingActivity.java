package com.bear.bp.ui.My;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bear.bp.R;
import com.bear.bp.StaticGlobal;
import com.bear.bp.util.LoginRegisterServer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class PrivalSettingActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView headPortrait;     // 头像
    private ImageView back;             // 返回
    private RelativeLayout background;  // 背景
    private String fileName = StaticGlobal.username + ".jpg";
    private String name = "uploadHeadImage";

    private RequestOptions options;

    // 服务端地址
    private String url = "http://182.92.159.2/LoginDemo/AndroidTestDemo/UploadHeadImageServlet?";
    // 个人头像地址
    private String headImageUrl = "http://182.92.159.2/pic/headImage/"+StaticGlobal.username + ".jpg" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prival_setting);
        // 隐藏actionbar
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        initLayout();
        setListener();

        options = new RequestOptions()
                .placeholder(R.drawable.load)
                .error(R.drawable.my_account)      // 没有找到头像时的图片
                .skipMemoryCache(true)      // 跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // 不要再disk硬盘缓存
                .fitCenter()
                .circleCrop();
        Glide.with(this).load(headImageUrl).apply(options).into(headPortrait);

    }
    // 寻找各个控件
    private void initLayout(){
        back = findViewById(R.id.from_privalsetting_back_setting);
        headPortrait = findViewById(R.id.head_portrait);
        background = findViewById(R.id.my_background);
    }
    // 设置监听事件
    private void setListener(){
        back.setOnClickListener(this);
        headPortrait.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.from_privalsetting_back_setting:
                Intent intent = new Intent(PrivalSettingActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.head_portrait:
                openAlbum();
                break;
            default:
                break;
        }
    }

    // 打开相册
    private void openAlbum(){
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    // 从相册里选好图片后回调这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                         //获取系统返回的照片的Uri
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        //从系统表中查询指定Uri对应的照片
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        //获取照片路径
                        String path = cursor.getString(columnIndex);
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        // 显示图片
                        Glide.with(this)
                                .load(bitmap)
                                .apply(options)
                                .into(headPortrait);

                        // 上传图片到服务器
                        LoginRegisterServer.uploadHeadImage(url, path, name, fileName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

}
