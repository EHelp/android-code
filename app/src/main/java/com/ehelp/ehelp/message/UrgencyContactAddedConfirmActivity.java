package com.ehelp.ehelp.message;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.ehelp.ehelp.R;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.mycontact.MyContactActivity;
import com.ehelp.ehelp.util.DBManager;
import com.ehelp.ehelp.util.SystemMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by UWTH on 2015/11/15.
 */
public class UrgencyContactAddedConfirmActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private TextView tv_nickname;
    private TextView tv_reputation;
    private TextView tv_helping_time;
    private TextView tv_isCertification;
    private TextView tv_realname;
    private TextView tv_location;
    private TextView tv_occupation;
    private ImageView iv_creditvalue;
    private ImageView iv_sex;
    private SharedPreferences sharedPreferences;
    private SystemMessage systemMessage;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmurgencycontactadded);
        sharedPreferences = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        systemMessage = (SystemMessage)getIntent().getSerializableExtra("contact");
        init();
        getInfo();
    }

    private void findView() {
        tv_location = (TextView)findViewById(R.id.tv_location);
        tv_nickname = (TextView)findViewById(R.id.tv_nickname);
        tv_occupation = (TextView)findViewById(R.id.tv_occupation);
        tv_reputation = (TextView)findViewById(R.id.tv_creditvalue);
        tv_helping_time = (TextView)findViewById(R.id.tv_helpingtime);
        tv_location.setText(systemMessage.getLocation());
        tv_nickname.setText(systemMessage.getNickname());
        tv_occupation.setText(systemMessage.getOccupation());

        tv_isCertification = (TextView)findViewById(R.id.tv_isCertification);
        iv_sex = (ImageView)findViewById(R.id.iv_sex);
        iv_creditvalue = (ImageView)findViewById(R.id.iv_creditlevel);
        tv_realname = (TextView)findViewById(R.id.tv_realname);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.mymessage));
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
    }

    private void init() {
        initToolbar();
        findView();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_yes: {
                HandleContact(1);
                break;
            }
            case R.id.btn_no: {
                HandleContact(0);
                break;
            }
        }
    }

    private void HandleContact(int operation) {
        String urlString = Config.HOST;
        String location = "/android/user/handle_static_relation";
        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();

        try {
            jo.put("id", sharedPreferences.getString("id", "none"));
            jo.put("user_id", systemMessage.getUser_id());
            jo.put("operation", operation);

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
                        Toast.makeText(UrgencyContactAddedConfirmActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    }
//                    Toast.makeText(SponsorAskActivity.this, str, Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        if (jsonObject.getString("status").equals("200")) {

                            Toast.makeText(UrgencyContactAddedConfirmActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                            DBManager dbManager = new DBManager(UrgencyContactAddedConfirmActivity.this);
                            dbManager.deleteSysMessage(systemMessage.get_id());
                            finish();
                        } else {
                            Toast.makeText(UrgencyContactAddedConfirmActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();
    }

    private void getInfo() {
        String urlString = Config.HOST;
        String location = "/android/user/get_information";

        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            jo.put("id", systemMessage.getUser_id());


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
                        Toast.makeText(UrgencyContactAddedConfirmActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        if (jsonObject.getString("status").equals("200")) {
                            tv_reputation.setText(jsonObject.getDouble("reputation")+"");
//                            tv_helping_time.setText(jsonObject.getInt("support_number")+"");
                            int temp = (int)jsonObject.getDouble("reputation");
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
                            if (jsonObject.getInt("is_verify") == 1) {
                                tv_realname.setText(jsonObject.getString("name"));
                                tv_isCertification.setText("是");
                            } else {
                                tv_realname.setText("未实名认证");
                                tv_isCertification.setText("否");
                            }
                            if (jsonObject.getInt("gender") == 1) {
                                iv_sex.setImageResource(R.mipmap.male);
                            } else {
                                iv_sex.setImageResource(R.mipmap.female);
                            }
                        } else {
                            Toast.makeText(UrgencyContactAddedConfirmActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
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
