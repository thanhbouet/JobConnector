package com.example.jobconnector;

import static com.example.jobconnector.MainActivity.worker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;



public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    TextView home;
    TextView account;
    TextView privacy;
    TextView aboutUs;
    TextView logOut;
    TextView quit;
    Intent intent;
    String activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        home = findViewById(R.id.home_st);
        account = findViewById(R.id.account_st);
        privacy = findViewById(R.id.privacy_st);
        aboutUs = findViewById(R.id.about_us);
        logOut = findViewById(R.id.log_out);
        quit = findViewById(R.id.quit);

        home.setOnClickListener(this);
        account.setOnClickListener(this);
        privacy.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        logOut.setOnClickListener(this);
        quit.setOnClickListener(this);

        intent = getIntent();
        activity = intent.getStringExtra("pre-activity");


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (activity.equals("feed")) {
            startActivity(new Intent(SettingActivity.this, feedActivity.class));
            finish();
        } else {
            startActivity(new Intent(SettingActivity.this, WorkActivity.class));
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_st:
            {
                startActivity(new Intent(SettingActivity.this,WorkActivity.class));
                finish();
                break;
            }
            case R.id.account_st:
            {
                if (worker.equals("Job Seeker")) {
                    startActivity(new Intent(SettingActivity.this, AccountJobSeeker.class)
                            .putExtra("username",MainActivity.username)
                            .putExtra("message",""));
                } else {
                    startActivity(new Intent(SettingActivity.this, AccountEmployer.class).putExtra("employer",MainActivity.username));
                }
                finish();
                break;
            }
            case R.id.privacy_st: {
                break;
            }
            case R.id.about_us: {
                System.out.println("");
                break;
            }
            case R.id.log_out: {
                startActivity(new Intent(SettingActivity.this,MainActivity.class));
                finish();
                break;
            }
            case R.id.quit: {
                quit();
                break;
            }
        }
    }
    public void quit() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(startMain);
    }
}