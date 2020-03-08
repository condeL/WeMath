package com.welearn.wemath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //handler to make a delay
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                routing();
            }
        }, 1000);
    }


    //to route depending on whether the user is logged-in or not
    public void routing(){
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mUser == null){
            //no signed in user
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            //signed in user
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
