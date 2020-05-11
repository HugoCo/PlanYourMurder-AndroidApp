package com.example.planyourmurder.ui.controller;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.planyourmurder.R;

public class NameActivity extends AppCompatActivity {

    private Button button_confirm;
    private EditText edit_name;
    private EditText edit_password;
    public static final int HOME_PAGE_ACTIVITY_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        this.button_confirm= findViewById(R.id.activity_name_button_confirm);
        this.edit_name=findViewById(R.id.activity_name_editText);
        this.edit_password=findViewById(R.id.activity_password_editText);


        button_confirm.setEnabled(false);

        edit_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                button_confirm.setEnabled(s.toString().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edit_name.getText().toString();
                String password = edit_password.getText().toString();
                Intent homePageIntent = new Intent(NameActivity.this, HomePageActivity.class);
                homePageIntent.putExtra("name", name);
                startActivity(homePageIntent);

            }
        });
    }

}

