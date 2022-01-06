package com.example.leidosrollvan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class BusinessRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView existingBusinessAccount;
    private EditText editBusinessTextName, editBusinessTextEmail, editBusinessTextMobile, editBusinessTextPassword;
    private ProgressBar businessProgressBar;
    private CircularProgressButton registerBusiness;


    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_register);
        changeStatusBarColor();

        mAuth = FirebaseAuth.getInstance();

        existingBusinessAccount = (TextView) findViewById(R.id.existingBusinessAccount);
        existingBusinessAccount.setOnClickListener(this);

        registerBusiness = (CircularProgressButton) findViewById(R.id.cirBusinessRegisterButton);
        registerBusiness.setOnClickListener(this);

        editBusinessTextName = (EditText) findViewById(R.id.editBusinessTextName);
        editBusinessTextEmail = (EditText) findViewById(R.id.editBusinessTextEmail);
        editBusinessTextMobile = (EditText) findViewById(R.id.editBusinessTextMobile);
        editBusinessTextPassword = (EditText) findViewById(R.id.editBusinessTextPassword);

        businessProgressBar = (ProgressBar) findViewById(R.id.businessRegisterProgressBar);
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void toBusinessLoginPage(View view) {
        startActivity(new Intent(this,BusinessLoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    public void registerBusiness(){
        String businessEmail = editBusinessTextEmail.getText().toString().trim();
        String businessName = editBusinessTextName.getText().toString().trim();
        String businessMobile = editBusinessTextMobile.getText().toString().trim();
        String businessPassword = editBusinessTextPassword.getText().toString().trim();

        if(businessName.isEmpty()){
            editBusinessTextName.setError("Name is Required!");
            editBusinessTextName.requestFocus();
            return;
        }

        if(businessMobile.isEmpty()){
            editBusinessTextMobile.setError("Mobile Number is Required!");
            editBusinessTextMobile.requestFocus();
            return;
        }

        if(businessEmail.isEmpty()){
            editBusinessTextEmail.setError("Email is Required!");
            editBusinessTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(businessEmail).matches()){
            editBusinessTextEmail.setError("Please provide valid email!");
            editBusinessTextEmail.requestFocus();
            return;
        }

        if(businessPassword.isEmpty()){
            editBusinessTextPassword.setError("Password is Required!");
            editBusinessTextPassword.requestFocus();
            return;
        }

        if(businessPassword.length() < 6){
            editBusinessTextPassword.setError("Minimum password length should be 6 characters");
            editBusinessTextPassword.requestFocus();
            return;
        }
        businessProgressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(businessEmail, businessPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Business business = new Business(businessName, businessMobile, businessEmail);

                            FirebaseDatabase.getInstance().getReference()
                                    .child("Businesses")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(business).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(BusinessRegisterActivity.this, "Business has been registered successfully", Toast.LENGTH_LONG).show();
                                        businessProgressBar.setVisibility(View.GONE);
                                        startActivity(new Intent(BusinessRegisterActivity.this, BusinessLoginActivity.class));

                                    }else {
                                        Log.e("LoginActivity", "Failed Registration", task.getException());
                                        Toast.makeText(BusinessRegisterActivity.this, "Failed to register Business, try again!", Toast.LENGTH_LONG).show();
                                        businessProgressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(BusinessRegisterActivity.this, "Failed to register business, try again!", Toast.LENGTH_LONG).show();
                            businessProgressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.existingBusinessAccount:
                startActivity(new Intent(this, BusinessLoginActivity.class));
                break;
            case R.id.cirBusinessRegisterButton:
                registerBusiness();
                break;
        }

    }
}