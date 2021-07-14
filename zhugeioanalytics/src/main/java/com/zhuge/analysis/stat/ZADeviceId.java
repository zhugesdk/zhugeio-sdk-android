package com.zhuge.analysis.stat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.zhuge.analysis.util.Utils;
import com.zhuge.analysis.util.WifiInfoUtils;
import com.zhuge.analysis.util.ZhugeioUtils;

import java.util.UUID;

public class ZADeviceId {

    public static String getDeviceId(Context context) {

        return generateDid(context);
    }

    /**
     * 使用设备信息字段来生成UUID
     * @return 设备唯一标识did
     */
    private static String generateDid(Context context) {

        String imei = ZhugeioUtils.getIMEI(context);

//        String mac = ZhugeioUtils.getMacAddress(context);

        String android_id = ZhugeioUtils.getAndroidID(context);

        // 如果没有imei 使用设备应硬件信息  生成随机 UUID
        // 设备序列号  Android 10 取不到 暂时用AndroiID代替
        String serial = ZhugeioUtils.getSerialNo(context);

        String board = Build.BOARD; // 基板
        String brand = Build.BRAND; // 品牌
        String cpu = Build.CPU_ABI; // CPU
        String device = Build.DEVICE; // 驱动
        String display = Build.DISPLAY; //DISPLAY
        String host = Build.HOST; // 主机地址
        String id = Build.ID; //版本号
        String manufacturer = Build.MANUFACTURER; //制造商
        String model = Build.MODEL; //型号
        String product = Build.PRODUCT; //产品
        String tags = Build.TAGS; //标签
        String type = Build.TYPE; //版本类型
        String user = Build.USER; //用户版 厂商或系统版本不同值可能会不一样
        String info = "35" +
                board.length() % 10 + brand.length() % 10 +
                cpu.length() % 10 + device.length() % 10 +
                display.length() % 10 + host.length() % 10 +
                id.length() % 10 + manufacturer.length() % 10 +
                model.length() % 10 + product.length() % 10 +
                tags.length() % 10 + type.length() % 10 +
                user.length() % 10;

        if (serial.equals("") || serial == "unknown") {
            serial = "zhuge.serial" + android_id;
        }
        return new UUID(info.hashCode(), serial.hashCode()).toString();
    }

}
