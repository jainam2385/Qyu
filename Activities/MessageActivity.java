package com.example.qyu.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.qyu.Model.api.WebApi;
import com.example.qyu.R;

import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {
    private EditText sub, msg;
    private Button sendBtn;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        sub = findViewById(R.id.subjectET);
        msg = findViewById(R.id.messageET);
        sendBtn = findViewById(R.id.sendBtn);
        mRequestQueue = Volley.newRequestQueue(MessageActivity.this);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(sub.getText().toString()) || (TextUtils.isEmpty(msg.getText().toString()))){
                    Toast.makeText(MessageActivity.this, "Please, Enter the content to send!", Toast.LENGTH_SHORT).show();
                } else {
                    String url = "https://queueapplication.herokuapp.com/subscriptionapi/broadcast-message/";
                    final String token = "TOKEN 1da4aeda9cec13222d59e4c9ad1cea4572a809db";
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(MessageActivity.this, "Message Sent Successfully!", Toast.LENGTH_SHORT).show();
                            sub.setText("");
                            msg.setText("");
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
                            params.put("organization_id", WebApi.ID);
                            params.put("subject", sub.getText().toString());
                            params.put("message", msg.getText().toString());
                            return params;
                        }
                    };
                    mRequestQueue.add(request);
                }
            }
        });
    }
}