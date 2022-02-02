package com.example.leidosrollvan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.leidosrollvan.dataClasses.Business;
import com.example.leidosrollvan.dataClasses.BusinessImage;
import com.example.leidosrollvan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BusinessPageActivity extends AppCompatActivity implements View.OnClickListener {
    DatabaseReference reference;
    DatabaseReference imRef;
    Button homeButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_page);

        homeButton = (Button) findViewById(R.id.home_bus);
        homeButton.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        String b_id = bundle.getString("b_id");

        TextView businessPageName = (TextView) findViewById(R.id.business_page_name);
        TextView businessPageMob = (TextView) findViewById(R.id.business_page_mob);
        TextView businessPageEmail = (TextView) findViewById(R.id.business_page_email);
        ImageView businessPageImg = (ImageView) findViewById(R.id.busi_page_Image);
        reference = FirebaseDatabase.getInstance().getReference("Businesses");
        reference.child(b_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Business businessProfile = snapshot.getValue(Business.class);
                if (businessProfile != null) {
                    String businessName = businessProfile.businessName;
                    String businessMobile = businessProfile.businessMobile;
                    String businessEmail= businessProfile.businessEmail;
                    businessPageName.setText(businessName);
                    businessPageMob.setText(businessMobile);
                    businessPageEmail.setText(businessEmail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BusinessPageActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
            }
        });

        imRef = FirebaseDatabase.getInstance().getReference("Business Images");
        imRef.child(b_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String uri = snapshot.getValue(BusinessImage.class).mImageUrl;
                    Picasso.with(businessPageImg.getContext()).load(uri).into(businessPageImg);
                } else {
                    businessPageImg.setImageResource(R.drawable.ic_baseline_image_not_supported_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Image retrieval error",error.getMessage());
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_bus:
                startActivity(new Intent(this, MainActivity.class));
        }
    }
}
