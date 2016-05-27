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

/**
 * Created by UWTH on 2015/10/24.
 */
public class UserhelpFreshmenActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private LinearLayout linearLayouts[]=new LinearLayout[8];
    private LinearLayout title[] = new LinearLayout[8];
    private ImageView imageButtons[] = new ImageView[8];
    private boolean flag[] = new boolean[8];

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhelp_freshmen);
        init();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.freshmen));
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
        linearLayouts[0]=(LinearLayout)findViewById(R.id.newuserLayout1);
        linearLayouts[1]=(LinearLayout)findViewById(R.id.newuserLayout2);
        linearLayouts[2]=(LinearLayout)findViewById(R.id.newuserLayout3);
        linearLayouts[3]=(LinearLayout)findViewById(R.id.newuserLayout4);
        linearLayouts[4]=(LinearLayout)findViewById(R.id.newuserLayout5);
        linearLayouts[5]=(LinearLayout)findViewById(R.id.newuserLayout6);
        linearLayouts[6]=(LinearLayout)findViewById(R.id.newuserLayout7);
        linearLayouts[7]=(LinearLayout)findViewById(R.id.newuserLayout8);

        imageButtons[0]=(ImageView)findViewById(R.id.newuserButton1);
        imageButtons[1]=(ImageView)findViewById(R.id.newuserButton2);
        imageButtons[2]=(ImageView)findViewById(R.id.newuserButton3);
        imageButtons[3]=(ImageView)findViewById(R.id.newuserButton4);
        imageButtons[4]=(ImageView)findViewById(R.id.newuserButton5);
        imageButtons[5]=(ImageView)findViewById(R.id.newuserButton6);
        imageButtons[6]=(ImageView)findViewById(R.id.newuserButton7);
        imageButtons[7]=(ImageView)findViewById(R.id.newuserButton8);

        title[0] = (LinearLayout) findViewById(R.id.layout_title1);
        title[1] = (LinearLayout) findViewById(R.id.layout_title2);
        title[2] = (LinearLayout) findViewById(R.id.layout_title3);
        title[3] = (LinearLayout) findViewById(R.id.layout_title4);
        title[4] = (LinearLayout) findViewById(R.id.layout_title5);
        title[5] = (LinearLayout) findViewById(R.id.layout_title6);
        title[6] = (LinearLayout) findViewById(R.id.layout_title7);
        title[7] = (LinearLayout) findViewById(R.id.layout_title8);
    }

    private void init() {
        initToolbar();
        findView();
        setFlag();
        clickListener();
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().get("from").equals("credits")) {
                setTitleCheck(6);
                setTitleCheck(7);
            } else if (getIntent().getExtras().get("from").equals("coin")) {
                setTitleCheck(3);
                setTitleCheck(4);
                setTitleCheck(5);
            }
        }

    }

    private void setTitleCheck(int i) {
        imageButtons[i].setImageResource(R.mipmap.down);
        linearLayouts[i].setVisibility(View.VISIBLE);
        flag[i] = true;
    }

    private void setTitleUncheck(int i) {
        imageButtons[i].setImageResource(R.mipmap.right);
        linearLayouts[i].setVisibility(View.GONE);
        flag[i] = false;
    }

    private void setFlag() {
        for (int i = 0; i < 8; ++i) {
            flag[i] = false;
        }
    }

    private void clickListener() {
        title[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[0]) {
                    setTitleUncheck(0);
                } else {
                    setTitleCheck(0);
                }
            }
        });
        title[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[1]) {
                    setTitleUncheck(1);
                } else {
                    setTitleCheck(1);
                }
            }
        });
        title[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[2]) {
                    setTitleUncheck(2);
                } else {
                    setTitleCheck(2);
                }
            }
        });
        title[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[3]) {
                    setTitleUncheck(3);
                } else {
                    setTitleCheck(3);
                }
            }
        });
        title[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[4]) {
                    setTitleUncheck(4);
                } else {
                    setTitleCheck(4);
                }
            }
        });
        title[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[5]) {
                    setTitleUncheck(5);
                }
                else {
                    setTitleCheck(5);
                }
            }
        });
        title[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[6]) {
                    setTitleUncheck(6);
                } else {
                    setTitleCheck(6);
                }
            }
        });
        title[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[7]) {
                    setTitleUncheck(7);
                } else {
                    setTitleCheck(7);
                }
            }
        });
    }
}
