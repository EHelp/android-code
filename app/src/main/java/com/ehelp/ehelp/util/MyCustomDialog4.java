package com.ehelp.ehelp.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ehelp.ehelp.R;

/**
 * Created by UWTH on 2015/10/29.
 */
public class MyCustomDialog4 extends Dialog {
    //定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener{
        public void back(String name);
    }

    private String name;
    private OnCustomDialogListener customDialogListener;
    private TextView title;

    private TextView tv_male;
    private TextView tv_female;

    public MyCustomDialog4(Context context,String name,OnCustomDialogListener customDialogListener) {
        super(context);
        this.name = name;
        this.customDialogListener = customDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sex);
        //设置标题
        setTitle(name);
        title = (TextView) findViewById(R.id.title);
        title.setText(name);

        tv_male = (TextView) findViewById(R.id.tv_male);
        tv_female = (TextView) findViewById(R.id.tv_female);

        tv_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.back("男");
                MyCustomDialog4.this.dismiss();
            }
        });

        tv_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.back("女");
                MyCustomDialog4.this.dismiss();
            }
        });

    }
}
