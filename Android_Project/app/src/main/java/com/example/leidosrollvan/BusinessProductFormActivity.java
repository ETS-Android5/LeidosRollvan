package com.example.leidosrollvan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BusinessProductFormActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText productName, productPrice;
    private String productSection;
    private Button saveButton,cancelButton;
    private Spinner spinnerCategory,spinnerSection;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String businessID;
    Button SaveButton;
    String[] sections =  {"Breakfast","Lunch","Dinner","Dessert","Drinks"};
    AutoCompleteTextView autoCompleteSections;
    ArrayAdapter<String> adapterSections;
    @Override

    /*
    On create, populate dropdown tables.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_product_form);
        saveButton = (Button) findViewById(R.id.popupSave);
        cancelButton = (Button) findViewById(R.id.popupCancel);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getInstance().getCurrentUser();
        //spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSection = findViewById(R.id.spinnerSection);

        //populate adapter
        //ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,categories);
        ArrayAdapter adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,sections);


        //choose style of dropdown table
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set adapter to spinner (populate dropdown table)
        //spinnerCategory.setAdapter(adapter);
        spinnerSection.setAdapter(adapter2);



    }

    /*
    Converts form into BusinessMenu Object then save into database.
    NO FORM VALIDATION
     */
    public void save(){

        reference = FirebaseDatabase.getInstance().getReference("Business Menu");
        businessID = user.getUid();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(businessID)){//Menu or Categories exists
                    reference.child(businessID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //Menu exists
                            if(snapshot.hasChild("businessMenuItems")){
                                BusinessMenu oldMenu = snapshot.getValue(BusinessMenu.class);
                                productName = (EditText) findViewById(R.id.productNamePopup);
                                productPrice = (EditText) findViewById(R.id.productPricePopup);
                                spinnerSection = findViewById(R.id.spinnerSection);
                                String selectedName = productName.getText().toString().trim();
                                String selectedPrice = productPrice.getText().toString().trim();
                                String selectedSection = spinnerSection.getSelectedItem().toString().trim();
                                HashMap<String, String> item = new HashMap<String, String>();
                                item.put(selectedName,selectedPrice);
                                oldMenu.addMenuItems(selectedSection,item);
                                //old menu is now updated with new items
                                FirebaseDatabase.getInstance().getReference("Business Menu")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(oldMenu);
                            }
                            //Only categories exist
                            else{
                                BusinessMenu oldMenu = snapshot.getValue(BusinessMenu.class);
                                productName = (EditText) findViewById(R.id.productNamePopup);
                                productPrice = (EditText) findViewById(R.id.productPricePopup);
                                spinnerSection = findViewById(R.id.spinnerSection);
                                String selectedName = productName.getText().toString().trim();
                                String selectedPrice = productPrice.getText().toString().trim();

                                String selectedSection = spinnerSection.getSelectedItem().toString().trim();
                                HashMap<String, String> item = new HashMap<String, String>();
                                HashMap<String, ArrayList<HashMap<String,String>>> businessMenuItems = new HashMap<String, ArrayList<HashMap<String,String>>>();
                                ArrayList<HashMap<String,String>> items = new ArrayList<HashMap<String,String>>();

                                //populate fields with form data
                                item.put(selectedName,selectedPrice);
                                items.add(item);
                                businessMenuItems.put(selectedSection,items);

                                //Create object with form data
                                BusinessMenu newMenu = new BusinessMenu(businessMenuItems,oldMenu.getCategories());
                                FirebaseDatabase.getInstance().getReference("Business Menu")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(newMenu);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(BusinessProductFormActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
                        }

                    });
                }
                //Menu and categories don't exist
                else{
                    productName = (EditText) findViewById(R.id.productNamePopup);
                    productPrice = (EditText) findViewById(R.id.productPricePopup);

                    spinnerSection = findViewById(R.id.spinnerSection);
                    String selectedName = productName.getText().toString().trim();
                    String selectedPrice = productPrice.getText().toString().trim();
                    String selectedSection = spinnerSection.getSelectedItem().toString().trim();
                    HashMap<String, String> item = new HashMap<String, String>();
                    HashMap<String, ArrayList<HashMap<String,String>>> businessMenuItems = new HashMap<String, ArrayList<HashMap<String,String>>>();
                    ArrayList<HashMap<String,String>> items = new ArrayList<HashMap<String,String>>();

                    //populate fields with form data
                    item.put(selectedName,selectedPrice);
                    items.add(item);
                    businessMenuItems.put(selectedSection,items);

                    //Create object with form data. categories list empty.
                    BusinessMenu newMenu = new BusinessMenu(businessMenuItems,new ArrayList<String>());
                    FirebaseDatabase.getInstance().getReference("Business Menu")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(newMenu);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BusinessProductFormActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void cancel(View v){
        finish();
        startActivity(new Intent(this, BusinessHomeActivity.class));
    }

    public void clickSave(View v){
        save();
        startActivity(new Intent(this, BusinessHomeActivity.class));
    }

    @Override
    public void onClick(View v) {

    }
}