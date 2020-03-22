package com.welearn.wemath.lessons;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.welearn.wemath.R;

import java.util.prefs.PreferenceChangeEvent;

public class LessonActivity extends AppCompatActivity {

    private Button mNextButton;
    private Button mPrevButton;
    private Button mCommentsButton;
    private FragmentContainerView mCommentsFrgament;
    private WebView mWebView;
    private int numberpage;
    private SharedPreferences preferences;
    String url = "file:///android_asset/";
    String html = ".html";

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, LessonActivity.class);
        //intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);


        mCommentsButton = findViewById(R.id.lesson_comment_link);
        //mCommentsFrgament = findViewById(R.id.lesson_comment_fragment);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int numberpage = preferences.getInt("numberpage", 1);
        String url2 = preferences.getString("url2", null );

        mWebView = findViewById(R.id.lesson_webview);
        //mWebView.loadUrl("file:///android_asset/lessons_jhs1_1_1.html");
        mWebView.loadUrl(url+url2+"_"+numberpage+html);




        mNextButton = findViewById(R.id.next_question_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                int numberpage = preferences.getInt("numberpage", 1);
                numberpage++;

                SharedPreferences.Editor edit = preferences.edit();
                edit.putInt("numberpage", numberpage);
                edit.commit();
                Intent intent =  LessonQuestionActivity.newIntent(LessonActivity.this);
                //intent.putExtra("numberpage", numberpage);
                //startActivityForResult(intent, 100);
                startActivity(intent);
            }
        });

        mPrevButton = findViewById(R.id.prev_lesson_button);
        if (numberpage == 1){
            mPrevButton.setVisibility(View.GONE);
        }
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numberpage = preferences.getInt("numberpage", 1);
                if(numberpage != 1) {
                    //numberpage--;
                    //SharedPreferences.Editor edit = preferences.edit();
                    //edit.putInt("numberpage", numberpage);
                    //edit.commit();
                    Intent intent = LessonQuestionActivity.newIntent(LessonActivity.this);
                    startActivity(intent);
                } else {
                    //onBackPressed();

                }
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

    void makeFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.lesson_comment_fragment);
        FrameLayout frameLayout = findViewById(R.id.lesson_comment_fragment);
        if (frameLayout.getVisibility() == View.GONE) {
            frameLayout.setVisibility(View.VISIBLE);
            if (fragment == null) { //check if it's not already there in case of an activity reopening
                fragment = new CommentsFragment();
                fm.beginTransaction().add(R.id.lesson_comment_fragment, fragment).commit(); //chainable methods because they all return a FragTran
            }
        } else {
            frameLayout.setVisibility(View.GONE);
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 100){
            if(resultCode == RESULT_OK){
                int numberpage = data.getIntExtra("numberpage", 1);
                //numberpage++;
                if(1 != numberpage){
                    mWebView = findViewById(R.id.lesson_webview);
                    mWebView.loadUrl(url+numberpage+html);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/
}
