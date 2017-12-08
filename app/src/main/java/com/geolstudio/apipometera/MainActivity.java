package com.geolstudio.apipometera;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final EditText to, subject, body;
        to = findViewById(R.id.to);
        subject = findViewById(R.id.subject);
        body = findViewById(R.id.body);

        final TextView response = findViewById(R.id.response);
        final TextView quota = findViewById(R.id.quota);

        Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    conn.setRequestProperty("X-Pometera-Api-Key", "a7c2925d-d40e-4651-853d-d43471d411a2");
                    conn.setUseCaches(false);

                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("token", "KS-0IFyXWlKhVirZoXrBokBCAPE6MTUxMjczMTMzNjc1ODM3Mjg4Ng==")
                            .appendQueryParameter("to", to.getText().toString().trim())
                            .appendQueryParameter("subject", subject.getText().toString().trim())
                            .appendQueryParameter("body", body.getText().toString().trim());
                    String query = builder.build().getEncodedQuery();

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();
                    conn.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    int response_code = conn.getResponseCode();

                    if (response_code == HttpURLConnection.HTTP_OK) {

                        InputStream input = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                        StringBuilder result = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                        response.setText(result);

                        quota.setText(conn.getHeaderField("x-quota-remaining"));

                    } else {
                        response.setText(conn.getResponseCode());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    conn.disconnect();
                }
            }
        });
    }
}
