
package com.ehelp.ehelp.bank;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.ehelp.ehelp.R;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class LovingBankTransferChoose extends AppCompatActivity {

    //显示转账人姓名
    private TextView transaccount;
    private Button bt;

    private android.support.v7.widget.Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private EditText trans_count;

    private SharedPreferences sharedPref;
    MaterialDialog dialog;

    String name;
    int rid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lovingbank_transferchoose);
        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        rid = bundle.getInt("id");
        name = bundle.getString("name");

        transaccount = (TextView) findViewById(R.id.trans_account);
        bt = (Button) findViewById(R.id.trans_sure);
        trans_count = (EditText) findViewById(R.id.trans_count);
        //此处应该从数据库读数据
        transaccount.setText(name);
        init();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

    }

    private void initToolbar() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.title_activity_transfer));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LovingBankTransferChoose.this, LovingBankTransferActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init(){
        initToolbar();
    }

    private void loadData() {
        dialog = new MaterialDialog.Builder(this)
                .theme(Theme.LIGHT)
                .title("正在转账")
                .content("请稍候...")
                .progress(true, 0)
                .cancelable(false)
                .show();

        int c = Integer.parseInt(trans_count.getText().toString());

        String urlString = Config.HOST;
        String location = "/android/lovingbank/handler_transfer";

        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            jo.put("sender", Integer.parseInt(sharedPref.getString("id", "none")));
            jo.put("receiver", rid);
            jo.put("love_coin", c);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
            @Override
            public void onResponse(Object content) {
                if (content != null) {
                    String str = ((BackPack) content).getBody();
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        if (jsonObject.getString("status").equals("200")) {
                            dialog.cancel();
                            finish();
                            EventBus.getDefault().post(new RefreshEvent());
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                dialog.cancel();
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();
    }
}
