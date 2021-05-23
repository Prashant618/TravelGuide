package com.thecodecity.mapsdirection;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

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
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SelectingLcoation extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
//EditText et1;
Button b1;
    double latitude1 ;
    double longitude1;
   static String latilongisrc="";
    static String latilongidst="";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String TAG = "SelectingLcoation";
    private Location mLastLocation;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    // Button btnd;
    private boolean READ_PHONE_STATE_granted = false;
    private boolean mRequestingLocationUpdates = false;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    public static String locationofuser="";
    TextView locationofuserview;
    static String bookName="";
    public static String busnumber="";
    static JSONObject jsonObj = null;
    public static  ArrayList<String> latilongidata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecting_lcoation);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner3);
//et1=(EditText)findViewById(R.id.editText);
      //  et2=(EditText)findViewById(R.id.editText2);
        b1=(Button)findViewById(R.id.button2);
        locationofuserview=(TextView)findViewById(R.id.textView3);

        String[] sourceArray = {"387","302","303","511","27","545","525","07","489"};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sourceArray);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        getLocationPermission();
        createLocationRequest();
        buildGoogleApiClient();



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String busplatenumber=spinner.getSelectedItem().toString();
                busnumber=busplatenumber;
                //String dst=et2.getText().toString();
                if(isServicesOK()) {

                    latilongisrc =latitude1+","+longitude1;// geoLocate(src);
                    latilongidst = getLattitudeAndLongitudebyplate(busplatenumber);
                }

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                String url = UrlLinks.getNotify;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

                String username = spinner.getSelectedItem().toString();

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
                        String doctorname = json_data.getString("longitude");
                        String availability = json_data.getString("latitude");
//                Data = Data + "" + doctorname + "" + "\n" + "" + availability + "" + "\n" + "" + operation + "" + "\n" + "" + education + "" + "\n" + "" + about + "" + "\n";

//                String stUrl =UrlLinks.urlserver+"/userimage/1.png";
                        Intent I = new Intent(getApplicationContext(),MapActivity.class);
                        I.putExtra("name",doctorname);
                        I.putExtra("pass",availability);
                        startActivity(I);
//                LoadImage loadImage = new LoadImage();
//                Bitmap bmp= loadImage.execute(stUrl);



//                        exampleList.add(new ExampleItem1(doctorname,availability,operation,education,time));
                        // lstBook.add(new Book(alldata,operation,"Description book", R.drawable.ic_launcher_background));


                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });


    }

    public static  String getLattitudeAndLongitudebyplate(String busplatenumber) {
        latilongidata=new ArrayList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String url= UrlLinks.getdataforbus;

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

        nameValuePairs.add(new BasicNameValuePair("busplatenumber",busplatenumber));



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
                bookName=json_data.getString("studentname");
                latilongidata.add(bookName);
               // return bookName;
//
//



            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }



        return bookName;
    }

    private String geoLocate(String src){
        Log.d(TAG, "geoLocate: geolocating");
        String latitlongi="";
        String searchString = src;

        Geocoder geocoder = new Geocoder(SelectingLcoation.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);
            double latiobt=address.getLatitude();
            double longiobt=address.getLongitude();
             latitlongi=latiobt+","+longiobt;
            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

        }
        return latitlongi;
    }
    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;

            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(SelectingLcoation.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(SelectingLcoation.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }
    private void displayLocation() {
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
             latitude1 = mLastLocation.getLatitude();
             longitude1 = mLastLocation.getLongitude();
            try {
                Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
                String result = null;
                List<Address> addressList = geocoder.getFromLocation(
                        latitude1, longitude1, 1);
                if (addressList != null && addressList .size() > 0) {
                    Address address = addressList .get(0);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address .getMaxAddressLineIndex(); i++) {
                        sb.append(address .getAddressLine(i)).append("\n");
                    }

                    String add=  address.getAddressLine(0);

                    //sb.append(address .getLocality()).append("\n");
                    //sb.append(address .getPostalCode()).append("\n");
                    //sb.append(address .getCountryName());
                    result = add.toString();
                    locationofuser=add;
                    locationofuserview.setText(""+locationofuser);
                    Log.i("result","result "+result);
                    //postUserDetails(result);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
            // Log.i(TAG, "Location not found. Make sure location is enabled on the device");
        }
    }
    protected void startLocationUpdates() {

//        LocationServices.FusedLocationApi.requestLocationUpdates(
//                mGoogleApiClient, mLocationRequest,this);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);

    }
    @Override
    protected void onStart() {
        super.onStart();
        try{
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            checkPlayServices();
            // Resuming the periodic location updates
            if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
                startLocationUpdates();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try{
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                // Log.i(TAG, "Google play services not supported in this device");
                //Toast.makeText(getApplicationContext(), "Google play services not supported in this device", Toast.LENGTH_LONG)
                //  .show();
                // finish();
            }
            return false;
        }
        return true;
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(SelectingLcoation.this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
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
