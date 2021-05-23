package com.thecodecity.mapsdirection;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ViewPass extends AppCompatActivity {
    TextView tx1,tx2,tx3,tx4,tx5,tx6,tx7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pass);

        tx1 = findViewById(R.id.textView1);
        tx2 = findViewById(R.id.textView3);
        tx3 = findViewById(R.id.textView5);
        tx4 = findViewById(R.id.textView7);
        tx5 = findViewById(R.id.textView9);
        tx6 = findViewById(R.id.textView11);
        tx7 = findViewById(R.id.textView13);

        String use = getIntent().getStringExtra("username");
        String bus = getIntent().getStringExtra("busnumber");
        String busp = getIntent().getStringExtra("busplate1");
        String sou = getIntent().getStringExtra("source1");
        String des = getIntent().getStringExtra("destination1");
        String fro = getIntent().getStringExtra("from1");
        String to = getIntent().getStringExtra("to1");

        tx1.setText(use);
        tx2.setText(bus);
        tx3.setText(busp);
        tx4.setText(sou);
        tx5.setText(des);
        tx6.setText(fro);
        tx7.setText(to);

    }

}