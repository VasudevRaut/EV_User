package com.example.evchargingfinal.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.evchargingfinal.R;
import com.example.evchargingfinal.User;
import com.example.evchargingfinal.auth.LoginActivity;
import com.example.evchargingfinal.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileActivity extends AppCompatActivity {

    private User user;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());

        init();

//

        getUserData();

        setContentView(binding.getRoot());
        Button logout = findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEventLis();
            }
        });

    }

    private void setEventLis(){
        firebaseAuth
                .signOut();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        finish();
    }

    private void setData(){
        binding.tvName.setText(user.getUser_name());
        binding.tvEmail.setText(user.getUser_email());
        binding.tvMobileNumber.setText(user.getUser_mobile_number());
        binding.tvPoints.setText(Integer.toString(user.getPoints()));
    }

    private void getUserData(){
        firebaseFirestore
                .collection("User")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot doc) {
                        user = doc.toObject(User.class);
                        setData();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void init(){

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
}