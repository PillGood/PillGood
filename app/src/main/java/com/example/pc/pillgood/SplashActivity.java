package com.example.pc.pillgood;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        //start to creat db
        //        Intent intent = new Intent(getApplicationContext(), UpdateDBService.class);
        //        startService(intent);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}