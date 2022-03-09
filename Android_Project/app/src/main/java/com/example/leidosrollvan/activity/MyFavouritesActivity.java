package com.example.leidosrollvan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.leidosrollvan.R;
import com.example.leidosrollvan.adapters.FaveAdapter;
import com.example.leidosrollvan.dataClasses.Business;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MyFavouritesActivity extends AppCompatActivity {
    TextView noFave;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private String userID, businessID, busName;
    private ArrayList<String> favesList = new ArrayList();
    private Object busID, businessName;
    //private Object ListView;
    private ListView favoritesList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favourites);
        load();
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefreshMyFave);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
                pullToRefresh.setRefreshing(false);
            }
        });
        //load();
        favesList.add("test");
        favesList.add("test2");
        favesList.add("test3");
        ListView listView = (ListView) findViewById(R.id.favoritesList);
        FaveAdapter adapter = new FaveAdapter(getApplicationContext(), favesList);
        listView.setAdapter(adapter);
    }



    void load(){


        user = mAuth.getInstance().getCurrentUser();
        if(user!= null) {
            userID = user.getUid();
        }

        DatabaseReference faveRef = FirebaseDatabase.getInstance().getReference("Favourites");

        faveRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(userID)) {
                    //ArrayList faveList = new ArrayList();
                    faveRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<String> favesList = new ArrayList();
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                businessName = snapshot1.getValue();
                                busName = String.valueOf(businessName);
                                favesList.add(busName);
//                                favesList.add(businessName);
//                                DatabaseReference busRef = FirebaseDatabase.getInstance().getReference("Businesses");
//                                busRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        if(snapshot.hasChild(businessID)){
//                                            businessName = snapshot.child("businessName").getValue();
//                                            busName = String.valueOf(businessName);
//                                            favesList.add(busName);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
                            }
//                                if(favesList.isEmpty()==true){
//                                    noFave.setVisibility(View.VISIBLE);
//                                }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    noFave.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}





