package com.example.pc.pillgood;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jakewharton.rxbinding2.widget.RxTextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class EnrollmentActivity extends AppCompatActivity {
    Intent intent;
    JSONArray scanningResultArray;
    JSONObject scanningResult;
    EditText edit_diseaseName;
    EditText edit_hospital;
    TextView tvDate;
    String diseaseName;
    long date;
    String hospitalName;
    Button enrollmentBtn;
    Button cancleBtn;
    Long tempDate;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    Calendar calendar = Calendar.getInstance();
    TextView alarmTime;
    Switch alarmSwitch;

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            String date = String.format("%d년 %d월 %d일", datePicker.getYear(), datePicker.getMonth()+1, datePicker.getDayOfMonth());
            tvDate.setText(date);
        }
    };

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            String time = String.format("%02d:%02d", timePicker.getHour(), timePicker.getMinute());
            alarmTime.setText(time);
            alarmSwitch.setChecked(true);
        }
    };

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

        if (wayToEnroll == 1) { // Fast enrollment using qr scanning
            try {
                scanningResultArray = new JSONArray(intent.getStringExtra("QRscanResult"));
                scanningResult = scanningResultArray.getJSONObject(0);

                diseaseName = scanningResult.getString("diseaseName");
                edit_diseaseName.setText(diseaseName);

                date = scanningResult.getLong("filledDate");
                DateTime dateTime = new DateTime(date);
                DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy.MM.dd");
                tvDate.setText(dateTimeFormatter.print(dateTime));
                tempDate = date;

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
        } else {
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
                date = tempDate;

                // store the data
                EntryDatabaseHandler handler = EntryDatabaseHandler.getInstance(getApplicationContext());
                Log.v("Database Size!!!", Integer.toString(handler.getEntryCount()));
                Entry entry = new Entry(handler.getEntryCount(), diseaseName, hospitalName, date);
                handler.addEntry(entry);
                Log.v("Database Size!!!", Integer.toString(handler.getEntryCount()));

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
}
