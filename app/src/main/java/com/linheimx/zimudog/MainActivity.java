package com.linheimx.zimudog;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;

import com.linheimx.zimudog.vp.custview.WaveView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.wave)
    WaveView wave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        wave.setDuration(7000);
        wave.setStyle(Paint.Style.FILL);
        wave.setColor(Color.BLUE);
        wave.setInterpolator(new LinearOutSlowInInterpolator());
        wave.start();
    }
}
