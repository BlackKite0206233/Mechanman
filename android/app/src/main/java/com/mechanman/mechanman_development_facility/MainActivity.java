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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    BluetoothAdapter bluetoothAdapter = null;
    BluetoothDevice device = null;
    BluetoothSocket socket = null;

    //static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

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
                if(developTotalTime != 0) {
                    ArrayList<Integer> List = new ArrayList<Integer>();
                    List.add(developTotalTime);
                    List.add(developStopTime);
                    List.add(developRollingTime);
                    List.add(developRollingSpeed);
                    assignWork(development, "顯影", "暫停", 2, List);
                    List.clear();
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
                isBtConnect = true;
            }
            progress.dismiss();
        }

    }


}
