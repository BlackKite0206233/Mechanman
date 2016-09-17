package com.mechanman.mechanman_development_facility;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import com.mechanman.mechanman_development_facility.CountDownTimerPausable;

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

    private int rememberStatus = 0;

    protected int prewettingTime;
    protected int developTotalTime;
    protected int developStopTime;
    protected int developRollingTime;
    protected int developRollingSpeed;
    protected int stopBathTime;
    protected int fixerTime;

    protected TextView timeCount;

    CountDownTimerPausable countDownTimerPausable;
    NumberFormat numberFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        prewetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ArrayList<Integer> List = new ArrayList<Integer>();
                if(!"".equals(prewettingTXT.getText().toString())) {
                    prewettingTime = Integer.parseInt(prewettingTXT.getText().toString());
                    List.add(prewettingTime);
                    assignWork(prewetting, "預濕", "暫停", 1, List);
                    List.clear();
                }
            }
        });

        development.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ArrayList<Integer> List = new ArrayList<Integer>();
                assignWork(development, "顯影", "暫停", 2, List);
                List.clear();
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
                if(!"".equals(stopBathTXT.getText().toString())) {
                    ArrayList<Integer> List = new ArrayList<Integer>();
                    stopBathTime = Integer.parseInt(stopBathTXT.getText().toString());
                    List.add(stopBathTime);
                    assignWork(stopBath, "急制", "暫停", 3, List);
                    List.clear();
                }
            }
        });

        fixer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!"".equals(fixerTXT.getText().toString())) {
                    ArrayList<Integer> List = new ArrayList<Integer>();
                    fixerTime = Integer.parseInt(fixerTXT.getText().toString());
                    List.add(fixerTime);
                    assignWork(fixer, "定影", "暫停", 4, List);
                    List.clear();
                }
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

        timeCount = (TextView)findViewById(R.id.timeCount);
        String string = "000";
        timeCount.setText(string);

    }

    private void assignWork(Button button, String originalString, String changeString, int status, ArrayList<Integer> list) {
        numberFormat = new DecimalFormat("000");

        if(rememberStatus == 0 || rememberStatus != status) {
            rememberStatus = status;
            countDownTimerPausable = new CountDownTimerPausable() {
                @Override
                public void onTick(long millisUntilFinished) {
                    String string = numberFormat.format(millisUntilFinished / 1000);
                    timeCount.setText(string);
                    if(millisUntilFinished / 1000 <= 10) {
                        timeCount.setTextColor(Color.RED);
                    } else {
                        timeCount.setTextColor(Color.BLACK);
                    }
                }

                @Override
                public void onFinish() {
                    countDownTimerPausable.millisInFuture = 0;
                    String string = numberFormat.format(0);
                    timeCount.setText(string);
                }
            };
        }


        if(button.getText().toString().equals(originalString)) {
            if(countDownTimerPausable.millisInFuture != list.get(0) * 1000) {
                countDownTimerPausable.setCountDownTime(list.get(0) * 1000, 1000);
            }
            countDownTimerPausable.start();

            /*
            * code
            * push status and list to arduino with bluetooth
            * */

            button.setText(changeString);
        } else if(button.getText().toString().equals(changeString)) {

            countDownTimerPausable.pause();
            /*
            * code
            * push pause to arduino with bluetooth
            * */

            button.setText(originalString);
        }
    }


}
