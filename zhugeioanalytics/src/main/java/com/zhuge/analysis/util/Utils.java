package com.zhuge.analysis.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 其他工具类
 * Created by Omen on 15/11/9.
 */
public class Utils {
    private static final String TAG = "com.zhuge.Utils";
    public static  String publicKey = "";

    /**
     * md5加密
     *
     * @param string 待加密字符
     * @return 加密字符
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
    public static String publicKeyEncodeRSA(byte[] str) {
        // 4.公钥加密
        if (TextUtils.isEmpty(publicKey)){
            Log.e(TAG,"public key is null");
            return null;
        }
        byte[] result = null;
        try {
            byte[] key = Base64.decode(publicKey,Base64.DEFAULT);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            result = cipher.doFinal(str);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return Base64.encodeToString(result,Base64.NO_WRAP);
    }

    public static int getTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getRawOffset();
    }

    /**
     * @param obj 要克隆的对象
     * @return 克隆的对象
     */
    public static JSONObject conversionRevenuePropertiesKey(final JSONObject obj) {
        if (obj == null) {
            return null;
        }
        // obj.names returns null if the json obj is empty.
        JSONArray nameArray = null;
        try {
            nameArray = obj.names();
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e(TAG, e.toString());
        }
        int len = (nameArray != null ? nameArray.length() : 0);
        try {
            JSONObject copy = new JSONObject();
            for (int i = 0; i < len; i++) {
                String key = nameArray.optString(i);
                Object value = obj.opt(key);
                StringBuilder builder = new StringBuilder(key);
                builder.insert(0,'$');
                copy.put(builder.toString(),value);
            }
            return copy;
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    /**
     * 浅拷贝，能减小一点发生异常的概率
     * @param obj 要克隆的对象
     * @return 克隆的对象
     */
    public static JSONObject cloneJSONObject(final JSONObject obj) {
        if (obj == null) {
            return null;
        }

        // obj.names returns null if the json obj is empty.
        JSONArray nameArray;
        try {
            nameArray = obj.names();
            int len = (nameArray != null ? nameArray.length() : 0);
            JSONObject copy = new JSONObject();
            for (int i = 0; i < len; i++) {
                String key = nameArray.optString(i);
                Object value = obj.opt(key);
                StringBuilder builder = new StringBuilder(key);
                if (key.contains("$") == false) {
                    builder.insert(0,'_');
                }
                copy.put(builder.toString(),value);
            }
            return copy;
        } catch (Exception e) {
            ZGLogger.handleException(TAG,"clone json error.",e);
            return null;
        }
    }

    public static String timeStamp2Date (long time){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
        return dateFormat.format(new Date(time));
    }
    public static String parseUrl(String url , String checkPoint,String endpoint){
        if (url == null || url.trim().length() == 0){
            return null;
        }
        String tempUrl = url.toLowerCase(Locale.ENGLISH);
        StringBuilder sb = new StringBuilder(tempUrl);
        if (sb.charAt(sb.length()-1) == '/'){
            sb.deleteCharAt(url.length()-1);
        }
        int index = sb.lastIndexOf(checkPoint);
        if (index != -1){
            sb.delete(index,sb.length());
        }
        if (sb.charAt(sb.length()-1) == '/'){
            sb.append(endpoint);
        }else {
            sb.append("/")
                    .append(endpoint);
        }
        return sb.toString();
    }
    public static SecretKey getAESKey() {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128);
            return kgen.generateKey();
        } catch (Exception e) {
            ZGLogger.handleException(TAG,"getAESKey error.",e);
        }
        return null;
    }

    /**
     * AES 加密
     * @param secretKey 加密密钥
     * @param bytes 待加密数据
     * @return base64编码加密数据
     */
    public static String AESEncrypt(SecretKey secretKey, byte[] bytes) {
        if (secretKey == null || bytes == null){
            ZGLogger.logError(TAG,"AES Key is null.");
            return null;
        }
        try {

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//            cipher.init(Cipher.ENCRYPT_MODE,secretKey,new IvParameterSpec(secretKey.getEncoded()));
            cipher.init(Cipher.ENCRYPT_MODE,secretKey);
            byte[] result = cipher.doFinal(bytes);
            return Base64.encodeToString(result,Base64.NO_WRAP);
        }catch (Exception e){
            ZGLogger.handleException(TAG,"AES Encrypt error.",e);
        }
        return null;
    }

    public static byte[] compress(byte[] data){
        byte[] output = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
            Deflater deflater = new Deflater();
            DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(outputStream,deflater,8192);
            deflaterOutputStream.write(data);
            deflaterOutputStream.finish();
            deflaterOutputStream.close();
            output = outputStream.toByteArray();
        }catch (Exception e){
            ZGLogger.handleException(TAG,"compress error",e);
        }
        return output;
    }

    /**
     * 在图片上画出指定范围的矩形
     * @param bitmap 原始图片
     * @param array 坐标点
     */
    public void drawMosaic(Bitmap bitmap, JSONArray array){
        try {
            for (int i=0,len=array.length();i<len;i++){
                JSONObject item = array.getJSONObject(i);
                int x = item.getInt("x");
                int y = item.getInt("y");
                int width = item.getInt("width");
                int height = item.getInt("height");
                Canvas canvas = new Canvas();
                boolean mutable = bitmap.isMutable();
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setStyle(Paint.Style.FILL);
                Rect rect = new Rect(x,y,x+width,y+height);
                Bitmap map2 ;
                if (!mutable){
                    map2 = bitmap.copy(Bitmap.Config.ARGB_8888,true);
                }else {
                    map2 = bitmap;
                }
                canvas.drawBitmap(map2,null,rect,paint);
            }

        }catch (Exception e){
            ZGLogger.handleException(TAG,"drawMosaic get error",e);
        }
    }

    public static String arrayListToString(ArrayList list, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    public static String androidId(Context mContext) {
        String androidId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }


    /**
     * 检测权限
     *
     * @param context Context
     * @param permission 权限名称
     * @return true:已允许该权限; false:没有允许该权限
     */
    public static boolean checkHasPermission(Context context, String permission) {
        try {
            Class<?> contextCompat = null;
            try {
                contextCompat = Class.forName("android.support.v4.content.ContextCompat");
            } catch (Exception e) {
                //ignored
            }

            if (contextCompat == null) {
                try {
                    contextCompat = Class.forName("androidx.core.content.ContextCompat");
                } catch (Exception e) {
                    //ignored
                }
            }

            if (contextCompat == null) {
                return true;
            }

            Method checkSelfPermissionMethod = contextCompat.getMethod("checkSelfPermission", Context.class, String.class);
            int result = (int) checkSelfPermissionMethod.invoke(null, new Object[]{context, permission});
            if (result != PackageManager.PERMISSION_GRANTED) {
                ZGLogger.logVerbose("You can fix this by adding the following to your AndroidManifest.xml file:\n"
                        + "<uses-permission android:name=\"" + permission + "\" />");
                return false;
            }

            return true;
        } catch (Exception e) {
            ZGLogger.logError(TAG, e.toString());
            return true;
        }
    }

    public static boolean objectEquals(Object objL, Object objR) {
        return objL == objR || objL != null && objL.equals(objR);
    }
}


