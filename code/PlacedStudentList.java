package com.great3.smartactivatedcollegewebbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

public class PlacedStudentList extends AppCompatActivity {

    ListView placedList;
    String batch = "";
    boolean flag = true;
    ArrayList<String> details = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_student_list);

        Bundle extra = getIntent().getExtras();
        batch = extra.getString("batch");



        Toast.makeText(getApplicationContext(),batch,Toast.LENGTH_LONG).show();

        placedList = (ListView)findViewById(R.id.placed_student_list);

        details.add(" $$$ $$$ $$$ ");
        call();

        Firebase mRefPlacedList = new Firebase("https://sacw-signup.firebaseio.com/KSRCOLLEGEOFTECHNOLOGY/placedstudent/"+batch);

        mRefPlacedList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                String det = dataSnapshot.getValue(String.class);
                details.add(det);
                call();

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
    public void call()
    {
        if(details.size()>1&&flag)
        {
            flag=false;
            details.remove(0);
        }
        String web[] = details.toArray(new String[0]);
        final Custom_placed_list adapter = new Custom_placed_list(PlacedStudentList.this, web);
        placedList.setAdapter(adapter);
    }
}
