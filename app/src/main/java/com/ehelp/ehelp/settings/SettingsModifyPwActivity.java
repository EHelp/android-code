package com.ehelp.ehelp.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by UWTH on 2015/10/23.
 */
public class SettingsModifyPwActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private Button toolbar_icon;
    private TextView tv_account;
    private EditText et_pw;
    private EditText et_newpw;
    private EditText et_confirmpw;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_modifypw);
        sharedPreferences = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        init();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.modifypw));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsModifyPwActivity.this, SettingsSecurityActivity.class);
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
                String currentPw = et_pw.getText().toString();
                if (!judgeCurrentPw(currentPw)) {
                    Toast.makeText(SettingsModifyPwActivity.this, "当前密码不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                String newPw = et_newpw.getText().toString();
                String confirmPw = et_confirmpw.getText().toString();
                if (newPw.equals("")) {
                    Toast.makeText(SettingsModifyPwActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPw.length() < 6) {
                    Toast.makeText(SettingsModifyPwActivity.this, "密码过短", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPw.length() > 15) {
                    Toast.makeText(SettingsModifyPwActivity.this, "密码过长", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newPw.equals(confirmPw)) {
                    Toast.makeText(SettingsModifyPwActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                ModifyPw();

            }
        });
    }

    private void ModifyPw() {

        String urlString = Config.HOST;
        String location = "/android/user/change_password";

        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            String salt = sharedPreferences.getString("salt", "none");
            jo.put("phone", sharedPreferences.getString("phone", "none"));
            jo.put("password", Md5(et_pw.getText().toString()+salt));
            jo.put("new_password", et_newpw.getText().toString());
            jo.put("salt", salt);


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
                        Toast.makeText(SettingsModifyPwActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        if (sharedPreferences.getString("status", "").equals("200")) {
                            Toast.makeText(SettingsModifyPwActivity.this, getResources().getText(R.string.modifypwfinish), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SettingsModifyPwActivity.this, SettingsActivity.class);
                            startActivity(intent);
                        } else if (sharedPreferences.getString("status", "").equals("400")){
                            Toast.makeText(SettingsModifyPwActivity.this, "原密码错误", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SettingsModifyPwActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();
    }

    private void findView() {
        et_pw = (EditText) findViewById(R.id.et_pw);
        et_newpw = (EditText) findViewById(R.id.et_newpw);
        et_confirmpw = (EditText) findViewById(R.id.et_confirmpw);
        tv_account = (TextView)findViewById(R.id.tv_account);

        tv_account.setText(sharedPreferences.getString("phone", "none"));
    }

    private void init() {
        initToolbar();
        findView();
    }

    // 判断输入的当前密码是否正确
    public boolean judgeCurrentPw(String pw) {
        return true;
    }

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
