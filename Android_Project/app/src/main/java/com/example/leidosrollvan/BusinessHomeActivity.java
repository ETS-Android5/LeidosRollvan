package com.example.leidosrollvan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BusinessHomeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button businessHomeLogout;
    private ProgressBar businessHomeProgressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String businessID;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    BusinessMenu menu;
    HashMap<String, HashMap<String, Double>> menuMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_home);

        recyclerView = findViewById(R.id.recycler1);
        reference = FirebaseDatabase.getInstance().getReference("Businesses/businessMenu");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new MyAdapter(this,menu);
        recyclerView.setAdapter(myAdapter);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Convert firebase data into BusinessMenu Object
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    HashMap<String, Double> foodItems = new HashMap<String,Double>();
                    String category = dataSnapshot.getKey();
                    for(DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                        String name = dataSnapshot2.getKey();
                        double price = (double) dataSnapshot2.getValue();
                        foodItems.put(name,price);
                    }
                    menuMap.put(category,foodItems);
                }
                menu = new BusinessMenu(menuMap);
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        businessHomeProgressBar = (ProgressBar) findViewById(R.id.business_home_progressBar);
        businessHomeProgressBar.setVisibility(View.VISIBLE);

        user = mAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Businesses");
        businessID = user.getUid();

        businessHomeLogout = (Button) findViewById(R.id.business_home_logout);
        businessHomeLogout.setOnClickListener(this);

        final TextView businessHomeName = (TextView) findViewById(R.id.business_home_name);
        final TextView businessHomeMobile = (TextView) findViewById(R.id.business_home_mobile);

        reference.child(businessID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Business businessProfile = snapshot.getValue(Business.class);

                if (businessProfile != null){
                    String businessName = businessProfile.businessName;
                    String businessMobile = businessProfile.businessMobile;

                    businessHomeName.setText("Business Name: " + businessName);

                    businessHomeMobile.setText("Business Contact: " + businessMobile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BusinessHomeActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
            }
        });

        businessHomeProgressBar.setVisibility(View.GONE);
        businessHomeName.setVisibility(View.VISIBLE);
        businessHomeMobile.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.business_home_logout:
                mAuth.getInstance().signOut();
                startActivity(new Intent(this, BusinessLoginActivity.class));
        }
    }
}