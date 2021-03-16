package com.example.qyu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.qyu.model.api.WebApi;

import java.util.HashMap;
import java.util.Map;

public class OrgAccountActivity extends AppCompatActivity {
    private EditText orgAccName;
    private EditText orgAccEmail;
    private EditText orgID;
    private EditText orgAccAddress;
    private EditText orgAccContact;
    private Button editBtn;
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
        orgID.setText(WebApi.orgID);
        orgAccName.setText(WebApi.orgName);
        orgAccContact.setText(WebApi.orgContact);
        orgAccAddress.setText(WebApi.orgAddress);
        orgAccEmail.setText(WebApi.orgEmail);
    }
}