package com.example.jobconnector;

public class SplashItem {
    private String title;
    private String description;
    int imgView;

    public SplashItem(String title, String description, int imgView) {
        this.title = title;
        this.description = description;
        this.imgView = imgView;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImgView() {
        return imgView;
    }

    public void setImgView(int imgView) {
        this.imgView = imgView;
    }
}
