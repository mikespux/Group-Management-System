package com.gms.chat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gms.R;

import java.util.ArrayList;
import java.util.List;


public class notadapter extends ArrayAdapter<list> {

    Activity context;
    List<list> items;
    Integer[] imageId = {
            R.drawable.no_profile
    };


    public notadapter(Activity mainActivity, ArrayList<list> dataArrayList) {
        super(mainActivity, 0, dataArrayList);

        this.context = mainActivity;
        this.items = dataArrayList;
    }


    private class ViewHolder {

        TextView message, name;
        ImageView image;


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        notadapter.ViewHolder holder = null;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(
                    R.layout.not_item, parent, false);

            holder = new notadapter.ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.image = (ImageView) convertView.findViewById(R.id.image);

            convertView.setTag(holder);

        } else {
            holder = (notadapter.ViewHolder) convertView.getTag();
        }

        list productItems = items.get(position);


        holder.name.setText(productItems.getName());
        holder.message.setText(productItems.getMessage());

        holder.image.setImageResource(productItems.getImageId());

        return convertView;

    }


}