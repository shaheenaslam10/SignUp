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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shaheen.a5thgeneration.signupproject.Valoidators.Validations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    EditText name ,email,pass,cnfrm_pass ;
    TextView register;
    ProgressDialog progress;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        pass = (EditText)findViewById(R.id.password);
        cnfrm_pass = (EditText)findViewById(R.id.confirm_password);

        mAuth = FirebaseAuth.getInstance();

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Validations.checkConnection(SignUpActivity.this)){


                    if (name.getText().toString().length()<1){
                        Toast.makeText(SignUpActivity.this, "Name can't be Empty", Toast.LENGTH_SHORT).show();
                    }else if (email.getText().toString().length()<1){
                        Toast.makeText(SignUpActivity.this, "Email can't be Empty", Toast.LENGTH_SHORT).show();
                    }else if (!(isEmailValid(email.getText().toString()))){
                        Toast.makeText(SignUpActivity.this, "Email Format not Correct.", Toast.LENGTH_SHORT).show();
                    }else if (pass.getText().toString().length()<1){
                        Toast.makeText(SignUpActivity.this, "Password can't be Empty..", Toast.LENGTH_SHORT).show();
                    }else if (!(pass.getText().toString().equals(cnfrm_pass.getText().toString()))){
                        Toast.makeText(SignUpActivity.this, "Password Do not Matched", Toast.LENGTH_SHORT).show();
                    } else {

                        progress = ProgressDialog.show(SignUpActivity.this,"","Registering User.....", true);


                        mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (!task.isSuccessful()) {

                                            Log.d("shani","authentication failed ..... "+task.getException());
                                            if (task.getException().toString().contains("com.google.firebase.auth.FirebaseAuthUserCollisionException")){
                                                Toast.makeText(SignUpActivity.this, "Email is Used By Another User", Toast.LENGTH_SHORT).show();
                                            }
                                            progress.dismiss();

                                        } else {

                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                            DatabaseReference ref = database.getReference();
                                            DatabaseReference users = ref.child("users");
                                            DatabaseReference users_id = users.child(user.getUid());
                                            DatabaseReference users_name = users_id.child("name");
                                            DatabaseReference users_email = users_id.child("email");
                                            users_name.setValue(name.getText().toString());
                                            users_email.setValue(email.getText().toString());

                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(name.getText().toString())
                                                    .build();

                                            user.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d("shani", "User profile updated.");
                                                                FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                                                                Log.d("shani","in signUp username ......"+user1.getDisplayName());

                                                            }
                                                        }
                                                    });

                                            Log.d("shani","id of user ......"+user.getUid());

                                            progress.dismiss();
                                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                            intent.putExtra("from","emailpass");
                                            startActivity(intent);
                                            finish();

                                        }
                                    }
                                });



                    }
                }else {
                    Toast.makeText(SignUpActivity.this, "No Internet....", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
