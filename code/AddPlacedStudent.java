package com.great3.smartactivatedcollegewebbook;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.Calendar;

public class AddPlacedStudent extends AppCompatActivity {


    EditText name;
    EditText company;
    EditText salary;
    EditText dept;
    EditText batch;
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_placed_student);

        name = (EditText)findViewById(R.id.add_placed_name);
        company = (EditText)findViewById(R.id.add_placed_company);
        salary = (EditText)findViewById(R.id.add_placed_salary);
        dept = (EditText)findViewById(R.id.add_placed_dept);
        batch = (EditText)findViewById(R.id.add_placed_batch);
        ok = (Button)findViewById(R.id.add_placed_button);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //vibrator
                Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);

                String nameStr = name.getText().toString();
                String deptStr = dept.getText().toString();
                String companyStr = dept.getText().toString();
                String salartStr = salary.getText().toString();
                String batchStr = batch.getText().toString();


                if(!nameStr.equals("")&&!deptStr.equals("")&&!companyStr.equals(""))
                {
                    Calendar c = Calendar.getInstance();
                    int cur = c.get(Calendar.YEAR);
                    if(Integer.parseInt(batchStr)>=2014&&(Integer.parseInt(batchStr)<=cur)&&Integer.parseInt(salartStr)>1000)
                    {
                        String val = nameStr+"$$$"+deptStr+"$$$"+companyStr+"$$$"+salartStr;
                        Firebase mRefPlaced = new Firebase("https://sacw-signup.firebaseio.com/KSRCOLLEGEOFTECHNOLOGY/placedstudent/");
                        mRefPlaced.child(batchStr).child(System.currentTimeMillis()+"").setValue(val);
                        Toast.makeText(getApplicationContext(),"Added successfully",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),PlacedStudentDetails.class));
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please enter the valid year and salary",Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please enter all the details",Toast.LENGTH_LONG).show();
                }



            }
        });
    }
}
