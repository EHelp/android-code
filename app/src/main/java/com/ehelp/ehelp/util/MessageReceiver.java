package com.ehelp.ehelp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.Toast;

import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.square.HelpEventFinishActivity;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by chenzhe on 2015/12/1.
 */
public class MessageReceiver extends XGPushBaseReceiver {

    private Handler handler;

    private void sendAskNumber() {
        handler = HandlerShare.handler;
        handler.sendEmptyMessage(10);
    }

    public void onRegisterResult(Context context, int var2, XGPushRegisterResult xgPushRegisterResult) {
        String temp = xgPushRegisterResult.getAccount();
    }

    public void onUnregisterResult(Context var1, int var2) {

    }

    public void onSetTagResult(Context var1, int var2, String var3) {

    }

    public void onDeleteTagResult(Context var1, int var2, String var3) {

    }

    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
//        Activity activity = (Activity)context;
//        Toast.makeText(context, xgPushTextMessage.getContent(), Toast.LENGTH_SHORT).show();
        int message_type = -1;
        JSONObject jsonObject;
        SharedPreferences sharedPref = context.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        try {
            jsonObject = new JSONObject(xgPushTextMessage.getCustomContent());
            SharedPreferences.Editor editor = sharedPref.edit();
            message_type = jsonObject.getInt("message-type");
            DBManager dbManager = new DBManager(context);
            HashMap data = new HashMap<String, Object>();
            String temp = sharedPref.getString("id", "0");

            switch (message_type) {
                case 1:
                    int ask_num = sharedPref.getInt("ask_num", 0);
                    ask_num++;

                    editor.putInt("ask_num", ask_num);
                    editor.commit();
                    sendAskNumber();
                    break;
                case 2:
                    Intent intent1 = new Intent(context, HelpEventFinishActivity.class);
                    intent1.putExtra("cancel", 1);
                    intent1.putExtra("event_id", sharedPref.getString("event_id", "0"));
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MainActivity.isgoinghelp = false;
                    editor.remove("event_id");
                    editor.commit();
                    context.startActivity(intent1);
                    break;
                case 3:
                    Intent intent = new Intent(context, HelpEventFinishActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MainActivity.isgoinghelp = false;
                    editor.remove("event_id");
                    editor.commit();
                    context.startActivity(intent);
                    break;
                case 4:
                /*data.get("user_id"), data.get("nickname"), data.get("realname"), data.get("location"), data.get("occupation")*/


                    data.put("user_id", jsonObject.getInt("user_id"));
                    data.put("nickname", jsonObject.getString("nickname"));
                    data.put("realname", jsonObject.getString("realname"));
                    data.put("location", jsonObject.getString("location"));
                    data.put("occupation", OccupationEncode.OccupationToString(jsonObject.getInt("occupation")));
                    data.put("content", jsonObject.getString("content"));

                    dbManager.addContact(data, Integer.parseInt(temp));
                    break;
                case 5:
                    data.put("nickname", jsonObject.getString("launcher"));
                    data.put("author", jsonObject.getString("nickname"));
                    data.put("event_id", jsonObject.getInt("event_id"));
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("time"));
                    String time = new SimpleDateFormat("MM-dd HH:mm").format(date);
                    data.put("time", time);
                    data.put("title", jsonObject.getString("title"));
                    data.put("content", jsonObject.getString("content"));
                    data.put("is_like", jsonObject.getInt("is_like"));
                    data.put("love_coin", jsonObject.getInt("love_coin"));
                    data.put("follow_number", jsonObject.getInt("follow_number"));
                    data.put("support_number", jsonObject.getInt("support_number"));

                    dbManager.addResponse(data, Integer.parseInt(temp));
                    break;
                case 6:
                    String nickname = jsonObject.getString("nicknname");
                    int status = jsonObject.getInt("status");
                    data.put("nickname", nickname);
                    data.put("status", status);

                    dbManager.addConResult(data, Integer.parseInt(temp));
                    break;
                case 7:
                    data.put("nickname", jsonObject.getString("nickname"));
                    data.put("event_id", jsonObject.getInt("event_id"));
                    Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("time"));
                    String time1 = new SimpleDateFormat("MM-dd HH:mm").format(date1);
                    data.put("time", time1);
                    data.put("title", jsonObject.getString("title"));
                    data.put("content", jsonObject.getString("content"));
                    data.put("is_like", jsonObject.getInt("is_like"));
                    data.put("love_coin", jsonObject.getInt("love_coin"));
                    data.put("follow_number", jsonObject.getInt("follow_number"));
                    data.put("support_number", jsonObject.getInt("support_number"));
                    dbManager.addAdopt(data, Integer.parseInt(temp));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void onNotifactionClickedResult(Context var1, XGPushClickedResult var2) {

    }

    public void onNotifactionShowedResult(Context var1, XGPushShowedResult var2) {

    }
}
