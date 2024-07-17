package com.fish.birdProducted.utils;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/8 14:2
 * 浏览器信息工具类
 */
public class BrowserUtil {

    /**
     * 获取浏览器名称及版本
     * @param request request
     * @return 名称 - 版本号
     * eg：   User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36
     */
    public static String browserName(HttpServletRequest request){
        String userAgent = request.getHeader("User-Agent");
        UserAgent ua = UserAgent.parseUserAgentString(userAgent);
        Browser browser = ua.getBrowser();
        return browser.getName() + "-" + browser.getVersion(userAgent);
    }

    /**
     * 获取操作系统名称
     * @param request request
     * @return 名称
     */
    public static String osName(HttpServletRequest request){
        /*String userAgent = request.getHeader("User-Agent");
        UserAgent ua = UserAgent.parseUserAgentString(userAgent);
        OperatingSystem os = ua.getOperatingSystem();*/
        String osName = System.getProperty("os.name");

        return osName;
    }

}
