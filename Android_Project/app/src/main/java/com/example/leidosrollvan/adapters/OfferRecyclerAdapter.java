package com.example.leidosrollvan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leidosrollvan.R;
import com.example.leidosrollvan.dataClasses.Offer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OfferRecyclerAdapter extends RecyclerView.Adapter<OfferRecyclerAdapter.MyViewHolder> {
    private ArrayList<String> itemList;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String businessID;



    public OfferRecyclerAdapter(ArrayList<String> itemList) {
        this.itemList = itemList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView offerBody;
        private ImageView deleteImage;

        public MyViewHolder(final View view) {
            super(view);
            offerBody = (TextView) view.findViewById(R.id.offerbody);
            deleteImage = (ImageView) view.findViewById(R.id.deleteImageOff);
        }
    }

    @NonNull
    @Override
    public OfferRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferRecyclerAdapter.MyViewHolder holder, int position) {
        String body = itemList.get(position);
        holder.offerBody.setText(body);
        holder.deleteImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                reference = FirebaseDatabase.getInstance().getReference("Business Offer");
                businessID = user.getUid();
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        reference.child(businessID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                itemList.remove(body);
                                if(itemList.size()==0){
                                    notifyDataSetChanged();
                                }
                                Offer offer = new Offer(itemList);
                                reference.child(businessID).setValue(offer);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
    });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}