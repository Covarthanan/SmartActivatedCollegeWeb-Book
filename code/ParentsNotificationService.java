package com.great3.smartactivatedcollegewebbook;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
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

public class ParentsNotificationService extends Service {


    String reg = "";
    Boolean flag = false;
    ArrayList countArray = new ArrayList();
    String count_str;
    String count = "0";


    static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    NotificationManager notificationManager;

    public ParentsNotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        File count_path = new File (Environment.getExternalStorageDirectory(),"/SmartActivatedCollegeWebBook/.parents/");
        if (!count_path.exists())
        {
            count_path.mkdirs();
        }

        File file = new File(count_path,"regno.txt");
        if (!file.exists())
        {
            flag = false;
        }
        else
        {
            flag = true;

            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                DataInputStream dataInputStream = new DataInputStream(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
                reg = ""+bufferedReader.readLine().toString();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (flag)
        {

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

                            final Firebase mRef_count = new Firebase("https://smartactivatedcollegeweb-9d204.firebaseio.com/PARENTS/"+reg+"/");

            mRef_count.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    countArray.add(dataSnapshot.getValue());

                    count_str = ""+countArray.size();

                    File dir_complaint = new File(Environment.getExternalStorageDirectory(), "/SmartActivatedCollegeWebBook/.parents/");

                    if (!dir_complaint.exists()) {
                        dir_complaint.mkdirs();
                    }

                    File file_complaint = new File(dir_complaint, "count.txt");

                    if (file_complaint.exists()) {
                        try {

                            FileInputStream fileInputStream = new FileInputStream(file_complaint);
                            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
                            count = ""+bufferedReader.readLine().toString();

                            if(Integer.parseInt(count_str)>Integer.parseInt(count))
                            {
                                notification("New Complaints");
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {

                        notification("New Complaints");

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
                            ConnectionHelper.isOnline = true;
                        }
                    }
                }
            };

            registerReceiver(receiver,filter);

        }
        return START_STICKY;
    }
    void notification(String msg)
    {
//        if (!msg.isEmpty())
//        {
            Uri sound = Uri.parse("android.resource://" +getPackageName() + "/" + R.raw.notification);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle("Complaints")
                    .setContentText(msg);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1,mBuilder.build());

        //}
    }
}
