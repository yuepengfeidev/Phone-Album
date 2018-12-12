package com.example.a79875.phonealbum.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a79875.phonealbum.R;
import com.example.a79875.phonealbum.adapter.AlbumAdapter;
import com.example.a79875.phonealbum.adapter.FolderAdapter;
import com.example.a79875.phonealbum.entity.FolderData;
import com.example.a79875.phonealbum.entity.PhotoData;
import com.example.a79875.phonealbum.utils.Delete;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// 照片列表
public class AlbumActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView photoAlbumRecyclerView;
    private List<PhotoData> photoList = new ArrayList<>();
    private AlbumAdapter albumAdapter;
    private String upTitle;
    private ImageButton ibBack;
    private TextView tvBack;
    private TextView title;
    String folderUrl;
    private TextView choose;
    boolean selectState = false; // 选择按钮，状态，true为选择图片，false为点击进入图片显示
    List<PhotoData> selectData = new ArrayList<>();
    Toolbar toolbar;

    private TextView cancel;
    private TextView isSelectCount;
    private ImageView share;
    private ImageView delete;
    Toolbar toolsToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        initView();
        initData();

    }

    private void initData() {
        folderUrl = getIntent().getStringExtra("folderUrl");

        newFolderName(folderUrl);

        title.setText(upTitle);

        photoUrlInCurrentFolder(folderUrl);

        photoAlbumRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        albumAdapter = new AlbumAdapter(this, photoList);
        photoAlbumRecyclerView.setAdapter(albumAdapter);

        albumAdapter.setOnItemClickListener(new AlbumAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (selectState) {// 点击选择后可进行选择图片
                    if (!albumAdapter.isSelected.get(position)) {
                        albumAdapter.isSelected.put(position, true); // 修改map的值保存状态
                        albumAdapter.notifyItemChanged(position);
                        selectData.add(photoList.get(position));


                    } else {
                        albumAdapter.isSelected.put(position, false); // 修改map的值保存状态
                        albumAdapter.notifyItemChanged(position);
                        selectData.remove(photoList.get(position));
                    }

                    isSelectCount.setText("已选中" + selectData.size() + "项");
                } else {// 不在选择状态则点击图片进入到图片显示
                    Intent intent = new Intent(AlbumActivity.this, PhotoViewActivity.class);
                    intent.putExtra("photoList", (Serializable) photoList);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            }
        });

        ibBack.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        cancel.setOnClickListener(this);
        choose.setOnClickListener(this);
        delete.setOnClickListener(this);
        share.setOnClickListener(this);
    }

    private void initView() {
        toolbar = findViewById(R.id.album_activity_toolbar);
        photoAlbumRecyclerView = findViewById(R.id.rv_album_photo);
        ibBack = findViewById(R.id.ib_album_activity_back);
        tvBack = findViewById(R.id.tv_album_activity_back);
        title = findViewById(R.id.album_activity_title);
        choose = findViewById(R.id.tv_bt_choose);

        toolsToolbar = findViewById(R.id.add_friend_tools_toolbar);
        cancel = findViewById(R.id.tv_bt_cancel);
        isSelectCount = findViewById(R.id.tv_bt_choose_count);
        share = findViewById(R.id.iv_share_choose);
        delete = findViewById(R.id.iv_delete_choose);
    }

    // 获取所有照片
    public void getAllPhotos() {

        Uri albumUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projAlbum = {MediaStore.Images.Media.DATA};

        Cursor mCursor = this.getContentResolver().query(albumUri, projAlbum, null,

                // 最后修改时间排序

                null, MediaStore.Images.Media.DATE_MODIFIED + " desc");

        assert mCursor != null;

        while (mCursor.moveToNext()) {
            // 获取所有照片路径
            String uri = mCursor.getString(0);
            photoList.add(new PhotoData(uri));

        }
    }

    // 获取当前文件夹中所有图片的路径
    public void photoUrlInCurrentFolder(String currentUrl) {
        photoList.clear();
        if (currentUrl.equals("所有照片")) {
            getAllPhotos();
        } else {
            Cursor cursor = getApplicationContext().getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null,
                    MediaStore.Images.ImageColumns.DATA + " like '%" + currentUrl + "%'",
                    null,
                    MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC"
            );
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    photoList.add(new PhotoData(cursor.getString(1)));
                }
            }
        }
    }

    // 根据路径截取名字
    private void newFolderName(String folderUrl) {
        int start = folderUrl.lastIndexOf("/");
        if (start != -1) {
            String newName = folderUrl.substring(start + 1, folderUrl.length());
            this.upTitle = newName;
        } else {
            this.upTitle = folderUrl;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_album_activity_back:
            case R.id.tv_album_activity_back:
                finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_bt_choose:// 点击选择，切换到选择图片状态，切换toolbar
                albumAdapter.isChoose = true;
                selectState = true;
                toolbar.setVisibility(View.GONE);
                toolsToolbar.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_bt_cancel:// 点击取消，切换toolbar,同时取消所有所选图片
                selectState = false;
                toolbar.setVisibility(View.VISIBLE);
                toolsToolbar.setVisibility(View.GONE);
                cancel();
                albumAdapter.isChoose = false;
                break;
            case R.id.iv_delete_choose:
                deleteChoose();
                albumAdapter.notifyDataSetChanged();

                // 删除完后 状态变成取消状态
                selectState = false;
                toolbar.setVisibility(View.VISIBLE);
                toolsToolbar.setVisibility(View.GONE);
                cancel();
                albumAdapter.isChoose = false;
                break;
            case R.id.iv_share_choose:
                sharechoose();

                // 分享完后 状态变成取消状态
                selectState = false;
                toolbar.setVisibility(View.VISIBLE);
                toolsToolbar.setVisibility(View.GONE);
                cancel();
                albumAdapter.isChoose = false;
                break;
        }
    }

    // 取消所有选择的图片
    public void cancel() {
        for (int i = 0; i < photoList.size(); i++) {
            if (albumAdapter.isSelected.get(i)) {
                albumAdapter.isSelected.put(i, false);
                selectData.clear();
            }
        }
        albumAdapter.notifyDataSetChanged();
        isSelectCount.setText("已选中" + selectData.size() + "项");
    }

    // 删除所选择的图片
    public void deleteChoose() {
        boolean result = true;
        for (PhotoData photoData : selectData) {
            result = Delete.deleteCurrentImage(this, photoData.getPhotoUrl());
            photoList.remove(photoData);
            // 通知item被移除
            albumAdapter.notifyItemRemoved(photoList.indexOf(photoData));
            albumAdapter.notifyItemRangeChanged(photoList.indexOf(photoData), photoList.size());
        }
        if (result) {
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
        }
    }

    public void sharechoose() {
        ArrayList<Uri> imageUris = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            for (PhotoData photoData : selectData){
                Uri imageContentUri  = FileProvider.getUriForFile(this,
                        this.getPackageName() + ".provider",new File(photoData.getPhotoUrl()));
                imageUris.add(imageContentUri);
            }
        } else {
            for (PhotoData photoData : selectData) {
                Uri uri = Uri.fromFile(new File(photoData.getPhotoUrl()));
                imageUris.add(uri);
            }
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent, "share"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        photoUrlInCurrentFolder(folderUrl);
        albumAdapter.notifyDataSetChanged();
    }
}
