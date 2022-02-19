package com.example.leidosrollvan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leidosrollvan.R;
import com.example.leidosrollvan.staticClasses.InputValidation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;

public class businessItemRecyclerAdapter extends RecyclerView.Adapter<businessItemRecyclerAdapter.MyViewHolder> {
    private ArrayList<HashMap<String, String>> itemList;
    private String Section;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String businessID;

    public businessItemRecyclerAdapter(ArrayList<HashMap<String, String>> itemList,String Section) {
        this.itemList = itemList;
        this.Section = Section;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView foodName, foodPrice;

        public MyViewHolder(final View view) {
            super(view);
            foodName = (TextView) view.findViewById(R.id.foodName_bpage);
            foodPrice = (TextView) view.findViewById(R.id.foodPrice_bpage);
        }
    }

    @NonNull
    @Override
    public businessItemRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_card_business_page, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull businessItemRecyclerAdapter.MyViewHolder holder, int position) {
        HashMap<String, String> item = itemList.get(position);
        String name = item.keySet().toArray()[0].toString();
        String price = "£"+item.get(name);
        name = InputValidation.decodeFromFirebaseKey(name);
        holder.foodName.setText(name);
        holder.foodPrice.setText(price);
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
