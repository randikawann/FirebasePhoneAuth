package com.example.randikawann.firebasephoneauth;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class AuthActivity extends AppCompatActivity {

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private TextView tvError;
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private Button btConfirm;
    private EditText etPhoneNum;
    private EditText etValidationKey;
    private int btntype=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mAuth = FirebaseAuth.getInstance();


        etPhoneNum = (EditText) findViewById(R.id.etPhoneNum);
        etValidationKey = (EditText) findViewById(R.id.etValidationKey);
        tvError = (TextView) findViewById(R.id.tvError);
        btConfirm = (Button) findViewById(R.id.btConfirm);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(btntype==0) {
                    etValidationKey.setVisibility(View.INVISIBLE);
                    progressBar1.setVisibility(View.VISIBLE);
                    //etPhoneNum.setVisibility(View.INVISIBLE);
                    btConfirm.setVisibility(View.VISIBLE);

                    String phoneNum = etPhoneNum.getText().toString();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNum,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            AuthActivity.this,               // Activity (for callback binding)
                            mCallbacks);        // OnVerificationStateChangedCallbacks
                }else{
                    btConfirm.setEnabled(true);
                    etValidationKey.setVisibility(View.VISIBLE);

                    String verificationCode = etValidationKey.getText().toString();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                    signInWithPhoneAuthCredential(credential);
                }
                }
        });
        //onVerifi
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                tvError.setText("There was some error in Verification");
                tvError.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
//                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                btntype=1;
                //etPhoneNum.setVisibility(View.INVISIBLE);
                etValidationKey.setVisibility(View.VISIBLE);
                btConfirm.setText("Verify Code");
                btConfirm.setEnabled(true);


                // ...
            }
        };
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();

                            Intent mainIntent = new Intent(AuthActivity.this,MainActivity.class);
                            startActivity(mainIntent);
                            finish();


                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            tvError.setText("There was some error in login");
                            tvError.setVisibility(View.VISIBLE);
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

}
