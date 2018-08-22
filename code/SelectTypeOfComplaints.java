package com.great3.smartactivatedcollegewebbook;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.utilities.Base64;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class SelectTypeOfComplaints extends AppCompatActivity {

    private static final int REQUEST_AUDIO_PERMISSION_RESULT = 1 ;
    private static final int REQUEST_CAMERA_PERMISSION_RESULT = 1 ;
    FirebaseStorage mRef_Firebase_Storage;

    MediaPlayer mediaPlayer;
    MediaRecorder mediaRecorder;
    String OUTPUT_FILE;

    ImageView start;
    ImageView stop;
    TextView timer_text;
    ImageView play;
    ImageView stopPlay;
    Button send;
    ProgressDialog progressDialog;

    int flag = 0;

    Thread timer;
    int min,sec,totalDuration;

    Uri downloadUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_type_of_complaints);

        timer_text = (TextView)findViewById(R.id.timer);

        progressDialog = new ProgressDialog(this);

        Bundle extra = getIntent().getExtras();
        final String reg = extra.getString("reg");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) ==
                    PackageManager.PERMISSION_GRANTED) {
                // put your code for Version>=Marshmallow
            } else {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.RECORD_AUDIO)) {
                    Toast.makeText(this,
                            "App required access to audio", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{android.Manifest.permission.RECORD_AUDIO
                }, REQUEST_CAMERA_PERMISSION_RESULT);
            }

        } else {
            // put your code for Version < Marshmallow
        }


        timer = new Thread(new Runnable() {
            @Override
            public void run() {

                totalDuration = mediaPlayer.getDuration();

                while (0< totalDuration) {

                    try {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                int cut_pos = mediaPlayer.getCurrentPosition();
                                min = (cut_pos/1000)/60;
                                sec = (cut_pos/1000)%60;

                                if (sec<=9)
                                {
                                    timer_text.setText(""+min+":"+"0"+sec);
                                }else
                                {
                                    timer_text.setText(""+min+":"+sec);
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        OUTPUT_FILE = Environment.getExternalStorageDirectory()+"/Complaints.3gpp";

        start = (ImageView)findViewById(R.id.start_voice);
        stop = (ImageView)findViewById(R.id.stop_voice);
        play = (ImageView)findViewById(R.id.play_voice);
        stopPlay = (ImageView)findViewById(R.id.stop_play_voice);
        send = (Button)findViewById(R.id.send_voice_com);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startRecording();
                    timer_text.setText("recording.......");
                    flag++;
                }catch (Exception e)
                {

                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopRecording();
                timer_text.setText("recorded");
                flag++;

            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    startPlay();
                    timer.start();
                }
                catch (Exception e)
                {

                }
            }
        });

        stopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopPlay();

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                if (flag == 2)
                {
                    Uri uri = Uri.parse(OUTPUT_FILE);

                    try {
                        final String title = "Complaints"+System.currentTimeMillis();
                        StorageReference mRef_Firebase_Storage = FirebaseStorage.getInstance().getReference().child("VoiceRecord/"+ reg +"/"+title);
                        InputStream stream = new FileInputStream(uri.getPath());
                        UploadTask uploadTask = mRef_Firebase_Storage.putStream(stream);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(getApplicationContext(),"Upload Failed..",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                                Firebase mRef = new Firebase("https://sacwlibrary.firebaseio.com/VoiceRecord/"+reg+"/");
                                Firebase mRefUri = new Firebase("https://sacwlibrary.firebaseio.com/file/");
                                mRef.child(System.currentTimeMillis() + "").setValue(title);
                                mRefUri.child(title).child("downloadUrl").setValue("" + downloadUri);
                                mRefUri.child(title).child("filename").setValue(title);
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }else
                {
                    progressDialog.dismiss();
                    flag = 0;
                    Toast.makeText(getApplicationContext(),"You are not recored properly..",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void startRecording() throws IOException {
        ditchMediaRecorder();

        File output = new File(OUTPUT_FILE);
        if(output.exists())
        {
            output.delete();
        }
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(OUTPUT_FILE);
        mediaRecorder.prepare();
        mediaRecorder.start();
    }

    void stopRecording()
    {
        if(mediaRecorder!=null)
        {
            mediaRecorder.stop();
        }
    }

    private void ditchMediaRecorder() {

            if(mediaRecorder!=null)
            {
                mediaRecorder.release();
            }
    }

    void startPlay() throws Exception
    {
        ditchMediaPlayer();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(OUTPUT_FILE);
        mediaPlayer.prepare();
        mediaPlayer.start();

    }

    void stopPlay()
    {
        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
        }

    }

    void ditchMediaPlayer()
    {

        if(mediaPlayer!=null)
        {
            try
            {
                mediaPlayer.release();

            }catch (Exception e)
            {

            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_AUDIO_PERMISSION_RESULT) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),
                        "Application will not have audio on record", Toast.LENGTH_SHORT).show();
            }
        }
    }
}