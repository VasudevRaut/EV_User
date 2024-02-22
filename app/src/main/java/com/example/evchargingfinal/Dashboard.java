package com.example.evchargingfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.evchargingfinal.databinding.ActivityDashboardBinding;
import com.example.evchargingfinal.databinding.ActivityMainBinding;
import com.example.evchargingfinal.envo_impact.EnviromentalImpactActivity;
import com.example.evchargingfinal.profile.ProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class Dashboard extends AppCompatActivity {
    private Owner owner;

    private Notification notification;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private int check = 1;

    private ActivityDashboardBinding binding;
    EditText token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
//        token = findViewById(R.id.token);

        LinearLayout lat = findViewById(R.id.chatbot);
//        lat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(Dashboard.this, "CAllll", Toast.LENGTH_SHORT).show();
//
//
//            }
//        });


//        lat.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Toast.makeText(Dashboard.this, "Done", Toast.LENGTH_SHORT).show();
//                return false;
//
//            }
//        });


//        getToken("");

        checkPre();
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
        binding.Historys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, GetAllHistoryActivity.class));

            }
        });


}

//    private void getToken(String emp_contact) {
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful() && task.getResult() != null) {
//                        String tokens = task.getResult();
//                        Toast.makeText(this, ""+tokens, Toast.LENGTH_SHORT).show();
//                        Log.d("Vasudev",tokens);
//                        token.setText(tokens);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//                        // Save or send the token as needed
//                        //textView.setText(token);
////                        Log.d("FCM Token", token);
//                        //Toast.makeText(this, ""+token, Toast.LENGTH_SHORT).show();
//                    } else {
//                        // Handle token retrieval error
//                    }
//                });
//    }
//




    private void checkPre() {
        firebaseFirestore
                .collection("Notification")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot doc) {
                        if (!doc.exists()) {
                            check = 0;
                        }

                        getData();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Dashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void getData() {
        if (check == 1) { //already there
            firebaseFirestore
                    .collection("Notification")
                    .document(firebaseAuth.getCurrentUser().getEmail())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot doc) {
                            notification = doc.toObject(Notification.class);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Dashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else { //new
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            String tokens = task.getResult();

                            notification = new Notification();

                            notification.setNoti_email(firebaseAuth.getCurrentUser().getEmail());
                            notification.setNoti_token(tokens);
//                            Toast.makeText(this, "" + tokens, Toast.LENGTH_SHORT).show();

                            firebaseFirestore
                                    .collection("Notification")
                                    .document(notification.getNoti_email())
                                    .set(notification)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
//                                            Toast.makeText(Dashboard.this, "Notification Values Added", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Dashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else {
                            // Handle token retrieval error
                        }
                    });

        }
    }


    public void ChatBot(View view) {

        Intent intent = new Intent(Dashboard.this,ChatBot.class);
        startActivity(intent);

//        Toast.makeText(this, "calll herer", Toast.LENGTH_SHORT).show();

    }
}