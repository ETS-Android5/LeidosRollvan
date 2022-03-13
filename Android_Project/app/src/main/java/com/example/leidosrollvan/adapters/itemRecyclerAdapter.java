package com.example.leidosrollvan.adapters;

import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leidosrollvan.R;
import com.example.leidosrollvan.dataClasses.BusinessMenu;
import com.example.leidosrollvan.staticClasses.InputValidation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class itemRecyclerAdapter extends RecyclerView.Adapter<itemRecyclerAdapter.MyViewHolder> {
    private ArrayList<HashMap<String, String>> itemList;
    private String Section;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String businessID;



    public itemRecyclerAdapter(ArrayList<HashMap<String, String>> itemList,String Section) {
        this.itemList = itemList;
        this.Section = Section;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView foodName, foodPrice;
        private ImageView deleteImage;

        public MyViewHolder(final View view) {
            super(view);
            foodName = (TextView) view.findViewById(R.id.foodName);
            foodPrice = (TextView) view.findViewById(R.id.foodPrice);
            deleteImage = (ImageView) view.findViewById(R.id.deleteImage);
        }
    }

    @NonNull
    @Override
    public itemRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull itemRecyclerAdapter.MyViewHolder holder, int position) {
        HashMap<String, String> item = itemList.get(position);
        String name = item.keySet().toArray()[0].toString();
        String price = "$"+item.get(name);
        name = InputValidation.decodeFromFirebaseKey(name);
        holder.foodName.setText(name);
        holder.foodPrice.setText(price);
        holder.deleteImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                reference = FirebaseDatabase.getInstance().getReference("Business Menu");
                businessID = user.getUid();
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        reference.child(businessID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                BusinessMenu oldMenu = snapshot.getValue(BusinessMenu.class);
                                Log.i("oldMenu", "onDataChange: "+oldMenu);
                                oldMenu.removeBusinessMenuItems(Section,item);
                                itemList.remove(item);
                                if(itemList.size()==0){
                                    notifyDataSetChanged();
                                }
                                FirebaseDatabase.getInstance().getReference("Business Menu")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(oldMenu);
                                notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(v.getContext(), "Something Went Wrong!", Toast.LENGTH_LONG).show();
                    }
                });
            }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(v.getContext(), "Something Went Wrong!", Toast.LENGTH_LONG).show();
                    };
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
