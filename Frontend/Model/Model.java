package com.example.qyu.Model;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.qyu.Model.api.WebApi;
import com.example.qyu.Model.api.API;

public class Model {
    private static Model sInstance = null;
    private final API mApi;

    public static Model getInstance(Application application){
        if (sInstance == null){
            sInstance = new Model(application);
        }
        return sInstance;
    }
    private final Application mApplication;
    private Model(Application application) {
        mApplication = application;
        mApi = new WebApi(mApplication);
    }
    public Application getApplication() {return mApplication; }
    public void login(Activity LoginActivity, String email, String pass, Button loginBtn, ProgressBar pb) {
        mApi.login(LoginActivity, email, pass, loginBtn, pb);
    }
    public void register(String username, String fname, String lname, String email, String password, String phone){
        mApi.register(username, fname, lname, email, password, phone);
    }
    public void orgRegister(Activity activity, String name, String email, String password, String phone, String Address, String GST,Button button, ProgressBar progressBar){
        mApi.orgRegister(activity, name, email, password, phone, Address, GST, button, progressBar);
    }
    public void orgLogin(Activity orgLoginActivity, String email, String password, Button orgLogin, ProgressBar orgLoginPB){
        mApi.orgLogin(orgLoginActivity, email, password, orgLogin, orgLoginPB);
    }
    public void createAnEvent(String eventName, String eventDesc, String startDate, String endDate, String maxP, String avg_time, String isPrivate){
        mApi.createAnEvent(eventName, eventDesc, startDate, endDate, maxP, avg_time, isPrivate);
    }
    public void startAnEvent(String eventID) {
        mApi.startAnEvent(eventID);
    }
    public void joinEvent(Activity activity, String id, String avg, ProgressDialog progressDialog, String s) {
        mApi.joinEvent(activity, id, avg, progressDialog, s);
    }
    public void statusChange(String id, String eventID, String status){
        mApi.statusChange(id, eventID, status);
    }
    public void unsubscribeOrg(final Activity activity, final String orgId) {
        mApi.unsubscribeOrg(activity, orgId);
    }
    public void registerEvent(final Activity activity, final String eventID, final ProgressDialog progressDialog){
        mApi.registerEvent(activity, eventID, progressDialog);
    }
}
