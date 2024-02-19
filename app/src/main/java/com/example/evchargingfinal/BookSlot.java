package com.example.evchargingfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.evchargingfinal.auth.RegisterActivity;
import com.example.evchargingfinal.databinding.ActivityBookSlotBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.intellij.lang.annotations.JdkConstants;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookSlot extends AppCompatActivity implements PaymentResultListener {

    RecyclerView recyclerView;


    private List<TimeSlot> itemList;
    private AdapterView.OnItemClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;


    //List<DataDishes> dataholder;
    List<TimeSlot> data_list;
    private AddSlotAdapter dishAdapter;
    LinearLayoutManager layoutManager;



    private ActivityBookSlotBinding binding;
    String  selectedItem="";

    List<EVStation> evStations = null;
    List<String> timeSlots;
    List<String> ListSubStation,Listsub;
    EVStation station;
    List<Integer> slot = null;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String sharedPreferencesFileTitle = "EV";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_slot);


        binding = ActivityBookSlotBinding.inflate(getLayoutInflater());

        init();



        setContentView(binding.getRoot());

        getOwnerEVStations("chetandagajipatil333@gmail.com");





        Button button = findViewById(R.id.bookButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(sharedPreferencesFileTitle, MODE_PRIVATE);
                String user_id;
                user_id = sharedPreferences.getString("status","");

                if(!selectedItem.equals("") && !user_id.equals("-1"))
                {

                    //send update request
                    List<Integer> slot = new ArrayList<>(Arrays.asList(new Integer[48]));
                    for(int i = 0; i< 48;i++)
                    {
                        if(!user_id.equals(i+""))
                        slot.set(i,0);
                    }
                    Map<String, Object> data = new HashMap<>();
                    slot.set(Integer.parseInt(user_id),1);
                    data.put("slot",slot);
//                    Toast.makeText(BookSlot.this, ""+selectedItem, Toast.LENGTH_SHORT).show();

                    firebaseFirestore
                            .collection("Owner")
                            .document("chetandagajipatil333@gmail.com")
                            .collection("EV_Station")
                            .document(selectedItem)
                            .update(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
//                                    Toast.makeText(BookSlot.this, "Onsuccess", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(BookSlot.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                    startPayment();
//                    storePaymentData("dagajipatil043@gmail.com","Test","12222","7387579912",200,"Vasudv",20);
                }







            }
        });

//        String[] timeSlots = {"Slot 1", "Slot 2", "Slot 3", "Slot 4"}; // Replace with your actual time slots




        recyclerView = findViewById(R.id.allslots);
        layoutManager = new GridLayoutManager(this,3);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        data_list = new ArrayList<>();



        SharedPreferences sharedPreferences = getSharedPreferences(sharedPreferencesFileTitle, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("status", "-1");
        editor.apply();






















//        List<String> timeSlotss = new ArrayList<>();


    }



    private void getValues(String evs_id) {
        for (EVStation evStation : evStations) {
            if (!evStation.getEvs_id().equals(evs_id)) continue;
            station = evStation;
            break;
        }
    }

    private void bookSlot(int id) { //to manipulate slots
        slot.set(id, 1 - slot.get(id));
    }

    private void getOwnerEVStations(String owner_id) {
        firebaseFirestore
                .collection("Owner")
                .document(owner_id)
                .collection("EV_Station")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snaps) {
                        if (snaps == null) return;
                        evStations.addAll(snaps.toObjects(EVStation.class));

                        for(int i = 0 ; i <evStations.size();i++)
                        {
                            ListSubStation.add(evStations.get(i).evs_id);
                            Listsub.add("Charging Point "+(i+1));
                        }


//                        Toast.makeText(BookSlot.this, ""+evStations.size(), Toast.LENGTH_SHORT).show();
                        Spinner spinner = findViewById(R.id.spinnerTimeSlot);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(BookSlot.this, android.R.layout.simple_spinner_item, Listsub);

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);


                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                // This method will be invoked when an item in the spinner is selected
//                                 selectedItem = (String) parentView.getItemAtPosition(position);
                                 selectedItem = ListSubStation.get(position);


                                setEventLis(owner_id,selectedItem);




                                // Do something with the selected item
