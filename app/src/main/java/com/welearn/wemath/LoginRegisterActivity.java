package com.welearn.wemath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class LoginRegisterActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private Button mRegisterButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        mNameEditText = findViewById(R.id.login_register_editname);
        mRegisterButton = findViewById(R.id.login_register_button);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                    if (mNameEditText.getText().toString().trim().length() != 0){
                        Toast.makeText(LoginRegisterActivity.this, "Registering...", Toast.LENGTH_LONG).show();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(mNameEditText.getText().toString())
                                .build();

                    currentUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("Profile update", "User profile updated.");
                                        Toast.makeText(LoginRegisterActivity.this, "Registration successful!", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                } else{
                        Toast.makeText(LoginRegisterActivity.this, "Please enter a valid username", Toast.LENGTH_LONG).show();
                    }
            }
        });
    }
}
