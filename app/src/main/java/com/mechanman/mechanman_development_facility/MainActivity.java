package com.mechanman.mechanman_development_facility;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button prewetting;
    private Button development;
    private Button setting;
    private Button stopBath;
    private Button fixer;
    private Button exit;

    private EditText prewettingTXT;
    private EditText stopBathTXT;
    private EditText fixerTXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        prewetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        development.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        setting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, setDevelopTime.class);
                startActivity(intent);
            }
        });

        stopBath.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        fixer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        exit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        prewetting = (Button)findViewById(R.id.prewetting);
        development = (Button)findViewById(R.id.development);
        setting = (Button)findViewById(R.id.setting);
        stopBath = (Button)findViewById(R.id.stopBath);
        fixer = (Button)findViewById(R.id.Fixer);
        exit = (Button)findViewById(R.id.EXIT);

        prewettingTXT = (EditText)findViewById(R.id.prewettingTXT);
        stopBathTXT = (EditText)findViewById(R.id.stopBathTXT);
        fixerTXT = (EditText)findViewById(R.id.fixerTXT);
    }
}
