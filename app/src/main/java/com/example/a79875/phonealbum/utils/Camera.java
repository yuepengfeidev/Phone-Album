package com.example.a79875.phonealbum.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a79875.phonealbum.activity.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Camera {

    // 拍照
    public static HashMap<String,Object> getPicFromCamera(Context context) {
        HashMap<String,Object> hashMap = new HashMap<>();
        String fileName = System.currentTimeMillis() + ".png";
        // 用于保存调用相机拍照后生成的图片
        File tempFile = new File(Environment.getExternalStorageDirectory().getPath() + "/拍照",fileName );

        // 跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断版本，在7.0以上使用fileprovider 获取uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            Log.e("getPicFromAlbum", contentUri.toString());
        } else {// 否则使用Uri.fromFile(file)获取uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        hashMap.put("intent",intent);
        hashMap.put("tempFile",tempFile);

        return hashMap;
    }

}
