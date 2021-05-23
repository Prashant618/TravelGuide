package com.thecodecity.mapsdirection;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

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

public class TravelHistory extends AppCompatActivity {
    EditText Searchtext;
    private ExampleAdapter1 adapter;
    ImageButton bt_mic;
    private List<ExampleItem1> exampleList;
    private List<ExampleItem1> examples;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_history);

        fillExampleList();
        setUpRecyclerView();
        initToolbar();
        this.Searchtext = (EditText) findViewById(R.id.search_input);
        this.Searchtext.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                filterQuery(editable.toString());
            }
        });
    }

    private void fillExampleList() {
        exampleList = new ArrayList();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String url = UrlLinks.travelhistory;

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
                String doctorname = json_data.getString("username");
                String availability = json_data.getString("phone");
                String operation = json_data.getString("busno");
                String education = json_data.getString("Destination");
                String time = json_data.getString("time");
//                Data = Data + "" + doctorname + "" + "\n" + "" + availability + "" + "\n" + "" + operation + "" + "\n" + "" + education + "" + "\n" + "" + about + "" + "\n";

//                String stUrl =UrlLinks.urlserver+"/userimage/1.png";

//                LoadImage loadImage = new LoadImage();
//                Bitmap bmp= loadImage.execute(stUrl);


                exampleList.add(new ExampleItem1("Name:"+doctorname,"Mob No:"+availability,"Bus no:"+operation,"Desti"+education,"Time:"+time));
               // exampleList.add(new ExampleItem1(doctorname,availability,operation,education,time));
                // lstBook.add(new Book(alldata,operation,"Description book", R.drawable.ic_launcher_background));


            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById( R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        this.adapter = new ExampleAdapter1(exampleList,getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(this.adapter);
    }

    private void initToolbar() {
//        setSupportActionBar((Toolbar) findViewById( R.id.toolbar));
//        getSupportActionBar().setTitle((CharSequence) "Filter Activity");
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /* access modifiers changed from: private */
    public void filterQuery(String text) {
        ArrayList<ExampleItem1> filterdNames = new ArrayList<>();
        for (ExampleItem1 s : exampleList) {
            if (s.getmText3().toLowerCase().contains(text) || s.getmText4().toLowerCase().contains(text)) {
                filterdNames.add(s);
            }
        }
        this.adapter.setFilter(filterdNames);
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