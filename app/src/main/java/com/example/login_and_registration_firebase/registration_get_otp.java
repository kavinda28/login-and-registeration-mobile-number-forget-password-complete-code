package com.example.login_and_registration_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

public class registration_get_otp extends AppCompatActivity {
EditText ed_mobile_number;
Button get_opt;
ProgressBar progressBar;

CountryCodePicker countryCodePicker;

TextInputLayout ed_mobile_no_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_get_otp);

        ed_mobile_number = findViewById(R.id.ed_mobile_no_plaintext);
        get_opt = findViewById(R.id.bt_get_otp);
        progressBar = findViewById(R.id.progressBar3);
        countryCodePicker = findViewById(R.id.countryCodePicker);

        ed_mobile_no_layout = findViewById(R.id.ed_mobile_no);

        get_opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Get_OTP();
            }
        });
    }

    private void Get_OTP() {
        //startActivity(new Intent(getApplicationContext(),request_otp_code.class));

        String mobile_no = ed_mobile_number.getText().toString().trim();

        if (mobile_no.isEmpty()){
            ed_mobile_no_layout.setError("Mobile No is required!");
            return;
        }else {
            ed_mobile_no_layout.setError(null);
            ed_mobile_no_layout.setErrorEnabled(false);
        }

        progressBar.setVisibility(View.VISIBLE);
        String full_name = getIntent().getStringExtra("fullname");
        String username = getIntent().getStringExtra("user_name");
        String password = getIntent().getStringExtra("password");

        String from_registration = getIntent().getStringExtra("fromregistration");

        String user_enter_mobileno = ed_mobile_number.getText().toString().trim();
        String phone_number = "+"+countryCodePicker.getFullNumber() + user_enter_mobileno;

        Intent intent = new Intent(getApplicationContext(),request_otp_code.class);
        intent.putExtra("fullname",full_name);
        intent.putExtra("user_name",username);
        intent.putExtra("password",password);
        intent.putExtra("phone_number",phone_number);
        intent.putExtra("from_registration",from_registration);
        startActivity(intent);


    }
}
