package com.mechanman.mechanman_development_facility;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

    private String Speed = "1";

    private String[] speed = {"1", "2", "3", "4", "5"};
    ArrayAdapter<String> speedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_develop_time);

        init();

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("developTotalTime", Integer.parseInt(totalTime.getText().toString()));
                bundle.putInt("developStopTime", Integer.parseInt(stopTime.getText().toString()));
                bundle.putInt("developRollingTime", Integer.parseInt(rollingTime.getText().toString()));
                bundle.putInt("developRollingSpeed", Integer.parseInt(Speed));
                intent.putExtras(bundle);
                setDevelopTime.this.setResult(RESULT_OK, intent);
                finish();
            }
        });

        rollingSpeed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Speed = speed[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        speedList = new ArrayAdapter<String>(setDevelopTime.this, android.R.layout.simple_spinner_item, speed);
        rollingSpeed.setAdapter(speedList);
    }
}
