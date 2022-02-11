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
import com.example.leidosrollvan.dataClasses.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyNotificationsActivity extends AppCompatActivity {

    TextView subHead,noSub;
    RecyclerView subSection;
    SubscribedRecyclerAdapter subSectionAdapter;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    String userID;

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
        subHead = (TextView) findViewById(R.id.subscribedBusinesses);
        subSection = (RecyclerView) findViewById(R.id.subsSection);
        noSub= (TextView) findViewById(R.id.noSubBusiness);

        user = mAuth.getInstance().getCurrentUser();
        if(user!= null) {
            userID = user.getUid();
        }

        DatabaseReference referenceNoti = FirebaseDatabase.getInstance().getReference("User Notis");
        referenceNoti.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(userID)){
                    referenceNoti.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Notification noti = snapshot.getValue(Notification.class);
                            ArrayList<String> notis=noti.getNotis();
                            subHead.setVisibility(View.VISIBLE);
                            subSection.setVisibility(View.VISIBLE);
                            subSectionAdapter = new SubscribedRecyclerAdapter(notis);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            subSection.setLayoutManager(layoutManager);
                            subSection.setItemAnimator(new DefaultItemAnimator());
                            subSection.setAdapter(subSectionAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    noSub.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
