package com.example.qyu.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qyu.Model.Model;
import com.example.qyu.R;

import java.util.List;


public class SubscribedAdapter extends ArrayAdapter<String> {
    Activity activity;
    List<String> orgNames;
    List<String> orgID;
    AlertDialog.Builder dlgAlert;

    public SubscribedAdapter(Activity activity, List<String> title, List<String> id) {
        super(activity, R.layout.unsubscribe_listview_layout, title);
        this.activity = activity;
        this.orgNames = title;
        this.orgID = id;

        dlgAlert = new AlertDialog.Builder(activity);
        dlgAlert.setMessage("Do you want to unsubscribe the organization?");
        dlgAlert.setTitle("Qyu");
        dlgAlert.setCancelable(true);
        dlgAlert.create();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.unsubscribe_listview_layout, null,true);
        TextView titleText = (TextView) rowView.findViewById(R.id.orgnameUnsub);
        Button button = rowView.findViewById(R.id.unSubBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Model model = Model.getInstance(activity.getApplication());
                        model.unsubscribeOrg(activity, orgID.get(position));
                    }
                });
                dlgAlert.show();
            }
        });
        titleText.setText(orgNames.get(position));
        return rowView;
    }
}
