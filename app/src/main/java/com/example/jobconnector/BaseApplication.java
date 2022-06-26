package com.example.jobconnector;

import android.app.Application;
import android.text.Html;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaseApplication extends Application {
    public static String domain;
    public static ArrayList<String> favoriteJob = new ArrayList<>();

    @Override
    public void onCreate() {
        domain = getString(R.string.domain);
        super.onCreate();
    }


}
