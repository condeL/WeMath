package com.welearn.wemath.lessons;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.welearn.wemath.R;

public class LessonActivity extends AppCompatActivity {

    int QUESTION_ACTIVITY;
    public static final int NEXT_REQUEST_CODE = 1;
    public static final int PREV_REQUEST_CODE = 2;


    private Button mNextButton;
    private Button mPrevButton;
    private Button mCommentsButton;
    private FragmentContainerView mCommentsFrgament;
    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    private WebView mWebView;
    private NestedScrollView mScrollView;
    private FrameLayout mFrameLayout;


    private String mYear, mSection;
    private int mTopic, mLesson, mMaxLesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);


        mYear = LessonActivityArgs.fromBundle(getIntent().getExtras()).getYear();
        mSection = LessonActivityArgs.fromBundle(getIntent().getExtras()).getSection();
        mTopic = LessonActivityArgs.fromBundle(getIntent().getExtras()).getTopic();
        mLesson = LessonActivityArgs.fromBundle(getIntent().getExtras()).getLesson();
        mMaxLesson = LessonActivityArgs.fromBundle(getIntent().getExtras()).getMaxLesson();

        mCommentsButton = findViewById(R.id.lesson_comment_link);

        mScrollView = findViewById(R.id.lesson_activity_scrollView);
        mWebView = findViewById(R.id.lesson_webview);

        mWebView.loadUrl("file:///android_asset/" + mSection + mYear + "_" + mTopic + "." + mLesson + ".html");




        mNextButton = findViewById(R.id.next_question_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                navigate(true);
            }
        });

        mPrevButton = findViewById(R.id.prev_lesson_button);
        if (mLesson == 1){
            mPrevButton.setVisibility(View.GONE);
        }
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mLesson != 1) {
                    navigate(false);

                }
            }
        });

        mFrameLayout = findViewById(R.id.lesson_comment_fragment);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.lesson_comment_fragment);

        mCommentsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (mFrameLayout.getVisibility() == View.GONE) {
                    mFrameLayout.setVisibility(View.VISIBLE);
                    if (mFragment == null) { //check if it's not already there in case of an activity reopening
                        Bundle bundle = new Bundle();
                        bundle.putString("choice", "comments/" + mSection + "/" + mYear + "/" + mTopic + "/" + mLesson);
                        mFragment = new CommentsFragment();
                        mFragment.setArguments(bundle);
                        mFragmentManager.beginTransaction().add(R.id.lesson_comment_fragment, mFragment).commit(); //chainable methods because they all return a FragTran
                    }
                } else {
                    mFrameLayout.setVisibility(View.GONE);
                }

            }
        });
    }

    void navigate (boolean next){
        Intent intent = new Intent(getApplicationContext(),LessonQuestionActivity.class);
        if(!next) {
            mLesson--;
        }
        intent.putExtra("section", mSection);
        intent.putExtra("year", mYear);
        intent.putExtra("topic", mTopic);
        intent.putExtra("lesson", mLesson);

        startActivityForResult(intent, QUESTION_ACTIVITY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == QUESTION_ACTIVITY){
            if(resultCode == NEXT_REQUEST_CODE){
                mLesson++;
                if(mLesson > mMaxLesson){
                    Toast.makeText(getBaseContext(),"Congratulations, topic finished!", Toast.LENGTH_LONG).show();
                    finish();
                }
                if (mLesson != 1){
                    mPrevButton.setVisibility(View.VISIBLE);
                }
                mWebView.loadUrl("file:///android_asset/" + mSection + mYear + "_" + mTopic + "." + mLesson + ".html");
                //mWebView.pageUp(true);
                mScrollView.fullScroll(View.FOCUS_UP);
                if(mFragmentManager.getBackStackEntryCount() > 0) {
                    mFragmentManager.beginTransaction().detach(mFragment).commit();
                    mFragment = null;
                    mFrameLayout.setVisibility(View.GONE);
                }

            } else if (resultCode == PREV_REQUEST_CODE){
                if (mLesson != 1){
                    mPrevButton.setVisibility(View.VISIBLE);
                } else
                    mPrevButton.setVisibility(View.GONE);
                mWebView.loadUrl("file:///android_asset/" + mSection + mYear + "_" + mTopic + "." + mLesson + ".html");
                if(mFragmentManager.getBackStackEntryCount() > 0) {
                    mFragmentManager.beginTransaction().detach(mFragment).commit();
                    mFragment = null;
                    mFrameLayout.setVisibility(View.GONE);
                }
                mScrollView.fullScroll(View.FOCUS_DOWN);

            }
        }
    }
}
