package com.example.jobconnector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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


public class MainActivity extends AppCompatActivity {

    MaterialEditText email, password;
    CheckBox loginState;
    Button login, register;
    SharedPreferences sharedPreferences;
    public static String username;
    public static String worker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        loginState = findViewById(R.id.loginstate);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtPassword = password.getText().toString();
                String txtEmail = email.getText().toString();
                if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)) {
                    Toast.makeText(MainActivity.this, "All field Required", Toast.LENGTH_SHORT).show();
                } else {
                    login(txtEmail, txtPassword);
                }
            }
        });

        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        email.setText(sharedPreferences.getString("email", ""));
        password.setText(sharedPreferences.getString("password", ""));
        loginState.setChecked(sharedPreferences.getBoolean("checked", false));

    }

    private void login(String email, String password) {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Loging In");
        progressDialog.show();
        String uRl = "http://10.0.2.2/loginregister/login.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.startsWith("Login")) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, response.substring(0, 16), Toast.LENGTH_SHORT).show();
                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (loginState.isChecked()) {
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.putBoolean("checked", true);
                        editor.apply();
                    } else {
                        editor.remove("email");
                        editor.remove("password");
                        editor.remove("checked");
                        editor.apply();
                    }
                    String[] str = response.substring(16).split("\\|");
                    username = str[0];
                    worker = str[1];
                    startActivity(new Intent(MainActivity.this, WorkActivity.class));
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("email", email);
                param.put("password", password);
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(MainActivity.this).addToRequestQueue(request);
    }
}