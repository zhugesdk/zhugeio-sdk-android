package com.zhuge.zhugeiodemo.fragment;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zhuge.analysis.stat.ZhugeSDK;
import com.zhuge.analysis.stat.exp.entities.ViewExposeData;
import com.zhuge.zhugeiodemo.R;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class CategoryFragment extends Fragment {

    Button buttona;
    Button buttonb;
    Button buttonc;
    Button buttond;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        buttona = (Button) rootView.findViewById(R.id.aaaa);
        ViewExposeData exposeData = new ViewExposeData(buttona, "exp-buttona");
        JSONObject prop = new JSONObject();
        try {
            prop.put("key","value");
        } catch (Exception e) {

        }
        exposeData.setProp(prop);
        ZhugeSDK.getInstance().viewExpTrack(exposeData);

        buttonb = (Button) rootView.findViewById(R.id.bbbb);
        buttonc = (Button) rootView.findViewById(R.id.cccc);
        buttond = (Button) rootView.findViewById(R.id.dddd);

        buttona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Category ButtonA ->","QAQ");
            }
        });

        buttonb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Category ButtonB ->","QAQ");
            }
        });

        buttonc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Category ButtonC ->","QAQ");
            }
        });

        buttond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Category ButtonD ->","QAQ");
            }
        });
        return rootView;
    }

}