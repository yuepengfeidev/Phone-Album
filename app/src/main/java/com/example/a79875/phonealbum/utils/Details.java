package com.example.a79875.phonealbum.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class Details {


    // 显示该文件详情
    public static HashMap<String, String> showDetails(Context context, String fileUrl) {
        HashMap<String, String> details = new HashMap<>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = MediaStore.Images.Media.query(resolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,
                MediaStore.Images.Media.DATA + "=?",
                new String[]{fileUrl},null);
        try {
            while (cursor.moveToNext()) {
                // 照片名称
                String fileTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                // 创建时间
                long date = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String createDate = formatter.format(date);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(fileUrl, options);

                // 照片类型
                String fileType = options.outMimeType;
                // 照片长度
                String fileLength = String.valueOf(options.outHeight);
                // 照片宽度
                String fileWidth = String.valueOf(options.outWidth);
                // 照片尺寸
                String fileDimensions = fileLength + "×" + fileWidth;

                File f = new File(fileUrl);
                FileInputStream fis = new FileInputStream(f);

                // 照片大小
                String size = String.valueOf(fis.available() / 1000);
                String fileSize = size + "KB";

                details.put("名称",fileTitle);
                details.put("时间",createDate);
                details.put("类型",fileType);
                details.put("尺寸",fileDimensions);
                details.put("大小",fileSize);

                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return details;
    }
}
