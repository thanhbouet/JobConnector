package com.example.jobconnector;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.example.jobconnector.MainActivity.worker;

public class SearchActivity extends AppCompatActivity implements RetrieveImageTask.Listener {

    private ListView listView;
    private ArrayList<JobInfor> jobInfors = new ArrayList<>();
    private JobAdapter adapter;
    private TextView searchResult;
    private EditText edtSearch;
    private ImageButton imgBtnSearch;
    private ProgressBar progressBar;
    private MaterialSpinner spCareer;
    private MaterialSpinner spRank;
    private MaterialSpinner spAddress;
    private MaterialSpinner spType;

    private Button btnAcount;

    private String stringCareer = "";
    private String stringAddress = "";
    private String stringRank = "";
    private String stringType = "";
    private List<String> bitmapList = new ArrayList<>();
    private String finalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mapping();

        adapter = new JobAdapter(this, R.layout.row_inf, jobInfors);
        listView.setAdapter(adapter);

        imgBtnSearch.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }


            String keyword = edtSearch.getText().toString();
            if (stringCareer.equals("Tất cả ngành nghề")) {
                stringCareer = "";
            }
            if (stringRank.equals("Tất cả cấp bậc")) {
                stringRank = "";
            }
            if (stringAddress.equals("Tất cả địa điểm")) {
                stringAddress = "";
            }
            if (stringType.equals("Loại công việc")) {
                stringType = "";
            }

            jobInfors.clear();
            adapter.notifyDataSetChanged();
            if (keyword.trim().equals("")) {
                Toast.makeText(SearchActivity.this, "Please enter your keyword to search!", Toast.LENGTH_LONG).show();
                searchResult.setText("Result");
            } else {
                progressBar.setVisibility(View.VISIBLE);
                getData(keyword, stringCareer, stringAddress, stringRank, stringType);
            }
        });

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            String nameCompany = jobInfors.get(i).getCompanyName();
            String nameJob = jobInfors.get(i).getJobName();
            String time = jobInfors.get(i).getTimeLimit();
            String address = jobInfors.get(i).getAddress();

            Intent intent = new Intent(SearchActivity.this, JobDetails.class);
            intent.putExtra("nameCompany", nameCompany);
            intent.putExtra("nameJob", nameJob);
            intent.putExtra("time", time);
            intent.putExtra("address", address);

            startActivity(intent);
        });

        ArrayAdapter<CharSequence> careerAdapter = ArrayAdapter.createFromResource(this, R.array.filterCareer, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spCareer.setAdapter(careerAdapter);

        spCareer.setOnItemSelectedListener((view, position, id, item) -> {
            String[] str = getResources().getStringArray(R.array.filterCareer);
            stringCareer = str[position];
        });

        ArrayAdapter<CharSequence> addressAdapter = ArrayAdapter.createFromResource(this, R.array.filterAddress, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spAddress.setAdapter(addressAdapter);

        spAddress.setOnItemSelectedListener((view, position, id, item) -> {
            String[] str = getResources().getStringArray(R.array.filterAddress);
            stringAddress = str[position];

        });
        ArrayAdapter<CharSequence> rankAdapter = ArrayAdapter.createFromResource(this, R.array.filterRank, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spRank.setAdapter(rankAdapter);

        spRank.setOnItemSelectedListener((view, position, id, item) -> {
            String[] str = getResources().getStringArray(R.array.filterRank);
            stringRank = str[position];
        });

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.filterType, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spType.setAdapter(typeAdapter);

        spType.setOnItemSelectedListener((view, position, id, item) -> {
            String[] str = getResources().getStringArray(R.array.filterType);
            stringType = str[position];
        });



        btnAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (worker.equals("Job Seeker")) {
                    startActivity(new Intent(SearchActivity.this, AccountJobSeeker.class));
                } else {
                    startActivity(new Intent(SearchActivity.this, AccountEmployer.class).putExtra("employer",MainActivity.username));
                }
            }
        });

    }

    @SuppressLint("DiscouragedPrivateApi")
    private void mapping() {
        listView = findViewById(R.id.listview);
        searchResult = findViewById(R.id.SearchResult);
        edtSearch = findViewById(R.id.edtSearch);
        imgBtnSearch = findViewById(R.id.imgBtnSearch);
        progressBar = findViewById(R.id.PB_Loading);
        spCareer = findViewById(R.id.spCareer);
        spAddress = findViewById(R.id.spAddress);
        spRank = findViewById(R.id.spRank);
        spType = findViewById(R.id.spType);

        btnAcount = findViewById(R.id.account);
    }

    private void getData(String keyword, String career, String address, String rank, String type) {
        progressBar.setVisibility(View.INVISIBLE);
        String url = "http://10.0.2.2/information/getData.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String image_url = object.getString("image_url");
                            bitmapList.add(image_url);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new RetrieveImageTask(this).execute(bitmapList);
                    finalData = response;
                },
                error -> {
                    Toast.makeText(SearchActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("keyword", keyword);
                param.put("career", career);
                param.put("address", address);
                param.put("rank", rank);
                param.put("type", type);
                return param;
            }
        };

        requestQueue.add(request);
    }

    @Override
    public void onImageLoaded(List<Bitmap> bitmap) {
        System.out.println("Size of bitmap in search: " + bitmap.size());

        try {
            JSONArray jsonArray = new JSONArray(finalData);
            String result;
            if (jsonArray.length() > 0) {
                if (jsonArray.length() == 1) {
                    result = "Have 1 result for \"" + edtSearch.getText() + "\"";
                } else {
                    result = "Have " + jsonArray.length() + " results for \"" + edtSearch.getText() + "\"";
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String jobName = object.getString("job_name");
                    String companyName = object.getString("company_name");
                    String[] time = object.getString("time_limit").split("-");
                    String timeLimit = String.format("%s - %s - %s", time[2], time[1], time[0]);
                    String addressss = object.getString("address");
                    jobInfors.add(new JobInfor(jobName, companyName, addressss, timeLimit, null));
                    adapter.notifyDataSetChanged();
                }

            } else {
                result = "Don't have any result for \"" + edtSearch.getText() + "\"";
                adapter.notifyDataSetChanged();
            }
            searchResult.setText(result);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < jobInfors.size(); i++) {
            jobInfors.get(i).setImageURL(bitmap.get(i));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError() {

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SearchActivity.this, WorkActivity.class));
        finish();
    }
}