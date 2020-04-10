package org.demo.handler;

/**
 * create by TIAN FENG on 2020/4/10
 */
public class PlatformUtil {

    private static Platform platform = Platform.ANDROID;

    public static void setPlatform(Platform platform) {
        PlatformUtil.platform = platform;
    }

    public static void d(String tag, String msg) {
        platform.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        platform.e(tag, msg);
    }

    public static long currentTimeMillis() {
        return platform.currentTimeMillis();
    }

    public static boolean isAndroid() {
        return platform == Platform.ANDROID;
    }
}
