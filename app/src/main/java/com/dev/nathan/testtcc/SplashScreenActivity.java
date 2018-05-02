package com.dev.nathan.testtcc;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dev.nathan.testtcc.controler.MainActivity;
import com.dev.nathan.testtcc.maps.MapsActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToMain();
            }
        }, 2000);

    }

    private void goToMain() {
        Intent intent = new Intent(SplashScreenActivity.this,
               MapsActivity.class);
        startActivity(intent);
        finish();
    }

}
