package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.chattingapp.Models.Users;
import com.example.chattingapp.databinding.ActivityPhoneSignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class PhoneSignInActivity extends AppCompatActivity {


    ActivityPhoneSignInBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String mVerificationId;

    private static final String TAG = "MAIN_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneSignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.edtOTPCode.setVisibility(View.GONE);
        binding.txtResend.setVisibility(View.GONE);
        binding.btnSubmit.setVisibility(View.GONE);

        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(PhoneSignInActivity.this);
        progressDialog.setTitle("Please, wait!");
        progressDialog.setMessage("We're sending the OTP to you!");


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(PhoneSignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d(TAG, "onCodeSent:" + verificationId);

                mVerificationId = verificationId;
                forceResendingToken = token;

                progressDialog.dismiss();

                binding.edtPhone.setVisibility(View.GONE);

                Toast.makeText(PhoneSignInActivity.this, "Verification code sent...", Toast.LENGTH_SHORT).show();
            }
        };


        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (binding.edtPhone.getText().toString().isEmpty()) {
                    binding.edtPhone.setError("Please, enter your phone number to continue");
                    return;
                }

                binding.edtOTPCode.setVisibility(View.VISIBLE);
                binding.txtResend.setVisibility(View.VISIBLE);
                binding.btnContinue.setVisibility(View.GONE);
                binding.btnSubmit.setVisibility(View.VISIBLE);

                String phone = binding.edtPhone.getText().toString();

                startPhoneNumberVerification(phone);


            }
        });

        binding.txtResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edtPhone.getText().toString().isEmpty()) {
                    binding.edtPhone.setError("Please, enter your phone number to continue");
                    return;
                }
                String phone = binding.edtPhone.getText().toString().trim();
                resendVerificationCode(phone, forceResendingToken);

            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edtOTPCode.getText().toString().isEmpty()) {
                    binding.edtOTPCode.setError("Please, enter your OTP to continue");
                    return;
                }
                String code = binding.edtOTPCode.getText().toString().trim();
                verificationPhoneNumberWithCode(mVerificationId, code);

            }
        });


        binding.txtSignUpEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhoneSignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        binding.txtAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhoneSignInActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startPhoneNumberVerification(String phone) {

        progressDialog.setMessage("Verifying Phone Number");
        progressDialog.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void resendVerificationCode(String phone, PhoneAuthProvider.ForceResendingToken token) {
        progressDialog.setMessage("Resending OTP Code");
        progressDialog.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }


    private void verificationPhoneNumberWithCode(String verificationId, String code) {

        progressDialog.setMessage("Verifying OTP Code");
        progressDialog.show();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        progressDialog.setMessage("Logging in");
        progressDialog.show();

        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();

                            Intent intent = new Intent(PhoneSignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(PhoneSignInActivity.this, "Sign in with Phone Number", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                            }
                        }
                    }
                });
    }


}