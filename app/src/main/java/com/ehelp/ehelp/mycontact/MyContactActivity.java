package com.ehelp.ehelp.mycontact;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.account.LoginActivity;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.personcenter.UserMsgActivity;
import com.ehelp.ehelp.settings.SettingsActivity;
import com.ehelp.ehelp.square.HelpMsgDetailActivity;
import com.ehelp.ehelp.util.ActivityCollector;
import com.ehelp.ehelp.util.MyCustomDialog5;
import com.ehelp.ehelp.util.NoScrollListView;
import com.ehelp.ehelp.util.OccupationEncode;
import com.ehelp.ehelp.util.UserObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by UWTH on 2015/10/24.
 */
public class MyContactActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;

    //private TextView tv_hint;
    private NoScrollListView lv_family;
    private NoScrollListView lv_friend;
    private NoScrollListView lv_neighbor;
    private TextView tv_familynum;
    private TextView tv_friendnum;
    private TextView tv_neighbornum;
    private ImageView iv_switch1;
    private ImageView iv_switch2;
    private ImageView iv_switch3;
    private boolean isFamilyVisiable;
    private boolean isFriendVisiable;
    private boolean isNeighborVisiable;
    private Button btn_add;
    private SimpleAdapter familyadapter = null;
    private SimpleAdapter friendsadapter = null;
    private SimpleAdapter neibouradapter = null;
    private SharedPreferences sharedPref;
    private ArrayList<HashMap<String, Object>> family = new ArrayList<HashMap<String, Object>>();
    private ArrayList<HashMap<String, Object>> friends = new ArrayList<HashMap<String, Object>>();
    private ArrayList<HashMap<String, Object>> neibour = new ArrayList<HashMap<String, Object>>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycontact);
        sharedPref = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUrgencyContact();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.mycontact));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyContactActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findView() {
        lv_family = (NoScrollListView) findViewById(R.id.lv_family);
        lv_friend = (NoScrollListView) findViewById(R.id.lv_friend);
        lv_neighbor = (NoScrollListView) findViewById(R.id.lv_neighbor);
        tv_familynum = (TextView) findViewById(R.id.tv_familynum);
        tv_friendnum = (TextView) findViewById(R.id.tv_friendnum);
        tv_neighbornum = (TextView) findViewById(R.id.tv_neighbornum);
        btn_add = (Button) findViewById(R.id.btn_add);
        iv_switch1 = (ImageView) findViewById(R.id.iv_switch1);
        iv_switch2 = (ImageView) findViewById(R.id.iv_switch2);
        iv_switch3 = (ImageView) findViewById(R.id.iv_switch3);
    }

    private void initUrgencyContactList() {
        familyadapter = new SimpleAdapter(this, family, R.layout.layout_mycontact,
                new String[]{"head", "name"}, new int[]{R.id.iv_head, R.id.tv_nickname}) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final View view = super.getView(position, convertView, parent);
                LinearLayout layout_contact = (LinearLayout) view.findViewById(R.id.layout_contact);
                layout_contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MyContactActivity.this, UrgencyContactMsgActivity.class);
                        intent.putExtra("info", family.get(position));
                        startActivity(intent);
                    }
                });
                return view;
            }
        };
        lv_family.setAdapter(familyadapter);

        neibouradapter = new SimpleAdapter(this, neibour, R.layout.layout_mycontact,
                new String[]{"head", "name"}, new int[]{R.id.iv_head, R.id.tv_nickname}) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final View view = super.getView(position, convertView, parent);
                LinearLayout layout_contact = (LinearLayout) view.findViewById(R.id.layout_contact);
                layout_contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MyContactActivity.this, UrgencyContactMsgActivity.class);
                        intent.putExtra("info", neibour.get(position));
                        startActivity(intent);
                    }
                });
                return view;
            }
        };
        lv_neighbor.setAdapter(neibouradapter);

        friendsadapter = new SimpleAdapter(this, friends, R.layout.layout_mycontact,
                new String[]{"head", "name"}, new int[]{R.id.iv_head, R.id.tv_nickname}) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final View view = super.getView(position, convertView, parent);
                LinearLayout layout_contact = (LinearLayout) view.findViewById(R.id.layout_contact);
                layout_contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MyContactActivity.this, UrgencyContactMsgActivity.class);
                        intent.putExtra("info", friends.get(position));
                        startActivity(intent);
                    }
                });
                return view;
            }
        };
        lv_friend.setAdapter(friendsadapter);
    }

    private void init() {
        findView();
        initToolbar();
        initUrgencyContactList();
        isFamilyVisiable = true;
        isFriendVisiable = true;
        isNeighborVisiable = true;
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyContactActivity.this, UrgencySearchEhelpUserActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getUrgencyContact() {
//        urgencyContacts.clear();
        String urlString = Config.HOST;
        String location = "/android/user/get_static_relation";

        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            jo.put("id", sharedPref.getString("id", "none"));
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
                    if (str == null) {
                        Toast.makeText(MyContactActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    family.clear();
                    friends.clear();
                    neibour.clear();
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        if (sharedPref.getString("status", "").equals("200")) {

                            JSONArray jsonArray = jsonObject.getJSONArray("user_list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                HashMap<String, Object> temp = new HashMap<String, Object>();
                                UserObject data = new UserObject(jsonArray.getJSONObject(i));
                                temp.put("head", R.mipmap.head);
                                temp.put("name", data.getNickname());
                                temp.put("user_id", data.getId());
                                temp.put("realname", data.getName());
                                temp.put("occupation", OccupationEncode.OccupationToString(data.getOccupation()));
                                temp.put("creditvalue", data.getReputation());
                                temp.put("location", data.getLocation());
                                temp.put("gender", data.getGender());
                                temp.put("verify", data.getIs_verify());
                                temp.put("name", data.getNickname());
                                int relation = jsonArray.getJSONObject(i).getInt("relation_type");
                                if (relation == 0) {
                                    family.add(temp);
                                } else if (relation == 1) {
                                    neibour.add(temp);
                                } else {
                                    friends.add(temp);
                                }
                            }
                            familyadapter.notifyDataSetChanged();
                            tv_familynum.setText(family.size()+"人");
                            friendsadapter.notifyDataSetChanged();
                            tv_friendnum.setText(friends.size()+"人");
                            neibouradapter.notifyDataSetChanged();
                            tv_neighbornum.setText(neibour.size()+"人");
                        } else {
                            Toast.makeText(MyContactActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();
    }

    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.layout_family: {
                if (isFamilyVisiable) {
                    isFamilyVisiable = false;
                    lv_family.setVisibility(View.GONE);
                    iv_switch1.setImageResource(R.mipmap.right2);
                } else {
                    isFamilyVisiable = true;
                    lv_family.setVisibility(View.VISIBLE);
                    iv_switch1.setImageResource(R.mipmap.down2);
                }
                break;
            }
            case R.id.layout_friend: {
                if (isFriendVisiable) {
                    isFriendVisiable = false;
                    lv_friend.setVisibility(View.GONE);
                    iv_switch2.setImageResource(R.mipmap.right2);
                } else {
                    isFriendVisiable = true;
                    lv_friend.setVisibility(View.VISIBLE);
                    iv_switch2.setImageResource(R.mipmap.down2);
                }
                break;
            }
            case R.id.layout_neighbor: {
                if (isNeighborVisiable) {
                    isNeighborVisiable = false;
                    lv_neighbor.setVisibility(View.GONE);
                    iv_switch3.setImageResource(R.mipmap.right2);
                } else {
                    isNeighborVisiable = true;
                    lv_neighbor.setVisibility(View.VISIBLE);
                    iv_switch3.setImageResource(R.mipmap.down2);
                }
                break;
            }
        }
    }
}
