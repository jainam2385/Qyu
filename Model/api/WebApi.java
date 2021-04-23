package com.example.qyu.Model.api;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qyu.Activities.LoginActivity;
import com.example.qyu.Activities.MainActivity;
import com.example.qyu.Activities.OrgLoginActivity;
import com.example.qyu.Activities.OrgMainActivity;
import com.example.qyu.Activities.QueueActivity;
import com.example.qyu.R;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class WebApi implements API {

    public static String ID;
    public static String Name;
    public static String Email;
    public static String Address;
    public static String Contact;
    private RequestQueue mRequestQueue;
    SharedPreferences sp;
    private final Application mApplication;
    String token = "TOKEN 1da4aeda9cec13222d59e4c9ad1cea4572a809db";

    public WebApi(Application application) {
        mRequestQueue = Volley.newRequestQueue(application);
        sp = application.getSharedPreferences("login", MODE_PRIVATE);
        mApplication = application;
    }

    public void login(final Activity LoginActivity, final String email, final String pass, final Button loginBtn, final ProgressBar pb) {
        String url = "https://queueapplication.herokuapp.com/userapi/authenticate-user/";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(mApplication, "Successful Response!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(mApplication.getApplicationContext(), MainActivity.class);
                String value = response.substring(1, response.length() - 1);
                String[] keyValuePairs = value.split(",");
                String[] values = new String[10];
                int j = 0;
                for (String pair : keyValuePairs) {
                    String[] entry = pair.split(":");
                    values[j] = entry[1].trim();
                    j++;
                }
                ID = values[0];
                Address = values[4].substring(1, values[4].length() - 1);
                Contact = values[5].substring(1, values[5].length() - 1);
                Name = values[3].substring(1, values[3].length() - 1);
                Email = values[1].substring(1, values[1].length() - 1);
                sp.edit().putBoolean("isloggedin", true).apply();
                sp.edit().putString("ID", ID).apply();
                sp.edit().putBoolean("TYPE", true).apply();
                Toast.makeText(mApplication, "Logged In!", Toast.LENGTH_LONG).show();
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mApplication.startActivity(i);
                LoginActivity.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loginBtn.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                Toast.makeText(mApplication, "Error Response!" + error, Toast.LENGTH_LONG).show();
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
                params.put("username", email);
                params.put("password", pass);
                return params;
            }
        };
        mRequestQueue.add(request);
    }

    public void orgLogin(final Activity orgLoginActivity, final String email, final String password, final Button orgLogin, final ProgressBar orgLoginPB) {
        String url = "https://queueapplication.herokuapp.com/organizationapi/authenticate-organization/";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final Intent i = new Intent(mApplication.getApplicationContext(), OrgMainActivity.class);
                String value = response.substring(1, response.length() - 1);
                String[] keyValuePairs = value.split(",");
                String[] values = new String[10];
                int j = 0;
                for (String pair : keyValuePairs) {
                    String[] entry = pair.split(":");
                    values[j] = entry[1].trim();
                    j++;
                }
                ID = values[0];
                Address = values[4].substring(1, values[4].length() - 1);
                Contact = values[5].substring(1, values[5].length() - 1);
                Name = values[3].substring(1, values[3].length() - 1);
                Email = values[1].substring(1, values[1].length() - 1);
                SharedPreferences sp;
                sp = mApplication.getSharedPreferences("login", MODE_PRIVATE);
                sp.edit().putBoolean("isloggedin", true).apply();
                sp.edit().putBoolean("TYPE", false).apply();
                sp.edit().putString("ID", ID).apply();
                sp.edit().putString("Address", Address).apply();
                sp.edit().putString("Contact", Contact).apply();
                sp.edit().putString("Name", Name).apply();
                sp.edit().putString("Email", Email).apply();

                Toast.makeText(orgLoginActivity, "Logged In!", Toast.LENGTH_LONG).show();
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mApplication.startActivity(i);
                orgLoginActivity.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mApplication, "Error Response!" + error, Toast.LENGTH_LONG).show();
                orgLogin.setVisibility(View.VISIBLE);
                orgLoginPB.setVisibility(View.GONE);
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
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        mRequestQueue.add(request);
    }

    public void createAnEvent(final String eventName, final String eventDesc, final String startDate, final String endDate, final String maxP, final String avg_time, final String isPrivate) {
        String url = "https://queueapplication.herokuapp.com/eventapi/event-detail/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(mApplication, "Event Created!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(mApplication.getApplicationContext(), OrgMainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mApplication.startActivity(i);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mApplication, "Error: " + error, Toast.LENGTH_LONG).show();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", eventName);
                params.put("organization_id", ID);
                params.put("description", eventDesc);
                params.put("start_date_time", startDate);
                params.put("end_date_time", endDate);
                params.put("max_participants", maxP);
                params.put("avg_waiting_time", avg_time);
                params.put("is_private", isPrivate);
                params.put("status", "R");
                return params;
            }
        };

        mRequestQueue.add(stringRequest);
    }

    public void register(final String username, final String fname, final String lname, final String email, final String password, final String phone) {
        String url = "https://queueapplication.herokuapp.com/userapi/user-detail/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(mApplication, "Successful Response!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(mApplication.getApplicationContext(), LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mApplication.startActivity(i);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mApplication, "Error: " + error, Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("email", email);
                params.put("contact", phone);
                params.put("is_verified", String.valueOf(true));
                params.put("first_name", fname);
                params.put("last_name", lname);
                return params;
            }
        };

        mRequestQueue.add(stringRequest);
    }

    public void orgRegister(final Activity register, final String name, final String email, final String password, final String phone, final String address, final String gstNo, final Button button, final ProgressBar progressBar) {
        String url = "https://queueapplication.herokuapp.com/organizationapi/organization-detail/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(mApplication, "Successful Response!", Toast.LENGTH_SHORT).show();
                        final Intent i = new Intent(mApplication.getApplicationContext(), OrgLoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mApplication.startActivity(i);
                        register.finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        button.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(mApplication, "Error: " + error, Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                params.put("name", name);
                params.put("address", address);
                params.put("contact", phone);
                params.put("gstno", gstNo);
                params.put("rating", String.valueOf(0));
                params.put("is_verified", String.valueOf(true));
                return params;
            }
        };

        mRequestQueue.add(stringRequest);
    }

    public void startAnEvent(final String eventID) {
        String url = "https://queueapplication.herokuapp.com/eventapi/start-event/";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(mApplication, "Event Started!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mApplication, "Error Response!" + error, Toast.LENGTH_LONG).show();
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
                params.put("event_id", eventID);
                return params;
            }
        };
        mRequestQueue.add(request);
    }

    public void joinEvent(final Activity activity, final String eventID, final String eventAvg, final ProgressDialog progressDialog, final String orgID) {
        String url = "https://queueapplication.herokuapp.com/queueapi/queue-detail/";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(activity.getApplicationContext(), "Event Joined Successfully!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(activity, QueueActivity.class);
                i.putExtra("eventID", eventID);
                i.putExtra("eventAvg", eventAvg);
                i.putExtra("orgID", orgID);
                activity.startActivity(i);
                activity.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(mApplication, "Error: "+error, Toast.LENGTH_LONG).show();
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
                params.put("event_id", eventID);
                return params;
            }
        };
        mRequestQueue.add(request);
    }

    public void statusChange(String id, String eventID, String status) {
        String url = "https://queueapplication.herokuapp.com/queueapi/queue-detail/?user_id=" + id + "&event_id=" + eventID + "&status=" + status;
        StringRequest request = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(mApplication, "User Admitted!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mApplication, "Error Response!" + error, Toast.LENGTH_LONG).show();
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

    public void unsubscribeOrg(final Activity activity, final String orgId) {
        String urlForSub = "https://queueapplication.herokuapp.com/subscriptionapi/subscription-detail/?user_id="+sp.getString("ID", "")+"&organization_id="+orgId;
        StringRequest stringRequestSub = new StringRequest(Request.Method.DELETE, urlForSub,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(activity.getApplicationContext(), "Unsubscribed Successfully!", Toast.LENGTH_SHORT).show();
                        activity.finish();
                        activity.startActivity(activity.getIntent());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity.getApplicationContext(), "Error: " + error, Toast.LENGTH_LONG).show();
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

    public void registerEvent(final Activity activity, final String eventID, final ProgressDialog progressDialog) {
        String url = "https://queueapplication.herokuapp.com/queueapi/queue-detail/";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(activity.getApplicationContext(), "Registered Successfully!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(mApplication, "You have already registered for this event!", Toast.LENGTH_LONG).show();
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
                params.put("event_id", eventID);
                return params;
            }
        };
        mRequestQueue.add(request);
    }
}









