package com.example.evchargingfinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddSlotAdapter extends RecyclerView.Adapter<AddSlotAdapter.LeadData>{

    List<TimeSlot> dataholder2;
    AdapterView.OnItemClickListener listener;

    int checker = -1;
    int flag = -1;
    int isuserselected = 0;

    private FirebaseAuth firebaseAuth;
    List<EVStation> evStations = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    Context context;
    final String sharedPreferencesFileTitle = "EV";
    public AddSlotAdapter(List<TimeSlot> dataholder2, Context context) {
        this.dataholder2 = dataholder2;
        this.context = context;

    }

    private void init(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    public void setFilteredList(List<Owner> filteredList) {
        Log.println(Log.DEBUG,"debug", "Finally"+filteredList);
//        this.dataholder2 = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public LeadData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        init();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.booktimeslot,parent,false);
        return new LeadData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeadData holder, int position) {











        if(dataholder2.get(position).status.equals("")) {
            int color = Color.parseColor("#D5E4E6");

            holder.time.setBackgroundColor(color);
        }
        else {

            if(firebaseAuth.getCurrentUser().getEmail().equals(dataholder2.get(position).getStatus()))
            {
                isuserselected = 1;
                holder.time.setBackgroundColor(Color.GREEN);
            }
            else {
                holder.time.setBackgroundResource(R.drawable.edittext_background);
            }


        }




        holder.time.setText(dataholder2.get(position).getTime());


            holder.time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(Integer.parseInt(dataholder2.get(position).getChargeStationN())>(Integer.parseInt(dataholder2.get(position).getUser_mail())*30))

                    {
                        if(checker==-1 && dataholder2.get(position).status.equals("") && isuserselected==0 )
                        {
                            holder.time.setBackgroundColor(Color.GREEN);
                            SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPreferencesFileTitle, context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("status", position+"");
                            editor.apply();

                            flag = position;
                            checker = 2;
                        }
                        else if(position==flag)
                        {
                            int color = Color.parseColor("#D5E4E6");

                            holder.time.setBackgroundColor(color);
                            flag = -1;
                            checker = -1;
                            SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPreferencesFileTitle, context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("status", "-1");
                            editor.apply();
                        }
                    }
                    else {
                        Toast.makeText(context, "You don't have enough energy..", Toast.LENGTH_SHORT).show();
                    }





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
        TextView time;
        public LeadData(@NonNull View itemView)
        {
            super(itemView);
            time = itemView.findViewById(R.id.times);


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
