package com.shaheen.a5thgeneration.signupproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.util.Calendar;

import static android.R.attr.startYear;

public class TripActivity extends AppCompatActivity {

    TextView when_date,to_date;
    Button cancel_trip,create_trip;
    EditText travelling_to,trip_description,tag;
    CheckBox dates_flexible;
    String date_flexible_text;

    ProgressDialog progressDialog;
    ToggleButton i_pay;
    String paying;
    String to_day,to_month,to_year,from_day,from_month,from_year;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseUser fire_user;
    String st_travelling_to,st_trip_description,st_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        travelling_to = (EditText)findViewById(R.id.travelling_to);
        trip_description = (EditText)findViewById(R.id.trip_description);
        tag = (EditText)findViewById(R.id.tag);
        dates_flexible = (CheckBox)findViewById(R.id.dates_flexible);
        when_date = (TextView)findViewById(R.id.when_date);
        to_date = (TextView)findViewById(R.id.to_date);
        cancel_trip = (Button) findViewById(R.id.cancel_trip);
        create_trip = (Button) findViewById(R.id.create_trip);

        cancel_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        create_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Create_Trip();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        i_pay = (ToggleButton) findViewById(R.id.toggleiPay);
        paying = i_pay.getText().toString();
        ((RadioGroup) findViewById(R.id.paying_group)).setOnCheckedChangeListener(ToggleListenerPaying);


        when_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putInt("DATE",1);

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setArguments(bundle);

                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putInt("DATE",2);

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setArguments(bundle);

                newFragment.show(getFragmentManager(), "datePicker");
            }
        });



    }

    public void Create_Trip(){

        if (travelling_to.getText().toString().length()<6){
            Toast.makeText(this, "Please Enter Location", Toast.LENGTH_SHORT).show();
        }else if (from_day==null || from_month==null || from_year==null){
            Toast.makeText(this, "Please Select Starting Date..", Toast.LENGTH_SHORT).show();
        }else if (to_day==null || to_month==null || to_year==null){
            Toast.makeText(this, "Please Select End Date..", Toast.LENGTH_SHORT).show();
        }else if (trip_description.getText().toString().length()<6){
            Toast.makeText(this, "Please Describe Your Trip", Toast.LENGTH_SHORT).show();
        }else if (tag.getText().toString().length()<6){
            Toast.makeText(this, "Please Enter Tag", Toast.LENGTH_SHORT).show();
        }else {

           st_travelling_to = travelling_to.getText().toString();
            if (dates_flexible.isChecked()){
                date_flexible_text = "yes";
            }else {
                date_flexible_text = "no";
            }
            st_trip_description = trip_description.getText().toString();
            st_tag = tag.getText().toString();


            AsynchTaskUploadTrip uploadTrip = new AsynchTaskUploadTrip();
            uploadTrip.execute();

        }
    }


   final RadioGroup.OnCheckedChangeListener ToggleListenerPaying = new RadioGroup.OnCheckedChangeListener() {
       @Override
       public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
           for (int j = 0; j < radioGroup.getChildCount(); j++) {
               final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
               if (view.getId() == i){

                   view.setChecked(true);
                   view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.purple));
                   view.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                   paying = view.getText().toString();

               }else {
                   view.setChecked(false);
                   view.setBackgroundResource(R.drawable.border);
                   view.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
               }

           }
       }
   };
    public void onTogglePaying(View view) {
        ((RadioGroup)view.getParent()).check(view.getId());
        // app specific stuff ..
    }
    public class AsynchTaskUploadTrip extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {


            fire_user = FirebaseAuth.getInstance().getCurrentUser();
            assert fire_user != null;
            String mail= fire_user.getEmail();
            Log.d("shani","email ....."+mail);


            DatabaseReference all_users = databaseReference.child("all_users");
            DatabaseReference current_uid = all_users.child(fire_user.getUid());
            DatabaseReference my_trip = current_uid.child("my_trip");
            String userIdd = my_trip.push().getKey();

            my_trip.child(userIdd).child("travelling_to").setValue(st_travelling_to);

            my_trip.child(userIdd).child("from_day").setValue(from_day);
            my_trip.child(userIdd).child("from_month").setValue(from_month);
            my_trip.child(userIdd).child("from_year").setValue(from_year);
            my_trip.child(userIdd).child("to_day").setValue(to_day);
            my_trip.child(userIdd).child("to_month").setValue(to_month);
            my_trip.child(userIdd).child("to_year").setValue(to_year);
            // have to be implement number of days of trip

            my_trip.child(userIdd).child("dates_flexible").setValue(date_flexible_text);

            my_trip.child(userIdd).child("who_paying").setValue(paying);

            my_trip.child(userIdd).child("trip_description").setValue(st_trip_description);
            my_trip.child(userIdd).child("tag").setValue(st_tag);






            return null;
        }
        @Override
        protected void onPreExecute() {super.onPreExecute();
            progressDialog = ProgressDialog.show(TripActivity.this,"","Uploading Profile UsersData.....", true);}
        @Override
        protected void onPostExecute(String s) {super.onPostExecute(s);
            progressDialog.dismiss();}
        @Override
        protected void onProgressUpdate(String... values) {super.onProgressUpdate(values);}
    }
    class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        static final int START_DATE = 1;
        static final int END_DATE = 2;

        private int mChosenDate;

        int cur = 0;


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            Bundle bundle = this.getArguments();
            if(bundle != null){
                mChosenDate = bundle.getInt("DATE",1);
            }


            switch (mChosenDate) {

                case START_DATE:
                    cur = START_DATE;
                    return new DatePickerDialog(getActivity(), this, year, month, day);

                case END_DATE:
                    cur = END_DATE;
                    return new DatePickerDialog(getActivity(), this, year, month, day);


            }
            return null;
        }


        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            if(cur == START_DATE){
                // set selected date into textview
                Log.d("Date Début","Date1 : " + new StringBuilder().append(month + 1)
                        .append("/").append(day).append("/").append(year)
                        .append(" "));
                TextView when = (TextView)getActivity().findViewById(R.id.when_date);
                when.setText(new StringBuilder()
                        .append(day).append("/").append(month + 1).append("/").append(year).append(" "));

                from_day = String.valueOf(day);
                from_month = String.valueOf(month);
                from_year = String.valueOf(year);

            }
            else{
                Log.d("Date fin","Date3 : " + new StringBuilder().append(month + 1)
                        .append("/").append(day).append("/").append(year)
                        .append(" "));
                TextView to = (TextView)getActivity().findViewById(R.id.to_date);
                to.setText(new StringBuilder()
                        .append(day).append("/").append(month + 1).append("/").append(year).append(" "));

                to_day = String.valueOf(day);
                to_month = String.valueOf(month);
                to_year = String.valueOf(year);

            }
        }


    }
}
