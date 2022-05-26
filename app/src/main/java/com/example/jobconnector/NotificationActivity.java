package com.example.jobconnector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

public class NotificationActivity extends AppCompatActivity {
    TextView title;
    ListView listView;
    AppCompatButton backBtn;
    RecruitAdapter recruitAdapter;
    List<String> usernameList = new ArrayList<>();
    List<String> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        title = findViewById(R.id.title);
        listView = findViewById(R.id.list_application);
        backBtn = findViewById(R.id.back_btn);

        recruitAdapter = new RecruitAdapter(this,R.layout.recruit_inf,usernameList);
        listView.setAdapter(recruitAdapter);

        fetchData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(NotificationActivity.this, AccountJobSeeker.class)
                        .putExtra("username",usernameList.get(position))
                        .putExtra("message",messageList.get(position)));
                finish();
            }
        });
    }

    public void fetchData() {
            String url = "http://10.0.2.2/recruitment/recruitList.php";
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {
                        System.out.println(WorkActivity.companyName);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String username = object.getString("jobseeker");
                                String message = object.getString("message");
                                usernameList.add(username);
                                messageList.add(message);
                                System.out.println(username + " " + message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        recruitAdapter.notifyDataSetChanged();
                    },
                    error -> Toast.makeText(NotificationActivity.this, error.toString(), Toast.LENGTH_LONG).show()) {
                @NonNull
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("companyName", WorkActivity.companyName);
                    return param;
                }
            };

            requestQueue.add(request);
    }
}