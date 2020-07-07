package com.welearn.wemath.lessons;

/*fragment for selecting the lesson topic*/

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.welearn.wemath.R;

public class LessonTopicFragment extends Fragment {

    private String mYear, mSection;


    public static LessonTopicFragment newInstance() {
        return new LessonTopicFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lesson_topic_fragment, container, false);

        mYear = LessonTopicFragmentArgs.fromBundle(getArguments()).getYear();
        mSection = LessonTopicFragmentArgs.fromBundle(getArguments()).getSection();

        //set the title of the actionbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mSection.toUpperCase() + " Year " + mYear);

        RecyclerView view = v.findViewById(R.id.topic_list);
        ContentAdapter adapter = new ContentAdapter(view.getContext(), mYear, mSection);
        view.setAdapter(adapter);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }


    //View holder that will hold references to all the views in the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title, number, percentage;
        public ProgressBar progressBar;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.topic_card,parent, false));
            title = itemView.findViewById(R.id.topic_name);
            number = itemView.findViewById(R.id.topic_lessons_number);
            percentage = itemView.findViewById(R.id.topic_completed);
            progressBar = itemView.findViewById(R.id.topic_progressBar);


        }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{

        private final String[] mNames;
        ProgressBar mProgressBar;
        String mYear, mSection;
        Context mContext;
        SharedPreferences mPrefs;

        //pass it the year and section to represent the choice of the user
        public ContentAdapter(Context context, String year, String section){
            Resources resources = context.getResources();
            mYear = year;
            mSection = section;
            mContext = context;

            //to programmatically get the correct topics based on the bundled parameters
            String choice = "topics_" + section + year;
            int id = resources.getIdentifier(choice,"array",context.getPackageName());
            mNames = resources.getStringArray(id);

            mPrefs = PreferenceManager.getDefaultSharedPreferences((mContext));

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.title.setText(mNames[position % mNames.length]);
            //to programmatically get the correct topics based on the bundled parameters
            Resources resources = mContext.getResources();
            String choice = "lessons_" + mSection + mYear + "_" + (position+1);
            int id = resources.getIdentifier(choice,"array",mContext.getPackageName());

            String[] lessons = resources.getStringArray(id);
            int lessonSize = lessons.length;
            holder.number.setText(lessonSize + " lessons");

            String subject = mSection + mYear + (position+1);
            int cleared = mPrefs.getInt(subject, 1);
            holder.progressBar.setMax(lessonSize);
            holder.progressBar.setProgress(cleared-1);
            float progressPercent = (((float)cleared-1)/(float)lessonSize)*100;
            holder.percentage.setText((int)progressPercent + "%");

            holder.itemView.setOnClickListener(v -> {
                //set up the navigation action with the parameters
                NavDirections action = LessonTopicFragmentDirections.actionLessonTopicFragmentToLessonSelectionFragment(mYear, mSection, position+1, mNames[position % mNames.length]);
                Navigation.findNavController(v).navigate(action);
            });
        }

        @Override
        public int getItemCount() {
            return mNames.length;
        }

    }

}
