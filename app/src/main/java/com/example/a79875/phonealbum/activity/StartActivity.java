package com.example.a79875.phonealbum.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.a79875.phonealbum.R;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        final Thread myThread = new Thread() {
            @Override
            public void run() {
                requestAllPower();
                try {
                    while (true) {
                    if (checkPermission()) {// 获取到权限后才能进入
                        sleep(1900);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        myThread.start();
    }

    public void requestAllPower() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PERMISSION_GRANTED) {
                    Toast.makeText(this, "" + "权限申请成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "" + "权限申请失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    public boolean checkPermission() {
        PackageManager packageManager = getPackageManager();
        boolean write = (PackageManager.PERMISSION_GRANTED ==
                packageManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        "com.example.a79875.phonealbum"));
        boolean read = (PackageManager.PERMISSION_GRANTED ==
                packageManager.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                        "com.example.a79875.phonealbum"));
        if (write && read) {
            return true;
        } else {
            return false;
        }
    }

}
