package com.shaheen.a5thgeneration.signupproject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;

public class FeedbackActivity extends AppCompatActivity {

    EditText subject,message;
    Button send_message;

    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseUser fire_user;
    DatabaseReference users;
    DatabaseReference settings;
    String st_subject,st_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Feedback");
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }

                }

        );

        subject = (EditText)findViewById(R.id.subject);
        message = (EditText)findViewById(R.id.email_message);
        send_message = (Button)findViewById(R.id.email_send_message);
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subject.getText().toString().length()<1){
                    Toast.makeText(FeedbackActivity.this, "Please Enter Subject.", Toast.LENGTH_SHORT).show();
                }else if (message.getText().toString().length()<1){
                    Toast.makeText(FeedbackActivity.this, "Please Enter Message.", Toast.LENGTH_SHORT).show();
                }else {


                    st_subject = subject.getText().toString();
                    st_message = message.getText().toString();

                    AsynchTaskUploadFeedback asynchTaskUploadFeedback = new AsynchTaskUploadFeedback();
                    asynchTaskUploadFeedback.execute();
                    progressDialog.dismiss();

                }
            }
        });
    }

    public class AsynchTaskUploadFeedback extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(final String... strings) {

            fire_user = FirebaseAuth.getInstance().getCurrentUser();
            assert fire_user != null;
            String mail= fire_user.getEmail();
            Log.d("shani","email ....."+mail);

            mAuth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            DatabaseReference feedback = databaseReference.child("feedback");
            users = feedback.child(fire_user.getUid());

            String userIdd = users.push().getKey();

            users.child(userIdd).child("subject").setValue(st_subject);
            users.child(userIdd).child("message").setValue(st_message);

            return null;
        }
        @Override
        protected void onPreExecute() {super.onPreExecute();
            progressDialog = ProgressDialog.show(FeedbackActivity.this,"","Uploading Profile UsersData.....", true);}
        @Override
        protected void onPostExecute(String s) {super.onPostExecute(s);
            progressDialog.dismiss();}
        @Override
        protected void onProgressUpdate(String... values) {super.onProgressUpdate(values);}
    }
}
