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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.welearn.wemath.R;

public class LessonSelectionFragment extends Fragment {

    private LessonSelectionViewModel mViewModel;

    public static LessonSelectionFragment newInstance() {
        return new LessonSelectionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lesson_selection_fragment, container, false);

        RecyclerView view = v.findViewById(R.id.lesson_list);
        ContentAdapter adapter = new ContentAdapter(view.getContext());
        view.setAdapter(adapter);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LessonSelectionViewModel.class);
        // TODO: Use the ViewModel
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title, number, percentage;
        public ProgressBar progressBar;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.lesson_card,parent, false));
            title = itemView.findViewById(R.id.lesson_selection_name);
            number = itemView.findViewById(R.id.lesson_selection_number);
            percentage = itemView.findViewById(R.id.lesson_selection_completed);

            itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_lessonSelectionFragment_to_lessonActivity, null));
        }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{

        private final String[] mNames, mNumbers, mPercentages;
        //private final ProgressBar[] mProgressBars;

        public ContentAdapter(Context context){
            Resources resources = context.getResources();
            mNames = resources.getStringArray(R.array.lessons_shs1);
            mNumbers = resources.getStringArray(R.array.lessons_shs1);
            mPercentages = resources.getStringArray(R.array.lessons_shs1);
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
