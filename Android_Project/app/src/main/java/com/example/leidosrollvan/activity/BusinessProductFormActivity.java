package com.example.leidosrollvan.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.leidosrollvan.R;
import com.example.leidosrollvan.activitymethods.NameCheckMethods;
import com.example.leidosrollvan.dataClasses.BusinessMenu;
import com.example.leidosrollvan.staticClasses.InputValidation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
/*
TODO: Add form validation
 */
public class BusinessProductFormActivity extends AppCompatActivity implements View.OnClickListener {
    String[] sections =  {"Breakfast","Lunch","Dinner","Dessert","Drinks"};
    private EditText productName, productPrice;
    private Spinner spinnerSection;
    private DatabaseReference reference;
    private FirebaseUser user;
    private String businessID;
    private NameCheckMethods nameValidationClass = new NameCheckMethods();

    @Override

    /*
    On create, populate dropdown tables.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_product_form);
        Button saveButton = (Button) findViewById(R.id.popupSave);
        Button cancelButton = (Button) findViewById(R.id.popupCancel);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        //spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSection = findViewById(R.id.spinnerSection);
        //populate adapter
        ArrayAdapter adapter2 = new ArrayAdapter(this,R.layout.spinner_section,sections);


        //choose style of dropdown table
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set adapter to spinner (populate dropdown table)
        spinnerSection.setAdapter(adapter2);



    }

    /*
    Converts form into BusinessMenu Object then save into database.
     */
    public void save(){
        reference = FirebaseDatabase.getInstance().getReference("Business Menu");
        businessID = user.getUid();
        productName = (EditText) findViewById(R.id.productNamePopup);
        productPrice = (EditText) findViewById(R.id.productPricePopup);
        boolean productNameValidation = nameValidationClass.checkProductName(productName.getText().toString());
        boolean productPriceValidation = nameValidationClass.checkProductPrice(productPrice.getText().toString());
        String selectedSection = spinnerSection.getSelectedItem().toString().trim();
        String validatedName = InputValidation.encodeForFirebaseKey(productName.getText().toString().trim());
        if(productNameValidation){
            productName.setError("Invalid name");
            productName.requestFocus();
            return;
        }
        if(productPriceValidation){
            productPrice.setError("Invalid price");
            productPrice.requestFocus();
            return;
        }
        boolean validNum = true;
        validNum = productPrice.getText().toString().matches("-?\\d+(\\.\\d+)?");
        if(!validNum){
            productPrice.setError("Invalid price");
            productPrice.requestFocus();
            return;
        }
        if(productPrice.length()>=10){
            productPrice.setError("Invalid price");
            productPrice.requestFocus();
            return;
        }
        if(productName.length()>=50){
            productName.setError("Invalid Name");
            productName.requestFocus();
            return;
        }

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
                                spinnerSection = findViewById(R.id.spinnerSection);
                                Double selectedPriceD = Double.parseDouble(productPrice.getText().toString());
                                String selectedPrice = String.format("%.2f",selectedPriceD).trim();
                                HashMap<String, String> item = new HashMap<String, String>();
                                item.put(validatedName,selectedPrice);
                                oldMenu.addMenuItems(selectedSection,item);
                                //old menu is now updated with new items
                                FirebaseDatabase.getInstance().getReference("Business Menu")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(oldMenu);

                            }
                            //Only categories exist
                            else{
                                BusinessMenu oldMenu = snapshot.getValue(BusinessMenu.class);
                                spinnerSection = findViewById(R.id.spinnerSection);
                                Double selectedPriceD = Double.parseDouble(productPrice.getText().toString());
                                String selectedPrice = String.format("%.2f",selectedPriceD).trim();
                                String selectedSection = spinnerSection.getSelectedItem().toString().trim();
                                HashMap<String, String> item = new HashMap<String, String>();
                                HashMap<String, ArrayList<HashMap<String,String>>> businessMenuItems = new HashMap<String, ArrayList<HashMap<String,String>>>();
                                ArrayList<HashMap<String,String>> items = new ArrayList<HashMap<String,String>>();

                                //populate fields with form data
                                item.put(validatedName,selectedPrice);
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
                    spinnerSection = findViewById(R.id.spinnerSection);
                    Double selectedPriceD = Double.parseDouble(productPrice.getText().toString());
                    String selectedPrice = String.format("%.2f",selectedPriceD).trim();
                    String selectedSection = spinnerSection.getSelectedItem().toString().trim();
                    HashMap<String, String> item = new HashMap<String, String>();
                    HashMap<String, ArrayList<HashMap<String,String>>> businessMenuItems = new HashMap<String, ArrayList<HashMap<String,String>>>();
                    ArrayList<HashMap<String,String>> items = new ArrayList<HashMap<String,String>>();

                    //populate fields with form data
                    item.put(validatedName,selectedPrice);
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
        Intent i = new Intent(this, BusinessHomeActivity.class);
        startActivity(i);
    }


    public void cancel(View v){
        finish();
        startActivity(new Intent(this, BusinessHomeActivity.class));
    }

    public void clickSave(View v){
        save();
    }

    @Override
    public void onClick(View v) {

    }
}