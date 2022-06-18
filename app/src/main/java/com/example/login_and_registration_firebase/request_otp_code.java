package com.example.login_and_registration_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class request_otp_code extends AppCompatActivity {
PinView pin_from_user_sms;
String code_by_system;
    FirebaseAuth mAuth;
    String full_name,username,password,_phone_no,from_where;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_otp_code);
        mAuth  = FirebaseAuth.getInstance();
        pin_from_user_sms = findViewById(R.id.pinView);
        progressBar = findViewById(R.id.progressBar4);

         full_name = getIntent().getStringExtra("fullname");
         username = getIntent().getStringExtra("user_name");
         password = getIntent().getStringExtra("password");
         _phone_no = getIntent().getStringExtra("phone_number");
        from_where = getIntent().getStringExtra("from_registration");
        System.out.println("phone no is :::: "+_phone_no);

    Send_verification_code_to_user(_phone_no);
    }

    private void Send_verification_code_to_user(String phone_no) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone_no)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }



PhoneAuthProvider.OnVerificationStateChangedCallbacks
    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull  PhoneAuthCredential credential) {
                     String code=credential.getSmsCode();
                    if (code!= null){
                      pin_from_user_sms.setText(code);
                      VerifyCode(code);
                    }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(request_otp_code.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String s,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s,token);
            code_by_system= s;
        }
    };




    private void VerifyCode(String code) {
              PhoneAuthCredential credential= PhoneAuthProvider.getCredential(code_by_system,code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
      FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(request_otp_code.this,"Verification  Completed",Toast.LENGTH_LONG).show();
                           if (from_where.equals("1")){
                               Store_new_users_Data();
                           }else {
                               Toast.makeText(request_otp_code.this, "hello..change your password", Toast.LENGTH_SHORT).show();
                               Intent intent = new Intent(getApplicationContext(),fogot_password.class);
                               intent.putExtra("_phone_no",_phone_no);
                               startActivity(intent);
                               finish();
                           }


                        } else {
                            progressBar.setVisibility(View.GONE);
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                Toast.makeText(request_otp_code.this,"Verification Not Completed! try again",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void Store_new_users_Data() {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Users");
        User addnewUser = new User(full_name,username,password,_phone_no);
        reference.child(_phone_no).setValue(addnewUser);
    }

    //verify button manually
    public void VerifyCode_from_button(View view) {
        progressBar.setVisibility(View.VISIBLE);
         String code = pin_from_user_sms.getText().toString();
         if (!code.isEmpty()){
             VerifyCode(code);
             progressBar.setVisibility(View.GONE);
         }

    }

    public void Resend_OTP(View view) {

        Send_verification_code_to_user(_phone_no);
    }
}
