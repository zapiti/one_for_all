package com.dev.nathan.testtcc.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dev.nathan.testtcc.R;
import com.dev.nathan.testtcc.test.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ProfileFragment extends Fragment {



    private String user_id;



    private EditText setupName;
    private EditText setupFiliacao;
    private EditText setupTelefone;
    private EditText setupEmailExistent;
    private LinearLayout load;

    private Button setupBtn;


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;


    public ProfileFragment()  {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        final Context context = view.getContext();


        firebaseAuth = FirebaseAuth.getInstance();
        user_id = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();





        setupBtn = view.findViewById(R.id.setup_btn);
        load =  view.findViewById(R.id.avi2contentProfile);

        setupName = view.findViewById(R.id.setup_name);
        setupFiliacao = view.findViewById(R.id.setup_parent);
        setupTelefone =view. findViewById(R.id.setup_telefone);
        setupEmailExistent =view. findViewById(R.id.setup_email_existente);


        load.setVisibility(View.VISIBLE);
        setupBtn.setEnabled(false);

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){

                        String name = task.getResult().getString("name");
                        String filiacao = task.getResult().getString("filiacao");
                        String telefone = task.getResult().getString("telefone");
                        String emailexistent = task.getResult().getString("endexistent");



                        setupName.setText(name);
                        setupFiliacao .setText(filiacao);
                        setupTelefone .setText(telefone);
                        setupEmailExistent.setText(emailexistent);
                    }

                } else {


                    Toast.makeText(context, "Erro ao carregar", Toast.LENGTH_LONG).show();

                }

                load.setVisibility(View.GONE);
                setupBtn.setEnabled(true);

            }
        });


        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String user_name = setupName.getText().toString();
                final String user_telefone = setupTelefone.getText().toString();
                final String user_filiacao = setupFiliacao.getText().toString();
                final String user_emailexistent = setupEmailExistent.getText().toString();

                if (!TextUtils.isEmpty(user_name) ) {

                    load.setVisibility(View.VISIBLE);



                    user_id = firebaseAuth.getCurrentUser().getUid();





                    storeFirestore( user_name,user_emailexistent,user_filiacao,user_telefone);



                }

            }

        });


return view;


    }

    private void storeFirestore(String user_name, String user_emailexistent, String user_filiacao, String user_telefone) {


        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", user_name);
        userMap.put("filiacao", user_filiacao);
        userMap.put("telefone", user_telefone);
        userMap.put("endexistent", user_emailexistent);

        firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful() ){

                    Toast.makeText(getContext(), "Configurações do usuário  atualizadas.", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(getContext(), MainActivity.class);
                    startActivity(mainIntent);
                    Objects.requireNonNull(getActivity()).finish();
                        //todo/;resolver

                } else {


                    Toast.makeText(getContext(), "Erro nos dados", Toast.LENGTH_LONG).show();

                }

                load.setVisibility(View.GONE);

            }
        });


    }




}
