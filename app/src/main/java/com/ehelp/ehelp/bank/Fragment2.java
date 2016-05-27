package com.ehelp.ehelp.bank;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.ehelp.ehelp.R;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by jeese on 2015/10/13.
 */
public class Fragment2 extends Fragment {
    MaterialDialog dialog;
    private SharedPreferences sharedPref;

    private ListView listview;

    final ArrayList<HashMap<String, Object>> listItems=new ArrayList<HashMap<String, Object>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lbf2, container, false);//关联布局文件
        sharedPref = getActivity().getSharedPreferences("user_id", Context.MODE_PRIVATE);
        listview = (ListView) rootView.findViewById(R.id.listview);
        init();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void init(){
        SimpleAdapter simpleAdapter=new SimpleAdapter(getActivity(), listItems, R.layout.layout_lovingbank_record,
                new String[]{"name","time","num"},
                new int[]{R.id.name,R.id.time, R.id.num});

        listview.setAdapter(simpleAdapter);
        loadData();
    }

    private void loadData() {
        dialog = new MaterialDialog.Builder(getActivity())
                .theme(Theme.LIGHT)
                .title("正在加载")
                .content("请稍候...")
                .progress(true, 0)
                .cancelable(false)
                .show();

        String urlString = Config.HOST;
        String location = "/android/lovingbank/get_trade_coin_record";

        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            jo.put("id", Integer.parseInt(sharedPref.getString("id", "none")));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHelper httphelper = new HttpHelper(urlString, jo, new IResponse() {
            @Override
            public void onResponse(Object content) {
                if (content != null) {
                    String str = ((BackPack) content).getBody();
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        if (jsonObject.getString("status").equals("200")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("trade_record");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                HashMap<String, Object> listItem = new HashMap<String, Object>();
                                if(jsonArray.getJSONObject(i).getInt("love_coin")>=0){
                                    listItem.put("name","你响应的求助");
                                }else{
                                    listItem.put("name", "你发出的求助");
                                }
                                listItem.put("time", jsonArray.getJSONObject(i).getString("time"));
                                listItem.put("num","" + jsonArray.getJSONObject(i).getInt("love_coin"));
                                listItems.add(listItem);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                dialog.cancel();
                showData();
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();
    }

    private void showData(){
        SimpleAdapter simpleAdapter=new SimpleAdapter(getActivity(), listItems, R.layout.layout_lovingbank_record,
                new String[]{"name","time","num"},
                new int[]{R.id.name,R.id.time, R.id.num});

        listview.setAdapter(simpleAdapter);
    }

}
