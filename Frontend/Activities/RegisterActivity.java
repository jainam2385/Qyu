package com.example.qyu.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qyu.Model.Model;
import com.example.qyu.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText regUsername;
    private EditText regFirstName;
    private EditText regLastName;
    private EditText regEmail;
    private EditText regPhone;
    private EditText regPass;
    private Button regBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regUsername = findViewById(R.id.regUsername);
        regFirstName = findViewById(R.id.regFN);
        regLastName = findViewById(R.id.regLN);
        regEmail = findViewById(R.id.regEmail);
        regPass = findViewById(R.id.regPass);
        regPhone = findViewById(R.id.regPhone);
        regBtn = findViewById(R.id.regBtn);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = regEmail.getText().toString();
                String username = regUsername.getText().toString();
                String firstName = regFirstName.getText().toString();
                String lastName = regLastName.getText().toString();
                String phone = regPhone.getText().toString();
                String password = regPass.getText().toString();
                if (email.isEmpty() || username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please, Fill all the details!", Toast.LENGTH_SHORT).show();
                } else {
                    Model model = Model.getInstance(RegisterActivity.this.getApplication());
                    model.register(username, firstName, lastName, email, password, phone);
                }
            }
        });
    }
}