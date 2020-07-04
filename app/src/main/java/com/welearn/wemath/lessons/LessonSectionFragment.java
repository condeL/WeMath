package com.welearn.wemath.lessons;

/*fragment for selecting the school section*/

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.welearn.wemath.R;


public class LessonSectionFragment extends Fragment {

    private CardView mJHS, mSHS;

    public LessonSectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View root =  inflater.inflate(R.layout.fragment_lesson_section, container, false);

        mJHS = root.findViewById(R.id.jhs_card);
        mSHS = root.findViewById(R.id.shs_card);


        mJHS.setOnClickListener(v -> {
            String section = "jhs";
            //setting up the navigation action with the parameter to be passed
            NavDirections action = LessonSectionFragmentDirections.actionNavigationLessonSectionToLessonYearFragment(section);
            Navigation.findNavController(v).navigate(action);

        });


        mSHS.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String section = "shs";
                NavDirections action = LessonSectionFragmentDirections.actionNavigationLessonSectionToLessonYearFragment(section);
                Navigation.findNavController(v).navigate(action);

            }
        });


        return root;
    }

}
