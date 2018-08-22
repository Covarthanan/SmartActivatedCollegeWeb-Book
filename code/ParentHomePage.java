package com.great3.smartactivatedcollegewebbook;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ParentHomePage extends AppCompatActivity {

    TextView timeline;
    TextView complaints;

    String reg="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home_page);

        Bundle extra = getIntent().getExtras();
        reg = extra.getString("reg");
        timeline = (TextView)findViewById(R.id.parent_timeline_tab);
        complaints = (TextView)findViewById(R.id.parent_complaints_tab);

        timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                startActivity(new Intent(getApplicationContext(),ParentTimeline.class));


            }
        });

        complaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                startActivity(new Intent(getApplicationContext(),ParentComplaints.class).putExtra("reg",reg));

            }
        });


    }
}
