package com.example.jobconnector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class AccountJobSeeker extends AppCompatActivity implements RetrieveForOne.mListener {

    public TextView name;
    public TextView yearExp;
    public TextView fields;
    public TextView currentPos;
    public TextView update;
    private LinearLayout messageLayout;
    private TextView message;
    ImageView avatar,favourite;
    String imgURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_jobseeker);

        mapping();
        Intent intent = getIntent();

        String username = intent.getStringExtra("username");
        String message_string = intent.getStringExtra("message");
        messageLayout.setVisibility(View.GONE);
        if (message_string != null && !message_string.equals("")) {
            message.setText(message_string);
            messageLayout.setVisibility(View.INVISIBLE);
            favourite.setVisibility(View.GONE);
            update.setVisibility(View.GONE);
        }

        getData(username);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountJobSeeker.this, UpdateJobSeeker.class));
            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountJobSeeker.this, FavoriteActivity.class));
            }
        });

    }

    private void mapping() {
        name = findViewById(R.id.fullName);
        yearExp = findViewById(R.id.yearExp);
        fields = findViewById(R.id.fields);
        currentPos = findViewById(R.id.currentPosition);
        update = findViewById(R.id.update);
        avatar = findViewById(R.id.avatar);
        messageLayout = findViewById(R.id.message_layout);
        message = findViewById(R.id.message_field);
        favourite = findViewById(R.id.favorite_img);
    }

    public void getData(String username) {
        String url = getString(R.string.domain) + "/loginregister/getInfoJobSeeker.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        ProgressDialog progressDialog = new ProgressDialog(AccountJobSeeker.this);
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
                        String fullName = object.getString("fullname");
                        fullName = fullName.equals("null") ? "" : fullName;
                        String yearExperience = object.getString("year_experience");
                        yearExperience = yearExperience.equals("null") ? "" : yearExperience;
                        String Fields = object.getString("fields");
                        Fields = Fields.equals("null") ? "" : Fields;
                        String currentPosition = object.getString("current_position");
                        currentPosition = currentPosition.equals("null") ? "" : currentPosition;
                        imgURL = object.getString("image_url");
                        imgURL = imgURL.equals("null") ? "" : imgURL;
                        System.out.println("imgURL:" + imgURL);

                        name.setText(fullName);
                        yearExp.setText(String.format("Year Experience: %s", yearExperience));
                        fields.setText(String.format("Fields: %s", Fields));
                        currentPos.setText(String.format("Current Position: %s", currentPosition));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                    fetchImage();
                },
                error -> {
                    Toast.makeText(AccountJobSeeker.this, error.toString(), Toast.LENGTH_LONG).show();

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
        new RetrieveForOne(AccountJobSeeker.this).execute("http://10.0.2.2/image_storage/avatar_storage/job_seeker/" + imgURL);
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        avatar.setImageBitmap(bitmap);
    }

    @Override
    public void onError() {
        Toast.makeText(this, "Error on loading image", Toast.LENGTH_SHORT).show();
    }

}