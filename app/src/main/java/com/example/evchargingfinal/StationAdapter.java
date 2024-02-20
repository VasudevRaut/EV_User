package com.example.evchargingfinal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evchargingfinal.rating.GiveRatingActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.LeadData>{

    List<Owner> dataholder2;

    private FirebaseAuth firebaseAuth;
    List<EVStation> evStations = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    Context context;
    final String sharedPreferencesFileTitle = "ecoview";
    public StationAdapter(List<Owner> dataholder2, Context context) {
        this.dataholder2 = dataholder2;
        this.context = context;

    }

    private void init(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    public void setFilteredList(List<Owner> filteredList) {
        Log.println(Log.DEBUG,"debug", "Finally"+filteredList);
        this.dataholder2 = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public LeadData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        init();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.stationscard,parent,false);
        return new LeadData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeadData holder, int position) {




            holder.stationname.setText(dataholder2.get(position).getEv_station_name());
            holder.address.setText(getAddress(dataholder2.get(position).getOwner_location().getLatitude(),dataholder2.get(position).getOwner_location().getLongitude()));










        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final BottomSheetDialog bottomSheetDialog1 = new BottomSheetDialog(
                        context,R.style.BottomSheetDialogTheme
                );

                View bottomSheetView = LayoutInflater.from(context)
                        .inflate(
                                R.layout.layout_bottom_sheet, holder.bottomsheet
                        );






                TextView stationname = bottomSheetView.findViewById(R.id.stationname);
                TextView price,remainingEnergy,address;
                price = bottomSheetView.findViewById(R.id.price);
                remainingEnergy = bottomSheetView.findViewById(R.id.remainingenergy);
                address = bottomSheetView.findViewById(R.id.address);
                price.setText(dataholder2.get(position).getPrice()+"");

                address.setText(getAddress(dataholder2.get(position).getOwner_location().getLatitude(),dataholder2.get(position).getOwner_location().getLongitude()));
                stationname.setText(dataholder2.get(position).getEv_station_name());

                TextView giverating = bottomSheetView.findViewById(R.id.giveRating);
                giverating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(context, GiveRatingActivity.class);
                        intent.putExtra("owner_id",dataholder2.get(position).getOwner_id());
                        intent.putExtra("owner_email",dataholder2.get(position).getOwner_email());
                        context.startActivity(intent);


                    }
                });



                Button direction= bottomSheetView.findViewById(R.id.btnUpdate);
                Button book = bottomSheetView.findViewById(R.id.book);

//        t.setText(names.getOwner_name());


                //--------------------------------------write heare bottom sheet variable names------------------------


//        TextView button = findViewById(R.id.name);











                    book.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {




                            firebaseFirestore
                                    .collection("Owner")
                                    .document(dataholder2.get(position).getOwner_email())
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
                                                if(evStations.get(i).getEvs_energy()>(dataholder2.get(position).getPrice()*30))
                                                {

                                                    Intent intent  = new Intent(context,BookSlot.class);
//                                                    Toast.makeText(context, ""+dataholder2.get(position).getPrice(), Toast.LENGTH_SHORT).show();
                                                    intent.putExtra("owner_email",dataholder2.get(position).getOwner_email());
                                                    intent.putExtra("price",dataholder2.get(position).getPrice()+"");
//                                                    Toast.makeText(context, ""+dataholder2.get(position).getOwner_name(), Toast.LENGTH_SHORT).show();
                                                    intent.putExtra("owner_name",dataholder2.get(position).getOwner_name());
                                                    context.startActivity(intent);


                                                }






                                            }
//                                            remainingEnergy.setText(enrgy+"");



//                        Toast.makeText(MainActivity.this, ""+evStations.get(0).evs_energy, Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });











                        }
                    });






                direction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String origin = dataholder2.get(position).getLat()+","+dataholder2.get(position).getLang(); // New York coordinates
                        String destination = dataholder2.get(position).getOwner_location().getLatitude()+","+dataholder2.get(position).getOwner_location().getLongitude(); // Los Angeles coordinates



                        // Call the method to show directions
//        MapsHelper.showDirections(this, origin, destination);

                        Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=" + origin + "&daddr=" + destination);

                        // Create an Intent with the Uri
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                        // Set the package to Google Maps
                        mapIntent.setPackage("com.google.android.apps.maps");

                        // Check if there's an app to handle this intent
                        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                            // Start Google Maps with the intent
                            context.startActivity(mapIntent);
                        }








                    }
                });













                firebaseFirestore
                        .collection("Owner")
                        .document(dataholder2.get(position).getOwner_email())
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
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });




























                bottomSheetDialog1.setContentView(bottomSheetView);
                bottomSheetDialog1.show();



            }
        });



    }

    private String getAddress(double lati,double longi) {

        String add = "";

        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());

            addresses = geocoder.getFromLocation(lati, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            add = addresses.get(0).getAddressLine(0);
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return add;
    }

    @Override
    public int getItemCount() {
        return dataholder2.size();
    }


    class LeadData extends RecyclerView.ViewHolder
    {
        TextView stationname,address;
        LinearLayout card;

        LinearLayout bottomsheet;
        public LeadData(@NonNull View itemView)
        {
            super(itemView);
            bottomsheet = itemView.findViewById(R.id.bottomsheetcontainer);
            card = itemView.findViewById(R.id.card);
            stationname = itemView.findViewById(R.id.station_name);
            address = itemView.findViewById(R.id.distance);

        }
    }
    public boolean isNumeric(String str) {
        return str.matches("\\d+");
    }


//    public void showBottomSheetDialog(String tag)
//    {
//
//        final BottomSheetDialog bottomSheetDialog1 = new BottomSheetDialog(
//                context,R.style.BottomSheetDialogTheme
//        );
//
//
//        View bottomSheetView = LayoutInflater.from(context)
//                .inflate(
//                        R.layout.layout_bottom_sheet, (LinearLayout)findViewById(R.id.bottomsheetcontainer)
//                );
////        Owner allowner = mp.get(tag);
//
//
////
//        TextView stationname = bottomSheetView.findViewById(R.id.stationname);
//        TextView price,remainingEnergy,address;
////        Toast.makeText(this, ""+allowner.getPrice(), Toast.LENGTH_SHORT).show();
//        price = bottomSheetView.findViewById(R.id.price);
//        remainingEnergy = bottomSheetView.findViewById(R.id.remainingenergy);
//        address = bottomSheetView.findViewById(R.id.address);
////        Toast.makeText(this, ""+ Integer.toHexString(System.identityHashCode(allowner.getOwner_location())), Toast.LENGTH_SHORT).show();
////        price.setText(allowner.getPrice()+"");
////        remainingEnergy.setText(allowner.get);
////        address.setText(getAddress());
////        stationname.setText(allowner.getOwner_name());
//
//
//
//
//
//        Button direction= bottomSheetView.findViewById(R.id.btnUpdate);
//        Button book = bottomSheetView.findViewById(R.id.book);
//
//
//
//
//        bottomSheetDialog1.setContentView(bottomSheetView);
//        bottomSheetDialog1.show();
//    }




}
