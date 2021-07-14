package com.zhuge.analysis.stat;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.zhuge.analysis.util.ZGLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Omen on 2018/11/1.
 */
public class CachedBitmap {

    final int mClientDensity = DisplayMetrics.DENSITY_DEFAULT;

    /**
     * 用于存储生成的图像
     */
    private Bitmap mCached;
    private final Paint mPaint;
    private static final String TAG = "CachedBitMap";
    private final Paint masaicPaint;
    private float scale = 1.0f;

    public CachedBitmap() {
        mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
        mCached = null;
        masaicPaint = new Paint();
        masaicPaint.setColor(Color.BLACK);
        masaicPaint.setStyle(Paint.Style.FILL);
    }


    /**
     * 重绘图像
     * @param width 图像宽度
     * @param height 图像高度
     * @param destDensity 像素密度
     * @param source 源图像
     */
    private void recreate(int width, int height, int destDensity, Bitmap source) {
        if (null == mCached || mCached.getWidth() != width || mCached.getHeight() != height) {
            try {
                //若没有图片对象，生成一个新的对象
                mCached = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            } catch (final OutOfMemoryError e) {
                mCached = null;
            }
            if (null != mCached) {
                mCached.setDensity(destDensity);
            }
        }
        if (null != mCached) {
            final Canvas scaledCanvas = new Canvas(mCached);
            scaledCanvas.drawBitmap(source, 0, 0, mPaint);
        }
    }


    /**
     * 将图像转换为base64编码的字符串
     * @param rootView 根view
     * @param drawMosaic Bool
     * @param editableViewLocation 可编辑的view位置集合
     * @param currentSize 当前集合内可用数据的长度
     * @return base64String bitMap
     */
    public String getBase64StringFromView(View rootView,boolean drawMosaic,JSONArray editableViewLocation, int currentSize){
        tackScreenshot(rootView);
        if (drawMosaic){
            drawMosaicInCache(editableViewLocation,currentSize);
        }
        if (null == mCached || mCached.getWidth() == 0 || mCached.getHeight() == 0){
            return null;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mCached.compress(Bitmap.CompressFormat.JPEG,20,stream);
        byte[] bytes = stream.toByteArray();
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }


    /**
     * 将指定view的显示内容存储在mCached中
     * @param rootView 根view
     */
    public void tackScreenshot(View rootView) {
        if (rootView == null){
            return ;
        }
        Bitmap rawBitmap = null;

        //尝试view自身的截图方法
        try {
            @SuppressLint("PrivateApi")
            final Method createSnapshot = View.class.getDeclaredMethod("createSnapshot", Bitmap.Config.class, Integer.TYPE, Boolean.TYPE);
            createSnapshot.setAccessible(true);
            rawBitmap = (Bitmap) createSnapshot.invoke(rootView, Bitmap.Config.RGB_565, Color.WHITE, false);
            ZGLogger.logError(TAG,"create screenshot with createSnapshot Method");
        } catch (final NoSuchMethodException e) {
            ZGLogger.logError(TAG, "Can't call createSnapshot, will use drawCache " + e.toString());
        } catch (final IllegalArgumentException e) {
            ZGLogger.logError(TAG, "Can't call createSnapshot with arguments " + e.toString());
        } catch (final InvocationTargetException e) {
            ZGLogger.logError(TAG, "Exception when calling createSnapshot " + e.toString());
        } catch (final IllegalAccessException e) {
            ZGLogger.logError(TAG, "Can't access createSnapshot, using drawCache " + e.toString());
        } catch (final ClassCastException e) {
            ZGLogger.logError(TAG, "createSnapshot didn't return a bitmap? " + e.toString());
        }

        Boolean originalCacheState = null;
        try {
            if (null == rawBitmap) {
                originalCacheState = rootView.isDrawingCacheEnabled();
                rootView.setDrawingCacheEnabled(true);
                rootView.buildDrawingCache(true);
                rawBitmap = rootView.getDrawingCache();
                ZGLogger.logVerbose("create screenshot with drawing cache");
            }
        } catch (final RuntimeException e) {
            ZGLogger.logError(TAG, "Can't take a bitmap snapshot of view " + rootView + ", skipping for now. " + e.toString());
        }

        if (null != rawBitmap) {
            final int rawDensity = rawBitmap.getDensity();
            if (rawDensity != Bitmap.DENSITY_NONE) {
                scale = ((float) mClientDensity) / rawDensity;
            }

            final int rawWidth = rawBitmap.getWidth();
            final int rawHeight = rawBitmap.getHeight();
            final int destWidth = (int) ((rawBitmap.getWidth() * scale) + 0.5);
            final int destHeight = (int) ((rawBitmap.getHeight() * scale) + 0.5);
            if (rawWidth > 0 && rawHeight > 0 && destWidth > 0 && destHeight > 0) {
                recreate(destWidth, destHeight, mClientDensity, rawBitmap);
            }
        }

        if (null != originalCacheState && !originalCacheState) {
            rootView.setDrawingCacheEnabled(false);
        }
    }

    private void drawMosaicInCache(JSONArray editableTextRects , int currentSize) {
        if (currentSize > editableTextRects.length()){
            return;
        }
        try {
            Canvas canvas = new Canvas(mCached);
            for (int i=0;i<currentSize;i++){
                JSONObject item = editableTextRects.getJSONObject(i);
                float x = item.getInt("x") * scale;
                float y = item.getInt("y") * scale;
                float width = item.getInt("w")*scale;
                float height = item.getInt("h")*scale;
                RectF rect = new RectF(x,y,x+width,y+height);
                canvas.drawRect(rect,masaicPaint);
            }

        }catch (Exception e){
            ZGLogger.handleException(TAG,"drawMosaic error",e);
        }
    }


}
