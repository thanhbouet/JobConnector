package com.example.jobconnector;

import android.graphics.Bitmap;

public class JobInfor {

    private String jobName;
    private String companyName;
    private String address;
    private String timeLimit;
    private Bitmap imageURL;

    public JobInfor(String jobName, String companyName, String address, String timeLimit, Bitmap imageURL) {
        this.jobName = jobName;
        this.companyName = companyName;
        this.address = address;
        this.timeLimit = timeLimit;
        this.imageURL = imageURL;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Bitmap getImageURL() {
        return imageURL;
    }

    public void setImageURL(Bitmap imageURL) {
        this.imageURL = imageURL;
    }
}
