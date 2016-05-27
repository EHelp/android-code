package com.ehelp.ehelp.account;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by UWTH on 2015/11/7.
 */
public class FindPwActivity2 extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private EditText et_newpw;
    private EditText et_confirmpw;
    private MainResponse mainResponse = new MainResponse();
    private SharedPreferences sharedPref;

    class MainResponse implements IResponse {
        @Override
        public void onResponse(Object content) {
            if (content != null) {
                String str = ((BackPack) content).getBody();
                try{
                    JSONObject jsonObject = new JSONObject(str);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    String status = jsonObject.getString("status");
                    editor.putString("status_for_resetpwd", status);
                    editor.commit();
                    handler.sendEmptyMessage(-1);
                }catch (Exception e){e.printStackTrace();}
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpw2);
        init();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.resetpw));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindPwActivity2.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findView() {
        et_newpw = (EditText) findViewById(R.id.et_newpw);
        et_confirmpw = (EditText) findViewById(R.id.et_confirmpw);
    }

    private void init() {
        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        initToolbar();
        findView();
    }

    private Handler handler = new Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                if (sharedPref.getString("status_for_resetpwd", "").equals("200")) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("status_for_resetpwd", "500");
                    editor.commit();
                    Intent intent = new Intent(FindPwActivity2.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(FindPwActivity2.this, "重置密码成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FindPwActivity2.this, "重置密码失败，请稍候再试", Toast.LENGTH_SHORT).show();
                }
            }
            super.handleMessage(msg);
        }
    };

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_finish: {
                String pw = et_newpw.getText().toString();
                String confirm_pw = et_confirmpw.getText().toString();
                if (pw.equals("")) {
                    Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pw.length() < 6) {
                    Toast.makeText(this, "密码过短", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pw.length() > 15) {
                    Toast.makeText(this, "密码过长", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pw.equals(confirm_pw)) {
                    Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String urlString = Config.HOST;
                        String location = "/android/resetPassword";
                        urlString += location;
                        JSONObject jo = new JSONObject();
                        try {
                            jo.put("phone", sharedPref.getString("phone", ""));
                            jo.put("temp_id", sharedPref.getString("temp_id", ""));
                            jo.put("password", et_newpw.getText());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        HttpHelper httphelper = new HttpHelper(urlString, jo, mainResponse, HttpHelper.HTTP_POST, null);
                        httphelper.execute();
                    }
                }).start();
            }
        }
    }
}
