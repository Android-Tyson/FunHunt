package com.party.funhunt.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.party.funhunt.MainActivity;
import com.party.funhunt.R;
import com.party.funhunt.utils.Pref;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity extends AppCompatActivity {

    Pref pref;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pref = new Pref(this);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (pref.getPreference("userId").isEmpty()) {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }

            }
        }, 3000);
    }
}
