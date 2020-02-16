package com.welearn.wemath.lessons;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LessonTopicViewModel extends ViewModel {

    private String mYear;
    private String mSection;

    public LessonTopicViewModel(String year, String section){

        mYear = year;
        mSection = section;
    }

    public String getYear() {
        return mYear;
    }

    public String getSection() {
        return mYear;
    }
}
