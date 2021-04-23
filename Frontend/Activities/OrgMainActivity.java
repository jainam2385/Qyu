package com.example.qyu.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qyu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrgMainActivity extends AppCompatActivity {
    private ImageView createAnEventBtn;
    private ImageView orgMyAccountBtn;
    private ImageView dashboard;
    private ImageView logout;
    private ImageView msgBtn;
    SharedPreferences sp;
    Spinner spin;
    ListView listView;
    private RequestQueue mRequestQueue;
    String[] events = {"Ongoing", "Upcoming", "Archived"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_main);
        spin = findViewById(R.id.spinner);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        mRequestQueue = Volley.newRequestQueue(OrgMainActivity.this);
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, events);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(ad);
        listView = findViewById(R.id.listView);
        listView.setDividerHeight(12);
        createAnEventBtn = findViewById(R.id.createAnEventBtn);
        createAnEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrgMainActivity.this, CreateEventActivity.class);
                startActivity(i);
            }
        });

        dashboard = findViewById(R.id.orgDashboard);
        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrgMainActivity.this, DashboardActivity.class);
                startActivity(i);
            }
        });

        orgMyAccountBtn = findViewById(R.id.orgMyAccount);
        orgMyAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrgMainActivity.this, OrgAccountActivity.class);
                startActivity(i);
            }
        });

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp;
                sp = getSharedPreferences("login",MODE_PRIVATE);
                sp.edit().putBoolean("isloggedin",false).apply();
                sp.edit().putBoolean("TYPE",false).apply();
                Intent i = new Intent(OrgMainActivity.this, UserTypeActivity.class);
                startActivity(i);
                finish();
            }
        });

        msgBtn = findViewById(R.id.msgBtn);
        msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrgMainActivity.this, MessageActivity.class);
                startActivity(i);
            }
        });

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String token = "TOKEN 1da4aeda9cec13222d59e4c9ad1cea4572a809db";
                switch (position){
                    case 0:
                        String urlForA = "https://queueapplication.herokuapp.com/organizationapi/organization-events/?organization_id="+sp.getString("ID", "")+"&status=A";
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlForA,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        List<String> eventNames = new ArrayList<String>();
                                        listView.setBackgroundColor(getResources().getColor(R.color.lightACCENT));
                                        if (!response.equals("[]")) {
                                            String value = response.substring(2, response.length()-2);
                                            String[] keyValuePairs = value.split(",");
                                            final List<String> values = new ArrayList<String>();
                                            for (String pair : keyValuePairs) {
                                                String[] entry = pair.split(":");
                                                values.add(entry[1].trim());
                                            }
                                            int ex = values.size() / 11;
                                            final List<String> eventID = new ArrayList<String>();
                                            final List<String> eventAvg = new ArrayList<String>();
                                            int i = 1;
                                            while (ex > 0) {
                                                eventNames.add(values.get(i).substring(1, values.get(i).length()-1));
                                                eventID.add(values.get(i - 1));
                                                eventAvg.add(values.get(i+5));
                                                i += 11;
                                                ex--;
                                            }
                                            final ArrayAdapter<String> eventA = new ArrayAdapter<String>(OrgMainActivity.this, R.layout.listview_custom_layout, eventNames);
                                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    Intent i = new Intent(OrgMainActivity.this, OrgQueueActivity.class);
                                                    i.putExtra("eventID", eventID.get(position));
                                                    i.putExtra("avg", eventAvg.get(position));
                                                    startActivity(i);
                                                }
                                            });
                                            listView.setAdapter(eventA);
                                        } else {
                                            eventNames.add("No Ongoing Events!");
                                            final ArrayAdapter<String> eventA = new ArrayAdapter<String>(OrgMainActivity.this, R.layout.listview_custom_layout, eventNames);
                                            listView.setAdapter(eventA);
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(OrgMainActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
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
                        String urlForR = "https://queueapplication.herokuapp.com/organizationapi/organization-events/?organization_id="+sp.getString("ID", "")+"&status=R";
                        StringRequest stringRequestR = new StringRequest(Request.Method.GET, urlForR,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        final List<String> eventNames = new ArrayList<String>();
                                        final List<String> eventID = new ArrayList<String>();
                                        listView.setBackgroundColor(getResources().getColor(R.color.lightGREEN));
                                        final ListAdapter eventR = new com.example.qyu.Adapters.ListAdapter(OrgMainActivity.this, eventNames, eventID);
                                        if (response.equals("[]")){
                                            final ArrayAdapter<String> eventD = new ArrayAdapter<String>(OrgMainActivity.this, R.layout.listview_custom_layout, eventNames);
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
                                                eventNames.add(values.get(i).substring(1, values.get(i).length()-1));
                                                eventID.add(values.get(i - 1));
                                                i += 11;
                                                ex--;
                                            }
                                            listView.setAdapter(eventR);
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(OrgMainActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
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
                        final List<String> eventNames = new ArrayList<String>();
                        listView.setBackgroundColor(getResources().getColor(R.color.lightRED));
                        final ArrayAdapter<String> eventD = new ArrayAdapter<String>(OrgMainActivity.this, R.layout.listview_custom_layout, eventNames);
                        String urlForD = "https://queueapplication.herokuapp.com/organizationapi/organization-events/?organization_id="+sp.getString("ID", "")+"&status=D";
                        StringRequest stringRequestD = new StringRequest(Request.Method.GET, urlForD,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("[]")) {
                                            eventNames.add("No Archived Events!");
                                        } else {
                                            String value = response.substring(1, response.length() - 1);
                                            String[] keyValuePairs = value.split(",");
                                            final List<String> values = new ArrayList<String>();
                                            int j = 0;
                                            for (String pair : keyValuePairs) {
                                                String[] entry = pair.split(":");
                                                values.add(entry[1].trim());
                                                j++;
                                            }
                                            int ex = values.size() / 11;
                                            int i = 1;
                                            while (ex > 0) {
                                                eventNames.add(values.get(i).substring(1, values.get(i).length()-1));
                                                i += 11;
                                                ex--;
                                            }
                                        }
                                        listView.setAdapter(eventD);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(OrgMainActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
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
                        mRequestQueue.add(stringRequestD);
                        break;
                    default:
                        listView.setAdapter(null);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}