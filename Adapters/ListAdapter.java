package com.example.qyu.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
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


public class ListAdapter extends ArrayAdapter<String> {
    Activity context;
    List<String> title;
    List<String> id;
    AlertDialog.Builder dlgAlert;

    public ListAdapter(Activity context, List<String> title, List<String> id){
        super(context, R.layout.listview_layout, title);
        this.context = context;
        this.title = title;
        this.id = id;

        dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage("Do you want to start the event?");
        dlgAlert.setTitle("Qyu");
        dlgAlert.setCancelable(true);
        dlgAlert.create();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_layout, null,true);
        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        Button button = rowView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgAlert.show();
                dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Model model = Model.getInstance(context.getApplication());
                        model.startAnEvent(id.get(position));
                        title.remove(position);
                    }
                });
            }
        });
        titleText.setText(title.get(position));

        return rowView;
    }
}
