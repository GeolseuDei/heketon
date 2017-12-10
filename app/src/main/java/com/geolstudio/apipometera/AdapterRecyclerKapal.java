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
        holder.vessel_name.setText(dataKapalKedatangans.get(position).getVessel_name());
        holder.shipping_agent.setText(dataKapalKedatangans.get(position).getShipping_agent());
        holder.eta.setText("ETA : " + dataKapalKedatangans.get(position).getEta());
        holder.etd.setText("ETD : " + dataKapalKedatangans.get(position).getEtd());
        holder.origin_port.setText("Origin Port : " + dataKapalKedatangans.get(position).getOrigin_port());
        holder.final_port.setText("Final Port : " + dataKapalKedatangans.get(position).getFinal_port());
        holder.last_port.setText("Last Port : " + dataKapalKedatangans.get(position).getLast_port());
        holder.next_port.setText("Next Port : " + dataKapalKedatangans.get(position).getNext_port());
        holder.status.setText("Status : " + dataKapalKedatangans.get(position).getStatus());

        holder.btnKirimPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KirimEmailDataKapalActivity.position = position;
                context.startActivity(new Intent(context, KirimEmailDataKapalActivity.class));
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
        return dataKapalKedatangans.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView vessel_name, shipping_agent, eta, etd, origin_port, final_port, last_port, next_port, status;
        public Button btnKirimPDF;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            vessel_name = itemView.findViewById(R.id.txtvesselname);
            shipping_agent = itemView.findViewById(R.id.txtshippingagent);
            eta = itemView.findViewById(R.id.txteta);
            etd = itemView.findViewById(R.id.txtetd);
            origin_port = itemView.findViewById(R.id.txtorigin);
            final_port = itemView.findViewById(R.id.txtfinal);
            last_port = itemView.findViewById(R.id.txtlast);
            next_port = itemView.findViewById(R.id.txtnext);
            status = itemView.findViewById(R.id.txtStatus);

            btnKirimPDF = itemView.findViewById(R.id.btnKirimPDF);
        }
    }

}
