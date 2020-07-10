package com.example.powellparstagram.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.powellparstagram.R;
import com.example.powellparstagram.fragments.PostDetailFragment;
import com.example.powellparstagram.objects.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class ProfilePostsAdapter extends RecyclerView.Adapter<ProfilePostsAdapter.ViewHolder>{

    public static final String TAG = "PostsAdapter";

    private Context context;
    private List<Post> posts;
    private FragmentManager fragmentManager;
    private ParseUser currentUser = ParseUser.getCurrentUser();

    public ProfilePostsAdapter(Context context, List<Post> posts, FragmentManager fragmentManager) {
        this.context = context;
        this.posts = posts;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_profile, parent, false);
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

        private ImageView ivProfilePostPicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePostPicture = itemView.findViewById(R.id.ivProfilePostPicture);
        }

        public void bind(final Post post) {
            // Bind post image
            ParseFile postImage = post.getImage();
            if (postImage != null){
                Glide.with(context)
                        .load(postImage.getUrl())
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .into(ivProfilePostPicture);
            }
            else {
                ivProfilePostPicture.setImageResource(R.drawable.ic_baseline_person_24);
            }

            ivProfilePostPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new PostDetailFragment(fragmentManager);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("post", post);
                    fragment.setArguments(bundle);

                    fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                }
            });
        }
    }
}
