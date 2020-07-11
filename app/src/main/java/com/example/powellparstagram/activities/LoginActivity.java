package com.example.powellparstagram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.powellparstagram.R;
import com.example.powellparstagram.fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    final FragmentManager fragmentManager = getSupportFragmentManager();

    private ImageView ivBackgroundLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ivBackgroundLogin = findViewById(R.id.ivBackgroundLogin);

        Fragment fragment = new LoginFragment();
        fragmentManager.beginTransaction().replace(R.id.clContainer, fragment).commit();

        Glide.with(this)
                .asGif()
                .load(R.drawable.background)
                .into(ivBackgroundLogin);
    }

    public void goMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}