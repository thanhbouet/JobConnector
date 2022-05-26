package com.example.jobconnector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    ViewPager viewPager;
    DotsIndicator dotsIndicator;
    AppCompatButton btnNext;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        viewPager = findViewById(R.id.viewPager);
        dotsIndicator = findViewById(R.id.dots_indicator);
        btnNext = findViewById(R.id.nextBtn);

        if (restorePrefData()) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
        List<SplashItem> splashItems = new ArrayList<>();
        splashItems.add(new SplashItem("Find your Job","Finding your job fast and easily",R.drawable.splash1));
        splashItems.add(new SplashItem("Create your employer account","Within business account, you can share your recruit information and get more CV",R.drawable.splash2));
        splashItems.add(new SplashItem("Create your introduction","Share your major,experience and strength with anyone",R.drawable.splash3));

        System.out.println(splashItems.size());
        SplashViewAdapter splashAdapter = new SplashViewAdapter(splashItems,this);
        viewPager.setAdapter(splashAdapter);
        dotsIndicator.setViewPager(viewPager);
        viewPager.setHorizontalScrollBarEnabled(false);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem() == 0) {
                    viewPager.setCurrentItem(1);
                } else if (viewPager.getCurrentItem() == 1) {
                    viewPager.setCurrentItem(2);
                } else if(viewPager.getCurrentItem() == 2) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    savePrefsData();
                    finish();
                }
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }
                    @Override
                    public void onPageSelected(int position) {
                        if (position == 1 || position == 0) {
                            btnNext.setText("NEXT");
                        } else {
                            btnNext.setText("START");
                        }
                    }
                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
        });
    }



    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        return pref.getBoolean("isIntroOpnend",false);
    }

    private void savePrefsData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.apply();


    }

}