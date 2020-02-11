package com.welearn.wemath.lessons;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

    private LessonTopicViewModel mViewModel;



    public static LessonTopicFragment newInstance() {
        return new LessonTopicFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lesson_topic_fragment, container, false);

        RecyclerView view = v.findViewById(R.id.topic_list);
        ContentAdapter adapter = new ContentAdapter(view.getContext());
        view.setAdapter(adapter);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LessonTopicViewModel.class);
        // TODO: Use the ViewModel
    }

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
            itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_lessonTopicFragment_to_lessonSelectionFragment, null));
        }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{

        private final String[] mNames, mNumbers, mPercentages;
        //private final ProgressBar[] mProgressBars;

        public ContentAdapter(Context context){
            Resources resources = context.getResources();
            mNames = resources.getStringArray(R.array.year1);
            mNumbers = resources.getStringArray(R.array.numbersyear1);
            mPercentages = resources.getStringArray(R.array.percentagesyear1);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.title.setText(mNames[position % mNames.length]);
            holder.number.setText(mNumbers[position % mNumbers.length]);
            holder.percentage.setText(mPercentages[position % mPercentages.length]);
        }

        @Override
        public int getItemCount() {
            return mNames.length;
        }

    }

}
