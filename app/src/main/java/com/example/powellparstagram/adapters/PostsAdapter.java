package com.example.powellparstagram.adapters;

import android.content.Context;
import android.os.Bundle;
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
import com.example.powellparstagram.R;
import com.example.powellparstagram.fragments.PostDetailFragment;
import com.example.powellparstagram.objects.Post;
import com.parse.ParseFile;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;
    private FragmentManager fragmentManager;

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
        private ImageView ivPostPicture;
        private ConstraintLayout clPost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvPostDescription = itemView.findViewById(R.id.tvPostDescription);
            ivPostPicture = itemView.findViewById(R.id.ivPostPicture);
            clPost = itemView.findViewById(R.id.clPost);
        }

        public void bind(final Post post) {
            tvUsername.setText(post.getUser().getUsername());
            tvPostDescription.setText(post.getDescription());

            ParseFile image = post.getImage();
            if (image != null){
                Glide.with(context)
                        .load(image.getUrl())
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .into(ivPostPicture);
            }
            else {
                ivPostPicture.setImageResource(R.drawable.ic_baseline_person_24);
            }

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
}
