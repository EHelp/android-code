package com.ehelp.ehelp.mycontact;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.myparti.MyPartiActivity;
import com.ehelp.ehelp.util.MyCustomDialog5;
import com.ehelp.ehelp.util.NullDealer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by UWTH on 2015/12/5.
 */
public class UrgencyContactMsgActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private Button toolbar_icon;

    private SharedPreferences sharedPref;

    private TextView tv_nickname;
    private TextView tv_realname;
    private TextView tv_sex;
    private TextView tv_occupation;
    private TextView tv_reputation;
    private TextView tv_location;
    private TextView tv_verify;
    private ImageView iv_sex;
    private ImageView iv_creditvalue;
    private HashMap data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermsg);
        findView();
        init();
        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);
    }

    private void findView() {
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_realname = (TextView) findViewById(R.id.tv_realname);
        tv_reputation = (TextView) findViewById(R.id.tv_creditvalue);
        tv_occupation = (TextView) findViewById(R.id.tv_occupation);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_verify = (TextView) findViewById(R.id.tv_isCertification);
        tv_location = (TextView) findViewById(R.id.tv_location);
        iv_sex = (ImageView) findViewById(R.id.iv_sex);
        iv_creditvalue = (ImageView)findViewById(R.id.iv_creditlevel);

        data = (HashMap) getIntent().getSerializableExtra("info");
        if (data != null) {
            tv_nickname.setText(data.get("name").toString());
            tv_realname.setText(data.get("realname").toString());
            tv_reputation.setText(data.get("creditvalue").toString());
            int temp = (int)(double)data.get("creditvalue");
            switch (temp) {
                case 0:
                    iv_creditvalue.setImageResource(R.mipmap.creditlevel0);
                    break;
                case 1:
                    iv_creditvalue.setImageResource(R.mipmap.creditlevel1);
                    break;
                case 2:
                    iv_creditvalue.setImageResource(R.mipmap.creditlevel2);
                    break;
                case 3:
                    iv_creditvalue.setImageResource(R.mipmap.creditlevel3);
                    break;
                case 4:
                    iv_creditvalue.setImageResource(R.mipmap.creditlevel4);
                    break;
                case 5:
                    iv_creditvalue.setImageResource(R.mipmap.creditlevel5);
                    break;
            }
            tv_occupation.setText(data.get("occupation").toString());
            tv_location.setText(NullDealer.DealNull(data.get("location").toString()));
            if ((int) data.get("verify") == 0) {
                tv_verify.setText("否");
                tv_realname.setText("未实名认证");
            } else {
                tv_verify.setText("是");
                tv_realname.setText(data.get("realname").toString());
            }
            if ((int) data.get("gender") == 1) {
                iv_sex.setImageResource(R.mipmap.male);
            } else {
                iv_sex.setImageResource(R.mipmap.female);
            }
        } else {
            getInfo(getIntent().getIntExtra("event_id", -1));
        }
    }

    private void getInfo(final int event_id) {

        // 目标URL
        String urlString = Config.HOST;
        String location = "/android/recipient_info";
        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();

        try {
            jo.put("event_id", event_id);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
            @Override
            public void onResponse(Object content) {
                if (content != null) {
                    String str = ((BackPack) content).getBody();
//                            Toast.makeText(UserMsgActivity.this, str, Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        if (sharedPref.getString("status", "none").equals("200")) {
                            tv_nickname.setText(jsonObject.getString("nickname"));
                            tv_occupation.setText(jsonObject.getString("occupation"));
                            tv_reputation.setText(jsonObject.getDouble("reputation") + "");
                            tv_location.setText(jsonObject.getString("location"));
                            if (jsonObject.getInt("gender") == 1) {
                                iv_sex.setImageResource(R.mipmap.male);
                            } else {
                                iv_sex.setImageResource(R.mipmap.female);
                            }
                            if (jsonObject.getInt("is_verify") == 1) {
                                tv_verify.setText("是");
                                tv_realname.setText(jsonObject.getString("name") + "（已通过实名认证）");
                            } else {
                                tv_verify.setText("否");
                                tv_realname.setText("（未实名认证）");
                            }
                        }
//                                handler.sendEmptyMessage(-1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();

    }

    private void DeleteContact() {

            // 目标URL
            String urlString = Config.HOST;
            String location = "/android/user/manage_static_relation";
            urlString += location;
            // JSON对象，存放http请求参数
            JSONObject jo = new JSONObject();

            try {
                jo.put("operation", 0);
                jo.put("id", sharedPref.getString("id", "none"));
                jo.put("user_id", data.get("user_id"));


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
                            Toast.makeText(UrgencyContactMsgActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                            return;
                        }
//                            Toast.makeText(UserMsgActivity.this, str, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(str);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("status", jsonObject.getString("status"));
                            editor.commit();
                            if (sharedPref.getString("status", "none").equals("200")) {
                                Toast.makeText(UrgencyContactMsgActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                UrgencyContactMsgActivity.this.finish();
                            } else {
                                Toast.makeText(UrgencyContactMsgActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                            }
//                                handler.sendEmptyMessage(-1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, HttpHelper.HTTP_POST, null);
            httphelper.execute();

        }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.usermsg));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 设置右边按钮
        toolbar_icon = (Button) findViewById(R.id.toolbar_icon);
        toolbar_icon.setBackgroundResource(R.drawable.delete_selector);
        toolbar_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomDialog5 dialog = new MyCustomDialog5(UrgencyContactMsgActivity.this, "是否删除该联系人？",
                        new MyCustomDialog5.OnCustomDialogListener() {
                            @Override
                            public void back(boolean flag) {
                                if (flag) {
                                    DeleteContact();
                                }
                            }
                        });
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
            }
        });
    }

    private void init() {
        initToolbar();
    }
}
