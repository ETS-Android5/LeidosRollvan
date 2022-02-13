package com.example.leidosrollvan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leidosrollvan.R;
import com.example.leidosrollvan.dataClasses.Notification;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class SubscribedRecyclerAdapter extends RecyclerView.Adapter<SubscribedRecyclerAdapter.MyViewHolder> {
    private ArrayList<String> itemList;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;



    public SubscribedRecyclerAdapter(ArrayList<String> itemList) {
        this.itemList = itemList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView businessName;
        private ImageView deleteImage;

        public MyViewHolder(final View view) {
            super(view);
            businessName = (TextView) view.findViewById(R.id.offerbody);
            deleteImage = (ImageView) view.findViewById(R.id.deleteImageOff);
        }
    }

    @NonNull
    @Override
    public SubscribedRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscribedRecyclerAdapter.MyViewHolder holder, int position) {
        String body = itemList.get(position);
        holder.businessName.setText(body);
        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(body.replace('\'', '-').replace(' ', '-')).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(v.getContext(), "Unsubscribed", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                user = FirebaseAuth.getInstance().getCurrentUser();
                reference = FirebaseDatabase.getInstance().getReference("User Notis");
                userID = user.getUid();
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                itemList.remove(body);
                                if (itemList.size() == 0) {
                                    notifyDataSetChanged();
                                }
                                Notification noti = new Notification(itemList);
                                reference.child(userID).setValue(noti);
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