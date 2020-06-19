package com.welearn.wemath.lessons;

/*Fragment for the comments*/

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.welearn.wemath.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class CommentsFragment extends Fragment {

    private FirebaseUser mUser;
    FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private String mURL;

    private TextView mProfilePicture;
    private EditText mMessage;
    private ImageButton mSendButton;
    private ContentAdapterComments mAdapter;
    private RecyclerView mView;


    public CommentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_comments, container, false);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mURL = getArguments().getString("choice");
        String name = mUser.getDisplayName();

        mProfilePicture = root.findViewById(R.id.comments_fragment_profile_picture);
        mMessage = root.findViewById(R.id.comments_fragment_editText);
        mSendButton = root.findViewById(R.id.comments_fragment_send_button);

        mProfilePicture.setText(String.valueOf(name.toUpperCase().charAt(0)));

        int[] profileColors = getResources().getIntArray(R.array.profile_colors);
        Paint paint = new Paint();
        paint.setColor(profileColors[name.charAt(0)%6]);
        mProfilePicture.getBackground().setColorFilter(paint.getColor(), PorterDuff.Mode.ADD);

        mSendButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick (View v){

                if(!TextUtils.isEmpty(mMessage.getText()))
                    postComment(getContext(), mMessage);
                else
                    Toast.makeText(getContext(), "Please enter a comment", Toast.LENGTH_LONG).show();


            }
        });

        Toast.makeText(getContext(),"Fetching comments...", Toast.LENGTH_LONG).show();
        mView = root.findViewById(R.id.comments_view);
        mAdapter = new ContentAdapterComments(mView.getContext(), mURL);
        mView.setAdapter(mAdapter);
        mView.setHasFixedSize(true);
        mView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = ViewModelProviders.of(this).get(LessonTopicViewModel.class);
        //mViewModel = new LessonTopicViewModel(mYear, mSection);
        // TODO: Use the ViewModel
    }



    void postComment(final Context context, final EditText editText){
        Comment comment = new Comment(mUser.getUid(),mUser.getDisplayName(),editText.getText().toString());
        Toast.makeText(context,"Sending comment...", Toast.LENGTH_LONG).show();

        mDb.collection(mURL).document()
                .set(comment)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("success", "DocumentSnapshot successfully written!");
                        Toast.makeText(context,"Comment sent!", Toast.LENGTH_LONG).show();
                        editText.setText("");
                        mAdapter = new ContentAdapterComments(getContext(),mURL);
                        mView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("failure", "Error writing document", e);
                        Toast.makeText(context,"Error sending comment", Toast.LENGTH_LONG).show();

                    }
                });
        //Toast.makeText(context, editText.getText().toString(), Toast.LENGTH_LONG).show();

    }









    public static class ViewHolderComments extends RecyclerView.ViewHolder{
        public TextView usernameC, contentC, dateC, pictureC;
        public TextView repliesButtonC;
        public Button replyButtonC;
        public EditText messageC;
        public TextView downvotesC, upvotesC;
        public ImageView downvotesIC, upvotesIC;
        public ImageButton sendButtonC;
        public LinearLayout replyBarC;
        public RecyclerView repliesRecycler;

        public ViewHolderComments(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.comment_card,parent, false));
            usernameC = itemView.findViewById(R.id.comment_card_username);
            contentC = itemView.findViewById(R.id.comment_card_content);
            dateC = itemView.findViewById(R.id.comment_card_date);
            pictureC = itemView.findViewById(R.id.comment_card_profile_pic);

            downvotesC = itemView.findViewById(R.id.comment_card_downvotes_numbers);
            upvotesC = itemView.findViewById(R.id.comment_card_upvotes_numbers);

            downvotesIC = itemView.findViewById(R.id.comment_card_downvote);
            upvotesIC = itemView.findViewById(R.id.comment_card_upvote);

            repliesButtonC = itemView.findViewById(R.id.comment_card_replies);

            replyButtonC = itemView.findViewById(R.id.comment_card_reply_button);
            replyBarC = itemView.findViewById(R.id.comment_card_reply_bar);
            messageC = itemView.findViewById(R.id.comment_card_editText);
            sendButtonC = itemView.findViewById(R.id.comment_card_send_button);
            repliesRecycler = itemView.findViewById(R.id.replies_view);


        }
    }

    //the contentR adapter where the views are binded together
    public static class ContentAdapterComments extends RecyclerView.Adapter<ViewHolderComments>{

        //private final ArrayList<String> mUsernamesC, mContentsC, mDatesC, mDownvotesC, mUpvotesC;
        private final ArrayList<Pair<Comment,String>> mCommentsC;
        Context mContextC;
        FirebaseFirestore mDB = FirebaseFirestore.getInstance();
        private String mURLC;


        //private final ProgressBar[] mProgressBars;

        //pass it the year and section to represent the choice of the user
        public ContentAdapterComments(final Context context, String url){
            Resources resources = context.getResources();
            mURLC = url;
            mCommentsC = new ArrayList<>();

            mContextC = context;




            /*Query query = mDB.collection(mURL)
                    .orderBy("timestamp", Query.Direction.ASCENDING);*/


            mDB.collection(mURLC)
                    .orderBy("timestamp",Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("GETTING", document.getId() + " => " + document.getData());
                                    mCommentsC.add(new Pair(document.toObject(Comment.class), document.getId()));

                                    Toast.makeText(context,"Fetching successful!", Toast.LENGTH_LONG).show();
                                    notifyDataSetChanged();

                                }
                            } else {
                                Log.d("GETTING", "Error getting documents: ", task.getException());
                                Toast.makeText(context,"Fetching failed", Toast.LENGTH_LONG).show();

                            }
                        }
                    });


        }



        @Override
        public ViewHolderComments onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolderComments(LayoutInflater.from(parent.getContext()), parent);
        }

        //put the resource elements in the views using the ViewHolder
        @Override
        public void onBindViewHolder(final ViewHolderComments holder, final int position) {


            holder.usernameC.setText(mCommentsC.get(position % mCommentsC.size()).first.getName());
            holder.contentC.setText(mCommentsC.get(position % mCommentsC.size()).first.getMessage());
            holder.dateC.setText(new SimpleDateFormat("dd MMM yyyy | hh:mm aa").format(mCommentsC.get(position % mCommentsC.size()).first.getTimestamp()));
            holder.downvotesC.setText(String.valueOf(mCommentsC.get(position%mCommentsC.size()).first.getDownvotes()));
            holder.upvotesC.setText(String.valueOf(mCommentsC.get(position%mCommentsC.size()).first.getUpvotes()));

            String name = mCommentsC.get(position%mCommentsC.size()).first.getName();
            holder.pictureC.setText(String.valueOf(name.toUpperCase().charAt(0)));
            int[] profileColors = mContextC.getResources().getIntArray(R.array.profile_colors);
            Paint paint = new Paint();
            paint.setColor(profileColors[name.charAt(0)%6]);
            holder.pictureC.getBackground().setColorFilter(paint.getColor(), PorterDuff.Mode.ADD);

            holder.repliesButtonC.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick (View v){

                    if(holder.repliesRecycler.getVisibility() == View.GONE){
                        holder.repliesRecycler.setVisibility(View.VISIBLE);
                    } else {
                        holder.repliesRecycler.setVisibility(View.GONE);
                    }
                }
            });

            holder.replyButtonC.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick (View v){

                    if(holder.replyBarC.getVisibility() == View.GONE){
                        holder.replyBarC.setVisibility(View.VISIBLE);
                    } else {
                        holder.replyBarC.setVisibility(View.GONE);
                    }
                }
            });

            holder.sendButtonC.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick (View v) {


                    if (!TextUtils.isEmpty(holder.messageC.getText())) {

                        Toast.makeText(mContextC, "Sending reply...", Toast.LENGTH_LONG).show();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        Comment comment = new Comment(user.getUid(), user.getDisplayName(), holder.messageC.getText().toString());

                        mDB.document(mURLC + "/" + mCommentsC.get(position).second).collection("replies").document()
                                .set(comment)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("success", "DocumentSnapshot successfully written!");
                                        Toast.makeText(mContextC, "Reply sent!", Toast.LENGTH_LONG).show();
                                        holder.messageC.setText("");
                                        notifyItemChanged(position);

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("failure", "Error writing document", e);
                                        Toast.makeText(mContextC, "Error sending reply", Toast.LENGTH_LONG).show();
                                    }
                                });

                    } else {
                        Toast.makeText(mContextC, "Please enter a reply", Toast.LENGTH_LONG).show();
                    }
                }
            });


            holder.downvotesC.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick (View v) {


                        mDB.document(mURLC + "/" + mCommentsC.get(position).second)
                                .update("downvotes", mCommentsC.get(position).first.getDownvotes()+1)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("success", "DocumentSnapshot successfully written!");
                                        Toast.makeText(mContextC, "Downvoted", Toast.LENGTH_LONG).show();
                                        notifyItemChanged(position);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("failure", "Error writing document", e);
                                        Toast.makeText(mContextC, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                });

                }
            });

            holder.downvotesIC.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick (View v) {


                    mDB.document(mURLC + "/" + mCommentsC.get(position).second)
                            .update("downvotes", mCommentsC.get(position).first.getDownvotes()+1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("success", "DocumentSnapshot successfully written!");
                                    Toast.makeText(mContextC, "Downvoted", Toast.LENGTH_LONG).show();
                                    notifyItemChanged(position);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("failure", "Error writing document", e);
                                    Toast.makeText(mContextC, "Error", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            });

            holder.upvotesC.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick (View v) {


                    mDB.document(mURLC + "/" + mCommentsC.get(position).second)
                            .update("upvotes", mCommentsC.get(position).first.getUpvotes()+1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("success", "DocumentSnapshot successfully written!");
                                    Toast.makeText(mContextC, "Upvoted", Toast.LENGTH_LONG).show();
                                    notifyItemChanged(position);

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("failure", "Error writing document", e);
                                    Toast.makeText(mContextC, "Error", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            });

            holder.upvotesIC.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick (View v) {


                    mDB.document(mURLC + "/" + mCommentsC.get(position).second)
                            .update("upvotes", mCommentsC.get(position).first.getUpvotes()+1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("success", "DocumentSnapshot successfully written!");
                                    Toast.makeText(mContextC, "Upvoted", Toast.LENGTH_LONG).show();
                                    notifyItemChanged(position);

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("failure", "Error writing document", e);
                                    Toast.makeText(mContextC, "Error", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            });


            ContentAdapterReplies adapterR = new ContentAdapterReplies(mContextC, mCommentsC.get(position).second,position, this,mURLC);
            holder.repliesRecycler.setAdapter(adapterR);
            holder.repliesRecycler.setHasFixedSize(true);
            //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContextC,LinearLayoutManager.VERTICAL, false);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContextC);
            holder.repliesRecycler.setLayoutManager(linearLayoutManager);

           //holder.repliesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        @Override
        public int getItemCount() {
            return mCommentsC.size();
        }

        @Override
        public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
            super.registerAdapterDataObserver(observer);

            }

    }











    public static class ViewHolderReplies extends RecyclerView.ViewHolder{
        public TextView usernameR, contentR, dateR, pictureR;
        public Button replyButtonR;
        public EditText messageR;
        public TextView downvotesR, upvotesR;
        public ImageView downvotesIR, upvotesIR;
        public ImageButton sendButtonR;
        public LinearLayout replyBarR;



        public ViewHolderReplies(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.comment_reply_card, parent, false));
            usernameR = itemView.findViewById(R.id.reply_card_username);
            contentR = itemView.findViewById(R.id.reply_card_content);
            dateR = itemView.findViewById(R.id.reply_card_date);
            pictureR = itemView.findViewById(R.id.reply_profile_pic);

            downvotesR = itemView.findViewById(R.id.reply_card_downvotes_numbers);
            upvotesR = itemView.findViewById(R.id.reply_card_upvotes_numbers);

            downvotesIR = itemView.findViewById(R.id.reply_card_downvote);
            upvotesIR = itemView.findViewById(R.id.reply_card_upvote);

            replyButtonR = itemView.findViewById(R.id.reply_card_reply_button);
            replyBarR = itemView.findViewById(R.id.reply_card_reply_bar);
            messageR = itemView.findViewById(R.id.reply_card_editText);
            sendButtonR = itemView.findViewById(R.id.reply_card_send_button);

        }
    }

    //the contentR adapter where the views are binded together
    public static class ContentAdapterReplies extends RecyclerView.Adapter<ViewHolderReplies>{

        Context mContextR;
        private final ArrayList<Pair<Comment,String>> mCommentsR;
        FirebaseFirestore mDB = FirebaseFirestore.getInstance();
        String mParentRef;
        private String mURLR;
        int mParentPosition;
        ContentAdapterComments mParentAdapter;

        //private final ProgressBar[] mProgressBars;

        //pass it the year and section to represent the choice of the user
        public ContentAdapterReplies(Context context, String parentRef, int parentPosition, ContentAdapterComments parentAdapter, String url){
            Resources resources = context.getResources();
            mURLR = url;
            //String year = viewModel.getYear();
            //String section = viewModel.getSection();
            mContextR = context;
            mCommentsR = new ArrayList<>();
            mParentRef = parentRef;
            mParentAdapter = parentAdapter;
            mParentPosition = parentPosition;


            mDB.collection(mURLR + "/" + mParentRef+"/replies")
                    .orderBy("timestamp",Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("GETTING", document.getId() + " => " + document.getData());
                                    mCommentsR.add(new Pair(document.toObject(Comment.class), document.getId()));

                                    notifyDataSetChanged();

                                }
                            } else {
                                Log.d("GETTING", "Error getting documents: ", task.getException());
                                Toast.makeText(mContextR,"Fetching failed", Toast.LENGTH_LONG).show();

                            }
                        }
                    });

        }



        @Override
        public ViewHolderReplies onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolderReplies(LayoutInflater.from(parent.getContext()), parent);
        }

        //put the resource elements in the views using the ViewHolder
        @Override
        public void onBindViewHolder(final ViewHolderReplies holder, final int position) {



            holder.usernameR.setText(mCommentsR.get(position % mCommentsR.size()).first.getName());
            holder.contentR.setText(mCommentsR.get(position % mCommentsR.size()).first.getMessage());
            holder.dateR.setText(new SimpleDateFormat("dd MMM yyyy | hh:mm aa").format(mCommentsR.get(position % mCommentsR.size()).first.getTimestamp()));
            holder.downvotesR.setText(String.valueOf(mCommentsR.get(position%mCommentsR.size()).first.getDownvotes()));
            holder.upvotesR.setText(String.valueOf(mCommentsR.get(position%mCommentsR.size()).first.getUpvotes()));

            String name = mCommentsR.get(position%mCommentsR.size()).first.getName();
            holder.pictureR.setText(String.valueOf(name.toUpperCase().charAt(0)));
            int[] profileColors = mContextR.getResources().getIntArray(R.array.profile_colors);
            Paint paint = new Paint();
            paint.setColor(profileColors[name.charAt(0)%6]);
            holder.pictureR.getBackground().setColorFilter(paint.getColor(), PorterDuff.Mode.ADD);


            holder.replyButtonR.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick (View v){

                    if(holder.replyBarR.getVisibility() == View.GONE){
                        holder.replyBarR.setVisibility(View.VISIBLE);
                    } else {
                        holder.replyBarR.setVisibility(View.GONE);
                    }
                }
            });

            holder.sendButtonR.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick (View v) {

                    if (!TextUtils.isEmpty(holder.messageR.getText())) {

                        Toast.makeText(mContextR, "Sending reply...", Toast.LENGTH_LONG).show();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        Comment comment = new Comment(user.getUid(), user.getDisplayName(), holder.messageR.getText().toString());

                        mDB.document(mURLR + "/" + mParentRef).collection("replies").document()
                                .set(comment)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("success", "DocumentSnapshot successfully written!");
                                        Toast.makeText(mContextR, "Reply sent!", Toast.LENGTH_LONG).show();
                                        holder.messageR.setText("");
                                        mParentAdapter.notifyItemChanged(mParentPosition);

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("failure", "Error writing document", e);
                                        Toast.makeText(mContextR, "Error sending reply", Toast.LENGTH_LONG).show();

                                    }
                                });

                    } else{
                        Toast.makeText(mContextR, "Please enter a reply", Toast.LENGTH_LONG).show();
                    }
                }
            });


            holder.downvotesR.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick (View v) {


                    mDB.document(mURLR + "/" + mParentRef +"/replies/"+mCommentsR.get(position).second)
                            .update("downvotes", mCommentsR.get(position).first.getDownvotes()+1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("success", "DocumentSnapshot successfully written!");
                                    Toast.makeText(mContextR, "Downvoted", Toast.LENGTH_LONG).show();
                                    notifyItemChanged(position);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("failure", "Error writing document", e);
                                    Toast.makeText(mContextR, "Error", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            });

            holder.downvotesIR.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick (View v) {


                    mDB.document(mURLR + "/" + mParentRef +"/replies/"+mCommentsR.get(position).second)
                            .update("downvotes", mCommentsR.get(position).first.getDownvotes()+1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("success", "DocumentSnapshot successfully written!");
                                    Toast.makeText(mContextR, "Downvoted", Toast.LENGTH_LONG).show();
                                    notifyItemChanged(position);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("failure", "Error writing document", e);
                                    Toast.makeText(mContextR, "Error", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            });

            holder.upvotesR.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick (View v) {


                    mDB.document(mURLR + "/" + mParentRef +"/replies/"+mCommentsR.get(position).second)
                            .update("upvotes", mCommentsR.get(position).first.getUpvotes()+1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("success", "DocumentSnapshot successfully written!");
                                    Toast.makeText(mContextR, "Upvoted", Toast.LENGTH_LONG).show();
                                    notifyItemChanged(position);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("failure", "Error writing document", e);
                                    Toast.makeText(mContextR, "Error", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            });

            holder.upvotesIR.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick (View v) {


                    mDB.document(mURLR + "/" + mParentRef +"/replies/"+mCommentsR.get(position).second)
                            .update("upvotes", mCommentsR.get(position).first.getUpvotes()+1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("success", "DocumentSnapshot successfully written!");
                                    Toast.makeText(mContextR, "Upvoted", Toast.LENGTH_LONG).show();
                                    notifyItemChanged(position);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("failure", "Error writing document", e);
                                    Toast.makeText(mContextR, "Error", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            });
        }

        @Override
        public int getItemCount() {
            return mCommentsR.size();
        }

    }

}
