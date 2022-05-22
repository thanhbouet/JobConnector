package com.example.jobconnector;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class feedActivity extends AppCompatActivity implements View.OnClickListener{



    FeedFragment feedFragment;
    RecruitFragment recruitFragment;
    ViewPager viewPager;
    ImageView post_recruitment;
    ImageView newsFeed;

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

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        feedFragment = new FeedFragment();
        recruitFragment = new RecruitFragment();

        viewPagerAdapter.addFragment(feedFragment);
        viewPagerAdapter.addFragment(recruitFragment);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(onPageChangeListener);

        post_recruitment.setOnClickListener(this);
        newsFeed.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.post_recruitment:
                viewPager.setCurrentItem(1);
                break;
            case R.id.news_feed:
                viewPager.setCurrentItem(0);
                break;
        }
    }
}
