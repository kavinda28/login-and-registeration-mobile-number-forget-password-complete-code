package com.example.login_and_registration_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button login;
    EditText ed_phone_no, ed_password_plaintext;
    TextView register, forgot_password;
    ProgressBar progressBar;
    CountryCodePicker countryCodePicker;
    ImageView img_facebook;
    CheckBox saveLoginCheckBox;
    SharedPreferences sharedPreferences;
    private Boolean saveLogin;
    TextInputLayout textInputLayout_phone, textInputLayout_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //full screen window open

        login = findViewById(R.id.bt_login);
        login.setOnClickListener(this);

        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        forgot_password = findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(this);


        ed_phone_no = findViewById(R.id.ed_mobile_no_plaintext);
        ed_password_plaintext = findViewById(R.id.ed_password_plaintext);

        textInputLayout_phone = findViewById(R.id.ed_mobile_no); //text input layouts(for error show)
        textInputLayout_password = findViewById(R.id.ed_password);

        progressBar = findViewById(R.id.progressBar2);
        countryCodePicker = findViewById(R.id.countryCodePicker_login);

        img_facebook = findViewById(R.id.facebook);
        img_facebook.setOnClickListener(this);

        saveLoginCheckBox = (CheckBox) findViewById(R.id.checkBox_remember);

        sharedPreferences = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);

        saveLogin = sharedPreferences.getBoolean("remember", false);
        if (saveLogin == true) {
            ed_phone_no.setText(sharedPreferences.getString("phone_no", ""));
            ed_password_plaintext.setText(sharedPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        } else {
            saveLoginCheckBox.setChecked(false);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                startActivity(new Intent(this, registration.class));
                break;
            case R.id.bt_login:
                login_user();
                break;
            case R.id.forgot_password:
                Intent intent = new Intent(getApplicationContext(), registration_get_otp.class);
                intent.putExtra("fromregistration", "0");
                startActivity(intent);
                break;
            case R.id.facebook:
                facebook_login();
                break;
        }
    }


    private void login_user() {
        check_internet check_internets=new check_internet(MainActivity.this);
        if(! check_internets.isConnected()){
           // Toast.makeText(this, "please connect wifi", Toast.LENGTH_SHORT).show();
          showCustomerDialog();
        }else {
          //  Toast.makeText(this, "network is ok", Toast.LENGTH_SHORT).show();
        }


        progressBar.setVisibility(View.VISIBLE);
        String phone_no = ed_phone_no.getText().toString().trim();
//        if (phone_no.charAt(0) == '0') {
//            phone_no = phone_no.substring(1);  //if user enter 0 then this code will remove it.
//        }
        String password = ed_password_plaintext.getText().toString().trim();


        String completePhoneNumber = "+" + countryCodePicker.getFullNumber() + phone_no;

        if (phone_no.isEmpty()) {
            textInputLayout_phone.setError("Phone Number is required!");
            progressBar.setVisibility(View.GONE);
            return;
        } else {
            textInputLayout_phone.setError(null);
            textInputLayout_phone.setErrorEnabled(false);
            progressBar.setVisibility(View.GONE);
        }


        if (password.isEmpty()) {
            textInputLayout_password.setError("Password is required!");
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (password.length() < 6) {
            textInputLayout_password.setError("Password length should be 6 Characters!");
            progressBar.setVisibility(View.GONE);
            return;
        } else {
            textInputLayout_password.setError(null);
            textInputLayout_password.setErrorEnabled(false);
            progressBar.setVisibility(View.GONE);
        }
        System.out.println("completePhoneNumber:::::::::::::::::::::::::::::::::::" + completePhoneNumber);
        //Database
        Query chechUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobile_no").equalTo(completePhoneNumber);
        chechUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    textInputLayout_phone.setError(null);
                    textInputLayout_phone.setEnabled(false);

                    String system_password_from_firebase = snapshot.child(completePhoneNumber).child("password").getValue(String.class);
                    if (system_password_from_firebase.equals(password)) {
                        ed_password_plaintext.setError(null);
                        ed_password_plaintext.setEnabled(false);

                        Toast.makeText(MainActivity.this, "loging sucssfully", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        String fullname = snapshot.child(completePhoneNumber).child("fullname").getValue(String.class);
                        Toast.makeText(MainActivity.this, "full name::" + fullname, Toast.LENGTH_SHORT).show();

                        if (saveLoginCheckBox.isChecked()) {
                            Save_username_password(phone_no, password); // remember me
                        } else {

                            sharedPreferences.edit().putBoolean("remember", false).apply();
                        }

                        startActivity(new Intent(MainActivity.this, profile.class));
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Password does not match!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Data does not exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    private void Save_username_password(String phone_no, String password) {


        sharedPreferences.edit().putBoolean("remember", true).apply();
        sharedPreferences.edit().putString("phone_no", phone_no).apply();
        sharedPreferences.edit().putString("password", password).apply();


    }


    private void facebook_login() {
        Toast.makeText(MainActivity.this, "NOt developer yet see the social media login folder", Toast.LENGTH_SHORT).show();
    }

    private void showCustomerDialog() {
        System.out.println("...............................showCustomerDialog");


        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Please Connect to the internet to proceed Further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // startActivity(new Intent(getApplicationContext(),AffectedCountries.class));
                        // finish();
                    }
                });
        builder.show();

    } //show network ont working popup
}
