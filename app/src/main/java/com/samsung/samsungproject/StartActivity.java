package com.samsung.samsungproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.samsung.samsungproject.auth.CreateUserActivity;

public class StartActivity extends AppCompatActivity {

    ImageView imageView;
    private final int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initViews();

        new Handler().postDelayed(() -> {
            Intent i = new Intent(StartActivity.this, CreateUserActivity.class);
            startActivity(i);
            finish();
        }, SPLASH_TIME_OUT);
    }

    void initViews() {
        imageView = findViewById(R.id.imageView);
    }
}