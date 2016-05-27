package com.ehelp.ehelp.square;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.userhelp.UserhelpActivity;

/**
 * Created by UWTH on 2015/12/1.
 */
public class HelpEventFinishActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;

    private TextView tv_text;
    private String name = "帮客";

    private Button btn_goevaluate;

    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpeventfinish);

        init();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("救助事件已结束");
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        initToolbar();
        tv_text = (TextView) findViewById(R.id.tv_text);

        btn_goevaluate = (Button) findViewById(R.id.btn_goevaluate);

        btn_goevaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpEventFinishActivity.this, EvaluateListActivity2.class);
                startActivity(intent);
                finish();
            }
        });

        if (getIntent().getIntExtra("cancel", -1) == 1) {
            tv_text.setText("您正在响应的救助事件已由发起者“"
                    + name
                    +"”取消");
            btn_goevaluate.setVisibility(View.GONE);
        } else {
            tv_text.setText("您正在响应的救助事件已由发起者“"
                    + name
                    + "”结束，感谢您的热心！请您对求助者进行评价！");
        }
    }
}
