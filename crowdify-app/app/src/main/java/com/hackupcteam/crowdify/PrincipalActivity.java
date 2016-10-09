package com.hackupcteam.crowdify;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class PrincipalActivity extends AppCompatActivity {

    public TextView tvt;
    public String behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        int fila = getIntent().getIntExtra("fila",2);
        int columna = getIntent().getIntExtra("columna",5);

        behavior = "no tiene valor";

        String strings[] = {"http://54.245.32.155:8080/get_instructions/"+fila+"/"+columna};

        GetDataFromServer getDataFromServer = new GetDataFromServer();
        try {
            behavior = getDataFromServer.execute(strings).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Log.v("Principal",behavior);

        TextView tvm = (TextView) findViewById(R.id.textView);
        tvm.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/ProductSans-Regular.ttf"));


        TextView tv = (TextView) findViewById(R.id.titlePrincipal);
        tvt = (TextView) findViewById(R.id.timer);
        tvt.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/ProductSans-Bold.ttf"));
        tv.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/ProductSans-Bold.ttf"));
        MyCountDownTimer timer = new MyCountDownTimer(1000*3,1000);
        timer.start();
    }



    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long interval) {
            super(millisInFuture, interval);
        }

        @Override
        public void onFinish() {
            Intent intent = new Intent(getApplicationContext(),MosaicActivity.class);
            intent.putExtra("behavior",behavior);
            startActivity(intent);
            tvt.setText("Time's up!");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int seconds = (int) ((millisUntilFinished / 1000) % 60);
            int minutes = (int) ((millisUntilFinished / 1000) / 60);
            String sec = seconds < 10 ? "0"+ seconds : ""+seconds;
            String min = minutes < 10 ? "0"+minutes : ""+minutes;
            tvt.setText(min+":"+sec);
        }
    }
}
