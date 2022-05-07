package com.samsung.samsungproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.samsung.samsungproject.services.DatabaseService;

public class ChangeInfoActivity extends AppCompatActivity {

    EditText nameEditText;
    EditText infoEditText;
    ImageView imageView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        initViews();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickname = nameEditText.getText().toString();
                String info = infoEditText.getText().toString();
                DatabaseService.changeUser(nickname, info);
                finish();
            }
        });
    }

    void initViews() {
        nameEditText = findViewById(R.id.editTextName);
        infoEditText = findViewById(R.id.editTextInfo);
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.buttonApply);
    }
}