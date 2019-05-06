package com.example.nlp_sch;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nlp_sch.model.Sch_DB;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class Scheduling extends AppCompatActivity {
    private EditText topic,title,notes;
    private TextView date,stime,etime;
    Realm realm;
    private String startD,endD,startT,endT,fullSD,fullED;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener,timeSetListener2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduling);
        realm=Realm.getDefaultInstance();
        topic=findViewById(R.id.tpoicTxt);
        title=findViewById(R.id.editText);
        date=findViewById(R.id.datepick);
        stime=findViewById(R.id.StartTime);
        etime=findViewById(R.id.endTime);

        notes=findViewById(R.id.editText3);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog=new DatePickerDialog(Scheduling.this,
                        R.style.Theme_AppCompat_Dialog_MinWidth,dateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(2,63,59)));
                dialog.show();
            }
        });
        stime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal=Calendar.getInstance();
                int hours=cal.get(Calendar.HOUR);
                int minute=cal.get(Calendar.MINUTE);
                TimePickerDialog dialog=new TimePickerDialog(Scheduling.this,
                        R.style.Theme_AppCompat_DayNight_Dialog_MinWidth,timeSetListener,hours,minute,true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(2,63,59)));
                dialog.show();
            }
        });
        etime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal=Calendar.getInstance();
                int hours=cal.get(Calendar.HOUR);
                int minute=cal.get(Calendar.MINUTE);
                TimePickerDialog dialog=new TimePickerDialog(Scheduling.this,
                        R.style.Theme_AppCompat_DayNight_Dialog_MinWidth,timeSetListener2,hours,minute,true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(2,63,59)));
                dialog.show();
            }
        });
        dateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                startD=""+day+"/"+(month+1)+"/"+year;
                endD="0"+day+"/0"+(month+1)+"/"+year;
                date.setText(startD);
            }
        };
        timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minute) {
                startT=hours+":"+minute;
                stime.setText(startT);
            }
        };
        timeSetListener2=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minute) {
                endT=hours+":"+minute;
                etime.setText(endT);
            }
        };
        Button submit=findViewById(R.id.button2);
        Button cancle=findViewById(R.id.button5);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comitData();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void comitData() {
        fullSD=startD+ " "+startT;
        fullED=endD+" "+endT;
        realm.executeTransactionAsync(new Realm.Transaction() {
                                          @Override
                                          public void execute(Realm realm) {
                                              Sch_DB sch_db = realm.createObject(Sch_DB.class);
                                              sch_db.setTopic(topic.getText().toString().trim());
                                              sch_db.setTitle(title.getText().toString().trim());
                                              sch_db.setNotes(notes.getText().toString().trim());
                                              SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                                              ParsePosition parsePosition = new ParsePosition(0);
                                              sch_db.setStart(dateFormat.parse(fullSD,parsePosition));
                                              //catch (ParseException e){e.printStackTrace();}

                                              ParsePosition parsePosition1=new ParsePosition(1);
                                              sch_db.setEnd(dateFormat.parse(fullED,parsePosition1));
                                              //catch (ParseException e){e.printStackTrace();}

                                          }
                                      }, new Realm.Transaction.OnSuccess() {
                                          @Override
                                          public void onSuccess() {
                                              Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();

                                          }
                                      }, new Realm.Transaction.OnError() {
                                          @Override
                                          public void onError(Throwable error) {
                                              Toast.makeText(getApplicationContext(),"Failed to save",Toast.LENGTH_SHORT).show();
                                          }
                                      }

        );


    }

    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
