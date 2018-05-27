package com.dev.nathan.testtcc;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dev.nathan.testtcc.controler.InitialActivity;
import com.dev.nathan.testtcc.controler.MainActivity;
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
        //metodo de delay de 2 segundos para escolha ao abrir
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToMain();
            }
        }, 2000);

    }

    private void goToMain() {
        if (currentUser != null) {
            //se logado mandar para tela principal
            Intent intent = new Intent(SplashScreenActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            //se nao estiver logado mandar para a tela inicial
            Intent intent = new Intent(SplashScreenActivity.this,
                    InitialActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
