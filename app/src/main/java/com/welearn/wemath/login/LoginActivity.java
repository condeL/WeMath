package com.welearn.wemath.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.util.Patterns;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.welearn.wemath.MainActivity;
import com.welearn.wemath.R;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";

    private FirebaseAuth mAuth;
    private Button mRegisterButton, mSignInButton;
    private TextView mAnonymousButton;
    private EditText mEmail, mPassword;
    private CallbackManager mCallbackManager;
    private LoginButton mFacebookloginButton;
    private FirebaseUser mCurrentUser;

    private boolean mAnonymous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        mRegisterButton = findViewById(R.id.login_activity_register_button);
        mSignInButton = findViewById(R.id.login_activity_signin_button);
        mAnonymousButton = findViewById(R.id.login_activity_anonymous);
        mEmail = findViewById(R.id.login_activity_email);
        mPassword = findViewById(R.id.login_activity_password);
        mFacebookloginButton = findViewById(R.id.login_activity_facebook_button);


        // Check if user is anonymous and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        if(mCurrentUser == null) {
            mAnonymousButton.setVisibility(View.VISIBLE);
        }
        else {
            if(mCurrentUser.isAnonymous()) {
                mAnonymousButton.setVisibility(View.GONE);
                mAnonymous = true;
            }
        }

        //display sign-in button as disabled
        mSignInButton.getBackground().setAlpha(85);


        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isEmailValid(mEmail.getText().toString())){
                    mEmail.setError("Invalid email");
                    mRegisterButton.setEnabled(false);
                    mSignInButton.setEnabled(false);
                    mSignInButton.getBackground().setAlpha(85);
                }
                else if(isPasswordValid(mPassword.getText().toString())){
                    mRegisterButton.setEnabled(true);
                    mSignInButton.setEnabled(true);
                    mSignInButton.getBackground().setAlpha(255);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isPasswordValid(mPassword.getText().toString())){
                    mPassword.setError("Password needs >6 characters\nand >1 uppercase character");
                    mRegisterButton.setEnabled(false);
                    mSignInButton.setEnabled(false);
                    mSignInButton.getBackground().setAlpha(85);

                } else if(isEmailValid(mEmail.getText().toString())){
                    mRegisterButton.setEnabled(true);
                    mSignInButton.setEnabled(true);
                    mSignInButton.getBackground().setAlpha(255);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mRegisterButton.setOnClickListener(v -> {
            if (mEmail.getText().toString().trim().length() != 0 && mPassword.getText().toString().trim().length() != 0 ) {
                Toast.makeText(LoginActivity.this, "Creating new account...", Toast.LENGTH_SHORT).show();
                createUser(mEmail.getText().toString(), mPassword.getText().toString());
            } else{
                Toast.makeText(LoginActivity.this, "Please enter a valid email and password", Toast.LENGTH_SHORT).show();
            }

        });

        mSignInButton.setOnClickListener(v -> {
            if (mEmail.getText().toString().trim().length() != 0 && mPassword.getText().toString().trim().length() != 0 ) {
                Toast.makeText(LoginActivity.this, "Signing in...", Toast.LENGTH_SHORT).show();
                signInUser(mEmail.getText().toString(), mPassword.getText().toString());
            } else{
                Toast.makeText(LoginActivity.this, "Please enter a valid email and password", Toast.LENGTH_SHORT).show();
            }
        });


        mAnonymousButton.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Logging in...", Toast.LENGTH_SHORT).show();
            signInAnon();

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


    // Email validation check
    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }

        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            return false;
        }
    }

    // Password validation check
    private boolean isPasswordValid(String password) {
        if(!(password != null && password.trim().length() > 6)){
            return false;
        }
        for(int i = 0; i<password.length(); i++){
            if(Character.isUpperCase(password.charAt(i))){
                return true;
            }
        }
        return false;
    }

    private Spannable centeredToastMsg(String text){
        Spannable centeredText = new SpannableString(text);
        centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0, text.length() - 1,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return centeredText;
    }

    public void createUser(String email, String password){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");

                            createPlaceholderName();

                            Toast.makeText(LoginActivity.this, "Success!",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getBaseContext(), LoginRegisterActivity.class);
                            intent.putExtra("wasAnonymous", mAnonymous);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // If registration in fails, display a message to the user.
                    if( e instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(LoginActivity.this, centeredToastMsg("This email address is already in use by another account"), Toast.LENGTH_LONG).show();
                        mEmail.setText("");
                    }

                    else if(e instanceof FirebaseNetworkException){
                        Toast.makeText(LoginActivity.this, centeredToastMsg("Network error\nPlease check your connection"), Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, centeredToastMsg("Error\nPlease try again"), Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

    public void createPlaceholderName(){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName("NoName")
                .build();
        mCurrentUser = mAuth.getCurrentUser();
        mCurrentUser.updateProfile(profileUpdates)
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
                                Log.d(TAG, "signInWithEmail:success");
                                Toast.makeText(LoginActivity.this, "Sign-in successful.",
                                        Toast.LENGTH_SHORT).show();
                                redirect();
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // If sign in fails, display a message to the user.
                    if( e instanceof FirebaseAuthInvalidUserException){
                        Toast.makeText(LoginActivity.this, centeredToastMsg("User not found\nPlease create a new account"), Toast.LENGTH_LONG).show();
                        mEmail.setText("");
                    }
                    else if( e instanceof FirebaseAuthInvalidCredentialsException){
                        Toast.makeText(LoginActivity.this, centeredToastMsg("Invalid password\nPlease try again"), Toast.LENGTH_LONG).show();
                        mPassword.setText("");
                    }
                    else if(e instanceof FirebaseNetworkException){
                        Toast.makeText(LoginActivity.this, centeredToastMsg("Network error\nPlease check your connection"), Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, centeredToastMsg("Sign-in failed\nPlease try again"), Toast.LENGTH_LONG).show();
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
                            Log.d(TAG, "signInAnonymously:success");
                            createAnonUser();
                        } else {
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // If sign in fails, display a message to the user.
                if(e instanceof FirebaseNetworkException){
                    Toast.makeText(LoginActivity.this, centeredToastMsg("Network error\nPlease check your connection"), Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(LoginActivity.this, centeredToastMsg("Error\nPlease try again"), Toast.LENGTH_LONG).show();
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
                                Log.d(TAG, "signInWithCredential:success");
                                Toast.makeText(LoginActivity.this, "Authentication successful.",
                                        Toast.LENGTH_SHORT).show();
                                redirect();
                            } else {
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // If sign-in in fails, display a message to the user.
                    if(e instanceof FirebaseNetworkException){
                        Toast.makeText(LoginActivity.this, centeredToastMsg("Network error\nPlease check your connection"), Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, centeredToastMsg("Error\nPlease try again"), Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

    public void redirect(){
        if(mAnonymous){ //close the activity and return to normal if entered as anonymous
            finish();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            //restore button color
            mSignInButton.getBackground().setAlpha(255);
            finish();
        }
    }
}
