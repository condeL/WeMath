package com.welearn.wemath.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.welearn.wemath.MainActivity;
import com.welearn.wemath.R;

public class LoginRegisterActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private Button mRegisterButton;

    private Boolean mAnonymous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = this.getIntent();
        mAnonymous = intent.getBooleanExtra("wasAnonymous", false);

        mNameEditText = findViewById(R.id.login_register_editname);
        mRegisterButton = findViewById(R.id.login_register_button);

        mRegisterButton.setOnClickListener(v -> {
                if (mNameEditText.getText().toString().trim().length() != 0){
                    ProgressDialog progressDialog = new ProgressDialog(LoginRegisterActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setTitle("Registering");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(mNameEditText.getText().toString())
                            .build();

                currentUser.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Profile update", "User profile updated.");
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginRegisterActivity.this, "Registration successful!", Toast.LENGTH_LONG).show();
                                    if(mAnonymous){
                                        finish();
                                    }
                                    else {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else{
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginRegisterActivity.this, "Please check your connection", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            } else{
                    Toast.makeText(LoginRegisterActivity.this, "Please enter a username", Toast.LENGTH_LONG).show();
                }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
