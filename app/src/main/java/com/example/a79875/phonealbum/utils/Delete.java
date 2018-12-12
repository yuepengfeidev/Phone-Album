package com.example.a79875.phonealbum.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.a79875.phonealbum.adapter.MyImageAdapter;
import com.example.a79875.phonealbum.entity.PhotoData;

import java.io.File;
import java.util.List;

public class Delete {

    // 删除该文件
    public static boolean deleteCurrentImage(Context context, String fileUrl) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = MediaStore.Images.Media.query(resolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=?",
                new String[]{fileUrl}, null);

        boolean result = false;
        if (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uri = ContentUris.withAppendedId(contentUri, id);
            int count = context.getContentResolver().delete(uri, null, null);
            result = count == 1;
        } else {
            File file = new File(fileUrl);
            result = file.delete();
        }
        cursor.close();
        return result;
    }
}
