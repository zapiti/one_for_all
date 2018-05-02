package com.dev.nathan.testtcc.controler;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dev.nathan.testtcc.R;
import com.dev.nathan.testtcc.fragment.DeclarationFragmentOptionsArea;
import com.dev.nathan.testtcc.fragment.FriendsFragment;
import com.dev.nathan.testtcc.fragment.HelpFragment;
import com.dev.nathan.testtcc.fragment.HomeFragment;
import com.dev.nathan.testtcc.fragment.NewPostFragment;
import com.dev.nathan.testtcc.fragment.PlacesToAvoidFragment;
import com.dev.nathan.testtcc.fragment.ProfileFragment;
import com.dev.nathan.testtcc.fragment.RelatedFragmentOptionsArea;
import com.dev.nathan.testtcc.fragment.WebViewFragment;
import com.dev.nathan.testtcc.helper.BottomNavigationViewHelper;
import com.dev.nathan.testtcc.maps.MapsActivity;
import com.dev.nathan.testtcc.maps.MapsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Field;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;




   private RelatedFragmentOptionsArea relatedFragmentOptionsArea;
    private HelpFragment helpFragment;
    private PlacesToAvoidFragment placesToAvoidFragment;

    private HomeFragment homeFragment;
    private FriendsFragment notificationFragment;
    private ProfileFragment profileFragment;
    private NewPostFragment newPostFragment;
    private WebViewFragment webViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Toolbar mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        RelativeLayout logoutbt = findViewById(R.id.action_logout_btn_item);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.bottom_home_text));

        logoutbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Logout")
                        .setMessage("Deseja deslogar?")

                        .setPositiveButton("sim",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        intro();
                                    }
                                })
                        .setNegativeButton("não", null)
                        .show();
            }
        });



        if(mAuth.getCurrentUser() != null) {

            BottomNavigationView mainbottomNav = findViewById(R.id.mainBottomNav);

            // FRAGMENTS
            homeFragment = new HomeFragment();
            notificationFragment = new FriendsFragment();
            profileFragment = new ProfileFragment();
            newPostFragment = new NewPostFragment();
            helpFragment = new HelpFragment();
            placesToAvoidFragment = new PlacesToAvoidFragment();
            relatedFragmentOptionsArea = new RelatedFragmentOptionsArea(getString(R.string.info_title));
            webViewFragment = new WebViewFragment();

            initializeFragment();
            disableShiftMode(mainbottomNav);
            BottomNavigationViewHelper.removeShiftMode(mainbottomNav);
            mainbottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);

                    switch (item.getItemId()) {

                        case R.id.bottom_action_home:

                            replaceFragment(homeFragment);
                            getSupportActionBar().setTitle(getString(R.string.bottom_home_text));
                            return true;

                        case R.id.bottom_action_profile:

                            replaceFragment(profileFragment);
                            getSupportActionBar().setTitle(getString(R.string.menu_title_profile));
                            return true;

                        //blogs de apoio
                        case  R.id.support_blogs_item :
                            replaceFragment(webViewFragment);
                            getSupportActionBar().setTitle(getString(R.string.blog_title));
                            return true;
                        case R.id.bottom_action_new_post:



                            replaceFragment(newPostFragment);
                            getSupportActionBar().setTitle(getString(R.string.menu_title_new_post));
                            return true;



                        default:
                            return false;


                    }

                }
            });




        }


        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mainToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void intro() {
        mAuth.signOut();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null) {
            Intent intro = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intro);
        }
    }


    //fixa os bottoes na navigation vieew button

    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);

        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
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

                            Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                            startActivity(setupIntent);
                            finish();

                        }

                    } else {

                        String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(MainActivity.this, "Erro nos carregamento : " + errorMessage, Toast.LENGTH_LONG).show();


                    }

                }
            });

        }

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//
//            case R.id.action_logout_btn:
//                logOut();
//                return true;
//
//            case R.id.action_settings_btn:
//
//                Intent settingsIntent = new Intent(MainActivity.this, SetupActivity.class);
//                startActivity(settingsIntent);
//
//                return true;
//
//
//            default:
//                return false;
//
//
//        }
//
//    }




    private void logOut() {

        mAuth.signOut();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            sendToLogin();
        }

    }

    private void sendToLogin() {

        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();

    }




    private void initializeFragment(){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.main_container, homeFragment);
        fragmentTransaction.add(R.id.main_container, notificationFragment);
        fragmentTransaction.add(R.id.main_container, profileFragment);
        fragmentTransaction.add(R.id.main_container, newPostFragment);
        fragmentTransaction.add(R.id.main_container, helpFragment);
        fragmentTransaction.add(R.id.main_container, placesToAvoidFragment);
        fragmentTransaction.add(R.id.main_container,relatedFragmentOptionsArea);
        fragmentTransaction.add(R.id.main_container, webViewFragment);

        fragmentTransaction.hide(webViewFragment);
        fragmentTransaction.hide(relatedFragmentOptionsArea);
        fragmentTransaction.hide(placesToAvoidFragment);
        fragmentTransaction.hide(helpFragment);
        fragmentTransaction.hide(newPostFragment);
        fragmentTransaction.hide(notificationFragment);
        fragmentTransaction.hide(profileFragment);

        fragmentTransaction.commit();

    }

    private void replaceFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().setCustomAnimations( R.anim.slide_in_left, R.anim.slide_out_left);
        if(fragment == homeFragment){

            fragmentTransaction.hide(helpFragment);
            fragmentTransaction.hide(newPostFragment);
            fragmentTransaction.hide(profileFragment);
            fragmentTransaction.hide(notificationFragment);
            fragmentTransaction.hide(placesToAvoidFragment);
            fragmentTransaction.hide(relatedFragmentOptionsArea);
            fragmentTransaction.hide(webViewFragment);
        }

        if(fragment == profileFragment){

            fragmentTransaction.hide(helpFragment);
            fragmentTransaction.hide(newPostFragment);
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(notificationFragment);
            fragmentTransaction.hide(placesToAvoidFragment);
            fragmentTransaction.hide(relatedFragmentOptionsArea);
            fragmentTransaction.hide(webViewFragment);
        }

        if(fragment == notificationFragment){

            fragmentTransaction.hide(helpFragment);
            fragmentTransaction.hide(newPostFragment);
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(profileFragment);
            fragmentTransaction.hide(placesToAvoidFragment);
            fragmentTransaction.hide(relatedFragmentOptionsArea);
            fragmentTransaction.hide(webViewFragment);
        }
        if(fragment == newPostFragment){

            fragmentTransaction.hide(helpFragment);
            fragmentTransaction.hide(notificationFragment);
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(profileFragment);
            fragmentTransaction.hide(placesToAvoidFragment);
            fragmentTransaction.hide(relatedFragmentOptionsArea);
            fragmentTransaction.hide(webViewFragment);
        }
        if(fragment == helpFragment){

            fragmentTransaction.hide(newPostFragment);
            fragmentTransaction.hide(notificationFragment);
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(profileFragment);
            fragmentTransaction.hide(placesToAvoidFragment);
            fragmentTransaction.hide(relatedFragmentOptionsArea);
            fragmentTransaction.hide(webViewFragment);
        }
        if(fragment == placesToAvoidFragment){

            fragmentTransaction.hide(helpFragment);
            fragmentTransaction.hide(newPostFragment);
            fragmentTransaction.hide(notificationFragment);
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(profileFragment);
            fragmentTransaction.hide(relatedFragmentOptionsArea);
            fragmentTransaction.hide(webViewFragment);

        }
        if(fragment == relatedFragmentOptionsArea){

            fragmentTransaction.hide(helpFragment);
            fragmentTransaction.hide(newPostFragment);
            fragmentTransaction.hide(notificationFragment);
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(profileFragment);
            fragmentTransaction.hide(placesToAvoidFragment);
            fragmentTransaction.hide(webViewFragment);
        }
        if(fragment == webViewFragment){

            fragmentTransaction.hide(helpFragment);
            fragmentTransaction.hide(newPostFragment);
            fragmentTransaction.hide(notificationFragment);
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(profileFragment);
            fragmentTransaction.hide(placesToAvoidFragment);

        }



        fragmentTransaction.show(fragment);

        //fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();

    }







    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }
