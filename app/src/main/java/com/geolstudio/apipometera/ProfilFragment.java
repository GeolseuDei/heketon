package com.geolstudio.apipometera;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {


    public ProfilFragment() {
        // Required empty public constructor
    }

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TextView tvUserEmail, tvUserNoHP, tvUserStatusVerifHP, tvUserStatisVerifEmail;
        tvUserEmail = view.findViewById(R.id.tv_user_email);
        tvUserNoHP = view.findViewById(R.id.tv_user_nohp);
        tvUserStatusVerifHP = view.findViewById(R.id.tv_user_status_verif_hp);
        tvUserStatisVerifEmail = view.findViewById(R.id.tv_user_status_verif_email);

        final Button btnEdit, btnVerifEmail;
        btnEdit = view.findViewById(R.id.btnEdit);
        btnVerifEmail = view.findViewById(R.id.btnVerifikasiEmail);

        tvUserEmail.setText(LoginActivity.user.getEmail());
        tvUserNoHP.setText(LoginActivity.user.getNohp());
        if (LoginActivity.user.getStatus_verif_nohp().equalsIgnoreCase("0")) {
            tvUserStatusVerifHP.setText("Belum selesai");
        } else {
            tvUserStatusVerifHP.setText("Selesai");
        }
        if (LoginActivity.user.getStatus_verif_email().equalsIgnoreCase("0")) {
            tvUserStatisVerifEmail.setText("Belum selesai");
            btnVerifEmail.setVisibility(View.VISIBLE);
        } else {
            tvUserStatisVerifEmail.setText("Selesai");
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnVerifEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
                btnVerifEmail.setEnabled(false);
            }
        });

        return view;
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
                Toast.makeText(getContext(), "Response Code : " + response_code, Toast.LENGTH_SHORT).show();
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
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
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
                Toast.makeText(getContext(), "Response Code : " + response_code, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return token;
    }

    private String getKodeVerifEmail() {
        String kodeVerif = "";
        HttpURLConnection conn = null;
        URL url = null;
        try {
            url = new URL("http://103.52.146.34/heketon/request_kode_verif_email.php");
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
                    .appendQueryParameter("id", LoginActivity.user.getId());
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
                    kodeVerif = jsonObject.getString("kode");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getContext(), "Response Code : " + response_code, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return kodeVerif;
    }

    private void sendEmail() {
        String apikey = getPometeraAPIKey();
        String kodeVerifEmail = getKodeVerifEmail();
        String token = getHelioToken();
        String body = "Hai,\n"
                + LoginActivity.user.getEmail()
                + "\n\n" +
                "Untuk memverifikasi email anda, klik tautan berikut :\n\n" +
                "http://103.52.146.34/heketon/verifikasi_email.php?msg=" + kodeVerifEmail + "&iden=" + LoginActivity.user.getId() + "\n\n"+
                "Priok Report,\nTim Kebut Semalam.";
        HttpURLConnection conn = null;
        URL url = null;
        try {
            url = new URL("https://api.pometera.id/helio/compose");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setRequestProperty("X-Pometera-Api-Key", apikey);

            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("token", token)
                    .appendQueryParameter("to", LoginActivity.user.getEmail())
                    .appendQueryParameter("subject", "Verifikasi Email Priok Report")
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
                    if(code.equalsIgnoreCase("200")){
                        Toast.makeText(getContext(), "Email verifikasi berhasil dikirim.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getContext(), "Response Code : " + response_code, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
    }
}
