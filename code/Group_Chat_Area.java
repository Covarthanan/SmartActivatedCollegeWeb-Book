package com.great3.smartactivatedcollegewebbook;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by Glenn on 3/3/2018.
 */
public class Group_Chat_Area extends AppCompatActivity {

    /*****

     Note  : Don't forgot to add permissions in manifest
     read_external_storage
     write_external_storage

     ******/
    // Add this in top
    // <---
    EditText write;
    ImageView send;
    ListView msgList;
    String filename = "";
    TextView groupNameDisplay;
    // --->



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_view);


        //Get extra
        Bundle bundle = getIntent().getExtras();
        final String group = bundle.getString("group");
        final String register = bundle.getString("register");
        bean.globeReg = register;

        filename = group+".txt";

        // Add this inside the onCreate method
        // <---
        write = (EditText)findViewById(R.id.msgWrite);
        send = (ImageView) findViewById(R.id.send);
        msgList = (ListView)findViewById(R.id.chat_list);

        groupNameDisplay = (TextView)findViewById(R.id.group_name_display);
        groupNameDisplay.setText(group);

        final Firebase mRef = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/KSRCT/"+group);
        final Firebase mRefReg = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/KSRCT/register/"+register+"/"+group);
        showMsgFromFile();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                String msg = write.getText().toString();
                write.setText("");
                if(!msg.equals(""))
                {
                    final String time = System.currentTimeMillis()+"";
                    final String finalMsg = register+"$$"+msg +"$$"+ time;

                    mRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            String reg  = dataSnapshot.getValue(String.class);
                            Firebase mRef = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/KSRCT/register/"+reg+"/"+group);
                            mRef.child(time).setValue(finalMsg);
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
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Enter the message",Toast.LENGTH_SHORT).show();
                }


            }
        });

        mRefReg.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String val = dataSnapshot.getValue(String.class);
                String key = dataSnapshot.getKey();
                mRefReg.child(key).setValue(null);


                File dir = new File(Environment.getExternalStorageDirectory(),"/SmartActivatedCollegeWebBook/Group/");
                if(!dir.exists())
                {
                    dir.mkdirs();
                }
                File file = new File(dir,filename);
                if(file.exists()) {

                    String mmm = "";
                    try {

                        FileInputStream fis = new FileInputStream(file);
                        DataInputStream in = new DataInputStream(fis);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String strLine;
                        while ((strLine = br.readLine()) != null) {
                            mmm = mmm + strLine;
                        }
                        in.close();
                        mmm=mmm+"-----"+val;
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(mmm.getBytes());
                        fos.close();
                        showMsgFromFile();

                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(),"File not found1",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
                else
                {

                    try {

                        file.createNewFile();
                        FileOutputStream os = new FileOutputStream(file);
                        os.write(val.getBytes());
                        showMsgFromFile();
                    }catch (FileNotFoundException e)
                    {
                        Toast.makeText(getApplicationContext(),"File Not Found2",Toast.LENGTH_SHORT).show();
                    }
                    catch (IOException e)
                    {
                        Toast.makeText(getApplicationContext(),"IO exception",Toast.LENGTH_SHORT).show();
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


        // --->
    }

    // Add this method in outside the onCreate method
    public void showMsgFromFile()
    {

        String mmm = "";
        ArrayList<String>time = new ArrayList<>();
        ArrayList<String> totMsg = new ArrayList<>();


        File dir = new File(Environment.getExternalStorageDirectory(),"/SmartActivatedCollegeWebBook/Group/");
        if(!dir.exists())
        {
            dir.mkdirs();
        }
        File file = new File(dir,filename);
        if(file.exists())
        {
            try {

                FileInputStream fis = new FileInputStream(file);
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    mmm = mmm + strLine;
                }
                in.close();
                String msges[] = mmm.split("-----");
                for(int i=0;i<msges.length;i++)
                {
                    String msg = msges[i];
                    String sentTime = msg.substring(msg.lastIndexOf("$$")+2);
                    if(!time.contains(sentTime))
                    {
                        time.add(sentTime);
                        totMsg.add(msg);
                    }

                }
                String msgArray [] = totMsg.toArray(new String[0]);

                CustomChatList adapter = new CustomChatList(Group_Chat_Area.this, msgArray);
                msgList.setAdapter(adapter);
                msgList.setSelection(msgList.getAdapter().getCount()-1);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"File not found",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        else
        {
            /*try {
                FileOutputStream os = new FileOutputStream(file);
                os.write("1414111$$empty$$12345677".getBytes());
            }catch (FileNotFoundException e)
            {
                Toast.makeText(getApplicationContext(),"File Not Found",Toast.LENGTH_SHORT).show();
            }
            catch (IOException e)
            {
                Toast.makeText(getApplicationContext(),"IO exception",Toast.LENGTH_SHORT).show();
            }*/
            Toast.makeText(getApplicationContext(),"No msg",Toast.LENGTH_SHORT).show();

        }




    }


}
