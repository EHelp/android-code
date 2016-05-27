package com.ehelp.ehelp.mycontact;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.account.RegisterActivity;
import com.ehelp.ehelp.main.MainActivity;

/**
 * Created by UWTH on 2015/11/15.
 */
public class AddUrgencyContactByPhoneActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;

    private EditText et_name;
    private EditText et_phone;
    private EditText et_code;
    private Button btn_finish;
    private RadioButton rb_sendcode;

    int i = 60;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addurgencycontactbyphone);
        init();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.addurgencycontact));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddUrgencyContactByPhoneActivity.this,
                        AddUrgencyContactActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findView() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        rb_sendcode = (RadioButton) findViewById(R.id.rb_sendcode);
    }

    private void init() {
        findView();
        initToolbar();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_sendcode: {
                if (!judgePhoneNums(et_phone.getText().toString())) {
                    rb_sendcode.setChecked(false);
                    return;
                }
                rb_sendcode.setClickable(false);
                rb_sendcode.setBackground(getResources().getDrawable(R.drawable.btn_bg13));
                rb_sendcode.setText("正在发送(" + i + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
                break;
            }
            case R.id.btn_finish: {
                String name = et_name.getText().toString().trim();
                if (name.equals("")) {
                    Toast.makeText(this, "请输入联系人称呼", Toast.LENGTH_SHORT).show();
                    return;
                }
                String phone = et_phone.getText().toString().trim();
                if (phone.equals("")) {
                    Toast.makeText(this, "请输入联系人手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = et_code.getText().toString().trim();
                if (code.equals("")) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(this, MyContactActivity.class);
                Toast.makeText(this, "成功添加紧急联系人", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
                break;
            }
        }
    }

    Handler handler = new Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                rb_sendcode.setText("正在发送(" + i + ")");
            } else if (msg.what == -8) {
                rb_sendcode.setChecked(false);
                rb_sendcode.setText("发送验证码");
                rb_sendcode.setClickable(true);
                rb_sendcode.setBackground(getResources().getDrawable(R.drawable.btn_bg5));
                i = 60;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (RegisterActivity.isMatchLength(phoneNums, 11)
                && RegisterActivity.isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
        return false;
    }
}
