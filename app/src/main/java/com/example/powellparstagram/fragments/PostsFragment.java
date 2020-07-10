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
import com.example.powellparstagram.listeners.EndlessRecyclerViewScrollListener;
import com.example.powellparstagram.objects.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostsFragment extends Fragment {

    public static final String TAG = "PostsFragment";
    private int POST_LIMIT;
    private Date lastPostDate;

    private FragmentManager fragmentManager;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView rvPosts;
    private SwipeRefreshLayout swipeRefreshContainer;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    protected PostsAdapter postsAdapter;
    protected List<Post> allPosts;

    public PostsFragment() {
        // Required empty public constructor
    }

    public PostsFragment(FragmentManager fragmentManager, int postLimit) {
        this.fragmentManager = fragmentManager;
        this.POST_LIMIT = postLimit;
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
        linearLayoutManager  = new LinearLayoutManager(getContext());
        allPosts = new ArrayList<>();
        postsAdapter = new PostsAdapter(getContext(), allPosts, fragmentManager);
        rvPosts.setAdapter(postsAdapter);
        rvPosts.setLayoutManager(linearLayoutManager);

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMorePosts();
            }
        };

        rvPosts.addOnScrollListener(endlessRecyclerViewScrollListener);

        queryPosts();
    }

    private void loadMorePosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(POST_LIMIT);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.whereLessThan("createdAt", lastPostDate);
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
                lastPostDate = allPosts.get(allPosts.size() - 1).getCreatedAt();
            }
        });
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
                lastPostDate = allPosts.get(allPosts.size() - 1).getCreatedAt();
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

        swipeRefreshContainer.setRefreshing(false);
    }
}