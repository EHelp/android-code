package com.ehelp.ehelp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.ehelp.ehelp.personcenter.UserMsgActivity;
import com.ehelp.ehelp.square.AskMsg;
import com.ehelp.ehelp.square.AskMsgDetailActivity;
import com.ehelp.ehelp.square.ReplyAskActivity;
import com.ehelp.ehelp.util.ActivityCollector;
import com.ehelp.ehelp.util.CircleImageView;
import com.ehelp.ehelp.util.MyCustomDialog5;
import com.ehelp.ehelp.util.Reputation2level;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by UWTH on 2015/10/24.
 */
public class AskMsgAdapter extends BaseAdapter {
    private List<AskMsg> askMsgs;
    private int resource;
    private LayoutInflater inflater;
    private Context context;
    private SharedPreferences sharedPref;

    public AskMsgAdapter(List<AskMsg> askMsgs, int resource, Context context) {
        this.askMsgs = askMsgs;
        this.resource = resource;
        this.context = context;
        sharedPref = context.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(List<AskMsg> askMsgs) {
        this.askMsgs = askMsgs;
        notifyDataSetChanged();
    }

    private void ManageLike(final int position, final boolean trans, final View view) {

        // 目标URL
        String urlString = Config.HOST;
        String location = "/android/event/manage_like";

        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            jo.put("id", sharedPref.getString("id", "none"));
            jo.put("event_id", askMsgs.get(position).getEvent_id());
            int temp = 0;
            if (trans) temp = 1;
            jo.put("operation", temp);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
            @Override
            public void onResponse(Object content) {
                if (content != null) {
                    String str = ((BackPack) content).getBody();
                    if (str == null) {
                        Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        if (sharedPref.getString("status", "").equals("200")) {
                            final CheckBox cb_thumb = (CheckBox) view.findViewById(R.id.cb_thumb);
                            LinearLayout layout_thumb = (LinearLayout) view.findViewById(R.id.layout_thumb);
                            final AskMsg askMsg = askMsgs.get(position);
                            final TextView thumbNum = (TextView) view.findViewById(R.id.tv_thumbnum);
                            if (trans) {
                                cb_thumb.setChecked(true);
                                int num = Integer.parseInt(thumbNum.getText().toString());
                                thumbNum.setText((num + 1) + "");
                                askMsg.setIs_like(1);
                                askMsg.setThumbnum(askMsg.getThumbnum() + 1);
                            } else {
                                cb_thumb.setChecked(false);
                                int num = Integer.parseInt(thumbNum.getText().toString());

                                thumbNum.setText((num - 1) + "");
                                askMsg.setIs_like(0);
                                askMsg.setThumbnum(askMsg.getThumbnum() - 1);
                            }
                        } else {
                            Toast.makeText(context, "操作失败", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();

    }

    @Override
    public int getCount() {
        return askMsgs.size();
    }

    @Override
    public Object getItem(int position) {
        return askMsgs.get(position);
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
        final AskMsg askMsg = askMsgs.get(position);

        CircleImageView head = (CircleImageView) convertView.findViewById(R.id.iv_head);
        TextView name = (TextView) convertView.findViewById(R.id.tv_nickname);
        TextView time = (TextView) convertView.findViewById(R.id.tv_time);
        TextView title = (TextView) convertView.findViewById(R.id.tv_title);
        TextView replyNum = (TextView) convertView.findViewById(R.id.tv_replynum);
        TextView tv_coinnum = (TextView)convertView.findViewById(R.id.tv_coinnum);
        ImageView iv_isCertification = (ImageView)convertView.findViewById(R.id.iv_isCertification);
        ImageView iv_creditvalue = (ImageView)convertView.findViewById(R.id.iv_creditlevel);
        ImageView iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
        final TextView thumbNum = (TextView) convertView.findViewById(R.id.tv_thumbnum);

        head.setImageResource(askMsg.getHead());
        name.setText(askMsg.getName());
        time.setText(askMsg.getTime());
        title.setText(askMsg.getTitle());
        replyNum.setText(askMsg.getReplynum() + "");
        thumbNum.setText(askMsg.getThumbnum() + "");
        tv_coinnum.setText(askMsg.getLove_coin()+"");
        if (askMsg.getIs_verify() == 1)
            iv_isCertification.setImageResource(R.mipmap.certification);
        else
            iv_isCertification.setImageResource(R.mipmap.certification2);
        iv_creditvalue.setImageResource(Reputation2level.getLevel(askMsg.getReputation()));

        // 设置点赞事件
        final CheckBox cb_thumb = (CheckBox) convertView.findViewById(R.id.cb_thumb);
        if (askMsg.getIsLike()) cb_thumb.setChecked(true);
        else cb_thumb.setChecked(false);
        cb_thumb.setClickable(false);
        LinearLayout layout_thumb = (LinearLayout) convertView.findViewById(R.id.layout_thumb);
        layout_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_thumb.isChecked()) {
                    ManageLike(position, false, view);

                    /*cb_thumb.setChecked(false);
                    int num = Integer.parseInt(thumbNum.getText().toString());

                    thumbNum.setText((num - 1) + "");
                    askMsg.setThumbnum(askMsg.getThumbnum() - 1);*/
                } else {
                    ManageLike(position, true, view);
                    /*cb_thumb.setChecked(true);
                    int num = Integer.parseInt(thumbNum.getText().toString());
                    thumbNum.setText((num + 1) + "");
                    askMsg.setThumbnum(askMsg.getThumbnum() + 1);*/
                }
            }
        });

        /*cb_thumb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    int num = Integer.parseInt(thumbNum.getText().toString());
                    thumbNum.setText((num+1)+"");
                    askMsg.setThumbnum(askMsg.getThumbnum()+1);
                } else {
                    int num = Integer.parseInt(thumbNum.getText().toString());
                    thumbNum.setText((num-1)+"");
                    askMsg.setThumbnum(askMsg.getThumbnum()-1);
                }
            }
        });*/

        // 点击头像跳转至用户信息页面
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserMsgActivity.class);
                intent.putExtra("event_id", askMsg.getEvent_id());
                context.startActivity(intent);
            }
        });


        // 点击跳转至提问详情
        LinearLayout layout_askmsg = (LinearLayout) convertView.findViewById(R.id.layout_askmsg);
        layout_askmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AskMsgDetailActivity.class);
                intent.putExtra("data", askMsg);
                context.startActivity(intent);
            }
        });

        // 点击跳转至编辑回答页面
        LinearLayout layout_reply = (LinearLayout) convertView.findViewById(R.id.layout_reply);
        layout_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReplyAskActivity.class);
                intent.putExtra("data", askMsg);
                context.startActivity(intent);
            }
        });

        // 删除提问事件
        String launcherId = askMsg.getLauncher_id()+"";
        String myId = sharedPref.getString("id", "none");
        if (launcherId.equals(myId)) {
            iv_delete.setVisibility(View.VISIBLE);
        } else {
            iv_delete.setVisibility(View.GONE);
        }
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomDialog5 dialog = new MyCustomDialog5(context, "确定删除这条提问？",
                        new MyCustomDialog5.OnCustomDialogListener() {
                            @Override
                            public void back(boolean flag) {
                                if (flag) {
                                    deleteAskEvent(position);
                                }
                            }
                        });
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
            }
        });

        return convertView;
    }

    private void deleteAskEvent(final int position) {
        // 目标URL
        String urlString = Config.HOST;
        String location = "/android/";
        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();

        try {
            jo.put("event_id", askMsgs.get(position).getEvent_id());
            jo.put("id", sharedPref.getString("id", "none"));

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
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", Json.getString("status"));
                        editor.commit();

                        if (sharedPref.getString("status", "none").equals("200")) {
                            askMsgs.remove(position);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();
    }
}
