package com.example.qyu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

public class UserTypeActivity extends AppCompatActivity {
    private FrameLayout mainFL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
        mainFL = findViewById(R.id.mainFL);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(mainFL.getId(), new GetStartedFragment());
            ft.commit();
    }
}