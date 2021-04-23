package com.example.qyu.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.qyu.Fragments.GetStartedFragment;
import com.example.qyu.R;

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