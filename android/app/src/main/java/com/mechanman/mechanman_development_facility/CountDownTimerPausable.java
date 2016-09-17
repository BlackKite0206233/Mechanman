package com.mechanman.mechanman_development_facility;

import android.os.CountDownTimer;
import android.widget.Toast;

/**
 * Created by Yeh on 2016/9/12.
 */
public abstract class CountDownTimerPausable {
    long millisInFuture;
    long countDownInterval;
    long millisRemaining;

    CountDownTimer countDownTimer = null;

    boolean isPaused = true;

    public CountDownTimerPausable() {
        super();

    }

    public void setCountDownTime(long millisInFuture, long countDownInterval) {
        this.millisInFuture = millisInFuture;
        this.countDownInterval = countDownInterval;
        this.millisRemaining = this.millisInFuture;
    }

    private void createCountDownTimer() {
        countDownTimer = new CountDownTimer(millisRemaining, countDownInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                millisRemaining = millisUntilFinished;
                CountDownTimerPausable.this.onTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                CountDownTimerPausable.this.onFinish();
            }
        };
    }

    public abstract void onTick(long millisUntilFinished);

    public abstract void onFinish();

    public void cancel() {
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
        this.millisRemaining = 0;
    }

    public synchronized final CountDownTimerPausable start() {
        if(this.isPaused) {
            createCountDownTimer();
            countDownTimer.start();
            this.isPaused = false;
        }
        return this;
    }

    public void pause(){
        if(!this.isPaused) {
            countDownTimer.cancel();
        }
        this.isPaused = true;
    }

    /*public boolean isPaused() {
        return isPaused;
    }*/
}
