package com.example.qyu.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SecretActivity extends AppCompatActivity {
    private EditText secretCode;
    private Button joinBtn;
    SharedPreferences sp;
    private RequestQueue mRequestQueue;
    String token = "TOKEN 1da4aeda9cec13222d59e4c9ad1cea4572a809db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret);

        mRequestQueue = Volley.newRequestQueue(SecretActivity.this);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        secretCode = findViewById(R.id.secretCode);
        joinBtn = findViewById(R.id.joinPrivateBtn);

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (secretCode.getText().toString().equals("")){
                    Toast.makeText(SecretActivity.this, "Please, Enter the secret code!", Toast.LENGTH_SHORT).show();
                } else {
                    String url = "https://queueapplication.herokuapp.com/queueapi/private-event/";
                    final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String value = response.substring(0, response.length() - 1);
                            String[] valuePairs = value.split(",");
                            String[] values = new String[30];
                            int j = 0;
                            for (String pair : valuePairs) {
                                String[] entry = pair.split(":");
                                values[j] = entry[1].trim();
                                j++;
                            }
                            Intent i = new Intent(SecretActivity.this, QueueActivity.class);
                            i.putExtra("eventID", values[0]);
                            i.putExtra("eventAvg", values[6]);
                            startActivity(i);
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error Response!" + error, Toast.LENGTH_LONG).show();
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
                            params.put("security_key", secretCode.getText().toString());
                            return params;
                        }
                    };
                    mRequestQueue.add(request);
                }
            }
        });
    }
}