package com.great3.smartactivatedcollegewebbook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Glenn on 4/5/2018.
 */
public class ParentLogin extends AppCompatActivity {


    EditText regNo;
    Button login;
    String reg;

    Firebase mLoginRef;

    ArrayList arrayList = new ArrayList();

    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parents_login);

        regNo = (EditText) findViewById(R.id.login_username_parents);
        login = (Button) findViewById(R.id.login_parent_button);

        progressDialog = new ProgressDialog(ParentLogin.this);

        mLoginRef = new Firebase("https://sacw-signup.firebaseio.com/account");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                progressDialog.setMessage("Please Wait");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                reg = regNo.getText().toString();

                mLoginRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        arrayList.add(dataSnapshot.getValue());
                        startLogin();
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
        });

    }

    public void startLogin()
    {
        if (!reg.isEmpty()) {

            if (arrayList.contains(reg)) {



                File dir = new File(Environment.getExternalStorageDirectory(), "/SmartActivatedCollegeWebBook/.parents/");

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File file = new File(dir, "regno.txt");

                if (file.exists()) {
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(reg.getBytes());
                        fileOutputStream.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        file.createNewFile();
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(reg.getBytes());
                        fileOutputStream.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                startActivity(new Intent(getApplicationContext(), ParentHomePage.class).putExtra("reg", reg));
                progressDialog.dismiss();
                finish();

            } else {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Check your Reg.no & Signup..", Toast.LENGTH_SHORT).show();
            }
        } else {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), " Signup..", Toast.LENGTH_SHORT).show();
        }

    }

}
