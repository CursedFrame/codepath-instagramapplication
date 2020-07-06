package com.example.powellparstagram.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.powellparstagram.R;

public class PostActivity extends AppCompatActivity {

    public static final String TAG = "PostActivity";

    private Button btnCamera;
    private Button btnSubmitPost;
    private EditText etDescription;
    private ImageView ivPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        btnCamera = findViewById(R.id.btnCamera);
        btnSubmitPost = findViewById(R.id.btnSubmitPost);
        etDescription = findViewById(R.id.etDescription);
        ivPicture = findViewById(R.id.ivPicture);

    }
}