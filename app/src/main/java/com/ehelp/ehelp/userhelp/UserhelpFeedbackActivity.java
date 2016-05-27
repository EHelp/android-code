package com.ehelp.ehelp.userhelp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.settings.SettingsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ehelp.ehelp.util.HideSoftKeyboard.hideSoftKeyboard;

/**
 * Created by UWTH on 2015/11/21.
 */
public class UserhelpFeedbackActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;

    private Button btn_send;
    private EditText et_title;
    private EditText et_content;

    private SharedPreferences sharedPreferences;

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhelp_feedback);
        setupUI(findViewById(R.id.layout_feedback));
        sharedPreferences = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        init();
        setEditTextScrollEvent();
    }

    public void setEditTextScrollEvent() {
        et_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //当手指按下的时候
                    x1 = event.getX();
                    y1 = event.getY();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //当手指离开的时候
                    x2 = event.getX();
                    y2 = event.getY();
                    if (y1 - y2 > 50) {  // 向上滑
                        if (et_content.hasFocus()) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (imm != null) {
                                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                            }
                            et_content.clearFocus();
                        } else {
                            et_content.requestFocus();
                        }
                    }
                }
                return false;
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.feedback));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserhelpFeedbackActivity.this, UserhelpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findView() {
        btn_send = (Button) findViewById(R.id.btn_send);
        et_title = (EditText) findViewById(R.id.et_title);
        et_content = (EditText) findViewById(R.id.et_content);
    }

    private void init() {
        findView();
        initToolbar();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_title.getText().toString().trim();
                if (title.equals("")) {
                    Toast.makeText(UserhelpFeedbackActivity.this, "标题不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Feedback();
            }
        });
    }

    public void setupUI(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(UserhelpFeedbackActivity.this);
                    return false;
                }
            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private void Feedback() {
        String urlString = Config.HOST;
        String location = "/android/feedback";

        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            jo.put("title", et_title.getText().toString());
            jo.put("content", et_content.getText().toString());


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
            @Override
            public void onResponse(Object content) {
                if (content != null) {
                    String str = ((BackPack) content).getBody();

                    if (str == null) {
                        Toast.makeText(UserhelpFeedbackActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        if (sharedPreferences.getString("status", "").equals("200")) {
                            Toast.makeText(UserhelpFeedbackActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UserhelpFeedbackActivity.this, UserhelpFeedbackActivity2.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(UserhelpFeedbackActivity.this, "反馈失败", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();
    }
}
