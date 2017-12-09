package com.geolstudio.apipometera;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class RegisterActivity extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    EditText etEmail, etPassword, etRePassword, etNoHP;
    Button btnDaftar;

    public static String TAG_SHAREDPREFERENCES = "kebutsemalam";
    public static String idUser = "";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPreferences = getSharedPreferences(TAG_SHAREDPREFERENCES, MODE_PRIVATE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        etEmail = findViewById(R.id.et_email_daftar);
        etPassword = findViewById(R.id.et_password_daftar);
        etRePassword = findViewById(R.id.et_repassword_daftar);
        etNoHP = findViewById(R.id.et_nohp_daftar);
        btnDaftar = findViewById(R.id.btnDaftar);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etEmail.getText().toString().isEmpty()) {
                    if (etEmail.getText().toString().contains("@")) {
                        if (!etPassword.getText().toString().isEmpty()) {
                            if (!etRePassword.getText().toString().isEmpty()) {
                                if (!etNoHP.getText().toString().isEmpty()) {
                                    if (etRePassword.getText().toString().trim().equals(etPassword.getText().toString().trim())) {
                                        HttpURLConnection conn = null;
                                        URL url = null;
                                        try {
                                            url = new URL("http://192.168.100.16/heketon/register.php");
                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }

                                        try {
                                            conn = (HttpURLConnection) url.openConnection();
                                            conn.setReadTimeout(READ_TIMEOUT);
                                            conn.setConnectTimeout(CONNECTION_TIMEOUT);
                                            conn.setRequestMethod("POST");

                                            conn.setDoInput(true);
                                            conn.setDoOutput(true);

                                            Uri.Builder builder = new Uri.Builder()
                                                    .appendQueryParameter("email", etEmail.getText().toString().trim())
                                                    .appendQueryParameter("pw", etPassword.getText().toString().trim())
                                                    .appendQueryParameter("nohp", etNoHP.getText().toString().trim());
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

                                                    if (responses.equalsIgnoreCase("exist")) {
                                                        etEmail.setError("Email sudah terdaftar.");
                                                        etEmail.requestFocus();
                                                    } else {
                                                        if (responses.equalsIgnoreCase("400")) {
                                                            Toast.makeText(getApplicationContext(), "Terjadi error, coba kembali.", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            idUser = jsonObject.getString("id");
                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.putString("kode_verifikasi", responses);
                                                            editor.apply();

                                                            sendSMSNotif(etNoHP.getText().toString().trim(), responses);

                                                            startActivity(new Intent(getApplicationContext(), VerifyingActivity.class));
                                                        }
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
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Password tidak sama.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    etNoHP.setError("No HP tidak boleh kosong.");
                                    etNoHP.requestFocus();
                                }
                            } else {
                                etRePassword.setError("Ulang Password tidak boleh kosong.");
                                etRePassword.requestFocus();
                            }
                        } else {
                            etPassword.setError("Password tidak boleh kosong.");
                            etPassword.requestFocus();
                        }
                    } else {
                        etEmail.setError("Format email salah.");
                        etEmail.requestFocus();
                    }
                } else {
                    etEmail.setError("Email tidak boleh kosong.");
                    etEmail.requestFocus();
                }
            }
        });

    }

    private String getPometeraAPIKey() {
        String APIKey = "";
        HttpURLConnection conn = null;
        URL url = null;
        try {
            url = new URL("http://192.168.100.16/heketon/request_apikey.php");
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
                    .appendQueryParameter("namaapi", "smsnotif");
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

    private String requestSMSNotifToken() {
        String APIKey = getPometeraAPIKey();
        String token = "";
        HttpURLConnection conn = null;
        URL url = null;
        try {
            url = new URL("https://api.pometera.id/smsnotif/token");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("X-Pometera-Api-Key", APIKey);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setUseCaches(false);

            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("grant_type", "client_credentials");
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
                    String access_token = jsonObject.getString("access_token");
                    token = access_token;
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

    private void sendSMSNotif(String nohp, String kode_verif) {
        String APIKey = getPometeraAPIKey();
        String token = requestSMSNotifToken();
        String smsContent = "Terima kasih sudah mendaftar di Priok Report.\nKode verifikasi anda adalah : " + kode_verif;
        HttpURLConnection conn = null;
        URL url = null;
        try {
            url = new URL("https://api.pometera.id/smsnotif/messages");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("X-Pometera-Api-Key", APIKey);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setUseCaches(false);

            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("msisdn", nohp)
                    .appendQueryParameter("content", smsContent);
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
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }
}
