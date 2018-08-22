package com.great3.smartactivatedcollegewebbook;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;

public class ParentsVoiceComplaints extends AppCompatActivity {

    ListView voice_listView;
    ArrayList arrayList_voice = new ArrayList<>();
    Firebase mRef_voice;
    ProgressDialog progressDialog;
    Uri uri_voice;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_voice_complaints);

        voice_listView = (ListView)findViewById(R.id.voice_listView);

        progressDialog = new ProgressDialog(this);

        Bundle extra = getIntent().getExtras();
        final String reg = extra.getString("reg");

        mRef_voice = new Firebase("https://sacwlibrary.firebaseio.com/VoiceRecord/"+reg+"/");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList_voice);
        voice_listView.setAdapter(adapter);

        mRef_voice.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String voice = dataSnapshot.getValue(String.class);
                arrayList_voice.add(voice);
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

        voice_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                progressDialog.setMessage("Please wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
                final String title = (String) voice_listView.getItemAtPosition(position);
                Firebase mRef = new Firebase("https://sacwlibrary.firebaseio.com/file/" + title + "/downloadUrl");
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String url = dataSnapshot.getValue(String.class);

                        Firebase mRefFileName = new Firebase("https://sacwlibrary.firebaseio.com/file/" + title + "/filename");
                        mRefFileName.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String fileName = dataSnapshot.getValue(String.class);

                                File dir = new File(Environment.getExternalStorageDirectory(), "/SmartActivatedCollegeWebBook/VoiceComplaints/"+reg+"/");
                                if (!dir.exists()) {
                                    dir.mkdirs();
                                }
                                final File file = new File(dir, fileName+".3gpp");

                                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url);

                                storageReference.getFile(file).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Fail to download", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Downloaded Successfully", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getApplicationContext(),""+ file, Toast.LENGTH_SHORT).show();
                                        uri_voice = Uri.parse(String.valueOf(file));
                                        progressDialog.setMessage("Playing audio....");
                                        progressDialog.setCancelable(false);
                                        progressDialog.setCanceledOnTouchOutside(false);
                                        progressDialog.show();
                                        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri_voice);
                                        mediaPlayer.start();

                                        new CountDownTimer(mediaPlayer.getDuration(),1000) {
                                            @Override
                                            public void onTick(long millisUntilFinished) {

                                            }

                                            @Override
                                            public void onFinish() {

                                                progressDialog.dismiss();

                                            }
                                        }.start();
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
    }
}
