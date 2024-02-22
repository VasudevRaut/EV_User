package com.example.evchargingfinal;



import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evchargingfinal.databinding.ActivityMainBinding;
import com.google.android.gms.internal.maps.zzad;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
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
        setContentView(R.layout.activity_main);


        resultReceiver = new AddressResultReceiver(new Handler());


                getCurrentLocation1();
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview);

                if(mapFragment!=null)
                {
                    mapFragment.getMapAsync(MainActivity.this);
                }


        init();




    }






    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;




        LatLng latLng = new LatLng(18.447265,73.858926);
        LatLng latLng1 = new LatLng(18.467265,73.858926);


        userlocation = latLng;





        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                showBottomSheetDialog(marker.getTag()+"");
                return false; // Return false to indicate that we haven't consumed the event and default behavior should occur (i.e., show the info window)
            }
        });




    }

    public void getCurrentLocation1()
    {


        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }


        LocationServices.getFusedLocationProviderClient(MainActivity.this)
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
                            map.addMarker(new MarkerOptions().position(latLng2).title("I am here"));


                            map.addCircle(new CircleOptions()
                                    .center(latLng2)
                                    .radius(1000) // Radius in meters
                                    .strokeWidth(2)
                                    .strokeColor(Color.BLUE)
                                    .fillColor(Color.argb(70, 0, 0, 255)));

                            map.addCircle(new CircleOptions()
                                    .center(latLng2)
                                    .radius(50000) // Radius in meters
                                    .strokeWidth(2)
                                    .strokeColor(Color.BLUE)
                                    .fillColor(Color.argb(20, 0, 0, 20)));



                            map.moveCamera(CameraUpdateFactory.newLatLng(latLng2));
//
                            map.moveCamera(CameraUpdateFactory.zoomTo(12f));
                            map.animateCamera(CameraUpdateFactory.zoomTo(12f));





                            getData1();

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

    public void showBottomSheetDialog(String tag)
    {

        final BottomSheetDialog bottomSheetDialog1 = new BottomSheetDialog(
                MainActivity.this,R.style.BottomSheetDialogTheme
        );


        View bottomSheetView = LayoutInflater.from(MainActivity.this)
                .inflate(
                        R.layout.layout_bottom_sheet,
                        (LinearLayout)findViewById(R.id.bottomsheetcontainer)
                );
        Owner allowner = mp.get(tag);


//
        TextView stationname = bottomSheetView.findViewById(R.id.stationname);
        TextView price,remainingEnergy,address;


        TextView avg = bottomSheetView.findViewById(R.id.avgr);
        avg.setText(allowner.getAvg_rating()+"");


//        Toast.makeText(this, ""+allowner.getPrice(), Toast.LENGTH_SHORT).show();
        price = bottomSheetView.findViewById(R.id.price);
        remainingEnergy = bottomSheetView.findViewById(R.id.remainingenergy);
        address = bottomSheetView.findViewById(R.id.address);
//        Toast.makeText(this, ""+ Integer.toHexString(System.identityHashCode(allowner.getOwner_location())), Toast.LENGTH_SHORT).show();
        price.setText(allowner.getPrice()+"");

//        Toast.makeText(this, "Address :"+getAddress(allowner.getOwner_location().getLatitude(),allowner.getOwner_location().getLongitude()) , Toast.LENGTH_SHORT).show();
//        remainingEnergy.setText(allowner.get);
        address.setText(getAddress(allowner.getOwner_location().getLatitude(),allowner.getOwner_location().getLongitude()));
        stationname.setText(allowner.getOwner_name());





        Button direction= bottomSheetView.findViewById(R.id.btnUpdate);
        Button book = bottomSheetView.findViewById(R.id.book);

//        t.setText(names.getOwner_name());


        //--------------------------------------write heare bottom sheet variable names------------------------


//        TextView button = findViewById(R.id.name);
//        book.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent  = new Intent(getApplicationContext(),BookSlot.class);
//                startActivity(intent);
//            }
//        });
//
//
//
//
//        direction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String origin = lati+","+longi; // New York coordinates
//                String destination = mp.get(tag).getOwner_location().getLatitude()+","+mp.get(tag).getOwner_location().getLongitude(); // Los Angeles coordinates
//                Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=" + origin + "&daddr=" + destination);
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);// Set the package to Google Maps
//                mapIntent.setPackage("com.google.android.apps.maps");
//                if (mapIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivity(mapIntent);
//                }
//
//            }
//        });
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
//        firebaseFirestore
//                .collection("Owner")
//                .document(allowner.getOwner_email())
//                .collection("EV_Station")
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot snaps) {
//                        if (snaps == null) return;
//                        evStations.addAll(snaps.toObjects(EVStation.class));
//                        int enrgy = 0 ;
//                        for(int i = 0 ; i < evStations.size();i++)
//                        {
//                            enrgy+=evStations.get(i).getEvs_energy();
//                        }
//                                remainingEnergy.setText(enrgy+"");
//
//
//
////                        Toast.makeText(MainActivity.this, ""+evStations.get(0).evs_energy, Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
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
//
//
//
//
//

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                firebaseFirestore
                        .collection("Owner")
                        .document(allowner.getOwner_email())
                        .collection("EV_Station")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot snaps) {
                                if (snaps == null) return;
                                evStations.addAll(snaps.toObjects(EVStation.class));
                                int enrgy = 0 ;
                                for(int i = 0 ; i < evStations.size();i++)
                                {
                                    if(evStations.get(i).getEvs_energy()>(allowner.getPrice()*30))
                                    {

                                        Intent intent  = new Intent(MainActivity.this,BookSlot.class);
//                                                    Toast.makeText(context, ""+dataholder2.get(position).getPrice(), Toast.LENGTH_SHORT).show();
                                        intent.putExtra("owner_email",allowner.getOwner_email());
                                        intent.putExtra("price",allowner.getPrice()+"");
//                                                    Toast.makeText(context, ""+dataholder2.get(position).getOwner_name(), Toast.LENGTH_SHORT).show();
                                        intent.putExtra("owner_name",allowner.getOwner_name());
                                        startActivity(intent);


                                    }






                                }
//                                            remainingEnergy.setText(enrgy+"");



//                        Toast.makeText(MainActivity.this, ""+evStations.get(0).evs_energy, Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });











            }
        });






        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(MainActivity.this, ""+allowner.getLat()+","+allowner.getLang(), Toast.LENGTH_SHORT).show();

                String origin = lati+","+longi; // New York coordinates
                String destination = allowner.getOwner_location().getLatitude()+","+allowner.getOwner_location().getLongitude(); // Los Angeles coordinates



                // Call the method to show directions
