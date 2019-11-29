package com.bear.bp.ui.My;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bear.bp.R;
import com.bear.bp.StaticGlobal;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class MyFragment extends Fragment implements View.OnClickListener{

    private RelativeLayout collect;     // 收藏

    private RelativeLayout setting;     // 设置

    private RelativeLayout about;       // 关于

    private TextView user;              // 用户

    private ImageView imageView;        // 头像

    // 用户头像地址
    private String url = "http://182.92.159.2/pic/headImage/"+StaticGlobal.username + ".jpg" ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        initLayout(view);
        setListener();

        user.setText(StaticGlobal.username);

        return view;
    }
    // 寻找控件
    private void initLayout(View view){
        collect = view.findViewById(R.id.collect);
        setting = view.findViewById(R.id.setting);
        about = view.findViewById(R.id.about);
        user = view.findViewById(R.id.user);
        imageView = view.findViewById(R.id.icon_image);
    }
    // 设置监听事件
    private void setListener(){
        collect.setOnClickListener(this);
        setting.setOnClickListener(this);
        about.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentActivity activity = getActivity();
        switch (v.getId()){
            case R.id.collect:
                Intent intent1 = new Intent(activity, CollectActivity.class);
                startActivity(intent1);
                break;
            case R.id.setting:
                Intent intent2 = new Intent(activity, SettingActivity.class);
                startActivity(intent2);
                break;
            case R.id.about:
                Intent intent3 = new Intent(activity, AboutActivity.class);
                startActivity(intent3);
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.load)
                .error(R.drawable.my_account)      // 没有找到头像时的图片
                .skipMemoryCache(true)      // 跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // 不要再disk硬盘缓存
                .dontAnimate()                     // 解决加载过慢的问题
                .fitCenter()
                .circleCrop();

        Glide.with(getContext()).load(url).apply(options).into(imageView);
    }
}