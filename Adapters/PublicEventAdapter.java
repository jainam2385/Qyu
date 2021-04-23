package com.example.qyu.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class PublicEventAdapter extends ArrayAdapter<String> {
    Activity activity;
    List<String> title;
    List<String> eventID;
    List<String> eventDesc;
    List<String> eventMaxP;
    List<String> eventAvg;
    List<String> orgID;
    List<String> start;
    List<String> end;
    AlertDialog.Builder dlgAlert;
    ProgressDialog progressDialog;

    public PublicEventAdapter(Activity activity, List<String> title, List<String> id, List<String> desc, List<String> maxP, List<String> avg, List<String> orgID, List<String> start, List<String> end){
        super(activity, R.layout.listview_public_events, title);
        this.activity = activity;
        this.title = title;
        this.eventID = id;
        this.eventDesc = desc;
        this.eventMaxP = maxP;
        this.eventAvg = avg;
        this.orgID = orgID;
        this.start = start;
        this.end = end;

        dlgAlert = new AlertDialog.Builder(activity);
        dlgAlert.setMessage("Do you want to join the event?");
        dlgAlert.setTitle("Qyu");
        dlgAlert.setCancelable(true);
        dlgAlert.create();

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Joining Event!");
        progressDialog.setCancelable(false);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_public_events, null,true);
        TextView titleText = (TextView) rowView.findViewById(R.id.eventNamePub);
        Button button = rowView.findViewById(R.id.joinBtn);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.event_description_layout);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                TextView id = dialog.findViewById(R.id.eventID);
                id.setText("Event ID: " + eventID.get(position));
                TextView name = dialog.findViewById(R.id.eventName);
                name.setText("Event Name: " + title.get(position));
                TextView desc = dialog.findViewById(R.id.eventDesc);
                desc.setText("Event Description: " + eventDesc.get(position));
                TextView avg = dialog.findViewById(R.id.eventAvg);
                avg.setText("Average Waiting Time: " + eventAvg.get(position) + "min");
                TextView maxP = dialog.findViewById(R.id.eventMaxP);
                maxP.setText("Max Participants: " + eventMaxP.get(position));
                TextView startTV = dialog.findViewById(R.id.start);
                startTV.setText("Start Date / Time: " + start.get(position));
                TextView endTV = dialog.findViewById(R.id.end);
                endTV.setText("End Date / Time: " + end.get(position));
                dialog.show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.show();
                        Model model = Model.getInstance(activity.getApplication());
                        model.joinEvent(activity, eventID.get(position), eventAvg.get(position), progressDialog, orgID.get(position));
                    }
                });
                dlgAlert.show();
            }
        });
        titleText.setText(title.get(position));
        return rowView;
    }
}
