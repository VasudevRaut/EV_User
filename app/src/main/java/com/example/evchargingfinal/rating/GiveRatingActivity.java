package com.example.evchargingfinal.rating;

//Add to rating
//Maintain Avg rating

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.evchargingfinal.Owner;
import com.example.evchargingfinal.Rating;
import com.example.evchargingfinal.User;
import com.example.evchargingfinal.databinding.ActivityGiveRatingBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GiveRatingActivity extends AppCompatActivity {

    private ActivityGiveRatingBinding binding;
    private String rat_id, owner_id, owner_email, user_id, rat_name, rat_date, rat_title, rat_desc;
    private double rat_rating;

    //for rating maintenance
    double avg_rating;
    int reviews;
    private User user;
    private Owner owner;
    Map<String, Object> data = new HashMap<>();
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGiveRatingBinding.inflate(getLayoutInflater());

        init();

        getUserData();

        getOwnerData();

        setEventLis();

        setContentView(binding.getRoot());
    }

    private void updateOwnerRating() {
        reviews = reviews + 1;

        avg_rating = (avg_rating + rat_rating) / reviews;

        data.put("reviews", reviews);
        data.put("avg_rating", avg_rating);

        firebaseFirestore
                .collection("Owner")
                .document(owner_email)
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(GiveRatingActivity.this, "Owner Data Updated", Toast.LENGTH_SHORT).show();
                        setText();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GiveRatingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addReview() {
        firebaseFirestore
                .collection("Owner")
                .document(owner_email)
                .collection("Rating")
                .document(rat_id)
                .set(new Rating(rat_id, owner_id, owner_email, user_id, rat_name, rat_date, rat_title, rat_desc, rat_rating))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(GiveRatingActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        updateOwnerRating();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GiveRatingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void setEventLis() {
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getText();

                if (check() == 1) {
                    addReview();
                    finish();
                } else {
                    Toast.makeText(GiveRatingActivity.this, "All Fields are Mandatory", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getOwnerData() {
        firebaseFirestore
                .collection("Owner")
                .document(owner_email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot doc) {
                        if (doc == null) return;
                        owner = doc.toObject(Owner.class);

                        avg_rating = owner.getAvg_rating();
                        reviews = owner.getReviews();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GiveRatingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getUserData() {
        firebaseFirestore
                .collection("User")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot doc) {
                        if (doc == null) return;
                        user = doc.toObject(User.class);

                        binding.tvName.setText(user.getUser_name());
                        binding.tvDate.setText(rat_date);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GiveRatingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private int check() {
        if (rat_title.equals("") || rat_desc.equals("")) return 0;
        return 1;
    }

    private void getText() {
        rat_id = UUID.randomUUID().toString();
        //owner_id & owner_email will get from intent
        user_id = user.getUser_id();
        rat_name = user.getUser_name();
        //rat_date id fetched in init
        rat_title = binding.etTitle.getText().toString().trim();
        rat_desc = binding.etDesc.getText().toString().trim();
        rat_rating = binding.ratBarRating.getRating();
    }

    private void setText() {
        binding.ratBarRating.setRating(0);
        binding.etTitle.setText("");
        binding.etDesc.setText("");

        rat_title = rat_desc = "-1";
        rat_rating = -1;
    }

    private void init() {
        rat_id = owner_id = owner_email = user_id = rat_name = rat_date = rat_title = rat_desc = "-1";
        rat_rating = -1;

        LocalDate today = LocalDate.now();

        rat_date = today.toString();

        owner_id = getIntent().getExtras().getString("owner_id");
        owner_email = getIntent().getExtras().getString("owner_email");


        Log.d("TAG", owner_id);
        Log.d("TAG", owner_email);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
}