package com.ehelp.ehelp.guide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.account.LoginActivity;

/**
 * Created by UWTH on 2015/12/19.
 */
public class WelcomeActivity extends AppCompatActivity {
    public static boolean isStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if (isStart) {
            Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
            startActivity(intent);
            finish();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isStart = true;
                Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
