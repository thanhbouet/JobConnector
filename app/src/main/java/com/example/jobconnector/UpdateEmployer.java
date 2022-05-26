package com.example.jobconnector;

import static com.example.jobconnector.MainActivity.username;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class UpdateEmployer extends AppCompatActivity {

    private EditText fullName;
    private EditText company;
    private EditText fieldsCom;
    private EditText comAddr;
    private EditText descr;
    private Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_employer);

        mapping();

        getData();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = fullName.getText().toString();
                String comName = company.getText().toString();
                String comAddress = comAddr.getText().toString();
                String comFields = fieldsCom.getText().toString();
                String des = descr.getText().toString();

                updateCom(username, name, comName, comAddress, comFields, des);
            }
        });
    }

    private void mapping() {
        fullName = findViewById(R.id.yourfullname);
        company = findViewById(R.id.ComName);
        fieldsCom = findViewById(R.id.ComFields);
        comAddr = findViewById(R.id.compAddr);
        descr = findViewById(R.id.descrip);
        update = findViewById(R.id.updateCom);
    }

    private void getData() {
        String url = "http://10.0.2.2/loginregister/getInfoEmployer.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject object = jsonArray.getJSONObject(0);
                        String name = object.getString("fullname");
                        String comName = object.getString("company_name");
                        String comAddress = object.getString("company_address");
                        String comFields = object.getString("fields");
                        String des = object.getString("descriptions");

                        if (!name.equals("null")) fullName.setText(name);
                        if (!comName.equals("null")) company.setText(comName);
                        if (!comFields.equals("null")) fieldsCom.setText(comFields);
                        if (!comAddress.equals("null")) comAddr.setText(comAddress);
                        if (!des.equals("null")) descr.setText(des);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(UpdateEmployer.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void updateCom(String username, String name, String company_name, String company_address, String field, String descriptions) {
        String url = "http://10.0.2.2/loginregister/updateEmployer.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.equals("Update successfully")) {
                        Toast.makeText(UpdateEmployer.this, response, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UpdateEmployer.this, AccountEmployer.class).putExtra("employer",MainActivity.username));
                        finish();
                    } else {
                        Toast.makeText(UpdateEmployer.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(UpdateEmployer.this, error.toString(), Toast.LENGTH_LONG).show();
                }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("username", username);
                param.put("name", name);
                param.put("company_name", company_name);
                param.put("company_address", company_address);
                param.put("field", field);
                param.put("descriptions", descriptions);
                return param;
            }
        };

        requestQueue.add(request);
    }
}