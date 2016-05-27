package com.ehelp.ehelp.sponsorhelp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.square.SquareActivity;
import com.ehelp.ehelp.util.EventItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.ehelp.ehelp.util.HideSoftKeyboard.hideSoftKeyboard;

/**
 * Created by UWTH on 2015/10/24.
 */
public class SponsorHelpActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private Button toolbar_icon;
    private EditText et_title;
    private EditText et_content;
    private TextView tv_outofrange;
    private EditText et_coin;
    private ImageView iv_coin;
    private ImageView iv_peopleneed;
    private EditText et_peopleneed;
    private SharedPreferences sharedPref;
    private boolean uploading;

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsorhelp);
        setupUI(findViewById(R.id.layout_sponsorhelp));
        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        uploading = false;
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
                            et_coin.requestFocus();
                        } else {
                            if (et_peopleneed.hasFocus()) {
                                et_coin.requestFocus();
                            } else {
                                et_content.requestFocus();
                            }
//                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            if (imm != null) {
//                                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
//                            }
                        }
                    } else if (y2 - y1 > 50) {  // 向下滑
                        if (et_content.hasFocus()) {
                            et_title.requestFocus();
                        } else {
                            et_content.requestFocus();
//                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            if (imm != null) {
//                                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
//                            }
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
        toolbar_title.setText(getResources().getString(R.string.help));
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
        // 设置右边按钮
        toolbar_icon = (Button) findViewById(R.id.toolbar_icon);
        toolbar_icon.setBackgroundResource(R.drawable.sendicon_selector);
        toolbar_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_title.getText().length() == 0 || et_title.getText() == null) {
                    Toast.makeText(SponsorHelpActivity.this, "请填写标题", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_title.getText().length() > 14) {
                    Toast.makeText(SponsorHelpActivity.this, "标题超过字数限制", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_content.getText().length() > 100) {
                    Toast.makeText(SponsorHelpActivity.this, "描述超过字数限制", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_peopleneed.getText().length() == 0 || et_peopleneed.getText() == null) {
                    Toast.makeText(SponsorHelpActivity.this, "请填写所需帮助者人数", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_coin.getText().length() == 0 || et_coin.getText() == null) {
                    Toast.makeText(SponsorHelpActivity.this, "请填写悬赏爱心币", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!uploading)
                HelpLaunch();
            }
        });
    }

    private void HelpLaunch() {
        // 目标URL
        uploading = true;
        String urlString = Config.HOST;
        String location = "/android/help_launch";
        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();

        try {
            jo.put("id", sharedPref.getString("id", "none"));
            jo.put("type", 1);
            jo.put("longitude", MainActivity.longitude);
            jo.put("latitude", MainActivity.latitude);
            jo.put("title", et_title.getText().toString());
            jo.put("content", et_content.getText().toString());
            jo.put("demand_number", et_peopleneed.getText().toString());
            jo.put("love_coin", et_coin.getText().toString());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
            @Override
            public void onResponse(Object content) {
                if (content != null) {
                    String str = ((BackPack) content).getBody();
//                    Toast.makeText(SponsorHelpActivity.this, str, Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        if (jsonObject.getString("status").equals("200")) {

                            editor.putInt("event_id", jsonObject.getInt("event_id"));
                            editor.putInt("type", 1);
                            editor.commit();
                            Intent intent = new Intent(SponsorHelpActivity.this, MainActivity.class);
                            MainActivity.ishelping = true;
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SponsorHelpActivity.this, "求助失败", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                uploading = false;
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();
    }

    private void findView() {
        et_title = (EditText) findViewById(R.id.et_title);
        et_content = (EditText) findViewById(R.id.et_content);
        tv_outofrange = (TextView) findViewById(R.id.tv_outofrange);
        et_coin = (EditText) findViewById(R.id.et_coin);
        iv_coin = (ImageView) findViewById(R.id.iv_coin);
        iv_peopleneed = (ImageView) findViewById(R.id.iv_peopleneed);
        et_peopleneed = (EditText) findViewById(R.id.et_peopleneed);
    }

    private void init() {
        initToolbar();
        findView();

        et_title.addTextChangedListener(titleTextWatcher);
        et_content.addTextChangedListener(contentTextWatcher);
        et_coin.addTextChangedListener(coinTextWatcher);
        et_peopleneed.addTextChangedListener(peopleneedTextWatcher);
    }

    private TextWatcher titleTextWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (et_title.getText().length() > 14) {
                tv_outofrange.setText("标题请在14字以内");
                tv_outofrange.setVisibility(View.VISIBLE);
            } else {
                if (et_content.getText().length() > 100) {
                    tv_outofrange.setText("描述请在100字以内");
                    tv_outofrange.setVisibility(View.VISIBLE);
                } else {
                    tv_outofrange.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    private TextWatcher contentTextWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (et_content.getText().length() > 100 && et_title.getText().length() <= 14) {
                tv_outofrange.setText("描述请在100字以内");
                tv_outofrange.setVisibility(View.VISIBLE);
            } else if (et_title.getText().length() <= 14) {
                tv_outofrange.setVisibility(View.INVISIBLE);
            }
        }
    };

    private TextWatcher peopleneedTextWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (et_peopleneed.getText().length() == 0 || et_peopleneed.getText() == null) {
                iv_peopleneed.setImageDrawable(getResources().getDrawable(R.mipmap.people0));
            } else {
                iv_peopleneed.setImageDrawable(getResources().getDrawable(R.mipmap.people1));
            }
        }
    };

    private TextWatcher coinTextWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (et_coin.getText().length() == 0 || et_coin.getText() == null) {
                iv_coin.setImageDrawable(getResources().getDrawable(R.mipmap.coin1));
            } else {
                iv_coin.setImageDrawable(getResources().getDrawable(R.mipmap.coin0));
            }
        }
    };

    public void setupUI(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(SponsorHelpActivity.this);
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
}
