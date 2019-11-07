package com.bear.bp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bear.bp.PictureActivity;
import com.bear.bp.R;
import com.bear.bp.data.Picture;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder>{

    private Context context;

    private List<Picture> pictureList;

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView picture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.picture = itemView.findViewById(R.id.picture);
        }
    }

    public PictureAdapter(Context context, List<Picture> pictureList) {
        this.context = context;
        this.pictureList = pictureList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_item,
                parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Picture picture = pictureList.get(position);

        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.load)        // 加载图片时的图片
                .error(R.drawable.error);           // 加载失败时的图片

        Glide.with(context).load(picture.getPictureUrl()).apply(options).into(holder.picture);
        // 监听图片的点击事件
        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PictureActivity.class);
                intent.putExtra("pictureMessage", picture);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }
}
