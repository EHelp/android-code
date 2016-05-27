package com.ehelp.ehelp.myparti;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Text;
import com.ehelp.ehelp.R;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.personcenter.UserMsgActivity;
import com.ehelp.ehelp.settings.SettingsActivity;
import com.ehelp.ehelp.square.AskMsg;
import com.ehelp.ehelp.square.AskMsgDetailActivity;
import com.ehelp.ehelp.square.HelpMsgDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by UWTH on 2015/10/24.
 */
public class MyPartiActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;

    private TabHost tabHost;
    private ListView lv_myhelp;
    private ListView lv_mysponsor;

    private SharedPreferences sharedPref;

    private ArrayList<HashMap<String, Object>> myHelpData;
    private ArrayList<HashMap<String, Object>> mySponsorData;
    private SimpleAdapter simpleAdapter1;
    private SimpleAdapter simpleAdapter2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myparti);
        sharedPref = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        init();
        myHelpData = new ArrayList<HashMap<String, Object>>();
        mySponsorData = new ArrayList<HashMap<String, Object>>();
        setMyhelpListContent();
        setMySponsorListContent();

        getMyHelpData();
        getMySponsorData();
    }

    public void setMyhelpListContent() {
        SimpleAdapter adapter = new SimpleAdapter(this, myHelpData, R.layout.layout_myhelphistory,
                new String[]{"head", "launcher", "time", "title", "type"},
                new int[]{R.id.iv_head, R.id.tv_nickname, R.id.tv_time, R.id.tv_title, R.id.iv_helptypeicon}) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final View view = super.getView(position, convertView, parent);

                ImageView iv_head = (ImageView) view.findViewById(R.id.iv_head);
                iv_head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MyPartiActivity.this, UserMsgActivity.class);
                        intent.putExtra("event_id", (int)myHelpData.get(position).get("event_id"));
                        startActivity(intent);
                    }
                });

                final LinearLayout layout_myhelphistory = (LinearLayout) view.findViewById(R.id.layout_myhelphistory);
                final ImageView iv_type = (ImageView) view.findViewById(R.id.iv_helptypeicon);
                final TextView tv_title = (TextView)view.findViewById(R.id.tv_title);
                final TextView tv_detail = (TextView)view.findViewById(R.id.tv_detail);
                if ((int)myHelpData.get(position).get("type") == 0) {
                    iv_type.setImageResource(R.mipmap.ask_icon);
                } else if ((int)myHelpData.get(position).get("type") == 1) {
                    iv_type.setImageResource(R.mipmap.help_icon);
                } else {
                    iv_type.setImageResource(R.mipmap.sos_icon);
                }
                if (myHelpData.get(position).get("title").equals("null")) {
                    tv_title.setText("求救事件");
                }
                if ((int)myHelpData.get(position).get("type") == 0) {
                    tv_detail.setText("查看详情");
                }
                layout_myhelphistory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((int)myHelpData.get(position).get("type") == 0) {
                            Intent intent = new Intent(MyPartiActivity.this, AskMsgDetailActivity.class);

                            intent.putExtra("data", new AskMsg(myHelpData.get(position)));

                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(MyPartiActivity.this, HelpMsgDetailActivity.class);

                            intent.putExtra("event_id", (int)myHelpData.get(position).get("event_id"));
                            intent.putExtra("type", (int)myHelpData.get(position).get("type"));
                            intent.putExtra("invisible", 1);
                            startActivity(intent);
                        }
                    }
                });

                return view;
            }
        };
        lv_myhelp.setAdapter(adapter);
    }

    public void setMySponsorListContent() {
        SimpleAdapter adapter = new SimpleAdapter(this, mySponsorData, R.layout.layout_mysponsorhistory,
                new String[]{"time", "title", "type", "support_number"},
                new int[]{R.id.tv_time, R.id.tv_title, R.id.iv_helptypeicon, R.id.tv_responsenum}) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final View view = super.getView(position, convertView, parent);

                final LinearLayout layout_mysponsorhistory = (LinearLayout) view.findViewById(R.id.layout_mysponsorhistory);
                final ImageView iv_type = (ImageView) view.findViewById(R.id.iv_helptypeicon);
                final TextView tv_responsenum = (TextView) view.findViewById(R.id.tv_responsenum);
                final TextView tv_title = (TextView)view.findViewById(R.id.tv_title);
                tv_responsenum.setText(mySponsorData.get(position).get("support_number")+"人响应");
                if ((int)mySponsorData.get(position).get("type") == 0) {
                    iv_type.setImageResource(R.mipmap.ask_icon);
                } else if ((int)mySponsorData.get(position).get("type") == 1) {
                    iv_type.setImageResource(R.mipmap.help_icon);
                } else {
                    iv_type.setImageResource(R.mipmap.sos_icon);
                }
                if (mySponsorData.get(position).get("title").equals("null")) {
                    tv_title.setText("求救事件");
                }
                layout_mysponsorhistory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((int)mySponsorData.get(position).get("type") == 0) {
                            Intent intent = new Intent(MyPartiActivity.this, AskMsgDetailActivity.class);

                            intent.putExtra("data", new AskMsg(mySponsorData.get(position)));

                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(MyPartiActivity.this, HelpMsgDetailActivity.class);
                            intent.putExtra("invisible", 1);
                            intent.putExtra("event_id", (int)mySponsorData.get(position).get("event_id"));
                            intent.putExtra("type", (int)mySponsorData.get(position).get("type"));
                            startActivity(intent);
                        }
                    }
                });

                return view;
            }
        };
        lv_mysponsor.setAdapter(adapter);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.myparti));
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
        lv_myhelp = (ListView) findViewById(R.id.lv_myhelp);
        lv_mysponsor = (ListView) findViewById(R.id.lv_mysponsor);
    }

    private void initTab() {
        tabHost = (TabHost) findViewById(R.id.tabHost_message);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(getResources().getString(R.string.myhelp), null).setContent(R.id.layout_myhelp));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator(getResources().getString(R.string.mysponsor), null).setContent(R.id.layout_mysponsor));

        TabWidget tabWidget = tabHost.getTabWidget();
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(15);
            tv.setTextColor(getResources().getColor(R.color.black));
            View view = tabWidget.getChildAt(i);
            view.setBackgroundResource(R.drawable.tab_selector);
        }
    }

    private void init() {
        findView();
        initTab();
        initToolbar();
    }

    public ArrayList<HashMap<String, Object>> getMyhelpData() {
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> temp1 = new HashMap<String, Object>();
        temp1.put("head", R.mipmap.head);
        temp1.put("name", "帮客1号");
        temp1.put("time", "今天19:02");
        temp1.put("title", "在菜市场提不动东西");
        temp1.put("type", R.mipmap.help_icon);
        temp1.put("detailtxt", "");
        data.add(temp1);

        HashMap<String, Object> temp2 = new HashMap<String, Object>();
        temp2.put("head", R.mipmap.head);
        temp2.put("name", "帮客2号");
        temp2.put("time", "11月24日19:02");
        temp2.put("title", "SOS");
        temp2.put("type", R.mipmap.sos_icon);
        temp2.put("detailtxt", "");
        data.add(temp2);

        HashMap<String, Object> temp3 = new HashMap<String, Object>();
        temp3.put("head", R.mipmap.head);
        temp3.put("name", "帮客3号");
        temp3.put("time", "11月23日19:02");
        temp3.put("title", "小区物业营业时间？");
        temp3.put("type", R.mipmap.ask_icon);
        temp3.put("detailtxt", "查看详情");
        data.add(temp3);

        data.add(temp1);
        data.add(temp2);
        data.add(temp3);

        return data;
    }


    private void getMyHelpData() {
        myHelpData = new ArrayList<HashMap<String, Object>>();
        getInfo(1, -1);
    }

    private void getMySponsorData() {
        mySponsorData = new ArrayList<HashMap<String, Object>>();
        getInfo(0, -1);
    }

    private void getInfo(final int operation, final int event_id) {

        // 目标URL
        String urlString = Config.HOST;
        String location = "/android/get_my_events";
        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();

        try {
            jo.put("operation", operation);
            jo.put("id", sharedPref.getString("id", "none"));
            if (event_id != -1)
                jo.put("event_id", event_id);
            if (operation == 2) {
                jo.put("longitude", MainActivity.longitude);
                jo.put("latitude", MainActivity.latitude);
            }


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
                    try {
                        JSONObject Json = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", Json.getString("status"));
                        editor.commit();

                        if (sharedPref.getString("status", "none").equals("200")) {
                            JSONArray jsonArray = Json.getJSONArray("event_list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                HashMap<String, Object> temp = new HashMap<String, Object>();
                                //"head", "name", "time", "title", "distance", "response"
                                if (operation == 2) {
                                    LatLng mypos = new LatLng(MainActivity.latitude, MainActivity.longitude);
                                    LatLng targetpos = new LatLng(jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude"));
                                    String diss = new DecimalFormat("#.00").format(AMapUtils.calculateLineDistance(mypos, targetpos) / 1000);
                                    temp.put("distance", diss);
                                    temp.put("head", R.mipmap.head);
//                                    help_event_id = jsonObject.getInt("event_id");
                                } else {
//                                    ask_event_id = jsonObject.getInt("event_id");
                                }
                                temp.put("event_id", jsonObject.getInt("event_id"));
                                temp.put("type", jsonObject.getInt("type"));
                                temp.put("launcher_id", jsonObject.getInt("launcher_id"));
                                temp.put("launcher", jsonObject.getString("launcher"));
                                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("time"));
                                String time = new SimpleDateFormat("MM-dd HH:mm").format(date);
                                temp.put("time", time);
                                date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("last_time"));
                                time = new SimpleDateFormat("MM-dd HH:mm").format(date);
                                temp.put("last_time", time);
                                temp.put("state", jsonObject.getInt("state"));
                                temp.put("demand_number", jsonObject.getInt("demand_number"));
                                temp.put("support_number", jsonObject.getInt("support_number"));
                                temp.put("follow_number", jsonObject.getInt("follow_number"));
                                temp.put("group_pts", jsonObject.getDouble("group_pts"));
                                temp.put("love_coin", jsonObject.getInt("love_coin"));
//                                temp.put("contact", jsonObject.getString("contact"));
                                temp.put("title", jsonObject.getString("title"));
                                temp.put("content", jsonObject.getString("content"));
                                if ((int) temp.get("type") == 0)
                                    temp.put("is_like", jsonObject.getInt("is_like"));
                                temp.put("comment", jsonObject.getString("comment"));
                                temp.put("location", jsonObject.getString("location"));
                                temp.put("head", R.mipmap.head);
                                temp.put("is_verify", jsonObject.getInt("is_verify"));
                                temp.put("occupation", jsonObject.getInt("occupation"));
                                temp.put("reputation", jsonObject.getDouble("reputation"));

                                switch (operation) {
                                    case 0:
                                        mySponsorData.add(temp);
                                        break;
                                    case 1:
                                        myHelpData.add(temp);
                                }
                            }
                            if (operation == 0) {
                                setMySponsorListContent();
                            } else {
                                setMyhelpListContent();
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
}
