package com.example.leidosrollvan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class BusinessHomeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button businessHomeLogout;
    private ProgressBar businessHomeProgressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String businessID;
    private EditText productName, productPrice;
    private String productCategory,productSection;
    private Button saveButton,cancelButton;
    private TextView notifyNoItems,cat1,cat2,cat3,cat4,cat5;
    private RecyclerView breakfastSection,lunchSection,dinnerSection,dessertSection,drinksSection;
    private boolean paused = false;
    String[] categories =  {"Asian Cuisine","Kebab","Hot Dogs","Coffee and Tea","Burritos"};
    String[] sections =  {"Breakfast","Lunch","Dinner","Dessert","Drinks"};
    AutoCompleteTextView autoCompleteCategories;
    AutoCompleteTextView autoCompleteSections;
    ArrayAdapter<String> adapterCategories;
    ArrayAdapter<String> adapterSections;
    itemRecyclerAdapter adapter;
    ListView listview;
    EditText GetValue;
    BusinessMenu menu;
    HashMap<String, HashMap<String, Double>> menuMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_home);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });
        load();
    }

    protected void load(){
        cat1 = (TextView) findViewById(R.id.cat1);
        cat2 = (TextView) findViewById(R.id.cat2);
        cat3 = (TextView) findViewById(R.id.cat3);
        cat4 = (TextView) findViewById(R.id.cat4);
        cat5 = (TextView) findViewById(R.id.cat5);
        notifyNoItems = (TextView) findViewById(R.id.notifyNoItems);
        breakfastSection = (RecyclerView) findViewById(R.id.breakfastSection);
        lunchSection = (RecyclerView) findViewById(R.id.lunchSection);
        dinnerSection = (RecyclerView) findViewById(R.id.dinnerSection);
        dessertSection = (RecyclerView) findViewById(R.id.dessertSection);
        drinksSection = (RecyclerView) findViewById(R.id.drinksSection);

        businessHomeProgressBar = (ProgressBar) findViewById(R.id.business_home_progressBar);
        businessHomeProgressBar.setVisibility(View.VISIBLE);

        user = mAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Business Menu");
        businessID = user.getUid();
        businessHomeLogout = (Button) findViewById(R.id.business_home_logout);
        businessHomeLogout.setOnClickListener(this);
        businessHomeProgressBar.setVisibility(View.GONE);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(businessID + "/businessMenuItems")) {
                    reference.child(businessID).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                adapter = new itemRecyclerAdapter(oldMenu.getBusinessMenuItems().get("Breakfast"),"Breakfast");
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                breakfastSection.setLayoutManager(layoutManager);
                                breakfastSection.setItemAnimator(new DefaultItemAnimator());
                                breakfastSection.setAdapter(adapter);
                            }
                            if (sections.contains("Lunch")) {
                                cat2.setVisibility(View.VISIBLE);
                                lunchSection.setVisibility(View.VISIBLE);
                                oldMenu.getBusinessMenuItems().get("Lunch");
                                adapter = new itemRecyclerAdapter(oldMenu.getBusinessMenuItems().get("Lunch"),"Lunch");
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                lunchSection.setLayoutManager(layoutManager);
                                lunchSection.setItemAnimator(new DefaultItemAnimator());
                                lunchSection.setAdapter(adapter);
                            }
                            if (sections.contains("Dinner")) {
                                cat3.setVisibility(View.VISIBLE);
                                dinnerSection.setVisibility(View.VISIBLE);
                                oldMenu.getBusinessMenuItems().get("Dinner");
                                adapter = new itemRecyclerAdapter(oldMenu.getBusinessMenuItems().get("Dinner"),"Dinner");
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                dinnerSection.setLayoutManager(layoutManager);
                                dinnerSection.setItemAnimator(new DefaultItemAnimator());
                                dinnerSection.setAdapter(adapter);
                            }
                            if (sections.contains("Dessert")) {
                                cat4.setVisibility(View.VISIBLE);
                                dessertSection.setVisibility(View.VISIBLE);
                                oldMenu.getBusinessMenuItems().get("Dessert");
                                adapter = new itemRecyclerAdapter(oldMenu.getBusinessMenuItems().get("Dessert"),"Dessert");
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                dessertSection.setLayoutManager(layoutManager);
                                dessertSection.setItemAnimator(new DefaultItemAnimator());
                                dessertSection.setAdapter(adapter);
                            }
                            if (sections.contains("Drinks")) {
                                cat5.setVisibility(View.VISIBLE);
                                drinksSection.setVisibility(View.VISIBLE);
                                oldMenu.getBusinessMenuItems().get("Drinks");
                                adapter = new itemRecyclerAdapter(oldMenu.getBusinessMenuItems().get("Drinks"),"Drinks");
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                drinksSection.setLayoutManager(layoutManager);
                                drinksSection.setItemAnimator(new DefaultItemAnimator());
                                drinksSection.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(BusinessHomeActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
                        }

                    });
                } else {
                    notifyNoItems.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BusinessHomeActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void refresh(View v){
        this.recreate();
    }

    public void toAddPage(View view){
        startActivity(new Intent(this,  BusinessProductFormActivity.class));
    }

    public void toCategoriesPage(View view){
        startActivity(new Intent(this,  BusinessCategoryActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.business_home_logout:
                mAuth.getInstance().signOut();
                startActivity(new Intent(this, BusinessLoginActivity.class));
            case R.id.business_home_add:
                startActivity(new Intent(this, BusinessProductFormActivity.class));
        }
    }


}