package com.example.powellparstagram.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.powellparstagram.R;
import com.example.powellparstagram.objects.Post;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class PostDetailFragment extends Fragment {

    public static final String TAG = "PostDetailFragment";
    private ParseUser currentUser = ParseUser.getCurrentUser();

    private TextView tvDetailUsername;
    private TextView tvDetailTimestamp;
    private TextView tvDetailPostDescription;
    private TextView tvDetailLikeCount;
    private ImageView ivDetailPostPicture;
    private ImageView ivDetailProfilePicture;
    private ImageView ivDetailLike;
    private ImageView ivDetailComment;
    private FragmentManager fragmentManager;
    private boolean postLiked;
    private Post post;

    public PostDetailFragment(){

    }

    public PostDetailFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        post = bundle.getParcelable("post");

        tvDetailUsername = view.findViewById(R.id.tvDetailUsername);
        tvDetailTimestamp = view.findViewById(R.id.tvDetailTimestamp);
        tvDetailPostDescription = view.findViewById(R.id.tvDetailPostDescription);
        tvDetailLikeCount = view.findViewById(R.id.tvDetailLikeCount);
        ivDetailPostPicture = view.findViewById(R.id.ivDetailPostPicture);
        ivDetailProfilePicture = view.findViewById(R.id.ivDetailPostProfilePicture);
        ivDetailLike = view.findViewById(R.id.ivDetailLike);
        ivDetailComment = view.findViewById(R.id.ivDetailComment);

        tvDetailUsername.setText(post.getUser().getUsername());
        tvDetailTimestamp.setText(post.getCreatedAt().toString());
        tvDetailPostDescription.setText(post.getDescription());


        String numLikes = post.getLikeCount().toString();
        tvDetailLikeCount.setText(numLikes + " likes");

        // Bind post image
        ParseFile postImage = post.getImage();
        if (postImage != null){
            Glide.with(getContext())
                    .load(postImage.getUrl())
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .into(ivDetailPostPicture);
        }
        else {
            ivDetailPostPicture.setImageResource(R.drawable.ic_baseline_person_24);
        }

        // Bind profile image
        ParseFile profileImage = post.getUser().getParseFile("profileImage");
        if (profileImage != null) {
            Glide.with(getContext())
                    .load(profileImage.getUrl())
                    .transform(new CircleCrop())
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .into(ivDetailProfilePicture);
        }
        else {
            ivDetailProfilePicture.setImageResource(R.drawable.ic_baseline_person_24);
        }

        // Change liked icon whether liked or not
        ParseQuery<ParseObject> queryPosts = currentUser.getRelation("likedPosts").getQuery();
        queryPosts.whereEqualTo("objectId", post.getObjectId());
        queryPosts.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting post", e);
                    ivDetailLike.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_ufi_heart));
                    postLiked = false;
                    return;
                }
                ivDetailLike.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_ufi_heart_active));
                postLiked = true;
            }
        });

        // Bind onClickListener to ivLike to increment/decrement like count and update post like count
        ivDetailLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ParseRelation<Post> parseRelation = currentUser.getRelation("likedPosts");
                if (!postLiked) {
                    post.setLikeCount(post.getLikeCount().intValue() + 1);
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            parseRelation.add(post);
                            currentUser.saveInBackground();
                        }
                    });

                }
                else {
                    post.setLikeCount(post.getLikeCount().intValue() - 1);
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            parseRelation.remove(post);
                            currentUser.saveInBackground();
                        }
                    });
                }
                querySinglePost();
            }
        });

        ivDetailComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCommentDialogFragment();
            }
        });
    }

    private void querySinglePost(){
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo("objectId", post.getObjectId());
        query.getFirstInBackground(new GetCallback<Post>() {
            @Override
            public void done(Post object, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting post", e);
                }
            }
        });

        // If this delay isn't here, fragment will not update properly
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.detach(PostDetailFragment.this).attach(PostDetailFragment.this).commit();
            }
        }, 250);
    }
    private void goCommentDialogFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("post", post);

        CommentDialogFragment commentDialogFragment = CommentDialogFragment.newInstance("Some Title");
        commentDialogFragment.setArguments(bundle);
        commentDialogFragment.show(fragmentManager, "fragment_edit_name");
    }
}