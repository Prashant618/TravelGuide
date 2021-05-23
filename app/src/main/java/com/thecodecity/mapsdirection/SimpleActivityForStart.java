package com.thecodecity.mapsdirection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SimpleActivityForStart extends AppCompatActivity {
Button b,b3,b4,b5,b6,b7,b8,b9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_for_start);
        b=(Button)findViewById(R.id.button);
        b3=(Button)findViewById(R.id.button3);
        b4=(Button)findViewById(R.id.button4);
        b5=(Button)findViewById(R.id.button5);
        b6=(Button)findViewById(R.id.button1);
        b7=(Button)findViewById(R.id.button6);
        b8=(Button)findViewById(R.id.button7);
        b9=(Button)findViewById(R.id.button8);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent busroute=new Intent(SimpleActivityForStart.this,RouteviewActivity.class);
//                busroute.putExtra("path","");
                startActivity(busroute);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent busroute=new Intent(SimpleActivityForStart.this,GooglePlacesActivity.class);
//                busroute.putExtra("path","");
                startActivity(busroute);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent busroute=new Intent(SimpleActivityForStart.this,SelectingSourceDestination.class);
//                busroute.putExtra("path","");
                startActivity(busroute);
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent busroute=new Intent(SimpleActivityForStart.this,userUpdate.class);
//                busroute.putExtra("path","");
                startActivity(busroute);
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent busroute=new Intent(SimpleActivityForStart.this,userBookbus.class);
//                busroute.putExtra("path","");
                startActivity(busroute);
            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent busroute=new Intent(SimpleActivityForStart.this,TravelHistory.class);
//                busroute.putExtra("path","");
                startActivity(busroute);
            }
        });
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent busroute=new Intent(SimpleActivityForStart.this,MonthlyPass.class);
//                busroute.putExtra("path","");
                startActivity(busroute);
            }
        });


    }
}
