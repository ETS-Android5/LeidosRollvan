package com.example.leidosrollvan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.leidosrollvan.R;
import com.example.leidosrollvan.adapters.CategoryAdapter;
import com.example.leidosrollvan.dataClasses.Business;
import com.example.leidosrollvan.dataClasses.BusinessMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    DatabaseReference reference;
    DatabaseReference imRef;
    List<String> Categories;
    ArrayList<BusinessMenu> arrayList;
    ArrayList<Business> arrayList2;
    ArrayList<String> ds;
    ArrayList<String> test;
    ArrayAdapter<String> arrayAdapter;
    RecyclerView recyclerView;
    CategoryAdapter myAdapter;
    ArrayList<String> businessIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        //listView = findViewById(R.id.listView5);

        Categories = new ArrayList<>();
        //displayList = new ArrayList<>();
//ArrayList.contains("StringToBeChecked");
        Bundle bundle = getIntent().getExtras();
        String categoryName1 = bundle.getString("category");
        TextView theTextView = (TextView) findViewById(R.id.CategotyTesting);
        theTextView.setText(categoryName1);

        recyclerView = findViewById(R.id.CategoryRecyclerView);
        //listView = (ListView) findViewById(R.id.listView5);
        reference = FirebaseDatabase.getInstance().getReference("Businesses");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ds = new ArrayList<>();
        arrayList = new ArrayList<BusinessMenu>();
        arrayList2= new ArrayList<Business>();
        businessIdList = new ArrayList<>();
        test = new ArrayList<String>();

        myAdapter = new CategoryAdapter(this, arrayList2);
        recyclerView.setAdapter(myAdapter);

        imRef = FirebaseDatabase.getInstance().getReference("Businesses Menu").child("categories");

        imRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    arrayList.clear();
                    for (DataSnapshot dss : snapshot.getChildren()) {
                        BusinessMenu CatName = dss.getValue(BusinessMenu.class);
                        ArrayList<String> BusinessTest = CatName.getCategories();
                        if(BusinessTest.contains(categoryName1)){
                            test.add(dss.getKey());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        for(int i =0; i<test.size();i++){
            reference = FirebaseDatabase.getInstance().getReference("Businesses");
            reference.child(test.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Business business = dataSnapshot.getValue(Business.class);
                        arrayList2.add(business);


                    }
                    myAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }






    }

        /*public void DisplayInfo (View view)
        {

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Business business = dataSnapshot.getValue(Business.class);
                        arrayList.add(business);


                    }
                    myAdapter.notifyDataSetChanged();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }*/


        //theTextView.setText(categoryName1);



}


