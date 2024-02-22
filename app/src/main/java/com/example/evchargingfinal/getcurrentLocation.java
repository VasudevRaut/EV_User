package com.example.evchargingfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class getcurrentLocation extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    ProgressBar progressBar;
    TextView textLatLong, address, postcode, locaity, state, district, country;
    ResultReceiver resultReceiver;

    private static final double EARTH_RADIUS = 6371000; // meters

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getcurrent_location);
        resultReceiver = new AddressResultReceiver(new Handler());

        progressBar = findViewById(R.id.progress_circular);
        textLatLong = findViewById(R.id.textLatLong);

        address = findViewById(R.id.textaddress);
        locaity = findViewById(R.id.textlocality);
        postcode = findViewById(R.id.textcode);
        country = findViewById(R.id.textcountry);
        district = findViewById(R.id.textdistrict);
        state = findViewById(R.id.textstate);






        final ArrayList<Pair<Double, Double>> locations = new ArrayList<>();
        locations.add(new Pair<>(37.7749, -122.4194)); // San Francisco
        locations.add(new Pair<>(34.0522, -118.2437)); // Los Angeles
        locations.add(new Pair<>(40.7128, -74.0060)); // New York

        Button showMapButton = findViewById(R.id.show_map_button);
        showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                String origin = "18.447265,73.858926"; // New York coordinates
//                String destination = "18.4563394,73.8772821"; // Los Angeles coordinates
//
//
//
//                // Call the method to show directions
////        MapsHelper.showDirections(this, origin, destination);
//
//                Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=" + origin + "&daddr=" + destination);
//
//                // Create an Intent with the Uri
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//
//                // Set the package to Google Maps
//                mapIntent.setPackage("com.google.android.apps.maps");
//
//                // Check if there's an app to handle this intent
//                if (mapIntent.resolveActivity(getPackageManager()) != null) {
//                    // Start Google Maps with the intent
//                    startActivity(mapIntent);
//                }


//                Intent intent = new Intent(MainActivity.this, MapFragment.class);
////                intent.putExtra("locations", locations);
//                startActivity(intent);
            }
        });







        double startLat = 18.447265; // Latitude of first location
        double startLon = 73.858926; // Longitude of first location
        double endLat = 18.4563394; // Latitude of second location
        double endLon = 73.8772821; // Longitude of second location
//
//        LatLng origin = new LatLng(startLat, startLon);
//        LatLng destination = new LatLng(endLat, endLon);
//        String  TAG = "Vasudev";
//        RouteFetcher routeFetcher = new RouteFetcher(new RouteFetcher.RouteListener() {
//            @Override
//            public void onRoutesReceived(DirectionsRoute[] routes) {
//                // Process routes and choose the best one
//                // You can iterate through the routes array to access each route
//                // For example, you can access the distance and duration of each route:
//
//                for (DirectionsRoute route : routes) {
//                    Log.d(TAG, "Distance: " + route.legs[0].distance);
//                    Log.d(TAG, "Duration: " + route.legs[0].duration);
//                }
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                Log.e(TAG, "Error: " + errorMessage);
//            }
//        });
//
//        routeFetcher.execute(origin, destination);























        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getcurrentLocation.this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                    getCurrentLocation();
                }
            }
        });

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
//        Toast.makeText(this, "getcurrentLocation", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
        LocationRequest locationRequest = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            locationRequest = new LocationRequest();
        }
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        LocationServices.getFusedLocationProviderClient(getcurrentLocation.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(getApplicationContext())
                                .removeLocationUpdates(this);
//                        Toast.makeText(getcurrentLocation.this, "when set locatin", Toast.LENGTH_SHORT).show();
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestlocIndex = locationResult.getLocations().size() - 1;
                            double lati = locationResult.getLocations().get(latestlocIndex).getLatitude();
                            double longi = locationResult.getLocations().get(latestlocIndex).getLongitude();
                            textLatLong.setText(String.format("Latitude : %s\n Longitude: %s", lati, longi));
                            progressBar.setVisibility(View.GONE);

//                            Location location = new Location("providerNA");
//                            location.setLongitude(longi);
//                            location.setLatitude(lati);
//                            fetchaddressfromlocation(location);

                        } else {
                            progressBar.setVisibility(View.GONE);

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
                address.setText(resultData.getString(Constants.ADDRESS));
                locaity.setText(resultData.getString(Constants.LOCAITY));
                state.setText(resultData.getString(Constants.STATE));
                district.setText(resultData.getString(Constants.DISTRICT));
                country.setText(resultData.getString(Constants.COUNTRY));
                postcode.setText(resultData.getString(Constants.POST_CODE));
            } else {
//                Toast.makeText(getcurrentLocation.this, resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }

        }


    }

    private void fetchaddressfromlocation(Location location) {
//        Toast.makeText(this, "fetcaddressfromlocation", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, FetchAddressIntentServices.class);
        intent.putExtra(Constants.RECEVIER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);


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

    // Alternatively, you can use Android's built-in Location class to calculate distance
    public static float calculateDistanceUsingLocation(double lat1, double lon1, double lat2, double lon2) {
        Location location1 = new Location("");
        location1.setLatitude(lat1);
        location1.setLongitude(lon1);

        Location location2 = new Location("");
        location2.setLatitude(lat2);
        location2.setLongitude(lon2);

        return location1.distanceTo(location2);
    }









}