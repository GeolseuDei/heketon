package com.geolstudio.apipometera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class JadwalKapalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_kapal);

        EditText et_tglMulai, et_tglSelesai, et_namakode;
        Button btn_cari_kapal;

        et_tglMulai = findViewById(R.id.et_tglMulai);
    }
}
