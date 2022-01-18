package com.example.leidosrollvan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class businessForgotPassword extends AppCompatActivity {
    private EditText businessEmail;
    private Button businessResetPasswordButton;
    private ProgressBar businessProgressBar;

    FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_forgot_password);
        businessEmail = (EditText) findViewById(R.id.businessEmail);
        businessResetPasswordButton = (Button) findViewById(R.id.businessResetPassword);
        businessProgressBar = (ProgressBar) findViewById(R.id.businessForgotPasswordProgressBar);

        mauth = FirebaseAuth.getInstance();

        businessResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }
    private void resetPassword() {
        String email = businessEmail.getText().toString().trim();

        if (email.isEmpty()) {
            businessEmail.setError("email required");
            businessEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            businessEmail.setError("Please provide a valid email");
            businessEmail.requestFocus();
            return;
        }

        businessProgressBar.setVisibility(View.VISIBLE);
        mauth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(businessForgotPassword.this, "Check your email to reset your password ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(businessForgotPassword.this, "email was not sent please try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}