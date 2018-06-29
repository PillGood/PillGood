package com.example.pc.pillgood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class EnrollmentActivity extends AppCompatActivity {
    Intent intent;
    JSONArray scanningResultArray;
    JSONObject scanningResult;
    EditText edit_diseaseName;
    EditText edit_when;
    EditText edit_hospital;
    String diseaseName;
    long date;
    String hospitalName;
    Button enrollmentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        intent = getIntent();
        int wayToEnroll = intent.getIntExtra("wayToEnroll", 0);

        edit_diseaseName = (EditText) findViewById(R.id.diseaseName);
        edit_when = (EditText) findViewById(R.id.when);
        edit_hospital = (EditText) findViewById(R.id.hospital);
        enrollmentBtn = (Button) findViewById(R.id.enrollmentBtn);

        if (wayToEnroll == 1) { // Fast enrollment using qr scanning
            try {
                scanningResultArray = new JSONArray(intent.getStringExtra("QRscanResult"));
                scanningResult = scanningResultArray.getJSONObject(0);

                diseaseName = scanningResult.getString("diseaseName");
                edit_diseaseName.setText(diseaseName);

                date = scanningResult.getLong("filledDate");
                edit_when.setText(Long.toString(date));

                JSONObject hospitalObj;
                try {
                    hospitalObj = scanningResult.getJSONObject("hospital");
                    hospitalName = hospitalObj.getString("hName");
                    edit_hospital.setText(hospitalName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else {
            Observable<String> obs =
                    RxTextView.textChanges(edit_diseaseName).filter(new Predicate<CharSequence>() {
                        @Override
                        public boolean test(CharSequence charSequence) throws Exception {
                            return (charSequence.length() > 3);
                        }
                    }).debounce(500, TimeUnit.MILLISECONDS)
                    .map(new Function<CharSequence, String>() {
                        @Override
                        public String apply(CharSequence charSequence) throws Exception {
                            return charSequence.toString();
                        }
                    });

            obs.subscribe(new Consumer<String>() {
                @Override
                public void accept(String s) throws Exception {
                    Log.d("autoComplete!!!!", "!!!!!!!!!");

                }
            });
        }

        enrollmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diseaseName = edit_diseaseName.getText().toString();
                hospitalName = edit_hospital.getText().toString();
                date = Long.parseLong(edit_when.getText().toString());

                // store the data
                EntryDatabaseHandler handler = EntryDatabaseHandler.getInstance(getApplicationContext());
                Entry entry = new Entry(handler.getEntryCount(), diseaseName, hospitalName, date);
                handler.addEntry(entry);
                Log.v("Database Size!!!", Integer.toString(handler.getEntryCount()));

                //return to home
                finish();
            }
        });
    }


}
