package com.great3.smartactivatedcollegewebbook;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

public class PlacedStudentDetails extends AppCompatActivity {

    ListView batchList ;
    int start = 2014;
    ArrayList<String> years = new ArrayList<>();
    ImageView add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_student_details);
        batchList = (ListView)findViewById(R.id.placed_student_batch_list);
        add = (ImageView)findViewById(R.id.add_placed_student);

        Calendar c = Calendar.getInstance();
        int endYear = c.get(Calendar.YEAR);


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
                        add.setVisibility(View.INVISIBLE);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }


        for(int startYear=2014;startYear<=endYear;startYear++)
        {
            years.add(startYear+"");
        }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,years);
        batchList.setAdapter(arrayAdapter);

        batchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                int batch = start+position;
                startActivity(new Intent(getApplicationContext(),PlacedStudentList.class).putExtra("batch",batch+""));

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                startActivity(new Intent(getApplicationContext(),AddPlacedStudent.class));

            }
        });
    }
}
