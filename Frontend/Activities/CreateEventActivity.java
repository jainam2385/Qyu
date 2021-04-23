package com.example.qyu.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qyu.Adapters.CustomDateTimePicker;
import com.example.qyu.Model.Model;
import com.example.qyu.R;

import java.util.Calendar;
import java.util.Date;

public class CreateEventActivity extends AppCompatActivity {
    private Button createBtn;
    private EditText eventName;
    private EditText eventDesc;
    private EditText noOfParticipants;
    private EditText avgWaitingTime;
    private EditText start;
    private EditText end;
    CustomDateTimePicker startTimePicker, EndTimePicker;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        getSupportActionBar().setTitle("Create an Event");
        eventName = findViewById(R.id.eventID);
        eventDesc = findViewById(R.id.eventDesc);
        noOfParticipants = findViewById(R.id.noOfP);
        avgWaitingTime = findViewById(R.id.avgTime);
        start = findViewById(R.id.startDate);
        end = findViewById(R.id.endDate);
        checkBox = findViewById(R.id.isPrivate);

        startTimePicker = new CustomDateTimePicker(this, new CustomDateTimePicker.ICustomDateTimeListener() {
            @Override
            public void onSet(Dialog dialog, Calendar calendarSelected, Date dateSelected, int year, String monthFullName, String monthShortName, int monthNumber, int date, String weekDayFullName, String weekDayShortName, int hour24, int hour12, int min, int sec, String AM_PM) {
                start.setText(year + "-" + (monthNumber + 1) + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH) + "T" + hour12 + ":" + min + ":" + sec + "Z");
            }

            @Override
            public void onCancel() {
            }
        });
        startTimePicker.set24HourFormat(false);
        startTimePicker.setDate(Calendar.getInstance());

        EndTimePicker = new CustomDateTimePicker(this, new CustomDateTimePicker.ICustomDateTimeListener() {
            @Override
            public void onSet(Dialog dialog, Calendar calendarSelected, Date dateSelected, int year, String monthFullName, String monthShortName, int monthNumber, int date, String weekDayFullName, String weekDayShortName, int hour24, int hour12, int min, int sec, String AM_PM) {
                end.setText(year + "-" + (monthNumber + 1) + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH) + "T" + hour12 + ":" + min + ":" + sec + "Z");
            }

            @Override
            public void onCancel() {
            }
        });
        EndTimePicker.set24HourFormat(false);
        EndTimePicker.setDate(Calendar.getInstance());

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimePicker.showDialog();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndTimePicker.showDialog();
            }
        });

        createBtn = findViewById(R.id.createBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model m = Model.getInstance(CreateEventActivity.this.getApplication());
                if (eventName.getText().toString().equals("") || eventDesc.getText().toString().equals("") || noOfParticipants.getText().toString().equals("") || avgWaitingTime.getText().toString().equals("")){
                    Toast.makeText(CreateEventActivity.this, "Please, Fill the details!", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkBox.isChecked()) {
                        m.createAnEvent(eventName.getText().toString(), eventDesc.getText().toString(), start.getText().toString(), end.getText().toString(), noOfParticipants.getText().toString(), avgWaitingTime.getText().toString(), "true");
                    } else {
                        m.createAnEvent(eventName.getText().toString(), eventDesc.getText().toString(), start.getText().toString(), end.getText().toString(), noOfParticipants.getText().toString(), avgWaitingTime.getText().toString(), "false");
                    }
                }
            }
        });
    }
}