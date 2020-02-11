package com.welearn.wemath.lessons;

import android.media.Image;

public class Lesson {
    private String mTitle;
    private String mContent;
    private String mImage;

    Lesson(String title, String content, String image){
        mTitle = title;
        mContent = content;
        mImage = image;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }
}
