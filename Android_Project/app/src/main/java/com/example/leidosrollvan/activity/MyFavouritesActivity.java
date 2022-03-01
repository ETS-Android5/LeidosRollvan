package com.example.leidosrollvan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.leidosrollvan.R;
import com.example.leidosrollvan.adapters.SubscribedRecyclerAdapter;
import com.example.leidosrollvan.dataClasses.Business;
import com.example.leidosrollvan.dataClasses.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyFavouritesActivity extends AppCompatActivity {
    TextView faveHead, noFave;
    RecyclerView faveSection;
    SubscribedRecyclerAdapter subSectionAdapter;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private String userID, busID, businessID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notifications);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefreshMyNoti);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
                pullToRefresh.setRefreshing(false);
            }
        });
        load();
    }



    void load(){
        faveHead = (TextView) findViewById(R.id.subscribedBusinesses);
        faveSection = (RecyclerView) findViewById(R.id.subsSection);
        noFave= (TextView) findViewById(R.id.noSubBusiness);

        user = mAuth.getInstance().getCurrentUser();
        if(user!= null) {
            userID = user.getUid();
        }

        DatabaseReference faveRef = FirebaseDatabase.getInstance().getReference("Favourites");
        faveRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(userID)) {
                    ArrayList faveList = new ArrayList();
                    faveRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                Business busID = snapshot1.getValue(Business.class);
                                String busName = busID.getBusinessName();
                                faveList.add(busName);
                            }
                            faveHead.setVisibility(View.VISIBLE);
                            faveSection.setVisibility(View.VISIBLE);
                            subSectionAdapter = new SubscribedRecyclerAdapter(faveList);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            faveSection.setLayoutManager(layoutManager);
                            faveSection.setItemAnimator(new DefaultItemAnimator());
                            faveSection.setAdapter(subSectionAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    noFave.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }












//        DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("Favourites");
//        ArrayList<String> busList =new ArrayList<String>();
//        //DatabaseReference busRef = FirebaseDatabase.getInstance().getReference("Businesses");
//        favRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.child(busID).hasChild(userID)){
//                    businessID = busID;
//                    DatabaseReference busRef = FirebaseDatabase.getInstance().getReference("Businesses");
//                    busRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if(snapshot.hasChild(businessID)){
//                                Business business = snapshot.getValue(Business.class);
//                                busList.add(business.getBusinessName());
//
//
//                            }
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//

//                faveHead.setVisibility(View.VISIBLE);
//                faveSection.setVisibility(View.VISIBLE);
//                subSectionAdapter = new SubscribedRecyclerAdapter(busList);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//                faveSection.setLayoutManager(layoutManager);
//                faveSection.setItemAnimator(new DefaultItemAnimator());
//                faveSection.setAdapter(subSectionAdapter);
            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });



