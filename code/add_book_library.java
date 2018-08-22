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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import java.util.ArrayList;

public class add_book_library extends AppCompatActivity {


    private static final int REQUEST_CODE = 63845;
    String dept_name = "";
    Button add_book_btn,submit_ebook;
    EditText ebook_title;
    ImageView attach_ebook;
    ListView listView_ebook;
    ArrayList<String> ebook_list = new ArrayList<>();
    Firebase mRef_ebook;
    RelativeLayout add_book;
    RelativeLayout ebook_list_layout;
    LinearLayout add_book_layout;

    TextView display_filename;
    String path = "";
    Uri downloadUri = null;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_library);

        progressDialog = new ProgressDialog(add_book_library.this);
        listView_ebook = (ListView) findViewById(R.id.listView_ebook);
        add_book_btn = (Button) findViewById(R.id.add_book_btn);
        submit_ebook = (Button) findViewById(R.id.submit_ebook);
        ebook_title = (EditText) findViewById(R.id.project_title);
        attach_ebook = (ImageView) findViewById(R.id.attach_project);
        add_book_layout = (LinearLayout) findViewById(R.id.add_book_layout);
        ebook_list_layout = (RelativeLayout) findViewById(R.id.ebook_list_layout);
        display_filename = (TextView)findViewById(R.id.lib_file_name_dis);

        add_book_layout.setVisibility(View.INVISIBLE);
        //Get extra for dept_name

        Bundle extra = getIntent().getExtras();
        dept_name = extra.getString("dept_name");


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
                        add_book_btn.setVisibility(View.INVISIBLE);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }

        //firebase reference for library to retrive data

        mRef_ebook = new Firebase("https://sacwlibrary.firebaseio.com/library/" + dept_name);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ebook_list);
        listView_ebook.setAdapter(arrayAdapter);


        mRef_ebook.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String ebook = dataSnapshot.getValue(String.class);
                ebook_list.add(ebook);
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


        add_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                add_book_btn.setVisibility(View.INVISIBLE);
                add_book_layout.setVisibility(View.VISIBLE);
            }
        });


        attach_ebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                showChooser();
            }
        });

        submit_ebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                progressDialog.setMessage("Please wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
                if(ebook_title.getText().toString().equals(""))
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please enter the title", Toast.LENGTH_SHORT).show();

                }else {
                    if (path.equals("")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Please select the file", Toast.LENGTH_SHORT).show();

                    } else {

                        try {
                            final String fileName = path.substring(path.lastIndexOf("/") + 1);
                            StorageReference ref = FirebaseStorage.getInstance().getReference().child("library/" + dept_name + fileName);
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

                                    String tit = ebook_title.getText().toString();
                                    downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                                    Firebase mRef = new Firebase("https://sacwlibrary.firebaseio.com/library/" + dept_name);
                                    Firebase mRefUri = new Firebase("https://sacwlibrary.firebaseio.com/file/");
                                    mRef.child(System.currentTimeMillis() + "").setValue(tit);
                                    mRefUri.child(tit).child("downloadUrl").setValue("" + downloadUri);
                                    mRefUri.child(tit).child("filename").setValue(fileName);
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

            }
        });


        listView_ebook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                progressDialog.setMessage("Please wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
                final String title = (String) listView_ebook.getItemAtPosition(position);
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

                                File dir = new File(Environment.getExternalStorageDirectory(), "/SmartActivatedCollegeWebBook/Library/"+dept_name+"/");
                                if (!dir.exists()) {
                                    dir.mkdirs();
                                }
                                File file = new File(dir, fileName);

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

        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        final Uri uri = data.getData();

                        try {
                            path = FileUtils.getPath(this, uri);
                            Toast.makeText(getApplicationContext(), "File : " + path, Toast.LENGTH_SHORT).show();
                            display_filename.setText(path.substring(path.lastIndexOf("/") + 1));
                        } catch (Exception e) {

                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
