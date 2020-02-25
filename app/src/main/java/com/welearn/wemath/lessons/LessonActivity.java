package com.welearn.wemath.lessons;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
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
    private TextView mCommentsButton;
    private FragmentContainerView mCommentsFrgament;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        //mLesson = new Lesson("Title", "Content", "C:\\Users\\hp\\AndroidStudioProjects\\math-application\\app\\src\\main\\res\\drawable\\fblogo.png");
        mTitle= findViewById(R.id.lesson_title);
        mContent = findViewById(R.id.lesson_content);
        mImage = findViewById(R.id.lesson_image);
        mCommentsButton = findViewById(R.id.lesson_comment_link);
        //mCommentsFrgament = findViewById(R.id.lesson_comment_fragment);

        mWebView = findViewById(R.id.lesson_webview);
        mWebView.loadUrl("file:///android_asset/htmltest.html");


        mNextButton = findViewById(R.id.next_question_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent =  LessonQuestionActivity.newIntent(LessonActivity.this);
                startActivity(intent);
            }
        });

        mCommentsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Intent intent =  LessonQuestionActivity.newIntent(LessonActivity.this);
                //startActivity(intent);
                makeFragment();

            }
        });
    }

    void makeFragment(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.lesson_comment_fragment);

        if (fragment == null){ //check if it's not already there in case of an activity reopening
            fragment = new CommentsFragment();
            fm.beginTransaction().add(R.id.lesson_comment_fragment,fragment).commit(); //chainable methods because they all return a FragTran
        }
    }
}
