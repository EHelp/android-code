package com.ehelp.ehelp.personcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.ehelp.ehelp.R;
import com.ehelp.ehelp.httpclient.BackPack;
import com.ehelp.ehelp.httpclient.Config;
import com.ehelp.ehelp.httpclient.HttpHelper;
import com.ehelp.ehelp.httpclient.IResponse;
import com.ehelp.ehelp.main.MainActivity;
import com.ehelp.ehelp.util.CircleImageView;
import com.ehelp.ehelp.util.MyCustomDialog;
import com.ehelp.ehelp.util.MyCustomDialog2;
import com.ehelp.ehelp.util.MyCustomDialog3;
import com.ehelp.ehelp.util.MyCustomDialog4;
import com.ehelp.ehelp.util.OccupationEncode;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by UWTH on 2015/10/24.
 */
public class PersonCenterActivity extends AppCompatActivity implements GeocodeSearch.OnGeocodeSearchListener {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button toolbar_navigationIcon;
    //private LinearLayout bsLayout;
    //private LinearLayout healthLayout;

    private TextView tv_username;
    private TextView tv_phonenum;
    private TextView tv_email;
    private TextView tv_address;
    private TextView tv_realname;
    private TextView tv_occupation;
    private TextView tv_sex;
    private TextView tv_creditvalue;
    private TextView tv_coinnum;
    private TextView tv_helpingtime;
    private TextView tv_anaphylaxis;
    private TextView tv_druguse;
    private TextView tv_medicalhistory;

    // 需要加粗的字体
    //private TextView tv_head2;
    private TextView tv_username2;
    private TextView tv_phonenum2;
    private TextView tv_email2;
    private TextView tv_address2;
    private TextView tv_realname2;
    private TextView tv_occupation2;
    private TextView tv_sex2;
    private TextView tv_creditvalue2;
    private TextView tv_coinnum2;
    private TextView tv_helpingtime2;
    private TextView tv_anaphylaxis2;
    private TextView tv_druguse2;
    private TextView tv_medicalhistory2;
    private TextView tv_basicinfo;
    private TextView tv_ehelpinfo;
    private TextView tv_healthinfo;

    //private CircleImageView iv_head;
    //private Bitmap head;
    private SharedPreferences sharedPref;

    private GeocodeSearch geocoderSearch;

