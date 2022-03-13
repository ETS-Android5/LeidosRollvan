package com.example.leidosrollvan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.leidosrollvan.dataClasses.Notification;
import com.example.leidosrollvan.dataClasses.OpeningTimes;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BusinessPageActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference reference, OTreference;
    private DatabaseReference imRef, favRef,notiRef;
    private ImageButton faveButton,notiSubButton;
    private Button homeButton,OTButton;
    boolean userClick = false;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userID, businessID, businessName, businessMobile, businessEmail;
    private TextView businessPageName, businessPageMob, businessPageEmail,OpeningHours,OpeningWarning;
    private CheckBox sun,mon,tue,wed,thu,fri,sat;
    private LinearLayout OTLayout;
    private ImageView businessPageImg;
    private TextView notifyNoItems,cat1,cat2,cat3,cat4,cat5;
    private businessItemRecyclerAdapter adapter;
    private RecyclerView breakfastSection,lunchSection,dinnerSection,dessertSection,drinksSection;
    private boolean paused = false;
    private String[] categories =  {"Vegan","Tea and Coffee","Healthy","Fast Food","Desserts","Rolls"};
    private String[] sections =  {"Breakfast","Lunch","Dinner","Dessert","Drinks"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_page);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            notiSubButton = (ImageButton) findViewById(R.id.noti);
            notiSubButton.setOnClickListener(this);
            notiSubButton.setVisibility(View.VISIBLE);
        }
        homeButton = (Button) findViewById(R.id.home_bus);
        homeButton.setOnClickListener(this);


        user = mAuth.getInstance().getCurrentUser();
        if(user!= null) {
            userID = user.getUid();
        }
        faveButton = (ImageButton) findViewById(R.id.faveButton);
        faveButton.setOnClickListener(this);

        OpeningHours = (TextView) findViewById(R.id.OpeningHoursText);
        OpeningWarning = (TextView) findViewById(R.id.noOpeningHours);

        sun = (CheckBox) findViewById(R.id.checkBoxSun);
        mon = (CheckBox) findViewById(R.id.checkBoxMon);
        tue = (CheckBox) findViewById(R.id.checkBoxTue);
        wed = (CheckBox) findViewById(R.id.checkBoxWed);
        thu = (CheckBox) findViewById(R.id.checkBoxThu);
        fri = (CheckBox) findViewById(R.id.checkBoxFri);
        sat = (CheckBox) findViewById(R.id.checkBoxSat);

        OTLayout = (LinearLayout) findViewById(R.id.OTLayout);

        OTButton = (Button) findViewById(R.id.OTButton);
        OTButton.setOnClickListener(this);



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

        OTreference = FirebaseDatabase.getInstance().getReference("Business OT");
        OTreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(b_id)){ //if previous data found, then load into boxes
                    OTreference = FirebaseDatabase.getInstance().getReference("Business OT/"+b_id);
                    OTreference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            OpeningTimes OT = snapshot.getValue(OpeningTimes.class);
                            OpeningWarning.setVisibility(View.GONE);
                            OTButton.setVisibility(View.VISIBLE);
                            OpeningHours.setText(OT.toString());
                            ArrayList<String>Days = OT.getDaysOfWeek();
                            if(Days.contains("Monday")){
                                mon.setChecked(true);
                            }
                            if(Days.contains("Tuesday")){
                                tue.setChecked(true);
                            }
                            if(Days.contains("Wednesday")){
                                wed.setChecked(true);
                            }
                            if(Days.contains("Thursday")){
                                thu.setChecked(true);
                            }
                            if(Days.contains("Friday")){
                                fri.setChecked(true);
                            }
                            if(Days.contains("Saturday")){
                                sat.setChecked(true);
                            }
                            if(Days.contains("Sunday")){
                                sun.setChecked(true);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(BusinessPageActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
                        }
                    });
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
        notiRef = FirebaseDatabase.getInstance().getReference("User Notis");
        if(user != null) {
            getFaveStatus(businessID, userID);
            getNotiStatus(userID);
        }
    }

    public void getNotiStatus( String userID) {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            notiRef = FirebaseDatabase.getInstance().getReference("User Notis");
            notiRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(userID)) {
                        notiRef.child(userID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Notification noti = snapshot.getValue(Notification.class);
                                ArrayList<String> notis = noti.getNotis();
                                if (notis.contains(businessName)) {
                                    notiSubButton.setImageResource(R.drawable.ic_baseline_notifications_off_24);
                                    notiSubButton.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                                } else {
                                    notiSubButton.setImageResource(R.drawable.ic_baseline_notifications_24);
                                    notiSubButton.setBackgroundColor(getResources().getColor(R.color.grey));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        notiSubButton.setImageResource(R.drawable.ic_baseline_notifications_24);
                        notiSubButton.setBackgroundColor(getResources().getColor(R.color.grey));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void getFaveStatus(String businessID, String userID){
        favRef = FirebaseDatabase.getInstance().getReference("Favourites");
        favRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(userID).hasChild(businessID)){
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

                        if (snapshot.child(userID).hasChild(businessID)) {
                            favRef.child(userID).child(businessID).removeValue();
                        } else {
                            favRef.child(userID).child(businessID).setValue(businessName);
                        }
                    }
                }
                else{
                    Toast.makeText(BusinessPageActivity.this, "Login to add to favourites", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setNoti(String businessName,String userID) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference referenceNoti = FirebaseDatabase.getInstance().getReference("User Notis");
            referenceNoti.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(userID)) {
                        referenceNoti.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Notification noti = snapshot.getValue(Notification.class);
                                ArrayList<String> notis = noti.getNotis();
                                if (!notis.contains(businessName)) {
                                    noti.addNoti(businessName);
                                    referenceNoti.child(userID).setValue(noti);
                                    FirebaseMessaging.getInstance().subscribeToTopic(businessName.replace('\'', '-').replace(' ', '-')).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getApplicationContext(), "Subscribed", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    noti.deleteNoti(businessName);
                                    referenceNoti.child(userID).setValue(noti);
                                    FirebaseMessaging.getInstance().unsubscribeFromTopic(businessName.replace('\'', '-').replace(' ', '-')).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getApplicationContext(), "Unsubscribed", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        ArrayList<String> notis = new ArrayList<String>();
                        notis.add(businessName);
                        Notification noti = new Notification(notis);
                        referenceNoti.child(userID).setValue(noti);
                    }
                }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                }

            });
        }
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
            case R.id.OTButton:
                if(OTLayout.getVisibility()==View.VISIBLE){
                    OTLayout.setVisibility(View.GONE);
                    break;
                }
                OTLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.noti:
                setNoti(businessName,userID);
                break;
                }

        }
    }
