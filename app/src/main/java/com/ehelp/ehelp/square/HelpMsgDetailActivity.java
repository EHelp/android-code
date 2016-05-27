package com.ehelp.ehelp.square;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.ehelp.ehelp.R;
import com.ehelp.ehelp.adapter.ReplyAdapter;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.personcenter.UserMsgActivity;
import com.ehelp.ehelp.sponsorhelp.HelperListActivity;
import com.ehelp.ehelp.util.ActivityCollector;
import com.ehelp.ehelp.util.CircleImageView;
import com.ehelp.ehelp.util.EventItem;
import com.ehelp.ehelp.util.NoScrollListView;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by UWTH on 2015/10/25.
 */
public class HelpMsgDetailActivity extends AppCompatActivity implements GeocodeSearch.OnGeocodeSearchListener {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private SharedPreferences sharedPref;
    private int launcher_id;
    private int event_id;
    private Handler handler;

    private CircleImageView iv_head;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_time;
    private TextView tv_title;
    private TextView tv_description;
    private Button btn_gohelp;
    private ImageView iv_phone;
    private TextView tv_people;
    private TextView tv_realname;
    private TextView tv_coinnum;
    private TextView tv_address;
    private ImageView iv_sex;
    private ImageView iv_creditvalue;
    private LinearLayout layout_hastitle;
    private int support_num;
    private int demand_num;
    private int type;
    // 需要加粗的TextView
    private TextView tv_name2;
    private TextView tv_phone2;
    private TextView tv_time2;
    private TextView tv_people2;
    private TextView tv_realname2;
    private TextView tv_address2;

    private GeocodeSearch geocoderSearch;

