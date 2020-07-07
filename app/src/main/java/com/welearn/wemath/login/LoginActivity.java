package com.welearn.wemath.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.welearn.wemath.MainActivity;
import com.welearn.wemath.R;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";

    private FirebaseAuth mAuth;
    private Button mRegisterButton, mSignInButton, mSignOutButton;
    private TextView mAnonymousButton;
    private EditText mEmail, mPassword;
    private CallbackManager mCallbackManager;
    private LoginButton mFacebookloginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();

        mRegisterButton = findViewById(R.id.login_activity_register_button);
        mSignInButton = findViewById(R.id.login_activity_signin_button);
        mSignOutButton = findViewById(R.id.login_activity_signout_button);
        mAnonymousButton = findViewById(R.id.login_activity_anonymous);
        mEmail = findViewById(R.id.login_activity_email);
        mPassword = findViewById(R.id.login_activity_password);
        mFacebookloginButton = findViewById(R.id.login_activity_facebook_button);


        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        mRegisterButton.setOnClickListener(v -> {
            if (mEmail.getText().toString().trim().length() != 0 && mPassword.getText().toString().trim().length() != 0 ) {
                Toast.makeText(LoginActivity.this, "Creating new account...", Toast.LENGTH_SHORT).show();
                createUser(mEmail.getText().toString(), mPassword.getText().toString());
                mEmail.setText("");
                mPassword.setText("");
            } else{
                Toast.makeText(LoginActivity.this, "Please enter a valid email and password", Toast.LENGTH_SHORT).show();
            }

        });

        mSignInButton.setOnClickListener(v -> {
            if (mEmail.getText().toString().trim().length() != 0 && mPassword.getText().toString().trim().length() != 0 ) {
                Toast.makeText(LoginActivity.this, "Signing in...", Toast.LENGTH_SHORT).show();
                signInUser(mEmail.getText().toString(), mPassword.getText().toString());
                mEmail.setText("");
                mPassword.setText("");
            } else{
                Toast.makeText(LoginActivity.this, "Please enter a valid email and password", Toast.LENGTH_SHORT).show();
            }
        });


        mAnonymousButton.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Logging in...", Toast.LENGTH_SHORT).show();
            signInAnon();

        });

        mSignOutButton.setOnClickListener(v -> {
            if(mAuth.getCurrentUser().isAnonymous()){
                mAuth.signOut();
                updateUI(null);
            }else {
                UserInfo profile = mAuth.getCurrentUser().getProviderData().get(1);
                if (profile.getProviderId().equalsIgnoreCase("facebook.com")) {
                    mAuth.signOut();
                    LoginManager.getInstance().logOut();
                    updateUI(null);
                } else {
                    mAuth.signOut();
                    updateUI(null);
                }
            }
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        });

        mCallbackManager = CallbackManager.Factory.create();
        mFacebookloginButton.setPermissions("email", "public_profile");
        mFacebookloginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                    handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    public void updateUI(FirebaseUser user){
        if(user != null){
                mEmail.setVisibility(View.GONE);
                mPassword.setVisibility(View.GONE);
                mSignInButton.setVisibility(View.GONE);
                mRegisterButton.setVisibility(View.GONE);
                mFacebookloginButton.setVisibility(View.GONE);
                mAnonymousButton.setVisibility(View.GONE);
                mSignOutButton.setVisibility((View.VISIBLE));

        } else{
            mSignOutButton.setVisibility((View.GONE));
            mEmail.setVisibility(View.VISIBLE);
            mPassword.setVisibility(View.VISIBLE);
            mSignInButton.setVisibility(View.VISIBLE);
            mRegisterButton.setVisibility(View.VISIBLE);
            mFacebookloginButton.setVisibility(View.VISIBLE);
            mAnonymousButton.setVisibility(View.VISIBLE);

        }

    }

    public void createUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            createPlaceholderName();

                            Toast.makeText(LoginActivity.this, "Success!",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), LoginRegisterActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Invalid email",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void createPlaceholderName(){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName("NoName")
                .build();
        FirebaseUser user = mAuth.getCurrentUser();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Profile update", "User profile updated.");
                    }
                });
    }

    public void signInUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Sign-in successful.",
                                    Toast.LENGTH_SHORT).show();
                            redirect();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Sign-in failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void signInAnon(){
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            createAnonUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Please check your internet connection",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

    }

    public void createAnonUser(){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName("Guest")
                .build();

        mAuth.getCurrentUser().updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Profile update", "User profile updated.");
                            Toast.makeText(getBaseContext(), "Logged-in as Guest", Toast.LENGTH_SHORT).show();
                            redirect();
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(LoginActivity.this, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            redirect();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    public void redirect(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
