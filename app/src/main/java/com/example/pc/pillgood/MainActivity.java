package com.example.pc.pillgood;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.zxing.integration.android.IntentIntegrator;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton plusBtn, fastEnrollmentBtn, enrollmentBtn;
    Boolean plusBtnIsClicked = false;
    private IntentIntegrator qrScan;
    private RecyclerView rvNavigation;
    DrawerLayout drawer;
    private Toolbar toolbar;
    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_WIFI_STATE,
    Manifest.permission.CHANGE_WIFI_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        requestAllPermissions();

        ViewPager viewPager =  findViewById(R.id.viewpager);
        MainFragmentAdapter adapter = new MainFragmentAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        prepareNavigation();

        // initializing scan object
        qrScan = new IntentIntegrator(this);

        //-----pop up floating btn
        plusBtn = findViewById(R.id.mainPlusBtn);
        fastEnrollmentBtn =  findViewById(R.id.mainFastEnrollmentBtn);
        enrollmentBtn =  findViewById(R.id.mainEnrollmentBtn);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation plusBtnAnim, fastEnrollmentAnim, enrollmentAnim;
                plusBtnAnim= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_plusbtn);
                if (plusBtnIsClicked) {
                    fastEnrollmentBtn.setVisibility(View.INVISIBLE);
                    enrollmentBtn.setVisibility(View.INVISIBLE);
                    fastEnrollmentAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_down_fastbtn);
                    enrollmentAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_down_enrollmentbtn);
                    plusBtnIsClicked = false;
                } else {
                    fastEnrollmentBtn.setVisibility(View.VISIBLE);
                    enrollmentBtn.setVisibility(View.VISIBLE);
                    fastEnrollmentAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_up_fastbtn);
                    enrollmentAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_up_enrollmentbtn);
                    plusBtnIsClicked = true;
                }
                plusBtn.startAnimation(plusBtnAnim);
                fastEnrollmentBtn.startAnimation(fastEnrollmentAnim);
                enrollmentBtn.startAnimation(enrollmentAnim);
            }
        });

        fastEnrollmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.setPrompt("Scanning...");
                qrScan.initiateScan();
            }
        });


        enrollmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void prepareNavigation(){
        rvNavigation = findViewById(R.id.navigation_drawer);
        String[] titles = {getResources().getString(R.string.navigation_home),
                getResources().getString(R.string.navigation_map),
                getResources().getString(R.string.navigation_profile)};
        int[] elements = {R.drawable.ic_keyboard_arrow_down_black_48dp,
                R.drawable.ic_keyboard_arrow_down_black_48dp,
                R.drawable.ic_keyboard_arrow_down_black_48dp};
        NavigationAdapter navigationAdapter = new NavigationAdapter(titles, elements);
        rvNavigation.setHasFixedSize(true);
        rvNavigation.setLayoutManager(new LinearLayoutManager(this));
        rvNavigation.setAdapter(navigationAdapter);
        navigationAdapter.setOnItemClickListener(new NavigationAdapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View v, int position) {
                switch (position) {
                    case 1:
                        break;
                    case 2:
                        Intent intent=new Intent(getApplicationContext(), MapActivity.class);
                        startActivity(intent);
                        break;
                    case 3:

                        break;
                }
                drawer.closeDrawers();
            }
        });
        drawer = findViewById(R.id.drawer_layout);        // drawer object Assigned to the view
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(mDrawerToggle); // drawer Listener set to the drawer toggle
        mDrawerToggle.syncState();
        mDrawerToggle.setDrawerIndicatorEnabled(true);
    }
    //getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void requestAllPermissions() {
        if (EasyPermissions.hasPermissions(this, REQUIRED_PERMISSIONS)) {

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "SDGFSDFSDF", 1, REQUIRED_PERMISSIONS);
        }
    }
}
