package com.example.evchargingfinal;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class EnvTrackingAdapter extends RecyclerView.Adapter<EnvTrackingAdapter.LeadData>{

    List<User> dataholder2;

    Context context;
    public EnvTrackingAdapter(List<User> dataholder2, Context context) {
        Toast.makeText(context, "Call here"+dataholder2.size(), Toast.LENGTH_SHORT).show();
        this.dataholder2 = dataholder2;
        this.context = context;

    }

    public void setFilteredList(List<Owner> filteredList) {
//        this.dataholder2 = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public LeadData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.env_track_card,parent,false);
        return new LeadData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeadData holder, int position) {





    }

    @Override
    public int getItemCount() {

//        if(dataholder2.size()>50)
//        {
//            return 50;
//        }

        return dataholder2.size();
    }


    class LeadData extends RecyclerView.ViewHolder
    {
        TextView name,budget,prefer,property;
        CardView fullLead;
        ImageView platform;
        TextView lead_date,status,lead_type;
        public LeadData(@NonNull View itemView)
        {
            super(itemView);

        }
    }
    public boolean isNumeric(String str) {
        return str.matches("\\d+");
    }



}
