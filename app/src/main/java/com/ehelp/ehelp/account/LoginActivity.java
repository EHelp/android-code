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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.guide.GuideActivity;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;



/**
 * Created by UWTH on 2015/10/29.
 */
public class LoginActivity extends AppCompatActivity {
    private EditText et_phone;
    private EditText et_pw;
    private SharedPreferences sharedPref;
    private String phone;
    private Button btn_login;
    private MainResponse mainResponse = new MainResponse();

    class MainResponse implements IResponse {
        @Override
        public void onResponse(Object content) {
            if (content != null) {
                String str = ((BackPack) content).getBody();
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("status", jsonObject.getString("status"));
                    editor.commit();
                    if (sharedPref.getString("status", "").equals("200")) {
                        editor.putString("id", jsonObject.getString("id"));
                        editor.putString("account", jsonObject.getString("account"));
                        editor.putString("phone", phone);
                        editor.commit();
                    }

                    handler.sendEmptyMessage(-1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 查询status信息，如已登陆，则status不为0，跳过登陆直接进入主页
        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        String default_ = "0";
        String id;
        id = sharedPref.getString("id", default_);
        if (!id.equals(default_)) {
            Intent intent1 = new Intent(this, MainActivity.class);
            startActivity(intent1);
            finish();
        }

        init();
        String phone = sharedPref.getString("phone", "");
        if (!phone.equals(default_)) {
            et_phone.setText(phone);
        }




    }

    private void init() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pw = (EditText) findViewById(R.id.et_pw);
        btn_login = (Button) findViewById(R.id.btn_login);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login: {
                //如果有salt就直接登录，否则去后台请求salt
                getSalt();
                break;
            }
            case R.id.btn_register: {
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_forgetpw: {
                Intent intent = new Intent(this, FindPwActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    private void getSalt() {

        // 目标URL
        String urlString = Config.HOST;
        String location = "/android/getSalt";

        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            jo.put("phone", et_phone.getText());

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
                        Toast.makeText(LoginActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        if (sharedPref.getString("status", "").equals("200")) {
                            editor.putString("salt", jsonObject.getString("salt"));
                            editor.commit();
                            btn_login.setText("正在登录...");
                            signIn();
                        } else {
                            Toast.makeText(LoginActivity.this, "账号未注册", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();
    }

    private void signIn() {

        // 目标URL
        String urlString = Config.HOST;
        String location = "/android/login";
        String salt = sharedPref.getString("salt", "");
        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            phone = et_phone.getText().toString();
            jo.put("phone", phone);
            jo.put("password", Md5(et_pw.getText().toString() + salt));
            jo.put("salt", salt);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, mainResponse, HttpHelper.HTTP_POST, null);
        httphelper.execute();
    }

    Handler handler = new Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                if (sharedPref.getString("status", "").equals("200")) {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    btn_login.setText("登录");
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }
            super.handleMessage(msg);
        }
    };

    private static String Md5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //System.out.println("result: " + buf.toString());//32位的加密
            return buf.toString();
            //System.out.println("result: " + buf.toString().substring(8,24));//16位的加密
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "error";
    }
}
