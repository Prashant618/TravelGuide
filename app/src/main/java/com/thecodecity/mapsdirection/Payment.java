package com.thecodecity.mapsdirection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Payment extends AppCompatActivity {
    EditText amu,upid,upino;
    Button button;
    Spinner pro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        amu = findViewById(R.id.edamount);
        upid = findViewById(R.id.edupid);
        upino = findViewById(R.id.edupino);
        pro = findViewById(R.id.bank);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.bank, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pro.setAdapter(adapter);
        button = findViewById(R.id.button);
        amu.setText(getIntent().getStringExtra("prize1"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(Payment.this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED){

                        sendMessage();

                        String username = userLogin.userdata;
                        String tx1 = getIntent().getStringExtra("busno");
                        String tx2 = getIntent().getStringExtra("destination");
                        String tx3 = getIntent().getStringExtra("timer");
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        String url = UrlLinks.payment;

                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);

                        nameValuePairs.add(new BasicNameValuePair("username", username));
                        nameValuePairs.add(new BasicNameValuePair("busnumber", tx1));
                        nameValuePairs.add(new BasicNameValuePair("destinationofbus", tx2));
                        nameValuePairs.add(new BasicNameValuePair("timeofbus", tx3));


                        JSONObject result = null;
                        try {
                            result = jSOnClassforData.forCallingServer(url, nameValuePairs);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//        JSONArray jArray = new JSONArray(result.toString());
//
//        for(int i=0;i<jArray.length();i++) {
//            String alldata = jArray.get(i).toString();
//
//            String datasplit[] = alldata.split("_");
//            latilongidata.add(alldata);
//
//
//
//
//        }


                        JSONArray jArray = null;
                        try {
                            jArray = result.getJSONArray("jsonarrayval");
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
//
//                    System.out.println("*****JARRAY*****" + jArray.length());
//
//                    for (int i = 0; i < jArray.length(); i++) {


                        JSONObject json_data;

                        try {
                            json_data = jArray.getJSONObject(0);
                            String bookName = json_data.getString("bookName");
//                        String author = json_data.getString("author");
//                        String publisher = json_data.getString("publisher");

                            if (bookName.equals("1")) {

                                Toast.makeText(Payment.this, "Payment sucessfull", Toast.LENGTH_SHORT).show();


                            } else {

                                Toast.makeText(Payment.this, "Wrong details", Toast.LENGTH_SHORT).show();

                            }


                            //  SplittingBooktime=bookName.split(",");

//							 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//					        		 R.layout.textview, SplittingBooktime);


                            //  Toast.makeText(SelectingLcoation.this,"Doctor Available at "+ bookName, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                }else {
                    ActivityCompat.requestPermissions(Payment.this,new String[]{Manifest.permission.SEND_SMS},100);
                }
            }
        });
    }
    private void sendMessage(){

        String amount = amu.getText().toString().trim();
        String upi = upid.getText().toString().trim();
        String upno = upino.getText().toString().trim();
        String tx1 = getIntent().getStringExtra("busno");
        String tx2 = getIntent().getStringExtra("destination");
        String tx3 = getIntent().getStringExtra("timer");
        String message = "Your Bus no "+tx1+" is booked. "+amount+" is paid by upi id "+upi+" Bus reaching time is "+tx3;
        if(amount.equals("")||upi.equals("")||upno.equals("")){
            Toast.makeText(getApplicationContext(),"Fill details", Toast.LENGTH_SHORT).show();
        }else {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(upno,null,message,null,null);
            Toast.makeText(getApplicationContext(),"Message Sent", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode ==100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            sendMessage();
        }else {
            Toast.makeText(getApplicationContext(),"Permission Denied !", Toast.LENGTH_SHORT).show();
        }
    }
}