package com.example.evchargingfinal.envo_impact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evchargingfinal.EnvTrackingAdapter;
import com.example.evchargingfinal.R;
import com.example.evchargingfinal.User;
import com.example.evchargingfinal.databinding.ActivityEnviromentalImpactBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EnviromentalImpactActivity extends AppCompatActivity {

    private int points, energy, petrol_disel;
    private List<User> users;
    private User user;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ActivityEnviromentalImpactBinding binding;
    private EnvTrackingAdapter dishAdapter;
    private LinearLayoutManager layoutManager;
    private List<User> ratings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEnviromentalImpactBinding.inflate(getLayoutInflater());

        init();

        setEventLis();

        getUserData();

        setContentView(binding.getRoot());
    }


    private void sortUsers() {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                // Compare points in descending order
                return Integer.compare(user2.getPoints(), user1.getPoints());
            }
        });
    }

    private void getAllUsers() {
        firebaseFirestore
                .collection("User")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snaps) {
                        users.addAll(snaps.toObjects(User.class));
                        sortUsers();

                        dishAdapter = new EnvTrackingAdapter(users, EnviromentalImpactActivity.this);
                        binding.rvData.setAdapter(dishAdapter);
//                        evStations.addAll(snaps.getDocuments());
//                        addLearderboard();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EnviromentalImpactActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setUserData(){
        binding.tvEnergy.setText(Integer.toString(energy));
        binding.tvPetrolDisel.setText(Integer.toString(petrol_disel));
        binding.tvPoints.setText(Integer.toString(points));
    }

    private void getUserData() {
        Log.d("TAG", "getUserData: " + firebaseAuth.getCurrentUser().toString());
        firebaseFirestore
                .collection("User")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot doc) {
                        if (doc==null) return;

                        user = doc.toObject(User.class);

                        Log.d("TAG", "onSuccess: " + user.getUser_id());

                        energy = user.getEnergy_used();
                        petrol_disel = energy * 7; //can require this much fuel if not used ee
                        points = energy * 2;

                        setUserData();
                        getAllUsers();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EnviromentalImpactActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setEventLis() {

    }

    private void init() {
        users = new ArrayList<>();

        layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rvData.setLayoutManager(layoutManager);
//        ratings = new ArrayList<>();

        points = energy = petrol_disel = 0;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
}