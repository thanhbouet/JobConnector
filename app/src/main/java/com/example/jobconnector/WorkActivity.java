package com.example.jobconnector;

import static com.example.jobconnector.MainActivity.worker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class WorkActivity extends AppCompatActivity {
    private Button feedButton;
    private Button searchButton;
    private Button personalBtn;
    private Button settingBtn;
    public static String companyName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        feedButton = findViewById(R.id.feedButton);
        searchButton = findViewById(R.id.search_btn);
        personalBtn = findViewById(R.id.personal_btn);
        settingBtn = findViewById(R.id.setting_btn_menu);
        feedButton.setOnClickListener(view -> {
            startActivity(new Intent(WorkActivity.this,feedActivity.class));
            finish();
        });
        searchButton.setOnClickListener(v -> {
            startActivity(new Intent(WorkActivity.this,SearchActivity.class));
            finish();
        });
        personalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (worker.equals("Job Seeker")) {
                    startActivity(new Intent(WorkActivity.this, AccountJobSeeker.class)
                            .putExtra("username",MainActivity.username)
                            .putExtra("message",""));
                } else {
                    startActivity(new Intent(WorkActivity.this, AccountEmployer.class).putExtra("employer",MainActivity.username));
                }
            }
        });
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkActivity.this, SettingActivity.class).putExtra("pre-activity","work"));
                finish();
            }
        });
        if (worker.equals("Employer")) {
            getCompanyName();
        }


    }
    private void getCompanyName() {
        String url = "http://10.0.2.2/loginregister/getCompanyName.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    companyName = response;
                    Toast.makeText(WorkActivity.this,"Login with "+companyName,Toast.LENGTH_SHORT ).show();
                },
                Throwable::printStackTrace) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                System.out.println(MainActivity.username);
                param.put("username", MainActivity.username);
                return param;
            }
        };

        requestQueue.add(request);
    }
}