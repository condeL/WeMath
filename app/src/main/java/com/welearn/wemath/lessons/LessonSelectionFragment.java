package com.welearn.wemath.lessons;

import androidx.cardview.widget.CardView;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.welearn.wemath.R;

import static com.facebook.FacebookSdk.getApplicationContext;


public class LessonSelectionFragment extends Fragment {

    private LessonSelectionViewModel mViewModel;
    private String mYear, mSection;
    private int mTopic;

    public static LessonSelectionFragment newInstance() {
        return new LessonSelectionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lesson_selection_fragment, container, false);

        mYear = LessonSelectionFragmentArgs.fromBundle(getArguments()).getYear();
        mSection = LessonSelectionFragmentArgs.fromBundle(getArguments()).getSection();
        mTopic = LessonSelectionFragmentArgs.fromBundle(getArguments()).getTopic();
        //get a reference to the recycler view and give it to the custom adapter
        RecyclerView view = v.findViewById(R.id.lesson_list);
        ContentAdapter adapter = new ContentAdapter(view.getContext(), mYear, mSection, mTopic);
        view.setAdapter(adapter);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    //useless for now
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LessonSelectionViewModel.class);
        // TODO: Use the ViewModel
    }

    //View holder that will hold references to all the views in the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title, number;
        public ImageView button;
        public CardView card;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.lesson_card,parent, false));
            title = itemView.findViewById(R.id.lesson_selection_name);
            button = itemView.findViewById(R.id.lesson_selection_right_arrow);
            card = itemView.findViewById(R.id.lesson_selection_card);
        }
    }

    //the contentR adapter where the views are binded together
    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{

        private final String[] mNames; // mNumbers, mPercentages;
        private String mYear, mSection;
        private int mTopic, mClearedLesson;


        public ContentAdapter(Context context, String year, String section, int topic){
            //get the resource elements to put into the views
            Resources resources = context.getResources();

            mYear = year;
            mSection = section;
            mTopic = topic;

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String subject = mSection + mYear + mTopic;
            mClearedLesson = prefs.getInt(subject, 1);

            //to programmatically get the correct topics based on the bundled parameters
            String choice = "lessons_" + section + year + "_" + topic;
            //String choice2 = section + year + "_" + topic;
            int id = resources.getIdentifier(choice,"array",context.getPackageName());
            mNames = resources.getStringArray(id);


        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        //put the resource elements in the views using the ViewHolder

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.title.setText(mNames[position % mNames.length]);
            if(position > mClearedLesson-1) {
                holder.button.setImageResource(R.drawable.baseline_lock_24);
                holder.card.setCardBackgroundColor(getResources().getColor(R.color.lockedLesson));
            }
            else {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //set up the navigation action with the parameters
                        NavDirections action = LessonSelectionFragmentDirections.actionLessonSelectionFragmentToLessonActivity(mSection, mYear, mTopic, position + 1);
                        Navigation.findNavController(v).navigate(action);
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return mNames.length;
        }

    }

}
