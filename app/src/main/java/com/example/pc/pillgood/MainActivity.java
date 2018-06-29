package com.example.pc.pillgood;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton plusBtn, fastEnrollmentBtn, enrollmentBtn;
    Boolean plusBtnIsClicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager =  findViewById(R.id.viewpager);
        MainFragmentAdapter adapter = new MainFragmentAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //-----pop up floating btn
        plusBtn = (FloatingActionButton) findViewById(R.id.mainPlusBtn);
        fastEnrollmentBtn = (FloatingActionButton) findViewById(R.id.mainFastEnrollmentBtn);
        enrollmentBtn = (FloatingActionButton) findViewById(R.id.mainEnrollmentBtn);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation plusBtnAnim, fastEnrollmnetAnim, enrollmentAnim;
                plusBtnAnim=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_plusbtn);
                if (plusBtnIsClicked) {
                    fastEnrollmentBtn.setVisibility(View.INVISIBLE);
                    enrollmentBtn.setVisibility(View.INVISIBLE);
                    fastEnrollmnetAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_down_fastbtn);
                    enrollmentAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_down_enrollmentbtn);
                    plusBtnIsClicked = false;
                }
                else {
                    fastEnrollmentBtn.setVisibility(View.VISIBLE);
                    enrollmentBtn.setVisibility(View.VISIBLE);
                    fastEnrollmnetAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_up_fastbtn);
                    enrollmentAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_up_enrollmentbtn);
                    plusBtnIsClicked = true;
                }
                plusBtn.startAnimation(plusBtnAnim);
                fastEnrollmentBtn.startAnimation(fastEnrollmnetAnim);
                enrollmentBtn.startAnimation(enrollmentAnim);
            }
        });

        fastEnrollmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        enrollmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
