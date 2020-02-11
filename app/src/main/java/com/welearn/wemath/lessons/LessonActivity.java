package com.welearn.wemath.lessons;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.welearn.wemath.R;

public class LessonActivity extends AppCompatActivity {
    private Lesson mLesson;
    private TextView mTitle;
    private TextView mContent;
    private ImageView mImage;
    private Button mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        mLesson = new Lesson("Title", "Content", "C:\\Users\\hp\\AndroidStudioProjects\\math-application\\app\\src\\main\\res\\drawable\\fblogo.png");
        mTitle= findViewById(R.id.lesson_title);
        mContent = findViewById(R.id.lesson_content);
        mImage = findViewById(R.id.lesson_image);
        mNextButton = findViewById(R.id.next_question_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent =  LessonQuestionActivity.newIntent(LessonActivity.this);
                startActivity(intent);
            }
        });
    }
}
