package com.ehelp.ehelp.bank;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.ehelp.ehelp.R;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.main.MainActivity;
import com.liulishuo.magicprogresswidget.MagicProgressCircle;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by UWTH on 2015/10/24.
 */
public class LovingBankActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;

    private MagicProgressCircle mpc;
    private AnimTextView tv_score;
    private AnimTextView tv_lc;
    private AnimTextView tv_flc;
    private RelativeLayout rl111;
    private RelativeLayout rl222;
    private RelativeLayout rl333;
    private RelativeLayout rl444;

    private SharedPreferences sharedPref;

    MaterialDialog dialog;

    String score = "0";
    String love_coin = "0";
    String family_love_coin = "0";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lovingbank);
        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        EventBus.getDefault().register(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(RefreshEvent event) {
        loadData();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.bank));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LovingBankActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        initToolbar();
        initview();
        loadData();
    }

    private void initview(){
        mpc = (MagicProgressCircle) findViewById(R.id.mpc);
        tv_score = (AnimTextView) findViewById(R.id.tv_score);
        tv_lc = (AnimTextView) findViewById(R.id.tv_lc);
        tv_flc = (AnimTextView) findViewById(R.id.tv_flc);
        rl111 = (RelativeLayout) findViewById(R.id.rl111);
        rl222 = (RelativeLayout) findViewById(R.id.rl222);
        rl333 = (RelativeLayout) findViewById(R.id.rl333);
        rl444 = (RelativeLayout) findViewById(R.id.rl444);

        rl111.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LovingBankActivity.this, LovingAccountActivity.class);
                startActivity(intent);
            }
        });
        rl222.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LovingBankActivity.this, LovingBankRecordActivity.class);
                startActivity(intent);
            }
        });
        rl333.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LovingBankActivity.this, LovingBankTransferActivity.class);
                startActivity(intent);
            }
        });
        rl444.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LovingBankActivity.this, LovingBankServiceActivity.class);
                startActivity(intent);
            }
        });
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
        String location = "/android/lovingbank/get_user_score_and_lovecoin";

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
                            score = jsonObject.getString("score");
                            love_coin = jsonObject.getString("love_coin");
                            family_love_coin = jsonObject.getString("family_love_coin");
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

        int s = Integer.parseInt(score);
        int l = Integer.parseInt(love_coin);
        int f = Integer.parseInt(family_love_coin);
        float p;
        if(s<20){
            p = 20f;
        }else if(s<50&&s>=20){
            p = 50f;
        }else if(s<100&&s>=50){
            p = 100f;
        }else if(s<200&&s>=100){
            p = 200f;
        }else if(s<500&&s>=200){
            p = 500f;
        }else{
            p = 1000f;
        }

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(mpc, "percent", 0, s / p),
                ObjectAnimator.ofInt(tv_score, "score", 0, s),
                ObjectAnimator.ofInt(tv_lc, "score", 0, l),
                ObjectAnimator.ofInt(tv_flc, "score", 0, f)
        );
        set.setDuration(1000);
        set.setInterpolator(new AccelerateInterpolator());
        set.start();
    }

}
