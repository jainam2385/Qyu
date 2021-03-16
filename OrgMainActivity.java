package com.example.qyu;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;

import static com.example.qyu.model.api.WebApi.orgID;

public class OrgMainActivity extends AppCompatActivity {
    private ImageView createAnEventBtn;
    private ImageView orgMyAccountBtn;
    private ImageView logout;
    SharedPreferences sp;
    Spinner spin;
    ListView listView;
    private RequestQueue mRequestQueue;
    String[] events = {"Ongoing", "Upcoming", "Archived"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_main);
        getSupportActionBar().setTitle("Dashboard");
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
                sp.edit().putBoolean("logged",false).apply();
                Intent i = new Intent(OrgMainActivity.this, UserTypeActivity.class);
                startActivity(i);
                finish();
            }
        });

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String token = "TOKEN db82c38856dfadb1f5bc483adfdd018ede25657f";
                switch (position){
                    case 0:
                        String urlForA = "https://qyu.herokuapp.com/organizationapi/organization-events/?organization_id=1&status=A";
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlForA,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        String value = response.substring(1, response.length()-1);
                                        String[] keyValuePairs = value.split(",");
                                        String[] values = new String[20];
                                        if (!value.isEmpty()) {
                                            int j = 0;
                                            for (String pair : keyValuePairs) {
                                                String[] entry = pair.split(":");
                                                values[j] = entry[1].trim();
                                                j++;
                                            }
                                            listView.setBackgroundColor(getResources().getColor(R.color.lightACCENT));
                                            String[] eventNames = {values[1].substring(1, values[1].length() - 1)};
                                            final ArrayAdapter<String> eventA = new ArrayAdapter<String>(OrgMainActivity.this, R.layout.listview_custom_layout, eventNames);
                                            listView.setAdapter(eventA);
                                        } else {
                                            listView.setBackgroundColor(getResources().getColor(R.color.lightACCENT));
                                            String[] eventNames = {"No Ongoing Events!"};
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
                        String urlForR = "https://qyu.herokuapp.com/organizationapi/organization-events/?organization_id=1&status=R";
                        StringRequest stringRequestR = new StringRequest(Request.Method.GET, urlForR,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        String value = response.substring(1, response.length()-1);
                                        String[] keyValuePairs = value.split(",");
                                        String[] values = new String[20];
                                        int j = 0;
                                        for(String pair : keyValuePairs) {
                                            String[] entry = pair.split(":");
                                            values[j] = entry[1].trim();
                                            j++;
                                        }
                                        listView.setBackgroundColor(getResources().getColor(R.color.lightGREEN));
                                        String[] eventNames = {values[1].substring(1, values[1].length()-1), values[11].substring(1, values[11].length()-1)};
                                        final ArrayAdapter<String> eventR = new ArrayAdapter<String>(OrgMainActivity.this, R.layout.listview_custom_layout, eventNames);
                                        listView.setAdapter(eventR);
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
                        String urlForD = "https://qyu.herokuapp.com/organizationapi/organization-events/?organization_id=1&status=D";
                        StringRequest stringRequestD = new StringRequest(Request.Method.GET, urlForD,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        String value = response.substring(1, response.length()-1);
                                        String[] keyValuePairs = value.split(",");
                                        String[] values = new String[40];
                                        int j = 0;
                                        for(String pair : keyValuePairs) {
                                            String[] entry = pair.split(":");
                                            values[j] = entry[1].trim();
                                            j++;
                                        }
                                        listView.setBackgroundColor(getResources().getColor(R.color.lightRED));
                                        String[] eventNames = {values[1].substring(1, values[1].length()-1), values[11].substring(1, values[11].length()-1), values[21].substring(1, values[21].length()-1)};
                                        final ArrayAdapter<String> eventD = new ArrayAdapter<String>(OrgMainActivity.this, R.layout.listview_custom_layout, eventNames);
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