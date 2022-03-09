package com.example.leidosrollvan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leidosrollvan.R;

import java.util.ArrayList;

public class FaveAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> listOfFaves;
    LayoutInflater inflater;


    public FaveAdapter(Context ctx, ArrayList listOfFaves){
        this.context = ctx;
        this.listOfFaves = listOfFaves;
        inflater = LayoutInflater.from(ctx);
    }
    @Override
    public int getCount() {
        return listOfFaves.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.activity_fave_list_view, null);
        TextView text = (TextView) convertView.findViewById(R.id.FaveName);
        text.setText(listOfFaves.get(position));
        return convertView;

    }
}
