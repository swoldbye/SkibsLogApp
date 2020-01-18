package com.example.skibslogapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.skibslogapp.etapeoversigt.EtapeOversigt_frag;
import com.example.skibslogapp.model.Togt;
import com.example.skibslogapp.view.LogOversigt_frag;
import com.example.skibslogapp.view.OpretLog_frag;
import com.example.skibslogapp.view.oprettogt.OpretTogt_frag;
import com.example.skibslogapp.view.togtoversigt.TogtOversigt_frag;
import com.example.skibslogapp.view.UdtagData_frag;
import com.google.android.material.navigation.NavigationView;

/**
 *  This class contains the main activity and its functionalities:
 *
 *  - Toolbar
 *  - Left menu
 *
 *  MainActivity has a fragment container beneath the toolbar that shifts between fragments.
 */
public class Main_akt extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private OpretLog_frag opretLog_frag;
    private TogtOversigt_frag togtOversigt_frag;
    private LogOversigt_frag logOversigt_frag;
    private OpretTogt_frag opretTogt_frag;
    private EtapeOversigt_frag etapeOversigt_frag;
    private Togt togt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//      Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_white_18dp);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        configureNavigationDrawer();

        if (savedInstanceState == null) {
            Fragment fragment = new TogtOversigt_frag();
            getSupportFragmentManager().beginTransaction().add(R.id.fragContainer, fragment).commit();
        }
    }



    /**
     * If this method is set to a certain menu, then 3 points can be pressed in the upper right
     * corner of the toolbar, and a menu will emerge there.
     *
     * We only need the left menu, so this will be set to an empty menu, so no dots emerges
     *
     * @param menu The menu that will be inflated
     * @return True
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty, menu);
        return true;
    }

    /**
     * This function, gives functionalities to the left menu elements.
     *
     */

    UdtagData_frag udata = new UdtagData_frag();
    private void configureNavigationDrawer(){
        NavigationView navigationView;
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.leftMenu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                logOversigt_frag = new LogOversigt_frag();
                togtOversigt_frag = new TogtOversigt_frag();
                opretTogt_frag = new OpretTogt_frag();
                opretLog_frag = new OpretLog_frag();
                etapeOversigt_frag = new EtapeOversigt_frag(togt);

                int itemid = menuItem.getItemId();

                //Add functionalities to the menu items.

                if (itemid == R.id.nav_opret_togt) {
                    changeFragFromMenu(opretTogt_frag);

                } else if (itemid == R.id.nav_togt_oversigt) {
                    changeFragFromMenu(togtOversigt_frag);

                } else if (itemid == R.id.nav_opret_etape) {


                } else if (itemid == R.id.nav_etape_oversigt) {
                    changeFragFromMenu(etapeOversigt_frag);

                }else if (itemid == R.id.nav_opret_log){
                    changeFragFromMenu(opretLog_frag);

                }else if (itemid == R.id.nav_log_oversigt){
                   //changeFragFromMenu(logOversigt_frag);


                }else if (itemid == R.id.nav_email){
                    //todo: Make sure you can get back from this frag fragmentTransaction.addToBackStack(null);
                    changeFragFromMenu(udata);
                    //fragmentTransaction.addToBackStack("udata");

                }else {
                    //rework this________________________________
                    Toast.makeText(Main_akt.this,"Du klikkede på noget ikke funktionelt. prøv igen",
                            Toast.LENGTH_LONG).show();

                }
                return false;
            }
        });
    }

    /**
     * This function give functionalities to the toolbar Views
     *
     * @param menuItem The different items in the toolbar.
     * @return true if the function can be executed
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        switch (itemId) {

            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            // Indsæt flere entries, hvis der er...
        }
        return true;
    }

    /**
     * Code snippet taken from https://stackoverflow.com/questions/4828636/edittext-clear-focus-on-touch-outside
     * Hides the keyboard when going out of focus. Should also work for fragments
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * Helper function to change fragment from the left menu
     *
     * @param fragment The fragment you want to change to.
     * @return true
     */
    public boolean changeFragFromMenu(Fragment fragment) {
        FragmentManager fragmentManager = Main_akt.this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragContainer, fragment);
        fragmentTransaction.commit();
        drawerLayout.closeDrawers();
        return true;
    }

    /**
     * By calling this function you can hide the toolbar.
     */
    public void hideToolbar() {
        this.getSupportActionBar().hide();
    }

    /**
     * By calling this function you can show the toolbar.
     */
    public void showToolbar() {
        this.getSupportActionBar().show();
    }





}