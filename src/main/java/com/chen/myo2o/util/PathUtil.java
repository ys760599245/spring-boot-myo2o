package com.chen.myo2o.util;

import org.springframework.beans.factory.annotation.Value;

public class PathUtil {
    private static String seperator = System.getProperty("file.separator");

    private static String winPath;

    private static String linuxPath;
    private static String shopPath;

    @Value("${win.base.path}")
    public static void setWinPath(String winPath) {
        PathUtil.winPath = winPath;
    }

    @Value("${linux.base.path}")
    public static void setLinuxPath(String linuxPath) {
        PathUtil.linuxPath = linuxPath;
    }

    @Value("${shop.relevant.path}")
    public static void setShopPath(String shopPath) {
        PathUtil.shopPath = shopPath;
    }

    public static String getImgBasePath() {
        String os = System.getProperty("os.name");
        String basePath = "";
        if (os.toLowerCase().startsWith("win")) {
            basePath = winPath;
        } else {
            basePath = linuxPath;
        }
        basePath = basePath.replace("/", seperator);
        return basePath;
    }

    public static String getShopImgBasePath(Long shopId) {
        String imagePath = shopPath + shopId + "/";
        return imagePath.replace("/", seperator);
    }

}
