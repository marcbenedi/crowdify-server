package com.hackupcteam.crowdify;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MosaicActivity extends AppCompatActivity {

    private ArrayList<MyBehavior> myBehaviorArrayList = new ArrayList<>();
    private long startTime;
    private int currentBehavior;
    private Vibrator v;
    private boolean CVibrate;
    private boolean CFlash;
    private boolean firstTick;
    private long currentDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        startTime = System.currentTimeMillis();
        setContentView(R.layout.activity_mosaic);
        parseJSON(getIntent().getStringExtra("behavior"));

        getWindow().getDecorView().setSystemUiVisibility(
                 View.SYSTEM_UI_FLAG_IMMERSIVE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE);


        firstTick = true;

        currentBehavior = 0;
        executeBehavior(currentBehavior);

    }

    private void behave(MyBehavior myBehavior) {
        View screen = (View) findViewById(R.id.screen);
        screen.setBackgroundColor(myBehavior.getColor());

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        CVibrate =  myBehavior.getVibrate().equals("yes");
        CFlash = myBehavior.getFlash().equals("yes");
        currentDuration = myBehavior.getDurationInMillis();

        MyCountDownTimer myCountDownTimer = new MyCountDownTimer(myBehavior.getDurationInMillis(),10);
        myCountDownTimer.start();

    }

    private void executeBehavior(int i) {
        behave(myBehaviorArrayList.get(i%(myBehaviorArrayList.size())));
    }

    public void parseJSON(String JSON){
        try {
            JSONObject object = new JSONObject(JSON);
            JSONArray array = object.getJSONArray("behavior");
            for (int i=0; i < array.length(); ++i) {
                JSONObject instantBehavior = array.getJSONObject(i);
                MyBehavior myBehavior = new MyBehavior();
                myBehavior.setColor(Color.parseColor(instantBehavior.getString("color")));
                myBehavior.setVibrate(instantBehavior.getString("vibrate"));
                myBehavior.setFlash(instantBehavior.getString("flash"));
                myBehavior.setDurationInMillis(Long.parseLong(instantBehavior.getString("duration")));
                //myBehavior.setShake(instantBehavior.getString("shake"));
                myBehaviorArrayList.add(myBehavior);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long interval) {
            super(millisInFuture, interval);
        }

        @Override
        public void onFinish() {
            firstTick = true;
            if (CVibrate) v.cancel();
            ++currentBehavior;
            executeBehavior(currentBehavior);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (firstTick && CVibrate) {
                firstTick = false;
                v.vibrate(currentDuration);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        v.cancel();
    }
}
