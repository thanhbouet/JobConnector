package com.example.jobconnector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;

public class AccountEmployer extends AppCompatActivity implements RetrieveForOne.mListener{

    private TextView fullName;
    private TextView companyName;
    private TextView fields;
    private TextView address;
    private TextView update;
    private TextView desc;
    private TextView title_employer;
    private ImageView notificationIcon;
    ImageView updateIcon;
    ImageView companyAvatar;
    String imgURL;
    Intent intent;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_employer);

        mapping();

        intent = getIntent();
        username = intent.getStringExtra("employer");
        getData(username);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountEmployer.this, UpdateEmployer.class));
                finish();
            }
        });

        if (!username.equals(MainActivity.username)) {
            title_employer.setText("Company Information");
            updateIcon.setVisibility(View.INVISIBLE);
            update.setVisibility(View.INVISIBLE);
        }

        notificationIcon.setOnClickListener(v -> {
            startActivity(new Intent(AccountEmployer.this, NotificationActivity.class));
            finish();
        });
    }

    private void mapping() {
        fullName = findViewById(R.id.fullname);
        companyName = findViewById(R.id.compName);
        fields = findViewById(R.id.Fields);
        address = findViewById(R.id.comAddress);
        update = findViewById(R.id.ComUpdate);
        desc = findViewById(R.id.desc);
        companyAvatar = findViewById(R.id.company_avatar);
        title_employer = findViewById(R.id.title_employer);
        updateIcon = findViewById(R.id.update_icon);
        notificationIcon = findViewById(R.id.notification_img);
    }

    public void getData(String username) {
        String url = getString(R.string.domain) + "/loginregister/getInfoEmployer.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        ProgressDialog progressDialog = new ProgressDialog(AccountEmployer.this);
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
                        String fullname = object.getString("fullname");
                        fullname = fullname.equals("null") ? "" : fullname;
                        String comName = object.getString("company_name");
                        comName = comName.equals("null") ? "" : comName;
                        String Fields = object.getString("fields");
                        Fields = Fields.equals("null") ? "" : Fields;
                        String Addr = object.getString("company_address");
                        Addr = Addr.equals("null") ? "" : Addr;
                        String des = object.getString("descriptions");
                        des = des.equals("null") ? "" : des;
                        imgURL = object.getString("image_url");
                        imgURL = imgURL.equals("null") ? "" : imgURL;

                        fullName.setText(String.format("My name is %s", fullname));
                        companyName.setText(comName);
                        fields.setText(String.format("Fields: %s", Fields));
                        address.setText(String.format("Address: %s", Addr));
                        desc.setText(des);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    fetchImage();
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(AccountEmployer.this, error.toString(), Toast.LENGTH_LONG).show();
                }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("username", username);
                return param;
            }
        };

        requestQueue.add(request);

    }

    private void fetchImage() {

        new RetrieveForOne(AccountEmployer.this).execute( getString(R.string.domain) + "/image_storage/avatar_storage/employer/" + imgURL);
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        companyAvatar.setImageBitmap(bitmap);
    }

    @Override
    public void onError() {
        Toast.makeText(this,"Error to load profile picture",Toast.LENGTH_SHORT).show();
    }


}