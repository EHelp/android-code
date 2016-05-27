package com.ehelp.ehelp.guide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.account.LoginActivity;
import com.ehelp.ehelp.main.MainActivity;

/**
 * Created by UWTH on 2015/12/18.
 */
public class GuideActivity extends AppCompatActivity {
    private Context mContext;
    private ViewFlipper vflp_help;
    private ImageView iv_navigation;

    private final static int MIN_MOVE = 150;   //最小距离
    private MyGestureListener mgListener;
    private GestureDetector mDetector;

    private boolean isFirstStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        iv_navigation = (ImageView) findViewById(R.id.iv_navigation);

        SharedPreferences preferences = getSharedPreferences("first_pref",
                MODE_PRIVATE);
        isFirstStart = preferences.getBoolean("isFirstStart", true);
        if (!isFirstStart) {
            Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        mContext = GuideActivity.this;
        //实例化SimpleOnGestureListener与GestureDetector对象
        mgListener = new MyGestureListener();
        mDetector = new GestureDetector(this, mgListener);
        vflp_help = (ViewFlipper) findViewById(R.id.vflp_help);

    }

    //重写onTouchEvent触发MyGestureListener里的方法
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }


    //自定义一个GestureListener,这个是View类下的，别写错哦！！！
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
            if (e1.getX() - e2.getX() > MIN_MOVE){
                switch (vflp_help.getDisplayedChild()) {
                    case 0: {
                        iv_navigation.setImageResource(R.mipmap.navigation2);
                        break;
                    }
                    case 1: {
                        iv_navigation.setImageResource(R.mipmap.navigation3);
                        break;
                    }
                    case 2: {
                        iv_navigation.setImageResource(R.mipmap.navigation4);
                        break;
                    }
                    case 3: {
                        SharedPreferences preferences = getSharedPreferences("first_pref",
                                MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("isFirstStart", false);
                        editor.commit();
                        Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    }
                }
                vflp_help.setInAnimation(mContext,R.anim.right_in);
                vflp_help.setOutAnimation(mContext, R.anim.right_out);
                vflp_help.showNext();
            } else if(e2.getX() - e1.getX() > MIN_MOVE){
                switch (vflp_help.getDisplayedChild()) {
                    case 0: {
                        return true;
                    }
                    case 1: {
                        iv_navigation.setImageResource(R.mipmap.navigation1);
                        break;
                    }
                    case 2: {
                        iv_navigation.setImageResource(R.mipmap.navigation2);
                        break;
                    }
                    case 3: {
                        iv_navigation.setImageResource(R.mipmap.navigation3);
                        break;
                    }
                }
                vflp_help.setInAnimation(mContext,R.anim.left_in);
                vflp_help.setOutAnimation(mContext, R.anim.left_out);
                vflp_help.showPrevious();
            }
            return true;
        }
    }
}
