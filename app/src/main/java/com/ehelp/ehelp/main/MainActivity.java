package com.ehelp.ehelp.main;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.ehelp.ehelp.R;
import com.ehelp.ehelp.account.LoginActivity;
import com.ehelp.ehelp.bank.BankActivity;
import com.ehelp.ehelp.bank.LovingBankActivity;
import com.ehelp.ehelp.credit.CreditsActivity;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.message.MyMessageActivity;
import com.ehelp.ehelp.mycontact.MyContactActivity;
import com.ehelp.ehelp.myparti.MyPartiActivity;
import com.ehelp.ehelp.personcenter.PersonCenterActivity;
import com.ehelp.ehelp.personcenter.UserMsgActivity;
import com.ehelp.ehelp.settings.SettingsActivity;
import com.ehelp.ehelp.sponsorhelp.HelperListActivity;
import com.ehelp.ehelp.sponsorsos.SponsorSosCountdownActivity;
import com.ehelp.ehelp.square.HelpEventFinishActivity;
import com.ehelp.ehelp.square.HelpMsgDetailActivity;
import com.ehelp.ehelp.square.HelpMsgDetailActivity2;
import com.ehelp.ehelp.square.SquareActivity;
import com.ehelp.ehelp.userhelp.UserhelpActivity;
import com.ehelp.ehelp.util.ActivityCollector;
import com.ehelp.ehelp.util.CircleImageView;
import com.ehelp.ehelp.util.DBManager;
import com.ehelp.ehelp.util.EventItem;
import com.ehelp.ehelp.util.HandlerShare;
import com.ehelp.ehelp.util.MyCustomDialog5;
import com.ehelp.ehelp.util.Reputation2level;
import com.ehelp.ehelp.util.SponsorEntryPopupWindow;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements AMap.OnMarkerClickListener, GeocodeSearch.OnGeocodeSearchListener, RouteSearch.OnRouteSearchListener, LocationSource, AMapLocationListener {

    private static double mn = 1e-5; //double比较的精度
    private static int new_ask = 0; //未查看提问数
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    private Button toolbar_icon;
    private TextView tv_msgnum;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private SponsorEntryPopupWindow menuWindow;
    private View transparentview;

    private LinearLayout layout_waithelp;
    private LinearLayout layout_gohelp;
    private Button btn_cancelhelp;
    private LinearLayout btn_checkhelper;
    private CircleImageView iv_head2;
    private Handler mHandler;
    private Runnable runnable;
    private ImageView iv_phone;
    private TextView tv_detail;
    private TextView num_of_helper;
    private int helpernum; //响应人数
    private int demandnum; //需要人数
    private Button btn_sponsor;

    // 侧边栏的信息
    private CircleImageView iv_head;
    private TextView tv_nickname;
    private TextView tv_creditvalue;
    private TextView tv_coinnum;
    private ImageView iv_creditlevel;
    private View havemsg;
    private double creditValue;


    private Marker mymarker;
    private SharedPreferences sharedPref;

    // 前往帮助栏
    private TextView tv_position2;
    private TextView tv_people;
    private TextView tv_nickname2;
    private TextView tv_phone2;

    public static String recent_address = "";  // 记录最近点击的求助者的位置
    public static String goinghelp_address = "";  // 记录前往帮助的求助者的地址
    //private double goinghelp_latitude, goinghelp_longitude;  // 记录前往帮助的求助者的经纬度
    private int goinghelp_eventId;
    private Intent myintent;

    public static boolean ishelping = false;
    public static boolean issosing = false;
    //    public static boolean postsos = false;
    public static boolean isgoinghelp = false;
    public static double latitude, longitude; //当前经纬度

    private ArrayList<EventItem> dataList;

    private MapView mapView;
    private AMap aMap;
    private UiSettings mUiSettings;
    private LocationManager locationManager;
    private GeocodeSearch geocoderSearch;
    private RouteSearch routeSearch;

    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    //声明定位回调监听器
    //public AMapLocationListener mLocationListener;
    private DBManager dbManager;
    private boolean first;
    private boolean first_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstInGuide();

        dbManager = new DBManager(this);
        first = true;
//        dbManager.deleteAllMessage();
        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        String temp = "ehelp_";
        temp += sharedPref.getString("account", "guest");
        Context context = getApplicationContext();
        XGPushManager.registerPush(context, temp); //信鸽注册


//        Toast.makeText(MainActivity.this, temp, Toast.LENGTH_SHORT).show();

        mapView = (MapView) findViewById(R.id.layout_map);
        mapView.onCreate(savedInstanceState);
        mHandler = new Handler() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void handleMessage(Message msg) {
//                获取事件的响应
                if (msg.what == -1) {
                    if (sharedPref.getString("status", "").equals("200")) {
//                        Toast.makeText(MainActivity.this, "获取事件成功", Toast.LENGTH_SHORT).show();
                        addMarker();
                    } else {
                        Toast.makeText(MainActivity.this, "获取事件失败", Toast.LENGTH_SHORT).show();
                    }
//                    获取人数的响应
                } else if (msg.what == 2) {
                    if (sharedPref.getString("status", "").equals("200")) {
//
                    } else {
                        Toast.makeText(MainActivity.this, "获取响应人数失败", Toast.LENGTH_SHORT).show();
                    }
                } else if (msg.what == 10) {
                    tv_msgnum.setText(sharedPref.getInt("ask_num", 0) + "");
                    tv_msgnum.setVisibility(View.VISIBLE);
                }
                super.handleMessage(msg);
            }
        };

        HandlerShare.handler = mHandler;

        initMap();

        init();

