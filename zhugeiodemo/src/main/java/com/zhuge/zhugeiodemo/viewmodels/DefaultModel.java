package com.zhuge.zhugeiodemo.viewmodels;

public class DefaultModel {

    private String title;
    private String explanation;
    private int image;

    public  DefaultModel(String title, String explanation, int image) {
        this.title = title;
        this.explanation = explanation;
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }
}
