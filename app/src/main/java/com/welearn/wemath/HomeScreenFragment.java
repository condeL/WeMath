package com.welearn.wemath;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeScreenFragment extends Fragment {

    private FirebaseUser mUser;
    private TextView mWelcomeTextView, mProfileImage;
    private Button mProfileButton;

    public HomeScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home_screen, container, false);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        String name = mUser.getDisplayName();
        mWelcomeTextView = root.findViewById(R.id.welcome_text_home);
        mWelcomeTextView.setText("Welcome, " + name + "!");

        mProfileImage = root.findViewById(R.id.profile_home_image);
        mProfileImage.setText(String.valueOf(name.toUpperCase().charAt(0)));
        int[] profileColors = getResources().getIntArray(R.array.profile_colors);
        Paint paint = new Paint();
        paint.setColor(profileColors[name.charAt(0)%6]);

        mProfileImage.getBackground().setColorFilter(paint.getColor(), PorterDuff.Mode.ADD);

        mProfileButton = root.findViewById(R.id.profile_button_main);
        mProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);

            }
        });



        return root;
    }

}
