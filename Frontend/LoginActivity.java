package com.example.qyu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.qyu.model.Model;

public class LoginActivity extends AppCompatActivity {
    private EditText loginUsername;
    private EditText loginPass;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginUsername = findViewById(R.id.loginUsername);
        loginPass = findViewById(R.id.loginPass);
        loginBtn = findViewById(R.id.loginButton);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginUsername.getText().toString();
                String pass = loginPass.getText().toString();
                Model model = Model.getInstance(LoginActivity.this.getApplication());
                model.login(email, pass);
            }
        });
    }
}