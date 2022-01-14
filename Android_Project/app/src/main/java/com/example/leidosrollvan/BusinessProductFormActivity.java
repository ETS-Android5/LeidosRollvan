package com.example.leidosrollvan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class BusinessProductFormActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText productName, productPrice;
    private String productCategory,productSection;
    private Button saveButton,cancelButton;
    private Spinner spinnerCategory,spinnerSection;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String businessID;
    String[] categories =  {"Asian Cuisine","Kebab","Hot Dogs","Coffee and Tea","Burritos"};
    String[] sections =  {"Breakfast","Lunch","Dinner","Dessert","Drinks"};
    AutoCompleteTextView autoCompleteCategories;
    AutoCompleteTextView autoCompleteSections;
    ArrayAdapter<String> adapterCategories;
    ArrayAdapter<String> adapterSections;
    @Override

    /*
    On create, populate dropdown tables.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_popup);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getInstance().getCurrentUser();
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSection = findViewById(R.id.spinnerSection);

        //populate adapter
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,categories);
        ArrayAdapter adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,sections);


        //choose style of dropdown table
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set adapter to spinner (populate dropdown table)
        spinnerCategory.setAdapter(adapter);
        spinnerSection.setAdapter(adapter2);



    }

    /*
    Converts form into BusinessMenu Object then save into database.
    NO FORM VALIDATION
     */
    public void save(){
        productName = (EditText) findViewById(R.id.productNamePopup);
        productPrice = (EditText) findViewById(R.id.productPricePopup);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSection = findViewById(R.id.spinnerSection);
        String selectedName = productName.getText().toString();
        String selectedPrice = productPrice.getText().toString();
        String selectedCategory = spinnerCategory.getSelectedItem().toString();
        String selectedSection = spinnerSection.getSelectedItem().toString();


        reference = FirebaseDatabase.getInstance().getReference("Business Menu");
        businessID = user.getUid();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(businessID)){
                    reference = FirebaseDatabase.getInstance().getReference("Business Menu");
                    businessID = user.getUid();
                    final BusinessMenu[] menu = new BusinessMenu[1];
                    reference.child(businessID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            BusinessMenu oldMenu = snapshot.getValue(BusinessMenu.class);
                            HashMap<String, String> item = new HashMap<String, String>();
                            //populate fields with form data
                            oldMenu.addCategories(selectedCategory);
                            item.put(selectedName,selectedPrice);
                            oldMenu.addMenuItems(selectedSection,item);
                            //old menu is now updated with new items
                            FirebaseDatabase.getInstance().getReference("Business Menu")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(oldMenu);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(BusinessProductFormActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
                        }

                    });
                }

                else{
                    HashMap<String, String> item = new HashMap<String, String>();
                    HashMap<String, ArrayList<HashMap<String,String>>> businessMenuItems = new HashMap<String, ArrayList<HashMap<String,String>>>();
                    ArrayList<HashMap<String,String>> items = new ArrayList<HashMap<String,String>>();
                    ArrayList<String> categories = new ArrayList<String>();

                    //populate fields with form data
                    categories.add(selectedCategory);
                    item.put(selectedName,selectedPrice);
                    items.add(item);
                    businessMenuItems.put(selectedSection,items);

                    //Create object with form data
                    BusinessMenu newMenu = new BusinessMenu(businessMenuItems,categories);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.popupCancel:
                startActivity(new Intent(this, BusinessHomeActivity.class));
            case R.id.popupSave:
                save();
                startActivity(new Intent(this, BusinessHomeActivity.class));
        }
    }
}