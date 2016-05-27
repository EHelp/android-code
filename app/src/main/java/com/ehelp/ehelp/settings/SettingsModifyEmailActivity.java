package com.ehelp.ehelp.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.message.MyMessageActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by UWTH on 2015/10/23.
 */
public class SettingsModifyEmailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private Button btn_next;

    private TextView tv_email;
    private EditText et_newemail;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_modifyemail);
        sharedPreferences = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        init();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmail = et_newemail.getText().toString().trim();
                if (!judgeEmailNums(newEmail)) {
                    Toast.makeText(SettingsModifyEmailActivity.this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                ModifyEmail();
            }
        });
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
                Intent intent = new Intent(SettingsModifyEmailActivity.this, SettingsSecurityActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findView() {
        et_newemail = (EditText) findViewById(R.id.et_newemail);
        btn_next = (Button) findViewById(R.id.btn_next);
        tv_email = (TextView)findViewById(R.id.tv_email);

        tv_email.setText(getIntent().getStringExtra("email"));
    }

    private void init() {
        initToolbar();
        findView();
    }

    public static boolean judgeEmailNums(String email) {
        String malReg = "\\w+@\\w+\\.com";
        return email.matches(malReg);
    }

    private void ModifyEmail() {

        String urlString = Config.HOST;
        String location = "/android/user/modify_email";

        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            jo.put("id", sharedPreferences.getString("id", "none"));
            jo.put("email", et_newemail.getText().toString().trim());


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
                        Toast.makeText(SettingsModifyEmailActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        if (sharedPreferences.getString("status", "").equals("200")) {
                            Toast.makeText(SettingsModifyEmailActivity.this, "修改绑定邮箱成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SettingsModifyEmailActivity.this, SettingsActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SettingsModifyEmailActivity.this, "修改邮箱失败", Toast.LENGTH_SHORT).show();
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
