package com.geolstudio.apipometera;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class ContainerActivity extends AppCompatActivity {

    public static ArrayList<DataContainer> dataContainers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        final TextView tvLoading = findViewById(R.id.tv_loading);
        final EditText etSearch = findViewById(R.id.et_search);
        ImageButton btnSearch = findViewById(R.id.btn_search);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etSearch.getText().toString().trim().isEmpty()) {
                    String kode = etSearch.getText().toString().trim();
                    RecyclerView recyclerView = findViewById(R.id.recycler_container);
                    dataContainers = new ArrayList<>(getDataContainer(kode));
                    AdapterRecyclerContainer adapterRecyclerContainer = new AdapterRecyclerContainer(getApplicationContext(), dataContainers);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(adapterRecyclerContainer);
                    tvLoading.setVisibility(View.GONE);
                }
            }
        });
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
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);

            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("namaapi", "container_tracking");
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

    private ArrayList getDataContainer(String kode) {
        String APIKey = getPometeraAPIKey();

        ArrayList<DataContainer> dataContainers = new ArrayList<>();
        HttpURLConnection conn = null;
        URL url = null;
        try {
            url = new URL("https://api.pometera.id/track_point/track_post");
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


            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("no_cont", kode);
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
                    JSONArray jsonArray = jsonObject.getJSONArray("payload");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String vessel_name = jsonArray.getJSONObject(i).getString("vessel_name");
                        String ata = jsonArray.getJSONObject(i).getString("ata");
                        String terminal_id = jsonArray.getJSONObject(i).getString("terminal_id");
                        String full_empty = jsonArray.getJSONObject(i).getString("full_empty");
                        String carrier = jsonArray.getJSONObject(i).getString("carrier");
                        dataContainers.add(new DataContainer(vessel_name, ata, terminal_id, full_empty, carrier));
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
        return dataContainers;
    }
}
