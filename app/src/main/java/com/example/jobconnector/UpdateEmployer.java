package com.example.jobconnector;

import static com.example.jobconnector.MainActivity.username;

import android.Manifest;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UpdateEmployer extends AppCompatActivity {

    private EditText fullName;
    private EditText company;
    private EditText fieldsCom;
    private EditText comAddr;
    private EditText descr;
    private Button update;
    private AppCompatButton browse;
    private ImageView imgView;
    private Bitmap bitmap;
    String encodeImageString;

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

        browse.setOnClickListener(v -> Dexter.withActivity(UpdateEmployer.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response)
                    {
                        Intent intent=new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent,"Browse Image"),1);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check());
    }

    private void mapping() {
        fullName = findViewById(R.id.yourfullname);
        company = findViewById(R.id.ComName);
        fieldsCom = findViewById(R.id.ComFields);
        comAddr = findViewById(R.id.compAddr);
        descr = findViewById(R.id.descrip);
        update = findViewById(R.id.updateCom);
        browse = findViewById(R.id.browseBtn);
        imgView = findViewById(R.id.jobImage);
    }

    private void getData() {
        String url = getString(R.string.domain) + "/loginregister/getInfoEmployer.php";
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

                        WorkActivity.companyName = comName;

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
                    error.printStackTrace();
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
        String url = getString(R.string.domain) + "/loginregister/updateEmployer.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.equals("Update successfully")) {
                        Toast.makeText(UpdateEmployer.this, response, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UpdateEmployer.this, AccountEmployer.class).putExtra("employer",MainActivity.username));
                        finish();
                    } else {
                        Log.e("error", response);
                        Toast.makeText(UpdateEmployer.this, response, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UpdateEmployer.this, AccountEmployer.class).putExtra("employer",MainActivity.username));
                        finish();
                    }
                },
                error -> {
                    Toast.makeText(UpdateEmployer.this, error.toString(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
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
                if (encodeImageString == null) {
                    URL url;
                    InputStream inputStream = null;
                    try {
                        url = new URL(getString(R.string.domain) + "/image_storage/images/sample_feed.jpg");
                        inputStream = url.openStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap bm = BitmapFactory.decodeStream(inputStream);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();
                    encodeImageString = android.util.Base64.encodeToString(b, Base64.DEFAULT);
                }
                param.put("image_upload",encodeImageString);
                return param;
            }
        };

        requestQueue.add(request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode==1 && resultCode== Activity.RESULT_OK)
        {
            Uri filepath=data.getData();
            try
            {
                InputStream inputStream= getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imgView.setImageBitmap(bitmap);
                encodeBitmapImage(bitmap);
            }catch (Exception ex)
            {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void encodeBitmapImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytesofimage =byteArrayOutputStream.toByteArray();
        encodeImageString = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
        System.out.println(encodeImageString == null);
    }
}