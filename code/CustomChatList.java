package com.great3.smartactivatedcollegewebbook;

/**
 * Created by Glenn on 3/3/2018.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kaliya on 9/13/2017.
 */
public class CustomChatList extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] web;


    public CustomChatList(Activity context, String[] web)
    {
        super(context,R.layout.user_single_list_view,web);
        this.context = context;
        this.web = web;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String msg = web[position];
        String registerNo = msg.substring(0,msg.indexOf("$$"));
        String TextArea = msg.substring(msg.indexOf("$$")+2,msg.lastIndexOf("$$"));
        String sentTime = msg.substring(msg.lastIndexOf("$$")+2);

            LayoutInflater inflater = context.getLayoutInflater();
            Date date = new Date(Long.parseLong(sentTime));
            String dateTime = "" + date;
            dateTime = dateTime.substring(0, dateTime.indexOf("GMT"));

            if (registerNo.equals(bean.globeReg)) {
                View rowView = inflater.inflate(R.layout.user_single_list_view, null, true);
                TextView regNo = (TextView) rowView.findViewById(R.id.user_chat_regno);
                TextView area = (TextView) rowView.findViewById(R.id.user_chat_text_area);
                TextView time = (TextView) rowView.findViewById(R.id.user_chat_time);
                regNo.setText(registerNo);
                area.setText(TextArea);
                time.setText(dateTime);
                return rowView;
            } else {
                View rowView = inflater.inflate(R.layout.single_list_view, null, true);
                TextView regNo = (TextView) rowView.findViewById(R.id.chat_regno);
                TextView area = (TextView) rowView.findViewById(R.id.chat_text_area);
                TextView time = (TextView) rowView.findViewById(R.id.chat_time);
                regNo.setText(registerNo);
                area.setText(TextArea);
                time.setText(dateTime);
                return rowView;
            }
    }
}



