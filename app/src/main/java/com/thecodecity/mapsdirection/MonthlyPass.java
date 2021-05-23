package com.thecodecity.mapsdirection;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MonthlyPass extends AppCompatActivity {
    EditText ed1,ed2,ed3,ed4;
    Spinner spin1,spin2;
    Button btn;
    TextView tx;
    public static String doctorname = "";
    public static String operation = "";
    public static String education = "";
    public static String availability = "";
    public static String time = "";
    public static String about = "";
    public static String contact = "";
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_pass);

        ed1 = findViewById(R.id.editText);
        ed2 = findViewById(R.id.editText1);
        ed3 = findViewById(R.id.editText2);
        ed4 = findViewById(R.id.editText3);
        spin1 = findViewById(R.id.spin);
        spin2 = findViewById(R.id.spin1);
        btn = findViewById(R.id.button);
        tx = findViewById(R.id.textView1);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.source, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.destination, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(adapter1);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        ed3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MonthlyPass.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayofmonth) {
                        String date = month + 1 + "/" + dayofmonth + "/" + year;
                        ed3.setText(date);

                        dayofmonth = dayofmonth - 1;
                        String date1 = month + 2 + "/" + dayofmonth  + "/" + year;
                        ed4.setText(date1);

                    }
                }, year, month, day);
                datePickerDialog.getDatePicker();
                datePickerDialog.show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String no = ed1.getText().toString();
                String plateno = ed2.getText().toString();
                String source = spin1.getSelectedItem().toString();
                String destination = spin2.getSelectedItem().toString();
                String from = ed3.getText().toString();
                String to = ed4.getText().toString();

                if (no.equals("") || plateno.equals("") || source.equals("") || destination.equals("")
                        || from.equals("") || to.equals("")) {
                    Toast.makeText(MonthlyPass.this, "Please enter details.", Toast.LENGTH_SHORT).show();
                }
                else {
                    paymetusingGpay();
                }

            }
        });

        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                String url = UrlLinks.Viewpass;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

                String username = userLogin.userdata;

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
                        doctorname = json_data.getString("username");
                        availability = json_data.getString("busno");
                        operation = json_data.getString("busplate");
                        education = json_data.getString("source");
                        time = json_data.getString("Destination");
                        about = json_data.getString("from");
                        contact = json_data.getString("to");
               Data = Data + "" + doctorname + "" + "\n" + "" + availability + "" + "\n" + "" + operation + "" + "\n" + "" + education + "" + "\n" + "" + about + "" + "\n";

//                String stUrl =UrlLinks.urlserver+"/userimage/1.png";


//                LoadImage loadImage = new LoadImage();
//                Bitmap bmp= loadImage.execute(stUrl);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if(Data.equals("")){
                    Toast.makeText(MonthlyPass.this, "There is no pass record for this user", Toast.LENGTH_SHORT).show();
                }else {
                    Intent io = new Intent(MonthlyPass.this, ViewPass.class);
                    io.putExtra("username",doctorname);
                    io.putExtra("busnumber",availability);
                    io.putExtra("busplate1",operation);
                    io.putExtra("source1",education);
                    io.putExtra("destination1",time);
                    io.putExtra("from1",about);
                    io.putExtra("to1",contact);
                    startActivity(io);
                }

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
            Toast.makeText(MonthlyPass.this,"Google pay app not found",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MonthlyPass.this,"Payment failed",Toast.LENGTH_SHORT).show();
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
            String no = ed1.getText().toString();
            String plateno = ed2.getText().toString();
            String source = spin1.getSelectedItem().toString();
            String destination = spin2.getSelectedItem().toString();
            String from = ed3.getText().toString();
            String to = ed4.getText().toString();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                String url = UrlLinks.Monthlypass;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);

                nameValuePairs.add(new BasicNameValuePair("username", username));
                nameValuePairs.add(new BasicNameValuePair("no", no));
                nameValuePairs.add(new BasicNameValuePair("plate", plateno));
                nameValuePairs.add(new BasicNameValuePair("source", source));
                nameValuePairs.add(new BasicNameValuePair("destination", destination));
                nameValuePairs.add(new BasicNameValuePair("from", from));
                nameValuePairs.add(new BasicNameValuePair("to", to));


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

                        Toast.makeText(MonthlyPass.this, "Monthly pass renewed", Toast.LENGTH_SHORT).show();
                        Intent io = new Intent(MonthlyPass.this, SimpleActivityForStart.class);

                        startActivity(io);

                    } else {

                        Toast.makeText(MonthlyPass.this, "Your pass is already renewed", Toast.LENGTH_SHORT).show();

                    }


                    //  SplittingBooktime=bookName.split(",");

//							 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//					        		 R.layout.textview, SplittingBooktime);


                    //  Toast.makeText(SelectingLcoation.this,"Doctor Available at "+ bookName, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }



            Toast.makeText(MonthlyPass.this,"Payment successful",Toast.LENGTH_SHORT).show();
        }else if(ispaymentcancle){
            Toast.makeText(MonthlyPass.this,"Payment cancled",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(MonthlyPass.this,"Payment failed",Toast.LENGTH_SHORT).show();
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
        protected String doInBackground(String... params) {

            String stringUrl = params[0];
            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection = (HttpURLConnection)
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
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                result = null;
            }
            return result;
        }
    }

}