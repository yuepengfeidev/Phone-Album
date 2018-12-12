package com.example.a79875.phonealbum.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.a79875.phonealbum.R;
import com.example.a79875.phonealbum.entity.FolderData;

import java.util.List;

import javax.security.auth.callback.Callback;

// 文件夹 卡片式布局
public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<FolderData> folderList;
    private OnItemClickListen onItemClickListen;
    public static boolean layoutState = false; // 布局状态，false则是初始化的卡片布局，true则是列表布局

    public FolderAdapter(Context context, List<FolderData> folderData) {
        this.context = context;
        this.folderList = folderData;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (layoutState) {
            view = layoutInflater.inflate(R.layout.folder2_item, viewGroup, false);
        } else {
            view = layoutInflater.inflate(R.layout.folder_item, viewGroup, false);
        }
        return new ViewHolder(view,layoutState);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        FolderData folderData = folderList.get(i);
        if (layoutState) {
            viewHolder.folderName2.setText(folderData.getFolderName());
            viewHolder.photoCount2.setText(String.valueOf(folderData.getCount()));
            Glide.with(context).load(folderData.getLatestPhotoUrl())
                    .thumbnail(0.1f)
                    .apply(RequestOptions.centerCropTransform())
                    .into(viewHolder.latestImage2);
        } else {
            Glide.with(context).load(folderData.getLatestPhotoUrl())
                    .thumbnail(0.1f)
                    .apply(RequestOptions.centerCropTransform())
                    .into(viewHolder.latestPhoto);
            viewHolder.folderName.setText(folderData.getFolderName());
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListen.onClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView latestPhoto;
        TextView folderName;

        ImageView latestImage2;
        TextView folderName2;
        TextView photoCount2;

        public ViewHolder(@NonNull View itemView, boolean layoutState) {
            super(itemView);
            if (layoutState) {
                latestImage2 = itemView.findViewById(R.id.iv_latest2_photo);
                folderName2 = itemView.findViewById(R.id.tv_folder2_name);
                photoCount2 = itemView.findViewById(R.id.tv_count2_photo);
            } else {
                latestPhoto = itemView.findViewById(R.id.iv_latest_photo);
                folderName = itemView.findViewById(R.id.tv_folder_name);
            }
        }
    }

    public interface OnItemClickListen {
        void onClick(int position);
    }

    public void setOnItemClickListen(OnItemClickListen onItemClickListen) {
        this.onItemClickListen = onItemClickListen;
    }

}
