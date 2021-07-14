package com.zhuge.zhugeiodemo.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.zhuge.analysis.stat.ZhugeSDK;
import com.zhuge.analysis.stat.exp.entities.ViewExposeData;
import com.zhuge.zhugeiodemo.R;
import com.zhuge.zhugeiodemo.Utils.Tracker;

import org.json.JSONException;
import org.json.JSONObject;

public class XSIdentifyActivity extends AppCompatActivity implements OnClickListener{

    Context mContext;
    EditText name;
    EditText prop1;
    EditText prop2;
    EditText prop3;
    EditText prop4;
    EditText value1;
    EditText value2;
    EditText value3;
    EditText value4;
    Button track;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify);

        mContext = getApplicationContext();
        name = (EditText) findViewById(R.id.name);
        prop1 = (EditText) findViewById(R.id.prop1);
        prop2 = (EditText) findViewById(R.id.prop2);
        prop3 = (EditText) findViewById(R.id.prop3);
        prop4 = (EditText) findViewById(R.id.prop4);
        value1 = (EditText) findViewById(R.id.value1);
        value2 = (EditText) findViewById(R.id.value2);
        value3 = (EditText) findViewById(R.id.value3);
        value4 = (EditText) findViewById(R.id.value4);
        track = (Button) findViewById(R.id.track_user);

        ViewExposeData exposeData = new ViewExposeData(track, "exp-track_user");
        JSONObject prop = new JSONObject();
        try {
            prop.put("key","value");
        } catch (Exception e) {

        }
        exposeData.setProp(prop);
        ZhugeSDK.getInstance().viewExpTrack(exposeData);
    }


    @Override
    public void onClick(View view) {

        String name = getName();
        JSONObject prop = getProp();

        switch (view.getId()) {

            case R.id.track_user:
                this.trackUser(name, prop);
                break;
            default:
                break;
        }
    }

    private void trackUser(String eventName, JSONObject pro) {
//        JSONObject pro = new JSONObject();
//        try {
//            pro.put("name","sunny");
//            pro.put("age","11");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Tracker.identify(this,eventName,pro);

    }


    private String getName(){
        String nameText = name.getText().toString();
        if (TextUtils.isEmpty(nameText)){
            return null;
        }
        return nameText;
    }

    /**
     * 对应的输入框获取对应的值
     * @return json prop
     */
    private JSONObject getProp(){
        String property1 = prop1.getText().toString();
        String property2 = prop2.getText().toString();
        String property3 = prop3.getText().toString();
        String property4 = prop4.getText().toString();
        String valueText1 = value1.getText().toString();
        String valueText2 = value2.getText().toString();
        String valueText3 = value3.getText().toString();
        String valueText4 = value4.getText().toString();
        JSONObject pro = new JSONObject();
        try {
            if (property1.length()!= 0 ){
                pro.put(property1,valueText1);
            }
            if (property2.length()!= 0 ){
                pro.put(property2,valueText2);
            }
            if (property3.length()!= 0 ){
                pro.put(property3,valueText3);
            }

            if (property4.length()!=0) {
                pro.put(property4,Double.valueOf(valueText4));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pro;
    }

}