//        MapsHelper.showDirections(this, origin, destination);

                Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=" + origin + "&daddr=" + destination);

                // Create an Intent with the Uri
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                // Set the package to Google Maps
                mapIntent.setPackage("com.google.android.apps.maps");

                // Check if there's an app to handle this intent
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    // Start Google Maps with the intent
                    startActivity(mapIntent);
                }








            }
        });













        firebaseFirestore
                .collection("Owner")
                .document()
                .collection("EV_Station")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snaps) {
                        if (snaps == null) return;
                        evStations.addAll(snaps.toObjects(EVStation.class));
                        int enrgy = 0 ;
                        for(int i = 0 ; i < evStations.size();i++)
                        {
                            enrgy+=evStations.get(i).getEvs_energy();
                        }
                        remainingEnergy.setText(enrgy+"");



//                        Toast.makeText(MainActivity.this, ""+evStations.get(0).evs_energy, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });






























        bottomSheetDialog1.setContentView(bottomSheetView);
        bottomSheetDialog1.show();
    }


    private String getAddress(double lati,double longi) {

        String add = "";

        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            addresses = geocoder.getFromLocation(lati, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            add = addresses.get(0).getAddressLine(0);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return add;
    }





    private void getData1(){
        firebaseFirestore.collection("Owner")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Owner owner = doc.toObject(Owner.class);
                            LatLng latLng2 = new LatLng(owner.getOwner_location().getLatitude(), owner.getOwner_location().getLongitude());
//                            map.addMarker(new MarkerOptions().position(latLng2).title(owner.getEv_station_name()).icon(BitmapDescriptorFactory.fromResource(R.drawable.carev)));
//                            Marker marker = new Marker((zzad) new MarkerOptions());


                            TextView label  = findViewById(R.id.label);
//                          label.setText(""+doc);
//                            Toast.makeText(MainActivity.this, ""+doc.get("EV_Station"), Toast.LENGTH_SHORT).show();

                            double red = calculateDistance(lati,longi,latLng2.latitude,latLng2.longitude);
//                            Toast.makeText(MainActivity.this, "REdius :"+red, Toast.LENGTH_SHORT).show();

                            if(red<50000)
                            {
                                mp.put(owner.getOwner_email(),owner);
                                Marker marker = map.addMarker(new MarkerOptions()
                                        .position(latLng2)
                                        .title(owner.getEv_station_name())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.custom)));

                                // Tag the marker with the document ID to identify it later
                                marker.setTag(doc.getId());
                            }





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