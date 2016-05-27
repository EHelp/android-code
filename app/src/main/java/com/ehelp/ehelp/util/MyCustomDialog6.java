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
 * Created by UWTH on 2015/10/27.
 */
public class MyCustomDialog6 extends Dialog {

    //定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener{
        public void back(String name);
    }

    private String name;
    private OnCustomDialogListener customDialogListener;
    private TextView title;

    private TextView tv_family;
    private TextView tv_friend;
    private TextView tv_neighbor;

    public MyCustomDialog6(Context context,String name,OnCustomDialogListener customDialogListener) {
        super(context);
        this.name = name;
        this.customDialogListener = customDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_group);
        //设置标题
        setTitle(name);
        title = (TextView) findViewById(R.id.title);
        title.setText(name);

        tv_family = (TextView) findViewById(R.id.tv_family);
        tv_friend = (TextView) findViewById(R.id.tv_friend);
        tv_neighbor = (TextView) findViewById(R.id.tv_neighbor);

        tv_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.back("家人");
                MyCustomDialog6.this.dismiss();
            }
        });

        tv_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.back("亲朋");
                MyCustomDialog6.this.dismiss();
            }
        });

        tv_neighbor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.back("邻居");
                MyCustomDialog6.this.dismiss();
            }
        });

    }
}
