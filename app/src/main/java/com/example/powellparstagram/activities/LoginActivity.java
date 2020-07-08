package com.example.powellparstagram.activities;

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
//    private ConstraintLayout clContainer;
//    private EditText etUsername;
//    private EditText etPassword;
//    private Button btnLogin;
//    private Button btnRegister;
    private ImageView ivBackgroundLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Fragment fragment = new LoginFragment();

        fragmentManager.beginTransaction().replace(R.id.clContainer, fragment).commit();

//        clContainer = findViewById(R.id.clContainer);
//
//        etUsername = findViewById(R.id.etUsername);
//        etPassword = findViewById(R.id.etPassword);
//        btnLogin = findViewById(R.id.btnLogin);
//        btnRegister = findViewById(R.id.btnRegister);
        ivBackgroundLogin = findViewById(R.id.ivBackgroundLogin);
//
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(TAG, "Login button clicked");
//                String username = etUsername.getText().toString();
//                String password = etPassword.getText().toString();
//                loginUser(username, password);
//            }
//        });
//
//        btnRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(TAG, "Register button clicked");
//                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivity(intent);
//            }
//        });
//
        Glide.with(this)
                .asGif()
                .load(R.drawable.background)
                .into(ivBackgroundLogin);
//
//        if (ParseUser.getCurrentUser() != null){
//            goMainActivity();
//        }
    }

//    private void loginUser(String username, String password){
//        Log.i(TAG, "User logged in");
//
//        // If user has signed in, send user to MainActivity
//        ParseUser.logInInBackground(username, password, new LogInCallback() {
//            @Override
//            public void done(ParseUser user, ParseException e) {
//                if (e != null){
//                    Log.e(TAG, "Issue with login", e);
//                    return;
//                }
//                goMainActivity();
//                Toast.makeText(LoginActivity.this, "Successful login", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void goMainActivity() {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        finish();
//    }
}