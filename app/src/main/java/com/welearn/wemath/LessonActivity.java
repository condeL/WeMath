package com.welearn.wemath;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class LessonActivity extends AppCompatActivity {
    private Lesson mLesson;
    private TextView mTitle;
    private TextView mContent;
    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        mLesson = new Lesson("Title", "Content", "C:\\Users\\hp\\AndroidStudioProjects\\math-application\\app\\src\\main\\res\\drawable\\fblogo.png");
        mTitle= findViewById(R.id.title);
        mContent = findViewById(R.id.content);
        mImage = findViewById(R.id.imageView);
    }
}
