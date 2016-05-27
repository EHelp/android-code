package com.ehelp.ehelp.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.account.LoginActivity;
import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.util.ActivityCollector;
import com.ehelp.ehelp.util.MyCustomDialog;
import com.ehelp.ehelp.util.MyCustomDialog5;
import com.tencent.android.tpush.XGPushManager;

/**
 * Created by UWTH on 2015/10/23.
 */
public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private SharedPreferences pref;

    private TextView tv_newversionhint;
    private ImageView iv_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.setting));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        initToolbar();

        tv_newversionhint = (TextView) findViewById(R.id.tv_newversionhint);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        if (isNewestVersion()) {
            tv_newversionhint.setText("已是最新版本");
            iv_right.setVisibility(View.GONE);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_accountandsecurity: {
                Intent intent = new Intent(this, SettingsSecurityActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.layout_logout: {
                //final boolean[] logout_flag = new boolean[1];
                MyCustomDialog5 dialog = new MyCustomDialog5(this, "确认退出登录？",
                        new MyCustomDialog5.OnCustomDialogListener() {
                            @Override
                            public void back(boolean flag) {
                                //logout_flag[0] = flag;
                                if (flag) {
                                    pref = SettingsActivity.this.getSharedPreferences("user_id", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.remove("status");
                                    editor.remove("id");
                                    editor.remove("salt");
                                    editor.commit();
                                    Context context = getApplicationContext();
                                    XGPushManager.registerPush(context, "*");
                                    Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                    ActivityCollector.getInstance().exit();
                                }
                            }
                        });
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
                break;
            }
            case R.id.layout_versionupdate: {
                if (tv_newversionhint.getText().toString().equals("已是最新版本")) {
                    Toast.makeText(this, "已是当前最新版本", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, SettingsNewVersionActivity.class);
                    startActivity(intent);
                }
                break;
            }
            case R.id.layout_privacysetting: {
                Intent intent = new Intent(this, SettingsPrivacyActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    public boolean isNewestVersion() {
        return true;
    }
}