    private static String path = "/sdcard/eHelp/";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personcenter);
        sharedPref = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        init();
        getInfo();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置标题
        toolbar.setTitle("");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.personcenter));
        setSupportActionBar(toolbar);
        // 设置左边按钮
        toolbar.setNavigationIcon(null);
        toolbar_navigationIcon = (Button) findViewById(R.id.toolbar_navigationIcon);
        toolbar_navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonCenterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findView() {
        tv_username = (TextView) findViewById(R.id.tv_nickname);
        tv_phonenum = (TextView) findViewById(R.id.tv_phone);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_occupation = (TextView) findViewById(R.id.tv_occupation);
        tv_realname = (TextView) findViewById(R.id.tv_realname);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_creditvalue = (TextView) findViewById(R.id.tv_creditvalue);
        tv_coinnum = (TextView) findViewById(R.id.tv_coinnum);
        tv_helpingtime = (TextView) findViewById(R.id.tv_helpingtime);
        tv_anaphylaxis = (TextView) findViewById(R.id.tv_anaphylaxis);
        tv_druguse = (TextView) findViewById(R.id.tv_druguse);
        tv_medicalhistory = (TextView) findViewById(R.id.tv_medicalhistory);

        //iv_head = (CircleImageView) findViewById(R.id.iv_head);

        //bsLayout = (LinearLayout) findViewById(R.id.bsLayout);
        //healthLayout = (LinearLayout) findViewById(R.id.healthLayout);

        // 需要加粗的字体
        //tv_head2 = (TextView) findViewById(R.id.tv_head2);
        tv_username2 = (TextView) findViewById(R.id.tv_nickname2);
        tv_phonenum2 = (TextView) findViewById(R.id.tv_phone2);
        tv_email2 = (TextView) findViewById(R.id.tv_email2);
        tv_address2 = (TextView) findViewById(R.id.tv_address2);
        tv_occupation2 = (TextView) findViewById(R.id.tv_occupation2);
        tv_realname2 = (TextView) findViewById(R.id.tv_realname2);
        tv_sex2 = (TextView) findViewById(R.id.tv_sex2);
        tv_creditvalue2 = (TextView) findViewById(R.id.tv_creditvalue2);
        tv_coinnum2 = (TextView) findViewById(R.id.tv_coinnum2);
        tv_helpingtime2 = (TextView) findViewById(R.id.tv_helpingtime2);
        tv_anaphylaxis2 = (TextView) findViewById(R.id.tv_anaphylaxis2);
        tv_druguse2 = (TextView) findViewById(R.id.tv_druguse2);
        tv_medicalhistory2 = (TextView) findViewById(R.id.tv_medicalhistory2);
        tv_basicinfo = (TextView) findViewById(R.id.tv_basicinfo);
        tv_ehelpinfo = (TextView) findViewById(R.id.tv_ehelpinfo);
        tv_healthinfo = (TextView) findViewById(R.id.tv_healthinfo);
    }

    private void init() {
        geocoderSearch = new GeocodeSearch(PersonCenterActivity.this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        findView();
        initToolbar();
        setBoldText();

//        Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");//从Sd中找头像，转换成Bitmap
//        if (bt != null) {
//            @SuppressWarnings("deprecation")
//            Drawable drawable = new BitmapDrawable(bt);//转换成drawable
//            iv_head.setImageDrawable(drawable);
//        } else {
//            /**
//             *  如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
//             *
//             */
//        }
    }

    private void setBoldText() {
        //tv_head2.getPaint().setFakeBoldText(true);
        tv_username2.getPaint().setFakeBoldText(true);
        tv_phonenum2.getPaint().setFakeBoldText(true);
        tv_email2.getPaint().setFakeBoldText(true);
        tv_address2.getPaint().setFakeBoldText(true);
        tv_occupation2.getPaint().setFakeBoldText(true);
        tv_realname2.getPaint().setFakeBoldText(true);
        tv_sex2.getPaint().setFakeBoldText(true);
        tv_creditvalue2.getPaint().setFakeBoldText(true);
        tv_coinnum2.getPaint().setFakeBoldText(true);
        tv_helpingtime2.getPaint().setFakeBoldText(true);
        tv_anaphylaxis2.getPaint().setFakeBoldText(true);
        tv_druguse2.getPaint().setFakeBoldText(true);
        tv_medicalhistory2.getPaint().setFakeBoldText(true);
        tv_basicinfo.getPaint().setFakeBoldText(true);
        tv_ehelpinfo.getPaint().setFakeBoldText(true);
        tv_healthinfo.getPaint().setFakeBoldText(true);
    }

    /*public void click(View view) {
        switch (view.getId()) {
            case R.id.layout_basicinfo: {
                if (bsLayout.getVisibility() == View.VISIBLE) {
                    bsLayout.setVisibility(View.GONE);
                } else {
                    bsLayout.setVisibility(View.VISIBLE);
                }
                break;
            }
            case R.id.layout_healthcard: {
                if (healthLayout.getVisibility() == View.VISIBLE) {
                    healthLayout.setVisibility(View.GONE);
                } else {
                    healthLayout.setVisibility(View.VISIBLE);
                }
                break;
            }
        }
    }*/

    public void onMsgClick(View view) {
        switch (view.getId()) {
            case R.id.layout_nickname: {
                MyCustomDialog dialog = new MyCustomDialog(this, "修改用户名",
                        new MyCustomDialog.OnCustomDialogListener() {
                            @Override
                            public void back(String name) {
                                if (name.equals("")) {
                                    Toast.makeText(PersonCenterActivity.this, "不能使用该用户名", Toast.LENGTH_SHORT).show();
                                } else {
                                    UpdateInfo("nickname", name);
//                                    if (sharedPref.getString("status", "none").equals("200"))
//                                    tv_username.setText(name);
                                }
                            }
                        });
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
                break;
            }
            case R.id.layout_address: {
                MyCustomDialog dialog = new MyCustomDialog(this, "修改住址",
                        new MyCustomDialog.OnCustomDialogListener() {
                            @Override
                            public void back(String name) {
                                UpdateInfo("location", name);
//                                tv_address.setText(name);
                            }
                        });
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
                break;
            }
            case R.id.layout_occupation: {
                MyCustomDialog2 dialog = new MyCustomDialog2(this, "修改职业",
                        new MyCustomDialog2.OnCustomDialogListener() {
                            @Override
                            public void back(String name) {
                                UpdateInfo("occupation", OccupationEncode.OccupationToInt(name));
                            }
                        });
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
                break;
            }
            case R.id.layout_sex: {
                MyCustomDialog4 dialog = new MyCustomDialog4(this, "修改性别",
                        new MyCustomDialog4.OnCustomDialogListener() {
                            @Override
                            public void back(String name) {
                                if (name.equals("男"))
                                    UpdateInfo("gender", 1);
                                else
                                    UpdateInfo("gender", 2);
                            }
                        });
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
                break;
            }
            case R.id.layout_anaphylaxis: {
                MyCustomDialog dialog = new MyCustomDialog(this, "修改过敏反应信息",
                        new MyCustomDialog.OnCustomDialogListener() {
                            @Override
                            public void back(String name) {
                                UpdateInfo("anaphylaxis", name);
                            }
                        });
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
                break;
            }
            case R.id.layout_druguse: {
                MyCustomDialog dialog = new MyCustomDialog(this, "修改药物使用信息",
                        new MyCustomDialog.OnCustomDialogListener() {
                            @Override
                            public void back(String name) {
                                UpdateInfo("medicine_taken", name);
                            }
                        });
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
                break;
            }
            case R.id.layout_medicalhistory: {
                MyCustomDialog dialog = new MyCustomDialog(this, "修改病史",
                        new MyCustomDialog.OnCustomDialogListener() {
                            @Override
                            public void back(String name) {
                                UpdateInfo("medical_history", name);
                            }
                        });
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
                break;
            }
        }
    }

    public void onHeadClick(View view) {
        MyCustomDialog3 dialog = new MyCustomDialog3(this, "修改头像",
                new MyCustomDialog3.OnCustomDialogListener() {
                    @Override
                    public void back(String name) {
                        if (name.equals("拍照")) {
                            modifyHeadByTakephoto();
                        } else if (name.equals("本地")) {
                            modifyHeadByPhoto();
                        }
                    }
                });
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    public void modifyHeadByTakephoto() {
        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                "head.jpg")));
        startActivityForResult(intent2, 2);//采用ForResult打开
    }

    public void modifyHeadByPhoto() {
        Intent intent1 = new Intent(Intent.ACTION_PICK, null);
        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent1, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());//裁剪图片
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));//裁剪图片
                }
                break;
            case 3:
