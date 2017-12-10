package com.geolstudio.apipometera;

import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class KirimEmailDataContainerActivity extends AppCompatActivity {

    public static int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kirim_email_data_container);

        StrictMode.ThreadPolicy policy;
        policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sendEmail();

        onBackPressed();
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
                    .appendQueryParameter("namaapi", "helio");
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

    private String getHelioToken() {
        String token = "";
        HttpURLConnection conn = null;
        URL url = null;
        try {
            url = new URL("http://103.52.146.34/heketon/request_token_helio.php");
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
                    .appendQueryParameter("nama", "priokreport");
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
                    token = jsonObject.getString("token");

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
        return token;
    }

    private void sendEmail() {
        String apikey = getPometeraAPIKey();
        String token = getHelioToken();
        String body =
                "Vessel name : " + ContainerActivity.dataContainers.get(position).getVessel_name() + "<br>" +
                        "ATA : " + ContainerActivity.dataContainers.get(position).getAta() + "<br>" +
                        "Terminal ID : " + ContainerActivity.dataContainers.get(position).getTerminal_id() + "<br>" +
                        "Full / Empty : " + ContainerActivity.dataContainers.get(position).getFull_empty() + "<br>" +
                        "Carrier : " + ContainerActivity.dataContainers.get(position).getCarrier();
        HttpURLConnection conn = null;
        URL url = null;
        try {
            url = new URL("https://api.pometera.id/helio/compose");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setRequestProperty("X-Pometera-Api-Key", apikey);

            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("token", token)
                    .appendQueryParameter("to", LoginActivity.user.getEmail())
                    .appendQueryParameter("subject", "Data container yang anda pilih")
                    .appendQueryParameter("body", body);
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
                    String code = jsonObject.getString("code");
                    if (code.equalsIgnoreCase("200")) {
                        Toast.makeText(getApplicationContext(), "Email data kapal berhasil dikirim.", Toast.LENGTH_SHORT).show();
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
}
