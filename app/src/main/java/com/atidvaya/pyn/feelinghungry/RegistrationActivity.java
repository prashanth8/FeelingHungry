package com.atidvaya.pyn.feelinghungry;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity {


    Firebase rootDataReference;
    EditText nameEt;
    EditText phoneNumberEt;
    Button registerBtn;

    CoordinatorLayout coordinatorLayout;



    SharedPreferences user_details_sp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Firebase.setAndroidContext(RegistrationActivity.this);
        rootDataReference=new Firebase("https://feelinghungry.firebaseio.com/registrations");

        user_details_sp=getSharedPreferences("user_details",MODE_PRIVATE);


        nameEt= (EditText) findViewById(R.id.nameEt);
        phoneNumberEt= (EditText) findViewById(R.id.phoneNumberEt);
        registerBtn= (Button) findViewById(R.id.registrationBtnId);
        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.main_content);





        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=nameEt.getText().toString();
                final String ph_no=phoneNumberEt.getText().toString();


                final ProgressDialog pDialog=new ProgressDialog(RegistrationActivity.this);
                pDialog.setMessage("Please wait");
                pDialog.setIndeterminate(true);
                pDialog.setCancelable(false);

                if(name!=null && !name.equals(""))
                {

                   String str=name;
                    str=str.substring(0,1).toUpperCase()+name.substring(1);
                    name=str;
                    System.out.println(name);


                    if(ph_no.equals(""))
                    {

                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Enter valid 10 digit mobile number", Snackbar.LENGTH_LONG);
                        snackbar.show();


                        phoneNumberEt.requestFocus();
                    }else if(ph_no.length()==10)
                    {

                        pDialog.show();
                        User user=new User(name,"+91"+ph_no);
                        rootDataReference.push().setValue(user);

                        final String finalName = name;
                        rootDataReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                pDialog.cancel();
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Hi " + finalName + ".\nThank you for registering with Feeling Hungry.", Snackbar.LENGTH_LONG);
                                snackbar.show();

                                SharedPreferences.Editor editor=user_details_sp.edit();
                                editor.putString("name",finalName);
                                editor.putString("mobile_number","+91"+ph_no);
                                editor.commit();




                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }, 3000);



                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                pDialog.cancel();
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "We are not able to register you at this moment. Please try again..", Snackbar.LENGTH_LONG);
                                snackbar.show();

                            }
                        });









                    }else
                    {


                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Enter valid 10 digit mobile number", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        phoneNumberEt.requestFocus();

                    }






                }
                else
                {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please enter your First and Last name", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    nameEt.requestFocus();

                }





            }
        });










    }
}
