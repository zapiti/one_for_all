package com.dev.nathan.testtcc.controler;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.dev.nathan.testtcc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private EditText loginEmailText;
    private EditText loginPassText;
    private LinearLayout load;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        TextView maininit = findViewById(R.id.btngomenu);

        maininit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ntent = new Intent(LoginActivity.this, InitialActivity.class);
                startActivity(ntent);
            }
        });


        loginEmailText = findViewById(R.id.login_email);
        loginPassText = findViewById(R.id.login_confirm_pass);
        Button loginBtn = findViewById(R.id.login_btn);
        Button loginRegBtn = findViewById(R.id.login_reg_btn);
        load = findViewById(R.id.avi2contentLogin);

        loginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regIntent);

            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginEmail = loginEmailText.getText().toString();
                String loginPass = loginPassText.getText().toString();

                if(!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPass)){
                    load.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(loginEmail, loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                sendToMain();

                            } else {


                                Toast.makeText(LoginActivity.this, "Falha ao logar verifique sua senha e seu email e tente novamente!", Toast.LENGTH_LONG).show();


                            }

                            load.setVisibility(View.GONE);

                        }
                    });

                }


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){

            sendToMain();

        }


    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent (this, InitialActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity (intent);

    }


    private void sendToMain() {

        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();

    }
}
