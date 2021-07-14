package com.zhuge.zhugeiodemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.bun.miitmdid.core.ErrorCode;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;

import com.zhuge.zhugeiodemo.Utils.Tracker;
import com.zhuge.zhugeiodemo.fragment.CategoryFragment;
import com.zhuge.zhugeiodemo.fragment.IndexFragment;
import com.zhuge.zhugeiodemo.fragment.UserFragment;


public class MainActivity extends AppCompatActivity implements View.OnClickListener ,IIdentifierListener{

    private RadioButton mRb1, mRb2, mRb3;

    private FragmentManager fManager;
    private IndexFragment indexFragment;
    private CategoryFragment categoryFragment;
    private UserFragment userFragment;

    String oaid;
    String vaid;
    String aaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        drawableBounds();

        Tracker.setupZhugeioAnalytics(this);
//        Tracker.setupMixpanel(this);

//        String did = ZADeviceId.getDeviceId(this);
//        XSLog.i("za did == ",did);


        getDeviceIds(getApplicationContext());

        getOAID();
    }


    public void getDeviceIds(Context cxt){

        long timeb=System.currentTimeMillis();
        // 方法调用
        int nres = CallFromReflect(cxt);

        long timee=System.currentTimeMillis();
        long offset=timee-timeb;
        if(nres == ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT){//不支持的设备

        }else if( nres == ErrorCode.INIT_ERROR_LOAD_CONFIGFILE){//加载配置文件出错

        }else if(nres == ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT){//不支持的设备厂商

        }else if(nres == ErrorCode.INIT_ERROR_RESULT_DELAY){//获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程

        }else if(nres == ErrorCode.INIT_HELPER_CALL_ERROR){//反射调用出错

        }
        Log.d(getClass().getSimpleName(),"return value: "+String.valueOf(nres));

    }

    /*
     * 获取相应id
     *
     * */
    @Override
    public void OnSupport(boolean isSupport, IdSupplier _supplier) {
        if(_supplier==null) {
            return;
        }
        String oaid=_supplier.getOAID();
        String vaid=_supplier.getVAID();
        String aaid=_supplier.getAAID();
        StringBuilder builder=new StringBuilder();
        builder.append("support: ").append(isSupport?"true":"false").append("\n");
        builder.append("OAID: ").append(oaid).append("\n");
        builder.append("VAID: ").append(vaid).append("\n");
        builder.append("AAID: ").append(aaid).append("\n");
        String idstext=builder.toString();
        AppIdsUpdater _listener = null;
        if(_listener!=null){
            _listener.OnIdsAvalid(idstext);
        }
    }

    public interface AppIdsUpdater{
        void OnIdsAvalid(@NonNull String ids);
    }

    private int CallFromReflect(Context cxt){
        return MdidSdkHelper.InitSdk(cxt,true,this);
    }


    /**
     * 获取oaid
     */
    public void getOAID() {

        MdidSdkHelper.InitSdk(getApplicationContext(), true, new IIdentifierListener() {
            @Override
            public void OnSupport(boolean b, IdSupplier idSupplier) {
                if (idSupplier == null) {
                    return;
                }
                if (idSupplier.isSupported()) {
                    String oaid = idSupplier.getOAID();
                    Log.i("oaid == ",oaid);
                } else {
                }
            }
        });
    }



    private void initView() {
        mRb1 = findViewById(R.id.rb1);
        mRb2 = findViewById(R.id.rb2);
        mRb3 = findViewById(R.id.rb3);
        mRb1.setOnClickListener(this);
        mRb2.setOnClickListener(this);
        mRb3.setOnClickListener(this);
    }

    private void drawableBounds() {
        Drawable drawable1 = getResources().getDrawable(R.drawable.bottom_tab1);
        Drawable drawable2 = getResources().getDrawable(R.drawable.bottom_tab2);
        Drawable drawable3 = getResources().getDrawable(R.drawable.bottom_tab3);
        //第一个 0 是距左边距离，第二个 0 是距上边距离，50 分别是长宽
        drawable1.setBounds(0, 0, 50, 50);
        drawable2.setBounds(0, 0, 50, 50);
        drawable3.setBounds(0, 0, 50, 50);
        mRb1.setCompoundDrawables(null, drawable1, null, null);
        mRb2.setCompoundDrawables(null, drawable2, null, null);
        mRb3.setCompoundDrawables(null, drawable3, null, null);

        fManager = getSupportFragmentManager();
        setChoiceItem(1);
    }

    /**
     * Fragment切换
     */
    private void setChoiceItem(int index) {
        FragmentTransaction transaction = fManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 1:
                if (indexFragment == null) {
                    indexFragment = new IndexFragment();
                    transaction.add(R.id.main_frame_layout, indexFragment);
                } else {
                    transaction.show(indexFragment);
                }
                break;
            case 2:
                if (categoryFragment == null) {
                    categoryFragment = new CategoryFragment();
                    transaction.add(R.id.main_frame_layout, categoryFragment);
                } else {
                    transaction.show(categoryFragment);
                }
                break;
            case 3:
                if (userFragment == null) {
                    userFragment = new UserFragment();
                    transaction.add(R.id.main_frame_layout, userFragment);
                } else {
                    transaction.show(userFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 隐藏片段
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (indexFragment != null) {
            transaction.hide(indexFragment);
        }
        if (categoryFragment != null) {
            transaction.hide(categoryFragment);
        }
        if (userFragment != null) {
            transaction.hide(userFragment);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb1:
                setChoiceItem(1);
                break;
            case R.id.rb2:
                setChoiceItem(2);
                break;
            case R.id.rb3:
                setChoiceItem(3);
                break;
            default:
                break;
        }
    }

}
