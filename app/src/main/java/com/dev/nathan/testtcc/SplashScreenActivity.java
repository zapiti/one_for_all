package com.dev.nathan.testtcc;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dev.nathan.testtcc.component.MapsActivity;
import com.dev.nathan.testtcc.test.InitialActivity;
import com.dev.nathan.testtcc.test.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToMain();
            }
        }, 2000);

    }

    private void goToMain() {



        if (currentUser != null) {

            Intent intent = new Intent(SplashScreenActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();


        }else {
            Intent intent = new Intent(SplashScreenActivity.this,
                    InitialActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
