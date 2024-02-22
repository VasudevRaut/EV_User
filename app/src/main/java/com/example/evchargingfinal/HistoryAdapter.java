package com.example.evchargingfinal;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;
import java.util.Locale;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.LeadData> {

    List<History> dataholder2;

    Context context;

    public HistoryAdapter(List<History> dataholder2, Context context) {
//        Toast.makeText(context, "Call here" + dataholder2.size(), Toast.LENGTH_SHORT).show();
        this.dataholder2 = dataholder2;
        this.context = context;

    }

    public void setFilteredList(List<History> filteredList) {
//        this.dataholder2 = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public LeadData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_layout, parent, false);
        return new LeadData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeadData holder, int position) {

        holder.watts.setText(""+dataholder2.get(position).getHistory_energy());
        holder.money.setText(""+dataholder2.get(position).getHistory_price());
        holder.time.setText(""+dataholder2.get(position).getHistory_time());
        holder.geolocation.setText(getAddress(dataholder2.get(position).getHistory_location().getLatitude(),dataholder2.get(position).getHistory_location().getLongitude()));


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


    class LeadData extends RecyclerView.ViewHolder {
        //        RatingBar ratingBar;
//        TextView tvID, tvAvailable, tvEnergy;
//
//        CardView cardView;
        TextView watts,money,geolocation,time;


        public LeadData(@NonNull View itemView) {
            super(itemView);

            watts = itemView.findViewById(R.id.watts);
            money = itemView.findViewById(R.id.money);
            geolocation = itemView.findViewById(R.id.geolocation);
            time = itemView.findViewById(R.id.time);
        }
    }

    public boolean isNumeric(String str) {
        return str.matches("\\d+");
    }


}