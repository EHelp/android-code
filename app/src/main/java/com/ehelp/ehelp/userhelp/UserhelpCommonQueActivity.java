package com.ehelp.ehelp.userhelp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.main.MainActivity;

/**
 * Created by UWTH on 2015/10/24.
 */
public class UserhelpCommonQueActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private LinearLayout linearLayouts[]=new LinearLayout[5];
    private LinearLayout title[] = new LinearLayout[5];
    private ImageView imageButtons[]=new ImageView[5];
    private boolean flag[]=new boolean[5];

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhelp_commonque);
        init();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.commonque));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserhelpCommonQueActivity.this, UserhelpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findView() {
        linearLayouts[0]=(LinearLayout)findViewById(R.id.newuserLayout1);
        linearLayouts[1]=(LinearLayout)findViewById(R.id.newuserLayout2);
        linearLayouts[2]=(LinearLayout)findViewById(R.id.newuserLayout3);
        linearLayouts[3]=(LinearLayout)findViewById(R.id.newuserLayout4);
        linearLayouts[4]=(LinearLayout)findViewById(R.id.newuserLayout5);

        imageButtons[0]=(ImageView)findViewById(R.id.newuserButton1);
        imageButtons[1]=(ImageView)findViewById(R.id.newuserButton2);
        imageButtons[2]=(ImageView)findViewById(R.id.newuserButton3);
        imageButtons[3]=(ImageView)findViewById(R.id.newuserButton4);
        imageButtons[4]=(ImageView)findViewById(R.id.newuserButton5);

        title[0] = (LinearLayout) findViewById(R.id.layout_title1);
        title[1] = (LinearLayout) findViewById(R.id.layout_title2);
        title[2] = (LinearLayout) findViewById(R.id.layout_title3);
        title[3] = (LinearLayout) findViewById(R.id.layout_title4);
        title[4] = (LinearLayout) findViewById(R.id.layout_title5);
    }

    private void init() {
        initToolbar();
        findView();
        setFlag();
        clickListener();
    }

    private void setFlag() {
        for (int i = 0; i < 5; ++i) {
            flag[i] = false;
        }
    }

    private void clickListener() {
        title[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[0]) {
                    imageButtons[0].setImageResource(R.mipmap.right);
                    linearLayouts[0].setVisibility(View.GONE);
                    flag[0] = false;
                } else {
                    imageButtons[0].setImageResource(R.mipmap.down);
                    linearLayouts[0].setVisibility(View.VISIBLE);
                    flag[0] = true;
                }
            }
        });
        title[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[1]) {
                    imageButtons[1].setImageResource(R.mipmap.right);
                    linearLayouts[1].setVisibility(View.GONE);
                    flag[1] = false;
                } else {
                    imageButtons[1].setImageResource(R.mipmap.down);
                    linearLayouts[1].setVisibility(View.VISIBLE);
                    flag[1] = true;
                }
            }
        });
        title[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[2]) {
                    imageButtons[2].setImageResource(R.mipmap.right);
                    linearLayouts[2].setVisibility(View.GONE);
                    flag[2] = false;
                } else {
                    imageButtons[2].setImageResource(R.mipmap.down);
                    linearLayouts[2].setVisibility(View.VISIBLE);
                    flag[2] = true;
                }
            }
        });
        title[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[3]) {
                    imageButtons[3].setImageResource(R.mipmap.right);
                    linearLayouts[3].setVisibility(View.GONE);
                    flag[3] = false;
                } else {
                    imageButtons[3].setImageResource(R.mipmap.down);
                    linearLayouts[3].setVisibility(View.VISIBLE);
                    flag[3] = true;
                }
            }
        });
        title[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[4]) {
                    imageButtons[4].setImageResource(R.mipmap.right);
                    linearLayouts[4].setVisibility(View.GONE);
                    flag[4] = false;
                } else {
                    imageButtons[4].setImageResource(R.mipmap.down);
                    linearLayouts[4].setVisibility(View.VISIBLE);
                    flag[4] = true;
                }
            }
        });
    }
}
