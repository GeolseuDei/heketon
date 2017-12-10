package com.geolstudio.apipometera;

import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.ArrayList;

public class DetailPencarianKapalActivity extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    public static ArrayList<DataKapalKedatangan> dataKapalKedatangans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pencarian_kapal);

        StrictMode.ThreadPolicy policy;
        policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TextView tv_loading_kapal = findViewById(R.id.tv_loading_kapal);

        String tglmulai = "", tglselesai = "", namakode = "";
        tglmulai = JadwalKapalActivity.tglmulai;
        tglselesai = JadwalKapalActivity.tglselesai;
        namakode = JadwalKapalActivity.namakode;

        RecyclerView recyclerView = findViewById(R.id.recycler_kapal);
        dataKapalKedatangans = new ArrayList<>(getDataKapal(tglmulai, tglselesai, namakode));
        AdapterRecyclerKapal adapterRecyclerKapal = new AdapterRecyclerKapal(getApplicationContext(), dataKapalKedatangans);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterRecyclerKapal);

        tv_loading_kapal.setText("Jumlah data : " + dataKapalKedatangans.size());
    }

    private String getPometeraAPIKey() {
        String APIKey = "";
        HttpURLConnection conn = null;
        URL url = null;
        try {
            url = new URL("http://103.52.146.34/heketon/request_apikey.php");
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
                    .appendQueryParameter("namaapi", "jadwal_kedatangan_kapal");
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
                    APIKey = responses;
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
        return APIKey;
    }

    private ArrayList getDataKapal(String tglmulai, String tglselesai, String namakode) {
        String APIKey = getPometeraAPIKey();

        ArrayList<DataKapalKedatangan> dataKapalKedatangans = new ArrayList<>();
        HttpURLConnection conn = null;
        URL url = null;
        try {
            url = new URL("https://api.pometera.id/vessel/schedule_post");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setRequestProperty("X-Pometera-Api-Key", APIKey);

            conn.setDoInput(true);
            conn.setDoOutput(true);

            String query = "";
            if (namakode.length() == 0) {
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("start", tglmulai)
                        .appendQueryParameter("end", tglselesai);
                query = builder.build().getEncodedQuery();
            } else if (namakode.length() == 4) {
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("start", tglmulai)
                        .appendQueryParameter("end", tglselesai)
                        .appendQueryParameter("key", "vessel_reference")
                        .appendQueryParameter("value", namakode);
                query = builder.build().getEncodedQuery();
            } else {
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("start", tglmulai)
                        .appendQueryParameter("end", tglselesai)
                        .appendQueryParameter("key", "vessel_name")
                        .appendQueryParameter("value", namakode);
                query = builder.build().getEncodedQuery();
            }

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
                    JSONArray jsonArray = jsonObject.getJSONArray("payload");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String vessel_name = jsonArray.getJSONObject(i).getString("vessel_name");
                        String shipping_agent = jsonArray.getJSONObject(i).getString("shipping_agent");
                        String eta = jsonArray.getJSONObject(i).getString("eta");
                        String etd = jsonArray.getJSONObject(i).getString("etd");
                        String origin_port = jsonArray.getJSONObject(i).getString("origin_port");
                        String final_port = jsonArray.getJSONObject(i).getString("final_port");
                        String last_port = jsonArray.getJSONObject(i).getString("last_port");
                        String next_port = jsonArray.getJSONObject(i).getString("next_port");
                        String status = jsonArray.getJSONObject(i).getString("status");
                        dataKapalKedatangans.add(new DataKapalKedatangan(vessel_name, shipping_agent, eta, etd, origin_port, final_port, last_port, next_port, status));
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
        return dataKapalKedatangans;
    }
}
