package com.example.a79875.phonealbum.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.a79875.phonealbum.entity.PhotoData;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class MyImageAdapter extends PagerAdapter {
    private List<PhotoData> photoDataList;
    private AppCompatActivity appCompatActivity;

    public MyImageAdapter(List<PhotoData> photoDataList , AppCompatActivity appCompatActivity){
        this.photoDataList = photoDataList;
        this.appCompatActivity = appCompatActivity;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        String url = photoDataList.get(position).getPhotoUrl();
        PhotoView photoView =  new PhotoView(appCompatActivity);
        Glide.with(appCompatActivity)
                .load(url)
                .apply(RequestOptions.centerInsideTransform())
                .into(photoView);
        container.addView(photoView);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appCompatActivity.finish();
            }
        });
        return photoView;
    }

    @Override
    public int getCount() {
        return photoDataList != null ? photoDataList.size(): 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
