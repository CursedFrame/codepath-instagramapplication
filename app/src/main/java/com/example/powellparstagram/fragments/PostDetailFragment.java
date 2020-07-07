package com.example.powellparstagram.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.powellparstagram.R;
import com.example.powellparstagram.objects.Post;
import com.parse.ParseFile;

public class PostDetailFragment extends Fragment {

    public static final String TAG = "PostDetailFragment";

    private TextView tvDetailUsername;
    private TextView tvDetailTimestamp;
    private TextView tvDetailDescription;
    private ImageView ivDetailPicture;

    public PostDetailFragment() {
        // Required empty public constructor
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
        Post post = bundle.getParcelable("post");

        tvDetailUsername = view.findViewById(R.id.tvDetailUsername);
        tvDetailTimestamp = view.findViewById(R.id.tvDetailTimestamp);
        tvDetailDescription = view.findViewById(R.id.tvDetailDescription);
        ivDetailPicture = view.findViewById(R.id.ivDetailPicture);

        tvDetailUsername.setText(post.getUser().getUsername());
        tvDetailTimestamp.setText(post.getCreatedAt().toString());
        tvDetailDescription.setText(post.getDescription());

        ParseFile image = post.getImage();
        if (image != null){
            Glide.with(getContext())
                    .load(image.getUrl())
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .into(ivDetailPicture);
        }
        else {
            ivDetailPicture.setImageResource(R.drawable.ic_baseline_person_24);
        }
    }
}