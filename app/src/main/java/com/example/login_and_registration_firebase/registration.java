package com.example.login_and_registration_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class registration extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    EditText ed_fullname, ed_user_name, ed_password, ed_conform_password;
    Button register_user;
    ProgressBar progressBar;
    TextView have_account;

    TextInputLayout ed_fullname_layout, ed_user_name_layout, ed_password_layout, ed_conform_password_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //full screen window open

        mAuth = FirebaseAuth.getInstance();

        ed_fullname = findViewById(R.id.ed_full_name_plaintext);
        ed_user_name = findViewById(R.id.ed_mobile_no_plaintext);
        ed_password = findViewById(R.id.ed_password_plaintext);
        ed_conform_password = findViewById(R.id.ed_conform_password_plaintext);

        register_user = findViewById(R.id.bt_Register);
        register_user.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        have_account = findViewById(R.id.already_account);
        have_account.setOnClickListener(this);

        //TextInputLayout for error show
        ed_fullname_layout = findViewById(R.id.ed_full_name);
        ed_user_name_layout = findViewById(R.id.ed_mobile_no);
        ed_password_layout = findViewById(R.id.ed_password);
        ed_conform_password_layout = findViewById(R.id.ed_conform_password);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_Register:
                registerUser();
                break;
            case R.id.already_account:
                startActivity(new Intent(registration.this, MainActivity.class));
                break;

        }

    }

    private void registerUser() {
        String fullname = ed_fullname.getText().toString().trim();
        String user_name = ed_user_name.getText().toString().trim();
        String password = ed_password.getText().toString().trim();
        String conform_password = ed_conform_password.getText().toString().trim();
//full name
        if (fullname.isEmpty()) {
            ed_fullname_layout.setError("Full name is required!");
            return;
        } else {
            ed_fullname_layout.setError(null);
            ed_fullname_layout.setErrorEnabled(false);
        }

//username
        if (user_name.isEmpty()) {
            ed_user_name_layout.setError("user name is required!");
            return;
        }
        if (user_name.length() >= 15) {
            ed_user_name_layout.setError("Username too Long!");
            return;
        }

        String noWhitespace = "\\A\\w{4,20}\\z";

        if (!user_name.matches(noWhitespace)) {
            ed_user_name_layout.setError("White Space are not allowed!");
            return;
        } else {
            ed_user_name_layout.setError(null);
            ed_user_name_layout.setErrorEnabled(false);
        }


//password
        if (password.isEmpty()) {
            ed_password_layout.setError("Password is required!");
            return;
        }
        String upperCaseChars = "(.*[A-Z].*)";
        if (!password.matches(upperCaseChars)) {
            ed_password_layout.setError("Password must have atleast one uppercase character!");
            return;
        }
        if (password.length() < 6) {
            ed_password_layout.setError("Password length should be 6 Characters!");
            return;
        }
        if (conform_password.isEmpty()) {
            ed_conform_password_layout.setError("Conform Password is required!");
            return;
        }
        if (!password.equals(conform_password)) {
            ed_conform_password_layout.setError("Password Not matching!");
            return;
        } else {
            ed_conform_password_layout.setError(null);
            ed_conform_password_layout.setErrorEnabled(false);
        }

        Intent intent = new Intent(getApplicationContext(), registration_get_otp.class);
        intent.putExtra("fullname", fullname);
        intent.putExtra("user_name", user_name);
        intent.putExtra("password", password);
        intent.putExtra("fromregistration", "1");
        startActivity(intent);


//        progressBar.setVisibility(View.VISIBLE);
//        mAuth.createUserWithEmailAndPassword(mobile_no,password)
//             .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                 @Override
//                 public void onComplete(@NonNull Task<AuthResult> task) {
//                     if (task.isSuccessful()){
//                       User user=new User(fullname,mobile_no,password);
//
//                     FirebaseDatabase.getInstance().getReference("Users")
//                             .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                             .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                         @Override
//                         public void onComplete(@NonNull Task<Void> task) {
//                             if (task.isSuccessful()){
//                                 Toast.makeText(registration.this,"user has been Registered successfully!",Toast.LENGTH_LONG).show();
//                                 progressBar.setVisibility(View.GONE);
//                             }else {
//                                 Toast.makeText(registration.this,"Failed to register! Try again!",Toast.LENGTH_LONG).show();
//                                 progressBar.setVisibility(View.GONE);
//                             }
//                         }
//                     });
//
//                     }else {
//                         Toast.makeText(registration.this,"Failed to register! Try again!",Toast.LENGTH_LONG).show();
//                         progressBar.setVisibility(View.GONE);
//                     }
//                 }
//             });

    }


}
