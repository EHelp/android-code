package com.ehelp.ehelp.settings;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.account.FindPwActivity;
import com.ehelp.ehelp.account.RegisterActivity;
import com.ehelp.ehelp.account.RegisterActivity2;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;

import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by UWTH on 2015/10/23.
 */
public class SettingsModifyPhoneActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private Button toolbar_icon;

    private RadioButton rb_sendcode;
    private EditText et_phone;
    private EditText et_code;
    private TextView tv_phone;

    private SharedPreferences sharedPreferences;
    private EventHandler eh;
    private String temp_id;
    int i = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_modifyphone);
        sharedPreferences = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        temp_id = "";
        init();

        SMSSDK.initSDK(this, FindPwActivity.ID, FindPwActivity.SECRET_KEY);
        eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        ModifyPhone();
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Log.d("sms", "success");
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {


                    }
                } else {
                    try {
                        Throwable throwable = (Throwable) data;
                        throwable.printStackTrace();
                        JSONObject object = new JSONObject(throwable.getMessage());
                        String des = object.optString("detail");//错误描述
                        int status = object.optInt("status");//错误代码
                        if (status > 0 && !TextUtils.isEmpty(des)) {
                            Toast.makeText(SettingsModifyPhoneActivity.this, des, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        //do something
                    }
                }
            }
        };
        SMSSDK.registerEventHandler(eh);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.modifyphone));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsModifyPhoneActivity.this, SettingsSecurityActivity.class);
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
                String phone = et_phone.getText().toString().trim();
                if (phone.equals("")) {
                    Toast.makeText(SettingsModifyPhoneActivity.this, "请输入新手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = et_code.getText().toString().trim();
                if (code.equals("")) {
                    Toast.makeText(SettingsModifyPhoneActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (temp_id.equals("")) {
                    Toast.makeText(SettingsModifyPhoneActivity.this, "请获取验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                SMSSDK.submitVerificationCode("86", et_phone.getText().toString(), et_code.getText().toString());
            }
        });
    }

    private void findView() {
        rb_sendcode = (RadioButton) findViewById(R.id.rb_sendcode);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        tv_phone = (TextView)findViewById(R.id.tv_phone);

        tv_phone.setText(sharedPreferences.getString("phone", "none"));
    }

    private void init() {
        initToolbar();
        findView();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_sendcode: {
                if (!judgePhoneNums(et_phone.getText().toString())) {
                    rb_sendcode.setChecked(false);
                    return;
                }
                rb_sendcode.setClickable(false);
                rb_sendcode.setBackground(getResources().getDrawable(R.drawable.btn_bg13));
                rb_sendcode.setText("正在发送(" + i + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
                getMes();

                break;
            }
        }

    }

    Handler handler = new Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                rb_sendcode.setText("正在发送(" + i + ")");
            } else if (msg.what == -8) {
                rb_sendcode.setChecked(false);
                rb_sendcode.setText("发送验证码");
                rb_sendcode.setClickable(true);
                rb_sendcode.setBackground(getResources().getDrawable(R.drawable.btn_bg5));
                i = 60;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (RegisterActivity.isMatchLength(phoneNums, 11)
                && RegisterActivity.isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void getMes() {

        String urlString = Config.HOST;
        String location = "/android/getMes";

        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            jo.put("phone", sharedPreferences.getString("phone", "none"));
            jo.put("type", 0);

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
                        Toast.makeText(SettingsModifyPhoneActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        if (sharedPreferences.getString("status", "").equals("200")) {
                            temp_id = jsonObject.getString("temp_id");
                            SMSSDK.getVerificationCode("86", et_phone.getText().toString());
                        } else if (sharedPreferences.getString("status", "").equals("250")){
                            Toast.makeText(SettingsModifyPhoneActivity.this, "短信发送失败", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SettingsModifyPhoneActivity.this, "此号码已注册", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();
    }

    private void ModifyPhone() {

        String urlString = Config.HOST;
        String location = "/android/user/modify_phone";

        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            jo.put("phone", et_phone.getText().toString());
            jo.put("id", sharedPreferences.getString("id", "none"));
            jo.put("temp_id", temp_id);
            jo.put("sms_code", "1234");

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
                        Toast.makeText(SettingsModifyPhoneActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        if (sharedPreferences.getString("status", "").equals("200")) {
                            Toast.makeText(SettingsModifyPhoneActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SettingsModifyPhoneActivity.this, SettingsActivity.class);
                            startActivity(intent);
                        } else if (sharedPreferences.getString("status", "").equals("400")){
                            Toast.makeText(SettingsModifyPhoneActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SettingsModifyPhoneActivity.this, "修改手机号失败", Toast.LENGTH_SHORT).show();
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
