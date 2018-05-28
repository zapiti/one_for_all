package com.dev.nathan.testtcc.controler;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.nathan.testtcc.R;
import com.dev.nathan.testtcc.fragment.MapsFragment;
import com.dev.nathan.testtcc.fragment.HelpFragment;
import com.dev.nathan.testtcc.fragment.HomeFragment;
import com.dev.nathan.testtcc.fragment.NewPostFragment;
import com.dev.nathan.testtcc.fragment.ProfileFragment;
import com.dev.nathan.testtcc.fragment.WebViewFragment;
import com.dev.nathan.testtcc.panicbutton.MainPanicButtonActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

public class MainActivity extends AppCompatActivity implements DuoMenuView.OnMenuClickListener {
    //Declaração de variaveis
    private MenuAdapter mMenuAdapter;
    private ViewHolder mViewHolder;
    private MapsFragment mMap;
    private Button mLogout;
    private ArrayList<String> mTitles = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private TextView mTitle;
    private TextView mT2;
    private static final int REQUEST_LOCATION = 1;
    //Criando a view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        //Inicialização de variaveis
        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));
        mMap = new MapsFragment();
        mViewHolder = new ViewHolder();
        mTitle.setText("Um Por Todos");
        mLogout.setVisibility(View.VISIBLE);
        mLogout.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));

        //Quando clicar em deslogar
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               intro();
            }
        });

        // Açoes da toolbar
        handleToolbar();
        handleMenu();
        handleDrawer();

        // Mostrar Fragmento principal
        goToFragment(new HomeFragment());
        mMenuAdapter.setViewSelected(0, true);
        setTitle(mTitles.get(0));

        //Inicializando Firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


    }
    //Funçoes da barra de navegação entra fragmentos
    private void handleToolbar() {
        setSupportActionBar(mViewHolder.mToolbar);
    }
    private void handleDrawer() {
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
                mViewHolder.mDuoDrawerLayout,
                mViewHolder.mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mViewHolder.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();

    }
    private void handleMenu() {
        mMenuAdapter = new MenuAdapter(mTitles);

        mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
        mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);
    }

    @Override
    public void onFooterClicked() {
        Toast.makeText(this, "onFooterClicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHeaderClicked() {
        Toast.makeText(this, "onHeaderClicked", Toast.LENGTH_SHORT).show();
    }
    //Funçao de trocar fragmentos
    private void goToFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        //Titulo
        setTitle(mTitles.get(position));
        //Funcao da barra de navegaçao
        mMenuAdapter.setViewSelected(position, true);
        //Navegação entre fragmentos quando clicar em cada posiçao
        switch (position) {
            case 0 :{
                goToFragment(new HomeFragment());
                break;
            }
            case 1 :{
                goToFragment(new NewPostFragment());
                break;
            }
            case 2 :{

                if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(this), Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                        (this, Manifest.permission.SEND_RESPOND_VIA_MESSAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(Objects.requireNonNull(this), new String[]{Manifest.permission.SEND_SMS}, REQUEST_LOCATION);
                }
                else   if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(this), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                        (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(Objects.requireNonNull(this), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

                }


                else if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(this), Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                        (this, Manifest.permission.CHANGE_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(Objects.requireNonNull(this), new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_LOCATION);

                }else {

                    startActivity(new Intent(this, MainPanicButtonActivity.class));
                }
                break;
            }
            case 3 :{
                goToFragment(new MapsFragment());
                break;
            }
            case 4 :{
                goToFragment(new ProfileFragment());
                break;
            }
            case 5 :{
                goToFragment(new WebViewFragment());
                break;
            }
            case 6 : {
                goToFragment(new HelpFragment());
                break;
            }
            case 7 :{
                    goToFragment(new HelpFragment());
                    break;
            }
            default:
                goToFragment(mMap);
                break;
        }

        // Fechar drawer
        mViewHolder.mDuoDrawerLayout.closeDrawer();
    }
    // Manda para o login
    private void sendToLogin() {
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
       startActivity(loginIntent);
        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){

            sendToLogin();

        } else {

            String current_user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

            firebaseFirestore.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                        if(!task.getResult().exists()){

                            Intent setupIntent = new Intent(getApplicationContext(), SetupActivity.class);
                            startActivity(setupIntent);
                            finish();

                        }

                    } else {

                        String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(getApplicationContext(), "Erro nos carregamento : " + errorMessage, Toast.LENGTH_LONG).show();


                    }

                }
            });

        }

    }
    //Funçao de deslogar
    private void intro() {
        mAuth.signOut();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null) {
            Intent intro = new Intent(getApplicationContext(), InitialActivity.class);
            startActivity(intro);
        }
    }

    public class ViewHolder {
        private DuoDrawerLayout mDuoDrawerLayout;
        private DuoMenuView mDuoMenuView;
        private Toolbar mToolbar;

        ViewHolder() {
            mDuoDrawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            mLogout = findViewById(R.id.duo_view_footer_text);

            mTitle = findViewById(R.id.duo_view_header_text_title);
            mT2 = findViewById(R.id.duo_view_header_text_sub_title);
            mT2.setVisibility(View.GONE);

        }
    }
}
