package com.example.jobconnector;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import org.jsoup.Jsoup;

public class feedActivity extends AppCompatActivity implements View.OnClickListener{



    FeedFragment feedFragment;
    RecruitFragment recruitFragment;
    ViewPager viewPager;
    ImageView post_recruitment;
    ImageView newsFeed;
    ImageView setting;

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        post_recruitment = findViewById(R.id.post_recruitment);
        newsFeed = findViewById(R.id.news_feed);
        viewPager = findViewById(R.id.viewPager);
        setting = findViewById(R.id.setting_btn);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        feedFragment = new FeedFragment();
        recruitFragment = new RecruitFragment();

        viewPagerAdapter.addFragment(feedFragment);
        if (MainActivity.worker.equals("Employer")) {
            viewPagerAdapter.addFragment(recruitFragment);
        }

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(onPageChangeListener);

        post_recruitment.setOnClickListener(this);
        newsFeed.setOnClickListener(this);
        setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.post_recruitment:
                if (MainActivity.worker.equals("Employer")) {
                    viewPager.setCurrentItem(1);
                } else {
                    new AlertDialog.Builder(feedActivity.this)
                            .setTitle("Your account is not Employer")
                            .setMessage("Only employer Account can post a recruitment. Please register a new employer account to continue!")
                            .setPositiveButton(android.R.string.yes, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                break;
            case R.id.news_feed:
                viewPager.setCurrentItem(0);
                break;
            case R.id.setting_btn: {
                //Intent intent = new Intent(feedActivity.this,SettingActivity.class);
                //intent.putExtra("pre-activity","feed");
                startActivity(new Intent(feedActivity.this,SettingActivity.class).putExtra("pre-activity","feed"));
                finish();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            startActivity(new Intent(feedActivity.this, WorkActivity.class));
            finish();
    }


}
