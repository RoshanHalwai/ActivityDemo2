package com.example.hp.activitydemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class ReferAndEarn extends AppCompatActivity {

    Button shareCode, referralCodeBtn;
    String referralCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_and_earn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        shareCode = (Button) findViewById(R.id.shareCode);
        referralCodeBtn = (Button) findViewById(R.id.referralCode);
        referralCode = referralCodeBtn.getText().toString();

        shareCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");

                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "https://freshfoods.com/");

                String shareMessage = "Sign up NOW on FreshFoods using the link https://freshfoods.com or " +
                        "using my referral code " + referralCode + " and get " +
                        "Rs. 100 OFF on your next order. Hurry up. . ! ! !";

                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);

                startActivity(Intent.createChooser(shareIntent, "Share Using"));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.terms_and_conditions, menu);
        return true;

    }
}
