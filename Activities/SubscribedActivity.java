package com.example.qyu.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
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
import com.example.qyu.Adapters.SubscribedAdapter;
import com.example.qyu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubscribedActivity extends AppCompatActivity {
    ListView listView;
    RequestQueue mRequestQueue;
    SubscribedAdapter adapter;
    SharedPreferences sp;
    final List<String> orgNames = new ArrayList<String>();
    final List<String> orgID = new ArrayList<String>();
    String token = "TOKEN 1da4aeda9cec13222d59e4c9ad1cea4572a809db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed);
        listView = findViewById(R.id.subListView);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        mRequestQueue = Volley.newRequestQueue(SubscribedActivity.this);
        getSubscribedOrganizations();
    }

    private void getSubscribedOrganizations() {
        String url = "https://queueapplication.herokuapp.com/subscriptionapi/organization-subscribed/?user_id="+sp.getString("ID", "");
        StringRequest stringRequestR = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        adapter = new SubscribedAdapter(SubscribedActivity.this, orgNames, orgID);
                        if (response.equals("[]")) {
                            orgNames.add("No Organizations!");
                            listView.setBackgroundColor(getResources().getColor(R.color.white));
                            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(SubscribedActivity.this, R.layout.listview_custom_layout, orgNames);
                            listView.setAdapter(adapter1);
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
                            listView.setAdapter(adapter);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SubscribedActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(SubscribedActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}