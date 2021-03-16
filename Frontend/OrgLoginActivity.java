package com.example.qyu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.qyu.model.Model;

public class OrgLoginActivity extends AppCompatActivity {
    private EditText orgLoginEmail;
    private EditText orgLoginPass;
    private Button orgLoginButton;
    private ProgressBar orgLoginPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_login);
        orgLoginEmail = findViewById(R.id.orgLoginEmail);
        orgLoginPass = findViewById(R.id.orgLoginPass);
        orgLoginButton = findViewById(R.id.orgLoginButton);
        orgLoginPB = findViewById(R.id.orgLoginPB);

        orgLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orgLoginPB.setVisibility(View.VISIBLE);
                orgLoginButton.setVisibility(View.GONE);
                Model model = Model.getInstance(OrgLoginActivity.this.getApplication());
                model.orgLogin(OrgLoginActivity.this, orgLoginEmail.getText().toString(), orgLoginPass.getText().toString(), orgLoginButton, orgLoginPB);
            }
        });
    }
}