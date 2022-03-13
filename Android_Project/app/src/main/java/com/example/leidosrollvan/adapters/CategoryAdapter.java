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
    ArrayList<String> list1 = new ArrayList<>();
    DatabaseReference imRef;
    private ImageView businessImage;
    private RecyclerViewClickInterface recyclerViewClickInterface;




    public CategoryAdapter(Context context,RecyclerViewClickInterface recyclerViewClickInterface) {
        this.context = context;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    public void setData(ArrayList<Business> list,ArrayList<String> listID) {
        list2.clear();
        list1.clear();
        list2 = list;
        list1 = listID;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_list_item, parent, false);
        return  new MyViewHolder(v);

    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Business business = list2.get(position);
        String currentID = list1.get(position);

        imRef = FirebaseDatabase.getInstance().getReference("Business Images");
        imRef.child(currentID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String uri = snapshot.getValue(BusinessImage.class).getImageUrl();
                    holder.businessName.setText(business.getBusinessName());
                    holder.businessContact.setText(business.getContact());
                    Picasso.with(holder.businessImage.getContext()).load(uri).into(holder.businessImage);
                }else{
                    holder.businessName.setText(business.getBusinessName());
                    holder.businessContact.setText(business.getContact());
                    holder.businessImage.setImageResource(R.drawable.ic_baseline_image_not_supported_24);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return list2.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        View mView;

        TextView businessName;
        TextView businessContact;
        ImageView businessImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            businessName = itemView.findViewById(R.id.nameRecyclerItem);
            businessImage = itemView.findViewById(R.id.imageRecyclerItem);
            businessContact = itemView.findViewById(R.id.contactRecyclerItem);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAbsoluteAdapterPosition());
                }
            });


        }

    }

    public interface RecyclerViewClickInterface {
        void onItemClick(int position);
    }

}