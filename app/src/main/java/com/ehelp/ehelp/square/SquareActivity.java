package com.ehelp.ehelp.square;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.ehelp.ehelp.R;
import com.ehelp.ehelp.adapter.AskMsgAdapter;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.myparti.MyPartiActivity;
import com.ehelp.ehelp.personcenter.UserMsgActivity;
import com.ehelp.ehelp.util.RefreshListView;
import com.ehelp.ehelp.util.SponsorEntryPopupWindow;
import com.ehelp.ehelp.util.SponsorEntryPopupWindow2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by UWTH on 2015/10/24.
 */
public class SquareActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private Button toolbar_icon;

    private TabHost tabHost;
    private RefreshListView lv_helpmsg;
    private RefreshListView lv_askmsg;
    private SponsorEntryPopupWindow2 menuWindow;
    private View transparentview;

    private ArrayList<AskMsg> askmsgData;
    private ArrayList<HashMap<String, Object>> helpmsgData;
    private ArrayList<HashMap<String, Object>> temphelpmsgData;
    private AskMsgAdapter askMsgAdapter;

    private SharedPreferences sharedPref;
    private SimpleAdapter simpleAdapter;
    private boolean loading_ask;
    private boolean loading_help;

    private int help_event_id;
    private int ask_event_id;

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square);
        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);

        loading_ask = false;
        loading_help = false;
        init();
        //setListViewScrollEvent();
    }

//    public void setListViewScrollEvent() {
//        lv_helpmsg.setLongClickable(true);
//        lv_helpmsg.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                //Toast.makeText(SquareActivity.this, "touch", Toast.LENGTH_SHORT).show();
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    //当手指按下的时候
//                    x1 = event.getX();
//                    y1 = event.getY();
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    //当手指离开的时候
//                    x2 = event.getX();
//                    y2 = event.getY();
//                    if(x2 - x1 > 50) {  // 向右滑
//                        //Toast.makeText(SquareActivity.this, "向右滑", Toast.LENGTH_SHORT).show();
//                        tabHost.setCurrentTab(0);
//                    }
//                }
//                return false;
//            }
//        });
//        lv_askmsg.setLongClickable(true);
//        lv_askmsg.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                //Toast.makeText(SquareActivity.this, "touch", Toast.LENGTH_SHORT).show();
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    //当手指按下的时候
//                    x1 = event.getX();
//                    y1 = event.getY();
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    //当手指离开的时候
//                    x2 = event.getX();
//                    y2 = event.getY();
//                    if (x1 - x2 > 50) {  // 向左滑
//                        //Toast.makeText(SquareActivity.this, "向左滑", Toast.LENGTH_SHORT).show();
//                        tabHost.setCurrentTab(1);
//                    }
//                }
//                return false;
//            }
//        });
//    }


    @Override
    protected void onResume() {
        super.onResume();
        if (tabHost.getCurrentTabTag().equals("tab1")) {
            getAskMsgData();
        } else {
            getHelpMsgData();
        }
    }


    public void initAskList() {
        lv_askmsg = (RefreshListView) findViewById(R.id.lv_askmsg);
        lv_askmsg.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onDownPullRefresh() {
                getAskMsgData();
            }

            @Override
            public void onLoadingMore() {
                mHandler.sendEmptyMessage(0);
            }
        });
        askmsgData = new ArrayList<AskMsg>();
        ask_event_id = -1;
//        getAskMsgData();
        // 初始化提问信息列表
        askMsgAdapter = new AskMsgAdapter(askmsgData, R.layout.layout_askmsg, this);
        lv_askmsg.setAdapter(askMsgAdapter);
    }

    public void initHelpList() {
        lv_helpmsg = (RefreshListView) findViewById(R.id.lv_helpmsg);
        lv_helpmsg.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onDownPullRefresh() {
                getHelpMsgData();
            }

            @Override
            public void onLoadingMore() {
                mHandler.sendEmptyMessage(1);
            }
        });
        help_event_id = -1;

        helpmsgData = new ArrayList<HashMap<String, Object>>();
