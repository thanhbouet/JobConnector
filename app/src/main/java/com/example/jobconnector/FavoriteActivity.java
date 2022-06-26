package com.example.jobconnector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteActivity extends AppCompatActivity {

    private ListView favoriteList;
    private FavoriteAdapter favoriteAdapter;
    private List<String> jobNameList = new ArrayList<>();
    private List<String> companyList = new ArrayList<>();
    AppCompatButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        favoriteList = findViewById(R.id.list_favorite);
        backButton = findViewById(R.id.back_btn);
        favoriteAdapter = new FavoriteAdapter(this,R.layout.favorite_inf,jobNameList,companyList);
        favoriteList.setAdapter(favoriteAdapter);

        getData();

        favoriteList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(FavoriteActivity.this, JobDetails.class);
            intent.putExtra("nameCompany", companyList.get(position));
            intent.putExtra("nameJob", jobNameList.get(position));
            startActivity(intent);
        });

        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void getData() {
        String url = getString(R.string.domain) + "/recruitment/favoriteList.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String jobName = object.getString("jobName");
                            String companyName = object.getString("companyName");
                            jobNameList.add(jobName);
                            companyList.add(companyName);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    favoriteAdapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(FavoriteActivity.this, error.toString(), Toast.LENGTH_LONG).show()) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> param = new HashMap<>();
                param.put("username", MainActivity.username);
                return param;
            }
        };

        requestQueue.add(request);
    }

}