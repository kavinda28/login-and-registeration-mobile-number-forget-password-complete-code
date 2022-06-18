package com.example.login_and_registration_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class fogot_password extends AppCompatActivity {
    EditText ed_password, ed_conform_password;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fogot_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //full screen window open

        ed_password = findViewById(R.id.ed_password_plaintext);
        ed_conform_password = findViewById(R.id.ed_confirm_password_plaintext);
        progressBar = findViewById(R.id.progressBar5);

    }
    public void update(View view) {
        Update_password();
    }
    private void Update_password() {
        String password = ed_password.getText().toString().trim();
        String conform_password = ed_conform_password.getText().toString().trim();


        if (password.isEmpty()) {
            ed_password.setError("Password is required!");
            ed_password.requestFocus();
            return;
        }
        String upperCaseChars = "(.*[A-Z].*)";
        if (!password.matches(upperCaseChars)) {
            ed_password.setError("Password must have atleast one uppercase character!");
            ed_password.requestFocus();
            return;
        }
        if (password.length() < 6) {
            ed_password.setError("Password length should be 6 Characters!");
            ed_password.requestFocus();
            return;
        }
        if (conform_password.isEmpty()) {
            ed_conform_password.setError("Conform Password is required!");
            ed_conform_password.requestFocus();
            return;
        }
        if (!password.equals(conform_password)) {
            ed_conform_password.setError("Password Not matching!");
            ed_conform_password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        String new_password = ed_conform_password.getText().toString().trim();
        String User_phonenumber = getIntent().getStringExtra("_phone_no");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(User_phonenumber).child("password").setValue(new_password);

        startActivity(new Intent(getApplicationContext(), complet_fogot_password.class));
        finish();


    }


}
