package com.example.jobconnector;

import android.Manifest;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import static com.example.jobconnector.MainActivity.username;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ResponseDelivery;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RecruitFragment extends Fragment {
    /**
     * Pre_Post.
     */
    private MaterialEditText jobName;
    private TextView companyName;
    private MaterialEditText timeLimit;
    private MaterialEditText addressDetail;
    private MaterialEditText rank;
    private MaterialEditText salary;
    private MaterialEditText jobType;
    private MaterialEditText jobDes;
    private MaterialEditText jobReq;
    private MaterialEditText benefit;
    private MaterialEditText career;
    private MaterialEditText address;
    private ImageView imgView;
    private AppCompatButton browseBtn;
    private AppCompatButton uploadBtn;
    ContextWrapper contextWrapper;
    ScrollView mainScroll;


    /**
     * Completed
     */
    RelativeLayout successLayout;
    AppCompatButton newPostBtn;
    AppCompatButton feedBtn;

    Bitmap bitmap;
    String encodeImageString;
    public final String url = "http://10.0.2.2/image_storage/fileupload.php";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recruit_fragment_layout, container, false);
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        jobName = view.findViewById(R.id.jobName);
        career = view.findViewById(R.id.career);
        address = view.findViewById(R.id.address);

        companyName = view.findViewById(R.id.companyName);
        companyName.setText(WorkActivity.companyName);

        timeLimit = view.findViewById(R.id.timeLimit);
        addressDetail = view.findViewById(R.id.address_detail);
        browseBtn = view.findViewById(R.id.browseBtn);
        rank = view.findViewById(R.id.rank);
        salary = view.findViewById(R.id.salary);
        jobType = view.findViewById(R.id.job_type);
        jobDes = view.findViewById(R.id.job_des);
        jobReq = view.findViewById(R.id.job_req);
        benefit = view.findViewById(R.id.benefit);
        uploadBtn = view.findViewById(R.id.upload_btn);
        imgView = view.findViewById(R.id.jobImage);
        mainScroll = view.findViewById(R.id.scrollView1);
        successLayout = view.findViewById(R.id.upload_success_layout);
        newPostBtn = view.findViewById(R.id.new_post_btn);
        feedBtn = view.findViewById(R.id.news_feed_btn);
        /*
        AccountEmployer accountEmployer = new AccountEmployer();
        accountEmployer.getData(username);
        companyName.setText(accountEmployer.getCompanyName());
        */
        browseBtn.setOnClickListener(v -> Dexter.withActivity(getActivity())
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
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploaddatatodb();
            }
        });
        newPostBtn.setOnClickListener(v -> {
            imgView.setImageResource(R.drawable.sample_feed);
            jobName.setText("");
            career.setText("");
            address.setText("");
            companyName.setText("");
            timeLimit.setText("");
            addressDetail.setText("");
            rank.setText("");
            salary.setText("");
            jobType.setText("");
            jobDes.setText("");
            jobReq.setText("");
            benefit.setText("");
            encodeImageString = "";
            successLayout.setVisibility(View.GONE);
            mainScroll.setVisibility(View.VISIBLE);
        });
        feedBtn.setOnClickListener(v -> {
            ViewPager viewPager = getActivity().findViewById(R.id.viewPager);
            viewPager.setCurrentItem(0);
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode==1 && resultCode== Activity.RESULT_OK)
        {
            Uri filepath=data.getData();
            try
            {
                contextWrapper = new ContextWrapper(getActivity());
                InputStream inputStream= contextWrapper.getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
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
        byte[] bytesofimage=byteArrayOutputStream.toByteArray();

        encodeImageString = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
        if (encodeImageString == null) {
            System.out.println("oi roi oi");
        }
    }

    private void uploaddatatodb()
    {


        final String jobNameFinal = jobName.getText().toString().trim();
        final String companyNameFinal = companyName.getText().toString().trim();
        final String timeLimitFinal = timeLimit.getText().toString().trim();
        final String addressDetailFinal = addressDetail.getText().toString().trim();
        final String rankFinal = rank.getText().toString().trim();
        final String salaryFinal = salary.getText().toString().trim();
        final String jobDesFinal = jobDes.getText().toString().trim();
        final String jobTypeFinal = jobType.getText().toString().trim();
        final String jobReqFinal = jobReq.getText().toString().trim();
        final String benefitFinal = benefit.getText().toString().trim();
        final String careerFinal = career.getText().toString().trim();
        final String addressFinal = address.getText().toString().trim();


        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                System.out.println(response);
                Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                mainScroll.setVisibility(View.GONE);
                successLayout.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> map=new HashMap<String, String>();
                map.put("job_name",jobNameFinal);
                map.put("company_name",companyNameFinal);
                map.put("time_limit",timeLimitFinal);
                map.put("address_detail",addressDetailFinal);
                map.put("rank",rankFinal);
                map.put("salary",salaryFinal);
                map.put("job_des",jobDesFinal);
                map.put("job_req",jobReqFinal);
                map.put("job_type",jobTypeFinal);
                map.put("benefit",benefitFinal);
                map.put("upload",encodeImageString);
                map.put("address",addressFinal);
                map.put("career",careerFinal);
                return map;
            }
        };


        RequestQueue queue= Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
}
