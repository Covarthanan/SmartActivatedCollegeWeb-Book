package com.great3.smartactivatedcollegewebbook;


import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Glenn on 4/5/2018.
 */
public class ParentStart extends AppCompatActivity {


    //globe for firebase link

    String register_no = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo_layout);

        startService(new Intent(getBaseContext(),ParentsNotificationService.class));

        new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                File profile_path = new File (Environment.getExternalStorageDirectory(),"/SmartActivatedCollegeWebBook/.parents/");
                if (!profile_path.exists())
                {
                    profile_path.mkdirs();
                }

                File file = new File(profile_path,"regno.txt");
                if(!file.exists())
                {
                    //if not logged in
                    startActivity(new Intent(getApplicationContext(),ParentLogin.class));
                    finish();
                }
                else
                {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
                        String reg = bufferedReader.readLine();
                        Toast.makeText(getApplicationContext(),reg,Toast.LENGTH_SHORT).show();

                        if (reg.equals(null))
                        {
                            startActivity(new Intent(getApplicationContext(),ParentLogin.class));
                        }
                        else
                        {
                            startActivity(new Intent(getApplicationContext(),ParentHomePage.class).putExtra("reg",reg));
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }
}
