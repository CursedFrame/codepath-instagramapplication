package com.example.powellparstagram.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.powellparstagram.R;
import com.example.powellparstagram.fragments.PostDetailFragment;
import com.example.powellparstagram.objects.Post;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    public static final String TAG = "PostsAdapter";

    private Context context;
    private List<Post> posts;
    private FragmentManager fragmentManager;
    private ParseUser currentUser = ParseUser.getCurrentUser();

    public PostsAdapter(Context context, List<Post> posts, FragmentManager fragmentManager) {
        this.context = context;
        this.posts = posts;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvUsername;
        private TextView tvPostDescription;
        private TextView tvLikeCount;
        private ImageView ivPostPicture;
        private ImageView ivPostProfilePicture;
        private ImageView ivLike;
        private ConstraintLayout clPost;
        private boolean postLiked;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvPostDescription = itemView.findViewById(R.id.tvPostDescription);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            ivPostPicture = itemView.findViewById(R.id.ivPostPicture);
            ivPostProfilePicture = itemView.findViewById(R.id.ivPostProfilePicture);
            ivLike = itemView.findViewById(R.id.ivLike);
            clPost = itemView.findViewById(R.id.clPost);
        }

        public void bind(final Post post) {
            tvUsername.setText(post.getUser().getUsername());
            tvPostDescription.setText(post.getDescription());
            String numLikes = post.getLikeCount().toString();
            tvLikeCount.setText(numLikes + " likes");

            // Bind post image
            ParseFile postImage = post.getImage();
            if (postImage != null){
                Glide.with(context)
                        .load(postImage.getUrl())
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .into(ivPostPicture);
            }
            else {
                ivPostPicture.setImageResource(R.drawable.ic_baseline_person_24);
            }

            // Bind profile image
            ParseFile profileImage = post.getUser().getParseFile("profileImage");
            if (profileImage != null) {
                Glide.with(context)
                        .load(profileImage.getUrl())
                        .transform(new CircleCrop())
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .into(ivPostProfilePicture);
            }
            else {
                ivPostProfilePicture.setImageResource(R.drawable.ic_baseline_person_24);
            }

            // Bind onClickListener to clPost to open PostDetailFragment
            clPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new PostDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("post", post);
                    fragment.setArguments(bundle);

                    fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                }
            });

            ParseQuery<ParseObject> queryPosts = currentUser.getRelation("likedPosts").getQuery();
            queryPosts.whereEqualTo("objectId", post.getObjectId());
            queryPosts.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e != null){
                        Log.e(TAG, "Issue with getting post", e);
                        ivLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_ufi_heart));
                        postLiked = false;
                        return;
                    }
                    ivLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_ufi_heart_active));
                    postLiked = true;
                }
            });

            // Bind onClickListener to ivLike to increment/decrement like count and update post like count
            ivLike.setOnClickListener(new View.OnClickListener() {
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
                                ivLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_ufi_heart_active));
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
                                ivLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_ufi_heart));
                            }
                        });
                    }
                    querySinglePost(post, getAdapterPosition());
                }
            });
        }
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    private void querySinglePost(Post post, final int position){
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo("objectId", post.getObjectId());
        query.getFirstInBackground(new GetCallback<Post>() {
            @Override
            public void done(Post object, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting post", e);
                }
                posts.remove(position);
                posts.add(position, object);
                notifyItemChanged(position);
            }
        });
    }
}
