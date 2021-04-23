package com.example.qyu.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qyu.Model.Model;
import com.example.qyu.R;

public class OrgRegisterActivity extends AppCompatActivity {
    private EditText orgName;
    private EditText orgEmail;
    private EditText orgPass;
    private EditText orgPhone;
    private EditText orgAddress;
    private EditText gstIN;
    private RequestQueue mRequestQueue;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private Button orgRegisterBtn, validateBtn;
    private ConstraintLayout gstCl, detailsCl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_register);

        gstCl = findViewById(R.id.gstCL);
        detailsCl = findViewById(R.id.detailsCL);
        validateBtn = findViewById(R.id.validateBtn);
        mRequestQueue = Volley.newRequestQueue(OrgRegisterActivity.this);

        progressDialog = new ProgressDialog(OrgRegisterActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Validating, Please Wait!");

        orgName = findViewById(R.id.orgName);
        orgEmail = findViewById(R.id.orgEmail);
        orgPass = findViewById(R.id.orgPass);
        orgAddress = findViewById(R.id.orgAddress);
        orgPhone = findViewById(R.id.orgPhone);
        progressBar = findViewById(R.id.orgRegPb);
        gstIN = findViewById(R.id.gstIN);

        final String regex = "^[0-9]{2}[A-Z]{5}[0-9]{4}" + "[A-Z]{1}[1-9A-Z]{1}" + "Z[0-9A-Z]{1}$";

        orgRegisterBtn = findViewById(R.id.orgRegisterBtn);

        orgRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = orgName.getText().toString();
                String email = orgEmail.getText().toString();
                String pass =  orgPass.getText().toString();
                String phone = orgPhone.getText().toString();
                String address =  orgAddress.getText().toString();
                String gstNo = gstIN.getText().toString();

                if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || phone.isEmpty() || address.isEmpty() || gstNo.isEmpty()){
                    Toast.makeText(OrgRegisterActivity.this, "Please, Fill all the details!", Toast.LENGTH_SHORT).show();
                } else if(gstIN.getText().toString().length() != 15 || (!gstIN.getText().toString().matches(regex))){
                    Toast.makeText(OrgRegisterActivity.this, "Enter valid GST Number!", Toast.LENGTH_SHORT).show();
                } else {
                    orgRegisterBtn.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    Model model = Model.getInstance(OrgRegisterActivity.this.getApplication());
                    model.orgRegister(OrgRegisterActivity.this, name, email, pass, phone, address, gstNo, orgRegisterBtn, progressBar);
                }
            }
        });

        validateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                if (!gstIN.getText().toString().equals("")) {
                    String url = "https://sheet.gstincheck.ml/check/74eb8aceca7f9978a43e9d1e65511385/" + gstIN.getText().toString();
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.substring(8, 12).equals("true")){
                                        gstCl.setVisibility(View.GONE);
                                        detailsCl.setVisibility(View.VISIBLE);
                                        Toast.makeText(OrgRegisterActivity.this, "Welcome to Qyu!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(OrgRegisterActivity.this, "Enter valid GST Number!", Toast.LENGTH_SHORT).show();
                                    }
                                    progressDialog.dismiss();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(OrgRegisterActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                                }
                            });
                    mRequestQueue.add(stringRequest);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(OrgRegisterActivity.this, "Please, Enter your GST Number!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}