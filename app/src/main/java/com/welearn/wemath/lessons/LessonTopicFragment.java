package com.welearn.wemath.lessons;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.welearn.wemath.R;

public class LessonTopicFragment extends Fragment {

    //not used yet
    private LessonTopicViewModel mViewModel;
    //Strings to get represent the selection of the user
    private String mYear, mSection;



    public static LessonTopicFragment newInstance() {
        return new LessonTopicFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lesson_topic_fragment, container, false);

        //retrieve the parameters from the bundle
        mYear = LessonTopicFragmentArgs.fromBundle(getArguments()).getYear();
        mSection = LessonTopicFragmentArgs.fromBundle(getArguments()).getSection();
        //mViewModel = new LessonTopicViewModel(mYear, mSection);

        //set the title of the actionbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mSection.toUpperCase() + " Year " + mYear);
        //get a reference to the recycler view and give it to the custom adapter
        RecyclerView view = v.findViewById(R.id.topic_list);
        ContentAdapter adapter = new ContentAdapter(view.getContext(), mYear, mSection);
        view.setAdapter(adapter);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    //useless for now
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = ViewModelProviders.of(this).get(LessonTopicViewModel.class);
        //mViewModel = new LessonTopicViewModel(mYear, mSection);
        // TODO: Use the ViewModel
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
            //progressBar = itemView.findViewById(R.id.progressBar);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Context context = v.getContext();
                    //Intent intent = new Intent(context, DetailActivity.class);
                    //intent.putExtra(DetailActivity.EXTRA_POSITION, getAdapterPosition());
                    //context.startActivity(intent);
                }
            });*/
            //itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_lessonTopicFragment_to_lessonSelectionFragment, null));
        }
    }

    //the contentR adapter where the views are binded together
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{

        private final String[] mNames, mNumbers, mPercentages;
        //private final ProgressBar[] mProgressBars;
        String mYear, mSection;
        Context mContext;

        //pass it the year and section to represent the choice of the user
        public ContentAdapter(Context context, String year, String section){
            Resources resources = context.getResources();
            //String year = viewModel.getYear();
            //String section = viewModel.getSection();
            mYear = year;
            mSection = section;
            mContext = context;

            //to programmatically get the correct topics based on the bundled parameters
            String choice = "topics_" + section + year;
            int id = resources.getIdentifier(choice,"array",context.getPackageName());

            mNames = resources.getStringArray(id);
            mNumbers = resources.getStringArray(id);
            mPercentages = resources.getStringArray(id);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        //put the resource elements in the views using the ViewHolder
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.title.setText(mNames[position % mNames.length]);
            //to programmatically get the correct topics based on the bundled parameters
            Resources resources = mContext.getResources();
            String choice = "lessons_" + mSection + mYear + "_" + (position+1);
            int id = resources.getIdentifier(choice,"array",mContext.getPackageName());

            String[] lessons = resources.getStringArray(id);
            holder.number.setText(lessons.length + " lessons");
            //holder.percentage.setText(mPercentages[position % mPercentages.length]);
            //holder.percentage.setText("50%");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //set up the navigation action with the parameters
                    NavDirections action = LessonTopicFragmentDirections.actionLessonTopicFragmentToLessonSelectionFragment(mYear, mSection, position+1);
                    Navigation.findNavController(v).navigate(action);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mNames.length;
        }

    }

}
