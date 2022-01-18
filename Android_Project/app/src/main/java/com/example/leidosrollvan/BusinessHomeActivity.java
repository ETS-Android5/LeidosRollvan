package com.example.leidosrollvan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    String[] categories =  {"Asian Cuisine","Kebab","Hot Dogs","Coffee and Tea","Burritos"};
    String[] sections =  {"Breakfast","Lunch","Dinner","Dessert","Drinks"};
    AutoCompleteTextView autoCompleteCategories;
    AutoCompleteTextView autoCompleteSections;
    ArrayAdapter<String> adapterCategories;
    ArrayAdapter<String> adapterSections;
    ListView listview;
    EditText GetValue;
    ArrayAdapter adapter;
    RecyclerView recyclerView;
    BusinessMenu menu;
    HashMap<String, HashMap<String, Double>> menuMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_home);

        businessHomeProgressBar = (ProgressBar) findViewById(R.id.business_home_progressBar);
        businessHomeProgressBar.setVisibility(View.VISIBLE);

        user = mAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Businesses");
        businessID = user.getUid();

        businessHomeLogout = (Button) findViewById(R.id.business_home_logout);
        businessHomeLogout.setOnClickListener(this);


        reference.child(businessID).child("businessMenu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               // Business businessProfile = snapshot.getValue(Business.class);
                BusinessMenu menu = snapshot.getValue(BusinessMenu.class);

                if (menu != null){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BusinessHomeActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
            }
        });

        businessHomeProgressBar.setVisibility(View.GONE);
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