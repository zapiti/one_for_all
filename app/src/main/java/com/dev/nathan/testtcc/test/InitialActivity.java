package com.dev.nathan.testtcc.test;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.nathan.testtcc.R;
import com.dev.nathan.testtcc.controler.LoginActivity;
import com.dev.nathan.testtcc.fragment.HelpFragment;
import com.dev.nathan.testtcc.fragment.HomeFragment;
import com.dev.nathan.testtcc.fragment.MapsFragment;
import com.dev.nathan.testtcc.fragment.NewPostFragment;
import com.dev.nathan.testtcc.fragment.ProfileFragment;
import com.dev.nathan.testtcc.fragment.WebViewFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

public class InitialActivity extends AppCompatActivity implements DuoMenuView.OnMenuClickListener {
    private MenuAdapter mMenuAdapter;
    private ViewHolder mViewHolder;
    private Button mButonn;
    private MapsFragment mMap;
    private TextView mTitle;
    private TextView mT2;

    private ArrayList<String> mTitles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));

        // Initialize the views
        mViewHolder = new ViewHolder();
        mTitle.setText("Um Por Todos");




        mButonn.setVisibility(View.INVISIBLE);

        // Handle toolbar actions
        handleToolbar();

        // Handle menu actions
        handleMenu();

        // Handle drawer actions
        handleDrawer();

        // Show main fragment in container
        goToFragment(new MapsFragment());
        mMenuAdapter.setViewSelected(3, true);
        setTitle(mTitles.get(3));
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

        transaction.replace(R.id.container, fragment).commit();
    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        // Set the toolbar title


        // Set the right options selected
        mMenuAdapter.setViewSelected(position, true);



                if(position == 3){
                    goToFragment(new MapsFragment());
                }else {

                    Validar();


                }


        // Close the drawer
        mViewHolder.mDuoDrawerLayout.closeDrawer();
    }

    private void Validar() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(InitialActivity.this);
        builder.setMessage("Para acessar esses menus voce deve estar logado!\nDeseja continuar?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(InitialActivity.this, MainActivity.class));
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                }).show();
    }

    private class ViewHolder {
        private DuoDrawerLayout mDuoDrawerLayout;
        private DuoMenuView mDuoMenuView;
        private Toolbar mToolbar;

        ViewHolder() {
            mDuoDrawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
            mToolbar = (Toolbar) findViewById(R.id.toolbar);

            mButonn = findViewById(R.id.duo_view_footer_text);
            mTitle = findViewById(R.id.duo_view_header_text_title);
            mT2 = findViewById(R.id.duo_view_header_text_sub_title);
            mT2.setVisibility(View.GONE);


        }
    }
}
