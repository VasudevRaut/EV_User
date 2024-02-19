package com.example.evchargingfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.evchargingfinal.databinding.ActivitySavingEnvironmentBinding;
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

public class savingEnvironment extends AppCompatActivity {

    RecyclerView recyclerView;
    //List<DataDishes> dataholder;
    List<User> data_list;
    private EnvTrackingAdapter envTrackingAdapter;
    LinearLayoutManager layoutManager;

    List<Owner> data;

    int points, energy, petrol_disel;

    List<User> users;
    private User user;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ActivitySavingEnvironmentBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_environment);

        recyclerView = findViewById(R.id.env_track_recycler);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        binding = ActivitySavingEnvironmentBinding.inflate(getLayoutInflater());

        init();

//        fetchFromDatabase();
//
        setEventLis();
//
        getUserData();
        getAllUsers();



        setContentView(binding.getRoot());

//        fetchFromDatabase();



    }


    public void fetchFromDatabase()
    {
        //fetch data here





        data_list = new ArrayList<>();
//        Toast.makeText(this, ""+users.size(), Toast.LENGTH_SHORT).show();

//        for(User use : users)
//        {
//                    data_list.add(0,new User());
//
//        }

//        data_list.addAll(users);
//        data_list.add(0,new Owner("Vasudevd","Vasudev","Vasudev","Vasudev",12.00,"Vasudev",5));



        //then add adapter class in it

    }

    private void sortUsers(){
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                // Compare points in descending order
                return Integer.compare(user2.getPoints(), user1.getPoints());
            }
        });
    }

    private void getAllUsers(){
        users = new ArrayList<>();
        data_list = new ArrayList<>();

        firebaseFirestore
                .collection("User")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snaps) {
                        if(snaps==null)return;;
                        users.addAll(snaps.toObjects(User.class));

                        for(int i = 0 ; i <users.size();i++)
                        {
                            data_list.add(0,new User("","","","",43,43));
                            envTrackingAdapter = new EnvTrackingAdapter(data_list, savingEnvironment.this);
                            recyclerView.setAdapter(envTrackingAdapter);
                        }


                        Toast.makeText(savingEnvironment.this, "------"+users.size(), Toast.LENGTH_SHORT).show();
                        sortUsers();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(savingEnvironment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void calculateParams(){
        energy = user.getEnergy_used();
        petrol_disel = energy*7; //can require this much fuel if not used ee
        points = energy*2;
    }

    private void getUserData() {
        firebaseFirestore
                .collection("User")
                .document("dagajipatil043@gmail.com")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot doc) {
                        user = doc.toObject(User.class);
                        calculateParams();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(savingEnvironment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setEventLis() {

    }

    private void init() {

        user = null;
        points = energy = petrol_disel = 0;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
}



}