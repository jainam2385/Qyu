package com.example.qyu.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qyu.Model.Model;
import com.example.qyu.R;

import java.util.List;

public class WaitingAdapter extends ArrayAdapter<String> {
    Activity context;
    List<String> name;
    List<String> id;
    String eventID;
    AlertDialog.Builder dlgAlert;

    public WaitingAdapter(Activity context, List<String> id, List<String> name, String eventID){
        super(context, R.layout.listview_layout, name);
        this.context = context;
        this.name = name;
        this.id = id;
        this.eventID = eventID;

        dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setTitle("Qyu");
        dlgAlert.setCancelable(true);
        dlgAlert.create();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.org_queue_listview, null,true);
        TextView titleText = (TextView) rowView.findViewById(R.id.CustName);
        Button admit = rowView.findViewById(R.id.admitBtn);
        Button remove = rowView.findViewById(R.id.removeBtn);
        admit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgAlert.setMessage("Are you sure you want to admit the person?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Model model = Model.getInstance(context.getApplication());
                        model.statusChange(id.get(position), eventID, "C");
                    }
                });
                dlgAlert.show();
            }
        });
        titleText.setText(name.get(position));

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgAlert.setMessage("Are you sure you want to remove the person?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Model model = Model.getInstance(context.getApplication());
                                model.statusChange(id.get(position), eventID, "R");
                            }
                        });
                dlgAlert.show();
            }
        });

        return rowView;
    }
}
