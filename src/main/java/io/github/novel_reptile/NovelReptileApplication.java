package io.github.novel_reptile;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author 翁丞健
 * @Date 2022/5/26 20:56
 * @Version 1.0.0
 */
@SpringBootApplication
@EnableScheduling
@MapperScan("io.github.novel_reptile.mapper")
public class NovelReptileApplication {
    public static void main(String[] args) {
        SpringApplication.run(NovelReptileApplication.class,args);
    }
}
