package com.great3.smartactivatedcollegewebbook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class ProjectDetails extends AppCompatActivity {


    ImageView addProject;
    ListView projectListview;
    ArrayList<String> files = new ArrayList<>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        progressDialog = new ProgressDialog(ProjectDetails.this);
        addProject = (ImageView)findViewById(R.id.add_project);
        projectListview = (ListView)findViewById(R.id.project_listview);



        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,files);
        projectListview.setAdapter(arrayAdapter);

        Firebase mRef = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/project/");
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String tit = dataSnapshot.getValue(String.class);
                files.add(tit);
                arrayAdapter.notifyDataSetChanged();
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


        projectListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                progressDialog.setMessage("Please wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
                final String title = (String)projectListview.getItemAtPosition(position);
                Firebase mRef = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/projectfile/"+title+"/downloadUrl");
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String url = dataSnapshot.getValue(String.class);

                        Firebase mRefFileName = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/projectfile/"+title+"/filename");
                        mRefFileName.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String fileName = dataSnapshot.getValue(String.class);


                                File dir = new File(Environment.getExternalStorageDirectory(),"/SmartActivatedCollegeWebBook/Project/");
                                if(!dir.exists())
                                {
                                    dir.mkdirs();
                                }
                                File file = new File(dir,fileName);

                                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url);

                                storageReference.getFile(file).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Fail to download",Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Downloaded Successfully",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                                progressDialog.dismiss();
                            }
                        });



                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                        progressDialog.dismiss();
                    }
                });


            }
        });



        addProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getApplicationContext(),AddProjectDetails.class));

            }
        });


    }
}
