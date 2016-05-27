package com.ehelp.ehelp.mycontact;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.account.RegisterActivity;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.square.AskMsg;
import com.ehelp.ehelp.util.NullDealer;
import com.ehelp.ehelp.util.OccupationEncode;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by UWTH on 2015/11/14.
 */
public class UrgencySearchEhelpUserActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;

    private EditText et_phone;
    private TextView tv_search;
    private LinearLayout layout_usermsg;

    private TextView tv_nickname;
    private TextView tv_occupation;
    private TextView tv_reputation;
    private TextView tv_location;
    private TextView tv_verify;
    private TextView tv_realname;
    private ImageView iv_creditvalue;
    private ImageView iv_sex;

    private String phone;
    private int user_id;

    private SharedPreferences sharedPref;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchehelpuser);
        sharedPref = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        init();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("添加联系人");
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
        et_phone = (EditText) findViewById(R.id.et_phone);
        tv_search = (TextView) findViewById(R.id.tv_search);
        layout_usermsg = (LinearLayout) findViewById(R.id.layout_usermsg);
        tv_location = (TextView)findViewById(R.id.tv_location);
        tv_nickname = (TextView)findViewById(R.id.tv_nickname);
        tv_occupation = (TextView)findViewById(R.id.tv_occupation);
        tv_reputation = (TextView)findViewById(R.id.tv_creditvalue);
        tv_realname = (TextView)findViewById(R.id.tv_realname);
        tv_verify = (TextView)findViewById(R.id.tv_isCertification);
        iv_creditvalue = (ImageView)findViewById(R.id.iv_creditlevel);
        iv_sex = (ImageView)findViewById(R.id.iv_sex);
    }

    private void init() {
        findView();
        initToolbar();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search: {
                String phoneNum = et_phone.getText().toString().trim();
//                if (!RegisterActivity.isMobileNO(phoneNum)) {
//                    Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                getInfofromPhone(phoneNum);
                // 收起软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
                break;
            }
            case R.id.btn_add: {
                if (phone.equals(sharedPref.getString("phone", "none"))) {
                    Toast.makeText(this, "不能添加自己为紧急联系人！", Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent intent = new Intent(this, UrgencyConfirmMsgActivity.class);
                intent.putExtra("nickname", tv_nickname.getText().toString());
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                break;
            }
        }
    }

    public boolean isEhelpAccount(String num) {
        return true;
    }

    private void getInfofromPhone(final String phone) {
        String urlString = Config.HOST;
        String location = "/android/user/get_user_info_from_phone";

        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            jo.put("phone", phone);

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
                        Toast.makeText(UrgencySearchEhelpUserActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        if (sharedPref.getString("status", "").equals("200")) {
                            tv_nickname.setText(jsonObject.getString("nickname"));
                            tv_reputation.setText(jsonObject.getString("reputation"));
                            int temp = (int)jsonObject.getDouble("reputation");
                            switch (temp) {
                                case 0:
                                    iv_creditvalue.setImageResource(R.mipmap.creditlevel0);
                                    break;
                                case 1:
                                    iv_creditvalue.setImageResource(R.mipmap.creditlevel1);
                                    break;
                                case 2:
                                    iv_creditvalue.setImageResource(R.mipmap.creditlevel2);
                                    break;
                                case 3:
                                    iv_creditvalue.setImageResource(R.mipmap.creditlevel3);
                                    break;
                                case 4:
                                    iv_creditvalue.setImageResource(R.mipmap.creditlevel4);
                                    break;
                                case 5:
                                    iv_creditvalue.setImageResource(R.mipmap.creditlevel5);
                                    break;
                            }

                            tv_occupation.setText(OccupationEncode.OccupationToString(jsonObject.getInt("occupation")));
                            tv_location.setText(NullDealer.DealNull(jsonObject.getString("location")));
                            user_id = jsonObject.getInt("user_id");
                            UrgencySearchEhelpUserActivity.this.phone = phone;
                            layout_usermsg.setVisibility(View.VISIBLE);
                            if (jsonObject.getInt("is_verify") == 1) {
                                tv_realname.setText(jsonObject.getString("name"));
                            } else {
                                tv_realname.setText("未实名认证");
                            }
                            if (jsonObject.getInt("gender") == 1) {
                                iv_sex.setImageResource(R.mipmap.male);
                            } else {
                                iv_sex.setImageResource(R.mipmap.female);
                            }
                        } else {
                            layout_usermsg.setVisibility(View.INVISIBLE);
                            Toast.makeText(UrgencySearchEhelpUserActivity.this, "无此用户", Toast.LENGTH_SHORT).show();
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