//      重启app后，回归退出前的状态
        String id = sharedPref.getString("id", "none") + "_";
        switch (sharedPref.getInt(id + "condition", -1)) {
//            正在帮助别人
            case 1:
                tv_nickname2.setText(sharedPref.getString(id + "helper_username", "none"));
                tv_phone2.setText(sharedPref.getString(id + "helper_phone", "none"));
                tv_position2.setText(sharedPref.getString(id + "helper_location", "none"));
                tv_people.setText(sharedPref.getString(id + "helper_number", "none"));
                MainActivity.isgoinghelp = true;
                break;
//            正在求救
            case 2:
                MainActivity.issosing = true;
                break;
//            正在求助
            case 3:
                MainActivity.ishelping = true;
                break;
            default:
                break;
        }
    }

    //    获取周边事件
    private void GetEvent() {

        // 目标URL
        String urlString = Config.HOST;
        String location = "/android/get_events";
        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();

        try {
            jo.put("longitude", longitude);
            jo.put("latitude", latitude);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
            @Override
            public void onResponse(Object content) {
                if (content != null) {
                    String str = ((BackPack) content).getBody();
//                            Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();

                        if (jsonObject.getString("status").equals("200")) {

                            JSONArray data = jsonObject.getJSONArray("info");
                            dataList = new ArrayList<EventItem>();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);
                                dataList.add(new EventItem(item.getInt("event_id"), item.getInt("type"), item.getDouble("longitude"), item.getDouble("latitude")));
                            }
                        }
                        mHandler.sendEmptyMessage(-1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        //Toast.makeText(this, "123", Toast.LENGTH_SHORT).show();
        LatLng pos = new LatLng(latitude, longitude);
        // 创建一个设置经纬度的CameraUpdate
        CameraUpdate cu = CameraUpdateFactory.changeLatLng(pos);
        // 更新地图显示区域
        aMap.moveCamera(cu);
    }

    @Override
    public void deactivate() {

    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                latitude = amapLocation.getLatitude();//获取经度
                longitude = amapLocation.getLongitude();//获取纬度
                //Toast.makeText(MainActivity.this, "latitude is:"+latitude+"\nlongitude is :"+longitude, Toast.LENGTH_SHORT).show();
                amapLocation.getAccuracy();//获取精度信息

                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getRoad();//街道信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码

//                Intent intent = new Intent(MainActivity.this, HelpEventFinishActivity.class);
//                startActivity(intent);

                if (first) UpdateLocation();
                GetEvent();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。

            }
        }
    }

    /*
        class SosResponse implements IResponse {
            @Override
            public void onResponse(Object content) {
                if (content != null) {
                    String str = ((BackPack) content).getBody();
                    Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.putString("event_id", jsonObject.getString("event_id"));
                        editor.commit();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    */
