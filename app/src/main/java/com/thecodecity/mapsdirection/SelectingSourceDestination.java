package com.thecodecity.mapsdirection;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SelectingSourceDestination extends AppCompatActivity {
    Button b1,b7,b5;
    AlertDialog.Builder builder;
    public static String stringTextView="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecting_source_destination);
        builder = new AlertDialog.Builder(this);
//        builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        b1=(Button)findViewById(R.id.button6);
        b7=(Button)findViewById(R.id.button7);
        b5=(Button)findViewById(R.id.button5);
        String[] sourceArray = {"Dadar","Matunga","Sion","Kurla","Vidyavihar","Ghatkopar","Vikhroli","Kanjurmarg",
        "Bhandup","Nahur","Mulund","Thane"};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sourceArray);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner2.setAdapter(dataAdapter);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String source=spinner.getSelectedItem().toString();
                String destination=spinner2.getSelectedItem().toString();

                    getBusTimings(source,destination);


            }
        });

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent busroute=new Intent(SelectingSourceDestination.this,WebViewActivity.class);
//                busroute.putExtra("path","");
                startActivity(busroute);

            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent busroute=new Intent(SelectingSourceDestination.this,SelectingLcoation.class);
//                busroute.putExtra("path","");
                startActivity(busroute);
            }
        });

    }

    private String getBusTimings(String source, String destination) {
        String bookName="";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String url= UrlLinks.getbustimeable;

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

        nameValuePairs.add(new BasicNameValuePair("source",source));
        nameValuePairs.add(new BasicNameValuePair("destination",destination));

        JSONObject jsonObj=null;
        //jSOnClassforData.forCallingStringBasic(url,nameValuePairs);


        try {
            jsonObj = jSOnClassforData.forCallingServer(url, nameValuePairs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jArray = null;
        try {
            jArray = jsonObj.getJSONArray("jsonarrayval");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("*****JARRAY*****" + jArray.length());

        for (int i = 0; i < jArray.length(); i++) {


            JSONObject json_data;
            try {
                json_data = jArray.getJSONObject(i);
//                bookName=json_data.getString("studentname");
                String busnumber,busplatenumber,uptiming,downtiming,stop;
                busnumber=json_data.getString("busnumber");
                busplatenumber=json_data.getString("busplatenumber");
                uptiming=json_data.getString("stops");
                downtiming=json_data.getString("studentname");
                stop=json_data.getString("stops1");
                // return bookName;
//

                    String msgtoall="bus number is "+busnumber+"\n\n busplatenumber is "+busplatenumber+"\n\n uptime is "+uptiming+"\n\n down timing is "+downtiming+"\n\n Stops are "+stop;
                    builder.setMessage(msgtoall)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
//                                finish();
                                    dialog.cancel();
                                    Toast.makeText(getApplicationContext(),"you choose yes action for alertbox",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Bus Timetable");
                    alert.show();

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return bookName;
    }


}
