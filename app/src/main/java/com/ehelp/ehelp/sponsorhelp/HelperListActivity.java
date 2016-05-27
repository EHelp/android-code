package com.ehelp.ehelp.sponsorhelp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.ehelp.ehelp.square.HelpMsgDetailActivity;
import com.ehelp.ehelp.util.MyCustomDialog5;
import com.ehelp.ehelp.util.NoScrollListView;
import com.ehelp.ehelp.util.Reputation2level;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ehelp.ehelp.R.id.layout_refreshlayout;

/**
 * Created by UWTH on 2015/10/25.
 */
public class HelperListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private NoScrollListView lv_helperlist;
    private Button btn_finishhelp;

    private SwipeRefreshLayout refreshLayout;

    private SharedPreferences sharedPref;
    private ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
    String type = "";

    private int helperNum;
    private double helpersReputation;
    private ImageView iv_teamcreditlevel;
    private TextView tv_nohelper;
    private LinearLayout layout_teamlevel;

    public  Handler handler = new Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                refreshLayout.setRefreshing(false);
                toolbar_title.setText(getResources().getString(R.string.helperlist));
                toolbar_navigationIcon.setBackgroundResource(R.mipmap.back);
                toolbar_navigationIcon.setClickable(true);
                if (sharedPref.getString("status", "").equals("200")) {
                    Toast.makeText(HelperListActivity.this, "获取施助者成功", Toast.LENGTH_SHORT).show();
                    SimpleAdapter simpleAdapter = new SimpleAdapter(HelperListActivity.this, data, R.layout.layout_helpermsg,
                            new String[]{"head", "nickname", "realname", "occupation", "reputation"},
                            new int[]{R.id.iv_head, R.id.tv_nickname, R.id.tv_realname, R.id.tv_occupation, R.id.tv_creditvalue}) {
                        @Override
                        public View getView(final int position, View convertView, ViewGroup parent) {
                            final View view = super.getView(position, convertView, parent);
                            TextView tv_nickname2 = (TextView) view.findViewById(R.id.tv_nickname2);
                            tv_nickname2.getPaint().setFakeBoldText(true);
                            TextView tv_realname2 = (TextView) view.findViewById(R.id.tv_realname2);
                            tv_realname2.getPaint().setFakeBoldText(true);
                            TextView tv_creditvalue2 = (TextView) view.findViewById(R.id.tv_creditvalue2);
                            tv_creditvalue2.getPaint().setFakeBoldText(true);
                            LinearLayout layout_helpermsg = (LinearLayout) view.findViewById(R.id.layout_helpermsg);
                            ImageView iv_sex = (ImageView)view.findViewById(R.id.iv_sex);
                            ImageView iv_creditvalue = (ImageView)view.findViewById(R.id.iv_creditlevel);
                            int temp = (int)(double)data.get(position).get("reputation");
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
                            if ((int)data.get(position).get("gender") == 1) {
                                iv_sex.setImageResource(R.mipmap.male);
                            } else {
                                iv_sex.setImageResource(R.mipmap.female);
                            }
                            layout_helpermsg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(HelperListActivity.this, UserMsgActivity.class);
                                    intent.putExtra("info", data.get(position));
                                    startActivity(intent);
                                }
                            });
                            return view;
                        }
                    };

                    lv_helperlist.setAdapter(simpleAdapter);

                    iv_teamcreditlevel.setImageResource(Reputation2level.getLevel(helpersReputation/helperNum));
                } else {
                    Toast.makeText(HelperListActivity.this, "获取施助者失败", Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == 0) {
                if (sharedPref.getString("status", "").equals("200")) {
                    Toast.makeText(HelperListActivity.this, "您已结束事件", Toast.LENGTH_SHORT).show();
                    MainActivity.ishelping = false;
                    MainActivity.issosing = false;
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.remove("event_id");
                    editor.commit();
                    if (data.size() == 0) {
                        Intent intent = new Intent(HelperListActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(HelperListActivity.this, EvaluateListActivity.class);
                        ArrayList list = new ArrayList();
                        list.add(data);
                        intent.putParcelableArrayListExtra("data", list);
                        startActivity(intent);
                    }
                    finish();
                } else {
                    Toast.makeText(HelperListActivity.this, "提交失败，请检查是否重复提交", Toast.LENGTH_SHORT).show();
                }
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helperlist);
        init();
        getHelperData();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.helperlist));
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

    private void findView() {
        lv_helperlist = (NoScrollListView) findViewById(R.id.lv_helpermsg);
        btn_finishhelp = (Button) findViewById(R.id.btn_finishhelp);
        iv_teamcreditlevel = (ImageView) findViewById(R.id.iv_teamcreditlevel);
        tv_nohelper = (TextView) findViewById(R.id.tv_nohelper);
        layout_teamlevel = (LinearLayout) findViewById(R.id.layout_teamlevel);
    }

    private void FinishSOS() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 目标URL
                String urlString = Config.HOST;
                String location = "/android/emergency_complete";
                urlString += location;
                // JSON对象，存放http请求参数
                JSONObject jo = new JSONObject();
                try {
                    jo.put("id", sharedPref.getString("id", "0"));
                    jo.put("event_id", sharedPref.getInt("event_id", -1));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
                    @Override
                    public void onResponse(Object content) {
                        if (content != null) {
                            String str = ((BackPack) content).getBody();
//                            Toast.makeText(HelperListActivity.this, str, Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject jsonObject = new JSONObject(str);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("status", jsonObject.getString("status"));
                                editor.commit();
                                handler.sendEmptyMessage(0);
                            }catch (Exception e){e.printStackTrace();}
                        }
                    }
                }, HttpHelper.HTTP_POST, null);
                httphelper.execute();
            }
        }).start();
    }

    private void init() {
        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        initToolbar();
        findView();
        if (getIntent().getBooleanExtra("ishelp", true)) {
            type = "求助";
        } else {
            type = "求救";
        }
        btn_finishhelp.setText("完成本次"+type+"事件");

        btn_finishhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomDialog5 dialog = new MyCustomDialog5(HelperListActivity.this, "确认结束"+type+"事件？",
                        new MyCustomDialog5.OnCustomDialogListener() {
                            @Override
                            public void back(boolean flag) {
                                if (flag) {
                                    if (MainActivity.issosing || MainActivity.ishelping) {
                                        FinishSOS();
                                    }
                                    /*
                                    MainActivity.ishelping = false;
                                    MainActivity.issosing = false;

                                    Intent intent = new Intent(HelperListActivity.this, EvaluateListActivity.class);
                                    startActivity(intent);
                                    finish();
                                    */
                                }
                            }
                        });
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

            }
        });

        refreshLayout = (SwipeRefreshLayout) findViewById(layout_refreshlayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.orange, R.color.blue,
                android.R.color.holo_green_light, R.color.blue2);

    }



    public ArrayList<HashMap<String, Object>> getHelperData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 目标URL
                String urlString = Config.HOST;
                String location = "/android/emergency_helper_list";
                urlString += location;
                // JSON对象，存放http请求参数
                JSONObject jo = new JSONObject();
                try {
                    jo.put("event_id", sharedPref.getInt("event_id", -1));

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
                    @Override
                    public void onResponse(Object content) {
                        if (content != null) {
                            String str = ((BackPack) content).getBody();
//                            Toast.makeText(HelperListActivity.this, str, Toast.LENGTH_SHORT).show();
                            try {
                                data = new ArrayList<HashMap<String, Object>>();
                                JSONObject jsonObject = new JSONObject(str);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("status", jsonObject.getString("status"));
                                editor.commit();
                                JSONArray jsonArray = jsonObject.getJSONArray("info");
                                if (jsonArray.length() > 0) {
                                    layout_teamlevel.setVisibility(View.VISIBLE);
                                    btn_finishhelp.setVisibility(View.VISIBLE);
                                    tv_nohelper.setVisibility(View.GONE);
                                    helperNum = jsonArray.length();
                                    helpersReputation = 0;
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject helper = jsonArray.getJSONObject(i);
                                        HashMap<String, Object> temp = new HashMap<String, Object>();
                                        temp.put("head", R.mipmap.head);
                                        temp.put("user_id", helper.getInt("id"));
                                        temp.put("nickname", helper.getString("nickname"));
                                        temp.put("event_id", sharedPref.getInt("event_id", -1));
                                        temp.put("type", sharedPref.getInt("type", -1));
                                        temp.put("occupation", helper.getInt("occupation"));
                                        temp.put("reputation", helper.getDouble("reputation"));
                                        temp.put("verify", helper.getInt("is_verify"));
                                        temp.put("location", helper.getString("location"));
                                        temp.put("gender", helper.getInt("gender"));
                                        if ((int)temp.get("verify") != 0) {
                                            temp.put("realname", helper.getString("name")+"(已通过实名认证)");
                                        } else {
                                            temp.put("realname", "（未实名认证）");
                                        }
                                        data.add(temp);

                                        helpersReputation += helper.getDouble("reputation");
                                    }
                                } else {
                                    layout_teamlevel.setVisibility(View.GONE);
                                    btn_finishhelp.setVisibility(View.GONE);
                                    tv_nohelper.setVisibility(View.VISIBLE);
                                }

                                handler.sendEmptyMessage(-1);

                            }catch (Exception e){e.printStackTrace();}
                        }
                    }
                }, HttpHelper.HTTP_POST, null);
                httphelper.execute();
            }
        }).start();

        return data;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        toolbar_title.setText("正在载入...");
        toolbar_navigationIcon.setBackground(null);
        toolbar_navigationIcon.setClickable(false);
        getHelperData();
        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                toolbar_title.setText(getResources().getString(R.string.helperlist));
                toolbar_navigationIcon.setBackgroundResource(R.mipmap.back);
                toolbar_navigationIcon.setClickable(true);
            }
        }, 2500);
        */
    }
}
