package com.example.qyu;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public class GetStartedFragment extends Fragment {
    private FrameLayout mainFL;
    private Button custBtn;
    private Button orgBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_get_started, container, false);
        mainFL = getActivity().findViewById(R.id.mainFL);
        custBtn = v.findViewById(R.id.custBtn);
        custBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(mainFL, new CustomerFragment());
            }
        });

        orgBtn = v.findViewById(R.id.orgBtn);
        orgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(mainFL, new OrgFragment());
            }
        });

        return v;
    }

    private void setFragment(FrameLayout fl, Fragment fragment){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(fl.getId(), fragment);
        ft.commit();
    }
}