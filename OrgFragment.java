package com.example.qyu;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class OrgFragment extends Fragment {
    private Button orgRegBtn;
    private Button orgLogBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_org, container, false);
        orgRegBtn = v.findViewById(R.id.orgRegBtn);
        orgLogBtn = v.findViewById(R.id.orgLoginBtn);
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