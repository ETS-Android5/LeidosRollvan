package com.example.leidosrollvan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class BusinessRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView existingBusinessAccount;
    private EditText editBusinessTextName, editBusinessTextEmail, editBusinessTextMobile, editBusinessTextPassword;
    private ProgressBar businessProgressBar;
    private CircularProgressButton registerBusiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_register);
        changeStatusBarColor();

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
        startActivity(new Intent(this, BusinessHomeActivity.class));
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