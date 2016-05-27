package com.ehelp.ehelp.bank;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.settings.SettingsActivity;
import com.ehelp.ehelp.userhelp.UserhelpFreshmenActivity;

import java.util.ArrayList;

/**
 * Created by UWTH on 2015/10/24.
 */
public class BankActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;

    private TextView tv_detail;
    private TextView tv_coinnum;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        init();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.bank));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BankActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findView() {
        tv_detail = (TextView) findViewById(R.id.tv_detail);
        tv_coinnum = (TextView)findViewById(R.id.tv_coinnum);

        tv_coinnum.setText(getIntent().getStringExtra("coin"));
    }

    private void init() {
        findView();
        initToolbar();
        tv_detail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        tv_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BankActivity.this, UserhelpFreshmenActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("from", "coin");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
