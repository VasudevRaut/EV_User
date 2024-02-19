package com.example.evchargingfinal.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.evchargingfinal.Dashboard;
import com.example.evchargingfinal.MainActivity;
import com.example.evchargingfinal.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    String email, pass;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        init();

        setEventLis();

        setContentView(binding.getRoot());
    }

    private void init() {

        email = pass = "-1";

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void getText() {
        email = binding.etEmail.getText().toString().trim();
        pass = binding.etPass.getText().toString().trim();

        if (email.equals("")) email = "-1";
        if (pass.equals("")) pass = "-1";
    }

    private int check() {
        if (email.equals("-1") || pass.equals("-1")) return 0;
        return 1;
    }

    private void setEventLis() {
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getText();

                if (check() == 1) {
                    firebaseAuth
                            .signInWithEmailAndPassword(email, pass)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, Dashboard.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Mandatory", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

}