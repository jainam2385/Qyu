package com.example.qyu.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.qyu.Model.api.WebApi;
import com.example.qyu.R;

public class OrgAccountActivity extends AppCompatActivity {
    private EditText orgAccName;
    private EditText orgAccEmail;
    private EditText orgID;
    private EditText orgAccAddress;
    private EditText orgAccContact;
    private Button editBtn;
    SharedPreferences sp;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_account);
        orgAccName = findViewById(R.id.orgAccName);
        orgID = findViewById(R.id.orgIDET);
        orgAccAddress = findViewById(R.id.orgAccAddress);
        orgAccEmail = findViewById(R.id.orgAccEmail);
        orgAccContact = findViewById(R.id.orgAccContact);
        saveBtn = findViewById(R.id.orgSaveBtn);
        editBtn = findViewById(R.id.orgEditBtn);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        setDetails();
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBtn.setVisibility(View.VISIBLE);
                orgAccName.setEnabled(true);
                orgAccEmail.setEnabled(true);
                orgAccAddress.setEnabled(true);
                orgAccContact.setEnabled(true);
            }
        });
    }

    private void setDetails() {
        orgID.setText(sp.getString("ID", ""));
        orgAccName.setText(sp.getString("Name", ""));
        orgAccContact.setText(sp.getString("Contact", ""));
        orgAccAddress.setText(sp.getString("Address", ""));
        orgAccEmail.setText(sp.getString("Email", ""));
    }
}