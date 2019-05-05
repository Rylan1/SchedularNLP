package com.example.nlp_sch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button schButton=findViewById(R.id.button5);
        schButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSch();
            }
        });

    }

    private void openSch() {
        Intent intent=new Intent(this,Scheduling.class);
        startActivity(intent);
    }
}
