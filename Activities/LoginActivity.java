package com.example.qyu.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.qyu.Model.Model;
import com.example.qyu.R;

public class LoginActivity extends AppCompatActivity {
    private EditText loginUsername;
    private EditText loginPass;
    private Button loginBtn;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginUsername = findViewById(R.id.loginUsername);
        loginPass = findViewById(R.id.loginPass);
        loginBtn = findViewById(R.id.loginButton);
        pb= findViewById(R.id.loginPB);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginUsername.getText().toString();
                String pass = loginPass.getText().toString();
                if (email.isEmpty() || pass.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please, Fill all the details!", Toast.LENGTH_SHORT).show();
                } else {
                    loginBtn.setVisibility(View.GONE);
                    pb.setVisibility(View.VISIBLE);
                    Model model = Model.getInstance(LoginActivity.this.getApplication());
                    model.login(LoginActivity.this, email, pass, loginBtn, pb);
                }
            }
        });
    }
}