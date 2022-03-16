package com.example.leidosrollvan.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.leidosrollvan.R;
import com.example.leidosrollvan.activitymethods.EmailCheckMethods;
import com.example.leidosrollvan.activitymethods.PasswordCheckMethods;
import com.example.leidosrollvan.dataClassesForMethods.EmailPasswordResponseModel;
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

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class BusinessLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register, businessForgotPassword;
    private Button goToUserLogin;
    private EditText editBusinessTextEmail, editBusinessTextPassword;
    private CircularProgressButton businessLogin;

    private EmailCheckMethods emailCheckClass = new EmailCheckMethods();
    private PasswordCheckMethods passwordCheckClass = new PasswordCheckMethods();

    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private String userID;
    private DatabaseReference verifyRef;
    private boolean verified = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        //for changing status bar icon colors
//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
        setContentView(R.layout.activity_business_login);

        register = (TextView) findViewById(R.id.businessRegister);
        register.setOnClickListener(this);

        goToUserLogin = (Button) findViewById(R.id.gotoUserLogin);
        goToUserLogin.setOnClickListener(this);

        businessLogin = (CircularProgressButton) findViewById(R.id.cirBusinessLoginButton);
        businessLogin.setOnClickListener(this);

        editBusinessTextEmail = (EditText) findViewById(R.id.editBusinessLoginTextEmail);
        editBusinessTextPassword= (EditText) findViewById(R.id.editBusinessLoginTextPassword);

        businessForgotPassword = (TextView) findViewById(R.id.businessForgotPassword);
        businessForgotPassword.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.businessLoginProgressBar);
        mAuth = FirebaseAuth.getInstance();
    }

    public void toBusinessRegisterPage(View view){
        startActivity(new Intent(BusinessLoginActivity.this,  BusinessRegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
    }

    public void businessLogin(){

        //startActivity(new Intent(this, BusinessHomeActivity.class));
        String businessEmail = editBusinessTextEmail.getText().toString().trim();
        String password = editBusinessTextPassword.getText().toString().trim();

        EmailPasswordResponseModel checkedEmail = emailCheckClass.checkEmail(businessEmail);
        EmailPasswordResponseModel checkedPassword = passwordCheckClass.checkPassword(password);

        if(!checkedEmail.getStatus()){
            editBusinessTextEmail.setError(checkedEmail.getMessage());
            editBusinessTextEmail.requestFocus();
            return;
        }

        if (!checkedPassword.getStatus()) {
            editBusinessTextPassword.setError(checkedPassword.getMessage());
            editBusinessTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(businessEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser user = mAuth.getCurrentUser();
                    userID = user.getUid();

                    if(user.isEmailVerified() == true){
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(BusinessLoginActivity.this, BusinessHomeActivity.class));
                    }
                    else {
                        isVerified();
                        if(verified == true){
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(BusinessLoginActivity.this, BusinessHomeActivity.class));
                        }



                        //mAuth.signOut();
                        //Toast.makeText(BusinessLoginActivity.this, "Verify your email before logging in", Toast.LENGTH_SHORT).show();
                        //user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        //    @Override
                        //    public void onSuccess(Void aVoid) {
                        //        Toast.makeText(BusinessLoginActivity.this, "Verification email has been sent.", Toast.LENGTH_SHORT).show();
                        //    }
                        //});
                    }
                }
                else {
                    Toast.makeText( BusinessLoginActivity.this, "Failed to Login! Please check your credentials!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
    public void forgotPassword(){

    }
    public void isVerified(){
        verifyRef = FirebaseDatabase.getInstance().getReference("Users still to verify");
        verifyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(userID)){
                    mAuth.signOut();
                    Toast.makeText(BusinessLoginActivity.this, "Verify your email before logging in", Toast.LENGTH_SHORT).show();
                    verified = false;
                }
                else{
                    verified = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.cirBusinessLoginButton:
                businessLogin();
                break;
            case R.id.gotoUserLogin:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.businessRegister:
                startActivity(new Intent(this, BusinessRegisterActivity.class));
                break;
            case R.id.businessForgotPassword:
                startActivity(new Intent(this, businessForgotPasswordActivity.class));
                break;
        }

    }
}