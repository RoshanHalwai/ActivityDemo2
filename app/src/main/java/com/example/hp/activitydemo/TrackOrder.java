package com.example.hp.activitydemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.daimajia.numberprogressbar.OnProgressBarListener;

import java.util.Timer;
import java.util.TimerTask;

public class TrackOrder extends AppCompatActivity implements OnProgressBarListener {
    ImageView checked1, checked2, checked3, checked4;
    private Timer timer;
    private NumberProgressBar bnp1, bnp2, bnp3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        checked1 = (ImageView) findViewById(R.id.check1);
        checked2 = (ImageView) findViewById(R.id.check2);
        checked3 = (ImageView) findViewById(R.id.check3);
        checked4 = (ImageView) findViewById(R.id.check4);
        bnp1 = (NumberProgressBar) findViewById(R.id.numberbar1);
        bnp2 = (NumberProgressBar) findViewById(R.id.numberbar2);
        bnp3 = (NumberProgressBar) findViewById(R.id.numberbar3);
        bnp1.setOnProgressBarListener(this);
        bnp2.setOnProgressBarListener(this);
        bnp3.setOnProgressBarListener(this);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bnp1.incrementProgressBy(1);
                    }
                });
            }
        }, 1000, 100);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checked2.setVisibility(View.VISIBLE);
                        bnp2.incrementProgressBy(1);
                    }
                });
            }
        }, 10 * 1000, 100);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checked3.setVisibility(View.VISIBLE);
                        bnp3.incrementProgressBy(1);
                    }
                });
            }
        }, 20 * 1000, 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onProgressChange(int current, int max) {
        if (current == max) {
            if (bnp3.getProgress() == 100) {
                checked4.setVisibility(View.VISIBLE);
                timer.cancel();
            }
            /*Toast.makeText(getApplicationContext(), getString(R.string.finish), Toast.LENGTH_SHORT).show();
            checked2.setVisibility(View.VISIBLE);
            timer.cancel();*/
            //bnp.setProgress(0);
        }
    }
}
