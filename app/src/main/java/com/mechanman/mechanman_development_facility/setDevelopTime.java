package com.mechanman.mechanman_development_facility;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class setDevelopTime extends AppCompatActivity {

    private Spinner flim;
    private Spinner developer;
    private Spinner push;

    private Button search;

    private EditText totalTime;
    private EditText stopTime;
    private EditText rollingTime;

    private Spinner rollingSpeed;

    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_develop_time);

        init();

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(setDevelopTime.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        flim = (Spinner)findViewById(R.id.film);
        developer = (Spinner)findViewById(R.id.developer);
        push = (Spinner)findViewById(R.id.push);

        search = (Button)findViewById(R.id.search);

        totalTime = (EditText)findViewById(R.id.totalTime);
        stopTime = (EditText)findViewById(R.id.stopTime);
        rollingTime = (EditText)findViewById(R.id.rollingTime);

        rollingSpeed = (Spinner)findViewById(R.id.rollingSpeed);

        back = (Button)findViewById(R.id.back);
    }
}
