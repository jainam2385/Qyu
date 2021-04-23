package com.example.qyu.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
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

public class MainActivity extends AppCompatActivity {
    private ImageView logoutBtn, eventBtn, subscribedBtn;
    SearchView searchView;
    ListView listView;
    RequestQueue mRequestQueue;
    final List<String> orgNames = new ArrayList<String>();
    SharedPreferences sp;
    ArrayAdapter<String> adapter;
    final List<String> orgID = new ArrayList<String>();
    String token = "TOKEN 1da4aeda9cec13222d59e4c9ad1cea4572a809db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRequestQueue = Volley.newRequestQueue(MainActivity.this);
        searchView = findViewById(R.id.searchView);
        listView = findViewById(R.id.organizationNamesLV);
        eventBtn = findViewById(R.id.userEvents);
        subscribedBtn = findViewById(R.id.subscribedOrg);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        getOrganizationNames();
        eventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, EventsActivity.class);
                startActivity(i);
            }
        });

        subscribedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SubscribedActivity.class);
                startActivity(i);
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String urlForSub = "https://queueapplication.herokuapp.com/subscriptionapi/subscription-detail/";
                StringRequest stringRequestSub = new StringRequest(Request.Method.POST, urlForSub,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(MainActivity.this, "Subscribed Successfully!", Toast.LENGTH_SHORT).show();
                                orgNames.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
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
                        params.put("organization_id", orgID.get(position));
                        params.put("user_id", sp.getString("ID", ""));
                        return params;
                    }
                };
                mRequestQueue.add(stringRequestSub);
            }
        });

        logoutBtn = findViewById(R.id.custLogoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putBoolean("isloggedin", false).apply();
                sp.edit().putBoolean("TYPE", false).apply();
                Intent i = new Intent(MainActivity.this, UserTypeActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void getOrganizationNames() {
        listView.setBackgroundColor(getResources().getColor(R.color.white));
        String url = "https://queueapplication.herokuapp.com/subscriptionapi/organization-unsubscribed/?user_id="+sp.getString("ID", "");
        StringRequest stringRequestR = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.listview_custom_layout, orgNames);
                        if (response.equals("[]")) {
                            orgNames.add("No Organizations!");
                        } else {
                            final String value = response.substring(2, response.length() - 2);
                            String[] keyValuePairs = value.split(",");
                            final List<String> values = new ArrayList<String>();
                            for (String pair : keyValuePairs) {
                                String[] entry = pair.split(":");
                                values.add(entry[1].trim());
                            }
                            int ex = values.size() / 8;
                            int i = 3;
                            while (ex > 0) {
                                if (i <= values.size()) {
                                    orgNames.add(values.get(i).substring(1, values.get(i).length() - 1));
                                    orgID.add(values.get(i - 3));
                                    ex--;
                                }
                                i += 9;
                            }
                        }
                        listView.setAdapter(adapter);
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                if (orgNames.contains(query)) {
                                    adapter.getFilter().filter(query);
                                } else {
                                    Toast.makeText(MainActivity.this, "No Match found", Toast.LENGTH_LONG).show();
                                }
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                adapter.getFilter().filter(newText);
                                return false;
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
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
    }
}