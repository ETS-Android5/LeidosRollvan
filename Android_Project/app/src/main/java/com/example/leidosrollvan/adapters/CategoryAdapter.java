package com.example.leidosrollvan.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leidosrollvan.R;
import com.example.leidosrollvan.activity.BusinessPageActivity;
import com.example.leidosrollvan.activity.CategoryActivity;
import com.example.leidosrollvan.dataClasses.Business;
import com.example.leidosrollvan.dataClasses.BusinessImage;
import com.example.leidosrollvan.dataClasses.BusinessMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    Context context;
    ArrayList<Business> list2 = new ArrayList<>();
    DatabaseReference imRef;
    private ImageView businessImage;
    private RecyclerViewClickInterface recyclerViewClickInterface;


    public CategoryAdapter(Context context,RecyclerViewClickInterface recyclerViewClickInterface) {
        this.context = context;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
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

    public class MyViewHolder extends RecyclerView.ViewHolder{
        View mView;

        TextView businessName;
        ImageView businessImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            businessName = itemView.findViewById(R.id.nameRecyclerItem);
            businessImage = itemView.findViewById(R.id.imageRecyclerItem);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });


        }

    }

    public interface RecyclerViewClickInterface {
        void onItemClick(int position);
    }

}