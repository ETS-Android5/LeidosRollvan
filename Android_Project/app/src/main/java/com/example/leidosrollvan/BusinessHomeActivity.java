package com.example.leidosrollvan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.constants.ListAppsActivityContract;
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
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
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
    Button addButton;
    EditText GetValue;
    ArrayAdapter adapter;
    RecyclerView recyclerView;
    BusinessMenu menu;
    HashMap<String, HashMap<String, Double>> menuMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_home);

        addButton = findViewById(R.id.button1);
        businessHomeProgressBar = (ProgressBar) findViewById(R.id.business_home_progressBar);
        businessHomeProgressBar.setVisibility(View.VISIBLE);

        user = mAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Businesses");
        businessID = user.getUid();

        businessHomeLogout = (Button) findViewById(R.id.business_home_logout);
        businessHomeLogout.setOnClickListener(this);

        final TextView businessHomeName = (TextView) findViewById(R.id.business_home_name);
        final TextView businessHomeMobile = (TextView) findViewById(R.id.business_home_mobile);

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
        businessHomeName.setVisibility(View.VISIBLE);
        businessHomeMobile.setVisibility(View.VISIBLE);
    }



    public void toAddPage(View view){
        startActivity(new Intent(this,  BusinessProductFormActivity.class));
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