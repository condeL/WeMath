package com.welearn.wemath.lessons;

/*fragment for selecting the lesson*/


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import android.widget.TextView;

import com.welearn.wemath.R;


public class LessonSelectionFragment extends Fragment {

    private String mYear, mSection, mTopicName;
    private int mTopic;
    private ContentAdapter mAdapter;
    private RecyclerView mRecyclerView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lesson_selection_fragment, container, false);

        mYear = LessonSelectionFragmentArgs.fromBundle(getArguments()).getYear();
        mSection = LessonSelectionFragmentArgs.fromBundle(getArguments()).getSection();
        mTopic = LessonSelectionFragmentArgs.fromBundle(getArguments()).getTopic();
        mTopicName = LessonSelectionFragmentArgs.fromBundle(getArguments()).getTopicName();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mTopicName);


        mRecyclerView = v.findViewById(R.id.lesson_list);
        mAdapter = new ContentAdapter(mRecyclerView.getContext(), mYear, mSection, mTopic);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }


    @Override
    public void onResume() {
        //in case the user has cleared new lessons
        super.onResume();
        mAdapter.mClearedLesson = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt(mSection+mYear+mTopic, 1);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);

    }

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

    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{

        private final String[] mNames; // mNumbers, mPercentages;
        private String mYear, mSection;
        private int mTopic, mClearedLesson;


        public ContentAdapter(Context context, String year, String section, int topic){
            Resources resources = context.getResources();

            mYear = year;
            mSection = section;
            mTopic = topic;

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            String subject = mSection + mYear + mTopic;
            mClearedLesson = prefs.getInt(subject, 1);

            //to programmatically get the correct topics based on the bundled parameters
            String choice = "lessons_" + section + year + "_" + topic;
            int id = resources.getIdentifier(choice,"array",context.getPackageName());
            mNames = resources.getStringArray(id);


        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.title.setText(mNames[position % mNames.length]);
            if(position > mClearedLesson-1) {
                holder.button.setImageResource(R.drawable.baseline_lock_24);
                holder.card.setCardBackgroundColor(getResources().getColor(R.color.lockedLesson));
            }
            else {
                holder.itemView.setOnClickListener(v -> {
                    //set up the navigation action with the parameters
                    NavDirections action = LessonSelectionFragmentDirections.actionLessonSelectionFragmentToLessonActivity(mSection, mYear, mTopic, position + 1, getItemCount());
                    Navigation.findNavController(v).navigate(action);
                });
            }
        }

        @Override
        public int getItemCount() {
            return mNames.length;
        }
    }
}
