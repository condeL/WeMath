package com.welearn.wemath;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class LessonSectionFragment extends Fragment {

    private ImageButton mJHS, mSHS;

    public LessonSectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View root =  inflater.inflate(R.layout.fragment_lesson_section, container, false);

        mJHS = root.findViewById(R.id.jhs_button);
        mSHS = root.findViewById(R.id.shs_button);

        mJHS.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_lesson_section_to_lessonYearFragment, null));

        mSHS.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_lesson_section_to_lessonYearFragment, null));

        return root;
    }

}
