package com.example.jobconnector;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JobDetails extends AppCompatActivity implements RetrieveForOne.mListener {

    private TextView jobName_TV;
    private TextView companyName_TV;
    private TextView timeLimit_TV;
    private TextView addressDetail_TV;
    private TextView rank_TV;
    private TextView salary_TV;
    private TextView jobType_TV;
    private TextView jobDes_TV;
    private TextView jobReq_TV;
    private TextView benefit_TV;
    private ImageView imgDetail;
    private String imageURL;
    private String username;
    private AppCompatButton apply_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_detail);

        mapping();

        Bundle bundle = getIntent().getExtras();
        String nameCompany = bundle.getString("nameCompany");
        String nameJob = bundle.getString("nameJob");

        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.worker.equals("Employer")) {
                    startActivity(new Intent(JobDetails.this,ApplyActivity.class)
                            .putExtra("getbackCpname",nameCompany)
                            .putExtra("getbackJbname",nameJob));
                    finish();
                } else {
                    new AlertDialog.Builder(JobDetails.this)
                            .setTitle("You are Employer")
                            .setMessage("Only Job Seeker Account can apply to this. Please register a new account to continue!")
                            .setPositiveButton(android.R.string.yes, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
        getUserName(nameCompany);
        getData(nameJob, nameCompany);

    }

    public void mapping() {
        jobName_TV = findViewById(R.id.jobName_TV);
        companyName_TV = findViewById(R.id.companyName_TV);
        timeLimit_TV = findViewById(R.id.timeLimit_TV);
        addressDetail_TV = findViewById(R.id.address_detail_TV);
        rank_TV = findViewById(R.id.rank_TV);
        salary_TV = findViewById(R.id.salary_TV);
        jobDes_TV = findViewById(R.id.job_des_TV);
        jobType_TV = findViewById(R.id.job_type_TV);
        jobReq_TV = findViewById(R.id.job_req_TV);
        benefit_TV = findViewById(R.id.benefit_TV);
        imgDetail = findViewById(R.id.job_img_detail);
        apply_btn = findViewById(R.id.apply_btn);
    }

    public void getData(String job_name, String company_name) {
        String url = "http://10.0.2.2/information/inforData.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        ProgressDialog progressDialog = new ProgressDialog(JobDetails.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Loading");
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        progressDialog.dismiss();
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject object = jsonArray.getJSONObject(0);
                        String jobName = object.getString("job_name");
                        String companyName = object.getString("company_name");
                        String timeLimit = object.getString("time_limit");
                        String addressDetail = object.getString("address_detail");
                        String salary = object.getString("salary");
                        String rank = object.getString("rank");
                        String jobType = object.getString("job_type");
                        String jobDes = object.getString("job_descriptions");
                        String jobReq = object.getString("job_requirements");
                        String benefit = object.getString("benefit");
                        imageURL = object.getString("image_url");

                        jobName_TV.setText(jobName);
                        companyName_TV.setText(companyName);
                        String[] time = timeLimit.split("-");
                        timeLimit_TV.setText(String.format("%s - %s - %s", time[2], time[1], time[0]));
                        addressDetail_TV.setText(addressDetail);
                        rank_TV.setText(rank);
                        salary_TV.setText(salary);
                        jobType_TV.setText(jobType);
                        jobDes_TV.setText(jobDes);
                        jobReq_TV.setText(jobReq);
                        benefit_TV.setText(benefit);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    fetchImage();
                },
                error -> {
                    Toast.makeText(JobDetails.this, error.toString(), Toast.LENGTH_LONG).show();
                }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("job_name", job_name);
                param.put("company_name", company_name);
                return param;
            }
        };

        requestQueue.add(request);
    }
    private void fetchImage() {
        new RetrieveForOne(JobDetails.this).execute("http://10.0.2.2/image_storage/images/" + imageURL);
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        imgDetail.setImageBitmap(bitmap);
    }

    @Override
    public void onError() {
        Toast.makeText(this,"Cant open imageURL",Toast.LENGTH_SHORT).show();
    }

    private void getUserName(String companyName) {
        String url = "http://10.0.2.2/loginregister/getUsername.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    username = response;
                    Toast.makeText(JobDetails.this,"See information of "+username,Toast.LENGTH_SHORT ).show();
                    companyName_TV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(JobDetails.this,AccountEmployer.class).putExtra("employer",username));
                        }
                    });
                },
                Throwable::printStackTrace) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("companyName", companyName);
                return param;
            }
        };
        requestQueue.add(request);
    }
}