//        getHelpMsgData();
        // 初始化求助信息列表
        setHelpAdapter();

    }

    private void setHelpAdapter() {
        simpleAdapter = new SimpleAdapter(SquareActivity.this, helpmsgData, R.layout.layout_helpmsg,
                new String[]{"head", "launcher", "time", "title", "distance", "support_number"},
                new int[]{R.id.iv_head, R.id.tv_nickname, R.id.tv_time, R.id.tv_title,
                        R.id.tv_distance, R.id.tv_responsenum}) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final View view = super.getView(position, convertView, parent);

                ImageView iv_head = (ImageView) view.findViewById(R.id.iv_head);
                iv_head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SquareActivity.this, UserMsgActivity.class);
                        intent.putExtra("event_id", (int) helpmsgData.get(position).get("event_id"));
                        startActivity(intent);
                    }
                });
                LinearLayout layout_helpmsg = (LinearLayout) view.findViewById(R.id.layout_helpmsg);
                layout_helpmsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SquareActivity.this, HelpMsgDetailActivity.class);
                        intent.putExtra("event_id", (int) helpmsgData.get(position).get("event_id"));
                        startActivity(intent);
                    }
                });

                TextView tv_distance = (TextView) view.findViewById(R.id.tv_distance);
                String dis = helpmsgData.get(position).get("distance").toString();
                dis += "km";
                tv_distance.setText(dis);
                TextView tv_responsenum = (TextView) view.findViewById(R.id.tv_responsenum);
                String res = helpmsgData.get(position).get("support_number").toString();
                res += "人响应";
                tv_responsenum.setText(res);


                ImageView iv_creditvalue = (ImageView)view.findViewById(R.id.iv_creditlevel);
                ImageView iv_isCertification = (ImageView) view.findViewById(R.id.iv_isCertification);
                if ((int)helpmsgData.get(position).get("is_verify") == 1)
                    iv_isCertification.setImageResource(R.mipmap.certification);
                else
                    iv_isCertification.setImageResource(R.mipmap.certification2);
                int temp = (int)(double)helpmsgData.get(position).get("reputation");
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
                return view;
            }
        };
        lv_helpmsg.setAdapter(simpleAdapter);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.square));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SquareActivity.this, MainActivity.class);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("ask_num", 0);
                editor.commit();
                startActivity(intent);
                finish();
            }
        });
        // 设置右边按钮
        toolbar_icon = (Button) findViewById(R.id.toolbar_icon);
        toolbar_icon.setBackgroundResource(R.drawable.plus_selector);
        toolbar_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow = new SponsorEntryPopupWindow2(SquareActivity.this);
                //显示窗口
                menuWindow.showAtLocation(SquareActivity.this.findViewById(R.id.layout_square),
                        Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                //设置背景颜色变暗
                Animation animationIn = AnimationUtils.loadAnimation(SquareActivity.this, R.anim.alpha_in);
                transparentview.setVisibility(View.VISIBLE);
                transparentview.startAnimation(animationIn);
                menuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Animation animationOut = AnimationUtils.loadAnimation(SquareActivity.this, R.anim.alpha_out);
                        transparentview.startAnimation(animationOut);
                        transparentview.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void initTab() {
        tabHost = (TabHost) findViewById(R.id.tabHost_message);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(getResources().getString(R.string.ask), null).setContent(R.id.layout_askmsg));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator(getResources().getString(R.string.help), null).setContent(R.id.layout_helpmsg));

        TabWidget tabWidget = tabHost.getTabWidget();
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(15);
            tv.setTextColor(getResources().getColor(R.color.black));
            View view = tabWidget.getChildAt(i);
            view.setBackgroundResource(R.drawable.tab_selector);
        }

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("tab1")) {
                    getAskMsgData();
                } else {
                    getHelpMsgData();
                }
            }
        });
    }

    private void init() {
        initToolbar();
        initTab();
        initAskList();
        initHelpList();
        transparentview = findViewById(R.id.transparentview);
    }

    //声明Handler
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    //Toast.makeText(SquareActivity.this, "加载更多...", Toast.LENGTH_SHORT).show();
                    loadMoreAskMsg();  //加载更多数据，这里可以使用异步加载
//                    askMsgAdapter.refresh(askmsgData);
                    break;
                case 1:
                    //Toast.makeText(SquareActivity.this, "加载更多...", Toast.LENGTH_SHORT).show();
                    loadMoreHelpMsg();  //加载更多数据，这里可以使用异步加载
