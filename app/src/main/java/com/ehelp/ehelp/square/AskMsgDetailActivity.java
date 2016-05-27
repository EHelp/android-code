package com.ehelp.ehelp.square;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.ehelp.ehelp.R;
import com.ehelp.ehelp.adapter.AskMsgAdapter;
import com.ehelp.ehelp.adapter.ReplyAdapter;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.message.MyMessageActivity;
import com.ehelp.ehelp.util.CircleImageView;
import com.ehelp.ehelp.util.MyCustomDialog5;
import com.ehelp.ehelp.util.NoScrollListView;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by UWTH on 2015/10/25.
 */
public class AskMsgDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;

    private CircleImageView iv_head;
    private TextView tv_name;
    private TextView tv_time;
    private TextView tv_title;
    private TextView tv_description;
    private CheckBox cb_thumb;
    private TextView tv_thumbnum;
    private TextView tv_replynum;
    private TextView tv_coinnum;
    private NoScrollListView lv_askreply;
    private ScrollView scrollView;
    private LinearLayout layout_response;
    private ImageView iv_delete;
    //    private LinearLayout layout_editreply;
//    private EditText et_reply;
//    private Button btn_send;
    private LinearLayout layout_thumb;
    private SharedPreferences sharedPref;
    private AskMsg askMsg;
    private ReplyAdapter replyAdapter;
    private List<Reply> replies;
    private int answer_id;

    // 需要加粗的字体
    private TextView tv_concern;
    private TextView tv_ans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_askmsg_detail);
        sharedPref = this.getSharedPreferences("user_id", Context.MODE_PRIVATE);

        init();

        layout_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_thumb.isChecked()) {
                    ManageLike(false);
                } else {
                    ManageLike(true);
                }
            }
        });

        layout_response.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AskMsgDetailActivity.this, ReplyAskActivity.class);
                intent.putExtra("data", askMsg);
                startActivity(intent);
            }
        });

//        btn_send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (et_reply.getText().toString().length() <= 0 || et_reply.getText().toString() == null) {
//                    Toast.makeText(AskMsgDetailActivity.this, "请编辑回答", Toast.LENGTH_SHORT).show();
//                } else {
//                    /*Toast.makeText(AskMsgDetailActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                    et_reply.setText("");
//                    if (imm != null) {
//                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
//                    }*/
//                    PostAnswer(et_reply.getText().toString());
//                }
//            }
//        });


        // 删除提问事件
        String launcherId = askMsg.getLauncher_id() + "";
        String myId = sharedPref.getString("id", "none");
        if (launcherId.equals(myId)) {
            iv_delete.setVisibility(View.VISIBLE);
        } else {
            iv_delete.setVisibility(View.GONE);
        }
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomDialog5 dialog = new MyCustomDialog5(AskMsgDetailActivity.this, "确定删除这条提问？",
                        new MyCustomDialog5.OnCustomDialogListener() {
                            @Override
                            public void back(boolean flag) {
                                if (flag) {
                                    deleteAskEvent();
                                }
                            }
                        });
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(this);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.askdetail));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void findView() {
        iv_head = (CircleImageView) findViewById(R.id.iv_head);
        tv_name = (TextView) findViewById(R.id.tv_nickname);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_description = (TextView) findViewById(R.id.tv_description);
        cb_thumb = (CheckBox) findViewById(R.id.cb_thumb);
        tv_thumbnum = (TextView) findViewById(R.id.tv_thumbnum);
        tv_replynum = (TextView) findViewById(R.id.tv_replynum);
        lv_askreply = (NoScrollListView) findViewById(R.id.lv_askreply);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        layout_response = (LinearLayout) findViewById(R.id.layout_response);
        layout_thumb = (LinearLayout) findViewById(R.id.layout_thumb);
        tv_coinnum = (TextView) findViewById(R.id.tv_coinnum);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);

        if (getIntent().getSerializableExtra("data") != null) {
            askMsg = (AskMsg) getIntent().getSerializableExtra("data");

        }
