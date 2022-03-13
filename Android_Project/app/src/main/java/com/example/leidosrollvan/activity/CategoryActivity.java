package com.example.leidosrollvan.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoryActivity extends AppCompatActivity implements CategoryAdapter.RecyclerViewClickInterface {
    DatabaseReference reference;
    DatabaseReference imRef;
    List<String> Categories;
    RecyclerView recyclerView;
    CategoryAdapter myAdapter;
    List<Business> listData;
    List<String> offerIdList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Categories = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        String categoryName1 = bundle.getString("category");
        TextView theTextView = (TextView) findViewById(R.id.CategotyTesting);
        theTextView.setText(categoryName1);

        recyclerView = findViewById(R.id.CategoryRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new CategoryAdapter(this,this);
        recyclerView.setAdapter(myAdapter);

        imRef = FirebaseDatabase.getInstance().getReference("Business Menu");

        imRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    GenericTypeIndicator<HashMap<String, BusinessMenu>> t = new GenericTypeIndicator<HashMap<String, BusinessMenu>>() {
                    };

                    HashMap<String, BusinessMenu> menuMap = snapshot.getValue(t);
                    if (menuMap != null) {

                        processBusinessSelection(menuMap, categoryName1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void processBusinessSelection(HashMap<String, BusinessMenu> menuMap, String category) {
        reference = FirebaseDatabase.getInstance().getReference("Businesses");
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //(offerIdList as ArrayList<>).clear;
                if (snapshot.exists()) {
                    GenericTypeIndicator<HashMap<String, Business>> t = new GenericTypeIndicator<HashMap<String, Business>>() {
                    };

                    HashMap<String, Business> businessMap = snapshot.getValue(t);
                    if (businessMap != null) {

                        HashMap<String, BusinessMenu> filteredMap = new HashMap<>(
                                menuMap.entrySet().stream().filter(
                                        a -> a.getValue().categories.contains(category)
                                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                        );
                        listData =
                                businessMap.entrySet().stream().filter(
                                        a -> filteredMap.containsKey(a.getKey())
                                ).map(Map.Entry::getValue).collect(Collectors.toList());
                        offerIdList = businessMap.entrySet().stream().filter(
                                a -> filteredMap.containsKey(a.getKey())
                        ).map(Map.Entry::getKey).collect(Collectors.toList());

                        //offerIdList = businessMap.keySet().stream().collect(Collectors.toList());
                        myAdapter.setData(new ArrayList<>(listData),new ArrayList<>(offerIdList));
                        if (listData.size() == 0) {
                            TextView NoAvailable = (TextView) findViewById(R.id.textViewNoAvailable);
                            NoAvailable.setText("No businesses available");
                        }
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, BusinessPageActivity.class);
        intent.putExtra("b_id", offerIdList.get(position));
        startActivity(intent);
    }
}