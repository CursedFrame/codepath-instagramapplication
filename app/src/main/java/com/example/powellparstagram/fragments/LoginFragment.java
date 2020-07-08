package com.example.powellparstagram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.powellparstagram.R;
import com.example.powellparstagram.activities.LoginActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginFragment extends Fragment {

    public static final String TAG = "LoginFragment";

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ParseUser.getCurrentUser() != null) {
            ((LoginActivity) getActivity()).goMainActivity();
        }

        etUsername = view.findViewById(R.id.etUsernameRegister);
        etPassword = view.findViewById(R.id.etPasswordRegister);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnRegister = view.findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Login button clicked");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Log.i(TAG, "Login button clicked");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                registerUser(username, password);

                if (ParseUser.getCurrentUser() != null) {
                    ((LoginActivity) getActivity()).goMainActivity();
                }
            }
        });
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "User logged in");

        // If user has signed in, send user to MainActivity
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with login", e);
                    return;
                }
                ((LoginActivity)getActivity()).goMainActivity();
                Toast.makeText(getActivity(), "Successful login", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUser(String username, String password) {
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    ((LoginActivity)getActivity()).goMainActivity();
                }
                else {
                    Log.e(TAG, "Issue with register", e);
                }
            }
        });
    }
}