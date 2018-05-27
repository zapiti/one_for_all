package com.dev.nathan.testtcc.inicializador;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.dev.nathan.testtcc.R;
import com.dev.nathan.testtcc.component.MapsActivity;
import com.dev.nathan.testtcc.fragment.MapsFragment;
import com.dev.nathan.testtcc.controler.LoginActivity;

import com.dev.nathan.testtcc.controler.SetupActivity;
import com.dev.nathan.testtcc.fragment.FriendsFragment;
import com.dev.nathan.testtcc.fragment.HelpFragment;
import com.dev.nathan.testtcc.fragment.HomeFragment;
import com.dev.nathan.testtcc.fragment.NewPostFragment;
import com.dev.nathan.testtcc.fragment.PlacesToAvoidFragment;
import com.dev.nathan.testtcc.fragment.ProfileFragment;
import com.dev.nathan.testtcc.fragment.RelatedFragmentOptionsArea;
import com.dev.nathan.testtcc.fragment.WebViewFragment;
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

public class secondActivity extends AppCompatActivity implements DuoMenuView.OnMenuClickListener {
    private MenuAdapter mMenuAdapter;
    private ViewHolder mViewHolder;
    private MapsFragment mMap;

    private ArrayList<String> mTitles = new ArrayList<>();
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
        setContentView(R.layout.activity_initial);
        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));

        mMap = new MapsFragment();
        // Initialize the views
        mViewHolder = new ViewHolder();

        // Handle toolbar actions
        handleToolbar();

        // Handle menu actions
        handleMenu();

        // Handle drawer actions
        handleDrawer();

        // Show main fragment in container
        goToFragment(new MainFragment());
        mMenuAdapter.setViewSelected(0, true);
        setTitle(mTitles.get(0));

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        // FRAGMENTS
        homeFragment = new HomeFragment();
        notificationFragment = new FriendsFragment();
        profileFragment = new ProfileFragment();
        newPostFragment = new NewPostFragment();
        helpFragment = new HelpFragment();
        placesToAvoidFragment = new PlacesToAvoidFragment();
        relatedFragmentOptionsArea = new RelatedFragmentOptionsArea(getString(R.string.info_title));
        webViewFragment = new WebViewFragment();

    }

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

    private void goToFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.container, fragment).commit();
    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        // Set the toolbar title
        setTitle(mTitles.get(position));

        // Set the right options selected
        mMenuAdapter.setViewSelected(position, true);

        // Navigate to the right fragment
        switch (position) {







        case 0 :{ Toast.makeText(this,"posiçao"+position,Toast.LENGTH_LONG).show();
            goToFragment(homeFragment);
            break;
            }
            case 1 :{ Toast.makeText(this,"posiçao"+position,Toast.LENGTH_LONG).show();
                goToFragment(profileFragment);
                break;
            }

            case 2 :{ Toast.makeText(this,"posiçao"+position,Toast.LENGTH_LONG).show();
            goToFragment(notificationFragment);
                break;
            }
            case 3 :{ Toast.makeText(this,"posiçao"+position,Toast.LENGTH_LONG).show();
                goToFragment(profileFragment);
                break;
            }
            case 4 :{ Toast.makeText(this,"posiçao"+position,Toast.LENGTH_LONG).show();
                goToFragment(newPostFragment);
                break;
            }
            case 5 :{ Toast.makeText(this,"posiçao"+position,Toast.LENGTH_LONG).show();
                goToFragment(helpFragment);
                break;
            }








            default:
                goToFragment(mMap);
                break;
        }

        // Close the drawer
        mViewHolder.mDuoDrawerLayout.closeDrawer();
    }

    private void logOut() {

        mAuth.signOut();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            sendToLogin();
        }

    }

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



    private void intro() {
        mAuth.signOut();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null) {
            Intent intro = new Intent(getApplicationContext(), MapsActivity.class);
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
        }
    }
}
