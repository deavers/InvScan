package com.example.invscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.net.InetAddress;

public class SplashScreen extends AppCompatActivity {

    ConnectionDetector cd;
    Double x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        x = (Math.random()*((3000-1000)+1))+1000;
        cd = new ConnectionDetector(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (cd.isConnected()) {
                    Toast.makeText(SplashScreen.this,"Internet Connected",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(SplashScreen.this,"Internet Not Connected",Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finishAndRemoveTask();
                        }
                    }, 2000);
                }
            }
        }, x.intValue());

    }

}