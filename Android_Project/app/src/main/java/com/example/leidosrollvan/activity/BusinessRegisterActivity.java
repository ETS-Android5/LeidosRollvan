package com.example.leidosrollvan.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.leidosrollvan.R;
import com.example.leidosrollvan.activitymethods.BusinessNameMobileCheckMethods;
import com.example.leidosrollvan.activitymethods.EmailCheckMethods;
import com.example.leidosrollvan.activitymethods.PasswordCheckMethods;
import com.example.leidosrollvan.dataClasses.Business;
import com.example.leidosrollvan.dataClasses.BusinessImage;
import com.example.leidosrollvan.dataClassesForMethods.EmailPasswordResponseModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class BusinessRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST  = 1;

    private TextView existingBusinessAccount;
    private EditText editBusinessTextName, editBusinessTextEmail, editBusinessTextMobile, editBusinessTextPassword;
    private ProgressBar businessProgressBar, imageProgressBar;
    private CircularProgressButton registerBusiness;
    private Button addImageButton;
    private ImageView bannerImage;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseReference, verifyRef;
    private String verifyID;

    private BusinessNameMobileCheckMethods nameMobileCheckerClass = new BusinessNameMobileCheckMethods();
    private EmailCheckMethods emailCheckerClass = new EmailCheckMethods();
    private PasswordCheckMethods passwordCheckerClass = new PasswordCheckMethods();


    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
        imageProgressBar = (ProgressBar) findViewById(R.id.imageProgressBar);

        addImageButton = (Button) findViewById(R.id.addImageButton);
        addImageButton.setOnClickListener(this);

        bannerImage = (ImageView) findViewById(R.id.businessImagePreview);

        mStorageRef = FirebaseStorage.getInstance().getReference("Business banners");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            //window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
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

        boolean checkedName = nameMobileCheckerClass.checkBusinessName(businessName);
        boolean checkedMobile = nameMobileCheckerClass.checkBusinessMobile(businessMobile);
        boolean checkedUri = nameMobileCheckerClass.checkUri(mImageUri);
        EmailPasswordResponseModel checkedEmail = emailCheckerClass.checkEmail(businessEmail);
        EmailPasswordResponseModel checkedPassword = passwordCheckerClass.checkPassword(businessPassword);

        if(checkedName){
            editBusinessTextName.setError("Name is Required!");
            editBusinessTextName.requestFocus();
            return;
        }

        if(checkedMobile){
            editBusinessTextMobile.setError("Mobile Number is Required!");
            editBusinessTextMobile.requestFocus();
            return;
        }

        if(!checkedEmail.getStatus()){
            editBusinessTextEmail.setError(checkedEmail.getMessage());
            editBusinessTextEmail.requestFocus();
            return;
        }

        if(!checkedPassword.getStatus()){
            editBusinessTextPassword.setError(checkedPassword.getMessage());
            editBusinessTextPassword.requestFocus();
            return;
        }

        if(checkedUri){
            addImageButton.setError("A banner image is required!");
            bannerImage.requestFocus();
            Toast.makeText(BusinessRegisterActivity.this, "Add a banner image!", Toast.LENGTH_SHORT).show();
            return;
        }

        businessProgressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(businessEmail, businessPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Business business = new Business(businessName, businessMobile, businessEmail);
                            verifyRef = FirebaseDatabase.getInstance().getReference("Users still to verify");
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Businesses")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(business).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        //Toast.makeText(BusinessRegisterActivity.this, "Business has been registered successfully", Toast.LENGTH_LONG).show();
                                        businessProgressBar.setVisibility(View.GONE);
                                        //send verification email
                                        FirebaseUser business = mAuth.getCurrentUser();
                                        verifyID = business.getUid();
                                        verifyRef.child(verifyID).setValue(true);

                                        business.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(BusinessRegisterActivity.this, "Verification email has been sent.", Toast.LENGTH_SHORT).show();
                                            }

                                        });

                                        uploadImage();
                                        startActivity(new Intent(BusinessRegisterActivity.this, BusinessLoginActivity.class));
                                    }
                                    else {
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

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
        && data != null && data.getData() != null){
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(bannerImage);

        }
    }

    private String getImageExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadImage() {
        if(mImageUri != null){
            StorageReference fileReference = mStorageRef
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()
                            + "_banner" + "." + getImageExtension(mImageUri));

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageProgressBar.setVisibility(View.GONE);
                            imageProgressBar.setProgress(0);
                            Toast.makeText(BusinessRegisterActivity.this, "Image added successfully", Toast.LENGTH_LONG).show();

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    BusinessImage businessImage = new BusinessImage(FirebaseAuth
                                            .getInstance().getCurrentUser().getUid() + "_banner" + "." + getImageExtension(mImageUri),
                                            uri.toString());

                                    FirebaseDatabase.getInstance().getReference("Business Images")
                                            .child(mAuth.getCurrentUser().getUid())
                                            .setValue(businessImage);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BusinessRegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            imageProgressBar.setVisibility(View.VISIBLE);
                            imageProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_LONG).show();
        }
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
            case R.id.addImageButton:
                selectImage();
                break;
        }

    }

}