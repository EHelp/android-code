package com.ehelp.ehelp.userhelp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.main.MainActivity;

/**
 * Created by UWTH on 2015/10/23.
 */
public class UserhelpActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhelp);
        init();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.helpandfeedback));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserhelpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        initToolbar();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_freshmen: {
                Intent intent = new Intent(this, UserhelpFreshmenActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.layout_commonque: {
                Intent intent = new Intent(this, UserhelpCommonQueActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.layout_feedback: {
                Intent intent = new Intent(this, UserhelpFeedbackActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.layout_aboutus: {
                Intent intent = new Intent(this, UserhelpAboutusActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.layout_basicfuninstru: {
                Intent intent = new Intent(this, UserhelpBasicfunActivity.class);
                startActivity(intent);
                break;
            }
        }
    }


}
