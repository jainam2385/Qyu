package com.example.qyu.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothHidDeviceAppSdpSettings;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.qyu.Adapters.ReviewAdapter;
import com.example.qyu.Model.api.WebApi;
import com.example.qyu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {
    String token = "TOKEN 1da4aeda9cec13222d59e4c9ad1cea4572a809db";
    RequestQueue mRequestQueue;
    SharedPreferences sp;
    RecyclerView rv;
    TextView on, up, left, arch, remo, comp, wait, rate, noOfr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mRequestQueue = Volley.newRequestQueue(DashboardActivity.this);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        on = findViewById(R.id.on);
        up = findViewById(R.id.up);
        left = findViewById(R.id.left);
        arch = findViewById(R.id.arch);
        remo = findViewById(R.id.remo);
        comp = findViewById(R.id.comp);
        wait = findViewById(R.id.wait);
        rate = findViewById(R.id.rating);
        noOfr = findViewById(R.id.noofrev);
        rv = findViewById(R.id.review_rv);
        getSupportActionBar().setTitle("Dashboard");
        setData();
        setReview();
    }

    private void setReview() {
        String url = "https://queueapplication.herokuapp.com/reviewapi/review-list/?organization_id=" + sp.getString("ID", "");
        StringRequest stringRequestSub = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("[]")) {
                            String value = response.substring(2, response.length() - 2);
                            String[] keyValuePairs = value.split(",");
                            final List<String> values = new ArrayList<String>();
                            for (String pair : keyValuePairs) {
                                String[] entry = pair.split(":");
                                values.add(entry[1].trim());
                            }
                            int ex = values.size() / 6;
                            values.add("end");
                            final List<String> rating = new ArrayList<String>();
                            final List<String> message = new ArrayList<String>();
                            int i = 1;
                            while (ex > 0) {
                                rating.add(values.get(i+1));
                                message.add(values.get(i+2));
                                i += 6;
                                ex--;
                            }
                            ReviewAdapter adapter = new ReviewAdapter(rating, message);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.this, LinearLayoutManager.VERTICAL, false);
                            rv.setLayoutManager(layoutManager);
                            rv.setHasFixedSize(true);
                            rv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DashboardActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
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
        mRequestQueue.add(stringRequestSub);
    }

    private void setData() {
        String url = "https://queueapplication.herokuapp.com/organizationapi/organization-stats/?organization_id=" + sp.getString("ID", "");
        StringRequest stringRequestSub = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
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
                        left.setText(values[0]);
                        wait.setText(values[1]);
                        remo.setText(values[2]);
                        comp.setText(values[3]);
                        up.setText(values[4]);
                        on.setText(values[5]);
                        arch.setText(values[6]);
                        noOfr.setText(values[7]);
                        rate.setText(values[8]);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DashboardActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
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
        mRequestQueue.add(stringRequestSub);
    }
}