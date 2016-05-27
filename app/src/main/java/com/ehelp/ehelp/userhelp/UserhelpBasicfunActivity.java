package com.ehelp.ehelp.userhelp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ehelp.ehelp.R;

/**
 * Created by UWTH on 2015/11/25.
 */
public class UserhelpBasicfunActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private TextView tv_title1;
    private TextView tv_title2;
    private TextView tv_title3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhelp_basicfun);
        init();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.basicfuninstru));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserhelpBasicfunActivity.this, UserhelpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findView() {
        tv_title1 = (TextView) findViewById(R.id.tv_title1);
        tv_title2 = (TextView) findViewById(R.id.tv_title2);
        tv_title3 = (TextView) findViewById(R.id.tv_title3);
    }

    private void init() {
        findView();
        initToolbar();
        tv_title1.getPaint().setFakeBoldText(true);
        tv_title2.getPaint().setFakeBoldText(true);
        tv_title3.getPaint().setFakeBoldText(true);
    }
}
