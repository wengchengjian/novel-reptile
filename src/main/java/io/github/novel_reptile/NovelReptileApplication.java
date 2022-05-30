package io.github.novel_reptile;

import io.github.novel_reptile.config.NovelProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author 翁丞健
 * @Date 2022/5/26 20:56
 * @Version 1.0.0
 */
@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(NovelProperties.class)
@MapperScan("io.github.novel_reptile.mapper")
public class NovelReptileApplication {
    public static void main(String[] args) {
        SpringApplication.run(NovelReptileApplication.class,args);
    }
}
