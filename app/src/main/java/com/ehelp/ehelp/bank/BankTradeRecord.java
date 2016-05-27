package com.ehelp.ehelp.bank;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by benson on 2015/11/20.
 */
public class BankTradeRecord extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private TabHost tabHost;
    private ListView lv_exchageRecord;
    private ListView lv_eventRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loving_trade);

        init();

        //初始化事件交易列表
        SimpleAdapter eventAdapter = new SimpleAdapter(this, getEventRecord(), R.layout.layout_loving_tradingrecord,
                new String[]{"message", "time", "coin"},
                new int[]{R.id.message, R.id.time, R.id.tradeCoin}) {
        };
        lv_eventRecord.setAdapter(eventAdapter);

        //初始化转账交易列表
        SimpleAdapter exchangeAdapter = new SimpleAdapter(this, getExchangeRecord(), R.layout.layout_loving_tradingrecord,
                new String[]{"message", "time", "coin"},
                new int[]{R.id.message, R.id.time, R.id.tradeCoin}) {
        };
        lv_exchageRecord.setAdapter(exchangeAdapter);

    }
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.recordmes));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BankTradeRecord.this, LovingBankActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initTab(){
        tabHost = (TabHost) findViewById(R.id.tabHost_message);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(getResources().getString(R.string.eventtraderecord), null).setContent(R.id.layout_eventrecordmsg));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator(getResources().getString(R.string.exchangetraderecord), null).setContent(R.id.layout_exchangerecordmsg));

        TabWidget tabWidget = tabHost.getTabWidget();
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(15);
            tv.setTextColor(getResources().getColor(R.color.black));
            View view = tabWidget.getChildAt(i);
            view.setBackgroundResource(R.drawable.tab_selector);
        }
    }
    private void init(){
        initToolbar();
        initTab();

        lv_eventRecord = (ListView) findViewById(R.id.lv_coin_trade);
        lv_exchageRecord = (ListView) findViewById(R.id.lv_coin_exchange);
    }

    public ArrayList<HashMap<String, Object>> getEventRecord() {
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> temp1 = new HashMap<String, Object>();
        temp1.put("message", "你响应的求助");
        temp1.put("time", "11-10");
        temp1.put("coin", "+￥15");
        data.add(temp1);

        HashMap<String, Object> temp2 = new HashMap<String, Object>();
        temp2.put("message", "你发出的求助");
        temp2.put("time", "11-9");
        temp2.put("coin", "-￥10");
        data.add(temp2);

        HashMap<String, Object> temp3 = new HashMap<String, Object>();
        temp3.put("message", "你响应的求助");
        temp3.put("time", "11-8");
        temp3.put("coin", "+￥8");
        data.add(temp3);

        HashMap<String, Object> temp4 = new HashMap<String, Object>();
        temp4.put("message", "你发出的求助");
        temp4.put("time", "11-7");
        temp4.put("coin", "-￥12");
        data.add(temp4);
        return data;
    }

    public ArrayList<HashMap<String, Object>> getExchangeRecord() {
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> temp1 = new HashMap<String, Object>();
        temp1.put("message", "陈先生");
        temp1.put("time", "11-10");
        temp1.put("coin", "+￥15");
        data.add(temp1);

        HashMap<String, Object> temp2 = new HashMap<String, Object>();
        temp2.put("message", "吴先生");
        temp2.put("time", "11-9");
        temp2.put("coin", "-￥10");
        data.add(temp2);

        HashMap<String, Object> temp3 = new HashMap<String, Object>();
        temp3.put("message", "李先生");
        temp3.put("time", "11-8");
        temp3.put("coin", "+￥8");
        data.add(temp3);
        return data;

    }
}
