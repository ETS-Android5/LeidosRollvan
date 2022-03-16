package com.example.leidosrollvan.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.leidosrollvan.R;
import com.example.leidosrollvan.activitymethods.BusinessNameMobileCheckMethods;
import com.example.leidosrollvan.activitymethods.EmailCheckMethods;
import com.example.leidosrollvan.activitymethods.PasswordCheckMethods;
import com.example.leidosrollvan.dataClasses.User;
import com.example.leidosrollvan.dataClassesForMethods.EmailPasswordResponseModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView existingAccount;
    private EditText editTextName, editTextEmail, editTextMobile, editTextPassword;
    private ProgressBar progressBar;
    private CircularProgressButton registerUser;

    private EmailCheckMethods emailCheckClass = new EmailCheckMethods();
    private PasswordCheckMethods passwordCheckClass = new PasswordCheckMethods();
    private BusinessNameMobileCheckMethods nameMobileCheckClass = new BusinessNameMobileCheckMethods();

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();

        mAuth = FirebaseAuth.getInstance();

        existingAccount = (TextView) findViewById(R.id.existingAccount);
        existingAccount.setOnClickListener(this);

        registerUser = (CircularProgressButton) findViewById(R.id.cirRegisterButton);
        registerUser.setOnClickListener(this);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextMobile = (EditText) findViewById(R.id.editTextMobile);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            //window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void toLoginPage(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    public void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String mobile = editTextMobile.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        Boolean checkedName = nameMobileCheckClass.checkBusinessName(name);
        Boolean checkedMobile = nameMobileCheckClass.checkBusinessMobile(mobile);
        EmailPasswordResponseModel checkedEmail = emailCheckClass.checkEmail(email);
        EmailPasswordResponseModel checkedPassword = passwordCheckClass.checkPassword(password);

        if(checkedName){
            editTextName.setError("Name is Required!");
            editTextName.requestFocus();
            return;
        }

        if(checkedMobile){
            editTextMobile.setError("Mobile Number is Required!");
            editTextMobile.requestFocus();
            return;
        }

        if(!checkedEmail.getStatus()){
            editTextEmail.setError(checkedEmail.getMessage());
            editTextEmail.requestFocus();
            return;
        }

        if(!checkedPassword.getStatus()){
            editTextPassword.setError(checkedPassword.getMessage());
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(name, mobile, email);

                            FirebaseDatabase.getInstance().getReference()
                                    .child("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        //Toast.makeText(RegisterActivity.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(RegisterActivity.this, "Verification email has been sent.", Toast.LENGTH_SHORT).show();
                                            }

                                        });
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                                    }else {
                                        Toast.makeText(RegisterActivity.this, "Failed to register user, try again!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(RegisterActivity.this, "Failed to register user, try again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.existingAccount:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                break;
            case R.id.cirRegisterButton:
                registerUser();
                break;
        }
    }
}