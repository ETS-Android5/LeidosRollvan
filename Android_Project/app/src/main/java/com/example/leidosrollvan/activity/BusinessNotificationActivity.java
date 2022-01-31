package com.example.leidosrollvan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.leidosrollvan.R;
import com.example.leidosrollvan.adapters.OfferRecyclerAdapter;
import com.example.leidosrollvan.dataClasses.Business;
import com.example.leidosrollvan.dataClasses.Offer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BusinessNotificationActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editNotiBody;
    private FirebaseUser user;
    String businessName;
    Button send;
    String body,busId;
    TextView offerHead;
    RecyclerView offerSection;
    OfferRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_noti);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefreshNoti);
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
        offerHead = (TextView) findViewById(R.id.activeOffer);
        offerSection = (RecyclerView) findViewById(R.id.offerSection);

        send=(Button) findViewById(R.id.send);
        send.setOnClickListener(this);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Businesses");
        user = FirebaseAuth.getInstance().getCurrentUser();
        busId = user.getUid();
        reference.child(busId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Business businessProfile = snapshot.getValue(Business.class);
                if (businessProfile != null) {
                    businessName = businessProfile.businessName;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BusinessNotificationActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
            }
        });

        DatabaseReference referenceOff = FirebaseDatabase.getInstance().getReference("Business Offer");
        referenceOff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(busId)){
                    referenceOff.child(busId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Offer offer = snapshot.getValue(Offer.class);
                            ArrayList<String> offers=offer.getOffers();
                            offerHead.setVisibility(View.VISIBLE);
                            offerSection.setVisibility(View.VISIBLE);
                            adapter = new OfferRecyclerAdapter(offers);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            offerSection.setLayoutManager(layoutManager);
                            offerSection.setItemAnimator(new DefaultItemAnimator());
                            offerSection.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    void send(){
        editNotiBody = (EditText) findViewById(R.id.editTextNotiBody);
        body = editNotiBody.getText().toString().trim();
        if(!body.isEmpty()){
            String topicName="/topics/"+businessName.replace('\'', '-').replace(' ', '-');
            FcmSenderActivity sender = new FcmSenderActivity(topicName,businessName,body,getApplicationContext(),BusinessNotificationActivity.this);
            sender.SendNotifications();
            Toast.makeText(BusinessNotificationActivity.this,"Notification Sent",Toast.LENGTH_LONG).show();
            DatabaseReference referenceOff = FirebaseDatabase.getInstance().getReference("Business Offer");
            referenceOff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(busId)) {
                        referenceOff.child(busId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Offer offer = snapshot.getValue(Offer.class);
                                offer.addOffer(body);
                                referenceOff.child(busId).setValue(offer);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        ArrayList<String> offers = new ArrayList<String>();
                        offers.add(body);
                        Offer offer = new Offer(offers);
                        referenceOff.child(busId).setValue(offer);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            Toast.makeText(BusinessNotificationActivity.this,"Please enter text",Toast.LENGTH_LONG).show();
        }
        editNotiBody.setText("");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.send:
                send();
                break;
        }

    }
}
