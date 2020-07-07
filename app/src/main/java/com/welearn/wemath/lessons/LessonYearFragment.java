package com.welearn.wemath.lessons;

/*fragment for selecting the school year*/

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.welearn.wemath.R;

public class LessonYearFragment extends Fragment {

    private CardView mYear1;
    private CardView mYear2;
    private CardView mYear3;

    public static LessonYearFragment newInstance() {
        return new LessonYearFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.lesson_year_fragment, container, false);

        //retrieve the section from the bundle
        final String section = LessonYearFragmentArgs.fromBundle(getArguments()).getSection();

        //set the actionbar title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(section.toUpperCase());

        mYear1 = root.findViewById(R.id.Year1_card);
        mYear2 = root.findViewById(R.id.Year2_card);
        mYear3 = root.findViewById(R.id.Year3_card);


        /*Setting the behaviours of the buttons*/

        mYear1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //parameters to be passed to the topics fragment
                String year = "1";

                //set up the navigation action with the parameters
                NavDirections action = LessonYearFragmentDirections.actionLessonYearFragmentToLessonTopicFragment(year, section);
                Navigation.findNavController(v).navigate(action);

            }
        });


        mYear2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String year = "2";
                //String section = LessonYearFragmentArgs.fromBundle(getArguments()).getSection();
                NavDirections action = LessonYearFragmentDirections.actionLessonYearFragmentToLessonTopicFragment(year, section);
                Navigation.findNavController(v).navigate(action);

            }
        });


        mYear3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String year = "3";
                //String section = LessonYearFragmentArgs.fromBundle(getArguments()).getSection();
                NavDirections action = LessonYearFragmentDirections.actionLessonYearFragmentToLessonTopicFragment(year, section);
                Navigation.findNavController(v).navigate(action);

            }
        });

        return root;
    }


}
