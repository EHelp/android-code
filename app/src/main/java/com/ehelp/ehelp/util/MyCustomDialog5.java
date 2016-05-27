package com.ehelp.ehelp.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ehelp.ehelp.R;

/**
 * Created by UWTH on 2015/11/14.
 */
public class MyCustomDialog5 extends Dialog {
    //定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener{
        public void back(boolean flag);
    }

    private String name;
    private OnCustomDialogListener customDialogListener;
    private Button confirm_btn;
    private Button cancel_btn;
    private TextView title;

    public MyCustomDialog5(Context context,String name,OnCustomDialogListener customDialogListener) {
        super(context);
        this.name = name;
        this.customDialogListener = customDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_alert);
        //设置标题
        setTitle(name);
        confirm_btn = (Button) findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.back(true);
                MyCustomDialog5.this.dismiss();
            }
        });
        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.back(false);
                MyCustomDialog5.this.dismiss();
            }
        });
        title = (TextView) findViewById(R.id.title);
        title.setText(name);
    }
}
