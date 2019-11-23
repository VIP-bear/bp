package com.bear.bp.ui.My;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bear.bp.R;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        initLayout(view);
        setListener();

        return view;
    }
    // 寻找控件
    private void initLayout(View view){
        collect = view.findViewById(R.id.collect);
        setting = view.findViewById(R.id.setting);
        about = view.findViewById(R.id.about);
    }
    // 设置监听事件
    private void setListener(){
        collect.setOnClickListener(this);
        setting.setOnClickListener(this);
        about.setOnClickListener(this);
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
}