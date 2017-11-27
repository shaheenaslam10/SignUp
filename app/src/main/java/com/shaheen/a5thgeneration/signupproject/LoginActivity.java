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
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shaheen.a5thgeneration.signupproject.Valoidators.Validations;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    EditText name ,email,pass ;
    TextView register ,create_account;
    FirebaseAuth mAuth;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        pass = (EditText)findViewById(R.id.password);
        create_account = (TextView) findViewById(R.id.create_account);
        register = (TextView) findViewById(R.id.register);



        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Validations.checkConnection(LoginActivity.this)){


                    if (email.getText().toString().length()<1){
                        Toast.makeText(LoginActivity.this, "Email can't be Empty", Toast.LENGTH_SHORT).show();
                    }else if (!(isEmailValid(email.getText().toString()))){
                        Toast.makeText(LoginActivity.this, "Email Format not Correct.", Toast.LENGTH_SHORT).show();
                    }else if (pass.getText().toString().length()<1){
                        Toast.makeText(LoginActivity.this, "Password can't be Empty..", Toast.LENGTH_SHORT).show();
                    }else {

                        progress = ProgressDialog.show(LoginActivity.this,"","Please Wait.....", true);


                        mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            progress.dismiss();
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            if (task.getException().toString().contains("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException")){
                                                Toast.makeText(LoginActivity.this, "Incorrect Email or Password..", Toast.LENGTH_LONG).show();
                                            }
                                            if (task.getException().toString().contains("com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted")){
                                                Toast.makeText(LoginActivity.this, "User is not registerd....", Toast.LENGTH_LONG).show();
                                            }
                                            Log.d("shani","exception of login ....."+task.getException());

                                            progress.dismiss();
                                        }
                                    }
                                });



                    }
                }else {
                    Toast.makeText(LoginActivity.this, "No Internet....", Toast.LENGTH_SHORT).show();
                }
            }
        });







    }


    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
       // updateUI(currentUser);
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
