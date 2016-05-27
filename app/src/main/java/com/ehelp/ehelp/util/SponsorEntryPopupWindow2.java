package com.ehelp.ehelp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.sponsorask.SponsorAskActivity;
import com.ehelp.ehelp.sponsorhelp.SponsorHelpActivity;

/**
 * Created by UWTH on 2015/11/13.
 */
public class SponsorEntryPopupWindow2 extends PopupWindow {
    private Button btn_ask;
    private Button btn_help;
    private Button btn_close;
    private View mMenuView;

    public SponsorEntryPopupWindow2(final Activity context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.dialog_sponsorentry2, null);
        btn_ask = (Button) mMenuView.findViewById(R.id.btn_ask);
        btn_help = (Button) mMenuView.findViewById(R.id.btn_help);
        btn_close = (Button) mMenuView.findViewById(R.id.btn_close);
        //取消按钮
        btn_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听

        btn_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SponsorAskActivity.class);
                context.startActivity(intent);
                dismiss();
            }
        });
        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.isgoinghelp) {
                    Toast.makeText(context, "您正在进行一项救助任务", Toast.LENGTH_SHORT).show();
                    return;
                } else if (MainActivity.issosing) {
                    Toast.makeText(context, "您正在求救！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (MainActivity.ishelping) {
                    Toast.makeText(context, "您正在求助！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(context, SponsorHelpActivity.class);
                context.startActivity(intent);
                dismiss();
            }
        });
        //设置SponsorEntryPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimTop);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.layout_popup).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}
