package io.github.novel_reptile.utils;

import us.codecraft.webmagic.Site;

/**
 * @Author 翁丞健
 * @Date 2022/5/26 23:22
 * @Version 1.0.0
 */
public class SiteUtil {

    public static Site getDefaultSite(){
        return Site.me().setSleepTime(100);
    }
}
