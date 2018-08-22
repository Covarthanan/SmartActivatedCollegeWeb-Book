package com.great3.smartactivatedcollegewebbook;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Complaints extends AppCompatActivity {

    EditText text;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);
        Bundle extra = getIntent().getExtras();
        final String reg = extra.getString("reg");
        TextView voice = (TextView)findViewById(R.id.voice_complaints);

        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),SelectTypeOfComplaints.class).putExtra("reg",reg));

            }
        });

        text = (EditText)findViewById(R.id.complaint_text);
        send= (Button)findViewById(R.id.complaint_send_button);

        final Firebase mRef = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/PARENTS/"+reg);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);


                File profile_path = new File (Environment.getExternalStorageDirectory(),"/SmartActivatedCollegeWebBook/Profile/");
                if (!profile_path.exists())
                {
                    profile_path.mkdirs();
                }

                File file1 = new File(profile_path,"type.txt");
                if(file1.exists())
                {
                    FileInputStream fis1 = null;
                    try {
                        fis1 = new FileInputStream(file1);

                        DataInputStream in1 = new DataInputStream(fis1);
                        BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
                        String type= null;
                        try {
                            type = br1.readLine();

                            if(type.equals("student"))
                            {
                                Toast.makeText(getApplicationContext(),"Only staffs are allowed to add complaints",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String com = text.getText().toString();

                                if(!com.equals(""))
                                {
                                    mRef.child(System.currentTimeMillis()+"").setValue(com);
                                    text.setText("");
                                    finish();

                                }

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }
}
