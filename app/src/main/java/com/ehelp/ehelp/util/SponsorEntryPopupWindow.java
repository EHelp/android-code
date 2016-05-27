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

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.sponsorask.SponsorAskActivity;
import com.ehelp.ehelp.sponsorhelp.SponsorHelpActivity;

/**
 * Created by UWTH on 2015/11/13.
 */
public class SponsorEntryPopupWindow extends PopupWindow {
    private LinearLayout layout_sponsorask;
    private LinearLayout layout_sponsorhelp;
    private TextView tv_cancel;
    private View mMenuView;

    public SponsorEntryPopupWindow(final Activity context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.dialog_sponsorentry, null);
        layout_sponsorask = (LinearLayout) mMenuView.findViewById(R.id.layout_sponsorask);
        layout_sponsorhelp = (LinearLayout) mMenuView.findViewById(R.id.layout_sponsorhelp);
        tv_cancel = (TextView) mMenuView.findViewById(R.id.tv_cancel);
        //取消按钮
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听

        layout_sponsorask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SponsorAskActivity.class);
                context.startActivity(intent);
                dismiss();
            }
        });
        layout_sponsorhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        this.setAnimationStyle(R.style.AnimBottom);
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
