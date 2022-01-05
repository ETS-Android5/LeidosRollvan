package com.example.leidosrollvan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BusinessHomeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button businessHomeLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_home);

        businessHomeLogout = (Button) findViewById(R.id.business_home_logout);
        businessHomeLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.business_home_logout:
                startActivity(new Intent(this, BusinessLoginActivity.class));
        }
    }
}