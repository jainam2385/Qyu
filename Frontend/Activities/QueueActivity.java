package com.example.qyu.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qyu.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class QueueActivity extends AppCompatActivity {
    private TextView cusToken, position, average;
    private RequestQueue mRequestQueue;
    String eventID, eventAvg;
    double avg;
    AlertDialog.Builder builder;
    SharedPreferences sp;
    String ID, orgID;
    EditText message;
    public static LinearLayout rate_now_container;
    Dialog dialog;
    Button leaveBtn;
    Timer timer;
    ProgressDialog progressDialog;
    String token = "TOKEN 1da4aeda9cec13222d59e4c9ad1cea4572a809db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        mRequestQueue = Volley.newRequestQueue(QueueActivity.this);
        dialog = new Dialog(QueueActivity.this);
        dialog.setContentView(R.layout.stars_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        message = dialog.findViewById(R.id.msgFB);

        progressDialog = new ProgressDialog(QueueActivity.this);
        progressDialog.setMessage("Rating!");

        leaveBtn = findViewById(R.id.leaveEvent);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        ID = sp.getString("ID", "");

        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Are you sure you want to leave the queue?").setTitle("Leave Queue");

        Intent i = getIntent();
        eventID = i.getStringExtra("eventID");
        eventAvg = i.getStringExtra("eventAvg");
        orgID = i.getStringExtra("orgID");

        avg = Double.parseDouble(eventAvg);

        cusToken = findViewById(R.id.token);
        position = findViewById(R.id.positionQueue);
        average = findViewById(R.id.averageTime);

        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 13) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        cusToken.setText("Token: " + salt.toString());

        final Handler handler = new Handler();
        timer = new Timer();
        TimerTask doTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {
                            setData();
                        } catch (Exception e) {
                            Toast.makeText(QueueActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        timer.schedule(doTask, 0, 15000);

        leaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveEvent();
            }
        });
    }

    private void leaveEvent() {
        dialog.show();
        int starPos = 0;
        rate_now_container = dialog.findViewById(R.id.rv_container);
        for (int x = 0; x < rate_now_container.getChildCount(); x++) {
            starPos = x;
            final int finalStarPos = starPos;
            rate_now_container.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRating(finalStarPos);
                }
            });
        }

        Button submitBtn = dialog.findViewById(R.id.starSubmitBtn);
        final int finalStarPos = starPos;
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                if (finalStarPos == 0){
                    Toast.makeText(QueueActivity.this, "Please, Provide a Feedback!", Toast.LENGTH_SHORT).show();
                } else {
                    saveRating(finalStarPos*2);
                    String url = "https://queueapplication.herokuapp.com/queueapi/queue-detail/?user_id=" + ID + "&event_id=" + eventID + "&status=L";
                    StringRequest requestL = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(QueueActivity.this, "You left the Queue!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            Intent i = new Intent(QueueActivity.this, EventsActivity.class);
                            timer.cancel();
                            startActivity(i);
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<String, String>();
                            headers.put("Authorization", token);
                            headers.put("Connection", "keep-alive");
                            return headers;
                        }
                    };
                    mRequestQueue.add(requestL);
                }
            }
        });


    }

    private void saveRating(final int starPos) {
        final String organizationID = Character.toString(orgID.charAt(0));
        final String msg;
        if (message.getText().toString().equals("")){
            msg="";
        } else {
            msg = message.getText().toString();
        }
        String url = "https://queueapplication.herokuapp.com/reviewapi/review-detail/";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(QueueActivity.this, "Thanks for your valuable feedback!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", token);
                headers.put("Connection", "keep-alive");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", sp.getString("ID", ""));
                params.put("organization_id", organizationID);
                params.put("rating", String.valueOf(starPos));
                params.put("message",  msg);
                return params;
            }
        };
        mRequestQueue.add(request);
    }

    private void setData() {
        String url = "https://queueapplication.herokuapp.com/queueapi/current-position/?user_id=" + ID + "&event_id=" + eventID;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String value = response.substring(1, response.length() - 1);
                String[] keyValuePairs = value.split(",");
                String[] values = new String[10];
                int j = 0;
                for (String pair : keyValuePairs) {
                    String[] entry = pair.split(":");
                    values[j] = entry[1].trim();
                    j++;
                }
                String pos = values[0];
                position.setText(pos);
                double position = Double.parseDouble(pos);
                average.setText("Waiting Time: " + (position * avg));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", token);
                headers.put("Connection", "keep-alive");
                return headers;
            }
        };
        mRequestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(QueueActivity.this, EventsActivity.class);
        timer.cancel();
        startActivity(i);
        finish();
    }

    public void setRating(final int starPos) {
        for (int x = 0; x < rate_now_container.getChildCount(); x++) {
            ImageView starView = (ImageView) rate_now_container.getChildAt(x);
            starView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= starPos) {
                starView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFBB00")));
            }
        }
    }
}