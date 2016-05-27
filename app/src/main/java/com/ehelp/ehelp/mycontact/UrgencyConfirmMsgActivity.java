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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.square.SquareActivity;
import com.ehelp.ehelp.util.MyCustomDialog4;
import com.ehelp.ehelp.util.MyCustomDialog6;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by UWTH on 2015/11/14.
 */
public class UrgencyConfirmMsgActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private TextView tv_name;
    private EditText et_confirmmsg;
    private EditText et_alias;
    private TextView tv_group;
    private TextView tv_alias;
    private TextView tv_group2;
    private LinearLayout layout_group;
    private SharedPreferences sharedPref;

    private Button btn_send;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editconfirmmsg);
        sharedPref = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        init();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("添加联系人");
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UrgencyConfirmMsgActivity.this, UrgencySearchEhelpUserActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findView() {
        btn_send = (Button) findViewById(R.id.btn_send);
        tv_name = (TextView)findViewById(R.id.tv_name);
        et_alias = (EditText)findViewById(R.id.et_alias);
        et_confirmmsg = (EditText)findViewById(R.id.et_confirmmsg);
        tv_group = (TextView) findViewById(R.id.tv_group);
        tv_name.setText(getIntent().getStringExtra("nickname"));
        tv_alias = (TextView) findViewById(R.id.tv_alias);
        tv_group2 = (TextView) findViewById(R.id.tv_group2);
        layout_group = (LinearLayout) findViewById(R.id.layout_group);
    }

    private void init() {
        findView();
        initToolbar();
        tv_alias.getPaint().setFakeBoldText(true);
        tv_group2.getPaint().setFakeBoldText(true);
        layout_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomDialog6 dialog = new MyCustomDialog6(UrgencyConfirmMsgActivity.this, "选择分组",
                        new MyCustomDialog6.OnCustomDialogListener() {
                            @Override
                            public void back(String name) {
                                tv_group.setText(name);
                            }
                        });
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
            }
        });
    }

    public void onClick(View view) {

        SendContactRequest();
    }

    private void SendContactRequest() {
        String urlString = Config.HOST;
        String location = "/android/user/manage_static_relation";
        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();

        try {
            jo.put("id", sharedPref.getString("id", "none"));
            jo.put("user_id", getIntent().getIntExtra("user_id", -1));
            jo.put("operation", 1);
            if (tv_group.getText().toString().equals("家人"))
                jo.put("type", 0);
            else if (tv_group.getText().toString().equals("邻居"))
                jo.put("type", 1);
            else
                jo.put("type", 2);
            if (!et_confirmmsg.getText().toString().equals(""))
                jo.put("content", et_confirmmsg.getText().toString());
            else
                jo.put("content", "");
            if (!et_alias.getText().toString().equals(""))
                jo.put("alias", et_alias.getText().toString());
            else
                jo.put("alias", tv_name.getText().toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
            @Override
            public void onResponse(Object content) {
                if (content != null) {
                    String str = ((BackPack) content).getBody();
//                    Toast.makeText(SponsorAskActivity.this, str, Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        if (jsonObject.getString("status").equals("200")) {

                            Toast.makeText(UrgencyConfirmMsgActivity.this, "请求已发送，请等待对方回复", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UrgencyConfirmMsgActivity.this, MyContactActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(UrgencyConfirmMsgActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
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
