package com.great3.smartactivatedcollegewebbook;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ParentComplaints extends AppCompatActivity {

    TextView voice_complaints;

    ListView comList;
    ArrayList<String> complaints = new ArrayList<>();

    ArrayList count = new ArrayList();
    String count_str;

    int flag = 0;
    String reg="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_complaints);

        voice_complaints = (TextView)findViewById(R.id.voice_complaints_btn);

        Bundle extra = getIntent().getExtras();
        reg = extra.getString("reg");

        voice_complaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),ParentsVoiceComplaints.class).putExtra("reg",reg));
            }
        });

        final Firebase mRef = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/PARENTS/"+reg);
        comList = (ListView)findViewById(R.id.complaint_listview);

        final ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.custom_complaint_box,R.id.comp_box,complaints);
        comList.setAdapter(adapter);

        final Firebase mRef_count = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/PARENTS/"+reg+"/");

        mRef_count.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                count.add(dataSnapshot.getValue());

                count_str = ""+count.size();

                File dir_complaint = new File(Environment.getExternalStorageDirectory(), "/SmartActivatedCollegeWebBook/.parents/");

                if (!dir_complaint.exists()) {
                    dir_complaint.mkdirs();
                }

                File file_complaint = new File(dir_complaint, "count.txt");

                if (file_complaint.exists()) {
                    try {

                        FileOutputStream fileOutputStream_complaint = new FileOutputStream(file_complaint);
                        fileOutputStream_complaint.write(count_str.getBytes());
                        fileOutputStream_complaint.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        file_complaint.createNewFile();
                        FileOutputStream fileOutputStream_complaint = new FileOutputStream(file_complaint);
                        fileOutputStream_complaint.write(count_str.getBytes());
                        fileOutputStream_complaint.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String co = dataSnapshot.getValue(String.class);
                complaints.add(co);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }
}
