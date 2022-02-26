package com.example.leidosrollvan.activity;

import android.app.ActionBar;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.leidosrollvan.R;
import com.example.leidosrollvan.dataClasses.Business;
import com.example.leidosrollvan.dataClasses.BusinessMenu;
import com.example.leidosrollvan.dataClasses.OpeningTimes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BusinessSettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference reference;
    private EditText businessName, businessMobile;
    private Button openingTime, closingTime;
    private CheckBox sun,mon,tue,wed,thu,fri,sat;
    private FirebaseUser user;
    private String businessID;
    int hour;
    int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_settings);
        Button saveButton = (Button) findViewById(R.id.popupSaveEdit);
        saveButton.setOnClickListener(this);
        Button cancelButton = (Button) findViewById(R.id.popupCancelEdit);
        cancelButton.setOnClickListener(this);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        //Get IDs
        businessName = (EditText) findViewById(R.id.businessNamePopup);
        businessMobile = (EditText) findViewById(R.id.businessMobilePopup);
        openingTime = (Button) findViewById(R.id.openingButton);
        openingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(BusinessSettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hour = hourOfDay;
                        minute = minute;
                        openingTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                    }
                // int style = AlertDialog.THEME_HOLO_DARK;
            },12,0,true);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            };
        });
        closingTime = (Button) findViewById(R.id.closingButton);
        closingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(BusinessSettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hour = hourOfDay;
                        minute = minute;
                        closingTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                    }
                    // int style = AlertDialog.THEME_HOLO_DARK;
                },12,0,true);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            };
        });
        sun = (CheckBox) findViewById(R.id.SundayBox);
        mon = (CheckBox) findViewById(R.id.MondayBox);
        tue = (CheckBox) findViewById(R.id.TuesdayBox);
        wed = (CheckBox) findViewById(R.id.WednesdayBox);
        thu = (CheckBox) findViewById(R.id.ThursdayBox);
        fri = (CheckBox) findViewById(R.id.FridayBox);
        sat = (CheckBox) findViewById(R.id.SaturdayBox);

        //Get business data class and show current business details
        String b_id = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Businesses");
        reference.child(b_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Business businessProfile = snapshot.getValue(Business.class);
                if (businessProfile != null) {
                    businessName.setText(businessProfile.businessName);
                    businessMobile.setText(businessProfile.businessMobile);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BusinessSettingsActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
            }
        });

        //Get OpeningTime data class and show current opening time.
        reference = FirebaseDatabase.getInstance().getReference("Business OT");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(b_id)){ //if previous data found, then load into boxes
                    reference.child(b_id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            OpeningTimes OT = snapshot.getValue(OpeningTimes.class);
                            openingTime.setText(String.format("%02d:%02d",OT.getHourOpening(),OT.getMinuteOpening()));
                            closingTime.setText(String.format("%02d:%02d",OT.getHourClosing(),OT.getMinuteClosing()));
                            if(OT.getDaysOfWeek().contains("Sunday")){
                                sun.setChecked(true);
                            }
                            if(OT.getDaysOfWeek().contains("Monday")){
                                mon.setChecked(true);
                            }
                            if(OT.getDaysOfWeek().contains("Tuesday")){
                                tue.setChecked(true);
                            }
                            if(OT.getDaysOfWeek().contains("Wednesday")){
                                wed.setChecked(true);
                            }
                            if(OT.getDaysOfWeek().contains("Thursday")){
                                thu.setChecked(true);
                            }
                            if(OT.getDaysOfWeek().contains("Friday")){
                                fri.setChecked(true);
                            }
                            if(OT.getDaysOfWeek().contains("Saturday")){
                                sat.setChecked(true);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(BusinessSettingsActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BusinessSettingsActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
            }
        });
        //End of show business details

    }

    public void save() {
        //get all fields and validate
        String businessNameEdited = businessName.getText().toString().trim();
        String businessMobileEdited = businessMobile.getText().toString().trim();
        if(businessNameEdited.length()<1){
            businessName.setError("Invalid Name");
            businessName.requestFocus();
            return;
        }
        boolean validNum = true;
        validNum = businessMobile.getText().toString().matches("-?\\d+(\\.\\d+)?");
        if(!validNum){
            businessMobile.setError("Invalid number");
            businessMobile.requestFocus();
            return;
        }
        int openingTimeHours = Integer.parseInt(openingTime.getText().toString().split(":")[0]);
        int openingTimeMinutes = Integer.parseInt(openingTime.getText().toString().split(":")[1]);
        int closingTimeHours = Integer.parseInt(closingTime.getText().toString().split(":")[0]);
        int closingTimeMinutes = Integer.parseInt(closingTime.getText().toString().split(":")[1]);
        OpeningTimes OT = new OpeningTimes(openingTimeHours, openingTimeMinutes, closingTimeHours, closingTimeMinutes);
        if (sun.isChecked()) {
            OT.addDaysOfWeek("Sunday");
        }
        if (mon.isChecked()) {
            OT.addDaysOfWeek("Monday");
        }
        if (tue.isChecked()) {
            OT.addDaysOfWeek("Tuesday");
        }
        if (wed.isChecked()) {
            OT.addDaysOfWeek("Wednesday");
        }
        if (thu.isChecked()) {
            OT.addDaysOfWeek("Thursday");
        }
        if (fri.isChecked()) {
            OT.addDaysOfWeek("Friday");
        }
        if (sat.isChecked()) {
            OT.addDaysOfWeek("Saturday");
        }

        String b_id = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Business OT");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseDatabase.getInstance().getReference("Business OT")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(OT);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BusinessSettingsActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
            }

        });
        reference = FirebaseDatabase.getInstance().getReference("Businesses");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Business businessProfile = snapshot.getValue(Business.class);
                Business newBusinessProfile = new Business(businessNameEdited,businessMobileEdited,businessProfile.businessEmail);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Businesses")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                Map<String,Object> updates = new HashMap<String,Object>();
                updates.put("businessMobile",businessMobileEdited);
                updates.put("businessName",businessNameEdited);
                ref.updateChildren(updates);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.popupSaveEdit:
                save();
                startActivity(new Intent(this, BusinessHomeActivity.class));
                break;
            case R.id.popupCancelEdit:
                finish();
                startActivity(new Intent(this, BusinessHomeActivity.class));
                break;
        }
    }
}