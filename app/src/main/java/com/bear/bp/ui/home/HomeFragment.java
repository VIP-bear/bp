package com.bear.bp.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.bear.bp.R;
import com.bear.bp.StaticGlobal;
import com.bear.bp.adapter.PictureAdapter;
import com.bear.bp.data.Picture;
import com.bear.bp.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView imageListView;

    private PictureAdapter pictureAdapter;

    private static final String TAG = "HomeFragment";

    private String url = "http://182.92.159.2/LoginDemo/AndroidTestDemo/GetImageServlet?";

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    // 解析返回的数据并通知recycleView显示图片
                    Log.d(TAG, "handleMessage: "+(String)msg.obj);
                    HttpUtil.paserDataAndGetPictureUrl(StaticGlobal.pictureList, (String)msg.obj);
                    pictureAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // 加载布局
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 获取图片url
        if (StaticGlobal.pictureList.isEmpty()) {
            getAllImage(url, "allImage");
        }

        imageListView = view.findViewById(R.id.image_list_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        imageListView.setLayoutManager(layoutManager);
        pictureAdapter = new PictureAdapter(getContext(), StaticGlobal.pictureList);
        imageListView.setAdapter(pictureAdapter);

        return view;
    }

    // 获取全部图片
    private void getAllImage(String url, String requestType){
        final OkHttpClient client = new OkHttpClient();
        // 建立请求表单
        RequestBody formBody = new FormBody.Builder()
                .add("requestType", requestType)
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
                        handler.obtainMessage(0, response.body().string()).sendToTarget();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


}