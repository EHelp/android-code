package com.ehelp.ehelp.bank;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ehelp.ehelp.R;

import java.util.ArrayList;


public class LovingBankRecordActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;

    private TabLayout tab;
    private ViewPager vp;

    private ArrayList<Fragment> fragmentList;
    private ArrayList<String> titleList;

    private SharedPreferences sharedPref;

    MaterialDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lovingbankrecord);
        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);

        init();

    }

    private void initToolbar() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.choosetansferaccount));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LovingBankRecordActivity.this, LovingBankActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init(){
        initToolbar();
        initFragment();
        initViewPager();
    }

    private void initFragment() {
        fragmentList = new ArrayList<>();
        Fragment loginFragment = new Fragment1();
        Fragment registerFragment = new Fragment2();
        fragmentList.add(loginFragment);
        fragmentList.add(registerFragment);

        titleList = new ArrayList<>();
        titleList.add("转账交易记录");
        titleList.add("事件交易记录");
    }

    private void initViewPager() {
        tab = (TabLayout) findViewById(R.id.tab);
        vp = (ViewPager) findViewById(R.id.vp);

        MyFragmentPagerAdapter FragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, titleList);
        vp.setAdapter(FragmentPagerAdapter);
        tab.setupWithViewPager(vp);
        tab.setTabTextColors(Color.GRAY, Color.WHITE);//设置文本在选中和为选中时候的颜色
        tab.setSelectedTabIndicatorColor(getResources().getColor(R.color.orange));
        //tab.setSelectedTabIndicatorHeight(12);

        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }



}
