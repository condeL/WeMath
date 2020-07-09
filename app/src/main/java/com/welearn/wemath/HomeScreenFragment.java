package com.welearn.wemath;

/*the first screen a logged-in user will see*/

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.welearn.wemath.login.LogoutActivity;


public class HomeScreenFragment extends Fragment {

    private FirebaseUser mUser;
    private TextView mWelcomeTextView, mProfileImage;
    private Button mLogoutButton;

    public HomeScreenFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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

        mLogoutButton = root.findViewById(R.id.profile_button_main);
        mLogoutButton.setOnClickListener(v -> {

            Intent intent = new Intent(getContext(), LogoutActivity.class);
            startActivity(intent);

        });

        return root;
    }

}
