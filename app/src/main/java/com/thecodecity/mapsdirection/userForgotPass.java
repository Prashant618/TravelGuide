package com.thecodecity.mapsdirection;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class userForgotPass extends AppCompatActivity {
    EditText edPass,edCpass;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_forgot_pass);

        edPass = findViewById(R.id.editText1);
        edCpass = findViewById(R.id.editText2);
        btn = findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = getIntent().getStringExtra("usernameoffpass");
                String passward = edPass.getText().toString();
                String cpassward = edCpass.getText().toString();

                if(passward.equals("")||cpassward.equals("")) {
                    Toast.makeText(userForgotPass.this, "Please enter details.", Toast.LENGTH_SHORT).show();
                }
                if(!passward.equals(cpassward))
                {
                    Toast.makeText(userForgotPass.this,"Passward and Cpassword should be same.",Toast.LENGTH_SHORT).show();
                }
//                else if()
//                {
//                    Toast.makeText(userForgotPass.this,"Password must be of 8 alphabates",Toast.LENGTH_SHORT).show();
//                }
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
                    String url = UrlLinks.forgotPass;

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

                    nameValuePairs.add(new BasicNameValuePair("username", username));
                    nameValuePairs.add(new BasicNameValuePair("passward", passward));
                    nameValuePairs.add(new BasicNameValuePair("cpassward", cpassward));


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

                            Toast.makeText(userForgotPass.this, "Password Updated successfully", Toast.LENGTH_SHORT).show();
                            Intent io = new Intent(userForgotPass.this, userLogin.class);

                            startActivity(io);
                            finish();

                        } else {

                            Toast.makeText(userForgotPass.this, "Wrong username", Toast.LENGTH_SHORT).show();

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