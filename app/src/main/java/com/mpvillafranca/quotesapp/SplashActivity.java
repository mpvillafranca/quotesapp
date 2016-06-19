package com.mpvillafranca.quotesapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView txt_appname = (TextView) findViewById(R.id.txt_appname);

        // Animación desde alpha = 0 hasta alpha = 1
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
        fadeIn.setDuration(500);

        // Tipo de letra
        Typeface font = Typeface.createFromAsset(getResources().getAssets(), "fonts/molten.ttf");
        if (txt_appname != null) {
            txt_appname.setTypeface(font);
        }

        // Forzamos que la Splash sea vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Definimos la tarea para el Timer (comportamiento)
        TimerTask task = new TimerTask(){
            @Override
            public void run(){
                // Intent para ejecutar la MainActivity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                finish(); // Finaliza el splash
            }
        };

        // Timer
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }
}
