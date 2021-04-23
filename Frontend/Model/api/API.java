package com.example.qyu.Model.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Button;
import android.widget.ProgressBar;

public interface API {
    void login(Activity LoginActivity, String email, String pass, Button btn, ProgressBar pb);
    void register(String username, String fname, String lname, String email, String password, String phone);
    void orgRegister(Activity activity, String name, String email, String password, String phone, String address, String gstNO, Button button, ProgressBar progressBar);
    void orgLogin(Activity orgLoginActivity, String email, String password, Button orgLogin, ProgressBar orgLoginPB);
    void createAnEvent(String eventName, String eventDesc, String startDate, String endDate, String maxP, String avg_time, String isPrivate);
    void startAnEvent(String eventID);
    void joinEvent(Activity activity, String eventID, String eventAvg, ProgressDialog progressDialog, String s);
    void statusChange(String id, String eventID, String status);
    void unsubscribeOrg(Activity activity, String orgId);
    public void registerEvent(final Activity activity, final String eventID, final ProgressDialog progressDialog);
}
