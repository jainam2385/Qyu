package com.example.qyu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.qyu.model.Model;

public class OrgRegisterActivity extends AppCompatActivity {
    private EditText orgName;
    private EditText orgEmail;
    private EditText orgPass;
    private EditText orgPhone;
    private EditText orgAddress;
    private Button orgRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_register);
        orgName = findViewById(R.id.orgName);
        orgEmail = findViewById(R.id.orgEmail);
        orgPass = findViewById(R.id.orgPass);
        orgAddress = findViewById(R.id.orgAddress);
        orgPhone = findViewById(R.id.orgPhone);
        orgRegisterBtn = findViewById(R.id.orgRegisterBtn);

        orgRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model model = Model.getInstance(OrgRegisterActivity.this.getApplication());
                model.orgRegister(orgName.getText().toString(), orgEmail.getText().toString(), orgPass.getText().toString(), orgPhone.getText().toString(), orgAddress.getText().toString());
            }
        });
    }
}