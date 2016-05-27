package com.ehelp.ehelp.sponsorsos;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.util.RippleBackground;
import com.ehelp.ehelp.util.VibratorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by UWTH on 2015/10/24.
 */
public class SponsorSosCountdownActivity extends AppCompatActivity {
    private Button centerNum;
    private SharedPreferences sharedPref;
    private int time;
    private int T;
    Handler countDown;
    Runnable myRunnable;
    NotificationManager manager;
    int notificationID;

    /* 通过Binder，实现Activity与Service通信 */
    //protected MyBinder mBinder;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //mBinder = (MyBinder) service;
            System.out.println("绑定成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub

        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsorsos_countdown);

        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        // 绑定后台服务
        /*Intent bindIntent = new Intent(CountDownActivity.this,
                CoreService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
*/
        init();

        final RippleBackground rippleBackground = (RippleBackground) findViewById(R.id.content);
        rippleBackground.startRippleAnimation();

        time = 6;
        T = 6;

        countDown = new Handler();
        myRunnable = new Runnable() {
            @Override
            public void run() {
                if (time == 0) {
                    //mBinder.SendSOS();
                    SharedPreferences preferences = getSharedPreferences(
                            "eSOS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("sos_status", 2);
                    //editor.putString("sos_time", DateUtil.getDate());
                    editor.commit();
                    PostSOS();

                } else if (time == T) {
                    T++;
                    countDown.postDelayed(this, 500);
                } else {
                    time--;
                    centerNum.setText("" + time);
                    centerNum();
                    VibratorUtil.Vibrate(SponsorSosCountdownActivity.this, 400);
                    countDown.postDelayed(this, 1000);
                }
            }
        };

        countDown.post(myRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unbindService(connection);
        countDown.removeCallbacks(myRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    class SosResponse implements IResponse {
        @Override
        public void onResponse(Object content) {
            if (content != null) {
                String str = ((BackPack) content).getBody();
//                Toast.makeText(SponsorSosCountdownActivity.this, str, Toast.LENGTH_SHORT).show();
                if (str == null) {
                    Toast.makeText(SponsorSosCountdownActivity.this, "网络不通，请重试", Toast.LENGTH_SHORT).show();
                    SponsorSosCountdownActivity.this.finish();
                }
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("status", jsonObject.getString("status"));
                    editor.commit();
                    if (sharedPref.getString("status", "none").equals("200")) {
                        editor.putInt("event_id", jsonObject.getInt("event_id"));
                        editor.putInt("type", 2);
                        editor.commit();
                        Intent intent = new Intent(SponsorSosCountdownActivity.this,
                                MainActivity.class);
                        MainActivity.issosing = true;
//                        sendNotification();
                        startActivity(intent);
                        SponsorSosCountdownActivity.this.finish();
                    } else {
                        Toast.makeText(SponsorSosCountdownActivity.this, "求救失败，请重试", Toast.LENGTH_SHORT).show();
                        SponsorSosCountdownActivity.this.finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void PostSOS() {
        // 目标URL
        String urlString = Config.HOST;
        String location = "/android/emergency_launch";
        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            jo.put("id", sharedPref.getString("id", "0"));
            jo.put("type", 2);
            jo.put("longitude", MainActivity.longitude);
            jo.put("latitude", MainActivity.latitude);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, new SosResponse(), HttpHelper.HTTP_POST, null);
        httphelper.execute();

    }

    private void init() {
        centerNum = (Button) findViewById(R.id.centerNum);
        Button cencel = (Button) findViewById(R.id.cencel_button);
        cencel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SponsorSosCountdownActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void centerNum() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList = new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(centerNum,
                "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(centerNum,
                "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        centerNum.setVisibility(View.VISIBLE);
        animatorSet.start();
    }

    // 构造notification并发送至通知栏
    private void sendNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pintent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.logo);
        builder.setTicker("紧急求救事件");  // 手机状态栏的提示
        builder.setWhen(System.currentTimeMillis());  // 设置时间
        builder.setContentTitle("紧急求救事件");  // 设置标题
        builder.setContentText("您附近的人发起了紧急求救");  // 设置通知内容
        builder.setContentIntent(pintent);  // 设置点击后的意图
        //builder.setDefaults(Notification.DEFAULT_SOUND);
        //builder.setDefaults(Notification.DEFAULT_LIGHTS);
        //builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setDefaults(Notification.DEFAULT_ALL);  // 设置震动、声音、指示灯
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        //Notification notification = builder.getNotification();
        manager.notify(notificationID, notification);
    }
}
