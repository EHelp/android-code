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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.account.LoginActivity;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.util.ActivityCollector;
import com.ehelp.ehelp.util.DBManager;
import com.ehelp.ehelp.util.MyCustomDialog5;
import com.ehelp.ehelp.util.NoScrollListView;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by UWTH on 2015/10/25.
 */
public class EvaluateListActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private NoScrollListView lv_evaluate;
    private ArrayList<HashMap<String, Object>> data;
    private SimpleAdapter simpleAdapter;
    private DBManager dbManager;

    private RatingBar rb_evaluate;
    private SharedPreferences sharedPref;
    public Handler handler;
    private HashMap helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluatelist);
        dbManager = new DBManager(this);
        init();

        simpleAdapter = new SimpleAdapter(this, getEvaluateData(), R.layout.layout_mymsg,
                new String[]{"content"}, new int[]{R.id.tv_content}) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final View view = super.getView(position, convertView, parent);
                TextView tv_btn = (TextView) view.findViewById(R.id.tv_btn);
                tv_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(EvaluateListActivity.this, EvaluateActivity.class);
                        intent.putExtra("helper", data.get(position));
                        intent.putExtra("position", position);
                        startActivity(intent);
                    }
                });
                return view;
            }
        };
        lv_evaluate.setAdapter(simpleAdapter);

        handler = new Handler() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {

                    if (sharedPref.getString("status", "").equals("200")) {
                        //Toast.makeText(EvaluateActivity.this, "评价成功", Toast.LENGTH_SHORT).show();
                        if (getIntent().getStringExtra("content") != null) {
                            DBManager dbManager = new DBManager(EvaluateListActivity.this);
                            dbManager.deleteSysMessage((int)helper.get("_id"));
                            //EvaluateActivity.this.finish();
                            return;
                        }
//                        Intent intent = new Intent(EvaluateActivity.this, EvaluateListActivity.class);
//                        intent.putExtra("position", getIntent().getIntExtra("position", -1));
//                        startActivity(intent);
                    } else {
                        //Toast.makeText(EvaluateActivity.this, "您已评价过该对象", Toast.LENGTH_SHORT).show();
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
                String temp = getSharedPreferences("user_id", Context.MODE_PRIVATE).getString("id", "0");
                dbManager.addEvaluate(data, Integer.parseInt(temp));
                Intent intent = new Intent(EvaluateListActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void findView() {
        lv_evaluate = (NoScrollListView) findViewById(R.id.lv_evaluate);
        rb_evaluate = (RatingBar) findViewById(R.id.rb_evaluate);
    }

    private void init() {
        initToolbar();
        findView();

        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);

        rb_evaluate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                MyCustomDialog5 dialog = new MyCustomDialog5(EvaluateListActivity.this, "确认对所有帮客一键评价？",
                        new MyCustomDialog5.OnCustomDialogListener() {
                            @Override
                            public void back(boolean flag) {
                                //logout_flag[0] = flag;
                                if (flag) {
                                    Toast.makeText(EvaluateListActivity.this, "评价成功", Toast.LENGTH_SHORT).show();
                                    for (int i = 0; i < data.size(); i++) {
                                        helper = data.get(i);
                                        Evaluate();
                                    }
                                    finish();
                                }
                            }
                        });
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
            }
        });
    }

    public ArrayList<HashMap<String, Object>> getEvaluateData() {
        ArrayList list = getIntent().getParcelableArrayListExtra("data");
        data = (ArrayList<HashMap<String, Object>>)list.get(0);
        for (int i = 0; i < data.size(); i++){
            String nickname = (String)data.get(i).get("nickname");
            data.get(i).put("content", "对“"+nickname+"”的帮助进行评价");
        }

        return data;
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // must store the new intent unless getIntent() will return the old one
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getInt("position", -1) != -1) {
            data.remove(bundle.getInt("position"));
            simpleAdapter.notifyDataSetChanged();
        }
        if (data.size() == 0) {
            finish();
        }
        super.onResume();
    }

    private void Evaluate() {
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
            jo.put("value", rb_evaluate.getRating());
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
