package com.example.pc.pillgood;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton plusBtn, fastEnrollmentBtn, enrollmentBtn;
    Boolean plusBtnIsClicked = false;
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager =  findViewById(R.id.viewpager);
        MainFragmentAdapter adapter = new MainFragmentAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // initializing scan object
        qrScan = new IntentIntegrator(this);

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
                qrScan.setPrompt("Scanning...");
                qrScan.initiateScan();
            }
        });


        enrollmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EnrollmentActivity.class);
                intent.putExtra("wayToEnroll",2);
                startActivity(intent);
            }
        });
    }

    //getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            // There is no qr code
            if (result.getContents() == null) {
                Toast.makeText(MainActivity.this, "Cancle!", Toast.LENGTH_LONG).show();
            }
            else {
                // There is a result
                Toast.makeText(MainActivity.this, "Done!", Toast.LENGTH_SHORT);

                try {
//                    JSONObject scanningResult = new JSONObject(result.getContents());
                    JSONArray scanningResult = new JSONArray(result.getContents());

                    /**********/
                    // intent to enrollmentActivity and deliver the result to enrollmentActivity
                    Intent intent = new Intent(this, EnrollmentActivity.class);
                    intent.putExtra("wayToEnroll", 1);
                    intent.putExtra("QRscanResult", scanningResult.toString());
                    startActivity(intent);
                    /**********/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
