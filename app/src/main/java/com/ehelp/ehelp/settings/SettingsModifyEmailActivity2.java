package com.ehelp.ehelp.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;

/**
 * Created by UWTH on 2015/11/9.
 */
public class SettingsModifyEmailActivity2 extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private Button toolbar_icon;

    private EditText et_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_modifyemail2);
        init();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.modifyemail));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsModifyEmailActivity2.this, SettingsModifyEmailActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // 设置右边按钮
        toolbar_icon = (Button) findViewById(R.id.toolbar_icon);
        toolbar_icon.setBackgroundResource(R.drawable.confirmicon_selector);
        toolbar_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = et_code.getText().toString().trim();
                if (!judgeEmailCode(code)) {
                    Toast.makeText(SettingsModifyEmailActivity2.this, "验证码不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(SettingsModifyEmailActivity2.this, "修改绑定邮箱成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingsModifyEmailActivity2.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findView() {
        et_code = (EditText) findViewById(R.id.et_code);
    }

    private void init() {
        initToolbar();
        findView();
    }

    public boolean judgeEmailCode(String code) {
        return true;
    }
}
