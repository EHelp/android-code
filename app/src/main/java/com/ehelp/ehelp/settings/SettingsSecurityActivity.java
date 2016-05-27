package com.ehelp.ehelp.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.util.NullDealer;
import com.ehelp.ehelp.util.Reputation2level;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by UWTH on 2015/10/23.
 */
public class SettingsSecurityActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private SharedPreferences sharedPreferences;
    private TextView tv_phone;
    private TextView tv_email;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_security);
        sharedPreferences = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        init();

        tv_phone = (TextView)findViewById(R.id.tv_phone);
        tv_email = (TextView)findViewById(R.id.tv_email);

        getInfo();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.accountandsecurity));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsSecurityActivity.this, SettingsActivity.class);
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
            case R.id.layout_modifypw: {
                Intent intent = new Intent(this, SettingsModifyPwActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.layout_modifyphone: {
                Intent intent = new Intent(this, SettingsModifyPhoneActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.layout_modifyemail: {
                Intent intent = new Intent(this, SettingsModifyEmailActivity.class);
                intent.putExtra("email", tv_email.getText().toString());
                startActivity(intent);
                break;
            }
        }
    }

    private void getInfo() {
        String urlString = Config.HOST;
        String location = "/android/user/get_information";

        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            jo.put("id", sharedPreferences.getString("id", "none"));



        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
            @Override
            public void onResponse(Object content) {
                if (content != null) {
                    String str = ((BackPack) content).getBody();
                    if (str == null) {
                        Toast.makeText(SettingsSecurityActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        if (sharedPreferences.getString("status", "").equals("200")) {
                            tv_email.setText(NullDealer.DealNull(jsonObject.getString("email")));
                            tv_phone.setText(jsonObject.getString("phone"));
                        } else {
                            Toast.makeText(SettingsSecurityActivity.this, "获取个人信息失败", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();
    }
}
