package com.thecodecity.mapsdirection;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class userLogin extends AppCompatActivity {
    EditText uname,pass;
    Button log;
    TextView newUser,txNext;
    public static String userdata="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        uname = findViewById(R.id.editText1);
        pass = findViewById(R.id.editText2);
        log = findViewById(R.id.button1);
        newUser = findViewById(R.id.textView2);
        txNext = findViewById(R.id.textView3);

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(userLogin.this,userRegister.class);
                startActivity(io);
            }
        });

        txNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io1 = new Intent(userLogin.this,AuthForgotPass.class);
                startActivity(io1);
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = uname.getText().toString();
                String passward = pass.getText().toString();

                if (username.equals("") || passward.equals("")) {
                    Toast.makeText(userLogin.this, "Please fill login details", Toast.LENGTH_SHORT).show();

                } else {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String url = UrlLinks.checklogin;

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                    nameValuePairs.add(new BasicNameValuePair("username", username));
                    nameValuePairs.add(new BasicNameValuePair("passward", passward));


                    JSONObject result = null;
                    try {
                        result = jSOnClassforData.forCallingServer(url, nameValuePairs);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


//
//        for(int i=0;i<jArray.length();i++) {
//            String alldata = jArray.get(0).toString();
//
//            String datasplit[] = alldata.split("_");
//            latilongidata.add(alldata);
//
//
//
//
//        }


                    JSONArray jArray = null;
//                try {
                    try {
                        jArray = result.getJSONArray("jsonarrayval");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//                System.out.println("*****JARRAY*****" + jArray.length());
//
//                for (int i = 0; i < jArray.length(); i++) {


                    JSONObject json_data;

                    try {
                        json_data = jArray.getJSONObject(0);
                        String bookName = json_data.getString("bookName");
//                        String author = json_data.getString("author");
//                        String publisher = json_data.getString("publisher");

                        if (bookName.equals("1")) {
                            userdata = username;
                            Intent io = new Intent(userLogin.this, SimpleActivityForStart.class);
                            Toast.makeText(userLogin.this, "Welcome "+username, Toast.LENGTH_SHORT).show();
                            startActivity(io);

                            // NavController navController= Navigation.findNavController(view);
                            // navController.navigate(R.id.nav_slideshow);


                        } else {

                            Toast.makeText(userLogin.this, "Wrong username or password", Toast.LENGTH_SHORT).show();

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
}