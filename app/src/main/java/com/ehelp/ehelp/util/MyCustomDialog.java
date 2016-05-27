package com.ehelp.ehelp.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.settings.SettingsActivity;

/**
 * Created by UWTH on 2015/10/24.
 */
public class MyCustomDialog extends Dialog {

    //定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener{
        public void back(String name);
    }

    private String name;
    private OnCustomDialogListener customDialogListener;
    private EditText editMsg;
    private Button confirm_btn;
    private Button cancel_btn;
    private TextView title;

    public MyCustomDialog(Context context,String name,OnCustomDialogListener customDialogListener) {
        super(context);
        this.name = name;
        this.customDialogListener = customDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_editmsg);
        //设置标题
        setTitle(name);
        editMsg = (EditText)findViewById(R.id.edit_msg);
        confirm_btn = (Button) findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogListener.back(String.valueOf(editMsg.getText().toString()));
                MyCustomDialog.this.dismiss();
            }
        });
        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomDialog.this.dismiss();
            }
        });
        title = (TextView) findViewById(R.id.title);
        title.setText(name);
    }

}
