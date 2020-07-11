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
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

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
                        fragment = new PostsFragment(fragmentManager);
                        menuItem.setIcon(R.drawable.ic_action_home);
                        break;
                    case R.id.action_compose:
                        fragment = new ComposeFragment();
                        menuItem.setIcon(R.drawable.ic_action_new_post);
                        break;
                    case R.id.action_profile:
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("currentUser", ParseUser.getCurrentUser());
                        fragment = new ProfileFragment(fragmentManager);
                        fragment.setArguments(bundle);
                        menuItem.setIcon(R.drawable.ic_action_profile);
                        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                        return true;
                        //break;
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