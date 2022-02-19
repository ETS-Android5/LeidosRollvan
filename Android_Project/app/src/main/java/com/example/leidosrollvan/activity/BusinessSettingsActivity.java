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
import com.example.leidosrollvan.dataClasses.BusinessMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BusinessSettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference reference;
    private EditText businessName, businessPrice, businessOpening;
    private FirebaseUser user;
    private String businessID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_settings);
        Button saveButton = (Button) findViewById(R.id.popupSaveEdit);
        saveButton.setOnClickListener(this);
        Button cancelButton = (Button) findViewById(R.id.popupCancelEdit);
        cancelButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.popupSaveEdit:
                //save
                startActivity(new Intent(this, BusinessHomeActivity.class));
                break;
            case R.id.popupCancelEdit:
                finish();
                startActivity(new Intent(this, BusinessHomeActivity.class));
                break;
        }
    }
}