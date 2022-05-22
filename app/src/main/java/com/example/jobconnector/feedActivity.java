package com.example.jobconnector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class feedActivity extends AppCompatActivity implements RetrieveImageTask.Listener {

    ListView listView;
    ArrayList<Post> postList = new ArrayList<>();
    List<String> imageUrlList = new ArrayList<>();
    List<Bitmap> bitmapList = new ArrayList<>();
    String finalData;
    PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        listView = findViewById(R.id.postList);
        adapter = new PostAdapter(feedActivity.this, R.layout.post_layout, postList);
        listView.setAdapter(adapter);
        fetchData();
    }

    public ArrayList<String> jsonStringToArray(String jsonString) throws JSONException {

        ArrayList<String> stringArray = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            stringArray.add(jsonArray.getString(i));
        }

        return stringArray;
    }

    //Viet lai ham nay bang JSONObject.getString
    public List<String> getParams(String s) {
        StringBuilder prs = new StringBuilder(s);
        while (prs.indexOf("\"") != -1) {
            prs.deleteCharAt(prs.indexOf("\""));
        }
        prs.delete(prs.indexOf("id"),prs.indexOf("id") +3);
        prs.delete(prs.indexOf("caption"),prs.indexOf("caption") + 8);
        prs.delete(prs.indexOf("salary"),prs.indexOf("salary") + 7);
        prs.delete(prs.indexOf("jobType"),prs.indexOf("jobType") + 8);
        prs.delete(prs.indexOf("location"),prs.indexOf("location") + 9);
        prs.delete(prs.indexOf("imageURL"),prs.indexOf("imageURL") + 9);
        prs.delete(prs.indexOf("describle"),prs.indexOf("describle") + 10);

        prs.deleteCharAt(0);
        prs.deleteCharAt(prs.length()-1);
        String[] arrRs = prs.toString().split(",",7);
        List<String> arrResult = Arrays.asList(arrRs);
        if (arrResult.get(6).length() > 150) {
            StringBuilder cutString = new StringBuilder(arrResult.get(6));
            cutString.delete(150,cutString.length());
            cutString.append("...<Click for more deltail>");
            arrResult.set(6,cutString.toString());
        }
        return arrResult;
    }

    private void fetchData() {
        ProgressDialog progressDialog = new ProgressDialog(feedActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Loading");
        progressDialog.show();

        String uRl = "http://10.0.2.2/fetchdata/jobdata.php";
        StringRequest request = new StringRequest(Request.Method.GET, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    imageUrlList = getImageURL(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new RetrieveImageTask(feedActivity.this).execute(imageUrlList);
                finalData = response;
            }
        }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                System.out.println(error.toString());
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getmInstance(feedActivity.this).addToRequestQueue(request);
    }
    public List<String> getImageURL(String jsonString) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonString);
        List<String> urlResult = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            urlResult.add(jsonArray.getJSONObject(i).getString("imageURL"));
        }

        return urlResult;
    }

    @Override
    public void onImageLoaded(List<Bitmap> bitmap) {
        try {
            ArrayList<String> posts = jsonStringToArray(finalData);
            List<String> prs;
            for (String s : posts) {
                prs = getParams(s);
                //post(id,caption,descripton,img,salary,type,location)
                postList.add(new Post(Integer.parseInt(prs.get(0)),prs.get(1), prs.get(6), null, prs.get(2),prs.get(3),prs.get(4)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(feedActivity.this,"Adapter is set",Toast.LENGTH_SHORT).show();
        System.out.println("Size of bitmap :" + bitmap.size());
        bitmapList = bitmap;
        for (int i = 0; i < postList.size(); i++) {
            postList.get(i).setImageURL(bitmapList.get(i));
        }
        adapter = new PostAdapter(feedActivity.this, R.layout.post_layout, postList);
        listView.setAdapter(adapter);
    }

    @Override
    public void onError() {
        System.out.println("Error");
    }
}