package com.ehelp.ehelp.account;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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
import com.ehelp.ehelp.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by UWTH on 2015/10/29.
 */
public class FindPwActivity extends AppCompatActivity {
    public  static String ID = "a257119e9293";
    public static String SECRET_KEY = "bd29f4a6506f42b98fe4486a8bc6e6dd";
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private RadioButton rb_sendcode;
    private EditText et_phone;
    private EditText et_code;
    private MainResponse mainResponse = new MainResponse();
    private VerifyResponse verifyResponse = new VerifyResponse();
    private SharedPreferences sharedPref;
    private EventHandler eh;

    int i = 60;

    class MainResponse implements IResponse {
        @Override
        public void onResponse(Object content) {
            if (content != null) {
                String str = ((BackPack) content).getBody();
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        editor.putString("temp_id", jsonObject.getString("temp_id"));
                    }
                    editor.putString("status_for_sms", status);
                    editor.commit();
                    handler.sendEmptyMessage(-1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class VerifyResponse implements IResponse {
        @Override
        public void onResponse(Object content) {
            if (content != null) {
                String str = ((BackPack) content).getBody();
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("status_for_verify", jsonObject.getString("status"));
                    editor.commit();
                    handler.sendEmptyMessage(-2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpw);
        init();

        SMSSDK.initSDK(this, FindPwActivity.ID, FindPwActivity.SECRET_KEY);
        eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        Verify();
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
                            Toast.makeText(FindPwActivity.this, des, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        //do something
                    }
                }
            }
        };
        SMSSDK.registerEventHandler(eh);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.findpw));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindPwActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findView() {
        rb_sendcode = (RadioButton) findViewById(R.id.rb_sendcode);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
    }

    private void init() {
        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);
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

                String urlString = Config.HOST;
                String location = "/android/getMes";
                urlString += location;
                JSONObject jo = new JSONObject();
                try {
                    jo.put("phone", et_phone.getText().toString());
                    jo.put("type", "1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                HttpHelper httphelper = new HttpHelper(urlString, jo, mainResponse, HttpHelper.HTTP_POST, null);
                httphelper.execute();

//                handler.sendEmptyMessage(-1);
                SMSSDK.getVerificationCode("86", et_phone.getText().toString());
                break;
            }
            case R.id.btn_next: {
                String phone = et_phone.getText().toString().trim();
                if (phone.equals("")) {
                    Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = et_code.getText().toString().trim();
                if (code.equals("")) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                SMSSDK.submitVerificationCode("86", et_phone.getText().toString(), et_code.getText().toString());

                break;
            }
        }
    }

    Handler handler = new Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                rb_sendcode.setChecked(true);
                rb_sendcode.setClickable(false);
                rb_sendcode.setBackgroundResource(R.drawable.btn_bg13);
                rb_sendcode.setText("重新发送(" + i + ")");
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
                        handler.sendEmptyMessage(-7);
                    }
                }).start();
            } else if (msg.what == -2) {
                String status = sharedPref.getString("status_for_verify", "");
                if (status.equals("200")) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("status_for_verify", "500");
                    editor.commit();
                    Toast.makeText(FindPwActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FindPwActivity.this, FindPwActivity2.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(FindPwActivity.this, "验证失败，请重试", Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == -9) {
                rb_sendcode.setText("重新发送(" + i + ")");
            } else if (msg.what == -7) {
                rb_sendcode.setBackgroundResource(R.drawable.btn_selector3);
                rb_sendcode.setClickable(true);
                rb_sendcode.setText("发送验证码");
                rb_sendcode.setChecked(false);
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

    private void Verify() {

        String urlString = Config.HOST;
        String location = "/android/verifyMes";
        urlString += location;
        JSONObject jo = new JSONObject();
        try {
            jo.put("sms_code", "1234");
            jo.put("temp_id", sharedPref.getString("temp_id", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, verifyResponse, HttpHelper.HTTP_POST, null);
        httphelper.execute();

    }
}
