package com.welearn.wemath.quizzes.Admin;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.welearn.wemath.R;

public class AdminQuizSelectionFragment extends Fragment {


    public AdminQuizSelectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_admin_quiz_selection, container, false);

        //get a reference to the recycler view and give it to the custom adapter
        RecyclerView view = root.findViewById(R.id.admin_quiz_list);
        ContentAdapter adapter = new ContentAdapter(view.getContext(), getActivity());
        view.setAdapter(adapter);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(getActivity()));

        return root;
    }

    //View holder that will hold references to all the views in the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title, details, difficulty;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.quiz_admin_card,parent, false));
            title = itemView.findViewById(R.id.admin_quiz_card_title);
            details = itemView.findViewById(R.id.admin_quiz_card_details);
            difficulty = itemView.findViewById(R.id.admin_quiz_card_difficulty);
        }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<AdminQuizSelectionFragment.ViewHolder>{

        private final String[] mTitles, mDetails, mDifficulty;
        private int[] mIds;
        Context mContext;
        Activity mActivity;

        //pass it the year and section to represent the choice of the user
        public ContentAdapter(Context context, Activity activity){
            Resources resources = context.getResources();

            mContext = context;
            mActivity = activity;

            mTitles = resources.getStringArray(R.array.admin_quiz_titles);
            mDetails = resources.getStringArray(R.array.admin_quiz_details);
            mDifficulty = resources.getStringArray(R.array.admin_quiz_difficulties);
            mIds = resources.getIntArray(R.array.admin_quiz_ids);
        }

        @Override
        public AdminQuizSelectionFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AdminQuizSelectionFragment.ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        //put the resource elements in the views using the ViewHolder
        @Override
        public void onBindViewHolder(AdminQuizSelectionFragment.ViewHolder holder, final int position) {
            holder.title.setText(mTitles[position % mTitles.length]);
            holder.difficulty.append(mDifficulty[position % mTitles.length]);
            holder.details.setText(mDetails[position % mTitles.length]);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quiz_id = mIds[position];
                    //NavDirections action = UserQuizSelectionFragmentDirections.actionUserQuizSelectionToUserQuizTakingActivity(false, quiz_id);
                    Toast.makeText(mContext,"Setting up quiz...", Toast.LENGTH_SHORT).show();
                    // Navigation.findNavController(v).navigate(action);

                    Intent intent = new Intent(mContext, AdminQuizTakingActivity.class);
                    intent.putExtra("quiz_id", quiz_id);
                    mContext.startActivity(intent);
                    mActivity.finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mTitles.length;
        }

    }
}