//                if (data != null) {
//                    Bundle extras = data.getExtras();
//                    head = extras.getParcelable("data");
//                    if (head != null) {
//                        /**
//                         * 上传服务器代码
//                         */
//                        setPicToView(head);//保存在SD卡中
//                        iv_head.setImageBitmap(head);//用ImageView显示出来
//                    }
//                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    ;

    /**
     * 调用系统的裁剪
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";//图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getInfo() {
        String urlString = Config.HOST;
        String location = "/android/user/get_information";

        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            jo.put("id", sharedPref.getString("id", "none"));
            jo.put("phone", sharedPref.getString("phone", "none"));
            jo.put("account", sharedPref.getString("account", "none"));


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
                        Toast.makeText(PersonCenterActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(str);

                        if (jsonObject.getString("status").equals("200")) {

                            tv_username.setText(jsonObject.getString("nickname"));
                            if (jsonObject.getInt("is_verify") == 1)
                                tv_realname.setText(jsonObject.getString("name"));
                            if (jsonObject.getInt("gender") == 2) {
                                tv_sex.setText("女");
                            } else {
                                tv_sex.setText("男");
                            }
                            tv_phonenum.setText(jsonObject.getString("phone"));
                            if (!jsonObject.getString("email").equals("null"))
                                tv_email.setText(jsonObject.getString("email"));
                            RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(jsonObject.getDouble("latitude"),
                                    jsonObject.getDouble("longitude")), 200, GeocodeSearch.AMAP);
                            geocoderSearch.getFromLocationAsyn(query);
                            if (!jsonObject.getString("location").equals("null"))
                                tv_address.setText(jsonObject.getString("location"));
                            String[] oc = new String[]{"其他", "无业游民", "工人", "白领", "工程师",
                                    "教师", "学生", "警察", "保安", "医生", "运动员", "农民"};
                            tv_occupation.setText(oc[jsonObject.getInt("occupation")]);
                            tv_creditvalue.setText(jsonObject.getDouble("reputation") + "");
                            tv_helpingtime.setText(jsonObject.getInt("support_number") + "");
                            tv_coinnum.setText(jsonObject.getJSONObject("love_coin").getInt("love_coin") + "");
                            if (!jsonObject.getString("anaphylaxis").equals("null")) {
                                tv_anaphylaxis.setText(jsonObject.getString("anaphylaxis"));
                            } else {
                                tv_anaphylaxis.setText("无");
                            }
                            if (!jsonObject.getString("medicine_taken").equals("null")) {
                                tv_druguse.setText(jsonObject.getString("medicine_taken"));
                            } else {
                                tv_druguse.setText("无");
                            }
                            if (!jsonObject.getString("medical_history").equals("null")) {
                                tv_medicalhistory.setText(jsonObject.getString("medical_history"));
                            } else {
                                tv_medicalhistory.setText("无");
                            }
                        } else {
                            Toast.makeText(PersonCenterActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();
    }

    private void UpdateInfo(final String itemname, final Object to) {
        String urlString = Config.HOST;
        String location = "/android/user/modify_information";

        urlString += location;
        // JSON对象，存放http请求参数
        JSONObject jo = new JSONObject();
        try {
            jo.put("id", sharedPref.getString("id", "none"));
            jo.put(itemname, to.toString());


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
                        Toast.makeText(PersonCenterActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        SharedPreferences.Editor editor = sharedPref.edit();

                        if (jsonObject.getString("status").equals("200")) {

                            Toast.makeText(PersonCenterActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            if (itemname.equals("nickname")) {
                                editor.putString("account", to.toString());
                                editor.commit();
                                String temp = "ehelp_";
                                temp += to.toString();
                                Context context = getApplicationContext();
                                XGPushManager.registerPush(context, temp);
                            }

                        } else {
                            Toast.makeText(PersonCenterActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                        }
                        getInfo();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, HttpHelper.HTTP_POST, null);
        httphelper.execute();
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        String recent_address;
        if (i != 0) {
            recent_address = "未找到地址";
        } else {
            String city = regeocodeResult.getRegeocodeAddress().getCity();
            String district = regeocodeResult.getRegeocodeAddress().getDistrict();
            String neighborhood = regeocodeResult.getRegeocodeAddress().getNeighborhood();
            String building = regeocodeResult.getRegeocodeAddress().getBuilding();
            String crossroad = regeocodeResult.getRegeocodeAddress().getCrossroads().get(0).getFirstRoadName();
            String road = regeocodeResult.getRegeocodeAddress().getRoads().get(0).getName();
            String township = regeocodeResult.getRegeocodeAddress().getTownship();
            String formatAddress = regeocodeResult.getRegeocodeAddress().getFormatAddress();
            //Toast.makeText(this, city + district + neighborhood + road, Toast.LENGTH_SHORT).show();
            recent_address = city + district + neighborhood + road;
        }
        // Toast.makeText(this, recent_address, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
}
