package com.ehelp.ehelp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.message.MyMessageActivity;
import com.ehelp.ehelp.message.UrgencyContactAddedConfirmActivity;
import com.ehelp.ehelp.sponsorhelp.EvaluateActivity;
import com.ehelp.ehelp.square.AskMsg;
import com.ehelp.ehelp.square.Reply;
import com.ehelp.ehelp.util.DBManager;
import com.ehelp.ehelp.util.SystemMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

/**
 * Created by chenzhe on 2015/12/7.
 */
public class SystemMessageAdapter extends BaseAdapter {
    private List<SystemMessage> systemMessages;
    private int resource;
    private LayoutInflater inflater;
    private Context context;

    public SystemMessageAdapter(List<SystemMessage> systemMessages, int resource, Context context) {
        this.systemMessages = systemMessages;
        this.resource = resource;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return systemMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return systemMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
        }
        final View view = convertView;
        final SystemMessage systemMessage = systemMessages.get(position);

        TextView tv_content = (TextView)convertView.findViewById(R.id.tv_content);
        final TextView tv_btn = (TextView) convertView.findViewById(R.id.tv_btn);
        int type = systemMessage.getType();
        if (type == 1) {
            tv_content.setText("您还没有对\""+systemMessage.getNickname()+"\"进行评价");
            tv_btn.setText("去评价");

        } else if (type == 2) {
            tv_content.setText("用户\""+systemMessage.getNickname()+"\"请求添加您为紧急联系人");
            tv_btn.setText("查看");
        }
        tv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (tv_btn.getText().toString().equals("去评价")) {
                    intent.setClass(context, EvaluateActivity.class);
                    intent.putExtra("content", "system");
                    intent.putExtra("helper", systemMessages.get(position).convert());
                } else {
                    intent.setClass(context, UrgencyContactAddedConfirmActivity.class);
                    intent.putExtra("contact", systemMessages.get(position));
                }
                DBManager dbManager = new DBManager(context);
                dbManager.UpdateStatus(systemMessages.get(position));
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public void refresh(List<SystemMessage> Replies) {
        this.systemMessages = Replies;
        notifyDataSetChanged();
    }
}
