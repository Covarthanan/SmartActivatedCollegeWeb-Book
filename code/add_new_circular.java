package com.great3.smartactivatedcollegewebbook;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class add_new_circular extends AppCompatActivity {

    private static final String TAG = "add_new_circular";

    private static final int REQUEST_CODE = 6384;
    Button submit;
    EditText title;
    ImageView attach;
    String path = "";
    Uri downloadUri = null;
    ProgressDialog progressDialog;
    TextView display_filename;
    int count = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_circular);

        progressDialog = new ProgressDialog(add_new_circular.this);
        submit = (Button)findViewById(R.id.submit_circular);
        title = (EditText)findViewById(R.id.project_title);
        attach = (ImageView)findViewById(R.id.attach_project);
        display_filename = (TextView)findViewById(R.id.cir_display_file);



        final Firebase mRefCount = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/circularcount");

        mRefCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String c = dataSnapshot.getValue(String.class);
                count = Integer.parseInt(c);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                showChooser();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                progressDialog.setMessage("Please wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

                if(title.getText().toString().equals(""))
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please enter the title", Toast.LENGTH_SHORT).show();
                }
                else if(count!=-1){
                    if (path.equals("")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Please select the file", Toast.LENGTH_SHORT).show();

                    } else {

                        try {
                            final String fileName = path.substring(path.lastIndexOf("/") + 1);
                            StorageReference ref = FirebaseStorage.getInstance().getReference().child("files/" + fileName);
                            InputStream stream = new FileInputStream(new File(path));
                            UploadTask uploadTask = ref.putStream(stream);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    String tit = title.getText().toString();
                                    downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                                    Firebase mRef = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/title/");
                                    Firebase mRefUri = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/file/");
                                    mRef.child(System.currentTimeMillis() + "").setValue(tit);
                                    mRefUri.child(tit).child("downloadUrl").setValue("" + downloadUri);
                                    mRefUri.child(tit).child("filename").setValue(fileName);
                                    count++;
                                    mRefCount.setValue(count+"");
                                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                    path = "";
                                    progressDialog.dismiss();
                                    finish();

                                }
                            });
                        } catch (Exception e) {

                        }
                    }

                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"check your internet connection and try again",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void showChooser()
    {
        Intent target = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(target,"Loren ipsum");
        try
        {
            startActivityForResult(intent,REQUEST_CODE);
        }
        catch (ActivityNotFoundException e)
        {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case REQUEST_CODE:
                if(resultCode == RESULT_OK)
                {
                    if(data!=null)
                    {
                        final Uri uri = data.getData();

                        try
                        {
                            path = FileUtils.getPath(this,uri);
                            Toast.makeText(getApplicationContext(),"File : "+path,Toast.LENGTH_SHORT).show();
                            display_filename.setText(path.substring(path.lastIndexOf("/") + 1));
                        }
                        catch (Exception e)
                        {

                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
