package com.example.nlp_sch;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nlp_sch.model.Sch_DB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class DisplaySch extends AppCompatActivity {
    private TextView datepick;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private ListView listView;
    private String date2,dat1;
    Realm realm;
    ArrayList<Sch_DB> displaylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_sch);
        datepick=findViewById(R.id.dateView);
        listView=findViewById(R.id.good);
        displaylist=new ArrayList<>();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        int currentOrientation = this.getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else{
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        realm = Realm.getDefaultInstance();
        datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog=new DatePickerDialog(DisplaySch.this,
                        R.style.Theme_AppCompat_Dialog_MinWidth,dateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(51, 32, 42)));
                dialog.show();
            }
        });
        dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String d1="/0"+dayOfMonth;
                if(dayOfMonth>9){
                    d1= "/"+dayOfMonth;
                }
                datepick.setText(year+"/"+(month+1)+"/"+dayOfMonth);
                date2=year+"/0"+(month+1)+d1;
                displaylist.clear();
                getData();
            }

        };

    }

    private void getData() {
        RealmResults<Sch_DB> datas=realm.where(Sch_DB.class).findAll();
        SimpleDateFormat g=new SimpleDateFormat("yyyy/MM/dd");

        for(Sch_DB data:datas){
            try{
                dat1=g.format(data.getStart());
               // a=dat1;
                //b=date2;

                if(date2.equals(dat1)){
                    displaylist.add(data);
                }
            }
            catch(NullPointerException e){
                e.printStackTrace();
            }
        }
        showStuff();
    }
    private void showStuff() {
        arrayAdapter myCustome=new arrayAdapter(DisplaySch.this,displaylist);
        listView.setAdapter(myCustome);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(DisplaySch.this);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + displaylist.get(position).getTopic());
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        realm.beginTransaction();
                        displaylist.get(position).deleteFromRealm();
                        displaylist.remove(position);
                        realm.commitTransaction();

                        myCustome.notifyDataSetChanged();


                    }});
                adb.show();
            }
        });
    }
}
