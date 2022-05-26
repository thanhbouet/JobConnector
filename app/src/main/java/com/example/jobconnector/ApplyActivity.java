package com.example.jobconnector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class ApplyActivity extends AppCompatActivity {
    MaterialEditText message;
    TextView review;
    AppCompatButton send;
    AppCompatButton back;
    private String backupCompany;
    private String backupJobName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        message = findViewById(R.id.message_apply);
        review = findViewById(R.id.review_btn);
        send = findViewById(R.id.sent_btn);
        back = findViewById(R.id.back_btn);

        Intent intent = getIntent();
        backupCompany = intent.getStringExtra("getbackCpname");
        backupJobName = intent.getStringExtra("getbackJbname");
        System.out.println("Bakc up:" + backupCompany);

        back.setOnClickListener(v -> {
            startActivity(new Intent(ApplyActivity.this,JobDetails.class)
                    .putExtra("nameCompany",backupCompany)
                    .putExtra("nameJob",backupJobName));
            finish();
        });

        review.setOnClickListener(v -> {
            startActivity(new Intent(ApplyActivity.this,AccountJobSeeker.class)
                    .putExtra("username",MainActivity.username)
                    .putExtra("message",""));
            finish();
        });

        send.setOnClickListener(v -> applying());
    }
    private void applying() {
        String url = "http://10.0.2.2/recruitment/apply.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.equals("Update successfully")) {
                        Toast.makeText(ApplyActivity.this,response,Toast.LENGTH_SHORT).show();
                        back.callOnClick();
                    } else {
                        Toast.makeText(ApplyActivity.this,response,Toast.LENGTH_SHORT).show();
                    }
                },
                Throwable::printStackTrace) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("jobSeeker", MainActivity.username);
                param.put("employer", backupCompany);
                if (message.getText() == null) {
                    message.setText("");
                }
                param.put("message", message.getText().toString().trim());

                return param;
            }
        };

        requestQueue.add(request);
    }
}