//todo:aqui

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            DrawerLayout drawer =  findViewById(R.id.drawer_layout);

            drawer.openDrawer(GravityCompat.END);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
        //region <! principal !>
        //ajuda
        if(id ==R.id.help_item) {
            replaceFragment(helpFragment);
        }
        //locais a ser evitados
        if(id == R.id.places_to_avoid_item ) {
           startActivity(new Intent(MainActivity.this, MapsFragment.class));
        }
        //endregion

        //region <! relacionados !>
        //informaçoes
        if(id ==R.id.information_item ) {
            relatedFragmentOptionsArea = new RelatedFragmentOptionsArea(getString(R.string.info_title));
            replaceFragment(relatedFragmentOptionsArea);
        }

        //como se proteger
        if(id ==   R.id.how_to_protect_yourself_item ) {
            relatedFragmentOptionsArea = new RelatedFragmentOptionsArea(getString(R.string.protect_title));
            replaceFragment(relatedFragmentOptionsArea);
        }





        //midias sociais
        if(id == R.id.social_media_item ) {
            replaceFragment(homeFragment);
        }

        //endregion

        //region <! declaraçoes !>
        //sobre nos
        DeclarationFragmentOptionsArea declarationFragmentOptionsArea;
        if(id == R.id.about_us_item ) {
            declarationFragmentOptionsArea = new DeclarationFragmentOptionsArea(getString(R.string.about_us_title));
            replaceFragment(declarationFragmentOptionsArea);
        }
        //inspiraçoes
        if(id ==R.id.inspiration_item ) {
            declarationFragmentOptionsArea = new DeclarationFragmentOptionsArea(getString(R.string.inspiration_title));
            replaceFragment(declarationFragmentOptionsArea);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
