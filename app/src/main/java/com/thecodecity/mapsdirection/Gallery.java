package com.thecodecity.mapsdirection;

import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Gallery extends AppCompatActivity {
    TextView tx1,tx2,tx3,tx4,tx5;
    Button button,button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        tx1 = findViewById(R.id.tx1);
        tx2 = findViewById(R.id.tx2);
        tx3 = findViewById(R.id.tx3);
        tx4 = findViewById(R.id.tx4);
        tx5 = findViewById(R.id.tx5);
//        button = findViewById(R.id.btnbook);
        button1 = findViewById(R.id.gpay);

        final String no = getIntent().getStringExtra("no");
        String plate = getIntent().getStringExtra("plate");
        final String dest = getIntent().getStringExtra("des");
        final String time = getIntent().getStringExtra("time");

        Random random = new Random();
        int val = random.nextInt(60);
        final String number = Integer.toString(val);
        System.out.println("Prize " + number);

        tx1.setText(no);
        tx2.setText(plate);
        tx3.setText(dest);
        tx4.setText(time);
        tx5.setText("Rs."+number);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent io = new Intent(Gallery.this,Payment.class);
//                io.putExtra("busno",no);
//                io.putExtra("destination",dest);
//                io.putExtra("timer",time);
//                io.putExtra("prize1",number);
//                startActivity(io);
//            }
//        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymetusingGpay();
            }
        });
    }
    private void paymetusingGpay(){
        String GOOGLE_PAY_PACKAGE = "com.google.android.apps.nbu.paisa.user";

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", "9182dawinder@oksbi")
                .appendQueryParameter("pn", "Transport App payment")
                .appendQueryParameter("tn", "Payment just for testing")
                .appendQueryParameter("am", "1")
                .appendQueryParameter("cu", "INR")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        upiPayIntent.setPackage(GOOGLE_PAY_PACKAGE);
        try {
            startActivityForResult(upiPayIntent,101);
        }catch (Exception e){
            Toast.makeText(Gallery.this,"Google pay app not found",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101)
        {
            if(requestCode == RESULT_OK)
            {
                if(data != null)
                {
                    String value = data.getStringExtra("response");
                    ArrayList<String> list = new ArrayList<>();
                    list.add(value);
                    getStatus(list.get(0));

                }
            }else {
                Toast.makeText(Gallery.this,"Payment failed",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getStatus(String data){
        boolean ispaymentcancle=false;
        boolean ispaymentsuccess=false;
        boolean paymentfailed=false;
        String value[]=data.split("&");
        for (int i= 0;i<value.length;i++){
            String checkString[] = value[i].split("=");
            if(checkString.length>=2){
                if(checkString[0].toLowerCase().equals("status")) {
                    if(checkString[1].equals("success")){
                        ispaymentsuccess=true;
                    }
                }
            }else {
                ispaymentcancle=true;
            }
        }
        if(ispaymentsuccess){

            String username = userLogin.userdata;
            String b1 = tx1.getText().toString();
            String d1 = tx3.getText().toString();
            String t1 = tx4.getText().toString();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String url = UrlLinks.payment;

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);

            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("busnumber", b1));
            nameValuePairs.add(new BasicNameValuePair("destinationofbus", d1));
            nameValuePairs.add(new BasicNameValuePair("timeofbus", t1));


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

                    Toast.makeText(Gallery.this, "Payment sucessfull", Toast.LENGTH_SHORT).show();


                } else {

                    Toast.makeText(Gallery.this, "Wrong details", Toast.LENGTH_SHORT).show();

                }


                //  SplittingBooktime=bookName.split(",");

//							 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//					        		 R.layout.textview, SplittingBooktime);


                //  Toast.makeText(SelectingLcoation.this,"Doctor Available at "+ bookName, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Toast.makeText(Gallery.this,"Payment successful",Toast.LENGTH_SHORT).show();
        }else if(ispaymentcancle){
            Toast.makeText(Gallery.this,"Payment cancled",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(Gallery.this,"Payment failed",Toast.LENGTH_SHORT).show();
        }
    }
}