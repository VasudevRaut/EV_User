package com.example.evchargingfinal.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.evchargingfinal.User;
import com.example.evchargingfinal.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    private String user_id, user_name, user_email, user_mobile_number, user_pass;
    private int energy_used, points;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate((getLayoutInflater()));

        init();

        setEventLis();

        setContentView(binding.getRoot());
    }

    private int check() {
        if (user_id.equals("-1") || user_email.equals("-1") || user_mobile_number.equals("-1") || user_pass.equals("-1")) {
            return 0;
        }

        return 1;
    }

    private void getText() {
        user_id = UUID.randomUUID().toString();
        user_email = binding.etEmail.getText().toString().trim();
        user_name = binding.etName.getText().toString().trim();
        user_mobile_number = binding.etMobileNumber.getText().toString().trim();
        user_pass = binding.etPass.getText().toString().trim();
        energy_used = points = 0;
    }

    private void init() {
        user_id = user_email = user_name = user_mobile_number = user_pass = "-1";
        energy_used = points = 0;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void createNew() {
        firebaseAuth
                .createUserWithEmailAndPassword(user_email, user_pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(RegisterActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                        firebaseFirestore
                                .collection("User")
                                .document(user_email)
                                .set(new User(user_id, user_name, user_email , user_mobile_number, energy_used, points))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(RegisterActivity.this, "Data Entered", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setEventLis() {
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getText();

                if (check() == 1) {
                    createNew();
                } else {
                    Toast.makeText(RegisterActivity.this, "All Fields are Mandatory", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}