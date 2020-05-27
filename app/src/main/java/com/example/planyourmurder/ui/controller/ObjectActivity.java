package com.example.planyourmurder.ui.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.planyourmurder.R;

public class ObjectActivity extends AppCompatActivity {
    private TextView objectName;
    private String objectNamestr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object);

        this.objectName = findViewById(R.id.object_name_text_view);

        Intent intent = getIntent();
        if (intent.hasExtra("objectName")){
            objectNamestr = intent.getStringExtra("objectName");
            objectName.setText(objectNamestr);
        }
    }
}
