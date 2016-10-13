package com.mechanman.mechanman_development_facility;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import android.os.AsyncTask;
import java.io.IOException;
import java.util.UUID;

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

    private boolean isBtConnect = false;

    private ProgressDialog progress;

    protected int prewettingTime;
    protected int developTotalTime = 0;
    protected int developStopTime = 0;
    protected int developRollingTime = 0;
    protected int developRollingSpeed = 0;
    protected int stopBathTime;
    protected int fixerTime;

    protected TextView timeCount;

    NumberFormat numberFormat;
    CountDownTimerPausable countDownTimerPausable;


    Intent intent = new Intent();

    String address;
    String checkCode = "$2a$04$NcywggFZq1ktjuQ7n73l4.Q4KLVp6mGC8kNr7bALqBj2DJXNScFi2";
    String ctrlCode;

    boolean isPause = false;

    BluetoothAdapter bluetoothAdapter = null;
    BluetoothDevice device = null;
    BluetoothSocket socket = null;

    InputStream socketIn = null;
    OutputStream socketOut = null;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                developTotalTime = data.getExtras().getInt("developTotalTime");
                developStopTime = data.getExtras().getInt("developStopTime");
                developRollingTime = data.getExtras().getInt("developRollingTime");
                developRollingSpeed = data.getExtras().getInt("developRollingSpeed");
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent BTIntent = getIntent();
        address = BTIntent.getExtras().getString("device_address");

        new ConnectBT().execute();





        init();

        prewetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!"".equals(prewettingTXT.getText().toString())) {
                    prewettingTime = Integer.parseInt(prewettingTXT.getText().toString());
                    ctrlCode = "1:1:" + prewettingTXT.getText().toString();
                    assignWork(prewetting, "預濕", "暫停", 1, prewettingTime);
                }
            }
        });

        development.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(developTotalTime != 0) {
                    ctrlCode = "1:2:" + Integer.toString(developTotalTime) + ":"
                                    + Integer.toString(developStopTime) + ":"
                                    + Integer.toString(developRollingTime) + ":"
                                    + Integer.toString(developRollingSpeed);
                    assignWork(development, "顯影", "暫停", 2, developTotalTime);
                }
            }
        });

        setting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                intent.setClass(MainActivity.this, setDevelopTime.class);
                startActivityForResult(intent, 0);
            }
        });

        stopBath.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!"".equals(stopBathTXT.getText().toString())) {
                    stopBathTime = Integer.parseInt(stopBathTXT.getText().toString());
                    ctrlCode = "1:3:" + stopBathTXT.getText().toString();
                    assignWork(stopBath, "急制", "暫停", 3, stopBathTime);
                }
            }
        });

        fixer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!"".equals(fixerTXT.getText().toString())) {
                    fixerTime = Integer.parseInt(fixerTXT.getText().toString());
                    ctrlCode = "1:4:" + fixerTXT.getText().toString();
                    assignWork(fixer, "定影", "暫停", 4, fixerTime);
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                    }
                }

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

    private void assignWork(Button button, String originalString, String changeString, int status, int countTime) {
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
            if(countDownTimerPausable.millisInFuture != countTime * 1000) {
                countDownTimerPausable.setCountDownTime(countTime * 1000, 1000);
            }
            countDownTimerPausable.start();

            try {
                if(isPause) {
                    socketOut.write("2".getBytes());
                } else {
                    socketOut.write(ctrlCode.getBytes());
                }
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }

            button.setText(changeString);
        } else if(button.getText().toString().equals(changeString)) {

            countDownTimerPausable.pause();

            try {
                socketOut.write("0:".getBytes());
                isPause = true;
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }

            button.setText(originalString);
        }
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean connectSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(MainActivity.this, "Connecting...", "Please wait!!!");
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if(socket == null || !isBtConnect) {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    device = bluetoothAdapter.getRemoteDevice(address);
                    UUID muuid = device.getUuids()[0].getUuid();
                    socket = device.createInsecureRfcommSocketToServiceRecord(muuid);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    socket.connect();
                    socketIn = socket.getInputStream();
                    socketOut = socket.getOutputStream();
                }
            } catch (IOException e) {
                connectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(!connectSuccess) {
                Toast.makeText(getApplicationContext(), "Connection Failed. Is it a SPP Bluetooth? Try again.", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Connected.", Toast.LENGTH_LONG).show();
                try {
                    socketOut.write(checkCode.getBytes());
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, StartActivity.class);
                    startActivity(intent);
                    finish();
                }


                isBtConnect = true;
            }
            progress.dismiss();
        }

    }


}
