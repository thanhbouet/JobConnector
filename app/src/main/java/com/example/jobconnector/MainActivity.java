package com.example.jobconnector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    MaterialEditText email,password;
    CheckBox loginState;
    Button login, register;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
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
                    login(txtEmail,txtPassword);
                }
            }
        });
        String loginStatus = sharedPreferences.getString(getResources().getString(R.string.prefLoginState),"");
        if (loginStatus.equals("loggedin")) {
            startActivity(new Intent(MainActivity.this, WorkActivity.class));
        }

    }

    private void login(String email, String password) {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Logging In");
        progressDialog.show();
        String uRl = "http://10.0.2.2/loginregister/login.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Login Success")) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,response,Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (loginState.isChecked()) {
                        editor.putString(getResources().getString(R.string.prefLoginState),"loggedin");
                    } else {
                        editor.putString(getResources().getString(R.string.prefLoginState),"loggedout");
                    }
                    startActivity(new Intent(MainActivity.this, WorkActivity.class));
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,response,Toast.LENGTH_SHORT).show();
                    System.out.println(response);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }

        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("email",email);
                param.put("password",password);
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(MainActivity.this).addToRequestQueue(request);
    }
}