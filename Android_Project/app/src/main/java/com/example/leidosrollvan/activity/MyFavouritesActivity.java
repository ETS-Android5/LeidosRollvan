package com.example.leidosrollvan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leidosrollvan.R;
import com.example.leidosrollvan.adapters.CategoryAdapter;
import com.example.leidosrollvan.dataClasses.Business;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyFavouritesActivity extends AppCompatActivity implements CategoryAdapter.RecyclerViewClickInterface {
    TextView noFave;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private String userID, businessID, busName;
    private DatabaseReference favRef, busRef;
    private ArrayList<Business> favesList = new ArrayList<Business>();
    private CategoryAdapter myAdapter;
    private DataSnapshot data;
    private ArrayList<String> faveID = new ArrayList<String>();
    //private Object ListView;
    private ListView favoritesList;
    private TextView faveHead, noFaves;
    private RecyclerView businessRecyclerView, favesSection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favourites);

        user = mAuth.getInstance().getCurrentUser();
        if(user!= null) {
            userID = user.getUid();
        }

        noFaves = (TextView) findViewById(R.id.noFaves);
        businessRecyclerView = (RecyclerView) findViewById(R.id.faveSection);
        businessRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new CategoryAdapter(this, this);

        businessRecyclerView.setAdapter(myAdapter);

        favRef = FirebaseDatabase.getInstance().getReference("Favourites");
        busRef = FirebaseDatabase.getInstance().getReference("Businesses");

        favRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Log.d("Favourites", data.getKey());
                    faveID.add(data.getKey());

                }
                Log.d("ID SIZE: ", Integer.toString(faveID.size()));
                busRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (String id: faveID) {
                            Business business = snapshot.child(id).getValue(Business.class);
                            favesList.add(business);
                        }

                        if(favesList.size()!=0){
                            myAdapter.setData(favesList, faveID);
                            businessRecyclerView.setVisibility(View.VISIBLE);
                        }else {
                            noFaves.setVisibility(View.VISIBLE);
                        }
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

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, BusinessPageActivity.class);
        intent.putExtra("b_id", faveID.get(position));
        startActivity(intent);

    }
}





