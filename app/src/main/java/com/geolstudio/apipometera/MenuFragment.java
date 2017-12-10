package com.geolstudio.apipometera;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {


    public MenuFragment() {
        // Required empty public constructor
    }

    ImageButton btnContainer, btnJadwalKedatangan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        initValue(view);

        setListener();

        return view;
    }

    public void initValue(View view) {
        btnContainer = view.findViewById(R.id.btn_menu_container);
        btnJadwalKedatangan = view.findViewById(R.id.btn_menu_jadwalkedatangan);
    }

    public void setListener() {
        btnContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ContainerActivity.class));
            }
        });
        btnJadwalKedatangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), JadwalKapalActivity.class));
            }
        });
    }

}
