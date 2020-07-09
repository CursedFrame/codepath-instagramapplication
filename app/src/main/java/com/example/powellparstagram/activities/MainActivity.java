package com.example.powellparstagram.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.powellparstagram.R;
import com.example.powellparstagram.fragments.ComposeFragment;
import com.example.powellparstagram.fragments.PostsFragment;
import com.example.powellparstagram.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final int POST_LIMIT = 20;

    private BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Menu menu = bottomNavigationView.getMenu();
                menu.findItem(R.id.action_home).setIcon(R.drawable.ic_action_home_outline);
                menu.findItem(R.id.action_compose).setIcon(R.drawable.ic_action_new_post_outline);
                menu.findItem(R.id.action_profile).setIcon(R.drawable.ic_action_profile_outline);

                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        fragment = new PostsFragment(fragmentManager, POST_LIMIT);
                        menuItem.setIcon(R.drawable.ic_action_home);
                        break;
                    case R.id.action_compose:
                        fragment = new ComposeFragment();
                        menuItem.setIcon(R.drawable.ic_action_new_post);
                        break;
                    case R.id.action_profile:
                        fragment = new ProfileFragment(fragmentManager, POST_LIMIT);
                        menuItem.setIcon(R.drawable.ic_action_profile);
                        break;
                    default:
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }
}