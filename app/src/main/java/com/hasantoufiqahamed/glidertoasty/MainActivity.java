package com.hasantoufiqahamed.glidertoasty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextInputLayout imageLink;
    private static final int CAMERA_REQUEST = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        imageLink = findViewById(R.id.imageLink);

        //Show a image in Glider from camera
        findViewById(R.id.buttonCamera).setOnClickListener(buttonCamera -> {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                requestPermission();
            }
        });

        //Show a image in Glider from url link
        findViewById(R.id.buttonLink).setOnClickListener(buttonLink -> {
            String link = imageLink.getEditText().getText().toString().trim();

            if (link.isEmpty()) {
                imageLink.setError("Enter image link");
                imageLink.requestFocus();
            } else {
                Glide.with(this).load(link).centerCrop().into(imageView);
            }
        });

        //toast using toasty custom library
        findViewById(R.id.buttonToasty).setOnClickListener(buttonToasty -> {
            Toasty.normal(getApplicationContext(), "Toast using toasty custom library").show();
        });
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, CAMERA_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==CAMERA_REQUEST){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                openCamera();
            }
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Glide.with(this).load(bitmap).centerCrop().into(imageView);
        }
    }
}