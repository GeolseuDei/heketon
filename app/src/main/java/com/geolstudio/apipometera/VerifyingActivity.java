package com.geolstudio.apipometera;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class VerifyingActivity extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    SharedPreferences sharedPreferences;
    EditText etVerifikasi;
    Button btnVerifikasi;
    StrictMode.ThreadPolicy policy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifying);

        initValue();

        setListener();

        StrictMode.setThreadPolicy(policy);

        //Toast.makeText(getApplicationContext(), sharedPreferences.getString("kode_verifikasi", ""), Toast.LENGTH_LONG).show();
    }

    public void initValue() {
        policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        sharedPreferences = getSharedPreferences(RegisterActivity.TAG_SHAREDPREFERENCES, MODE_PRIVATE);

        etVerifikasi = findViewById(R.id.et_verifikasi);
        btnVerifikasi = findViewById(R.id.btnVerifikasi);
    }

    public void setListener() {
        btnVerifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etVerifikasi.getText().length() != 6) {
                    Toast.makeText(getApplicationContext(), "Kode verifikasi berjumlah 6 digit.", Toast.LENGTH_SHORT).show();
                } else {
                    if (etVerifikasi.getText().toString().equals(sharedPreferences.getString("kode_verifikasi", ""))) {
                        String id = RegisterActivity.idUser;
                        updateVerifikasiHP(id);
                    } else {
                        Toast.makeText(getApplicationContext(), "Kode verifikasi salah.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void updateVerifikasiHP(String id) {
        HttpURLConnection conn = null;
        URL url = null;
        try {
            url = new URL("http://103.52.146.34/heketon/verifikasi_hp.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);

            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("id", id);
            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

            int response_code = conn.getResponseCode();

            if (response_code == HttpURLConnection.HTTP_OK) {

                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result.toString());
                    String responses = jsonObject.getString("responses");
                    if (responses.equalsIgnoreCase("200")) {
                        Toast.makeText(getApplicationContext(), "Verifikasi berhasil.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Verifikasi gagal.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Response Code : " + response_code, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
