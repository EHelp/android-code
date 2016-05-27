package com.ehelp.ehelp.mycontact;

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
 * Created by UWTH on 2015/11/14.
 */
public class AddUrgencyContactActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addurgencycontact);
        init();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.addurgencycontact));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddUrgencyContactActivity.this, MyContactActivity.class);
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
            case R.id.layout_addbyehelpuser: {
                Intent intent = new Intent(this, UrgencySearchEhelpUserActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.layout_addbyphone: {
                Intent intent = new Intent(this, AddUrgencyContactByPhoneActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
