package com.victor.permissionsm;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera(v);
            }
        });

    }


    public void openCamera(View v) {
        //先判断系统版本是否为6.0或以上版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final String[] permissions = {Manifest.permission.CAMERA};
            // 第一步 检测权限:返回结果如下
            //PackageManager.PERMISSION_GRANTED(有权限)
            //PackageManager.PERMISSION_DENIED(无权限)
            int result = checkSelfPermission(permissions[0]);
            if (result == PackageManager.PERMISSION_GRANTED) {
                // 已经具有权限，直接打开相机
                startCamera();
            } else {
                // 第二步 申请权限
                // 判断是否需要向用户弹出权限需求说明
                if (shouldShowRequestPermissionRationale(permissions[0])) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("提示")
                            .setMessage("应用需要相机权限才能正常使用, 是否授权")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("授权", new DialogInterface.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissions, REQUEST_CODE);
                                }
                            })
                            .show();
                    return;// 结束执行，防止重复请求权限
                }
                requestPermissions(permissions, REQUEST_CODE);
            }
        } else {
            startCamera();
        }
    }

    // 第三步 监听请求权限的返回结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "获取到相机权限, 打开相机", Toast.LENGTH_SHORT).show();
                startCamera();
            } else {
                Toast.makeText(this, "获取相机权限失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }
}
