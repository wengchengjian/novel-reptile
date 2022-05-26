package io.github.novel_reptile.utils;

/**
 * @Author 翁丞健
 * @Date 2022/5/26 22:57
 * @Version 1.0.0
 */
public class NumberUtil {

    public static Long parseSpec(String str){
        String replace = str.replace("_", "");

        return Long.parseLong(replace);
    }
}
