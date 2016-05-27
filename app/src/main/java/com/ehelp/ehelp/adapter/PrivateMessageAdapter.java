package com.ehelp.ehelp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.message.UrgencyContactAddedConfirmActivity;
import com.ehelp.ehelp.sponsorhelp.EvaluateActivity;
import com.ehelp.ehelp.square.AskMsgDetailActivity;
import com.ehelp.ehelp.util.DBManager;
import com.ehelp.ehelp.util.PrivateMessage;
import com.ehelp.ehelp.util.SystemMessage;

import java.util.List;

/**
 * Created by chenzhe on 2015/12/14.
 */
public class PrivateMessageAdapter extends BaseAdapter {
    private List<PrivateMessage> privateMessages;
    private int resource;
    private LayoutInflater inflater;
    private Context context;

    public PrivateMessageAdapter(List<PrivateMessage> privateMessages, int resource, Context context) {
        this.privateMessages = privateMessages;
        this.resource = resource;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return privateMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return privateMessages.get(position);
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
        final PrivateMessage privateMessage = privateMessages.get(position);

        TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
        final TextView tv_btn = (TextView) convertView.findViewById(R.id.tv_btn);
        int type = privateMessage.getType();
        if (type == 3) {
            String temp = "同意";
            if (privateMessage.getStatus() == 0) temp = "拒绝";
            tv_content.setText(privateMessage.getNickname() + temp + "添加你为联系人");
            tv_btn.setVisibility(View.INVISIBLE);

        } else if (type == 4) {
            tv_content.setText("用户\"" + privateMessage.getAuthor() + "\"回复了你的提问");
            tv_btn.setText("查看");
        } else {
            tv_content.setText("用户\"" + privateMessage.getNickname() + "\"采纳了你的回答");
            tv_btn.setText("详情");
        }
        tv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, AskMsgDetailActivity.class);
                intent.putExtra("data", privateMessage.convert());

                DBManager dbManager = new DBManager(context);
                dbManager.deletePriMessage(privateMessage.get_id());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public void refresh(List<PrivateMessage> Replies) {
        this.privateMessages = Replies;
        notifyDataSetChanged();
    }
}
