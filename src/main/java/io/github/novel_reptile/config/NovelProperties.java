package io.github.novel_reptile.config;

import lombok.Data;
import org.apache.http.protocol.HTTP;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.AuthProvider;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 翁丞健
 * @Date 2022/5/30 19:56
 * @Version 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "novel")
public class NovelProperties {

    private Reptile reptile;

    @Data
    public  static class Reptile {

        /**
         * 是否开启爬虫
         */
        private boolean enabled = true;

        /**
         * 爬虫网站集合
         */
        private List<NovelSite> novelSites = new ArrayList<>();

        /**
         * 爬虫代理的配置
         */
        private ProxyConfig proxyConfig = new ProxyConfig();

        @Data
        public static class NovelSite {
            /**
             * 网站名
             */
            private String siteName;

            /**
             * 是否开启该网站的爬取
             */
            private boolean enabled;


        }

        @Data
        public static class ProxyConfig{
            /**
             * 是否开启获取代理的配置,默认关闭
             */
            private boolean enabled = false;


            /**
             * 是否是从远程获取的代理ip,默认从本地获取代理
             */
            private boolean remote = false;

            /**
             * 获取代理的方式
             */
            private ProxyConfgiEnum type;

            /**
             * 远程获取代理的地址
             */
            private String address;

            /**
             * 需要认证吗
             */
            private boolean needAuth;

            /**
             * 认证逻辑的处理
             */
            private Authentication authentication;

            /**
             * 获取代理的超时时间 默认5秒，在获取代理时用时超过了5秒将自动退出代理模式
             */
            private Integer timeout = 5000;

            /**
             * 自动恢复代理
             */
            private boolean autoRecovery = true;


            /**
             * 后续认证逻辑的扩展，暂时无认证实现
             */
            @Data
            public static class Authentication{

            }

            /**
             * 获取代理的方式
             */
            public enum ProxyConfgiEnum{
                HTTP,
            }
        }
    }


}
