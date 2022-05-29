package io.github.novel_reptile.utils;

import us.codecraft.webmagic.Site;

/**
 * @Author 翁丞健
 * @Date 2022/5/26 23:22
 * @Version 1.0.0
 */
public class SiteUtil {

    private static final Integer SLEEP_TIME  = 500;

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String  USER_AGENT =  "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.88 Safari/537.36";

    public static Site getDefaultSite(){
        return Site.me().setSleepTime(SLEEP_TIME).setCharset(DEFAULT_CHARSET).setUserAgent(USER_AGENT);
    }
}
