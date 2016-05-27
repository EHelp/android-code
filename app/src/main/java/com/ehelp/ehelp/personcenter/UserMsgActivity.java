package com.ehelp.ehelp.personcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.ehelp.ehelp.R;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.util.NullDealer;
import com.ehelp.ehelp.util.OccupationEncode;
import com.ehelp.ehelp.util.Reputation2level;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by UWTH on 2015/10/25.
 */
public class UserMsgActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;

    private  SharedPreferences sharedPref;

    private TextView tv_nickname;
    private TextView tv_realname;
    private ImageView iv_sex;
    private TextView tv_occupation;
    private TextView tv_reputation;
    private ImageView iv_reputationlevel;
    private TextView tv_location;
    private TextView tv_verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermsg);
        findView();
        init();
        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);
    }

    private void findView() {
        tv_nickname = (TextView)findViewById(R.id.tv_nickname);
        tv_realname = (TextView)findViewById(R.id.tv_realname);
        tv_reputation = (TextView)findViewById(R.id.tv_creditvalue);
        tv_occupation = (TextView)findViewById(R.id.tv_occupation);
        iv_sex = (ImageView)findViewById(R.id.iv_sex);
        tv_verify = (TextView)findViewById(R.id.tv_isCertification);
        tv_location = (TextView)findViewById(R.id.tv_location);
        iv_reputationlevel = (ImageView) findViewById(R.id.iv_creditlevel);

        HashMap data = (HashMap) getIntent().getSerializableExtra("info");
        if (data != null) {
            tv_nickname.setText(data.get("nickname").toString());
            tv_realname.setText(data.get("realname").toString());
            tv_reputation.setText(data.get("reputation").toString());
            int temp = (int)(double)data.get("reputation");
            Log.i("1", "hh");
            switch (temp) {
                case 0:
                    iv_reputationlevel.setImageResource(R.mipmap.creditlevel0);
                    break;
                case 1:
                    iv_reputationlevel.setImageResource(R.mipmap.creditlevel1);
                    break;
                case 2:
                    iv_reputationlevel.setImageResource(R.mipmap.creditlevel2);
                    break;
                case 3:
                    iv_reputationlevel.setImageResource(R.mipmap.creditlevel3);
                    break;
                case 4:
                    iv_reputationlevel.setImageResource(R.mipmap.creditlevel4);
                    break;
                case 5:
                    iv_reputationlevel.setImageResource(R.mipmap.creditlevel5);
                    break;
            }
            tv_occupation.setText(OccupationEncode.OccupationToString((int)data.get("occupation")));
            if ((int) data.get("verify") == 0) {
                tv_verify.setText("否");
            } else {
                tv_verify.setText("是");
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
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                                    tv_occupation.setText(OccupationEncode.OccupationToString(jsonObject.getInt("occupation")));
                                    tv_reputation.setText(jsonObject.getDouble("reputation")+"");
                                    int temp = (int)jsonObject.getDouble("reputation");
                                    switch (temp) {
                                        case 0:
                                            iv_reputationlevel.setImageResource(R.mipmap.creditlevel0);
                                            break;
                                        case 1:
                                            iv_reputationlevel.setImageResource(R.mipmap.creditlevel1);
                                            break;
                                        case 2:
                                            iv_reputationlevel.setImageResource(R.mipmap.creditlevel2);
                                            break;
                                        case 3:
                                            iv_reputationlevel.setImageResource(R.mipmap.creditlevel3);
                                            break;
                                        case 4:
                                            iv_reputationlevel.setImageResource(R.mipmap.creditlevel4);
                                            break;
                                        case 5:
                                            iv_reputationlevel.setImageResource(R.mipmap.creditlevel5);
                                            break;
                                    }
                                    tv_location.setText(NullDealer.DealNull(jsonObject.getString("location")));
                                    if (jsonObject.getInt("gender") == 1) {
                                        iv_sex.setImageResource(R.mipmap.male);
                                    } else {
                                        iv_sex.setImageResource(R.mipmap.female);
                                    }
//                                    iv_reputationlevel.setImageResource(Reputation2level.getLevel(jsonObject.getDouble("reputation")));
                                    if (jsonObject.getInt("is_verify") == 1) {
                                        tv_verify.setText("是");
                                        tv_realname.setText(jsonObject.getString("name")+"（已通过实名认证）");
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
        }).start();
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
    }

    private void setFontBold() {
        TextView tv_realname2 = (TextView) findViewById(R.id.tv_realname2);
        tv_realname2.getPaint().setFakeBoldText(true);
        TextView tv_isCertification2 = (TextView) findViewById(R.id.tv_isCertification2);
        tv_isCertification2.getPaint().setFakeBoldText(true);
        TextView tv_creditvalue2 = (TextView) findViewById(R.id.tv_creditvalue2);
        tv_creditvalue2.getPaint().setFakeBoldText(true);
        TextView tv_creditlevel2 = (TextView) findViewById(R.id.tv_creditlevel2);
        tv_creditlevel2.getPaint().setFakeBoldText(true);
        TextView tv_location2 = (TextView) findViewById(R.id.tv_location2);
        tv_location2.getPaint().setFakeBoldText(true);
        TextView tv_occupation2 = (TextView) findViewById(R.id.tv_occupation2);
        tv_occupation2.getPaint().setFakeBoldText(true);
    }

    private void init() {
        initToolbar();
        setFontBold();
    }
}
