package com.ehelp.ehelp.square;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.ehelp.ehelp.userhelp.UserhelpActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by UWTH on 2015/12/8.
 */
public class ReplyAskActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private Button toolbar_icon;
    private EditText et_reply;
    private AskMsg askMsg;
    private SharedPreferences sharedPref;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editans);
        init();
    }

    private void PostAnswer(String content) {
        // 目标URL
        String urlString = Config.HOST;
        String location = "/android/event/add_answer";
        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();

        try {
            jo.put("event_id", askMsg.getEvent_id());
            jo.put("id", sharedPref.getString("id", "none"));
            jo.put("content", content);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
            @Override
            public void onResponse(Object content) {
                if (content != null) {
                    String str = ((BackPack) content).getBody();
//                            Toast.makeText(SquareActivity.this, str, Toast.LENGTH_SHORT).show();
                    if (str == null) {
                        Toast.makeText(ReplyAskActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        JSONObject Json = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", Json.getString("status"));
                        editor.commit();

                        if (!sharedPref.getString("status", "none").equals("200")) {
                            Toast.makeText(ReplyAskActivity.this, "回复失败", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(ReplyAskActivity.this, AskMsgDetailActivity.class);
                            intent.putExtra("from", "replyAsk");

                            startActivity(intent);
                            finish();
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
        toolbar_title.setText("写回答");
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
        toolbar_icon.setBackgroundResource(R.drawable.sendicon_selector);
        toolbar_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ans = et_reply.getText().toString();
                if (ans.length() <= 0 || ans == null) {
                    Toast.makeText(ReplyAskActivity.this, "请编辑回答", Toast.LENGTH_SHORT).show();
                    return;
                }
                PostAnswer(ans);
            }
        });
    }

    private void init() {
        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        askMsg = (AskMsg)getIntent().getSerializableExtra("data");
        et_reply = (EditText) findViewById(R.id.et_reply);
        initToolbar();
    }
}
