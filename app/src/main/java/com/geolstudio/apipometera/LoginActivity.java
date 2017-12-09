package com.geolstudio.apipometera;

import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    public static final User user = new User();

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvDaftar;
    StrictMode.ThreadPolicy policy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initValue();

        setListener();

        StrictMode.setThreadPolicy(policy);
    }

    public void initValue(){
        policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        etEmail = findViewById(R.id.et_email_login);
        etPassword = findViewById(R.id.et_password_login);
        btnLogin = findViewById(R.id.btn_login);
        tvDaftar = findViewById(R.id.tv_daftar);
    }

    public void setListener(){

        //MARK : onclick button login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etEmail.getText().toString().isEmpty()){
                    if(!etPassword.getText().toString().isEmpty()){
                        HttpURLConnection conn = null;
                        URL url = null;
                        try {
                            url = new URL("http://103.52.146.34/heketon/login.php");
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
                                    .appendQueryParameter("email", etEmail.getText().toString().trim())
                                    .appendQueryParameter("password", etPassword.getText().toString().trim());
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
                                    if(responses.equalsIgnoreCase("200")){
                                        String id = jsonObject.getString("id");
                                        String email = jsonObject.getString("email");
                                        String nohp = jsonObject.getString("nohp");
                                        String status_verif_hp = jsonObject.getString("status_verif_nohp");
                                        String status_verif_email = jsonObject.getString("status_verif_email");

                                        user.setId(id);
                                        user.setEmail(email);
                                        user.setNohp(nohp);
                                        user.setStatus_verif_nohp(status_verif_hp);
                                        user.setStatus_verif_email(status_verif_email);

                                        Toast.makeText(getApplicationContext(), "Login berhasil.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Login gagal.", Toast.LENGTH_SHORT).show();
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
                        etPassword.setError("Masukkan password.");
                        etPassword.requestFocus();
                    }
                } else {
                    etEmail.setError("Masukkan email.");
                    etEmail.requestFocus();
                }
            }
        });

        //MARK : onclick button tvdaftar
        tvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
