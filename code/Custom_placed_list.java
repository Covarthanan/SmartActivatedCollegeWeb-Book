package com.great3.smartactivatedcollegewebbook;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by Glenn on 4/3/2018.
 */
public class Custom_placed_list extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] web;


    public Custom_placed_list(Activity context, String[] web) {
        super(context, R.layout.custom_placed_list, web);
        this.context = context;
        this.web = web;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String msg = web[position];
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_placed_list, null, true);
        String temp = msg;
        String name = temp.substring(0,temp.indexOf("$$$"));
        temp = temp.substring(temp.indexOf("$$$")+3);
        String Dept = temp.substring(0,temp.indexOf("$$$"));
        temp = temp.substring(temp.indexOf("$$$")+3);
        String Company = temp.substring(0,temp.indexOf("$$$"));
        temp = temp.substring(temp.indexOf("$$$")+3);
        String Salary = temp;


        TextView nameTxt = (TextView) rowView.findViewById(R.id.place_name);
        TextView deptTxt = (TextView) rowView.findViewById(R.id.placed_dept);
        TextView companyTxt = (TextView) rowView.findViewById(R.id.placed_company);
        TextView salaryTxt = (TextView)rowView.findViewById(R.id.placed_salary);

        if(name.equals(" "))
        {
            nameTxt.setText(name);
            deptTxt.setText(Dept);
            companyTxt.setText("Not Yet placed..:( ");
            salaryTxt.setText(Salary);
        }
        else {
            nameTxt.setText(name);
            deptTxt.setText("Department : " + Dept);
            companyTxt.setText("Company : " + Company);
            salaryTxt.setText("Salary : " + Salary);
        }
        return rowView;
    }


}
