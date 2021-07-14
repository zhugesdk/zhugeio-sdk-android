package com.zhuge.zhugeiodemo.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuge.analysis.stat.ZhugeSDK;
import com.zhuge.analysis.stat.exp.entities.ViewExposeData;
import com.zhuge.zhugeiodemo.R;
import com.zhuge.zhugeiodemo.viewmodels.DefaultModel;

import org.json.JSONObject;

import java.util.List;

public class ListViewDefaultAdapter extends BaseAdapter implements View.OnClickListener{

    private List<DefaultModel> mData;
    private LayoutInflater mInflater;
    private MyClickListener mListener;

    //自定义接口，用于回调按钮点击事件到Activity
    public interface MyClickListener{
        public void clickListener(View v);
    }


    public ListViewDefaultAdapter(Context context, List<DefaultModel> data, MyClickListener listener) {
        mData = data;
        mInflater = LayoutInflater.from(context);
        mListener = listener;

    }

    @Override
    public int getCount() {
        return mData.size();
    }


    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //获得ListView中的view
        View defaultView = mInflater.inflate(R.layout.default_item,null);

        DefaultModel model = mData.get(position);

        //获得自定义布局中每一个控件的对象。
        ImageView imagePhoto = (ImageView) defaultView.findViewById(R.id.img);
        TextView title = (TextView) defaultView.findViewById(R.id.tv1);
        TextView explanation = (TextView) defaultView.findViewById(R.id.tv2);

        //将数据一一添加到自定义的布局中。
        imagePhoto.setImageResource(model.getImage());
        title.setText(model.getTitle());
        explanation.setText(model.getExplanation());

        defaultView.setOnClickListener(this);
        defaultView.setTag(position);

        if (position == 4) {
            ViewExposeData exposeData = new ViewExposeData(defaultView, "exp-item4");
            JSONObject prop = new JSONObject();
            try {
                prop.put("key","value");
            } catch (Exception e) {

            }
            exposeData.setProp(prop);
            ZhugeSDK.getInstance().viewExpTrack(exposeData);
        }



        return defaultView ;
    }

    //响应按钮点击事件,调用子定义接口，并传入View
    @Override
    public void onClick(View v) {
        mListener.clickListener(v);
    }
}
