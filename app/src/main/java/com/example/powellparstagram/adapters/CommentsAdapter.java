package com.example.powellparstagram.adapters;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.powellparstagram.R;
import com.example.powellparstagram.objects.Comment;
import com.parse.ParseFile;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    public static final String TAG = "CommentsAdapter";

    List<Comment> comments;
    Context context;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivCommentProfilePicture;
        private TextView tvCommentText;
        private TextView tvCommentTimestamp;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCommentProfilePicture = itemView.findViewById(R.id.ivCommentProfilePicture);
            tvCommentText = itemView.findViewById(R.id.tvCommentText);
            tvCommentTimestamp = itemView.findViewById(R.id.tvCommentTimestamp);
        }

        public void bind(Comment comment){
            String commentUsername = comment.getCommentUser().getUsername();
            SpannableStringBuilder commentText = new SpannableStringBuilder( commentUsername + " " + comment.getCommentText());
            commentText.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, commentUsername.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvCommentText.setText(commentText);

            tvCommentTimestamp.setText(comment.getCreatedAt().toString());

            ParseFile profileImage = comment.getCommentUser().getParseFile("profileImage");
            if (profileImage != null) {
                Glide.with(context)
                        .load(profileImage.getUrl())
                        .transform(new CircleCrop())
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .into(ivCommentProfilePicture);
            }
            else {
                ivCommentProfilePicture.setImageResource(R.drawable.ic_baseline_person_24);
            }
        }
    }
}
