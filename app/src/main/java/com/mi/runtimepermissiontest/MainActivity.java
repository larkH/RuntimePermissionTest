package com.mi.runtimepermissiontest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button makeCall = findViewById(R.id.make_call);
        makeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);//用户是否授权
                //判断用户是否授权,0表示已授权
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    //如果未授权，则向用户申请授权，系统会弹出一个权限申请的对话框，然后用户选择同意或者拒绝后都会调用onRequestPermissionsResult方法
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    call();
                }
            }
        });
    }

    private void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:10086"));
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("PermissionsResult", "onRequestPermissionsResult: " + Arrays.toString(grantResults));
        switch (requestCode) {
            case 1:
                //grantResults为用户的授权结果，如果grantResults[0]＝0则表示用户同意授权，-1表示不同意
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}