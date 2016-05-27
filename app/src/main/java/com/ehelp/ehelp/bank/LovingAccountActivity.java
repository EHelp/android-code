package com.ehelp.ehelp.bank;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.ehelp.ehelp.R;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by benson on 2015/11/19.
 */
public class LovingAccountActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;

    private TextView username;
    private TextView rank;
    private TextView nickname;
    private TextView score;
    private TextView coin;
    private TextView family_coin;

    private String susername;
    private String srank;
    private String snickname;
    private String sscore;
    private String scoin;
    private String sfamily_coin;

    private SharedPreferences sharedPref;

    MaterialDialog dialog;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loving_account);
        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        init();

    }
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.accountinfo));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LovingAccountActivity.this, LovingBankActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void init(){
        initToolbar();
        initView();
        loadData();
    }

    private void initView(){
        username = (TextView) findViewById(R.id.username);
        rank = (TextView) findViewById(R.id.rank);
        nickname = (TextView) findViewById(R.id.nickname);
        score = (TextView) findViewById(R.id.score);
        coin = (TextView) findViewById(R.id.coin);
        family_coin = (TextView) findViewById(R.id.family_coin);
    }

    private void loadData() {
        dialog = new MaterialDialog.Builder(this)
                .theme(Theme.LIGHT)
                .title("正在加载")
                .content("请稍候...")
                .progress(true, 0)
                .cancelable(false)
                .show();

        String urlString = Config.HOST;
        String location = "/android/lovingbank/lovebank_personal_information";

        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            jo.put("user_id", Integer.parseInt(sharedPref.getString("id", "none")));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
            @Override
            public void onResponse(Object content) {
                if (content != null) {
                    String str = ((BackPack) content).getBody();
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        if (jsonObject.getString("status").equals("200")) {
                            susername = jsonObject.getString("nickname");
                            sscore = jsonObject.getString("score");
                            scoin = jsonObject.getString("love_coin");
                            sfamily_coin = jsonObject.getString("family_love_coin");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                dialog.cancel();
                showData();
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();
    }

    private void showData(){
        int s;
        int r;
        if(sscore != null){
            s = Integer.parseInt(sscore);
            r = s/10;
            srank = "" + r;
        }else{
            s = 0;
            r = 0;
        }


        if(s<20){
            snickname = "少先队员";
        }else if(s<50&&s>=20){
            snickname = "青年志愿者";
        }else if(s<100&&s>=50){
            snickname = "小雷锋";
        }else{
            snickname = "活雷锋";
        }

        username.setText(susername);
        rank.setText(srank);
        nickname.setText(snickname);
        score.setText(sscore);
        coin.setText(scoin);
        family_coin.setText(sfamily_coin);

    }


}
