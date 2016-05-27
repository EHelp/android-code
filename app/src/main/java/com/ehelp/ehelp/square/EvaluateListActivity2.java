package com.ehelp.ehelp.square;

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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.adapter.EvaluateHelperAdapter;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.sponsorhelp.EvaluateActivity;
import com.ehelp.ehelp.util.DBManager;
import com.ehelp.ehelp.util.MyCustomDialog5;
import com.ehelp.ehelp.util.NoScrollListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yunzhao on 2016/3/20.
 */
public class EvaluateListActivity2 extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;

    private NoScrollListView lv_evaluate;
    private ArrayList<Evaluate> data;
    private EvaluateHelperAdapter evaluateHelperAdapter;
    private DBManager dbManager;

    private RatingBar rb_evaluate;
    private Button btn_upload;
    private SharedPreferences sharedPref;

    public Handler handler = new Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage (Message msg){
            if (msg.what == 0) {
                evaluateHelperAdapter = new EvaluateHelperAdapter(getEvaluateData(), R.layout.layout_evaluate, EvaluateListActivity2.this);
                lv_evaluate.setAdapter(evaluateHelperAdapter);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate2);
        //dbManager = new DBManager(this);
        init();
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
//                String temp = getSharedPreferences("user_id", Context.MODE_PRIVATE).getString("id", "0");
//                dbManager.addEvaluate(data, Integer.parseInt(temp));
//                Intent intent = new Intent(EvaluateListActivity2.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }

    private void findView() {
        lv_evaluate = (NoScrollListView) findViewById(R.id.lv_evaluate);
        rb_evaluate = (RatingBar) findViewById(R.id.rb_evaluate);
        btn_upload = (Button) findViewById(R.id.btn_upload);
    }

    private void init() {
        initToolbar();
        findView();

        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);

        // 一键评价
        rb_evaluate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setRating(rb_evaluate.getRating());
                }
                evaluateHelperAdapter.refresh(data);
            }
        });

        evaluateHelperAdapter = new EvaluateHelperAdapter(getEvaluateData(), R.layout.layout_evaluate, EvaluateListActivity2.this);
        lv_evaluate.setAdapter(evaluateHelperAdapter);
    }

    public ArrayList<Evaluate> getEvaluateData() {
        data = new ArrayList<Evaluate>();
        for (int i = 0; i < 3; i++){
            Evaluate temp = new Evaluate("张三"+i, 3);
            data.add(temp);
        }
        return data;
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // must store the new intent unless getIntent() will return the old one
        setIntent(intent);
    }
//
//    @Override
//    protected void onResume() {
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null && bundle.getInt("position", -1) != -1) {
//            data.remove(bundle.getInt("position"));
//            simpleAdapter.notifyDataSetChanged();
//        }
//        if (data.size() == 0) {
//            finish();
//        }
//        super.onResume();
//    }

    private void Evaluate() {
        // 目标URL
//        String urlString = Config.HOST;
//        String location = "/android/evaluate_event";
//        urlString += location;
//        // JSON对象，存放http请求参数
//        JSONObject jo = new JSONObject();
//        try {
//            jo.put("id", sharedPref.getString("id", "0"));
//            jo.put("user_id", helper.get("user_id"));
//            jo.put("event_id", helper.get("event_id"));
//            jo.put("value", rb_evaluate.getRating());
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
//            @Override
//            public void onResponse(Object content) {
//                if (content != null) {
//                    String str = ((BackPack) content).getBody();
////                    Toast.makeText(EvaluateActivity.this, str, Toast.LENGTH_SHORT).show();
//                    try {
//                        JSONObject jsonObject = new JSONObject(str);
//                        SharedPreferences.Editor editor = sharedPref.edit();
//                        editor.putString("status", jsonObject.getString("status"));
//                        editor.commit();
//                        handler.sendEmptyMessage(0);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }, HttpHelper.HTTP_POST, null);
//        httphelper.execute();
    }

    // 提交按钮点击事件
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_upload:
                Evaluate();
        }
    }
}
