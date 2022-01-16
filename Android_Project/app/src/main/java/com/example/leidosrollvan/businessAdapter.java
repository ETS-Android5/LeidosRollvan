package com.example.leidosrollvan;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class businessAdapter extends ArrayAdapter {
        private ArrayList<String> businessNames;
        private ArrayList<Uri> images;
        private Activity context;

        public businessAdapter(Activity context, ArrayList<String> businessNames, ArrayList<Uri> imagess) {
            super(context, R.layout.row_business, businessNames);
            this.context = context;
            this.businessNames =businessNames;
            this.images=images;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row=convertView;
            LayoutInflater inflater = context.getLayoutInflater();
            if(convertView==null)
                row = inflater.inflate(R.layout.row_business, null, true);
            TextView textViewName = (TextView) row.findViewById(R.id.businessName);
            ImageView image = (ImageView) row.findViewById(R.id.banner);

            textViewName.setText(businessNames.get(position));
            image.setImageResource(R.drawable.burger);
            return  row;
        }
    }
