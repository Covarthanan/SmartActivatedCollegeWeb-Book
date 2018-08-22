package com.great3.smartactivatedcollegewebbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

public class ShowSearch extends AppCompatActivity {


    //Profile List
    //  <<-- Start
    ImageView backFromProfileView;
    ImageView profileView;
    TextView profileName;
    TextView profileReg;
    TextView profileAddress;
    TextView profileBatch;
    TextView profileEmail;
    TextView profileFatherName;
    TextView profileFatherPhone;
    TextView profileMotherName;
    TextView profileMotherPhone;
    TextView profileGender;
    TextView profilePhone;

    // End-->>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_search);


        Bundle extra = getIntent().getExtras();
        String department = extra.getString("dept");
        String batchYear = extra.getString("batch");
        final String registerNumber = extra.getString("reg");

        //ProfileView
        profileView = (ImageView) findViewById(R.id.search_image);
        profileName = (TextView)findViewById(R.id.search_view_name);
        profileReg = (TextView)findViewById(R.id.search_view_reg);
        profileAddress = (TextView)findViewById(R.id.search_view_address);
        profileBatch =(TextView)findViewById(R.id.search_view_batch);
        profileEmail = (TextView)findViewById(R.id.search_view_email);
        profileFatherName = (TextView)findViewById(R.id.search_view_father_name);
        profileFatherPhone = (TextView)findViewById(R.id.search_view_father_phone);
        profileMotherName = (TextView)findViewById(R.id.search_view_mother_name);
        profileMotherPhone = (TextView)findViewById(R.id.search_view_mother_phone);
        profileGender = (TextView)findViewById(R.id.search_view_gender);
        profilePhone = (TextView)findViewById(R.id.search_view_phone);


        Firebase acc = new Firebase("https://sacw-signup.firebaseio.com/KSRCOLLEGEOFTECHNOLOGY/"+department+"/"+batchYear+"/"+registerNumber+"/");

        acc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String,String> map = dataSnapshot.getValue(Map.class);
                String s = map +"";
                if(!s.equals("null")) {
                    profileName.setText("Name   :  " + map.get("name"));
                    profileReg.setText("Register Number :  " + registerNumber);
                    profileAddress.setText("Address :  " + map.get("address"));
                    profileBatch.setText("Batch :  " + map.get("batch"));
                    profileEmail.setText("Email :  " + map.get("email"));
                    profileFatherName.setText("Father Name  :  " + map.get("fathername"));
                    profileFatherPhone.setText("Father Phone :  " + map.get("fatherphone"));
                    profileMotherName.setText("Mother Name  :  " + map.get("mothername"));
                    profileMotherPhone.setText("Mother Phone :  " + map.get("motherphone"));
                    profileGender.setText("Gender   :  " + map.get("gender"));
                    profilePhone.setText("Phone :  " + map.get("phone"));
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid Entry",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
}
