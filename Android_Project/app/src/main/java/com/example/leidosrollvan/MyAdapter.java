package com.example.leidosrollvan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;

    BusinessMenu menu;


    public MyAdapter(Context context, BusinessMenu menu) {
        this.context = context;
        this.menu = menu;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //food Food = menu.getMenu().values();
        //holder.name.setText(Food.getName());
        //holder.price.setText((int) Food.getPrice());



    }

    @Override
    public int getItemCount() {
        return menu.getsize();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,price;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tvName);
            price = itemView.findViewById(R.id.tvPrice);


        }
    }

}