package com.example.qyu.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qyu.Adapters.WaitingAdapter;
import com.example.qyu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class OrgQueueActivity extends AppCompatActivity {
    ListView listView;
    String eventID, eventAvg;
    Button endEventBtn;
    List<String> userID;
    TextView tvEventID, tvEventAvg;
    private RequestQueue mRequestQueue;
    String token = "TOKEN 1da4aeda9cec13222d59e4c9ad1cea4572a809db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_queue);
        listView = findViewById(R.id.queueLV);
        tvEventID = findViewById(R.id.eventID);
        tvEventAvg = findViewById(R.id.avgTime);
        endEventBtn = findViewById(R.id.endBtn);
        mRequestQueue = Volley.newRequestQueue(OrgQueueActivity.this);

        Intent i = getIntent();
        eventID = i.getStringExtra("eventID");
        eventAvg = i.getStringExtra("avg");

        endEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endEvent();
            }
        });

        tvEventID.setText("Event ID: #" + eventID);
        tvEventAvg.setText("Average Waiting Time: " + eventAvg +" min(s)");
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {
                            setUsers();
                        }
                        catch (Exception e) {
                            Toast.makeText(OrgQueueActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        timer.schedule(doTask, 0, 3000);
    }

    private void endEvent() {
        String url = "https://queueapplication.herokuapp.com/eventapi/event-detail/?event_id="+eventID;
        final StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(OrgQueueActivity.this, "Event Ended!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(OrgQueueActivity.this, OrgMainActivity.class);
                startActivity(i);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrgQueueActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
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
        mRequestQueue.add(stringRequest);
    }

    private void setUsers() {
        String url = "https://queueapplication.herokuapp.com/queueapi/waiting-users/?event_id="+eventID;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<String> eventNames = new ArrayList<String>();
                if (!response.equals("[]")) {
                    String value = response.substring(2, response.length()-2);
                    String[] keyValuePairs = value.split(",");
                    final List<String> values = new ArrayList<String>();
                    for (String pair : keyValuePairs) {
                        String[] entry = pair.split(":");
                        values.add(entry[1].trim());
                    }
                    int ex = values.size() / 4;
                    userID = new ArrayList<String>();
                    final List<String> name = new ArrayList<String>();
                    int i = 0;
                    while (ex > 0) {
                        userID.add(values.get(i+1));
                        name.add(values.get(i+2).substring(1, values.get(i+2).length()-1) + " " + values.get(i+3).substring(1, values.get(i+3).length()-1));
                        i += 4;
                        ex--;
                    }
                    final WaitingAdapter eventA = new WaitingAdapter(OrgQueueActivity.this, userID, name, eventID);
                    listView.setAdapter(eventA);
                } else {
                    eventNames.add("No Waiting Queue!");
                    listView.setBackgroundColor(getResources().getColor(R.color.white));
                    final ArrayAdapter<String> eventA = new ArrayAdapter<String>(OrgQueueActivity.this, R.layout.listview_custom_layout, eventNames);
                    listView.setAdapter(eventA);
                }
            }
        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OrgQueueActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
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
        mRequestQueue.add(stringRequest);
    }
}