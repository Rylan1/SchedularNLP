package com.example.nlp_sch;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nlp_sch.model.Sch_DB;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Voice_Input extends AppCompatActivity {
    private static final int REQUEST=1;
    private ListView listView;
    Realm realm;
    ArrayList<Sch_DB>displaylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice__input);
        PackageManager manager =getPackageManager();
        listView=findViewById(R.id.good);
        realm=Realm.getDefaultInstance();
        displaylist=new ArrayList<>();
        List<ResolveInfo> listofmatches=manager.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),0);
        if (listofmatches.size()==0){
            Toast.makeText(this,"Sorry voice recognision not supported",Toast.LENGTH_LONG).show();
        }
        ImageButton voIN=findViewById(R.id.imageButton);
        voIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInput();
            }
        });
    }

    private void getInput() {
        Intent listenIntent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Enter Voice Command");
        listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,10);
        try{
            startActivityForResult(listenIntent,REQUEST);
        }
        catch (ActivityNotFoundException tim){
            tim.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST && resultCode==RESULT_OK){
            ArrayList<String>data1=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            TextView t1=findViewById(R.id.textView10);
            t1.setText("");
            for(String l:data1){
                t1.setText(t1.getText()+l+" ");
            }
            processInformation(data1);
            int i=0;
            for(String a:data1){
                Log.w("NLP",a+":"+i);
                i++;
            }

        }

    }

    private void processInformation(ArrayList<String> data1) {
        displaylist.clear();
        String[] words=data1.get(0).split("\\s");
        int i=0;
        for(String h1:words){
            if(h1.equals("today")){
                TextView t1=findViewById(R.id.textView10);
                t1.setText("tomorrow12121");
                rule1();
            }
            if(h1.equals("tomorrow")){
                rule2();
            }
            if(h1.equals("topic")){
                rule3(words[(i+1)]);
            }
            if(h1.equals("title")){
                rule4(words[(i+1)]);
            }
            i++;
        }
    }

    private void rule4(String word) {
        RealmResults<Sch_DB> datas=realm.where(Sch_DB.class).findAll();

        for(Sch_DB data:datas){
            try{


                TextView t1=findViewById(R.id.textView10);

                if(data.getTitle().toUpperCase().equals(word.toUpperCase())){
                    displaylist.add(data);
                    t1.setText(word+" "+data.getTopic());


                }
            }
            catch(NullPointerException e){
                e.printStackTrace();
            }
        }

        showData();

    }

    private void rule3(String word) {
        RealmResults<Sch_DB> datas=realm.where(Sch_DB.class).findAll();

        for(Sch_DB data:datas){
            try{


                TextView t1=findViewById(R.id.textView10);

                if(data.getTopic().toUpperCase().equals(word.toUpperCase())){
                    displaylist.add(data);
                    t1.setText(word+" "+data.getTopic());


                }
            }
            catch(NullPointerException e){
                e.printStackTrace();
            }
        }

        showData();

    }

    private void rule2() {
        RealmResults<Sch_DB> datas=realm.where(Sch_DB.class).findAll();
        SimpleDateFormat g=new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR,1);
        Date date=cal.getTime();

        String date2=g.format(date);


        for(Sch_DB data:datas){
            try{
                String date3=g.format(data.getStart());
                // a=dat1;
                //b=date2;
                TextView t1=findViewById(R.id.textView10);

                if(date2.equals(date3)){
                    displaylist.add(data);
                    t1.setText(date2+" "+date3);


                }
            }
            catch(NullPointerException e){
                e.printStackTrace();
            }
        }

        showData();

    }

    private void rule1() {
        RealmResults<Sch_DB> datas=realm.where(Sch_DB.class).findAll();
        SimpleDateFormat g=new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        Date date=cal.getTime();
        String date2=g.format(date);
        for(Sch_DB data:datas){
            try{
                String date3=g.format(data.getStart());
                // a=dat1;
                //b=date2;
                TextView t1=findViewById(R.id.textView10);
                t1.setText(date2+" "+date3);

                if(date2.equals(date3)){
                    displaylist.add(data);

                }
            }
            catch(NullPointerException e){
                e.printStackTrace();
            }
        }

        showData();
    }

    private void showData() {
        arrayAdapter1 myCustome=new arrayAdapter1(Voice_Input.this,displaylist);
        listView.setAdapter(myCustome);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(Voice_Input.this);
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
