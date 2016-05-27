package com.ehelp.ehelp.bank;

/**
 * Created by benson on 2015/11/26.
 */


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LovingBankServiceActivity extends AppCompatActivity {

    //数据
    private String temp = "爱心币";
    private String[] service_names=new String[]{"东北大米","金龙花生油","收拾家务"};
    private String[] coin_count=new String[]{temp+"30",temp+"30",temp+"30"};


    private int[] imageIds=new int[]{R.mipmap.service_bg,R.mipmap.service_bg,R.mipmap.service_bg};

    private ListView lv;
   // private Button bt;

    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;

    private TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lovingbank_service);

        ArrayList<HashMap<String, Object>> listItems=new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < service_names.length; i++) {
            HashMap<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("ServiceImage", imageIds[i]);
            listItem.put("ServiceName", service_names[i]);
            listItem.put("CoinCount",coin_count[i]);
            listItems.add(listItem);
        }

        lv = (ListView)findViewById(R.id.service_list);

        SimpleAdapter simpleAdapter=new SimpleAdapter(this, listItems, R.layout.layout_lovingbank_serviceitem,
                new String[]{"ServiceImage","ServiceName","CoinCount"},
                new int[]{R.id.service_image,R.id.service_name, R.id.coin_count});


        lv.setAdapter(simpleAdapter);

        init();



    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.service));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LovingBankServiceActivity.this, LovingBankActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void init(){
        initToolbar();
    }
}
