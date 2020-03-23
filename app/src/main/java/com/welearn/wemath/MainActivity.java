package com.welearn.wemath;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/* The main menu activity that hosts all the navigation fragments*/

public class MainActivity extends AppCompatActivity {

    AppBarConfiguration appBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
        final DrawerLayout drawerLayout = findViewById(R.id.main_drawer_layout);
        NavigationView drawerNavView = findViewById(R.id.drawer_nav_view);

        View header = drawerNavView.getHeaderView(0);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        TextView username = header.findViewById(R.id.header_username);
        TextView profileImage = header.findViewById(R.id.header_profile_image);

        String name = firebaseUser.getDisplayName();
        username.setText(name);
        profileImage.setText(String.valueOf(name.toUpperCase().charAt(0)));
        int[] profileColors = getResources().getIntArray(R.array.profile_colors);
        Paint paint = new Paint();
        paint.setColor(profileColors[name.charAt(0)%6]);
        profileImage.getBackground().setColorFilter(paint.getColor(), PorterDuff.Mode.ADD);

        TextView signInButton = header.findViewById(R.id.header_sign_in);
        signInButton.setText("Sign out");

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);


            }
        });

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_lesson_section, R.id.navigation_home, R.id.navigation_quiz_main)
                .setDrawerLayout(drawerLayout)
                .build();

        //add the burger drawer

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        NavigationUI.setupWithNavController(drawerNavView,navController);


        //https://stackoverflow.com/questions/4207880/android-how-do-i-prevent-the-soft-keyboard-from-pushing-my-view-up
        drawerLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                drawerLayout.getWindowVisibleDisplayFrame(r);
                int heightDiff = drawerLayout.getRootView().getHeight() - (r.bottom - r.top);

                if (heightDiff > 200) { // if more than 100 pixels, its probably a keyboard...
                    //ok now we know the keyboard is up...
                    navView.setVisibility(View.GONE);

                }else{
                    //ok now we know the keyboard is down...
                    navView.setVisibility(View.VISIBLE);

                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
