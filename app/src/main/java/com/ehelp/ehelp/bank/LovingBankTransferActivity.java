package com.ehelp.ehelp.bank;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.ehelp.ehelp.R;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LovingBankTransferActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;

    private SharedPreferences sharedPref;

    MaterialDialog dialog;

    public class User{
        String name;
        int id;
    }

    final List<User> userlist = new ArrayList<>();

    final List<String> strsss = new ArrayList<>();

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lovingbanktransfer);
        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);

        lv = (ListView) findViewById(R.id.trans_account);
        lv.setAdapter(new ArrayAdapter<String>(this, R.layout.layout_loving_transfer, strsss));


        init();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(LovingBankTransferActivity.this, LovingBankTransferChoose.class);
                intent.putExtra("id", userlist.get(arg2).id);
                intent.putExtra("name", userlist.get(arg2).name);
                startActivity(intent);
                finish();
            }
        });
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
                Intent intent = new Intent(LovingBankTransferActivity.this, LovingBankActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init(){
        initToolbar();
        loadData();
    }


    private void loadData() {
        dialog = new MaterialDialog.Builder(this)
                .theme(Theme.LIGHT)
                .title("正在加载")
                .content("请稍候...")
                .progress(true, 0)
                .cancelable(false)
                .show();

        String urlString = Config.HOST;
        String location = "/android/lovingbank/choose_transfer";

        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            jo.put("id", Integer.parseInt(sharedPref.getString("id", "none")));
            jo.put("state", 0);
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
                            JSONArray jsonArray = jsonObject.getJSONArray("user_list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                User user = new User();
                                user.id = jsonArray.getJSONObject(i).getInt("id");
                                user.name = jsonArray.getJSONObject(i).getString("nickname");
                                userlist.add(user);
                            }
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                showData();
                dialog.cancel();
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();
    }

    private void showData(){
        for(int i = 0; i < userlist.size(); i++){
            strsss.add(userlist.get(i).name);
        }
        lv.setAdapter(new ArrayAdapter<String>(this, R.layout.layout_loving_transfer, strsss));
    }


}
