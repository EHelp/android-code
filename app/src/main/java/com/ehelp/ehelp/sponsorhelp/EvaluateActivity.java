package com.ehelp.ehelp.sponsorhelp;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.personcenter.UserMsgActivity;
import com.ehelp.ehelp.util.DBManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by UWTH on 2015/10/25.
 */
public class EvaluateActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private Button btn_upload;
    private RatingBar rating;
    private RatingBar quality;
    private TextView nickname;
    private TextView tv_realname;
    //private CheckBox cb_ishelp;
    private LinearLayout layout_evaluate;
    private TextView tv_text;
    private RadioGroup rb_truth;
    private SharedPreferences sharedPref;
    public Handler handler;

    private HashMap helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        init();
        handler = new Handler() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {

                    if (sharedPref.getString("status", "").equals("200")) {
                        Toast.makeText(EvaluateActivity.this, "评价成功", Toast.LENGTH_SHORT).show();
                        if (getIntent().getStringExtra("content") != null) {
                            DBManager dbManager = new DBManager(EvaluateActivity.this);
                            dbManager.deleteSysMessage((int)helper.get("_id"));
                            EvaluateActivity.this.finish();
                            return;
                        }
                        Intent intent = new Intent(EvaluateActivity.this, EvaluateListActivity.class);
                        intent.putExtra("position", getIntent().getIntExtra("position", -1));
                        startActivity(intent);
                    } else {
                        Toast.makeText(EvaluateActivity.this, "您已评价过该对象", Toast.LENGTH_SHORT).show();
                    }
                }

                super.handleMessage(msg);
            }
        };
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.evaluate));
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
        helper = (HashMap) getIntent().getSerializableExtra("helper");
        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        rating = (RatingBar) findViewById(R.id.rb_speed);
        quality = (RatingBar) findViewById(R.id.rb_quality);
        nickname = (TextView) findViewById(R.id.tv_nickname);
        btn_upload = (Button) findViewById(R.id.btn_upload);

        nickname = (TextView)findViewById(R.id.tv_nickname);
        tv_realname = (TextView)findViewById(R.id.tv_realname);
        //cb_ishelp = (CheckBox) findViewById(R.id.cb_ishelp);
        rb_truth = (RadioGroup) findViewById(R.id.rb_truth);
        layout_evaluate = (LinearLayout) findViewById(R.id.layout_evaluate);
        tv_text = (TextView) findViewById(R.id.tv_text);
        tv_text.getPaint().setFakeBoldText(true);
        nickname.setText(helper.get("nickname").toString());
        tv_realname.setText(helper.get("realname").toString());
        rb_truth.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_true) {
                    layout_evaluate.setVisibility(View.VISIBLE);
                    btn_upload.setClickable(true);
                } else {
                    layout_evaluate.setVisibility(View.GONE);
                    btn_upload.setClickable(true);
                }
                btn_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Evaluate();
                    }
                });
                btn_upload.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
            }
        });
        btn_upload.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(EvaluateActivity.this, "您尚未评价", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
//        cb_ishelp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    layout_evaluate.setVisibility(View.VISIBLE);
//                } else {
//                    layout_evaluate.setVisibility(View.GONE);
//                }
//            }
//        });
    }

    private void Evaluate() {
//        if (rb_truth.getCheckedRadioButtonId() == R.id.rb_false) {
//
//            finish();
//        }
        // 目标URL
        String urlString = Config.HOST;
        String location = "/android/evaluate_event";
        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            jo.put("id", sharedPref.getString("id", "0"));
            jo.put("user_id", helper.get("user_id"));
            jo.put("event_id", helper.get("event_id"));
            jo.put("value", (rating.getRating()+quality.getRating())/2);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
            @Override
            public void onResponse(Object content) {
                if (content != null) {
                    String str = ((BackPack) content).getBody();
//                    Toast.makeText(EvaluateActivity.this, str, Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        handler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();

    }
}
