package com.example.jobconnector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText userName, password, email, mobile, confirmPassword;
    RadioGroup radioGroup;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mapping();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtUsername = userName.getText().toString();
                String txtPassword = password.getText().toString();
                String txtConfirmPassword = confirmPassword.getText().toString();
                String txtEmail = email.getText().toString();
                String txtMobile = mobile.getText().toString();

                if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)
                        || TextUtils.isEmpty(txtUsername) || TextUtils.isEmpty(txtMobile) || TextUtils.isEmpty(txtConfirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "All fields required", Toast.LENGTH_SHORT).show();
                } else if (!txtPassword.equals(txtConfirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Confirm password is incorrect", Toast.LENGTH_SHORT).show();
                } else {
                    int workerId = radioGroup.getCheckedRadioButtonId();
                    RadioButton worker_select = radioGroup.findViewById(workerId);
                    if (worker_select == null) {
                        Toast.makeText(RegisterActivity.this, "Worker required", Toast.LENGTH_SHORT).show();
                    } else {
                        String worker = worker_select.getText().toString();
                        registerNewAccount(txtUsername, txtEmail, txtPassword, txtMobile, worker);
                    }
                }

            }
        });
    }

    private void mapping() {
        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        radioGroup = findViewById(R.id.radioGr);
        register = findViewById(R.id.register);
        confirmPassword = findViewById(R.id.ConfirmPassword);
    }

    private void registerNewAccount(String username, String email, String password, String mobile, String worker) {
        ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Register New Account");
        progressDialog.show();
        String uRl = "http://10.0.2.2/loginregister/register.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Successfully Registered")) {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                    System.out.println(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("username", username);
                param.put("email", email);
                param.put("password", password);
                param.put("mobile", mobile);
                param.put("worker", worker);
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(RegisterActivity.this).addToRequestQueue(request);
    }
}