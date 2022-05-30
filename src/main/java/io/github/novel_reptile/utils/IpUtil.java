package io.github.novel_reptile.utils;

import cn.hutool.core.net.Ipv4Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.tomcat.util.net.IPv6Utils;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.utils.IPUtils;

import java.io.IOException;

/**
 * @Author 翁丞健
 * @Date 2022/5/29 18:58
 * @Version 1.0.0
 */
@Slf4j
public class IpUtil {

    private static final String  USER_AGENT =  "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.88 Safari/537.36";

    private static final String DEFAULT_REQUEST_URL = "http://www.baidu.com";
    /**
     * 代理IP有效检测
     *
     * @param proxyIp
     * @param proxyPort
     */
    public static boolean checkProxyIp(String proxyIp, int proxyPort) {

        if(!isCorrectIp(proxyIp)){
            log.info("ip format is invalid");
            return false;
        }

        try(CloseableHttpClient client = HttpClientBuilder.create().setProxy(new HttpHost(proxyIp,proxyPort)).build();){
            HttpGet httpGet = new HttpGet(DEFAULT_REQUEST_URL);
            httpGet.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
            httpGet.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
            httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate");
            httpGet.setHeader("User-Agent", USER_AGENT);
            RequestConfig config = RequestConfig.custom().setConnectTimeout(500).setCookieSpec(CookieSpecs.STANDARD).setSocketTimeout(500).build();
            httpGet.setConfig(config);

            HttpResponse response = client.execute(httpGet);
            int statuCode = response.getStatusLine().getStatusCode();

            if(statuCode == 200){
                log.info("{}:{} is valid",proxyIp,proxyPort);
                return true;
            }
            else{
                log.info("{}:{} is invalid",proxyIp,proxyPort);
                return false;
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.info("{}:{} is invalid",proxyIp,proxyPort);
            return false;
        }
    }

    public static boolean checkProxyIp(Proxy proxy){
        if(proxy==null){
            return false;
        }
        return checkProxyIp(proxy.getHost(),proxy.getPort());
    }

    private static final String IP_REGEX = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";

    /**
     * 函数 判断字符是否是IP
     *
     * @param ipString
     * @return
     */
    public static boolean isCorrectIp(String ipString) {

        if (StringUtils.isBlank(ipString)) {
            return false;
        }
        // 1、判断是否是7-15位之间（0.0.0.0-255.255.255.255.255）
        if (ipString.length() < 7 || ipString.length() > 15) {
            return false;
        }
        // 2、判断是否能以小数点分成四段
        String[] ipArray = ipString.split("\\.");
        if (ipArray.length != 4) {
            return false;
        }
        for (String s : ipArray) {
            // 3、判断每段是否都是数字
            try {
                int number = Integer.parseInt(s);
                // 4.判断每段数字是否都在0-255之间
                if (number < 0 || number > 255) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    /**
     * 正则表达式 判断字符是否为IP
     *
     * @param ipString
     * @return
     */
    public static boolean isCorrectIpRegular(String ipString) {

        if (StringUtils.isBlank(ipString)) {
            return false;
        }
        // 判断每段数字是否都位于0-255之间
        if (!ipString.matches(IP_REGEX)) {
            return false;
        }

        String[] ipArray = ipString.split("\\.");
        for (String s : ipArray) {
            int number = Integer.parseInt(s);
            // 判断每段数字是否都在0-255之间
            if (number < 0 || number > 255) {
                return false;
            }
        }
        return true;
    }
}
