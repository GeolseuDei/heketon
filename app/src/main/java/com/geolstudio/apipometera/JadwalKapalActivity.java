package com.geolstudio.apipometera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class JadwalKapalActivity extends AppCompatActivity {

    public static String tglmulai = "", tglselesai = "", namakode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_kapal);

        final EditText et_tglMulai, et_tglSelesai, et_namakode;
        Button btn_cari_kapal;

        et_tglMulai = findViewById(R.id.et_tglMulai);
        et_tglSelesai = findViewById(R.id.et_tglSelesai);
        et_namakode = findViewById(R.id.et_namakodekapal);

        btn_cari_kapal = findViewById(R.id.btn_cari_kapal);

        btn_cari_kapal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_tglMulai.getText().toString().trim().isEmpty()){
                    if(!et_tglSelesai.getText().toString().trim().isEmpty()){
                        tglmulai = et_tglMulai.getText().toString();
                        tglselesai = et_tglSelesai.getText().toString();
                        namakode = et_namakode.getText().toString();

                        startActivity(new Intent(getApplicationContext(), DetailPencarianKapalActivity.class));
                    } else {
                        et_tglSelesai.setError("Masukkan tanggal selesai.");
                        et_tglSelesai.requestFocus();
                    }
                } else {
                    et_tglMulai.setError("Masukkan tanggal mulai.");
                    et_tglMulai.requestFocus();
                }
            }
        });
    }
}
