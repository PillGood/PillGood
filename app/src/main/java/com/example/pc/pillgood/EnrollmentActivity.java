package com.example.pc.pillgood;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;


import com.jakewharton.rxbinding2.widget.RxTextView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;


public class EnrollmentActivity extends AppCompatActivity {
    Intent intent;
    JSONObject scanningResultArray;
    JSONObject scanningResult;
    EditText edit_diseaseName;
    EditText edit_hospital;
    TextView tvDate;
    String diseaseName;
    String date = "";
    String hospitalName;
    Button enrollmentBtn;
    Button cancleBtn;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    Calendar calendar = Calendar.getInstance();
    TextView alarmTime;
    Switch alarmSwitch;
    List<String> pillList = null;
    int filledYear, filledMonth, filledDay;
    int alarmHour, alarmMin;
    AutoCompleteTextView autoCompleteTextView0;
    AutoCompleteTextView autoCompleteTextView1;
    AutoCompleteTextView autoCompleteTextView2;
    AutoCompleteTextView autoCompleteTextView3;
    AutoCompleteTextView autoCompleteTextView4;

    ServerHandler serverHandler = new ServerHandler();
    List<String> autoCompleteList = new ArrayList<>();

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            filledYear = datePicker.getYear();
            filledMonth = datePicker.getMonth()+1;
            filledDay = datePicker.getDayOfMonth();
            String date = String.format("%d년 %d월 %d일", filledYear, filledMonth, filledDay);
            tvDate.setText(date);
        }
    };

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            alarmHour = timePicker.getHour();
            alarmMin = timePicker.getMinute();
            String time = String.format("%02d:%02d", alarmHour, alarmMin);
            alarmTime.setText(time);
            alarmSwitch.setChecked(true);

            SharedPreferences pref = getSharedPreferences("Push", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("alarm", true);
            editor.commit();
        }
    };

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        intent = getIntent();
        int wayToEnroll = intent.getIntExtra("wayToEnroll", 0);

        edit_diseaseName = (EditText) findViewById(R.id.diseaseName);
        edit_hospital = (EditText) findViewById(R.id.hospital);
        tvDate = findViewById(R.id.whenText);
        enrollmentBtn = (Button) findViewById(R.id.enrollmentBtn);
        alarmTime = findViewById(R.id.timeData);
        alarmSwitch = findViewById(R.id.alarmSwitch);
        cancleBtn = findViewById(R.id.cancelButton);

        final int curYear = calendar.get(Calendar.YEAR);
        final int curMonth = calendar.get(Calendar.MONTH);
        final int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        final int curHour = calendar.get(Calendar.HOUR_OF_DAY);
        final int curMinute = calendar.get(Calendar.MINUTE);

        datePickerDialog = new DatePickerDialog(this, dateSetListener, curYear, curMonth, curDay);
        timePickerDialog = new TimePickerDialog(this, timeSetListener, curHour, curMinute, true);

        autoCompleteTextView0 = (AutoCompleteTextView) findViewById(R.id.list);
        autoCompleteTextView1 = (AutoCompleteTextView) findViewById(R.id.list1);
        autoCompleteTextView2 = (AutoCompleteTextView) findViewById(R.id.list2);
        autoCompleteTextView3 = (AutoCompleteTextView) findViewById(R.id.list3);
        autoCompleteTextView4 = (AutoCompleteTextView) findViewById(R.id.list4);


        if (wayToEnroll == 1) { // Fast enrollment using qr scanning
            try {
                scanningResult = new JSONObject(intent.getStringExtra("QRscanResult"));


                date = scanningResult.getString("date");
                int year = Integer.parseInt(date.substring(0,4));
                int month = Integer.parseInt(date.substring(4,6));
                int day = Integer.parseInt(date.substring(6));
                tvDate.setText(year + "년 " + month + "월 "+ day + "일");

                hospitalName = scanningResult.getString("hospital_name");
                edit_hospital.setText(hospitalName);

                JSONArray pillArray;
                JSONObject pillObj;

                try {
                    pillArray = scanningResult.getJSONArray("pills");
                    for (int i=0; i<5; i++) {
                        pillObj = pillArray.getJSONObject(i);
                        getAutoCompleteViewObject(i).setText(pillObj.getString("pill_name"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
        }

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        alarmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });

        enrollmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diseaseName = edit_diseaseName.getText().toString();
                hospitalName = edit_hospital.getText().toString();
                date += String.format("%04d",filledYear);
                date += String.format("%02d", filledMonth);
                date += String.format("%02d", filledDay);

                // store the data
                EntryDatabaseHandler handler = EntryDatabaseHandler.getInstance(getApplicationContext());
                Log.v("Database Size!!!", Integer.toString(handler.getEntryCount()));
                Entry entry = new Entry(handler.getEntryCount(), diseaseName, hospitalName, Long.parseLong(date));
                handler.addEntry(entry);
                Log.v("Database Size!!!", Integer.toString(handler.getEntryCount()));

                // set push alarm
                AlarmManager manager = (AlarmManager) getSystemService(getApplicationContext().ALARM_SERVICE);
                Intent intent = new Intent(EnrollmentActivity.this, PushAlarmBroadcast.class);
                intent.putExtra("diseaseName", diseaseName);

                PendingIntent sender = PendingIntent.getBroadcast(EnrollmentActivity.this, 0, intent, 0);

                //set alarm
                calendar.set(curYear, curMonth, curDay, alarmHour, alarmMin, 0);

                //book alarm
                manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

                //return to home
                finish();
            }
        });

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private AutoCompleteTextView getAutoCompleteViewObject(int i) {
        if (i == 0)
            return this.autoCompleteTextView0;
        else if (i == 1)
            return this.autoCompleteTextView1;
        else if (i == 2)
            return this.autoCompleteTextView2;
        else if (i == 3)
            return this.autoCompleteTextView3;
        else if (i == 4)
            return this.autoCompleteTextView4;
        else
            return null;
    }
}

