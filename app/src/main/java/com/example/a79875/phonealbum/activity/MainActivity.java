package com.example.a79875.phonealbum.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.a79875.phonealbum.adapter.FolderAdapter;
import com.example.a79875.phonealbum.entity.FolderData;
import com.example.a79875.phonealbum.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.example.a79875.phonealbum.utils.Camera.*;

// 文件夹列表
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView folderRecyclerView;
    private List<FolderData> folderList = new ArrayList<>();
    private FolderAdapter folderAdapter;
    private ImageButton camera;
    private ImageButton dynamicLayout;
    private ImageButton dynamicLayout2;

    // 相机请求码
    private static final int CAMERA_REQUEST_CODE = 1;
    // 调用照相机返回图片文件的临时地址
    private File tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        folderRecyclerView = findViewById(R.id.rv_folder);
        camera = findViewById(R.id.ib_camera);
        dynamicLayout2 = findViewById(R.id.ib_dynamic_layout2);
        dynamicLayout = findViewById(R.id.ib_dynamic_layout);

        // 先获取所有照片放在第一个文件夹里
        allPhoto(this);
        // 然后将所有带图片的文件加都加到列表里
        query();

        folderAdapter.layoutState = false;

        folderAdapter = new FolderAdapter(this, folderList);
        // 初始化为false，卡片布局
        folderRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        folderRecyclerView.setAdapter(folderAdapter);

        folderAdapter.setOnItemClickListen(new FolderAdapter.OnItemClickListen() {
            @Override
            public void onClick(int position) {
                if (position == 0) {// 第一个为所有照片目录
                    Intent intent = new Intent(MainActivity.this, AlbumActivity.class);
                    intent.putExtra("folderUrl", "所有照片");
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(MainActivity.this, AlbumActivity.class);
                    intent.putExtra("folderUrl", folderList.get(position).getFolderFileUrl());
                    startActivity(intent);
                }
            }
        });

        camera.setOnClickListener(this);
        dynamicLayout.setOnClickListener(this);
        dynamicLayout2.setOnClickListener(this);

    }

    public void query() {
        Cursor cursor = this.getContentResolver().query(
                MediaStore.Files.getContentUri("external"),
                new String[]{
                        "COUNT(" + MediaStore.Files.FileColumns.PARENT + ") AS fileCount",
                        MediaStore.Files.FileColumns.DATA + " FROM (SELECT *"},
                MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE + ")"
                        + " ORDER BY " + MediaStore.Files.FileColumns.DATE_MODIFIED + " )"
                        + " GROUP BY (" + MediaStore.Files.FileColumns.PARENT, null, "fileCount DESC"
        );
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int imageFileCountINFolder = cursor.getInt(0);
                String latestImageFilePath = cursor.getString(1);
                File folderFile = new File(latestImageFilePath).getParentFile();// 最近更新的图片地址的根目录

                folderList.add(new FolderData(latestImageFilePath, folderFile.getAbsolutePath(), imageFileCountINFolder));

            }
            cursor.close();
        }
    }

    public void allPhoto(Context context) {
        List<String> allPhotos = new ArrayList<>(); // 所有照片的路径

        Uri albumUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projAlbum = {MediaStore.Images.Media.DATA};

        Cursor mCursor = context.getContentResolver().query(albumUri, projAlbum, null,

                // 最后修改时间排序

                null, MediaStore.Images.Media.DATE_MODIFIED + " desc");

        assert mCursor != null;

        while (mCursor.moveToNext()) {
            // 获取照片路径
            String uri = mCursor.getString(0);
            allPhotos.add(uri);

        }
        folderList.add(new FolderData(allPhotos.get(0), "所有照片", allPhotos.size()));
        mCursor.close();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:// 调用相机后返回
                // 通知图库更新图片
                MediaScannerConnection.scanFile(this, new String[]{tempFile.toString()}, null, null);
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_camera:
                HashMap<String, Object> hashMap = getPicFromCamera(this);
                Intent intent = (Intent) hashMap.get("intent");
                tempFile = (File) hashMap.get("tempFile");
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
                break;
            case R.id.ib_dynamic_layout:
                folderAdapter.layoutState = false;
                dynamicLayout2.setVisibility(View.VISIBLE);
                dynamicLayout.setVisibility(View.GONE);
                folderRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                folderRecyclerView.setAdapter(folderAdapter);
                folderAdapter.notifyDataSetChanged();
                break;
            case R.id.ib_dynamic_layout2:
                folderAdapter.layoutState = true;
                dynamicLayout2.setVisibility(View.GONE);
                dynamicLayout.setVisibility(View.VISIBLE);
                folderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                folderRecyclerView.setAdapter(folderAdapter);
                folderAdapter.notifyDataSetChanged();
                break;
        }
    }

}