    private static boolean uploading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpmsg_detail);
        init();

    }

    @Override
    protected void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
        Log.d("TPush", "onResumeXGPushClickedResult:" + click);
        if (click != null) { // 判断是否来自信鸽的打开方式
            String str = click.getCustomContent();
            try {
                JSONObject jsonObject = new JSONObject(str);
                event_id = jsonObject.getInt("event_id");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getInfo();
    }

    private void findView() {
        iv_head = (CircleImageView) findViewById(R.id.iv_head);
        tv_name = (TextView) findViewById(R.id.tv_nickname);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_description = (TextView) findViewById(R.id.tv_description);
        btn_gohelp = (Button) findViewById(R.id.btn_gohelp);
        iv_phone = (ImageView) findViewById(R.id.iv_phone);
        tv_people = (TextView) findViewById(R.id.tv_people);
        tv_realname = (TextView) findViewById(R.id.tv_realname);
        tv_coinnum = (TextView) findViewById(R.id.tv_coinnum);
        layout_hastitle = (LinearLayout) findViewById(R.id.layout_hastitle);
        tv_address = (TextView) findViewById(R.id.tv_address);
        iv_sex = (ImageView) findViewById(R.id.iv_sex);
        iv_creditvalue = (ImageView) findViewById(R.id.iv_creditlevel);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        if (getIntent().getIntExtra("type", -1) == 2) {
            toolbar_title.setText("求救详情");
        } else {
            toolbar_title.setText("求助详情");
        }
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCollector.getSize() > 0) {
                    finish();
                } else {
                    Intent intent = new Intent(HelpMsgDetailActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void init() {
        findView();
        initToolbar();

        launcher_id = -1;
        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);

        geocoderSearch = new GeocodeSearch(HelpMsgDetailActivity.this);
        geocoderSearch.setOnGeocodeSearchListener(this);

        // 字体加粗
        tv_name2 = (TextView) findViewById(R.id.tv_nickname2);
        tv_name2.getPaint().setFakeBoldText(true);
        tv_phone2 = (TextView) findViewById(R.id.tv_phone2);
        tv_phone2.getPaint().setFakeBoldText(true);
        tv_time2 = (TextView) findViewById(R.id.tv_time2);
        tv_time2.getPaint().setFakeBoldText(true);
        tv_people2 = (TextView) findViewById(R.id.tv_people2);
        tv_people2.getPaint().setFakeBoldText(true);
        tv_realname2 = (TextView) findViewById(R.id.tv_realname2);
        tv_realname2.getPaint().setFakeBoldText(true);
        tv_address2 = (TextView) findViewById(R.id.tv_address2);
        tv_address2.getPaint().setFakeBoldText(true);

        if (getIntent().getIntExtra("type", -1) == 2) {
            layout_hastitle.setVisibility(View.GONE);
        } else {
            layout_hastitle.setVisibility(View.VISIBLE);
        }
        if (getIntent().getIntExtra("invisible", -1) == 1) {
            btn_gohelp.setVisibility(View.INVISIBLE);
        } else {
            btn_gohelp.setVisibility(View.VISIBLE);
        }

        btn_gohelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.ishelping) {
                    Toast.makeText(HelpMsgDetailActivity.this, "您正处于求助状态", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (MainActivity.issosing) {
                    Toast.makeText(HelpMsgDetailActivity.this, "您正处于求救状态", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (MainActivity.isgoinghelp) {
                    Toast.makeText(HelpMsgDetailActivity.this, "您正在进行一个救助任务", Toast.LENGTH_SHORT).show();
                    return;
                }
                GoHelp();
            }
        });


        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpMsgDetailActivity.this, UserMsgActivity.class);
                intent.putExtra("event_id", event_id);
                startActivity(intent);
            }
        });

        iv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tv_phone.getText().toString()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            tv_people.setText(bundle.getInt("helper") + "/1");
            tv_address.setText(MainActivity.recent_address);
        }

        handler = new Handler() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == -1) {
                    if (sharedPref.getString("status", "").equals("200")) {
                        Toast.makeText(HelpMsgDetailActivity.this, "获取事件详情成功", Toast.LENGTH_SHORT).show();

                    } else {
//                        Toast.makeText(HelpMsgDetailActivity.this, String.valueOf(getIntent().getIntExtra("event_id", -1)), Toast.LENGTH_SHORT).show();
//                        getInfo();
                    }
                } else if (msg.what == 0) {
                    if (sharedPref.getString("status", "").equals("200")) {
                        Toast.makeText(HelpMsgDetailActivity.this, "您成为帮助者", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(HelpMsgDetailActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        intent1.putExtra("username", tv_name.getText().toString());
                        intent1.putExtra("phone", tv_phone.getText().toString());
                        intent1.putExtra("address", tv_address.getText().toString());
                        intent1.putExtra("event_id", getIntent().getIntExtra("event_id", -1));
                        intent1.putExtra("support_num", support_num + 1);
                        intent1.putExtra("demand_num", demand_num);

                        MainActivity.goinghelp_address = tv_address.getText().toString();
                        MainActivity.isgoinghelp = true;
                        startActivity(intent1);
                    } else {
                        Toast.makeText(HelpMsgDetailActivity.this, "该事件已结束", Toast.LENGTH_SHORT).show();
                    }
                    HelpMsgDetailActivity.uploading = false;
                }
                super.handleMessage(msg);
            }
        };
    }

    private void getInfo() {

        // 目标URL
        String urlString = Config.HOST;
        String location = "/android/event_details";
        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        Bundle bundle = getIntent().getExtras();
        try {
            if (bundle.getInt("event_id", -1) != -1)
                event_id = bundle.getInt("event_id", -1);
            if (getIntent().getIntExtra("invisible", -1) == -1) {
                if (event_id == sharedPref.getInt("event_id", -1))
                    btn_gohelp.setVisibility(View.INVISIBLE);
                else
                    btn_gohelp.setVisibility(View.VISIBLE);
            }
            jo.put("event_id", event_id);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
            @Override
            public void onResponse(Object content) {
                if (content != null) {
                    String str = ((BackPack) content).getBody();
//                            Toast.makeText(HelpMsgDetailActivity.this, str, Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        if (sharedPref.getString("status", "none").equals("200")) {
                            launcher_id = jsonObject.getInt("launcher_id");
                            type = jsonObject.getInt("type");
                            tv_name.setText(jsonObject.getString("launcher_name"));
                            tv_phone.setText(jsonObject.getString("contact"));
                            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("time"));
                            String time = new SimpleDateFormat("MM-dd HH:mm").format(date);
                            tv_time.setText(time);
                            support_num = jsonObject.getInt("support_number");
                            demand_num = jsonObject.getInt("demand_number");
                            tv_people.setText(support_num + "/" + demand_num);
                            if (jsonObject.getInt("gender") == 1) {
                                iv_sex.setImageResource(R.mipmap.male);
                            } else {
                                iv_sex.setImageResource(R.mipmap.female);
                            }
                            if (jsonObject.getInt("type") != 2) {
                                tv_title.setText(jsonObject.getString("title"));
                                tv_description.setText(jsonObject.getString("content"));
                                tv_coinnum.setText(jsonObject.getString("love_coin"));
                            } else {
                                toolbar_title.setText("求救详情");
                                layout_hastitle.setVisibility(View.GONE);
                                tv_description.setText("这是紧急求救，求救者可能没有时间填写求救详情信息!");
                            }

                            RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(jsonObject.getDouble("latitude"),
                                    jsonObject.getDouble("longitude")), 200, GeocodeSearch.AMAP);
                            geocoderSearch.getFromLocationAsyn(query);

                            int temp = (int) jsonObject.getDouble("reputation");
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
                        }
                        handler.sendEmptyMessage(-1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();
    }

    private void GoHelp() {

        if (uploading) {
            Toast.makeText(HelpMsgDetailActivity.this, "您已经上传了帮助申请", Toast.LENGTH_SHORT).show();
            return;
        }

        // 目标URL
        String urlString = Config.HOST;
        String location = "/android/give_support";
        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();

        try {
            jo.put("id", sharedPref.getString("id", "none"));
            jo.put("launcher_id", launcher_id);
            jo.put("event_id", event_id);
            jo.put("type", type);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
            @Override
            public void onResponse(Object content) {
                if (content != null) {
                    String str = ((BackPack) content).getBody();
//                            Toast.makeText(HelpMsgDetailActivity.this, str, Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        if (jsonObject.getString("status").equals("200")) {
                            editor.putInt("event_id", event_id);
                            editor.putInt("type", type);
                            editor.commit();
                        }
                        handler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        String recent_address;
        if (i != 0) {
            recent_address = "未找到地址";
        } else {
            String city = regeocodeResult.getRegeocodeAddress().getCity();
            String district = regeocodeResult.getRegeocodeAddress().getDistrict();
            String neighborhood = regeocodeResult.getRegeocodeAddress().getNeighborhood();
            String building = regeocodeResult.getRegeocodeAddress().getBuilding();
            String crossroad = regeocodeResult.getRegeocodeAddress().getCrossroads().get(0).getFirstRoadName();
            String road = regeocodeResult.getRegeocodeAddress().getRoads().get(0).getName();
            String township = regeocodeResult.getRegeocodeAddress().getTownship();
            String formatAddress = regeocodeResult.getRegeocodeAddress().getFormatAddress();
            //Toast.makeText(this, city + district + neighborhood + road, Toast.LENGTH_SHORT).show();
            recent_address = city + district + neighborhood + road;
        }
        tv_address.setText(recent_address);
        // Toast.makeText(this, recent_address, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // must store the new intent unless getIntent() will return the old one
        setIntent(intent);
    }
}
