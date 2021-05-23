package com.thecodecity.mapsdirection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class userRegister extends AppCompatActivity {
    EditText edUname,edPass,edEmail,edMob,edAdd,edOtp;
    Button btn,btn1;
    public static String OTPNUMBER="";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        edUname = findViewById(R.id.editText);
        edPass = findViewById(R.id.editText1);
        edEmail = findViewById(R.id.editText2);
        edMob = findViewById(R.id.editText3);
        edAdd = findViewById(R.id.editText4);
        edOtp = findViewById(R.id.editText5);
        btn = findViewById(R.id.button);

        Random random = new Random();
        int val = random.nextInt(1000000);
        String number = Integer.toString(val);
        OTPNUMBER = number;


        btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(userRegister.this, Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED){
                    sendMessage();

                }else {
                    ActivityCompat.requestPermissions(userRegister.this,new String[]{Manifest.permission.SEND_SMS},100);
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = edUname.getText().toString();
                String passward = edPass.getText().toString();
                String email = edEmail.getText().toString();
                String phone = edMob.getText().toString();
                String address = edAdd.getText().toString();
                String otp = edOtp.getText().toString();



                if(username.equals("")||passward.equals("")||email.equals("")||phone.equals("")||address.equals("")) {
                    Toast.makeText(userRegister.this, "Please enter details.", Toast.LENGTH_SHORT).show();
                }
                if (!email.matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(),"Envalid email address",Toast.LENGTH_SHORT).show();
                }
                if(otp.equals(""))
                {
                    Toast.makeText(userRegister.this,"Please verify OTP.",Toast.LENGTH_SHORT).show();
                }

                else if(!otp.equals(OTPNUMBER))
                {
                    Toast.makeText(userRegister.this,"Please enter Valid otp",Toast.LENGTH_SHORT).show();
                }
////                else if (!email.matches(emailPattern) )
////                {
////                    Toast.makeText(getApplicationContext(),"invalid email address",Toast.LENGTH_SHORT).show();
////                    // or
////                    // textView.setText("valid email");
////                }
//                else if (address.length()<10)
//                {
//                    Toast.makeText(getApplicationContext(),"Please insert proprer address",Toast.LENGTH_SHORT).show();
//                    // or
//                    // textView.setText("valid email");
//                }
                else {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String url = UrlLinks.checkregistration;

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);

                    nameValuePairs.add(new BasicNameValuePair("username", username));
                    nameValuePairs.add(new BasicNameValuePair("passward", passward));
                    nameValuePairs.add(new BasicNameValuePair("email", email));
                    nameValuePairs.add(new BasicNameValuePair("phone", phone));
                    nameValuePairs.add(new BasicNameValuePair("address", address));


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

                            Toast.makeText(userRegister.this, "User Added successfully", Toast.LENGTH_SHORT).show();
                            Intent io = new Intent(userRegister.this, userLogin.class);

                            startActivity(io);
                            finish();

                        } else {

                            Toast.makeText(userRegister.this, "Wrong username or password", Toast.LENGTH_SHORT).show();

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
        });

    }
    private void sendMessage(){

        String upno = edMob.getText().toString().trim();
        String message = "Your OTP for Transport app is "+ OTPNUMBER;
        if(OTPNUMBER.equals("")||upno.equals("")){
            Toast.makeText(getApplicationContext(),"Fill details",Toast.LENGTH_SHORT).show();
        }else {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(upno,null,message,null,null);
            Toast.makeText(getApplicationContext(),"Message Sent",Toast.LENGTH_SHORT).show();

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
}