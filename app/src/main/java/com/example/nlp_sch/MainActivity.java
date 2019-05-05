package com.example.nlp_sch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nlp_sch.model.Sch_DB;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    Realm realm;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button schButton=findViewById(R.id.button5);
        realm=Realm.getDefaultInstance();
        schButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSch();
            }
        });
        getCurrentTopic();
    }

    private void getCurrentTopic() {
        Calendar cal = Calendar.getInstance();
        Date date=cal.getTime();
        RealmResults<Sch_DB> sch_dbs=realm.where(Sch_DB.class).findAll();
        SimpleDateFormat f=new SimpleDateFormat("hh:mm aa");

        for (Sch_DB result:sch_dbs){
            try {
                if(date.before(result.getEnd())&&date.after(result.getStart())){
                    TextView topicView=findViewById(R.id.textView2);
                    TextView startTime=findViewById(R.id.textView11);
                    TextView endtime=findViewById(R.id.textView13);
                    topicView.setText(result.getTopic());
                    startTime.setText("Start Time:"+f.format(result.getStart()));
                    endtime.setText(" End Time:"+f.format(result.getEnd()));
                }
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }

        }
    }

    private void openSch() {
        Intent intent=new Intent(this,Scheduling.class);
        startActivity(intent);
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
