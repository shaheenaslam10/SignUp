package com.shaheen.a5thgeneration.signupproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SmsVerificationActivity extends AppCompatActivity {

    TextView send_phonenumber, verify_code;
    EditText phone , code ;
    String st_phone , st_code;
    ProgressDialog progress;
    FirebaseAuth auth;

    boolean mVerificationInProgress = false;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_verification);

        progress = ProgressDialog.show(SmsVerificationActivity.this,"","Verifying.....", true);
        progress.dismiss();

        phone = (EditText)findViewById(R.id.phone_number);
        code = (EditText)findViewById(R.id.code);
        send_phonenumber = (TextView) findViewById(R.id.send_phonenumber);
        verify_code = (TextView) findViewById(R.id.verify_code);


        st_code = code.getText().toString();
        st_phone = phone.getText().toString();

        Log.d("shani","phone nuber ......"+st_phone);

        auth = FirebaseAuth.getInstance();



        send_phonenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                progress.show();
                Log.d("shani","phone nuber ......"+phone.getText().toString());

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phone.getText().toString(),        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        SmsVerificationActivity.this,               // Activity (for callback binding)
                        mCallbacks);
                progress.dismiss();
            }
        });
        verify_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.show();



            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d("shani", "onVerificationCompleted:" + credential);
                credential.getSmsCode();

            //    signInWithPhoneAuthCredential(credential);

              //  PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code.getText().toString());
                auth.signInWithCredential(credential)
                        .addOnCompleteListener(SmsVerificationActivity.this, new OnCompleteListener()
                        {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Log.d("shani", "signInWithCredential:success");
                                    // FirebaseUser user = task.getResult().getUser();
                                    progress.dismiss();
                                    startActivity(new Intent(SmsVerificationActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Log.w("shani", "signInWithCredential:failure", task.getException());
                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                      /*  mVerificationField.setError("Invalid code.");*/
                                    }
                                }
                            }
                        });


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("shani", "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("shani", "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                Log.d("shani","verification id ....."+mVerificationId);
                Log.d("shani","mResend token ....."+mResendToken);

                // ...
            }
        };



    }
}
