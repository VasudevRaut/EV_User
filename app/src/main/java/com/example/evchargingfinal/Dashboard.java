package com.example.evchargingfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.evchargingfinal.databinding.ActivityDashboardBinding;
import com.example.evchargingfinal.databinding.ActivityMainBinding;
import com.example.evchargingfinal.envo_impact.EnviromentalImpactActivity;
import com.example.evchargingfinal.profile.ProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Dashboard extends AppCompatActivity {
    private Owner owner;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());

        init();

        setEventLis();

        setContentView(binding.getRoot());
    }

    private void goToRating() {
//        Intent intent = new Intent(Dashboard.this, GiveRatingActivity.class);
//        intent.putExtra("owner_id", owner.getOwner_id());
//        intent.putExtra("owner_email", owner.getOwner_email());
//        startActivity(intent);
//        finish();
    }

    private void getOwner() {
        //get email of owner from card
        firebaseFirestore
                .collection("Owner")
                .document("chetandagajipatil333@gmail.com")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot doc) {
                        if (doc == null) return;
                        owner = doc.toObject(Owner.class);

                        goToRating();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Dashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void setEventLis() {
        binding.btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, MainActivity.class));
            }

        });

//        binding.btnBookSlot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Dashboard.this, BookSlot.class));
//            }
//        });

        binding.btnEnvImpact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, EnviromentalImpactActivity.class));
            }
        });

        binding.btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, ProfileActivity.class));
            }
        });

        binding.btnViewStations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, ALLEVList.class));
            }
});



}

}