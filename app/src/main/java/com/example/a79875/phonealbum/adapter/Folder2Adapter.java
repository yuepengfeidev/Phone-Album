package com.example.a79875.phonealbum.adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.a79875.phonealbum.R;
import com.example.a79875.phonealbum.entity.FolderData;

import java.util.ArrayList;
import java.util.List;

// 文件夹 列表式布局
public class Folder2Adapter extends RecyclerView.Adapter<Folder2Adapter.ViewHolder> {
    private List<FolderData> folderDataList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private Context context;
    private OnItemClickListener2 onItemClickListener2;

    /*public Folder2Adapter(Context context, List<FolderData> folderDataList){
        this.context = context;
        this.folderDataList = folderDataList;
        layoutInflater= LayoutInflater.from(context);
    }*/

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.folder2_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        FolderData folderData = folderDataList.get(i);
        viewHolder.folderName.setText(folderData.getFolderName());
        viewHolder.photoCount.setText(folderData.getCount());
        Glide.with(context).load(folderDataList.get(i).getFolderFileUrl())
                .thumbnail(0.1f)
                .apply(RequestOptions.centerCropTransform())
                .into(viewHolder.latestImage);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener2.onClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folderDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView latestImage;
        TextView folderName;
        TextView photoCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            latestImage = itemView.findViewById(R.id.iv_latest2_photo);
            folderName = itemView.findViewById(R.id.tv_folder2_name);
            photoCount = itemView.findViewById(R.id.tv_count2_photo);
        }
    }

    public interface OnItemClickListener2{
        void onClick(int position);
    }

    public void setOnItemClickListener2(OnItemClickListener2 onItemClickListener2){
        this.onItemClickListener2 = onItemClickListener2;
    }
}
