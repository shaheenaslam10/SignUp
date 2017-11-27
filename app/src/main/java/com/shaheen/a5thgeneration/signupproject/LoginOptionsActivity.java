package com.shaheen.a5thgeneration.signupproject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.CountryPickerListener;
import com.shaheen.a5thgeneration.signupproject.Valoidators.Validations;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginOptionsActivity extends AppCompatActivity {

    TextView email,facebook,otp,codee;
    FirebaseAuth mAuth;
    CallbackManager callbackManager;
    RelativeLayout country_picker;

    String contry_code="+1";
    Dialog dialog;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_options);

        email = (TextView)findViewById(R.id.with_mail);
        facebook = (TextView)findViewById(R.id.facebook_register);
        otp = (TextView)findViewById(R.id.phone_register);




        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser()!=null){
            startActivity(new Intent(LoginOptionsActivity.this,MainActivity.class));
            finish();
        }
        callbackManager = CallbackManager.Factory.create();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Verifying...");

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginOptionsActivity.this,LoginActivity.class));
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validations.checkConnection(LoginOptionsActivity.this)){

                    LoginManager.getInstance().logInWithReadPermissions(LoginOptionsActivity.this, Arrays.asList("user_photos", "email", "public_profile", "user_posts"));
                    LoginManager.getInstance().logInWithPublishPermissions(LoginOptionsActivity.this, Arrays.asList("publish_actions"));
                    LoginManager.getInstance().registerCallback(callbackManager,
                            new FacebookCallback<LoginResult>()
                            {
                                @Override
                                public void onSuccess(LoginResult loginResult)
                                {
                                    // App code
                                    // handleFacebookAccessToken(loginResult.getAccessToken());
                                    AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                                    mAuth.signInWithCredential(credential)
                                            .addOnCompleteListener(LoginOptionsActivity.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        // Sign in success, update UI with the signed-in user's information
                                                        Log.d("shani", "signInWithCredential:success");
                                                        FirebaseUser user = mAuth.getCurrentUser();
                                                        startActivity(new Intent(LoginOptionsActivity.this, MainActivity.class));
                                                        finish();
                                                        // updateUI(user);
                                                    } else {
                                                        // If sign in fails, display a message to the user.
                                                        Log.w("shani", "signInWithCredential:failure", task.getException());
                                                        Toast.makeText(LoginOptionsActivity.this, "Authentication failed.",
                                                                Toast.LENGTH_SHORT).show();
                                                        // updateUI(null);
                                                    }

                                                    // ...
                                                }
                                            });
                                }

                                @Override
                                public void onCancel()
                                {
                                    // App code
                                }

                                @Override
                                public void onError(FacebookException exception)
                                {
                                    // App code
                                }
                            });
                }
                else {
                    Toast.makeText(LoginOptionsActivity.this, "No Internet..", Toast.LENGTH_SHORT).show();
                }
            }
        });
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                Toast.makeText(LoginOptionsActivity.this, "Timeout , please try again", Toast.LENGTH_SHORT).show();
            }
        });
        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d("shani", "onVerificationCompleted:" + credential);
                credential.getSmsCode();

                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(LoginOptionsActivity.this, new OnCompleteListener()
                        {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Log.d("shani", "signInWithCredential:success");
                                    // FirebaseUser user = task.getResult().getUser();
                                    progressDialog.dismiss();
                                    dialog.dismiss();
                                    Intent intent = new Intent(LoginOptionsActivity.this, MainActivity.class);
                                    intent.putExtra("phone","phone");
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.w("shani", "signInWithCredential:failure", task.getException());

                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        Toast.makeText(LoginOptionsActivity.this, "Invalid Code..", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w("shani", "onVerificationFailed", e);

                progressDialog.dismiss();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request

                    Toast.makeText(LoginOptionsActivity.this, "Invalid Request ... Try Again", Toast.LENGTH_SHORT).show();
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded

                    Toast.makeText(LoginOptionsActivity.this, "Sms Quote has Exceeded..", Toast.LENGTH_SHORT).show();
                    // ...
                }
                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Log.d("shani", "onCodeSent:" + verificationId);

                mVerificationId = verificationId;
                mResendToken = token;



                Log.d("shani","verification id ....."+mVerificationId);
                Log.d("shani","mResend token ....."+mResendToken);

            }
        };

    }
    public void showDialog(){
        dialog = new Dialog(LoginOptionsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.phone_dialog);

        final EditText text = (EditText) dialog.findViewById(R.id.phone_number);

       // Toast.makeText(this, "contry code ......"+contry_code, Toast.LENGTH_SHORT).show();
        TextView dialogButton = (TextView) dialog.findViewById(R.id.send_phonenumber);
        country_picker = (RelativeLayout) dialog.findViewById(R.id.country_picker);
        codee = (TextView) dialog.findViewById(R.id.code);

        final CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, final int flagDrawableResID) {

                        picker.dismiss();
                        codee.setText(""+dialCode);
                contry_code = dialCode;

            }
        });

        country_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validations.checkConnection(LoginOptionsActivity.this)){

                    if (!(text.getText().toString().length() <1 )){

                        progressDialog.show();
                        long delayInMillis = 10000;
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                //  Toast.makeText(LoginActivity.this, "If Code Not Recieved \n stry Again...", Toast.LENGTH_SHORT).show();
                            }
                        }, delayInMillis);
                        String number = contry_code+text.getText().toString();
                        Log.d("shani","mobile number with code = ......"+number);
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                number,        // Phone number to verify
                                60,                 // Timeout duration
                                TimeUnit.SECONDS,   // Unit of timeout
                                LoginOptionsActivity.this,               // Activity (for callback binding)
                                mCallbacks);
                    }else {
                        Toast.makeText(LoginOptionsActivity.this, "Please Enter Number", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(LoginOptionsActivity.this, "No Internet..", Toast.LENGTH_SHORT).show();
                }


            }


        });
        final TextView cancel = (TextView)dialog.findViewById(R.id.dial_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });


        dialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            // Toast.makeText(this, "on result show ... activity result ", Toast.LENGTH_SHORT).show();
            Log.d("shani","result ok  ....");

        }

    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("shani", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("shani", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginOptionsActivity.this, MainActivity.class));
                            finish();
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("shani", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginOptionsActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
