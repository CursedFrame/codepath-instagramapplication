package com.example.powellparstagram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.powellparstagram.R;
import com.example.powellparstagram.adapters.PostsAdapter;
import com.example.powellparstagram.objects.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {

    public static final String TAG = "PostsFragment";
    public static final int POST_LIMIT = 20;

    private FragmentManager fragmentManager;
    private RecyclerView rvPosts;
    private SwipeRefreshLayout swipeRefreshContainer;
    protected PostsAdapter postsAdapter;
    protected List<Post> allPosts;

    public PostsFragment() {
        // Required empty public constructor
    }

    public PostsFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshContainer = (SwipeRefreshLayout) view.findViewById(R.id.scPosts);
        swipeRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPosts();
            }
        });

        swipeRefreshContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // RecyclerView and Adapter
        rvPosts = view.findViewById(R.id.rvPosts);
        allPosts = new ArrayList<>();
        postsAdapter = new PostsAdapter(getContext(), allPosts, fragmentManager);
        rvPosts.setAdapter(postsAdapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        queryPosts();
    }

    protected void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(POST_LIMIT);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post : posts){
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                allPosts.addAll(posts);
                postsAdapter.notifyDataSetChanged();
            }
        });
    }

    public void refreshPosts(){
        // Clear adapter and posts list
        postsAdapter.clear();
        allPosts.clear();

        // Add refreshed posts into allPosts list
        queryPosts();
        postsAdapter.addAll(allPosts);

        //
        swipeRefreshContainer.setRefreshing(false);
    }
}

// STEPS TO REMEMBER FOR RECYCLER VIEW & ADAPTER
// 1. Create layout for one row in the list
// 2. Create the adapter
// 3. Create data source
// 4. Set the adapter on the recycler view
// 5. Set the layout manager on the recycler view