package com.ehelp.ehelp.message;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.adapter.PrivateMessageAdapter;
import com.ehelp.ehelp.adapter.SystemMessageAdapter;
import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.sponsorhelp.EvaluateActivity;
import com.ehelp.ehelp.util.DBManager;
import com.ehelp.ehelp.util.PrivateMessage;
import com.ehelp.ehelp.util.SystemMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by UWTH on 2015/10/24.
 */
public class MyMessageActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private TabHost tabHost;
    private ListView lv_personmsg;
    private ListView lv_systemmsg;
    private ArrayList<SystemMessage> data;
    private ArrayList<PrivateMessage> pri;
    private SystemMessageAdapter systemMessageAdapter;
    private PrivateMessageAdapter privateMessageAdapter;
    private DBManager dbManager;

    private SwipeRefreshLayout refreshLayout1;
    private SwipeRefreshLayout refreshLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        init();
        dbManager = new DBManager(this);
        String temp = getSharedPreferences("user_id", Context.MODE_PRIVATE).getString("id", "0");
        data = (ArrayList<SystemMessage>) dbManager.querySystem(Integer.parseInt(temp));

        pri = (ArrayList<PrivateMessage>) dbManager.queryPrivate(Integer.parseInt(temp));
        // 初始化个人信息列表
        privateMessageAdapter = new PrivateMessageAdapter(pri, R.layout.layout_mymsg, this);
        lv_personmsg.setAdapter(privateMessageAdapter);

        // 初始化系统信息列表
        systemMessageAdapter = new SystemMessageAdapter(data, R.layout.layout_mymsg, this);

        lv_systemmsg.setAdapter(systemMessageAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String temp = getSharedPreferences("user_id", Context.MODE_PRIVATE).getString("id", "0");
        data = (ArrayList<SystemMessage>) dbManager.querySystem(Integer.parseInt(temp));
        systemMessageAdapter.refresh(data);

        pri = (ArrayList<PrivateMessage>) dbManager.queryPrivate(Integer.parseInt(temp));
        privateMessageAdapter.refresh(pri);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.mymessage));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyMessageActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initTab() {
        tabHost = (TabHost) findViewById(R.id.tabHost_message);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(getResources().getString(R.string.personmsg), null).setContent(R.id.layout_personmsg));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator(getResources().getString(R.string.systemmsg), null).setContent(R.id.layout_systemmsg));

        TabWidget tabWidget = tabHost.getTabWidget();
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(15);
            tv.setTextColor(getResources().getColor(R.color.black));
            View view = tabWidget.getChildAt(i);
            view.setBackgroundResource(R.drawable.tab_selector);
        }
    }

    public void setRefresh() {
        refreshLayout1 = (SwipeRefreshLayout) findViewById(R.id.layout_refreshlayout1);
        // 个人消息下拉刷新事件
        refreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onRefresh() {
                refreshLayout1.setRefreshing(true);
                toolbar_title.setText("正在载入...");
                toolbar_navigationIcon.setBackground(null);
                toolbar_navigationIcon.setClickable(false);

                String temp = getSharedPreferences("user_id", Context.MODE_PRIVATE).getString("id", "0");
                pri = (ArrayList<PrivateMessage>)dbManager.queryPrivate(Integer.parseInt(temp));
                privateMessageAdapter.refresh(pri);

                refreshLayout1.setRefreshing(false);
                toolbar_title.setText(getResources().getString(R.string.mymessage));
                toolbar_navigationIcon.setBackgroundResource(R.mipmap.back);
                toolbar_navigationIcon.setClickable(true);


            }
        });
        refreshLayout1.setColorSchemeResources(R.color.orange, R.color.blue,
                android.R.color.holo_green_light, R.color.blue2);

        // 系统消息下拉刷新事件
        refreshLayout2 = (SwipeRefreshLayout) findViewById(R.id.layout_refreshlayout2);
        refreshLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onRefresh() {
                refreshLayout2.setRefreshing(true);
                toolbar_title.setText("正在载入...");
                toolbar_navigationIcon.setBackground(null);
                toolbar_navigationIcon.setClickable(false);

                String temp = getSharedPreferences("user_id", Context.MODE_PRIVATE).getString("id", "0");
                data = (ArrayList<SystemMessage>) dbManager.querySystem(Integer.parseInt(temp));
                systemMessageAdapter.refresh(data);

                refreshLayout2.setRefreshing(false);
                toolbar_title.setText(getResources().getString(R.string.mymessage));
                toolbar_navigationIcon.setBackgroundResource(R.mipmap.back);
                toolbar_navigationIcon.setClickable(true);
            }
        });
        refreshLayout2.setColorSchemeResources(R.color.orange, R.color.blue,
                android.R.color.holo_green_light, R.color.blue2);
    }

    private void init() {
        initToolbar();
        initTab();
        lv_personmsg = (ListView) findViewById(R.id.lv_personmsg);
        lv_systemmsg = (ListView) findViewById(R.id.lv_systemmsg);
        setRefresh();
    }


}