//                                Toast.makeText(getApplicationContext(), "Selected item: " + selectedItem, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                                // Do nothing if nothing is selected
                            }
                        });



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BookSlot.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setEventLis(String owner_id,String selectedItem) {

        firebaseFirestore
                .collection("Owner")
                .document(owner_id)
                .collection("EV_Station")
                .document(selectedItem)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        station = documentSnapshot.toObject(EVStation.class);
//                        Toast.makeText(BookSlot.this, ""+station.getSlot(), Toast.LENGTH_SHORT).show();

                        String charSN = "1";
                        String ownerMail = "Vasudev";
                        data_list.clear();
                        for (int i = 0; i < 48; i++) {
                            int hours = i / 2;
                            int minutes = (i % 2) * 30;
                            String startTime = String.format("%02d:%02d", hours, minutes);
                            String endTime = String.format("%02d:%02d", (hours + (i % 2 == 0 ? 0 : 1)) % 24, (minutes + 30) % 60);

                            data_list.add(new TimeSlot(charSN,startTime + " : " + endTime,ownerMail,""+station.getSlot().get(i)));
//            timeSlotss.add(startTime + " : " + endTime);
                        }
                        data_list.add(new TimeSlot(charSN,"startTime" + " : " + "endTime",ownerMail,""+station.getSlot().get(7)));



//



                        dishAdapter = new AddSlotAdapter(data_list, BookSlot.this);
                        recyclerView.setAdapter(dishAdapter);




                    }
                });

    }

    private void timeSlots() { // to set values of grid layout
        timeSlots = new ArrayList<>();
        for (int i = 0; i < 48; i++) {
            int hours = i / 2;
            int minutes = (i % 2) * 30;
            String startTime = String.format("%02d:%02d", hours, minutes);
            String endTime = String.format("%02d:%02d", (hours + (i % 2 == 0 ? 0 : 1)) % 24, (minutes + 30) % 60);
            timeSlots.add(startTime + " : " + endTime);
        }
    }

    private void init() {
        timeSlots();

        evStations = new ArrayList<>();
        ListSubStation = new ArrayList<>();
        Listsub = new ArrayList<>();
        slot = new ArrayList<>(Arrays.asList(new Integer[48]));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }


    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Successful : "+s, Toast.LENGTH_SHORT).show();



            storePaymentData("dagajipatil043@gmail.com","Test",s,"7387579912",200,"",20);


    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Error : "+s , Toast.LENGTH_SHORT).show();




    }
    private void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_9gCpkhJrHxTWoT");

        try {
            JSONObject options = new JSONObject();
            options.put("name", "DigiGrow");
            options.put("description", "Payment for XYZ");
            options.put("currency", "INR");
            options.put("amount", ""+(200*100)); // Amount in paise
            options.put("prefill.email", "vasudevraut156@gmail.com");
            options.put("prefill.contact", "7387579912");

            checkout.open(BookSlot.this, options);

        } catch (Exception e) {
//            showToast("Error: " + e.getMessage());
        }
    }



    public void storePaymentData(String owner_email, String owner_name, String transaction_id, String customer_phone, int amount_paid,String fromname,int esold) {
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Create a reference to the owner's document
        DocumentReference ownerRef = firebaseFirestore.collection("Owner").document("chetandagajipatil333@gmail.com");


        //payment_id, payment_from_user_id, payment_from_name, payment_to_owner_id, payment_to_name;
        //    int payment_amount, payment_energy_sold;

        // Create a map to store the payment data
        Map<String, Object> paymentData = new HashMap<>();
        paymentData.put("payment_id",transaction_id);
        paymentData.put("payment_from_user_id",customer_phone);
        paymentData.put("payment_from_name",fromname);
        paymentData.put("payment_to_owner_id",owner_email);
        paymentData.put("payment_to_name",owner_name);
        paymentData.put("payment_amount",amount_paid);
        paymentData.put("payment_energy_sold",esold);


//        paymentData.put("customer_phone", customer_phone);
//        paymentData.put("amount_paid", amount_paid);

        // Add the payment data to Firestore
        ownerRef.collection("Payments").document(transaction_id)
                .set(paymentData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data added successfully
                        // You can add further actions here if needed
                        Toast.makeText(BookSlot.this, "Data added succefully", Toast.LENGTH_SHORT).show();
                        Log.d("FirestoreHelper", "Payment data added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add data
                        Toast.makeText(BookSlot.this, "Fail to add", Toast.LENGTH_SHORT).show();
                        Log.e("FirestoreHelper", "Error adding payment data: " + e.getMessage());
                    }
                });

        // Update the owner name in the owner document
        ownerRef.update("owner_name", owner_name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Owner name updated successfully
                        // You can add further actions here if needed
                        Log.d("FirestoreHelper", "Owner name updated successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update owner name
                        Log.e("FirestoreHelper", "Error updating owner name: " + e.getMessage());
                    }
                });
    }
}