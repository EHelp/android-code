package com.ehelp.ehelp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.account.LoginActivity;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.square.AskMsg;
import com.ehelp.ehelp.square.AskMsgDetailActivity;
import com.ehelp.ehelp.square.Reply;
import com.ehelp.ehelp.util.ActivityCollector;
import com.ehelp.ehelp.util.CircleImageView;
import com.ehelp.ehelp.util.MyCustomDialog5;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by UWTH on 2015/10/25.
 */
public class ReplyAdapter extends BaseAdapter {
    private List<Reply> replies;
    private int resource;
    private LayoutInflater inflater;
    private Context context;
    private String nickname;
    private SharedPreferences sharedPreferences;

    public ReplyAdapter(List<Reply> replies, int resource, Context context) {
        this.replies = replies;
        this.resource = resource;
        this.context = context;
        sharedPreferences = context.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    @Override
    public int getCount() {
        return replies.size();
    }

    @Override
    public Object getItem(int position) {
        return replies.get(position);
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
        final Reply reply = replies.get(position);

        CircleImageView head = (CircleImageView) convertView.findViewById(R.id.iv_head);
        TextView name = (TextView) convertView.findViewById(R.id.tv_nickname);
        TextView time = (TextView) convertView.findViewById(R.id.tv_time);
        TextView content = (TextView) convertView.findViewById(R.id.tv_content);
        final ImageView iv_acceptans = (ImageView) convertView.findViewById(R.id.iv_acceptans);
        LinearLayout layout_reply = (LinearLayout) convertView.findViewById(R.id.layout_reply);
        String id = reply.getAuthor_id()+"";

        head.setImageResource(reply.getHead());
        name.setText(reply.getAuthor());
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(reply.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String mTime = new SimpleDateFormat("MM-dd HH:mm").format(date);
        time.setText(mTime);
        content.setText(reply.getContent());

        // 点击头像跳转至用户信息页面
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(context, ContactStrangerdetailmsgActivity.class);
                //context.startActivity(intent);
            }
        });

        final View finalConvertView = convertView;

        // 长按删除回答
        layout_reply.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (replies.get(position).getAuthor_id() != Integer.parseInt(sharedPreferences.getString("id", "none"))) {
                    return false;
                }
                MyCustomDialog5 dialog = new MyCustomDialog5(finalConvertView.getContext(), "是否删除您的回答？",
                        new MyCustomDialog5.OnCustomDialogListener() {
                            @Override
                            public void back(boolean flag) {
                                //logout_flag[0] = flag;
                                if (flag) {
                                    DeleteAnswer(position);
                                }
                            }
                        });
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
                return false;
            }
        });

        // 采纳回答按钮点击事件
        iv_acceptans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iv_acceptans.isClickable()) {
                    MyCustomDialog5 dialog = new MyCustomDialog5(finalConvertView.getContext(), "是否采纳其为最佳回答？",
                            new MyCustomDialog5.OnCustomDialogListener() {
                                @Override
                                public void back(boolean flag) {
                                    //logout_flag[0] = flag;
                                    if (flag) {
                                        AdoptAnswer(position, iv_acceptans);
                                    }
                                }
                            });
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                }
            }
        });


        if (!sharedPreferences.getString("nickname", "none").equals(nickname)) {
            iv_acceptans.setVisibility(View.INVISIBLE);
        } else {
            iv_acceptans.setVisibility(View.VISIBLE);
            iv_acceptans.setClickable(true);
        }
        if (reply.getIs_adopted() == 1) {
            iv_acceptans.setVisibility(View.VISIBLE);
            iv_acceptans.setImageResource(R.mipmap.accepted);
            iv_acceptans.setClickable(false);
        } else {
            iv_acceptans.setImageResource(R.mipmap.acceptans);
            iv_acceptans.setClickable(true);
        }

        return convertView;
    }

    public void refresh(List<Reply> Replies) {
        this.replies = Replies;
        notifyDataSetChanged();
    }

    private void DeleteAnswer(final int position) {

        // 目标URL
        String urlString = Config.HOST;
        String location = "/android/event/delete_answer";
        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();

        try {
            jo.put("event_id", replies.get(position).getEvent_id());
            jo.put("id", sharedPreferences.getString("id", "none"));
            jo.put("answer_id", replies.get(position).getAnswer_id());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
            @Override
            public void onResponse(Object content) {
                if (content != null) {
                    String str = ((BackPack) content).getBody();
//                            Toast.makeText(SquareActivity.this, str, Toast.LENGTH_SHORT).show();
                    if (str == null) {
                        Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        JSONObject Json = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status", Json.getString("status"));
                        editor.commit();

                        if (sharedPreferences.getString("status", "none").equals("200")) {
                            //et_reply.setText("");
                            //int temp = Integer.parseInt(tv_replynum.getText().toString());
                            //temp++;
                            //tv_replynum.setText(temp+"");
                            replies.remove(position);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                        }
//                                handler.sendEmptyMessage(-1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();

    }

    private void AdoptAnswer(final int position, final ImageView iv_acceptans) {

        // 目标URL
        String urlString = Config.HOST;
        String location = "/android/event/adopt_answer";
        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();

        try {
            jo.put("event_id", replies.get(position).getEvent_id());
            jo.put("id", sharedPreferences.getString("id", "none"));
            jo.put("answer_id", replies.get(position).getAnswer_id());


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
            @Override
            public void onResponse(Object content) {
                if (content != null) {
                    String str = ((BackPack) content).getBody();
//                            Toast.makeText(SquareActivity.this, str, Toast.LENGTH_SHORT).show();
                    if (str == null) {
                        Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        JSONObject Json = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("status", Json.getString("status"));
                        editor.commit();

                        if (sharedPreferences.getString("status", "none").equals("200")) {
                            iv_acceptans.setImageResource(R.mipmap.accepted);
                            iv_acceptans.setClickable(false);
                        } else {
                            Toast.makeText(context, "采纳失败", Toast.LENGTH_SHORT).show();
                        }
//                                handler.sendEmptyMessage(-1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();

    }
}
