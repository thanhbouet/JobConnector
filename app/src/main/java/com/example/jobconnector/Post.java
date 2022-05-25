package com.example.jobconnector;

import android.graphics.Bitmap;

public class Post {
    private int id;
    private String caption;
    private String companyName;
    private String describle;
    private Bitmap bitmapImg;
    private String salary;
    private String jobType;
    private String location;

    public Post(int id,String caption, String companyName, String describle, Bitmap bitmapImg, String salary, String jobType, String location) {
        this.id = id;
        this.caption = caption;
        this.describle = describle;
        this.bitmapImg = bitmapImg;
        this.salary = salary;
        this.jobType = jobType;
        this.location = location;
        this.companyName = companyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public String getDescrible() {
        return describle;
    }

    public Bitmap getImageURL() {
        return bitmapImg;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setDescrible(String describle) {
        this.describle = describle;
    }

    public void setImageURL(Bitmap imageURL) {
        this.bitmapImg = imageURL;
    }

    public String getSalary() {
        return salary;
    }

    public String getJobType() {
        return jobType;
    }

    public String getLocation() {
        return location;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
