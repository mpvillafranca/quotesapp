package com.mpvillafranca.quotesapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start = (Button) findViewById(R.id.new_quote);

        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent launchapp = new Intent(MainActivity.this, QuoteActivity.class);
                startActivity(launchapp);
            }
        });
    }
}
