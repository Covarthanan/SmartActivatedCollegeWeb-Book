package com.great3.smartactivatedcollegewebbook;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Glenn on 1/20/2018.
 */
public class CustomList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;

    public CustomList(Activity context,String web[],Integer imageId[])
    {
        super(context, R.layout.menu_list_single_item,web);
        this.context = context;
        this.imageId = imageId;
        this.web = web;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.menu_list_single_item,null,true);
        TextView menuItemText = (TextView)rowView.findViewById(R.id.menu_item_text);
        ImageView menuItemImage = (ImageView)rowView.findViewById(R.id.menu_item_imgae);
        menuItemText.setText(web[position]);
        menuItemImage.setImageResource(imageId[position]);
        return rowView;
    }
}
