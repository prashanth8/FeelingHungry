package com.atidvaya.pyn.feelinghungry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {


    SharedPreferences user_details_sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        setContentView(R.layout.activity_splash);


        user_details_sp=getSharedPreferences("user_details",MODE_PRIVATE);

        final String name=user_details_sp.getString("name","xxx");
                final String mobile_number=user_details_sp.getString("mobile_number","000");

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(name.equals("xxx"))
                {
                    Intent intent=new Intent(SplashActivity.this,RegistrationActivity.class);
                    startActivity(intent);
                    finish();

                }else
                {

                    Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("mobile_number",mobile_number);
                    startActivity(intent);
                    finish();
                }


            }
        }, 3000);
    }
}
