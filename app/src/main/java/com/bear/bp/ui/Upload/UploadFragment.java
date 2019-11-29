package com.bear.bp.ui.Upload;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bear.bp.R;
import com.bear.bp.StaticGlobal;
import com.bear.bp.util.LoginRegisterServer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class UploadFragment extends Fragment implements View.OnClickListener{

    private ImageView chooseImage;          // 选择图片
    private ImageButton closeImage;         // 关闭图片

    private EditText title;                 // 标题
    private EditText description;           // 说明
    private EditText lable;                 // 标签

    private Button commit;                  // 提交

    private RequestOptions options;

    private static final String TAG = "UploadFragment";

    // 服务端地址
    private String url = "http://182.92.159.2/LoginDemo/AndroidTestDemo/UploadImageServlet?";

    // 图片地址
    private String imagePath;

    // 格式化系统时间
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        options = new RequestOptions()
                .skipMemoryCache(true)      // 跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // 不要再disk硬盘缓存
                .fitCenter();

        initLayout(view);
        setListener();

        return view;
    }

    // 控件绑定
    private void initLayout(View view){
        chooseImage = view.findViewById(R.id.choose_image);
        closeImage = view.findViewById(R.id.close_image);
        title = view.findViewById(R.id.input_title);
        description = view.findViewById(R.id.input_description);
        lable = view.findViewById(R.id.input_label);
        commit = view.findViewById(R.id.commit);
    }
    // 设置监听事件
    private void setListener(){
        chooseImage.setOnClickListener(this);
        closeImage.setOnClickListener(this);
        commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.choose_image:
                openAlbum();
                break;
            case R.id.close_image:
                // 关闭图片
                chooseImage.setImageDrawable(null);
                break;
            case R.id.commit:
                if (!TextUtils.isEmpty(lable.getText().toString())  && (!TextUtils.isEmpty(title.getText().toString()))
                        && (!TextUtils.isEmpty(description.getText().toString())) && !(chooseImage.getDrawable() == null)) {
                    Date date = new Date(System.currentTimeMillis());
                    String inputLable = lable.getText().toString();
                    String inputTitle = title.getText().toString();
                    String inputDescription = description.getText().toString();
                    String imageName = simpleDateFormat.format(date).replace(" ", "-") + ".jpg";
                    // 上传图片到服务器
                    LoginRegisterServer.uploadImage(url, imagePath, StaticGlobal.username,
                            inputLable, inputTitle, inputDescription, imageName);
                }else {
                    Toast.makeText(getContext(), "不能有空", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    // 从相册里选好图片后回调这个方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        //获取系统返回的照片的Uri
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        //从系统表中查询指定Uri对应的照片
                        Cursor cursor = getContext().getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        //获取照片路径
                        imagePath = cursor.getString(columnIndex);
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        // 显示图片
                        Glide.with(this)
                                .load(bitmap)
                                .apply(options)
                                .into(chooseImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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
}