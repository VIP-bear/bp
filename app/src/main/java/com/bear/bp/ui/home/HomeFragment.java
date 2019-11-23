package com.bear.bp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bear.bp.R;
import com.bear.bp.StaticGlobal;
import com.bear.bp.adapter.PictureAdapter;
import com.bear.bp.data.Picture;
import com.bear.bp.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView imageListView;

    private PictureAdapter pictureAdapter;

    private static final String TAG = "HomeFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // 加载布局
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 获取图片url
        if (StaticGlobal.pictureList.isEmpty()) {
            HttpUtil.sendRequestWithOkHttp();
            HttpUtil.paserDataAndGetPictureUrl(StaticGlobal.pictureList);
        }

        Log.d(TAG, "onCreateView: "+"excuted");

        imageListView = view.findViewById(R.id.image_list_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        imageListView.setLayoutManager(layoutManager);
        pictureAdapter = new PictureAdapter(getContext(), StaticGlobal.pictureList);
        imageListView.setAdapter(pictureAdapter);

        return view;
    }

}