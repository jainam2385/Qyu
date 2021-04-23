package com.example.qyu.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.qyu.Activities.LoginActivity;
import com.example.qyu.Activities.RegisterActivity;
import com.example.qyu.R;

public class CustomerFragment extends Fragment {
    private FrameLayout mainFL;
    private Button custLogin;
    private Button custReg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_customer, container, false);
        mainFL = getActivity().findViewById(R.id.mainFL);
        custLogin = v.findViewById(R.id.custLogin);
        custLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
            }
        });

        custReg = v.findViewById(R.id.custReg);
        custReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), RegisterActivity.class);
                startActivity(i);
            }
        });
        return v;
    }
}