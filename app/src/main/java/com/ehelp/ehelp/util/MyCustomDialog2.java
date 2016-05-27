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
public class MyCustomDialog2 extends Dialog {

    //定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener{
        public void back(String name);
    }

    private String name;
    private OnCustomDialogListener customDialogListener;
    private TextView title;

    private TextView tv_doctor;
    private TextView tv_security;
    private TextView tv_policeman;
    private TextView tv_student;
    private TextView tv_teacher;
    private TextView tv_engineer;
    private TextView tv_whitecollar;
    private TextView tv_worker;
    private TextView tv_hobo;
    private TextView tv_others;

    public MyCustomDialog2(Context context,String name,OnCustomDialogListener customDialogListener) {
        super(context);
        this.name = name;
        this.customDialogListener = customDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_listmsg);
        //设置标题
        setTitle(name);
        title = (TextView) findViewById(R.id.title);
        title.setText(name);

        tv_doctor = (TextView) findViewById(R.id.tv_doctor);
        tv_security = (TextView) findViewById(R.id.tv_security);
        tv_policeman = (TextView) findViewById(R.id.tv_policeman);
        tv_student = (TextView) findViewById(R.id.tv_student);
        tv_teacher = (TextView) findViewById(R.id.tv_teacher);
        tv_engineer = (TextView) findViewById(R.id.tv_engineer);
        tv_whitecollar = (TextView) findViewById(R.id.tv_whitecollar);
        tv_worker = (TextView) findViewById(R.id.tv_worker);
        tv_hobo = (TextView) findViewById(R.id.tv_hobo);
        tv_others = (TextView) findViewById(R.id.tv_others);

        tv_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.back("医生");
                MyCustomDialog2.this.dismiss();
            }
        });

        tv_security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.back("保安");
                MyCustomDialog2.this.dismiss();
            }
        });

        tv_policeman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.back("警察");
                MyCustomDialog2.this.dismiss();
            }
        });

        tv_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.back("学生");
                MyCustomDialog2.this.dismiss();
            }
        });

        tv_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.back("教师");
                MyCustomDialog2.this.dismiss();
            }
        });

        tv_engineer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.back("工程师");
                MyCustomDialog2.this.dismiss();
            }
        });

        tv_whitecollar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.back("白领");
                MyCustomDialog2.this.dismiss();
            }
        });

        tv_worker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.back("工人");
                MyCustomDialog2.this.dismiss();
            }
        });

        tv_hobo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.back("无业游民");
                MyCustomDialog2.this.dismiss();
            }
        });

        tv_others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.back("其他");
                MyCustomDialog2.this.dismiss();
            }
        });
    }
}
