package com.geolstudio.apipometera;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by GeolseuDei on 10/18/2017.
 */

public class AdapterRecyclerContainer extends RecyclerView.Adapter<AdapterRecyclerContainer.ViewHolder> {

    ArrayList<DataContainer> dataContainers;
    Context context;

    public AdapterRecyclerContainer(Context context, ArrayList<DataContainer> dataContainers) {
        this.context = context;
        this.dataContainers = dataContainers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_container,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.vessel_name.setText(dataContainers.get(position).getVessel_name());
        holder.ata.setText("ATA : "+dataContainers.get(position).getAta());
        holder.terminal_id.setText("Terminal ID : " + dataContainers.get(position).getTerminal_id());
        holder.full_empty.setText("Full / Empty : " + dataContainers.get(position).getFull_empty());
        holder.carrier.setText("Carrier : " + dataContainers.get(position).getCarrier());

        holder.btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KirimEmailDataContainerActivity.position = position;

                context.startActivity(new Intent(context, KirimEmailDataContainerActivity.class));
            }
        });

        //view
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataContainers.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView vessel_name, ata, terminal_id, full_empty, carrier;
        public Button btnKirim;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            vessel_name = itemView.findViewById(R.id.txtvesselname);
            ata = itemView.findViewById(R.id.txtata);
            terminal_id = itemView.findViewById(R.id.txtterminal);
            full_empty = itemView.findViewById(R.id.txtfull);
            carrier = itemView.findViewById(R.id.txtcarrier);
            btnKirim = itemView.findViewById(R.id.btnKirimEmailContainer);
        }
    }

}
