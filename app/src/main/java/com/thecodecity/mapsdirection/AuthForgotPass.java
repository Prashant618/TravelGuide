package com.thecodecity.mapsdirection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class AuthForgotPass extends AppCompatActivity {
    EditText ed1,ed2;
    Button btn,btn1;
    public static String OTPNUMBER="";
    public static String MOBNO="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_forgot_pass);

        ed1 = findViewById(R.id.editText);
        ed2 = findViewById(R.id.editText1);
        btn = findViewById(R.id.button);
        btn1 = findViewById(R.id.button1);

        Random random = new Random();
        int val = random.nextInt(1000000);
        String number = Integer.toString(val);
        OTPNUMBER = number;

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(AuthForgotPass.this, Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED){
                    sendMessage();

                }else {
                    ActivityCompat.requestPermissions(AuthForgotPass.this,new String[]{Manifest.permission.SEND_SMS},100);
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String use = ed1.getText().toString();
                String otp = ed2.getText().toString();
                if(use.equals(""))
                {
                    Toast.makeText(AuthForgotPass.this,"Please enter username.",Toast.LENGTH_SHORT).show();
                }
                else if(otp.equals(""))
                {
                    Toast.makeText(AuthForgotPass.this,"Please verify OTP.",Toast.LENGTH_SHORT).show();
                }
                if(!otp.equals(OTPNUMBER))
                {
                    Toast.makeText(AuthForgotPass.this,"Please enter Valid otp",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent io = new Intent(AuthForgotPass.this, userForgotPass.class);
                    io.putExtra("usernameoffpass",use);
                    startActivity(io);
                    finish();

                }
            }
        });
    }

    private void sendMessage(){


        String username = ed1.getText().toString();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String url = UrlLinks.AuthForgot;

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

        JSONObject result = null;

        try {
            HttpGetRequest getRequest = new HttpGetRequest();
            //Perform the doInBackground method, passing in our url


            Map<String, String> request = new HashMap<String, String>();
            request.put("username",username);
            String result1 = "";


            String dataurl = buildSanitizedRequest(url, request);
            result1 = getRequest.execute(dataurl).get();
            result = new JSONObject(result1);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
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
//                System.out.println("*****JARRAY*****" + jArray.length());
//
//                for (int i = 0; i < jArray.length(); i++) {

        String Data = "";
        JSONObject json_data;

        try {
//                        json_data = jArray.getJSONObject(i);
            //  String bookName = result.getString("bookName");

            // JSONArray jArray = null;
            //  jArray = result.getJSONArray("bookName");
            System.out.println("*****JARRAY*****" + jArray);

            for (int k = 0; k < jArray.length(); k++) {
                json_data = jArray.getJSONObject(k);
                MOBNO = json_data.getString("phone");


            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String upno = MOBNO;
        String message = "Your OTP for Transport app is "+ OTPNUMBER;
        if(username.equals("")){
            Toast.makeText(getApplicationContext(),"Please enter username.",Toast.LENGTH_SHORT).show();
        }
        else {

            StrictMode.ThreadPolicy policy1 = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy1);
            String url1 = UrlLinks.Checkuserforpass;

            List<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(1);

            nameValuePairs1.add(new BasicNameValuePair("username", username));


            JSONObject result1 = null;
            try {
                result1 = jSOnClassforData.forCallingServer(url1, nameValuePairs1);
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


            JSONArray jArray1 = null;
            try {
                jArray1 = result1.getJSONArray("jsonarrayval");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//
//                    System.out.println("*****JARRAY*****" + jArray.length());
//
//                    for (int i = 0; i < jArray.length(); i++) {


            JSONObject json_data1;

            try {
                json_data1 = jArray1.getJSONObject(0);
                String bookName = json_data1.getString("bookName");
//                        String author = json_data.getString("author");
//                        String publisher = json_data.getString("publisher");

                if (bookName.equals("1")) {

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(upno,null,message,null,null);
                    Toast.makeText(getApplicationContext(),"Message Sent",Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(AuthForgotPass.this, "Wrong username", Toast.LENGTH_SHORT).show();

                }


                //  SplittingBooktime=bookName.split(",");

//							 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//					        		 R.layout.textview, SplittingBooktime);


                //  Toast.makeText(SelectingLcoation.this,"Doctor Available at "+ bookName, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode ==100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            sendMessage();
        }else {
            Toast.makeText(getApplicationContext(),"Permission Denied !",Toast.LENGTH_SHORT).show();
        }
    }

    private static String buildSanitizedRequest(String url, Map<String, String> mapOfStrings) {

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.encodedPath(url);
        if (mapOfStrings != null) {
            for (Map.Entry<String, String> entry : mapOfStrings.entrySet()) {
                Log.d("buildSanitizedRequest", "key: " + entry.getKey()
                        + " value: " + entry.getValue());
                uriBuilder.appendQueryParameter(entry.getKey(),
                        entry.getValue());
            }
        }
        String uriString;
        try {
            uriString = uriBuilder.build().toString(); // May throw an
            // UnsupportedOperationException
        } catch (Exception e) {
            Log.e("Exception", "Exception" + e);
        }

        return uriBuilder.build().toString();

    }
    public class HttpGetRequest extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "POST";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... params){

            String stringUrl = params[0];
            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            }
            catch(IOException e){
                e.printStackTrace();
                result = null;
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
//            if(prDialog!=null) {
//                prDialog.dismiss();
//            }

        }
    }
}