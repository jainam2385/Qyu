package com.example.qyu.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qyu.Adapters.PublicEventAdapter;
import com.example.qyu.Model.Model;
import com.example.qyu.R;
import com.google.android.gms.common.internal.Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventsActivity extends AppCompatActivity {
    Spinner spin;
    ListView listView;
    TextView secretEvent;
    SharedPreferences sp;
    ProgressDialog progressDialog;
    private RequestQueue mRequestQueue;
    String[] events = {"Ongoing", "Upcoming", "Waiting"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        spin = findViewById(R.id.custMainSpin);
        secretEvent = findViewById(R.id.secretEvent);
        sp = getSharedPreferences("login", MODE_PRIVATE);

        progressDialog = new ProgressDialog(EventsActivity.this);
        progressDialog.setMessage("Registering!");
        progressDialog.setCancelable(false);

        mRequestQueue = Volley.newRequestQueue(EventsActivity.this);
        final ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, events);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(ad);
        listView = findViewById(R.id.custEventLV);
        listView.setDividerHeight(12);

        secretEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventsActivity.this, SecretActivity.class);
                startActivity(i);
            }
        });

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String token = "TOKEN 1da4aeda9cec13222d59e4c9ad1cea4572a809db";
                switch (position) {
                    case 0:
                        String urlForA = "https://queueapplication.herokuapp.com/eventapi/public-events/?status=A";
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlForA,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        List<String> eventNames = new ArrayList<String>();
                                        listView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                        if (!response.equals("[]")) {
                                            String value = response.substring(2, response.length() - 2);
                                            String[] keyValuePairs = value.split(",");
                                            final List<String> values = new ArrayList<String>();
                                            for (String pair : keyValuePairs) {
                                                String[] entry = pair.split(":");
                                                values.add(entry[1].trim());
                                            }
                                            int ex = values.size() / 11;
                                            values.add("end");
                                            final List<String> eventID = new ArrayList<String>();
                                            final List<String> eventDesc = new ArrayList<String>();
                                            final List<String> eventMaxP = new ArrayList<String>();
                                            final List<String> eventAvg = new ArrayList<String>();
                                            final List<String> orgID = new ArrayList<String>();
                                            final List<String> start = new ArrayList<String>();
                                            final List<String> end = new ArrayList<String>();

                                            int i = 1;
                                            int k = 9;
                                            while (ex > 0) {
                                                eventNames.add(values.get(i).substring(1, values.get(i).length() - 1));
                                                eventID.add(values.get(i - 1));
                                                eventDesc.add(values.get(i + 1));
                                                start.add(values.get(i + 2));
                                                end.add(values.get(i + 3));
                                                eventMaxP.add(values.get(i + 4));
                                                eventAvg.add(values.get(i + 5));
                                                orgID.add(values.get(i + k));
                                                i += 11;
                                                k--;
                                                ex--;
                                            }
                                            final PublicEventAdapter eventA = new PublicEventAdapter(EventsActivity.this, eventNames, eventID, eventDesc, eventMaxP, eventAvg, orgID, start, end);
                                            listView.setAdapter(eventA);
                                        } else {
                                            eventNames.add("No Ongoing Events!");
                                            final ArrayAdapter<String> eventA = new ArrayAdapter<String>(EventsActivity.this, R.layout.listview_custom_layout, eventNames);
                                            listView.setAdapter(eventA);
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(EventsActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
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
                        break;
                    case 1:
                        listView.setOnItemClickListener(null);
                        String urlForR = "https://queueapplication.herokuapp.com/eventapi/public-events/?status=R";
                        StringRequest stringRequestR = new StringRequest(Request.Method.GET, urlForR,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        final List<String> eventNames = new ArrayList<String>();
                                        final List<String> eventID = new ArrayList<String>();
                                        listView.setBackgroundColor(getResources().getColor(R.color.white));
                                        final ArrayAdapter<String> eventR = new ArrayAdapter<String>(EventsActivity.this, R.layout.listview_custom_layout, eventNames);
                                        if (response.equals("[]")) {
                                            final ArrayAdapter<String> eventD = new ArrayAdapter<String>(EventsActivity.this, R.layout.listview_custom_layout, eventNames);
                                            eventNames.add("No Upcoming Events!");
                                            listView.setAdapter(eventD);
                                        } else {
                                            final String value = response.substring(2, response.length() - 2);
                                            String[] keyValuePairs = value.split(",");
                                            final List<String> values = new ArrayList<String>();
                                            for (String pair : keyValuePairs) {
                                                String[] entry = pair.split(":");
                                                values.add(entry[1].trim());
                                            }
                                            int ex = values.size() / 11;
                                            int i = 1;
                                            while (ex > 0) {
                                                eventNames.add(values.get(i).substring(1, values.get(i).length() - 1) + " (Click to register)");
                                                eventID.add(values.get(i - 1));
                                                i += 12;
                                                ex--;
                                            }
                                            listView.setAdapter(eventR);
                                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    Model model = Model.getInstance(getApplication());
                                                    model.registerEvent(EventsActivity.this, eventID.get(position), progressDialog);
                                                    eventNames.remove(position);
                                                    eventR.notifyDataSetChanged();
                                                }
                                            });
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(EventsActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
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
                        mRequestQueue.add(stringRequestR);
                        break;
                    case 2:
                        listView.setOnItemClickListener(null);
                        String url = "https://queueapplication.herokuapp.com/queueapi/user-event-logs/?user_id=" + sp.getString("ID", "") + "&status=W";
                        StringRequest stringrequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                List<String> eventNames = new ArrayList<String>();
                                listView.setBackgroundColor(getResources().getColor(R.color.white));
                                if (!response.equals("[]")) {
                                    String value = response.substring(2, response.length() - 2);
                                    String[] keyValuePairs = value.split(",");
                                    final List<String> values = new ArrayList<String>();
                                    for (String pair : keyValuePairs) {
                                        String[] entry = pair.split(":");
                                        values.add(entry[1].trim());
                                    }
                                    int ex = values.size() / 11;
                                    values.add("end");
                                    final List<String> eventID = new ArrayList<String>();
                                    final List<String> eventDesc = new ArrayList<String>();
                                    final List<String> eventMaxP = new ArrayList<String>();
                                    final List<String> eventAvg = new ArrayList<String>();
                                    final List<String> orgID = new ArrayList<String>();

                                    int i = 1;
                                    int k = 9;
                                    while (ex > 0) {
                                        eventNames.add(values.get(i).substring(1, values.get(i).length() - 1));
                                        eventID.add(values.get(i - 1));
                                        eventDesc.add(values.get(i + 1));
                                        eventMaxP.add(values.get(i + 4));
                                        eventAvg.add(values.get(i + 5));
                                        orgID.add(values.get(i + k));
                                        i += 12;
                                        k--;
                                        ex--;
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(EventsActivity.this, R.layout.listview_custom_layout, eventNames);
                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent i = new Intent(EventsActivity.this, QueueActivity.class);
                                            i.putExtra("eventID", eventID.get(position));
                                            i.putExtra("eventAvg", eventAvg.get(position));
                                            i.putExtra("orgID", orgID.get(position));
                                            startActivity(i);
                                            finish();
                                        }
                                    });
                                } else {
                                    eventNames.add("No Waiting Events!");
                                    final ArrayAdapter<String> eventA = new ArrayAdapter<String>(EventsActivity.this, R.layout.listview_custom_layout, eventNames);
                                    listView.setAdapter(eventA);
                                }
                            }
                        },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(EventsActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
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
                        mRequestQueue.add(stringrequest);
                        break;
                    default:
                        listView.setAdapter(null);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}