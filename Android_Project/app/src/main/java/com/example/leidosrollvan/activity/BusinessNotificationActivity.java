package com.example.leidosrollvan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.leidosrollvan.R;
import com.example.leidosrollvan.dataClasses.Business;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BusinessNotificationActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editNotiBody;
    private FirebaseUser user;
    String businessName;
    Button send;
    String body;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_noti);

        send=(Button) findViewById(R.id.send);
        send.setOnClickListener(this);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Businesses");
        user = FirebaseAuth.getInstance().getCurrentUser();
        String busId = user.getUid();
        reference.child(busId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Business businessProfile = snapshot.getValue(Business.class);
                if (businessProfile != null) {
                    businessName = businessProfile.businessName;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BusinessNotificationActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.send:
                editNotiBody = (EditText) findViewById(R.id.editTextNotiBody);
                body = editNotiBody.getText().toString().trim();
                if(!body.isEmpty()){
                    String topicName="/topics/"+businessName.replace('\'', '-').replace(' ', '-');
                    FcmSenderActivity sender = new FcmSenderActivity(topicName,businessName,body,getApplicationContext(),BusinessNotificationActivity.this);
                    sender.SendNotifications();
                    Toast.makeText(BusinessNotificationActivity.this,"Notification Sent",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(BusinessNotificationActivity.this,"Please enter text",Toast.LENGTH_LONG).show();
                }
                break;
        }

    }
}
