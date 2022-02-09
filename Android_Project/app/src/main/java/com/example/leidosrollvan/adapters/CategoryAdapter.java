package com.example.leidosrollvan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leidosrollvan.R;
import com.example.leidosrollvan.dataClasses.Business;
import com.example.leidosrollvan.dataClasses.BusinessMenu;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    Context context;
    ArrayList<Business> list2 = new ArrayList<>();


    public CategoryAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<Business> list) {
        list2.clear();
        list2 = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_list_item, parent, false);
        return  new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        Business business = list2.get(position);
        holder.businessName.setText(business.getBusinessName());


    }

    @Override
    public int getItemCount() {
        return list2.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView businessName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            businessName = itemView.findViewById(R.id.nameRecyclerItem);


        }
    }

}
