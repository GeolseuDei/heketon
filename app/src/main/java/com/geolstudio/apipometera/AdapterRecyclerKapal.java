package com.geolstudio.apipometera;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by GeolseuDei on 10/18/2017.
 */

public class AdapterRecyclerKapal extends RecyclerView.Adapter<AdapterRecyclerKapal.ViewHolder> {

    ArrayList<DataKapalKedatangan> dataKapalKedatangans;
    Context context;

    public AdapterRecyclerKapal(Context context, ArrayList<DataKapalKedatangan> dataKapalKedatangans) {
        this.context = context;
        this.dataKapalKedatangans = dataKapalKedatangans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_kapal,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        //image
//        holder.imageFood.setImageResource(R.drawable.doenjang);
//        holder.imageFood.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "Image position : " + position, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //name
//        holder.tvFoodName.setText(foodList.get(position).getName());
//
//        //rating
//        holder.ratingFood.setRating(foodList.get(position).getRating());
//
//        //button order
//        holder.btnOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "Button position : " + position, Toast.LENGTH_SHORT).show();
//            }
//        });

        //view
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataKapalKedatangans.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }
}
