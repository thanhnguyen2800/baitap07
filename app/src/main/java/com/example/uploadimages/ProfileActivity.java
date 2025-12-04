package com.example.uploadimages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class ProfileActivity extends AppCompatActivity {

    ImageView imgProfile;
    TextView tvUsername, tvFullName, tvEmail, tvGender;
    Button btnLogout;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Initialize views
        imgProfile = findViewById(R.id.imgProfile);
        tvUsername = findViewById(R.id.tvUsername);
        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvGender = findViewById(R.id.tvGender);
        btnLogout = findViewById(R.id.btnLogout);

        // Set click listeners
        imgProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> logout());

        // Handle insets for edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load user info every time the activity is resumed
        loadUserInfo();
    }

    private void loadUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences(RegisterActivity.USER_PREFS, MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "N/A");
        String fullName = sharedPreferences.getString("fullName", "N/A");
        String email = sharedPreferences.getString("email", "N/A");
        String gender = sharedPreferences.getString("gender", "N/A");
        userId = sharedPreferences.getString("id", "3");
        String avatarUriString = sharedPreferences.getString("avatar_uri", null);

        tvUsername.setText(username);
        tvFullName.setText(fullName);
        tvEmail.setText(email);
        tvGender.setText(gender);

        if (avatarUriString != null) {
            Glide.with(this).load(Uri.parse(avatarUriString)).into(imgProfile);
        } else {
            // Load a default image if no avatar is saved
            imgProfile.setImageResource(R.mipmap.ic_launcher_round);
        }
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences(RegisterActivity.USER_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
