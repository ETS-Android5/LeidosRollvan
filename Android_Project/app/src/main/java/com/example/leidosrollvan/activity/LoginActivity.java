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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register, continueAsGuest, forgotPasswordText;
    private EditText editTextEmail, editTextPassword;
    private CircularProgressButton login;
    private Button goToBusinessLogin;
    private EmailCheckMethods emailHelperClass = new EmailCheckMethods();
    private PasswordCheckMethods passwordHelperClass = new PasswordCheckMethods();

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        //for changing status bar icon colors
//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
        setContentView(R.layout.activity_login);
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        continueAsGuest = (TextView) findViewById(R.id.continueGuest);
        continueAsGuest.setOnClickListener(this);

        goToBusinessLogin = (Button) findViewById(R.id.gotoBusinessLogin);
        goToBusinessLogin.setOnClickListener(this);

        login = (CircularProgressButton) findViewById(R.id.cirLoginButton);
        login.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.editLoginTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editLoginTextPassword);

        forgotPasswordText = (TextView) findViewById(R.id.forgotPasswordText);
        forgotPasswordText.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.loginProgressBar);

        mAuth = FirebaseAuth.getInstance();
    }

    public void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        EmailPasswordResponseModel checkedEmail = emailHelperClass.checkEmail(email);
        EmailPasswordResponseModel checkedPassword = passwordHelperClass.checkPassword(password);

        if(!checkedEmail.getStatus()){
            editTextEmail.setError(checkedEmail.getMessage());
            editTextEmail.requestFocus();
            return;
        }

        if (!checkedPassword.getStatus()) {
            editTextPassword.setError(checkedPassword.getMessage());
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user.isEmailVerified() == true){
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    else{
                        mAuth.signOut();
                        Toast.makeText(LoginActivity.this, "Verify your email before logging in",Toast.LENGTH_SHORT).show();
                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this, "Verification email has been sent.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }else {
                    Toast.makeText(LoginActivity.this, "Failed to Login! Please check your credentials!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void toRegisterPage(View view){
        startActivity(new Intent(LoginActivity.this,  RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
    }

    public void forgotPassword(){

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.continueGuest:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.cirLoginButton:
                userLogin();
                break;
            case R.id.gotoBusinessLogin:
                startActivity(new Intent(this, BusinessLoginActivity.class));
                break;
            case R.id.forgotPasswordText:
                startActivity(new Intent(this, forgotPasswordActivity.class));
                break;
        }
    }
}


