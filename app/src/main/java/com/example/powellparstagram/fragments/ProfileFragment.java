package com.example.powellparstagram.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.powellparstagram.R;
import com.example.powellparstagram.activities.LoginActivity;
import com.example.powellparstagram.adapters.ProfilePostsAdapter;
import com.example.powellparstagram.objects.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    private int POST_LIMIT;

    private FragmentManager fragmentManager;
    private RecyclerView rvPosts;
    private ImageView ivSettings;
    private ImageView ivProfilePicture;
    private TextView tvProfileName;
    private SwipeRefreshLayout swipeRefreshContainerProfile;
    protected ProfilePostsAdapter profilePostsAdapter;
    protected List<Post> allPosts;
    private ParseUser currentUser;

    public ProfileFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public ProfileFragment(FragmentManager fragmentManager, int postLimit) {
        this.fragmentManager = fragmentManager;
        this.POST_LIMIT = postLimit;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        currentUser = bundle.getParcelable("currentUser");

        tvProfileName = view.findViewById(R.id.tvProfileName);
        tvProfileName.setText(currentUser.getUsername());
        ivSettings = view.findViewById(R.id.ivSettings);

        // Iff the profile is the user's profile, show the settings icon
        if (currentUser == ParseUser.getCurrentUser()) {
            ivSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu settingsMenu = new PopupMenu(getContext(), ivSettings);
                    settingsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_change_profile_picture:
                                    goProfilePictureDialogFragment();
                                    return true;
                                case R.id.action_log_out:
                                    ParseUser.logOut();
                                    ParseUser currentUser = ParseUser.getCurrentUser();
                                    goLoginActivity();
                                    return true;
                            }
                            return false;
                        }
                    });
                    settingsMenu.inflate(R.menu.menu_settings);
                    settingsMenu.show();
                }
            });
        }
        else {
            ivSettings.setVisibility(View.GONE);
        }

        ivProfilePicture = view.findViewById(R.id.ivProfileImage);

        // Bind profile image
        ParseFile image = currentUser.getParseFile("profileImage");
        Glide.with(getContext())
                .load(image.getUrl())
                .transform(new CircleCrop())
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(ivProfilePicture);

        swipeRefreshContainerProfile = view.findViewById(R.id.scProfile);
        swipeRefreshContainerProfile.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    refreshProfile();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        // RecyclerView and Adapter
        rvPosts = view.findViewById(R.id.rvProfile);
        allPosts = new ArrayList<>();
        profilePostsAdapter = new ProfilePostsAdapter(getContext(), allPosts, fragmentManager);
        rvPosts.setAdapter(profilePostsAdapter);
        rvPosts.setLayoutManager(new GridLayoutManager(getContext(), 3));

        queryPosts();
    }

    protected void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, currentUser);
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
                profilePostsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void goLoginActivity(){
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void goProfilePictureDialogFragment() {
       // FragmentManager fm = getSupportFragmentManager();
        ProfilePictureDialogFragment profilePictureDialogFragment = ProfilePictureDialogFragment.newInstance("Some Title");
        profilePictureDialogFragment.show(fragmentManager, "fragment_edit_name");
    }

    private void refreshProfile() throws ParseException {
        // Refresh user data
        currentUser.fetch();
        ParseFile image = currentUser.getParseFile("profileImage");
        Glide.with(getContext())
                .load(image.getUrl())
                .transform(new CircleCrop())
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(ivProfilePicture);
        swipeRefreshContainerProfile.setRefreshing(false);
    }
}