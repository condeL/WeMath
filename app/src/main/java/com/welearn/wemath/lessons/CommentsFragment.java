package com.welearn.wemath.lessons;

/*Fragment for the comments*/

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.welearn.wemath.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class CommentsFragment extends Fragment {


    public CommentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_comments, container, false);

        RecyclerView view = root.findViewById(R.id.comments_view);
        ContentAdapter adapter = new ContentAdapter(view.getContext());
        view.setAdapter(adapter);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(getActivity()));

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = ViewModelProviders.of(this).get(LessonTopicViewModel.class);
        //mViewModel = new LessonTopicViewModel(mYear, mSection);
        // TODO: Use the ViewModel
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username, content, date;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.comment_card,parent, false));
            username = itemView.findViewById(R.id.comment_card_username);
            content = itemView.findViewById(R.id.comment_card_content);
            date = itemView.findViewById(R.id.comment_card_date);

            //itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_lessonTopicFragment_to_lessonSelectionFragment, null));
        }
    }

    //the content adapter where the views are binded together
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{

        private final ArrayList<String> mUsernames, mContents, mDates;
        //private final ProgressBar[] mProgressBars;

        //pass it the year and section to represent the choice of the user
        public ContentAdapter(Context context){
            Resources resources = context.getResources();
            //String year = viewModel.getYear();
            //String section = viewModel.getSection();

            mUsernames = new ArrayList<>();
            mContents = new ArrayList<>();
            mDates = new ArrayList<>();

            try {
                // get JSONObject from JSON file
                JSONObject obj = new JSONObject(loadJSONFromAsset(context));
                // fetch JSONArray named users
                JSONArray userArray = obj.getJSONArray("comments");
                // implement for loop for getting users list data
                for (int i = 0; i < userArray.length(); i++) {
                    // create a JSONObject for fetching single user data
                    JSONObject comment = userArray.getJSONObject(i);
                    // fetch email and name and store it in arraylist
                    mUsernames.add(comment.getString("user"));
                    mContents.add(comment.getString("content"));
                    mDates.add(comment.getString("date"));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public String loadJSONFromAsset(Context context) {
            String json = null;
            try {
                InputStream is = context.getAssets().open("comments.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            return json;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        //put the resource elements in the views using the ViewHolder
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.username.setText(mUsernames.get(position % mUsernames.size()));
            holder.content.setText(mContents.get(position % mContents.size()));
            //holder.percentage.setText(mPercentages[position % mPercentages.length]);
            holder.date.setText(mDates.get(position % mDates.size()));
        }

        @Override
        public int getItemCount() {
            return mUsernames.size();
        }

    }

}
