package com.great3.smartactivatedcollegewebbook;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class SmartActivatedCollegeWebBookService extends Service {

    int newCount = 0;

    String regNo = "";
    String department = "";
    String batch= "";
    static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    NotificationManager manager ;
    Firebase mRefForCount;
    boolean flag = false;

    public SmartActivatedCollegeWebBookService() {
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.


        File profile_path = new File (Environment.getExternalStorageDirectory(),"/SmartActivatedCollegeWebBook/Profile/");
        if (!profile_path.exists())
        {
            profile_path.mkdirs();
        }

        File file = new File(profile_path,"profile.txt");
        if(!file.exists())
        {
            flag = false;
        }
        else
        {
            flag = true;

            try {

                FileInputStream fis = new FileInputStream(file);
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                String reg = "";
                String dept = "";
                String btch = "";
                String strLine="";
                int index = 0;
                while ((strLine = br.readLine()) != null) {

                        if(index == 0)
                            reg = strLine;
                        if(index==3)
                            dept = strLine;
                        if(index==4)
                            btch = strLine;
                        index++;
                }

                regNo = reg;
                department = dept;
                batch = btch;

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(),"File not found1",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }


        if(flag)
        {
            Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
            String batchGroup = department+batch;
            mRefForCount = new Firebase("https://instantpoetry-4a689.firebaseio.com/count");
            final Firebase mDeptGroup = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/KSRCT/register/"+regNo+"/"+department);
            final Firebase mBatchGroup = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/KSRCT/register/"+regNo+"/"+batchGroup);
            final Firebase mCirCount = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/circularcount");
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (CONNECTIVITY_CHANGE_ACTION.equals(action)) {
                        //check internet connection
                        if (!ConnectionHelper.isConnectedOrConnecting(context)) {
                            Toast.makeText(getApplicationContext(), "Offline", Toast.LENGTH_LONG).show();
                            if (context != null) {
                                boolean show = false;
                                if (ConnectionHelper.lastNoConnectionTs == -1) {//first time
                                    show = true;
                                    ConnectionHelper.lastNoConnectionTs = System.currentTimeMillis();
                                } else {
                                    if (System.currentTimeMillis() - ConnectionHelper.lastNoConnectionTs > 1000) {
                                        show = true;
                                        ConnectionHelper.lastNoConnectionTs = System.currentTimeMillis();
                                    }
                                }

                                if (show && ConnectionHelper.isOnline) {
                                    ConnectionHelper.isOnline = false;
                                }
                            }
                        } else {


                            mCirCount.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    String count = dataSnapshot.getValue(String.class);
                                    File dir = new File(Environment.getExternalStorageDirectory(),"/SmartActivatedCollegeWebBook/.cir/");
                                    if(!dir.exists())
                                    {
                                        dir.mkdirs();
                                    }
                                    File file = new File(dir,"count.txt");
                                    if(file.exists()) {

                                        try {

                                            FileInputStream fis = new FileInputStream(file);
                                            DataInputStream in = new DataInputStream(fis);
                                            BufferedReader br = new BufferedReader(new InputStreamReader(in));
                                            String strLine;
                                            String mmm="";
                                            while ((strLine = br.readLine()) != null) {
                                                mmm = mmm + strLine;
                                            }
                                            in.close();
                                            if(Integer.parseInt(mmm)<Integer.parseInt(count))
                                            {
                                                showNotifications3("New Circular");
                                            }

                                        } catch (IOException e) {
                                            Toast.makeText(getApplicationContext(),"File not found1",Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        }

                                    }

                                }
                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });


                            mBatchGroup.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                    String msg = "";
                                    msg = msg + dataSnapshot.getValue(String.class);
                                    // Toast.makeText(getApplicationContext(),"hi"+msg+"hi",Toast.LENGTH_SHORT).show();

                                    String registerNo = msg.substring(0,msg.indexOf("$$"));
                                    String TextArea = msg.substring(msg.indexOf("$$")+2,msg.lastIndexOf("$$"));
                                    String sentTime = msg.substring(msg.lastIndexOf("$$")+2);

                                    if(!registerNo.equals(regNo))
                                    showNotifications2(TextArea);

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

                            mDeptGroup.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                    String msg = "";
                                    msg = msg + dataSnapshot.getValue(String.class);
                                    // Toast.makeText(getApplicationContext(),"hi"+msg+"hi",Toast.LENGTH_SHORT).show();

                                    String registerNo = msg.substring(0,msg.indexOf("$$"));
                                    String TextArea = msg.substring(msg.indexOf("$$")+2,msg.lastIndexOf("$$"));
                                    String sentTime = msg.substring(msg.lastIndexOf("$$")+2);

                                    if(!registerNo.equals(regNo))
                                    showNotifications1(TextArea);


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

                            ConnectionHelper.isOnline = true;
                        }
                    }
                }
            };

            registerReceiver(receiver,filter);

        }
        return START_STICKY;
    }





    void showNotifications1(String msg)
    {
//        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
        if(!msg.equals("null:)")) {
            Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle(department)
                    .setContentText(msg);
              //      .setSound(sound);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(001, mBuilder.build());

            //vibrator
            Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(50);
        }
    }
    void showNotifications2(String msg)
    {
//        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
        if(!msg.equals("null:)")) {
            Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle(batch)
                    .setContentText(msg);
            //      .setSound(sound);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(002, mBuilder.build());

            //vibrator
            Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(50);
        }
    }

    void showNotifications3(String msg)
    {
//        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
        if(!msg.equals("null:)")) {
            Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle("Circular")
                    .setContentText(msg);
            //      .setSound(sound);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(003, mBuilder.build());

            //vibrator
            Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(50);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

}
