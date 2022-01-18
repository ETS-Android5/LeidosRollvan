package com.example.leidosrollvan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BusinessCategoryActivity extends AppCompatActivity{

    private final String[] categories = {"Asian Cuisine", "Kebab", "Hot Dogs", "Coffee and Tea", "Burritos"};
    private final ArrayList<String> initialCategories = new ArrayList<String>();
    private final ArrayList<String> finalCategories = new ArrayList<String>();
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String businessID;
    Context context  = this;
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_categories);
        //Firebase stuff
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Business Menu");
        businessID = user.getUid();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(businessID+"/categories")){
                    reference.child(businessID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            BusinessMenu oldMenu = snapshot.getValue(BusinessMenu.class);
                            for(String category : oldMenu.getCategories()){
                                initialCategories.add(category);
                            }
                            ViewGroup checkboxContainer = (ViewGroup) findViewById(R.id.checkboxes);
                            int count = 0;
                            for (String category : categories) {
                                CheckBox checkBox = new CheckBox(context);
                                checkBox.setText(category);
                                checkBox.setId(count);
                                count++;
                                //if category was previously selected and saved, show as ticked on load.
                                if(initialCategories.contains(category)){
                                    checkBox.setChecked(true);
                                }
                                checkboxContainer.addView(checkBox);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(BusinessCategoryActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
                        }

                    });
                }
                else{
                    ViewGroup checkboxContainer = (ViewGroup) findViewById(R.id.checkboxes);
                    int count = 0;
                    for (String category : categories) {
                        CheckBox checkBox = new CheckBox(context);
                        checkBox.setText(category);
                        checkBox.setId(count);
                        count++;
                        checkboxContainer.addView(checkBox);
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BusinessCategoryActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void save(){
        ViewGroup checkboxContainer = (ViewGroup) findViewById(R.id.checkboxes);

        for(int i = 0; i<categories.length;i++){
            CheckBox checkBox = (CheckBox) findViewById(i);
            if(checkBox.isChecked()){
                finalCategories.add(checkBox.getText().toString());
            }
        }
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Business Menu");
        businessID = user.getUid();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.hasChild(businessID+"/categories")){
                reference.child(businessID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        BusinessMenu oldMenu = snapshot.getValue(BusinessMenu.class);
                        oldMenu.setCategories(finalCategories);
                        FirebaseDatabase.getInstance().getReference("Business Menu")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(oldMenu);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(BusinessCategoryActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else{
                if(snapshot.hasChild(businessID+"/businessMenuItems")){
                    reference.child(businessID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            BusinessMenu oldMenu = snapshot.getValue(BusinessMenu.class);
                            oldMenu.setCategories(finalCategories);
                            FirebaseDatabase.getInstance().getReference("Business Menu")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(oldMenu);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(BusinessCategoryActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    HashMap<String, ArrayList<HashMap<String,String>>> businessMenuItems = new HashMap<String, ArrayList<HashMap<String,String>>>();
                    BusinessMenu newMenu = new BusinessMenu(businessMenuItems,finalCategories);
                    FirebaseDatabase.getInstance().getReference("Business Menu")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(newMenu);
                }

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(BusinessCategoryActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
        }
    });


    }

    public void toBusinessHome(View view){
        save();
        startActivity(new Intent(this,  BusinessHomeActivity.class));

    }
}