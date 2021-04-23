package com.example.qyu.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.qyu.Activities.OrgLoginActivity;
import com.example.qyu.Activities.OrgRegisterActivity;
import com.example.qyu.R;

public class OrgFragment extends Fragment {
    private FrameLayout mainFL;
    private Button orgRegBtn;
    private Button orgLogBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_org, container, false);
        orgRegBtn = v.findViewById(R.id.orgRegBtn);
        orgLogBtn = v.findViewById(R.id.orgLoginBtn);
        mainFL = getActivity().findViewById(R.id.mainFL);
        orgRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), OrgRegisterActivity.class);
                startActivity(i);
            }
        });
        orgLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), OrgLoginActivity.class);
                startActivity(i);
            }
        });
        return v;
    }
}