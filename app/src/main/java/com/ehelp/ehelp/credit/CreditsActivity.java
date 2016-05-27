package com.ehelp.ehelp.credit;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.userhelp.UserhelpFreshmenActivity;
import com.ehelp.ehelp.util.Reputation2level;

/**
 * Created by UWTH on 2015/11/20.
 */
public class CreditsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;

    private TextView tv_detail;
    private TextView tv_reputation;
    private ImageView iv_creditlevel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycredits);
        init();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.mycredits));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreditsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findView() {
        tv_reputation = (TextView)findViewById(R.id.tv_creditvalue);
        tv_detail = (TextView) findViewById(R.id.tv_detail);
        iv_creditlevel = (ImageView) findViewById(R.id.iv_creditlevel);
    }

    private void init() {
        findView();
        initToolbar();
        tv_detail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_reputation.setText(getIntent().getStringExtra("credit"));
        iv_creditlevel.setImageResource(Reputation2level.getLevel(Double.valueOf(getIntent().getStringExtra("credit"))));

        tv_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreditsActivity.this, UserhelpFreshmenActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("from", "credits");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
