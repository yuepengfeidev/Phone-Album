package com.example.a79875.phonealbum.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.a79875.phonealbum.R;
import com.example.a79875.phonealbum.entity.PhotoData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private Context mContext;
    private List<PhotoData> photoList = new ArrayList();
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;
    public static HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
    public static boolean isChoose;

    public AlbumAdapter(Context context, List<PhotoData> photoList) {
        this.mContext = context;
        this.photoList = photoList;
        layoutInflater = LayoutInflater.from(context);
            init();
    }

    // 初始化，设置所有item都为未选择
    private void init() {
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < photoList.size(); i++) {
            isSelected.put(i, false);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.album_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof ViewHolder) {
            PhotoData photoData = photoList.get(i);
            Glide.with(mContext).load(photoData.getPhotoUrl())
                    .thumbnail(0.1f)
                    .apply(RequestOptions.centerCropTransform())
                    .into(viewHolder.photo);
            if (isChoose) {
                if (isSelected.get(i)) {
                    viewHolder.checkIcon.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.checkIcon.setVisibility(View.GONE);
                }
            }
            if (onItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onClick(i);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        ImageView checkIcon;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            photo = view.findViewById(R.id.iv_album_photo);
            checkIcon = view.findViewById(R.id.iv_check_icon);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
