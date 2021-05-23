package com.thecodecity.mapsdirection;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompatBase;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.thecodecity.mapsdirection.directionhelpers.DataParser;
import com.thecodecity.mapsdirection.directionhelpers.FetchURL;
import com.thecodecity.mapsdirection.directionhelpers.TaskLoadedCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Vishal on 10/20/2018.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private GoogleMap mMap;
    private MarkerOptions place1, place2,place11;
    Button getDirection;
    private Polyline currentPolyline;
    TextView tvduration,distance;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 10*1000;

    private static final int REQUEST_LOCATION = 1;

    LocationManager locationManager;
    String latitude, longitude;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        tvduration=(TextView)findViewById(R.id.textView4);
        distance=(TextView)findViewById(R.id.textView2);
        getDirection = findViewById(R.id.btnGetDirection);
        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchURL(MapActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
                tvduration.setText(DataParser.timerequired+" minutes ");
                distance.setText("distance is "+DataParser.totaldistance+" meters ");

            }
        });


        //27.658143,85.3199503
        //27.667491,85.3208583
        String srcsplit[]=SelectingLcoation.latilongisrc.split(",");


        place1 = new MarkerOptions().position(new LatLng(Double.parseDouble(srcsplit[0]), Double.parseDouble(srcsplit[1]))).title("Location 1");




        ArrayList<String> latilongidata=SelectingLcoation.latilongidata;
        for(int k=0;k<latilongidata.size();k++)
        {
            String dstsplit[]=latilongidata.get(k).split(",");
            place2 = new MarkerOptions().position(new LatLng(Double.parseDouble(dstsplit[0]), Double.parseDouble(dstsplit[1]))).title("Location 2");


        }
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            OnGPS();
        } else {
            getLocation();
        }


    }
    @Override
    protected void onResume() {

        handler.postDelayed( runnable = new Runnable() {
            public void run() {
                //do something
                SelectingLcoation.getLattitudeAndLongitudebyplate(SelectingLcoation.busnumber);
                ArrayList<String> latilongidata=SelectingLcoation.latilongidata;
                for(int k=0;k<latilongidata.size();k++)
                {
                    String dstsplit[]=latilongidata.get(k).split(",");
                    place2 = new MarkerOptions().position(new LatLng(Double.parseDouble(dstsplit[0]), Double.parseDouble(dstsplit[1]))).title("Location 2");
                    mMap.addMarker(place2);

                }
                new FetchURL(MapActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
                tvduration.setText(DataParser.timerequired+" minutes ");
                distance.setText("distance is "+DataParser.totaldistance+" meters ");
                handler.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();

    }
    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("mylog", "Added Markers");
        mMap.addMarker(place1);
        String srcsplit[]=SelectingLcoation.latilongisrc.split(",");
        LatLng latLng = new LatLng(Double.parseDouble(srcsplit[0]), Double.parseDouble(srcsplit[1]));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        mMap.addMarker(place2);
        ArrayList<String> latilongidata=SelectingLcoation.latilongidata;
        for(int k=0;k<latilongidata.size();k++)
        {
            String dstsplit[]=latilongidata.get(k).split(",");
            place11 = new MarkerOptions().position(new LatLng(Double.parseDouble(dstsplit[0]), Double.parseDouble(dstsplit[1]))).title("Location 2");
            mMap.addMarker(place11);

        }
        //place11 = new MarkerOptions().position(new LatLng(0, 0)).title("Location next");

        String la1 = latitude;
        String lo1 = longitude;
        String la2 = getIntent().getStringExtra("pass");
        String lo2 = getIntent().getStringExtra("name");

        double lat1 = Double.parseDouble(la1);
        double lon1 = Double.parseDouble(lo1);
        double lat2 = Double.parseDouble(la2);
        double lon2 = Double.parseDouble(lo2);

        float[] resuls = new float[1];
        Location.distanceBetween(lat1,lon1,lat2,lon2,resuls);
        float distance = resuls[0];

        float i = 800;
        if(i >= distance) {
            String message = "Your stop is about to come.";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MapActivity.this)
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle("Notification")
                    .setContentText(message)
                    .setAutoCancel(true);


            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0,builder.build());
        }

    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin,15));

        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

            }
            else
            {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }

            //Thats All Run Your App
        }

    }

    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }


}