//        layout_editreply = (LinearLayout) findViewById(R.id.layout_editreply);
//        et_reply = (EditText) findViewById(R.id.et_reply);
//        btn_send = (Button) findViewById(R.id.btn_send);

        answer_id = -1;
    }

    private void init() {
        initToolbar();
        findView();

        // 手动把ScrollView滚动至最顶端
        scrollView.smoothScrollTo(0, 0);
        temp();

        setFontBold();
    }

    private void setFontBold() {
        tv_concern = (TextView) findViewById(R.id.tv_concern);
        tv_ans = (TextView) findViewById(R.id.tv_ans);
        tv_concern.getPaint().setFakeBoldText(true);
        tv_ans.getPaint().setFakeBoldText(true);
    }

    private void temp() {
        // 初始化提问回复列表
        replies = new ArrayList<>();
        replyAdapter = new ReplyAdapter(replies, R.layout.layout_reply, this);
        lv_askreply.setAdapter(replyAdapter);
    }

    private void getReplyData() {
        replies = new ArrayList<Reply>();
        getAnswers(-1);

    }

    private void UpdateReplyData() {
        getAnswers(answer_id);
    }

    private void ManageLike(final boolean trans) {

        // 目标URL
        String urlString = Config.HOST;
        String location = "/android/event/manage_like";

        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            jo.put("id", sharedPref.getString("id", "none"));
            jo.put("event_id", askMsg.getEvent_id());
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
                        Toast.makeText(AskMsgDetailActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", jsonObject.getString("status"));
                        editor.commit();
                        if (sharedPref.getString("status", "").equals("200")) {
                            if (!trans) {
                                cb_thumb.setChecked(false);
                                int num = Integer.parseInt(tv_thumbnum.getText().toString());
                                tv_thumbnum.setText((num - 1) + "");
                            } else {
                                cb_thumb.setChecked(true);
                                int num = Integer.parseInt(tv_thumbnum.getText().toString());
                                tv_thumbnum.setText((num + 1) + "");
                            }
                        } else {
                            Toast.makeText(AskMsgDetailActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();

    }

    private void getAnswers(final int answer_id) {

        // 目标URL
        String urlString = Config.HOST;
        String location = "/android/event/get_answers";
        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();

        try {
            jo.put("event_id", askMsg.getEvent_id());
            jo.put("id", sharedPref.getString("id", "none"));
            if (answer_id != -1) {
                jo.put("answer_id", answer_id);
            }


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
                    if (str.equals("")) {
                        Toast.makeText(AskMsgDetailActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        JSONObject Json = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", Json.getString("status"));
                        editor.commit();

                        if (sharedPref.getString("status", "none").equals("200")) {
                            JSONArray jsonArray = Json.getJSONArray("answer_list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Reply temp = new Reply(jsonArray.getJSONObject(i));
                                AskMsgDetailActivity.this.answer_id = temp.getAnswer_id();
                                replies.add(temp);
                            }
                            replyAdapter.refresh(replies);
                        } else {
                            Toast.makeText(AskMsgDetailActivity.this, "读取回复失败", Toast.LENGTH_SHORT).show();
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

    private void PostAnswer(String content) {

        // 目标URL
        String urlString = Config.HOST;
        String location = "/android/event/add_answer";
        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();

        try {
            jo.put("event_id", askMsg.getEvent_id());
            jo.put("id", sharedPref.getString("id", "none"));
            jo.put("content", content);


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
                        Toast.makeText(AskMsgDetailActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        JSONObject Json = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", Json.getString("status"));
                        editor.commit();

                        if (sharedPref.getString("status", "none").equals("200")) {
                            //et_reply.setText("");
                            //int temp = Integer.parseInt(tv_replynum.getText().toString());
                            //temp++;
                            //tv_replynum.setText(temp+"");
                            getReplyData();
                        } else {
                            Toast.makeText(AskMsgDetailActivity.this, "回复失败", Toast.LENGTH_SHORT).show();
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

    private void deleteAskEvent() {
        // 目标URL
        String urlString = Config.HOST;
        String location = "/android/";
        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();

        try {
            jo.put("event_id", askMsg.getEvent_id());
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
                        Toast.makeText(AskMsgDetailActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        JSONObject Json = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("status", Json.getString("status"));
                        editor.commit();

                        if (sharedPref.getString("status", "none").equals("200")) {
                            AskMsgDetailActivity.this.finish();
                        } else {
                            Toast.makeText(AskMsgDetailActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();
//        if (getIntent().getExtras().getString("reply") != null) {
//            String ans = getIntent().getExtras().getString("reply");
//            //Toast.makeText(this, ans, Toast.LENGTH_SHORT).show();
//            PostAnswer(ans);
//        }

        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
        Log.d("TPush", "onResumeXGPushClickedResult:" + click);
        if (click != null) { // 判断是否来自信鸽的打开方式
            String str = click.getCustomContent();
            askMsg = new AskMsg();
            try {
                JSONObject jsonObject = new JSONObject(str);
                askMsg.setName(jsonObject.getString("launcher"));
                askMsg.setEvent_id(jsonObject.getInt("event_id"));
                askMsg.setReplynum(jsonObject.getInt("support_number"));
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("time"));
                String time = new SimpleDateFormat("MM-dd HH:mm").format(date);
                askMsg.setTime(time);
                askMsg.setThumbnum(jsonObject.getInt("follow_number"));
                askMsg.setHead(R.mipmap.head);
                askMsg.setTitle(jsonObject.getString("title"));
                askMsg.setIs_like(jsonObject.getInt("is_like"));
                askMsg.setContent(jsonObject.getString("content"));
                askMsg.setLove_coin(jsonObject.getInt("love_coin"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        replyAdapter.setNickname(askMsg.getName());
        iv_head.setImageResource(R.mipmap.head);
        tv_name.setText(askMsg.getName());
        tv_time.setText(askMsg.getTime());
        tv_title.setText(askMsg.getTitle());
        tv_title.getPaint().setFakeBoldText(true);
        cb_thumb.setChecked(askMsg.getIsLike());
        tv_thumbnum.setText(askMsg.getThumbnum() + "");
        tv_replynum.setText(askMsg.getReplynum() + "");
        tv_description.setText(askMsg.getContent());
        tv_coinnum.setText(askMsg.getLove_coin() + "");

        if (getIntent().getStringExtra("from") != null &&
                getIntent().getStringExtra("from").equals("replyAsk")) {
            tv_replynum.setText((askMsg.getReplynum()+1) + "");
        }

        getReplyData();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // must store the new intent unless getIntent() will return the old one
        setIntent(intent);
    }

}
