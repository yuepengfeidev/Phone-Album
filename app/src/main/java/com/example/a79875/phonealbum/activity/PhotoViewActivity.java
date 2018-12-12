package com.example.a79875.phonealbum.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.a79875.phonealbum.R;
import com.example.a79875.phonealbum.adapter.MyImageAdapter;
import com.example.a79875.phonealbum.entity.PhotoData;
import com.example.a79875.phonealbum.utils.Delete;
import com.example.a79875.phonealbum.utils.Details;
import com.example.a79875.phonealbum.utils.FileUtils;
import com.example.a79875.phonealbum.widget.MyViewPager;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;

import java.io.File;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

// 图片显示
public class PhotoViewActivity extends AppCompatActivity implements View.OnClickListener {
    private MyViewPager myViewPager;
    private MyImageAdapter myImageAdapter;
    private List<PhotoData> photoDataList = new ArrayList<>();
    private int currentPosition;
    private ImageButton ibBack;
    private TextView countTitle;
    public static final int ACTION_REQUEST_EDITIMAGE = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_photo_view);

        Toolbar toolbar = findViewById(R.id.toolbar_photo_view);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.edit);
        getSupportActionBar().setDisplayShowTitleEnabled(false);// 隐藏默认标题栏

        this.photoDataList = (List<PhotoData>) getIntent().getSerializableExtra("photoList");
        this.currentPosition = getIntent().getIntExtra("position", 0);

        myViewPager = findViewById(R.id.vp_view_pager);
        ibBack = findViewById(R.id.ib_back);
        countTitle = findViewById(R.id.count_title);

        countTitle.setText((currentPosition + 1) + "/" + photoDataList.size());

        initData();

    }

    private void initData() {
        myImageAdapter = new MyImageAdapter(photoDataList, this);
        myViewPager.setAdapter(myImageAdapter);
        myViewPager.setCurrentItem(currentPosition, false);
        myViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                countTitle.setText((currentPosition + 1) + "/" + photoDataList.size());

            }
        });
        ibBack.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);//加载menu布局
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                editImageClick();
                finish();// 编辑完后退出到照片列表，更新出编辑后的照片
                break;
            case R.id.details:
                Intent intent = new Intent(this,DetailsActivity.class);
                intent.putExtra("fileUrl",photoDataList.get(currentPosition).getPhotoUrl());
                startActivity(intent);
                break;
            case R.id.share:
                share();
                break;
            case R.id.delete:
                boolean result = Delete.deleteCurrentImage(this, photoDataList.get(currentPosition).getPhotoUrl());
                if (result) {
                    photoDataList.remove(currentPosition);
                    myImageAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 分享
    private void share() {
        Uri imageUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

               Uri imageContentUri  = FileProvider.getUriForFile(this,
                        this.getPackageName() + ".provider",new File(photoDataList.get(currentPosition).getPhotoUrl()));
               imageUri = imageContentUri;
        } else {
                Uri uri = Uri.fromFile(new File(photoDataList.get(currentPosition).getPhotoUrl()));
                imageUri = uri;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM,imageUri);
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent,"share"));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
        }
    }

    // 开源图片编辑控件 imageeditor
    private void editImageClick() {
        File outputFile = FileUtils.genEditFile();
        EditImageActivity.start(this,photoDataList.get(currentPosition).getPhotoUrl()
                ,outputFile.getAbsolutePath(),ACTION_REQUEST_EDITIMAGE);
    }

}
