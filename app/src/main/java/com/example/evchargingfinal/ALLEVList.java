package com.example.evchargingfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evchargingfinal.databinding.ActivityMainBinding;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ALLEVList extends AppCompatActivity {

    RecyclerView recyclerView;
    //List<DataDishes> dataholder;
    List<Owner> data_list;
    private StationAdapter dishAdapter;
    LinearLayoutManager layoutManager;

    private SearchView searchView;
    List<Owner> data;


    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressDialog;



    double longi;
    double lati;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    ProgressBar progressBar;
    ResultReceiver resultReceiver;
    Owner owner;

    LatLng userlocation,dlocation;
    private MarkerOptions place1, place2;
    Button getDirection;
    private Polyline currentPolyline;
    List<EVStation> evStations = new ArrayList<>();
    Map<String,Owner> mp = new HashMap<>();
    Map<String,EVStation> mpevstation = new HashMap<>();
    private static final double EARTH_RADIUS = 6371000; // meters



    //    private ActivityProfileBinding binding;
    public ActivityMainBinding binding;
    //
//    Owner owner;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allevlist);

        resultReceiver = new ALLEVList.AddressResultReceiver(new Handler());
        init();
        TextView speed,rating ,price,nearby;

        speed = findViewById(R.id.speed);
        rating = findViewById(R.id.rating);
        price = findViewById(R.id.price);
        nearby = findViewById(R.id.nearby);


        speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speed.setBackgroundResource(R.drawable.gradient);
                rating.setBackgroundResource(R.drawable.edittext_background);
                price.setBackgroundResource(R.drawable.edittext_background);
                nearby.setBackgroundResource(R.drawable.edittext_background);



            }
        });

        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speed.setBackgroundResource(R.drawable.edittext_background);
                rating.setBackgroundResource(R.drawable.gradient);
                price.setBackgroundResource(R.drawable.edittext_background);
                nearby.setBackgroundResource(R.drawable.edittext_background);


                filterListOnRating();
            }
        });
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speed.setBackgroundResource(R.drawable.edittext_background);
                rating.setBackgroundResource(R.drawable.edittext_background);
                price.setBackgroundResource(R.drawable.gradient);
                nearby.setBackgroundResource(R.drawable.edittext_background);
                filterListOnPrice();
            }
        });

        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speed.setBackgroundResource(R.drawable.edittext_background);
                rating.setBackgroundResource(R.drawable.edittext_background);
                price.setBackgroundResource(R.drawable.edittext_background);
                nearby.setBackgroundResource(R.drawable.gradient);
                filterListOnnearBy();
            }
        });











        Button b = findViewById(R.id.ss);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });



















        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(swipeRefreshLayout.getContext(), ALLEVList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                filterList(s);
                return true;
            }
        });


        getCurrentLocation1();
        recyclerView = findViewById(R.id.recycler_for_dishes);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        setStation();




    }

    public void showBottomSheetDialog()
    {

        final BottomSheetDialog bottomSheetDialog1 = new BottomSheetDialog(
                ALLEVList.this,R.style.BottomSheetDialogTheme
        );


        View bottomSheetView = LayoutInflater.from(ALLEVList.this)
                .inflate(
                        R.layout.layout_bottom_sheet,
                        (LinearLayout)findViewById(R.id.bottomsheetcontainer)
                );




        bottomSheetDialog1.setContentView(bottomSheetView);
        bottomSheetDialog1.show();
    }



    public void setStation()
    {














        data_list = new ArrayList<>();







            dishAdapter = new StationAdapter(data_list, ALLEVList.this);
            recyclerView.setAdapter(dishAdapter);
//            progressDialog.dismiss();

        }


    private void filterList(String s) {

        List<Owner> filteredList = new ArrayList<>();

        for (Owner dataDishes : data_list)
        {
            if(dataDishes.getEv_station_name().toString().toLowerCase().contains(s.toString().toLowerCase())){
                filteredList.add(dataDishes);
            }
        }
        if (filteredList.isEmpty()){
            dishAdapter.setFilteredList(filteredList);

            //Toast.makeText(new AdminHome(), "No dish found", Toast.LENGTH_SHORT).show();
        }

        else {

            Log.println(Log.DEBUG,"debug", "Send..............."+filteredList);
            dishAdapter.setFilteredList(filteredList);

        }
    }


    private void filterListOnRating() {

        List<Owner> filteredList = new ArrayList<>();

        Collections.sort(data_list, new Comparator<Owner>() {
            @Override
            public int compare(Owner data1, Owner data2) {
                // Sort in descending order based on rating
                return Integer.compare((int)data2.getAvg_rating(), (int)data1.getAvg_rating());
            }
        });
        filteredList = data_list;
        if (filteredList.isEmpty()){
            dishAdapter.setFilteredList(filteredList);

            //Toast.makeText(new AdminHome(), "No dish found", Toast.LENGTH_SHORT).show();
        }

        else {

            Log.println(Log.DEBUG,"debug", "Send..............."+filteredList);
            dishAdapter.setFilteredList(filteredList);

        }
    }
    private void filterListOnPrice() {

        List<Owner> filteredList = new ArrayList<>();

        Collections.sort(data_list, new Comparator<Owner>() {
            @Override
            public int compare(Owner data1, Owner data2) {
                // Sort in descending order based on rating
                return Integer.compare((int)data1.getPrice(), (int)data2.getPrice());
            }
        });
        filteredList = data_list;
        if (filteredList.isEmpty()){
            dishAdapter.setFilteredList(filteredList);

            //Toast.makeText(new AdminHome(), "No dish found", Toast.LENGTH_SHORT).show();
        }

        else {

            Log.println(Log.DEBUG,"debug", "Send..............."+filteredList);
            dishAdapter.setFilteredList(filteredList);

        }
    }

    private void filterListOnnearBy() {

        List<Owner> filteredList = new ArrayList<>();

        Collections.sort(data_list, new Comparator<Owner>() {
            @Override
            public int compare(Owner data1, Owner data2) {
                // Sort in descending order based on rating
                return Integer.compare((int)data1.getPrice(), (int)data2.getPrice());
            }
        });
        filteredList = data_list;
        if (filteredList.isEmpty()){
            dishAdapter.setFilteredList(filteredList);

            //Toast.makeText(new AdminHome(), "No dish found", Toast.LENGTH_SHORT).show();
        }

        else {

            Log.println(Log.DEBUG,"debug", "Send..............."+filteredList);
            dishAdapter.setFilteredList(filteredList);

        }
    }








    public void getCurrentLocation1()
    {


        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ALLEVList.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
//            Toast.makeText(this, "Vasudev", Toast.LENGTH_SHORT).show();
            getCurrentLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission is denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
//        progressBar.setVisibility(View.VISIBLE);
//        Toast.makeText(this, "getcurrentLocation", Toast.LENGTH_SHORT).show();

        LocationRequest locationRequest = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            locationRequest = new LocationRequest();
        }
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }


        LocationServices.getFusedLocationProviderClient(ALLEVList.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(getApplicationContext())
                                .removeLocationUpdates(this);
//                        Toast.makeText(MainActivity.this, "when set locatin", Toast.LENGTH_SHORT).show();
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestlocIndex = locationResult.getLocations().size() - 1;
                            lati = locationResult.getLocations().get(latestlocIndex).getLatitude();
                            longi = locationResult.getLocations().get(latestlocIndex).getLongitude();
                            LatLng latLng2 = new LatLng(lati,longi);



                            setStation1();

                        } else {
//                            progressBar.setVisibility(View.GONE);

                        }
                    }
                }, Looper.getMainLooper());

    }

    private class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            progressBar.setVisibility(View.GONE);
            if (resultCode == Constants.SUCCESS_RESULT) {

            } else {
//                Toast.makeText(MainActivity.this, resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }

        }


    }













    public void setStation1()
    {














//        data_list = new ArrayList<>();
        data_list = new ArrayList<>();
        firebaseFirestore.collection("Owner")
            .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                Owner owner = doc.toObject(Owner.class);
                LatLng latLng2 = new LatLng(owner.getOwner_location().getLatitude(), owner.getOwner_location().getLongitude());





                data_list.add(new Owner(owner.getOwner_id(), owner.owner_email, owner.owner_name, owner.ev_station_name, owner.avg_rating, owner.getOwner_location(), owner.charging_points,owner.price,owner.charging_point_com_type_1, owner.charging_point_com_type_2, owner.charging_point_com_type_3, owner.reviews,owner.lat,owner.lang));
//



                dishAdapter = new StationAdapter(data_list, ALLEVList.this);
                recyclerView.setAdapter(dishAdapter);
                TextView label  = findViewById(R.id.label);

                double red = calculateDistance(lati,longi,latLng2.latitude,latLng2.longitude);


            }
        }
    })
            .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });








}

    private void init(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Convert latitude and longitude from degrees to radians
        double lat1Radians = Math.toRadians(lat1);
        double lon1Radians = Math.toRadians(lon1);
        double lat2Radians = Math.toRadians(lat2);
        double lon2Radians = Math.toRadians(lon2);

        // Compute the differences between the latitudes and longitudes
        double deltaLat = lat2Radians - lat1Radians;
        double deltaLon = lon2Radians - lon1Radians;

        // Compute the distance using Haversine formula
        double a = Math.pow(Math.sin(deltaLat / 2), 2) +
                Math.cos(lat1Radians) * Math.cos(lat2Radians) *
                        Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;

        return distance;
    }




}