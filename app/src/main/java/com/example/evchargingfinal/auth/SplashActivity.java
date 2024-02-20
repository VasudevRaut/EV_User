package com.example.evchargingfinal.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.evchargingfinal.Dashboard;
import com.example.evchargingfinal.MainActivity;
import com.example.evchargingfinal.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        init();

        checkLoginStatus();
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void checkLoginStatus() {
//        Toast.makeText(this, ""+firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(SplashActivity.this, Dashboard.class));
                    finish();
                } else {

                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();

                }
            }
        }, 1000);
    }
}