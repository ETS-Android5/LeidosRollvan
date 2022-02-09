package com.example.leidosrollvan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leidosrollvan.R;
import com.example.leidosrollvan.adapters.businessItemRecyclerAdapter;
import com.example.leidosrollvan.dataClasses.Business;
import com.example.leidosrollvan.dataClasses.BusinessImage;
import com.example.leidosrollvan.dataClasses.BusinessMenu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BusinessPageActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference reference;
    private DatabaseReference imRef, favRef;
    private ImageButton faveButton;
    private Button homeButton, notiSubButton, notiUnSubButton;
    boolean userClick = false;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userID, businessID, businessName, businessMobile, businessEmail; ;
    private TextView businessPageName, businessPageMob, businessPageEmail;
    private ImageView businessPageImg;
    private TextView notifyNoItems,cat1,cat2,cat3,cat4,cat5;
    businessItemRecyclerAdapter adapter;
    private RecyclerView breakfastSection,lunchSection,dinnerSection,dessertSection,drinksSection;
    private boolean paused = false;
    String[] categories =  {"Asian Cuisine","Kebab","Hot Dogs","Coffee and Tea","Burritos"};
    String[] sections =  {"Breakfast","Lunch","Dinner","Dessert","Drinks"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_page);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            notiSubButton = (Button) findViewById(R.id.noti);
            notiSubButton.setOnClickListener(this);
            notiUnSubButton = (Button) findViewById(R.id.notiUn);
            notiUnSubButton.setOnClickListener(this);
            notiSubButton.setVisibility(View.VISIBLE);
            notiUnSubButton.setVisibility(View.VISIBLE);
        }
        homeButton = (Button) findViewById(R.id.home_bus);
        homeButton.setOnClickListener(this);


        user = mAuth.getInstance().getCurrentUser();
        if(user!= null) {
            userID = user.getUid();
        }
        faveButton = (ImageButton) findViewById(R.id.faveButton);
        faveButton.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        String b_id = bundle.getString("b_id");

        businessPageName = (TextView) findViewById(R.id.business_page_name);
        businessPageMob = (TextView) findViewById(R.id.business_page_mob);
        businessPageEmail = (TextView) findViewById(R.id.business_page_email);
        businessPageImg = (ImageView) findViewById(R.id.busi_page_Image);
        reference = FirebaseDatabase.getInstance().getReference("Businesses");
        reference.child(b_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Business businessProfile = snapshot.getValue(Business.class);
                if (businessProfile != null) {
                    businessName = businessProfile.businessName;
                    businessMobile = businessProfile.businessMobile;
                    businessEmail= businessProfile.businessEmail;
                    businessPageName.setText(businessName);
                    businessPageMob.setText(businessMobile);
                    businessPageEmail.setText(businessEmail);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BusinessPageActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
            }
        });

        imRef = FirebaseDatabase.getInstance().getReference("Business Images");
        imRef.child(b_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String uri = snapshot.getValue(BusinessImage.class).mImageUrl;
                    Picasso.with(businessPageImg.getContext()).load(uri).into(businessPageImg);
                } else {
                    businessPageImg.setImageResource(R.drawable.ic_baseline_image_not_supported_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Image retrieval error",error.getMessage());
            }
        });
        load();
        businessID = b_id;

        favRef = FirebaseDatabase.getInstance().getReference("Favourites");
        if(user != null) {
            getFaveStatus(businessID, userID);
        }
    }

    public void getFaveStatus(String businessID, String userID){
        favRef = FirebaseDatabase.getInstance().getReference("Favourites");
        favRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(businessID).hasChild(userID)){
                    faveButton.setImageResource(R.drawable.heart_filled);
                }
                else{
                    faveButton.setImageResource(R.drawable.heart_outline);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setFave(String businessID, String userID){
        userClick = true;
        favRef = FirebaseDatabase.getInstance().getReference("Favourites");
        favRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(user != null) {
                    if (userClick == true) {

                        if (snapshot.child(businessID).hasChild(userID)) {
                            favRef.child(businessID).child(userID).removeValue();
                        } else {
                            favRef.child(businessID).child(userID).setValue(true);
                        }
                    }
                }
                else{
                    Toast.makeText(BusinessPageActivity.this, "login to add to favourites", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    protected void load() {
        cat1 = (TextView) findViewById(R.id.cat1_bpage);
        cat2 = (TextView) findViewById(R.id.cat2_bpage);
        cat3 = (TextView) findViewById(R.id.cat3_bpage);
        cat4 = (TextView) findViewById(R.id.cat4_bpage);
        cat5 = (TextView) findViewById(R.id.cat5_bpage);
        notifyNoItems = (TextView) findViewById(R.id.notifyBusinessNoItems);
        breakfastSection = (RecyclerView) findViewById(R.id.breakfastSection_bpage);
        lunchSection = (RecyclerView) findViewById(R.id.lunchSection_bpage);
        dinnerSection = (RecyclerView) findViewById(R.id.dinnerSection_bpage);
        dessertSection = (RecyclerView) findViewById(R.id.dessertSection_bpage);
        drinksSection = (RecyclerView) findViewById(R.id.drinksSection_bpage);
        Bundle bundle = getIntent().getExtras();
        String b_id = bundle.getString("b_id");
        user = mAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Business Menu");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(b_id + "/businessMenuItems")) {
                    reference.child(b_id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            BusinessMenu oldMenu = snapshot.getValue(BusinessMenu.class);
                            if (oldMenu.isEmptyMenu()) {
                                return;
                            }
                            ArrayList<String> sections = oldMenu.getSections();
                            if (sections.contains("Breakfast")) {
                                cat1.setVisibility(View.VISIBLE);
                                breakfastSection.setVisibility(View.VISIBLE);
                                oldMenu.getBusinessMenuItems().get("Breakfast");
                                adapter = new businessItemRecyclerAdapter(oldMenu.getBusinessMenuItems().get("Breakfast"), "Breakfast");
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                breakfastSection.setLayoutManager(layoutManager);
                                breakfastSection.setItemAnimator(new DefaultItemAnimator());
                                breakfastSection.setAdapter(adapter);
                            }
                            if (sections.contains("Lunch")) {
                                cat2.setVisibility(View.VISIBLE);
                                lunchSection.setVisibility(View.VISIBLE);
                                oldMenu.getBusinessMenuItems().get("Lunch");
                                adapter = new businessItemRecyclerAdapter(oldMenu.getBusinessMenuItems().get("Lunch"), "Lunch");
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                lunchSection.setLayoutManager(layoutManager);
                                lunchSection.setItemAnimator(new DefaultItemAnimator());
                                lunchSection.setAdapter(adapter);
                            }
                            if (sections.contains("Dinner")) {
                                cat3.setVisibility(View.VISIBLE);
                                dinnerSection.setVisibility(View.VISIBLE);
                                oldMenu.getBusinessMenuItems().get("Dinner");
                                adapter = new businessItemRecyclerAdapter(oldMenu.getBusinessMenuItems().get("Dinner"), "Dinner");
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                dinnerSection.setLayoutManager(layoutManager);
                                dinnerSection.setItemAnimator(new DefaultItemAnimator());
                                dinnerSection.setAdapter(adapter);
                            }
                            if (sections.contains("Dessert")) {
                                cat4.setVisibility(View.VISIBLE);
                                dessertSection.setVisibility(View.VISIBLE);
                                oldMenu.getBusinessMenuItems().get("Dessert");
                                adapter = new businessItemRecyclerAdapter(oldMenu.getBusinessMenuItems().get("Dessert"), "Dessert");
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                dessertSection.setLayoutManager(layoutManager);
                                dessertSection.setItemAnimator(new DefaultItemAnimator());
                                dessertSection.setAdapter(adapter);
                            }
                            if (sections.contains("Drinks")) {
                                cat5.setVisibility(View.VISIBLE);
                                drinksSection.setVisibility(View.VISIBLE);
                                oldMenu.getBusinessMenuItems().get("Drinks");
                                adapter = new businessItemRecyclerAdapter(oldMenu.getBusinessMenuItems().get("Drinks"), "Drinks");
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                drinksSection.setLayoutManager(layoutManager);
                                drinksSection.setItemAnimator(new DefaultItemAnimator());
                                drinksSection.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(BusinessPageActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
                        }

                    });
                } else {
                    notifyNoItems.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BusinessPageActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_bus:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.faveButton:
                setFave(businessID, userID);
                break;
            case R.id.noti:
                if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                    FirebaseMessaging.getInstance().subscribeToTopic(businessName.replace('\'', '-').replace(' ', '-')).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Subscribed", Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
                }
            case R.id.notiUn:
                if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(businessName.replace('\'', '-').replace(' ', '-')).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Unsubscribed", Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
                }
        }
    }
}