//                    simpleAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private void loadMoreAskMsg() { //加载更多数据
        getInfo(3, ask_event_id);
    }

    private void loadMoreHelpMsg() { //加载更多数据
        getInfo(2, help_event_id);
    }


    public void getHelpMsgData() {

        temphelpmsgData = new ArrayList<>();
        getInfo(2, -1);

    }

    private void getInfo(final int operation, final int event_id) {

        // 目标URL
        String urlString = Config.HOST;
        String location = "/android/get_my_events";
        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();

        try {
            jo.put("operation", operation);
            jo.put("id", sharedPref.getString("id", "none"));
            if (event_id != -1)
                jo.put("event_id", event_id);
            if (operation == 2) {
                jo.put("longitude", MainActivity.longitude);
                jo.put("latitude", MainActivity.latitude);
            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
            @Override
            public void onResponse(Object content) {
                if (content != null) {
                    String str = ((BackPack) content).getBody();
//                            Toast.makeText(SquareActivity.this, str, Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject Json = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", Json.getString("status"));
                        editor.commit();

                        if (sharedPref.getString("status", "none").equals("200")) {
                            JSONArray jsonArray = Json.getJSONArray("event_list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                HashMap<String, Object> temp = new HashMap<String, Object>();
                                //"head", "name", "time", "title", "distance", "response"
                                if (operation == 2) {
                                    LatLng mypos = new LatLng(MainActivity.latitude, MainActivity.longitude);
                                    LatLng targetpos = new LatLng(jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude"));
                                    String diss = new DecimalFormat("#.00").format(AMapUtils.calculateLineDistance(mypos, targetpos) / 1000);
                                    temp.put("distance", diss);
                                    temp.put("head", R.mipmap.head);
                                    help_event_id = jsonObject.getInt("event_id");
                                } else {
                                    ask_event_id = jsonObject.getInt("event_id");
                                }
                                temp.put("event_id", jsonObject.getInt("event_id"));
                                temp.put("type", jsonObject.getInt("type"));
                                temp.put("launcher_id", jsonObject.getInt("launcher_id"));
                                temp.put("launcher", jsonObject.getString("launcher"));
                                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("time"));
                                String time = new SimpleDateFormat("MM-dd HH:mm").format(date);
                                temp.put("time", time);
                                date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("last_time"));
                                time = new SimpleDateFormat("MM-dd HH:mm").format(date);
                                temp.put("last_time", time);
                                temp.put("state", jsonObject.getInt("state"));
                                temp.put("demand_number", jsonObject.getInt("demand_number"));
                                temp.put("support_number", jsonObject.getInt("support_number"));
                                temp.put("follow_number", jsonObject.getInt("follow_number"));
                                temp.put("group_pts", jsonObject.getDouble("group_pts"));
                                temp.put("love_coin", jsonObject.getInt("love_coin"));
//                                temp.put("contact", jsonObject.getString("contact"));
                                temp.put("title", jsonObject.getString("title"));
                                temp.put("content", jsonObject.getString("content"));
                                if ((int) temp.get("type") == 0)
                                    temp.put("is_like", jsonObject.getInt("is_like"));
                                temp.put("comment", jsonObject.getString("comment"));
                                temp.put("location", jsonObject.getString("location"));
                                temp.put("is_verify", jsonObject.getInt("is_verify"));
                                temp.put("occupation", jsonObject.getInt("occupation"));
                                temp.put("reputation", jsonObject.getDouble("reputation"));

                                switch (operation) {
                                    case 2:
                                        temphelpmsgData.add(temp);
                                        break;
                                    case 3:
                                        askmsgData.add(new AskMsg(temp));
                                }
                            }
                            if (operation == 2) {
                                if (event_id == -1) {
                                    lv_helpmsg.hideHeaderView();
                                    helpmsgData = temphelpmsgData;
                                    setHelpAdapter();
                                } else {
                                    lv_helpmsg.hideFooterView();
                                    helpmsgData = temphelpmsgData;
                                    simpleAdapter.notifyDataSetChanged();
                                }

                            } else {
                                askMsgAdapter.refresh(askmsgData);
                                if (event_id == -1) {
                                    lv_askmsg.hideHeaderView();
                                } else {
                                    lv_askmsg.hideFooterView();
                                }
                            }
                        }
//                                handler.sendEmptyMessage(-1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();

    }

    public void getAskMsgData() {
        askmsgData = new ArrayList<>();
        getInfo(3, -1);
    }


}