//    取消求救/求助的响应
    class CancelSosResponse implements IResponse {
        @Override
        public void onResponse(Object content) {
            if (content != null) {
                String str = ((BackPack) content).getBody();
//                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("status", jsonObject.getString("status"));
                    editor.commit();
                    if (jsonObject.getString("status").equals("200")) {
                        ishelping = false;
                        issosing = false;
                        editor.remove("event_id");
                        editor.commit();
                        layout_waithelp.setVisibility(View.GONE);
                        btn_sponsor.setVisibility(View.VISIBLE);
                        GetEvent();
                    } else {
                        Toast.makeText(MainActivity.this, "取消失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //    发送取消求救/求助的报文
    private void CancelSOS() {

        // 目标URL
        mHandler.removeCallbacks(runnable);
        String urlString = Config.HOST;
        String location = "/android/emergency_cancel";
        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        int event = sharedPref.getInt("event_id", -1);

        try {
            jo.put("id", sharedPref.getString("id", "0"));
            jo.put("event_id", event);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, new CancelSosResponse(), HttpHelper.HTTP_POST, null);
        httphelper.execute();

    }

    private void addMarker() {
        aMap.clear();
        LatLng pos = new LatLng(latitude, longitude);
        if (first_) {
            // 创建一个设置经纬度的CameraUpdate
            CameraUpdate cu = CameraUpdateFactory.changeLatLng(pos);
            // 更新地图显示区域
            aMap.moveCamera(cu);
            first_ = false;
        }
        // 创建一个MarkerOptions对象
        MarkerOptions markerOptions1 = new MarkerOptions();
        // 设置MarkerOptions的添加位置
        markerOptions1.position(pos);
        markerOptions1.icon(BitmapDescriptorFactory.fromResource(R.mipmap.mymark));
        markerOptions1.draggable(true);

        // 添加MarkerOptions(实际上是添加Marker)
        mymarker = aMap.addMarker(markerOptions1);

//                Toast.makeText(MainActivity.this, latitude + " " + longitude, Toast.LENGTH_SHORT).show();
        mymarker.setPosition(pos);
        for (int i = 0; i < dataList.size(); i++) {
            EventItem temp = dataList.get(i);
            if (temp.getEventid() == goinghelp_eventId && isgoinghelp) {
                final double goinghelp_latitude = temp.getLatitude();
                final double goinghelp_longitude = temp.getLongitude();
                setRouteSearch(goinghelp_latitude, goinghelp_longitude);
            }
            double lng = temp.getLongitude(), lat = temp.getLatitude();
            LatLng position = new LatLng(lat, lng);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(position);
            if (temp.getType() == 2) {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.sosmark));
            } else {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.helpmark));
            }
            markerOptions.draggable(false);
            aMap.addMarker(markerOptions);
        }
    }

    void firstInGuide() {
        SharedPreferences preferences = getSharedPreferences("first_pref",
                MODE_PRIVATE);
        boolean isFirstIn = preferences.getBoolean("isFirstIn", true);
        if (isFirstIn) {
            final Dialog dialog = new Dialog(this, R.style.Dialog_Fullscreen);
            dialog.setContentView(R.layout.dialog_guide);
            RelativeLayout layout_guide = (RelativeLayout) dialog.findViewById(R.id.layout_guide);
            layout_guide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstIn", false);
            editor.commit();
        }
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            CameraUpdate cu = CameraUpdateFactory.zoomTo(15);
            aMap.moveCamera(cu);
            CameraUpdate titleUpdate = CameraUpdateFactory.changeTilt(30);
            aMap.moveCamera(titleUpdate);
            // 创建一个MarkerOptions对象
            mUiSettings = aMap.getUiSettings();
            // 设置隐藏缩放按钮
            mUiSettings.setZoomControlsEnabled(false);
            // 设置定位监听。如果不设置此定位资源则定位按钮不可点击。
            aMap.setLocationSource(this);
            // 设置默认定位按钮是否显示
            mUiSettings.setMyLocationButtonEnabled(true);
            // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            aMap.setMyLocationEnabled(true);
        }
        setUpLocation();
        aMap.setOnMarkerClickListener(this);
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
    }

    private void setUpLocation() {
        //初始化定位
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(getApplicationContext());
            //设置定位回调监听
            mLocationClient.setLocationListener(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置是否只定位一次,默认为false
            mLocationOption.setOnceLocation(false);
            //设置是否强制刷新WIFI，默认为强制刷新
            mLocationOption.setWifiActiveScan(true);
            //设置是否允许模拟位置,默认为false，不允许模拟位置
            mLocationOption.setMockEnable(false);
            //设置定位间隔,单位毫秒,默认为2000ms
            mLocationOption.setInterval(10000);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
        }

        //启动定位
        mLocationClient.startLocation();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setBackgroundResource(R.drawable.accounticon_selector);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);//关闭抽屉
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);//打开抽屉
                }
            }
        });
        // 设置右边按钮
        toolbar_icon = (Button) findViewById(R.id.toolbar_icon);
        toolbar_icon.setBackgroundResource(R.drawable.square_selector);
        toolbar_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SquareActivity.class);
                startActivity(intent);
            }
        });

        tv_msgnum = (TextView) findViewById(R.id.tv_msgnum);
        tv_msgnum.setVisibility(View.VISIBLE);
    }

    private void findView() {
        helpernum = 0;
        demandnum = 0;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        transparentview = findViewById(R.id.transparentview);
        btn_sponsor = (Button) findViewById(R.id.btn_sponsor);

        num_of_helper = (TextView) findViewById(R.id.num_of_helper);

        // 等待帮助状态栏
        layout_waithelp = (LinearLayout) findViewById(R.id.layout_waithelp);
        btn_cancelhelp = (Button) findViewById(R.id.btn_cancelhelp);
        btn_checkhelper = (LinearLayout) findViewById(R.id.btn_check);
        // 前往帮助状态栏
        layout_gohelp = (LinearLayout) findViewById(R.id.layout_gohelp);
        iv_head2 = (CircleImageView) findViewById(R.id.iv_head2);
        iv_phone = (ImageView) findViewById(R.id.iv_phone);
        tv_detail = (TextView) findViewById(R.id.tv_detail);
        // 侧边栏信息
        iv_head = (CircleImageView) findViewById(R.id.iv_head);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_creditvalue = (TextView) findViewById(R.id.tv_creditvalue);
        tv_coinnum = (TextView) findViewById(R.id.tv_coinnum);
        iv_creditlevel = (ImageView) findViewById(R.id.iv_creditlevel);
        havemsg = (View) findViewById(R.id.havemsg);
        // 前往帮助栏信息
        tv_position2 = (TextView) findViewById(R.id.tv_position2);
        tv_phone2 = (TextView) findViewById(R.id.tv_phone2);
        tv_people = (TextView) findViewById(R.id.tv_people);
        tv_nickname2 = (TextView) findViewById(R.id.tv_nickname2);
    }

    private void init() {
        findView();
        initToolbar();
        // 收集activity，以便退出登录时集中销毁
        ActivityCollector.getInstance().addActivity(this);

        setSponsorHelpEvent();
        setWaitHelpEvent();
        setGoHelpEvent();
        setDrawers();
        isBarVisiable();


    }

    private void UpdateHelper() {
        runnable = new Runnable() {
            @Override
            public void run() {
                String urlString = Config.HOST;
                String location = "/android/get_helper_num";
                urlString += location;
                // JSON对象，存放http请求参数
                JSONObject jo = new JSONObject();

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
//                            Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject jsonObject = new JSONObject(str);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("status", jsonObject.getString("status"));

                                editor.commit();
                                if (sharedPref.getString("status", "").equals("200")) {
                                    helpernum = jsonObject.getInt("num");
                                    num_of_helper.setText(helpernum + "人响应");
                                    tv_people.setText(helpernum + "/" + demandnum);
                                }
                                mHandler.sendEmptyMessage(2);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, HttpHelper.HTTP_POST, null);
                httphelper.execute();

                mHandler.postDelayed(this, 2000);// 2000ms后执行this，即runable
            }
        };
        mHandler.postDelayed(runnable, 2000);
    }

    public void setDrawers() {
        mDrawerLayout.closeDrawers();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0, 0) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public void setSponsorHelpEvent() {
        btn_sponsor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow = new SponsorEntryPopupWindow(MainActivity.this);
                //显示窗口
                menuWindow.showAtLocation(MainActivity.this.findViewById(R.id.drawerlayout),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                //设置背景颜色变暗
                Animation animationIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha_in);
                transparentview.setVisibility(View.VISIBLE);
                transparentview.startAnimation(animationIn);
                menuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Animation animationOut = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha_out);
                        transparentview.startAnimation(animationOut);
                        transparentview.setVisibility(View.GONE);
                    }
                });
            }
        });

        btn_sponsor.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Toast.makeText(MainActivity.this, "haha", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SponsorSosCountdownActivity.class);
                startActivity(intent);
                return false;
            }
        });
    }

    public void isBarVisiable() {
        if (ishelping || issosing) {
            layout_waithelp.setVisibility(View.VISIBLE);
            btn_sponsor.setVisibility(View.GONE);
            if (issosing) {
                btn_cancelhelp.setText(getResources().getString(R.string.cancelsos));
            } else {
                btn_cancelhelp.setText(getResources().getString(R.string.cancelhelp));
            }
            UpdateHelper();
        } else {
            layout_waithelp.setVisibility(View.GONE);
        }
        if (isgoinghelp) {
            layout_gohelp.setVisibility(View.VISIBLE);
            btn_sponsor.setVisibility(View.GONE);
            if (getIntent().getExtras() != null) {
                Bundle bundle = getIntent().getExtras();
                tv_phone2.setText(bundle.getString("phone"));
                demandnum = bundle.getInt("demand_num");
                tv_people.setText(bundle.getInt("support_num") + "/" + demandnum);
                tv_nickname2.setText(bundle.getString("username"));
                goinghelp_eventId = bundle.getInt("event_id");
//                Toast.makeText(this, goinghelp_eventId + "", Toast.LENGTH_SHORT).show();
                tv_position2.setText(goinghelp_address);
            }
        } else {
            layout_gohelp.setVisibility(View.GONE);
        }
        if (!isgoinghelp && !issosing && !ishelping) {
            btn_sponsor.setVisibility(View.VISIBLE);
        }
    }

    public void setWaitHelpEvent() {
        btn_checkhelper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = num_of_helper.getText().toString();
                if (num.charAt(0) == '0') {
                    Toast.makeText(MainActivity.this, "暂时无人帮助", Toast.LENGTH_SHORT).show();
                    return;
                }
                String text = btn_cancelhelp.getText().toString();
                Intent intent = new Intent(MainActivity.this, HelperListActivity.class);
                if (text.equals("取消求助")) {
                    intent.putExtra("ishelp", true);
                } else {
                    intent.putExtra("ishelp", false);
                }
                startActivity(intent);
            }
        });

        btn_cancelhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = btn_cancelhelp.getText().toString();
                MyCustomDialog5 dialog = new MyCustomDialog5(MainActivity.this, "确认" + text + "？",
                        new MyCustomDialog5.OnCustomDialogListener() {
                            @Override
                            public void back(boolean flag) {
                                if (flag) {
                                    if (issosing || ishelping) {
                                        CancelSOS();
                                    }

                                }
                            }
                        });
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

            }
        });
    }

    public void setGoHelpEvent() {
        iv_head2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserMsgActivity.class);
                intent.putExtra("event_id", sharedPref.getInt("event_id", -1));
                startActivity(intent);
            }
        });

        iv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tv_phone2.getText().toString()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        tv_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HelpMsgDetailActivity2.class);
                Bundle bundle = new Bundle();
                bundle.putInt("helper", helpernum);
                bundle.putInt("type", sharedPref.getInt("type", -1));
                bundle.putString("location", tv_position2.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void clickleftdrawer(View view) {
        switch (view.getId()) {
            // 个人头像点击事件
            case R.id.iv_head: {
                Intent intent = new Intent(MainActivity.this, PersonCenterActivity.class);
                startActivity(intent);
                break;
            }
            // 信誉值、爱心币、我的消息
            case R.id.layout_credit: {
                Intent intent = new Intent(MainActivity.this, CreditsActivity.class);
                intent.putExtra("credit", creditValue + "");
                startActivity(intent);
                break;
            }
            case R.id.layout_coin: {
                Intent intent = new Intent(MainActivity.this, LovingBankActivity.class);
                intent.putExtra("coin", tv_coinnum.getText().toString());
                startActivity(intent);
                //Intent intent = new Intent(MainActivity.this, LovingBankActivity.class);
                //startActivity(intent);
                break;
            }
            case R.id.layout_msg: {
                Intent intent = new Intent(MainActivity.this, MyMessageActivity.class);
                startActivity(intent);
                break;
            }
            // 我的联系人、个人中心、我参与的、设置、帮助与反馈
            case R.id.layout_myContact: {
                Intent intent = new Intent(this, MyContactActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.layout_personcenter: {
                Intent intent = new Intent(this, PersonCenterActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.layout_myparti: {
                Intent intent = new Intent(this, MyPartiActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.layout_setting: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.layout_helpandfeedback: {
                Intent intent = new Intent(this, UserhelpActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // must store the new intent unless getIntent() will return the old one
        setIntent(intent);
    }

    @Override
    protected void onResume() {

        mapView.onResume();
        setUpLocation();
        isBarVisiable();
        getInfo();
        if (sharedPref.getInt("ask_num", 0) == 0) {
            tv_msgnum.setVisibility(View.INVISIBLE);
        } else {
            tv_msgnum.setText(sharedPref.getInt("ask_num", 0) + "");
            tv_msgnum.setVisibility(View.VISIBLE);
        }
        //tv_messagenum.setText(dbManager.getCount()+"");

        /*String path = "/sdcard/eHelp/";
        Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");//从Sd中找头像，转换成Bitmap
        if(bt != null){
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);//转换成drawable
            iv_head.setImageDrawable(drawable);
        }else{

             //如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中


        }*/
        first_ = true;
        int temp = Integer.parseInt(sharedPref.getString("id", "0"));
        if (dbManager.getCount(temp) == 0) {
            havemsg.setVisibility(View.INVISIBLE);
        } else {
            havemsg.setVisibility(View.VISIBLE);
        }
        super.onResume();

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // 判断点击的是否为我的位置的marker
        if (!marker.getIcons().get(0).equals(BitmapDescriptorFactory.fromResource(R.mipmap.helpmark))
                && !marker.getIcons().get(0).equals(BitmapDescriptorFactory.fromResource(R.mipmap.sosmark)))
            return false;

        // 根据经纬度获取地址信息
        final double latitude = marker.getPosition().latitude;
        final double longitude = marker.getPosition().longitude;
        getAddressFromPosition(latitude, longitude);

        myintent = new Intent(this, HelpMsgDetailActivity.class);
        Bundle bundle = new Bundle();

        for (int i = 0; i < dataList.size(); i++) {
            EventItem temp = dataList.get(i);
            if (equal(temp.getLatitude(), latitude) && equal(temp.getLongitude(), longitude)) {
                bundle.putInt("event_id", temp.getEventid());
                bundle.putInt("type", temp.getType());
                SharedPreferences.Editor editor = sharedPref.edit();
//                editor.putInt("event_id", temp.getEventid());
//                editor.putInt("type", temp.getType());
                editor.commit();
                break;
            }
        }

        myintent.putExtras(bundle);
        return false;
    }

    private boolean equal(double a, double b) {
        if (Math.abs(a - b) <= mn) return true;
        return false;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(runnable);
        mapView.onPause();
        mLocationClient.stopLocation();
        SharedPreferences.Editor editor = sharedPref.edit();
        String id = sharedPref.getString("id", "none") + "_";
        if (MainActivity.isgoinghelp) {
            editor.putInt(id + "condition", 1);
            editor.putString(id + "helper_location", tv_position2.getText().toString());
            editor.putString(id + "helper_username", tv_nickname2.getText().toString());
            editor.putString(id + "helper_phone", tv_phone2.getText().toString());
            editor.putString(id + "helper_number", tv_people.getText().toString());
        } else if (MainActivity.issosing) {
            editor.putInt(id + "condition", 2);
        } else if (MainActivity.ishelping) {
            editor.putInt(id + "condition", 3);
        } else {
            editor.putInt(id + "condition", 0);
        }
        editor.commit();
        //mLocationClient.onDestroy();
        //mLocationClient = null;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mLocationClient.onDestroy();
        mLocationClient = null;
        dbManager.closeDB();
        MainActivity.ishelping = false;
        MainActivity.isgoinghelp = false;
        MainActivity.issosing = false;
    }

    public void getAddressFromPosition(double latitude, double longitude) {
        LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
//latLonPoint参数表示一个Latlng，第二参数表示范围多少米，GeocodeSearch.AMAP表示是国测局坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
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
        startActivity(myintent);
        myintent = null;
        // Toast.makeText(this, recent_address, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    public void setRouteSearch(double hislatitude, double hislongitude) {
        LatLonPoint startPoint = new LatLonPoint(latitude, longitude);
        LatLonPoint endPoint = new LatLonPoint(hislatitude, hislongitude);
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                startPoint, endPoint);
        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
        routeSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询

    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
        //Toast.makeText(this, "In onWalkRouteSearched:code is " + i, Toast.LENGTH_SHORT).show();
        if (i == 0) {
            if (walkRouteResult != null && walkRouteResult.getPaths() != null
                    && walkRouteResult.getPaths().size() > 0) {
                WalkPath walkPath = walkRouteResult.getPaths().get(0);
                //aMap.clear();// 清理地图上的所有覆盖物
                WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(this,
                        aMap, walkPath, walkRouteResult.getStartPos(),
                        walkRouteResult.getTargetPos());
                walkRouteOverlay.removeFromMap();
                walkRouteOverlay.addToMap();
                walkRouteOverlay.zoomToSpan();
            } else {
                Toast.makeText(this, "对不起，没有搜索到相关数据！", Toast.LENGTH_SHORT).show();
            }
        } else if (i == 27) {
            Toast.makeText(this, "搜索失败,请检查网络连接！", Toast.LENGTH_SHORT).show();
        } else if (i == 32) {
            Toast.makeText(this, "key验证无效！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "未知错误，请稍后重试!错误码为" + i, Toast.LENGTH_SHORT).show();
        }
    }

    private void getInfo() {
        String urlString = Config.HOST;
        String location = "/android/user/get_information";

        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            jo.put("id", sharedPref.getString("id", "none"));
//            jo.put("phone", sharedPref.getString("phone", "none"));
//            jo.put("account", sharedPref.getString("account", "none"));


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
                        Toast.makeText(MainActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        if (sharedPref.getString("status", "").equals("200")) {
                            tv_nickname.setText(jsonObject.getString("nickname"));
                            editor.putString("nickname", jsonObject.getString("nickname"));
                            editor.commit();
                            tv_coinnum.setText(jsonObject.getJSONObject("love_coin").getInt("love_coin") + "");
                            tv_creditvalue.setText((int) (jsonObject.getDouble("reputation") + 0.5) + "");
                            int temp = (int)jsonObject.getDouble("reputation");
                            switch (temp) {
                                case 0:
                                    iv_creditlevel.setImageResource(R.mipmap.creditlevel0);
                                    break;
                                case 1:
                                    iv_creditlevel.setImageResource(R.mipmap.creditlevel1);
                                    break;
                                case 2:
                                    iv_creditlevel.setImageResource(R.mipmap.creditlevel2);
                                    break;
                                case 3:
                                    iv_creditlevel.setImageResource(R.mipmap.creditlevel3);
                                    break;
                                case 4:
                                    iv_creditlevel.setImageResource(R.mipmap.creditlevel4);
                                    break;
                                case 5:
                                    iv_creditlevel.setImageResource(R.mipmap.creditlevel5);
                                    break;
                            }
                            creditValue = jsonObject.getDouble("reputation");
                            iv_creditlevel.setImageResource(Reputation2level.getLevel(jsonObject.getDouble("reputation")));
                        } else {
                            Toast.makeText(MainActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();
    }

    private void UpdateLocation() {
        String urlString = Config.HOST;
        String location = "/android/user/modify_information";

        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            jo.put("id", sharedPref.getString("id", "none"));
            jo.put("longitude", MainActivity.longitude);
            jo.put("latitude", MainActivity.latitude);


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
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        if (sharedPref.getString("status", "").equals("200")) {
                            first = false;
                            Log.d("MainActivity", "update location success");
                        }
                        getInfo();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();
    }
}
