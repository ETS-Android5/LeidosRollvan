package com.example.leidosrollvan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class BusinessLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register;
    private Button goToUserLogin;
    private EditText editBusinessTextEmail, editBusinessTextPassword;
    private CircularProgressButton businessLogin;

    private ProgressBar progressBar;

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

        progressBar = (ProgressBar) findViewById(R.id.businessLoginProgressBar);
    }

    public void toBusinessRegisterPage(View view){
        startActivity(new Intent(BusinessLoginActivity.this,  BusinessRegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
    }

    public void businessLogin(){
        startActivity(new Intent(this, BusinessHomeActivity.class));
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
        }

    }
}