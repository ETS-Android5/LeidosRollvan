package com.example.leidosrollvan.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.leidosrollvan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class BusinessLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register, businessForgotPassword;
    private Button goToUserLogin;
    private EditText editBusinessTextEmail, editBusinessTextPassword;
    private CircularProgressButton businessLogin;

    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for changing status bar icon colors
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
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

        if(businessEmail.isEmpty()){
            editBusinessTextEmail.setError("Email is Required!");
            editBusinessTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(businessEmail).matches()){
            editBusinessTextEmail.setError("Please enter a valid Email!");
            editBusinessTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editBusinessTextPassword.setError("Password is required!");
            editBusinessTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            editBusinessTextPassword.setError("Minimum password length is 6 characters");
            editBusinessTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(businessEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(BusinessLoginActivity.this, BusinessHomeActivity.class));
                }else {
                    Toast.makeText( BusinessLoginActivity.this, "Failed to Login! Please check your credentials!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
    public void forgotPassword(){

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