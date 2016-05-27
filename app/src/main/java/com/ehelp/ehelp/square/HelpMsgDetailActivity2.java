package com.ehelp.ehelp.square;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
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
import com.ehelp.ehelp.sponsorhelp.EvaluateListActivity;
import com.ehelp.ehelp.util.CircleImageView;
import com.ehelp.ehelp.util.MyCustomDialog5;
import com.ehelp.ehelp.util.NoScrollListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by UWTH on 2015/10/25.
 */
public class HelpMsgDetailActivity2 extends AppCompatActivity implements GeocodeSearch.OnGeocodeSearchListener {
    private int launcher_id;

    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private SharedPreferences sharedPref;

    private CircleImageView iv_head;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_time;
    private TextView tv_title;
    private TextView tv_address;
    private TextView tv_description;
    private Button btn_cancelhelp;
    private ImageView iv_phone;
    private ImageView iv_sex;
    private ImageView iv_creditvalue;
    private TextView tv_people;
    private TextView tv_realname;
    private TextView tv_coinnum;

    // 需要加粗的TextView
    private TextView tv_name2;
    private TextView tv_phone2;
    private TextView tv_time2;
    private TextView tv_people2;
    private TextView tv_realname2;
    private TextView tv_address2;


    private LinearLayout layout_hastitle;
    private Handler handler;
    private int support_num;
    private int demand_num;
    private GeocodeSearch geocoderSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpmsg_detail);
        init();
        getInfo();
    }

    private void findView() {
        iv_head = (CircleImageView) findViewById(R.id.iv_head);
        tv_name = (TextView) findViewById(R.id.tv_nickname);
        layout_hastitle = (LinearLayout) findViewById(R.id.layout_hastitle);
        tv_people = (TextView) findViewById(R.id.tv_people);
        tv_coinnum = (TextView) findViewById(R.id.tv_coinnum);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_description = (TextView) findViewById(R.id.tv_description);
        btn_cancelhelp = (Button) findViewById(R.id.btn_gohelp);
        iv_phone = (ImageView) findViewById(R.id.iv_phone);
        tv_address = (TextView) findViewById(R.id.tv_address);
        iv_sex = (ImageView) findViewById(R.id.iv_sex);
        iv_creditvalue = (ImageView) findViewById(R.id.iv_creditlevel);
        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);
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
                finish();
            }
        });
    }

    private void init() {
        findView();
        initToolbar();

        geocoderSearch = new GeocodeSearch(HelpMsgDetailActivity2.this);
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
        tv_address2 = (TextView) findViewById(R.id.tv_address2);
        tv_address2.getPaint().setFakeBoldText(true);
        tv_realname2 = (TextView) findViewById(R.id.tv_realname2);
        tv_realname = (TextView) findViewById(R.id.tv_realname);
        tv_realname2.getPaint().setFakeBoldText(true);

        btn_cancelhelp.setText("放弃帮助");
        support_num = 0;
        demand_num = 0;
        if (getIntent().getIntExtra("type", -1) == 2) {
            layout_hastitle.setVisibility(View.GONE);
        } else {
            layout_hastitle.setVisibility(View.VISIBLE);
        }

        btn_cancelhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomDialog5 dialog = new MyCustomDialog5(HelpMsgDetailActivity2.this, "确认放弃此次帮助？",
                        new MyCustomDialog5.OnCustomDialogListener() {
                            @Override
                            public void back(boolean flag) {
                                if (flag) {
                                    CancelHelp();
                                }
                            }
                        });
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

            }
        });


        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpMsgDetailActivity2.this, UserMsgActivity.class);
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

        handler = new Handler() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == -1) {
                    if (sharedPref.getString("status", "").equals("200")) {
                        Toast.makeText(HelpMsgDetailActivity2.this, "获取事件详情成功", Toast.LENGTH_SHORT).show();

                    } else {
//                        Toast.makeText(HelpMsgDetailActivity2.this, String.valueOf(getIntent().getIntExtra("event_id", -1)), Toast.LENGTH_SHORT).show();
//                        getInfo();
                    }
                } else if (msg.what == 0) {
                    if (sharedPref.getString("status", "").equals("200")) {
                        Toast.makeText(HelpMsgDetailActivity2.this, "您放弃帮助", Toast.LENGTH_SHORT).show();
                        MainActivity.isgoinghelp = false;
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.remove("event_id");
                        editor.commit();
                        Intent intent = new Intent(HelpMsgDetailActivity2.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(HelpMsgDetailActivity2.this, "放弃帮助失败，请检查是否已经放弃", Toast.LENGTH_SHORT).show();
                    }
                }
                super.handleMessage(msg);
            }
        };
        Bundle bundle = getIntent().getExtras();

        tv_people.setText(bundle.getInt("helper") + "/1");
        tv_address.setText(bundle.getString("location"));
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
            jo.put("event_id", sharedPref.getInt("event_id", -1));


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
            @Override
            public void onResponse(Object content) {
                if (content != null) {
                    String str = ((BackPack) content).getBody();
//                    Toast.makeText(HelpMsgDetailActivity2.this, str, Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        if (sharedPref.getString("status", "none").equals("200")) {
                            launcher_id = jsonObject.getInt("launcher_id");
                            tv_name.setText(jsonObject.getString("launcher_name"));
                            tv_phone.setText(jsonObject.getString("contact"));
                            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("time"));
                            String time = new SimpleDateFormat("MM-dd HH:mm").format(date);
                            tv_time.setText(time);
                            support_num = jsonObject.getInt("support_number");
                            demand_num = jsonObject.getInt("demand_number");
                            tv_people.setText(support_num + "/" + demand_num);
//                            tv_title.setText(jsonObject.getString("title"));
//                            if (getIntent().getIntExtra("type", -1) == 2) {
//                                tv_description.setText("这是紧急求救，求救者可能没有时间填写求救详情信息!");
//                            } else {
//                                tv_description.setText(jsonObject.getString("content"));
//                            }
//                            tv_coinnum.setText(jsonObject.getInt("love_coin"));
                            if (jsonObject.getInt("type") != 2) {
                                tv_title.setText(jsonObject.getString("title"));
                                tv_description.setText(jsonObject.getString("content"));
                                tv_coinnum.setText(jsonObject.getString("love_coin"));
                            } else {
                                tv_description.setText("这是紧急求救，求救者可能没有时间填写求救详情信息!");
                            }
                            if (jsonObject.getInt("gender") == 1) {
                                iv_sex.setImageResource(R.mipmap.male);
                            } else {
                                iv_sex.setImageResource(R.mipmap.female);
                            }


//latLonPoint参数表示一个Latlng，第二参数表示范围多少米，GeocodeSearch.AMAP表示是国测局坐标系还是GPS原生坐标系
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

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // must store the new intent unless getIntent() will return the old one
        setIntent(intent);
    }

    private void CancelHelp() {

        // 目标URL
        String urlString = Config.HOST;
        String location = "/android/cancel_support";
        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();

        try {
            jo.put("id", sharedPref.getString("id", "none"));
            jo.put("launcher_id", launcher_id);
            jo.put("event_id", sharedPref.getInt("event_id", -1));
            jo.put("type", sharedPref.getInt("type", -1));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
            @Override
            public void onResponse(Object content) {
                if (content != null) {
                    String str = ((BackPack) content).getBody();
//                            Toast.makeText(HelpMsgDetailActivity2.this, str, Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
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
}
