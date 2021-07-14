package com.zhuge.zhugeiodemo.fragment;

import android.content.Intent;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuge.zhugeiodemo.MainActivity;
import com.zhuge.zhugeiodemo.R;
import com.zhuge.zhugeiodemo.activity.WebViewActivity;
import com.zhuge.zhugeiodemo.activity.XSDefaultEventActivity;
import com.zhuge.zhugeiodemo.activity.XSIdentifyActivity;
import com.zhuge.zhugeiodemo.viewmodels.DefaultModel;
import com.zhuge.zhugeiodemo.views.ListViewDefaultAdapter;

import java.util.ArrayList;


public class IndexFragment extends Fragment  implements ListViewDefaultAdapter.MyClickListener{

    ListView mListView;
    ArrayList<DefaultModel> dataArray;
    ListViewDefaultAdapter mDefaultAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_index, container, false);

        setupDataArray();
        initListView(rootView);

        return rootView;
    }

    private void setupDataArray() {
        dataArray = new ArrayList<DefaultModel>();

        DefaultModel userModel = new DefaultModel("identify","保持对用户的跟踪,记录用户更多的属性信息，便于你更了解你的用户.",R.mipmap.u7);
        DefaultModel modelA = new DefaultModel("自定义事件","在你希望记录用户行为的位置，自定义事件采集",R.mipmap.page);
        DefaultModel modelB = new DefaultModel("收入事件","收入数据采集，自动记录收入事件以及事件属性",R.mipmap.push);
        DefaultModel modelC = new DefaultModel("在WebView中进行统计","如果你的页面中使用了WebView嵌入HTML,JS的代码，并且希望统计HTML中的事件，可以通过下面的文档来进行跨平台的统计。",R.mipmap.modify_info);

        dataArray.add(userModel);
        dataArray.add(modelA);
        dataArray.add(modelB);
        dataArray.add(modelC);

        dataArray.add(modelC);
        dataArray.add(modelC);
        dataArray.add(modelC);
        dataArray.add(modelB);
        dataArray.add(modelC);
        dataArray.add(modelA);
        dataArray.add(modelC);
        dataArray.add(modelA);
        dataArray.add(modelB);
    }

    private void initListView(View rootView) {
        mListView = rootView.findViewById(R.id.mlist_view);
        mDefaultAdapter = new ListViewDefaultAdapter(this.getContext(),dataArray,this);

        mListView.setAdapter(mDefaultAdapter);
    }


    @Override
    public void clickListener(View v) {
        // 点击之后的操作在这里写
//        Toast.makeText(this.getContext(),
//                "listview的内部的按钮被点击了！，位置是-->" + (Integer) v.getTag() + ",内容是-->"
//                        + dataArray.get((Integer) v.getTag()),
//                Toast.LENGTH_SHORT).show();

        switch ((Integer) v.getTag()) {
            case 0 :
                this.openIdentifyActivity();
                break;
            case 1 :
                this.openDefaultEventActivity();
                break;
            case 2 :
                this.openRevenueActivity();
                break;
            case 3 :
                this.openWebViewActivity();
                break;

            default:

        }
    }

    private void openIdentifyActivity() {
        Intent identify = new Intent(this.getContext(), XSIdentifyActivity.class);
        startActivity(identify);
    }

    private void openDefaultEventActivity() {
        Intent events = new Intent(this.getContext(), XSDefaultEventActivity.class);
        startActivity(events);
    }

    private void openRevenueActivity() {
//        Intent revenue = new Intent(this, XSRevenueActivity.class);
//        startActivity(revenue);
    }

    private void openWebViewActivity() {
        Intent webview = new Intent(this.getContext(), WebViewActivity.class);
        startActivity(webview);
    }
}