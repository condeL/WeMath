package com.welearn.wemath.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.welearn.wemath.R;



public class LogoutActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button mSignOutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        mAuth = FirebaseAuth.getInstance();
        mSignOutButton = findViewById(R.id.logout_activity_signout_button);

        mSignOutButton.setOnClickListener(v -> {
            if(mAuth.getCurrentUser().isAnonymous()){
                mAuth.signOut();
            }else {
                UserInfo profile = mAuth.getCurrentUser().getProviderData().get(1);
                if (profile.getProviderId().equalsIgnoreCase("facebook.com")) {
                    mAuth.signOut();
                    LoginManager.getInstance().logOut();
                } else {
                    mAuth.signOut();
                }
            }
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        });
    }
}