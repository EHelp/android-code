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
public class MyCustomDialog3 extends Dialog {

    //定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener{
        public void back(String name);
    }

    private String name;
    private OnCustomDialogListener customDialogListener;
    private TextView title;

    private TextView tv_photo;
    private TextView tv_takephoto;
    //private Button btn_cancel;

    public MyCustomDialog3(Context context,String name,OnCustomDialogListener customDialogListener) {
        super(context);
        this.name = name;
        this.customDialogListener = customDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_modifyhead);
        //设置标题
        setTitle(name);
        title = (TextView) findViewById(R.id.title);
        title.setText(name);

        tv_photo = (TextView) findViewById(R.id.tv_photo);
        tv_takephoto = (TextView) findViewById(R.id.tv_takephoto);
        //btn_cancel = (Button) findViewById(R.id.btn_cancel);

        tv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.back("本地");
                MyCustomDialog3.this.dismiss();
            }
        });

        tv_takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.back("拍照");
                MyCustomDialog3.this.dismiss();
            }
        });

//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MyCustomDialog3.this.dismiss();
//            }
//        });


    }
}
